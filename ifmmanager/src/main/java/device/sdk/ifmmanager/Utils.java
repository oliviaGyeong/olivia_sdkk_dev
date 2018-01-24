package device.sdk.ifmmanager;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;

public class Utils {

    public static final boolean DEBUG_TEST_KCPSECU              = true;

    public static final boolean DEBUG_INFO                      = true;
    public static final boolean DEBUG_VALUE                     = true;
    public static final boolean DEBUG_ICREADER                  = true;
    
    public static final int MAX_ICREADER_PACKET_SIZE            = 756;
    
    //olivia 171108 add ICReader readThread handle MSG
    public static final int MSG_RECEIVE_IC_READER_DATA_OK       = 51;
    public static final int MSG_RECEIVE_IC_READER_DATA_FAIL     = 52;
    public static final int MSG_RECEIVE_IC_READER_DATA_TIMEOUT  = 53;
    
    //olivia 171108 add ICReader Timeout
    public static int       TIMEOUT_3MIN = 30000;
    public static int       TIMEOUT_7SEC = 7000;
    public static int       TIMEOUT_5SEC = 5000;
    public static int       TIMEOUT_3SEC = 3000;
    public static int       TIMEOUT_1SEC = 1000;

    public static int TIME_BUF_LEN  = 14;

    public static final String PREF_KEY_IC_COMD_ID = "ic_comd_id"; //TEST >> PREF_KEY_IC_COMMAND_ID

    /*구분코드
        A0~ 4A
    */
    //TEST CODE COMD ID +++
    public static final byte CMD_ID_REQ_DEVICE_INFO = (byte)0x58;               //KOCES 단말정보요청 comd Packet 확인용
    //TEST CODE COMD ID ---

    public static final byte CMD_FS  = (byte) 0x1c;
    public static final byte CMD_STX = (byte) 0x02;   // Start of Text
    public static final byte CMD_ETX = (byte) 0X03;   // End of Text

    public static final byte TRD_TYPE_REQ_INIT                  = (byte)0xA0;   //초기화 요청
    public static final byte TRD_TYPE_REQ_SELF_PROTECTION       = (byte)0xA1;   //무결성 검증 요청
    public static final byte TRD_TYPE_REQ_CURRENT_TIME          = (byte)0xA2;   // 시간전송 요청
    public static final byte TRD_TYPE_REQ_MUTUAL_AUTHENTICATION = (byte)0xA3;   //상호인증 요청
    public static final byte TRD_TYPE_REQ_ENCRRYPTION_STATE     = (byte)0xA4;   // 암호화키 D/N 상태요청
    public static final byte TRD_TYPE_REQ_ENCRRYPTION_COMPLETE  = (byte)0xA5;   // 암호화키 D/N 완료요청
    public static final byte TRD_TYPE_REQ_GENERAL_TRANSACTION   = (byte)0xA6;   // 일반거래요청
    public static final byte TRD_TYPE_REQ_IC_CARD_READ          = (byte)0xA7;   // IC 카드읽기 요청
    public static final byte TRD_TYPE_REQ_IC_CARD_DATA          = (byte)0xA8;   // IC 카드 데이터 요청
    public static final byte TRD_TYPE_REQ_IC_CARD_ISSUER        = (byte)0xA9;   // IC 카드 발급사 요청
    public static final byte TRD_TYPE_REQ_CASH_IC_READ          = (byte)0x1A;   // 현금IC 읽기 요청
    public static final byte TRD_TYPE_REQ_CASH_IC_DATA          = (byte)0x2A;   // 현금IC 데이터 요청
    public static final byte TRD_TYPE_REQ_CASH_IC_ACCOUNT_INFO  = (byte)0x3A;   // 현금IC 계좌정보 요청
    public static final byte TRD_TYPE_REQ_IS_INPUT_READER       = (byte)0x4A;   // 리더기 카드 삽입여부 요청
    public static final byte TRD_TYPE_EXCEPTION                 = (byte)0xAF;   // 예외처리

    public static final byte CMD_ERROR                          = (byte) 0xaf;   // Req, Rec 둘다 같음.   리더기 장애 발생 감지
 
    public static final byte CMD_ID_RECV_ACK = (byte)0x06;
    public static final byte CMD_ID_RECV_NAK = (byte)0x15;
    public static final byte CMD_ID_RECV_ESC = (byte)0x18;
    public static final byte CMD_ID_RECV_EOT = (byte)0x04;

    public static byte checkTrdType(byte[] rdpkt)
    {
        byte    trdTypeBytes =  rdpkt[3];
/*
        byte[] trdTypeBytes = new byte[1] ;
        
        System.arraycopy(rdpkt, 3, trdTypeBytes, 0, 1);
        String trdType = new String(trdTypeBytes);
*/
        return trdTypeBytes;
    }

    public static int checkNextFS(byte[] src, int srcLen, int start)
    {
        int     i=start;
        
        while(srcLen != start)
        {
            if(src[i++] == 0x1C)
                break;
            
            srcLen--;
        }
        
        if(i >= ((i - start) + srcLen)) // not found the FS
            return 0;
        
        return i;
    }
    
    public static String getFieldData(String strTitle, int sStart, int sizData, byte[] rcvPkt, LinkedHashMap<String, String> _outMap)
    {
        String  rcvStr = new String(rcvPkt);
        String strData = rcvStr.substring(sStart, (sStart + sizData));
        
        _outMap.put(strTitle, strData);
        
        return strData;
    }

    public static String getFieldDataEx(String strTitle, splitPacket sP, byte[] rcvPkt, LinkedHashMap<String, String> _outMap)
    {
        String  rcvStr = new String(rcvPkt);
        
        sP.strKey = strTitle;
        sP.sEnd = checkNextFS(rcvPkt, rcvPkt.length, sP.sStart);// FS 다음의 위치값(next field 의 시작위치)이 return 된다.
        sP.strData = rcvStr.substring(sP.sStart, sP.sEnd - 1/*FS를 제외*/);
        sP.sStart = sP.sEnd;  // skip the FS
        
        _outMap.put(sP.strKey, sP.strData);
        
        return sP.strData;
    }
    
    public static void showToastMessage(final Context context, final String tag, final String message)
    {
        Handler handler = new Handler(Looper.getMainLooper());
    
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                Log.e(tag, message);
            }
        });
    }


    public static byte[] convertCurrentDateToByteArray() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date(System.currentTimeMillis()));

        return date.getBytes();
    }


    public static String convertCurrentDateToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date(System.currentTimeMillis()));

        return date;
    }

    public static byte[] RJLZ(byte[] src, int capacity)
    {
        byte[]  dest=new byte[capacity];
        int     destLength=capacity - 1;
        
        Arrays.fill(dest, (byte)0x30);

        for (int i=(src.length - 1); i >= 0; i--)
        {
            dest[destLength--] = src[i];
        }

        return dest;
    }

    // Sha256
    public static byte[] getDigestToBuf(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            byte byteData[] = md.digest();

            return byteData;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    
    // Sha256
    public static String getDigestToString(byte[] bytes) {
        try {
            MessageDigest   md=MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            byte            byteData[]=md.digest();
            StringBuffer    sb=new StringBuffer();
            StringBuffer    hexString=new StringBuffer();
            
            for (int i = 0; i < byteData.length; i++)
            {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            for (int i = 0; i < byteData.length; i++)
            {
                String hex = Integer.toHexString(0xff & byteData[i]);
                
                if (hex.length() == 1)
                {
                    hexString.append('0');
                }
                
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

//    double randVal = Math.random();
    public static byte[] randToByteArray(int count) {
        byte[] randByteArray =  new byte[count*8];
        for (int i = 0; i < count; i++) {
            double randVal = Math.random();
//        int intVal = (int) (randVal*100)+1;
            byte[] converBuf = convertDoubleToByteArray(randVal);
            System.arraycopy(converBuf, 0, randByteArray,i*8 , converBuf.length);
        }
        return randByteArray;
    }


    public static byte[] convertDoubleToByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.putDouble(value);
        return buffer.array();
    }

    /**< convert ByteArray To Ascii String */
    public static String BA2AS(byte[] bytes) {
        String hexString = new BigInteger(bytes).toString(16);
        int hexNum = Integer.parseInt(hexString, 16);
        return Integer.toString(hexNum);
    }

    /**< convert Byte Array To String */
    public static String ba2Str(byte[] srcBuf, int startIndex, int length)
    {
        byte[] destbuf = new byte[length];
        
        System.arraycopy(srcBuf, startIndex, destbuf, 0, destbuf.length);
        String destStr = new String(destbuf);
        return destStr;
    }
    
    /**< convert Integer To Byte Array */
    public static byte[] i2BA(int capacity, final int num)
    {
        ByteBuffer  byteBuf = ByteBuffer.allocate(capacity).putInt(num);
        
        return byteBuf.array();
    }

    //byte to String Encode Korean
    /**< convert Byte Array To Korean Encode String */
    public static String ba2KrEnc(byte[] bytes)
    {
        String str = "";
        
        try {
            str = new String(bytes, "EUC-KR");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return str;
    }

    /**< HEX To Byte Array */
    public static byte[] h2BA(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }
        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    } // byte[] to hex

    /**< byte Array To Hex */
    public static String BA2Hex (byte[] ba)
    {
        if (ba == null || ba.length == 0)
            return null;

        StringBuffer    sb=new StringBuffer(ba.length * 2);
        String          hexNumber;
        
        for (int x=0; x < ba.length; x++)
        {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        
        return sb.toString();
    }


    public static void debugTx(String tag, byte[] dataBuf, int len)
    {
        Log.d(tag, " len " +len);
        
        String      str="";
        
        for (int i = 1; i < len+1; i++)
        {
            str += String.format("%02x", dataBuf[i-1]) + " ";
        
            if((i % 16) == 0)
            {
                Log.d(tag, str );
                str="";
            }
        }
        
        Log.d(tag, str);
    }


    public static void setSharedPreference(Context mContext,String key, String val)
    {
        SharedPreferences   pref=mContext.getSharedPreferences(key, mContext.MODE_PRIVATE);
        
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, val);
        editor.commit();
        
        return;
    }
    
    public static String getSharedPreference(Context mContext,String key)
    {
        SharedPreferences   pref=mContext.getSharedPreferences(key, mContext.MODE_PRIVATE);
        
        return pref.getString(key, null);
    }

    public static void setSharedPreferenceInt(Context mContext, String key, int val)
    {
        SharedPreferences           pref=mContext.getSharedPreferences(key, mContext.MODE_PRIVATE);
        SharedPreferences.Editor    editor=pref.edit();
        
        editor.putInt(key, val);
        editor.commit();
    }
    
    public static int getSharedPreferenceInt(Context mContext, String key)
    {
        SharedPreferences pref = mContext.getSharedPreferences(key, mContext.MODE_PRIVATE);
        return pref.getInt(key,-1);
    }

    // 값(Key Data) 삭제하기
    private static void removeSharedPreferences(Context mContext, String key)
    {
        SharedPreferences pref = mContext.getSharedPreferences(key, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    // 값(ALL Data) 삭제하기
    private static void removeAllSharedPreferences(Context mContext,String key)
    {
        SharedPreferences pref = mContext.getSharedPreferences(key, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static void SimpleDailog(Context mContext, String title, String message)
    {
        AlertDialog.Builder     builder=new AlertDialog.Builder(mContext);
        
        builder.setTitle(title);
        builder.setMessage(message);
        
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        
        builder.show();
    }
}
