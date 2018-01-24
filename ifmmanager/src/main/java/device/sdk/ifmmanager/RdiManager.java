package device.sdk.ifmmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Date;

import device.sdk.MsrManager;

/**
 * @hide  RDI Manager [on/off & write/read]
 */
public class RdiManager extends MsrManager
{

    private final String    TAG=RdiManager.class.getSimpleName();

    static
    {
        try {
            System.loadLibrary("pkt_IFM");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private native int checkReceivePacketFromIFM(byte[] vanPktBuf, byte[] rdPktBuf, int vanBufLen, int rdBufLen);
/*
    private native int setTestData (byte[] pIfmPktBuf);
*/

    private static Context mContext;
/*
    private static MsrManager mMsr;
*/

    public static int ENABLE = 1;
    public static int DISABLE = 0;

    private int TIMEOUT_3MIN = Utils.TIMEOUT_3MIN; //30000;
    private int TIMEOUT_3SEC = Utils.TIMEOUT_3SEC; //3000;
    private int TIMEOUT = TIMEOUT_3SEC;

/*
    private int MSG_RECEIVE_IC_READER_DATA_OK         = Utils.MSG_RECEIVE_IC_READER_DATA_OK;      //51;
    private int MSG_RECEIVE_IC_READER_DATA_FAIL       = Utils.MSG_RECEIVE_IC_READER_DATA_FAIL;    //52;
    private int MSG_RECEIVE_IC_READER_DATA_TIMEOUT    = Utils.MSG_RECEIVE_IC_READER_DATA_TIMEOUT; //53;
*/

    public boolean mIsRdiOpened = false;
    boolean         threadCheckFlag=true;

    public RdiManager(Context context)
    {
/*
        mMsr = new MsrManager();
*/
        mContext = context;
    }

    public boolean powerOn()
    {

        if (!mIsRdiOpened)
        {
            int     opend=rdiOpen();
            
            if (opend == 0)
            {
                mIsRdiOpened = true;
            }
            else if(opend == -1)
            {
                if(Utils.DEBUG_VALUE)
                    Log.e(TAG,"rdiOpen : false !!!!!!!!!!!!!!!!! ");
                mIsRdiOpened = false;
            }
        }
        
        return mIsRdiOpened;
    }

    public void powerDown()
    {
        threadCheckFlag = false;
        if (rdiClose() == 0)
            mIsRdiOpened = false;
    }

    // enable 1, disable 0.
    public int setEnable(int enable)
    {

        return rdiSetEnable(enable);
    }

    public int isEnabled()
    {
        return rdiIsEnabled();
    }

    public int read(byte[] data ,int length)
    {
        return rdiRead(data,length);
    }

    public int numOfElementsInBuf()
    {
        return rdiNelem();
    }

    public int clear()
    {
        return rdiClear();
    }

    public int write(byte[] data, int length)
    {
        byte[]      padingData = setPaddingData(data);

        if (Utils.DEBUG_VALUE)
            Utils.debugTx(" Write_To_Rdi_Packet", padingData, padingData.length);
        
        return rdiWrite(padingData, padingData.length);
    }

    public byte[] setPaddingData(byte[] data)
    {
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "setPaddingData() +++");
//      if (Utils.DEBUG_VALUE) Utils.debugTx("before setPaddingData", data, data.length);
        
        int     length = data.length;
        
        if ((length % 8) == 7)
            length++;
        
        length += (8 - (length % 8)) + 11; //pipo포함 전체 길이
        
        if (Utils.DEBUG_VALUE)
            Log.i(TAG, "length :" + length);

        byte[]      returnData=new byte[length];
//      returnData = data;

        for (int i = 0; i < length; i++)
        {
            if (i >= data.length)
                returnData[i] = (byte) 0xFF;
            else
                returnData[i] = data[i];
        }
        
//      if (Utils.DEBUG_VALUE)
//          Utils.debugTx(" Write_To_Rdi_Packet", returnData, returnData.length);
        
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "setPaddingData() ---");

        return returnData;
    }

    //Olivia MSR readThread()
    private Handler mMSRHandler = null;
    
    public void readData(Handler msrHandler,int timeout)
    {
        mMSRHandler = msrHandler;

        new Thread(new ReadThread(timeout)).start();
        
        return;
    }


    private class ReadThread implements Runnable
    {
        private String  TAG="ReadThread";
        int             mTimeout=0;
//        boolean         threadCheckFlag=true;

        /**< 생성자 초기화 : timeout의 초기값을 설정한다. */
        ReadThread( int timeout) {
            mTimeout = timeout;
        }


        @Override
        public void run()
        {
            if (Utils.DEBUG_INFO)
                Log.i(TAG, "ReadThread() - run() +++");

            threadCheckFlag = true;
            //Read
            long        timeout=mTimeout;
            long        stime=new Date().getTime();
            long        rtime=0L;
            int         rvalues=0;
            int         sizeOfDataRead=0;
            int         sizeOfRecvPacket=0;
            boolean     isRightData = false;
            byte[]      resMSRBuf=new byte[Utils.MAX_ICREADER_PACKET_SIZE]; //756
            byte[]      rdPktBuf=new byte[Utils.MAX_ICREADER_PACKET_SIZE];

            while (rtime < timeout && threadCheckFlag && !Thread.interrupted())
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    threadCheckFlag = false;
                }
                
                rtime = System.currentTimeMillis() - stime;
                
                if(Utils.DEBUG_VALUE)
                    Log.v(TAG,"rtime "+rtime);

                sizeOfDataRead = read(resMSRBuf, resMSRBuf.length);

                if (sizeOfDataRead == -1)
                {
                    //TODO: 재실행 여부..
//                    if (reConnect()) {
//                    } else {
//                        setHandleMassege(Utils.MSG_VANHOST_CONNECTION_ERROR, null); //TODO: chang FAIL MSG
//                        threadCheckFlag = false;
//                    }
                    setHandleMassege(null, Utils.MSG_RECEIVE_IC_READER_DATA_FAIL);
                    threadCheckFlag = false;
                }
                
                rvalues = rvalues + sizeOfDataRead;

                if(rtime > timeout)
                { //Timeout
                    setHandleMassege(null, Utils.MSG_RECEIVE_IC_READER_DATA_TIMEOUT);
                    threadCheckFlag = false;
                }


                if(rvalues >= 6 )
                {
                    threadCheckFlag = false;
                    break;
                }
            }

            if (sizeOfDataRead > 0)
            {
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "sizeOfDataRead= " + sizeOfDataRead);

                /*******checkData C file  - checkReceivePacketFromIFM() +++*******/
                sizeOfRecvPacket = checkReceivePacketFromIFM(rdPktBuf, resMSRBuf, rdPktBuf.length, sizeOfDataRead);

                if (sizeOfRecvPacket > 0)
                {
                    if (Utils.DEBUG_VALUE)
                        Utils.debugTx("rdPktBuf::", rdPktBuf, sizeOfRecvPacket); //TODO: Size ArrayIndexOutOfBoundsException ERROR
                    
                    byte[] recvData = new byte[sizeOfRecvPacket - 1];
                    
                    System.arraycopy(rdPktBuf, 1, recvData, 0, sizeOfRecvPacket - 1);//checkData -1 ( FF|FE )

                    //Data check FF == ok, FE = Fail
                    if (rdPktBuf[0] == (byte) 0xff)
                    { //is Data true
                        setHandleMassege(recvData, Utils.MSG_RECEIVE_IC_READER_DATA_OK);
                    }
                    else if (rdPktBuf[0] == (byte) 0xfe)
                    { //not Data
                        setHandleMassege(recvData, Utils.MSG_RECEIVE_IC_READER_DATA_FAIL);
                    }
                    else
                    { //ERROR
                        setHandleMassege(null, Utils.MSG_RECEIVE_IC_READER_DATA_FAIL);
                    }
                }
                else
                { //Fail Packet Data
                    setHandleMassege(null, Utils.MSG_RECEIVE_IC_READER_DATA_FAIL);
                }
            }

            if (Utils.DEBUG_INFO)
                Log.i(TAG, "ResponseThread() - run() ---");
        }


        public void setHandleMassege(byte[] recvData, int state)
        {
            if (Utils.DEBUG_INFO)
                Log.i(TAG,"setHandleMassege() +++");

            if(mMSRHandler == null)
            {
                if (Utils.DEBUG_INFO) Log.i(TAG,"mMSRHandler == null");
                return;
            }

            Message     obtainMsg=mMSRHandler.obtainMessage();
            
            obtainMsg.what = state;
            
            if(state == Utils.MSG_RECEIVE_IC_READER_DATA_OK )
                obtainMsg.obj = recvData;
            else if(state == Utils.MSG_RECEIVE_IC_READER_DATA_FAIL)
                ;
            else if(state == Utils.MSG_RECEIVE_IC_READER_DATA_TIMEOUT)
                ;

            mMSRHandler.sendMessage(obtainMsg);
            mMSRHandler = null;

            if (Utils.DEBUG_INFO)
                Log.i(TAG,"setHandleMassege() ---");
        }
    }
}

