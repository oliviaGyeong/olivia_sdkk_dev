package device.sdk.ifmmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.LinkedHashMap;

/*
 *  KCP Sample 앱에 연동될 API 가 있는 Class
 *
 */


public class IFMManager implements IFMControllerInterface.OnIFMControllerInterface {
    private final String TAG = IFMManager.class.getSimpleName();

    //load ICReader_packet.c +++
    static
    {
        try {
            System.loadLibrary("pkt_IFM");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private native int setTestData (byte[] pIfmPktBuf);

    private Context mContext;
    private ICReaderPacketManager mICReaderPacketManager;
    private RdiManager mRdiManager;
    private IFMControllerInterface.OnRecvCallback mRecvCallback;

    //KCP API +++
    Handler     mAppHandler;
    //KCP API ---

    /*
     * 공통 req_IFM Manager
     */
   public IFMManager() {

    }

    LinkedHashMap<String, String>   mInputMap = null;
    
    /**<
     * @param _mapVanResp   : VAN response data
     * @param trdType       : request command
     * @param timeOut       : IFM response waiting time
     */
    public void Req_IFMex(LinkedHashMap<String, String> _mapVanResp, byte trdType, int timeOut)
    {
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "Req_IFMex() +++  trdType : " + trdType);
        
        mInputMap = _mapVanResp;

        /**< _mapFromVAN 에서 IFM 에 넘겨줘야하는 데이터는 Byte 로 변경후
          *  mICReaderPacketManager.makeICReaderPacket 자리에 넣어줘야함. */
        byte[]  rdPktBuf=mICReaderPacketManager.makeICReaderPacket(_mapVanResp/*VAN resp data*/, trdType);
        
        if (Utils.DEBUG_VALUE)
            Utils.debugTx("", rdPktBuf, rdPktBuf.length);
        
        sendDataToMSR(rdPktBuf, rdPktBuf.length, timeOut);
    }

    //TODO: IC Reader로부터 받은 데이터를 처리하는 부분.
    @SuppressLint("HandlerLeak")
    private Handler mMSRHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (Utils.DEBUG_INFO)
                Log.i(TAG, "mMSRHandler - handleMessage() +++ ");
            
            int     failMessage = MSG_RECEIVE_IC_READER_DATA_FAIL;

            switch (msg.what)
            {
                case Utils.MSG_RECEIVE_IC_READER_DATA_OK:
                    byte[]                          recvRdPktBuf = (byte[]) msg.obj;
                    int                             trdtype = Utils.checkTrdType(recvRdPktBuf);
                    String                          recvStr = new String(recvRdPktBuf);
                    LinkedHashMap<String, String>   _outMap = new LinkedHashMap<>();

                    
                    if(recvRdPktBuf == null) {
                        Log.d(TAG, ">> MSG_RECEIVE_IC_READER_DATA_OK :: but packet buffer is null...");
                        break;
                    }
                    
                    if (Utils.DEBUG_VALUE)
                        Log.d(TAG, " << MSG_RECEIVE_IC_READER_DATA_OK >>");

                    
                    String      resultCode = CallbackDataFormat.getRespCode(recvRdPktBuf);
                    
                    Utils.showToastMessage(mContext, "DATA SUCCESS", "응답코드 : " + resultCode);

                    // 반환작업을 할곳 callbackDataFormat
                    CallbackDataFormat  callbackDataFormat = new CallbackDataFormat();
                    _outMap = callbackDataFormat.recvDataParsingMap(recvRdPktBuf);

                    sendCallback(MSG_RECEIVE_IC_READER_DATA_OK ,recvRdPktBuf, _outMap);
                    //KCP Return API
                    //setHandleMassege(recvRdPktBuf, msg.what);
                    break;

                case Utils.MSG_RECEIVE_IC_READER_DATA_FAIL:
                    if (Utils.DEBUG_VALUE)
                        Log.d(TAG, " << MSG_RECEIVE_IC_READER_DATA_FAIL >>");

                    sendCallback(MSG_RECEIVE_IC_READER_DATA_FAIL,null, null);
                    break;

                case Utils.MSG_RECEIVE_IC_READER_DATA_TIMEOUT:
                    if (Utils.DEBUG_VALUE)
                        Log.d(TAG, " << MSG_RECEIVE_IC_READER_DATA_TIMEOUT >>");
                    
                    sendCallback(MSG_RECEIVE_IC_READER_DATA_TIMEOUT,null, null);
                    break;
            }
            
            if (Utils.DEBUG_INFO)
                Log.i(TAG, "mMSRHandler - handleMessage() --- ");
        
        }
    };

    private void sendCallback(int callbackMsg, byte[] inputByte, LinkedHashMap<String, String> inputMap)
    {
        if (Utils.DEBUG_INFO)
            Log.i(TAG,"sendCallback() +++");
    
        mRecvCallback.onCallback(callbackMsg,inputByte, inputMap);
        
        //KCP Return API
//        setHandleMassege(null, msg.what);
        if (Utils.DEBUG_INFO)
            Log.i(TAG,"sendCallback() ---");
        
        return;
    }

    /********************  Rdi Manager +++ **************************************/
    //TODO :  check open Flag   == mIsRdiOpened
    private void onRdi()
    {
        if (Utils.DEBUG_INFO) {
            Log.i(TAG, "onRdi() +++");
        }
        
        if (mRdiManager == null) {
            mRdiManager = new RdiManager(mContext);
        }

        if (mRdiManager != null)
        {
            mRdiManager.setEnable(mRdiManager.DISABLE);
            mRdiManager.powerDown();
            mRdiManager.powerOn();
        }
        
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "onRdi()  ---");
        
        return;
    }

    private void offRdi()
    {
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "offRdi() +++");
        
        if (mRdiManager != null)
        {
            if (mRdiManager.isEnabled() == mRdiManager.ENABLE) {
                mRdiManager.setEnable(mRdiManager.DISABLE);
            }
            
            mRdiManager.powerDown();
        }
//        else
//        {
//            ;
//        }
        
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "offRdi() ---");

        return;
    }

    private void setupRdi()
    {
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "setupRdi() +++");
        
        if (mRdiManager == null) {
            onRdi();
        }

        if (mRdiManager != null) {
            if (mRdiManager.isEnabled() != mRdiManager.ENABLE) {
                mRdiManager.setEnable(mRdiManager.ENABLE);
            }
        }
        
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "setupRdi() ---");
        
        return;
    }

    private void disableRdi()
    {
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "disableRdi() +++");
        if (mRdiManager != null)
        {
            if (mRdiManager.isEnabled() != mRdiManager.DISABLE) {
                mRdiManager.setEnable(mRdiManager.DISABLE);
            }
        }
        else
        {
            if(Utils.DEBUG_VALUE)
                Log.v(TAG, "disableRdi _mRdiManager == null");
        }
    }

    private void sendDataToMSR(byte[] rdPkt, int size, int timeout)
    {
        setupRdi();
        mRdiManager.clear();
        
        int     result = mRdiManager.write(rdPkt, size);
        
        Log.e(TAG, "write result  :: " + result);

        if (mRdiManager != null && mRdiManager.isEnabled() == 1) {
            mRdiManager.readData(mMSRHandler, timeout);
        }
    }

    /********************  Rdi Manager --- **************************************/


    /*
     * 공통 req_IFM Manager
     */
    /**< 생성자 :: 전역변수를 초기화 한다. */
    @Override
    public void initController(Context context, IFMControllerInterface.OnRecvCallback recvCallback)
    {
        mContext = context;
        mICReaderPacketManager = new ICReaderPacketManager(context);
        mRdiManager = new RdiManager(context);
        mRecvCallback = recvCallback;
    }

    @Override
    public void powerOnRdi() {
        onRdi();
    }

    @Override
    public void powerDownRdi() {
        offRdi();
    }

    @Override
    public boolean isOpendRdi() {
        return  mRdiManager.mIsRdiOpened;
    }


//    public static final byte TRD_TYPE_REQ_INIT  = (byte)0xA0;                   //초기화 요청
//    public static final byte TRD_TYPE_REQ_SELF_PROTECTION = (byte)0xA1;         //무결성 검증 요청
//    public static final byte TRD_TYPE_REQ_CURRENT_TIME = (byte)0xA2;            // 시간전송 요청
//    public static final byte TRD_TYPE_REQ_MUTUAL_AUTHENTICATION = (byte)0xA3;   //상호인증 요청
//    public static final byte TRD_TYPE_REQ_ENCRRYPTION_STATE = (byte)0xA4;       // 암호화키 D/N 상태요청
//    public static final byte TRD_TYPE_REQ_ENCRRYPTION_COMPLETE = (byte)0xA5;    // 암호화키 D/N 완료요청
//    public static final byte TRD_TYPE_REQ_GENERAL_TRANSACTION = (byte)0xA6;     // 일반거래요청
//    public static final byte TRD_TYPE_REQ_IC_CARD_READ = (byte)0xA7;            // IC 카드읽기 요청
//    public static final byte TRD_TYPE_REQ_IC_CARD_DATA = (byte)0xA8;            // IC 카드 데이터 요청
//    public static final byte TRD_TYPE_REQ_IC_CARD_ISSUER = (byte)0xA9;          // IC 카드 발급사 요청
//    public static final byte TRD_TYPE_REQ_CASH_IC_READ = (byte)0x1A;            // 현금IC 읽기 요청
//    public static final byte TRD_TYPE_REQ_CASH_IC_DATA = (byte)0x2A;            // 현금IC 데이터 요청
//    public static final byte TRD_TYPE_REQ_CASH_IC_ACCOUNT_INFO = (byte)0x3A;    // 현금IC 계좌정보 요청
//    public static final byte TRD_TYPE_REQ_IS_INPUT_READER = (byte)0x4A;         // 리더기 카드 삽입여부 요청



    @Override
    public void reqIFM(LinkedHashMap<String, String> inMap, byte trdType)
    {
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "Req_IFM() +++  trdType : " + trdType);
        
        mInputMap = inMap;
        //_inMap에서 리더기에 넘겨줘야하는 필요한데이터는 Byte로 변경후 mICReaderPacketManager.makeICReaderPacket 자리에 넣어줘야함.
//        byte trdTypeBuf = getTryTypeBuf(trdType);

        byte[]  rdPktBuf = mICReaderPacketManager.makeICReaderPacket(inMap, trdType);
        int     rcvTimeout = Utils.TIMEOUT_3SEC;
        
        if (Utils.DEBUG_VALUE)
            Utils.debugTx("", rdPktBuf, rdPktBuf.length);

        
        switch(trdType)
        {
            case Utils.TRD_TYPE_REQ_ENCRRYPTION_STATE:
                rcvTimeout = Utils.TIMEOUT_7SEC;
                break;
            case Utils.TRD_TYPE_REQ_GENERAL_TRANSACTION:
                rcvTimeout = Utils.TIMEOUT_3MIN;
                break;
            case Utils.TRD_TYPE_REQ_IC_CARD_READ:
                rcvTimeout = Utils.TIMEOUT_3MIN;
                break;

            default:
                break;
        }
        
        sendDataToMSR(rdPktBuf, rdPktBuf.length, rcvTimeout);
    }

}