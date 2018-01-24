
////////////////////////////////////////////////////////////////////////////////////////////////////
// CallbackDataFormat.java

package device.sdk.ifmmanager;  // ref. libs\device.sdk.jar
import android.app.RemoteInput;
import android.util.Log;
import java.util.LinkedHashMap;

public class CallbackDataFormat {
    
    /**< get the current class file name :: Log message-에 사용 */
    private static final String TAG = CallbackDataFormat.class.getSimpleName();

    byte[] FS = new byte[]{Utils.CMD_FS};

    //공통헤더 리더기 데이타
    public class COMMON{
        public static final String KEY_TRD_TYPE         = "trd_type";
        public static final String KEY_RES_CODE         = "res_code";
        public static final String KEY_READER_SN        = "reader_sn";        //"1000000000");
        public static final String KEY_READER_VER_DATE  = "reader_ver_date";
        public static final String KEY_READER_VER       = "reader_ver";      //"C101");
        public static final String KEY_READER_CERTI     = "reader_cert";   //"#DUALPAY633C");

        public static final int LEN_TRD_TYPE            = 1;
        public static final int LEN_RES_CODE            = 2;
        public static final int LEN_READER_SN           = 10;
        public static final int LEN_READER_VER_DATE     = 6;
        public static final int LEN_READER_VER          = 4;
        public static final int LEN_READER_CERTI        = 12;
        public static final int LEN_COMMON              = 38;
    }

    public class MapData {
        //    From App Map Data
        public static final String current_date         = "current_date";
        public static final String random               = "random";
        public static final String hash                 = "hash";
        public static final String sign_value           = "sign_value";
        public static final String enc_key              = "enc_key";
        public static final String tr_code              = "tr_code";
        public static final String term_id              = "term_id";
        public static final String tot_amt              = "tot_amt";
        public static final String card_aid_num         = "card_aid_num";
        public static final String emv_data             = "emv_data";
        public static final String cash_type            = "cash_type";
        public static final String acc_index            = "acc_index";
        public static final String rand_num             = "rand_num";
        public static final String encrpt_pwd           = "encrpt_pwd";
        public static final String current_date_rn      = "current_date_rn";

//      To App Callback Data
//      public static final String current_date         = "curren_date";
        public static final String integt_verification  = "integt_verification";
        public static final String public_key_ver       = "public_key_ver";
        public static final String ksn                  = "ksn";
        public static final String input_enc            = "input_enc";
        public static final String ms_data              = "ms_data";
        public static final String enc_track2_data      = "enc_track2_data";
        public static final String ic_tr_type           = "ic_tr_type";
//      public static final String tr_code              = "tr_code";
        public static final String tr_type              = "tr_type";
        public static final String tr_date              = "tr_date";
        public static final String tr_sc_num            = "tr_sc_num";
        public static final String fb_code              = "fb_code";
        public static final String card_type            = "card_type";
        public static final String issuer_country_code  = "issuer_country_code";
        public static final String emv_key_dt           = "emv_key_dt";
        public static final String emv_data_dt          = "emv_data_dt";
        public static final String term_vr              = "term_vr";
        public static final String app_crypt            = "app_crypt";
        public static final String issuer_sr            = "issuer_sr";
        public static final String ic_input_card        ="ic_input_card";

        public static final int LEN_current_date        = 14;
        public static final int LEN_public_key_ver      = 2;
        public static final int LEN_ic_tr_type          = 1;
        public static final int LEN_tr_type             = 1;
        public static final int LEN_fb_code             = 2;
        public static final int LEN_card_type           = 2;
        public static final int LEN_issuer_country_code = 4;

//    From App Map Data
//    (byte)0xA0 초기화 요청
//    (byte)0xA1 무결성 검증 요청
//    (byte)0xA2 시간전송 요청                current_date
//    (byte)0xA3 상호인증 요청
//    (byte)0xA4 암호화키D/N 상태요청	        random hash  sign_value
//    (byte)0xA5 암호화키D/N 완료요청	        enc_key
//    (byte)0xA6 일반거래요청	    	        tr_code tr_type tr_date tr_sc_num
//    (byte)0xA7 IC 카드읽기 요청		        tr_code term_id tot_amt current_date
//    (byte)0xA8 IC 카드 데이터 요청		    tr_code term_id tot_amt current_date  card_aid_num
//    (byte)0xA9 IC 카드 발급사 요청		    emv_data
//    (byte)0x1A 현금IC 읽기 요청		        term_id current_date
//    (byte)0x2A 현금IC 데이터 요청		    term_id current_date cash_type acc_index rand_num encrypt_pwd
//    (byte)0x3A 현금IC 계좌정보 요청         term_id current_date_rn encrypt_pwd
//    (byte)0x4A 리더기 카드 삽입 여부 요청    term_id current_date

//    To App Callback Data
//    초기화 응답	(byte)0xA0	** 공통
//    무결성 검증 응답(byte)0xA1	            integt_verifi
//    시간 전송 응답(byte)0xA2	            current_date
//    상호 인증 응답(byte)0xA3	            public_key_ver
//    암호화 키 D/N 상태 응답(byte)0xA4	    public_key_ver ksn input_enc
//    암호화 키 D/N 완료 응답(byte)0xA5	** 공통
//    일반 거래 응답(byte)0xA6	            current_date ms_data ksn enc_track2_data
//    IC 카드 읽기 응답(byte)0xA7
//    IC 카드 데이터 승인응답(byte)0xA8	    ic_tr_type tr_type fb_code current_date card_type issuer_country_code ms_data ksn enc_track2_data
//                                          emv_data emv_key_dt emv_data_dt
//    IC 카드 발급사 응답(byte)0xA9	        term_vr app_crypt issuer_sr
//    현금IC 읽기 응답(byte)0x1A
//    현금IC 데이터 응답(byte)0x2A
//    현금IC 계좌정보 응답(byte)0x3A
//    리더기 카드 삽입 여부 요청(byte)0x4A     ic_input_card
    }

    /**< get Response Code from IFM response */
    public static String getRespCode(byte[] data)
    {
        if (Utils.DEBUG_INFO)
            Log.i(TAG, "getRespCode() +-");
        
        byte[]  resultBuf=new byte[COMMON.LEN_RES_CODE];
        int     strPos=ICReaderPacketManager.RdiFormat.STX +
                       ICReaderPacketManager.RdiFormat.DATA_LENGTH +
                       ICReaderPacketManager.RdiFormat.COMMANDID;
        
        System.arraycopy(data, strPos , resultBuf, 0, COMMON.LEN_RES_CODE); //resultCode
        
        return new String(resultBuf);   // return the Response Code
    }
    
    
    LinkedHashMap<String, String>       mToCalllbackMap=null;//_inMap;

    public LinkedHashMap<String, String> recvDataParsingMap(byte[] recvRdpkt)
    {
        byte        trdtype=Utils.checkTrdType(recvRdpkt);
        int         headerSize = 0;
        String      resultCode = getRespCode(recvRdpkt);
        String      recvStr = new String (recvRdpkt);
        String[]    recvStrArrayFS = recvStr.split(new String(FS));  // FS  => 0x1C값임.

        Log.v(TAG,"recvStrArrayFS length : "+recvStrArrayFS.length);

        for(int i=0; i< recvStrArrayFS.length; i++){
            Log.v(TAG,"recvStrArrayFS : "+i+": :"+recvStrArrayFS[i]);
            Log.v(TAG,"recvStrArrayFS : "+i+": :"+recvStrArrayFS[i].length());
        }

        mToCalllbackMap = null;
        mToCalllbackMap = new LinkedHashMap<>();
        if(recvRdpkt == null){
            return null;
        }
        //Header Parsing +++
        if(trdtype == Utils.TRD_TYPE_REQ_IC_CARD_DATA ) {   //TODO: ic 카드응답 데이터 header A8
            headerSize = getReaderInfoDataForIC( recvStrArrayFS[0], recvRdpkt); //trdtype ~ issuer_country_code
        }
        else{
            headerSize = getReaderInfoData( recvStrArrayFS[0], recvRdpkt);      //trdtype ~ reader_cert
        }
        
        if (headerSize == 0) {
            return null;
        }
        //Header Parsing ---

        //Data parsing  +++
        switch (trdtype){
            case Utils.TRD_TYPE_REQ_INIT:
            case Utils.TRD_TYPE_REQ_SELF_PROTECTION:
            case Utils.TRD_TYPE_REQ_CURRENT_TIME:
            case Utils.TRD_TYPE_REQ_MUTUAL_AUTHENTICATION:
            case Utils.TRD_TYPE_REQ_ENCRRYPTION_COMPLETE:
                break;
                
            case Utils.TRD_TYPE_REQ_ENCRRYPTION_STATE:
//              Header data key >> public_key_ver
//              Data Key >> ksn input_enc
                mToCalllbackMap.put(MapData.ksn,recvStrArrayFS[1]);
                mToCalllbackMap.put(MapData.input_enc,recvStrArrayFS[2]);
                break;
                
            case Utils.TRD_TYPE_REQ_GENERAL_TRANSACTION:
//                Header data key >> current_date
//                Data Key >> ms_data ksn enc_track2_data
                if(recvStrArrayFS == null ||recvStrArrayFS.length != 5 ){
                    return null;
                }
                mToCalllbackMap.put(MapData.ms_data, recvStrArrayFS[1]);
                mToCalllbackMap.put(MapData.ksn, recvStrArrayFS[2]);
                mToCalllbackMap.put(MapData.enc_track2_data, recvStrArrayFS[3]);
                break;
            case Utils.TRD_TYPE_REQ_IC_CARD_READ://TODO: 미구현
                break;
                
            case Utils.TRD_TYPE_REQ_IC_CARD_DATA:
//                Header data key >> ic_tr_type tr_type fb_code current_date card_type issuer_country_code
//                Data Key >> ms_data ksn enc_track2_data emv_data emv_key_dt emv_data_dt
                if(recvStrArrayFS == null ||recvStrArrayFS.length != 8 ){
                    return null;
                }
                
                mToCalllbackMap.put(MapData.ms_data,recvStrArrayFS[1]);
                mToCalllbackMap.put(MapData.ksn,recvStrArrayFS[2]);
                mToCalllbackMap.put(MapData.enc_track2_data,recvStrArrayFS[3]);
                mToCalllbackMap.put(MapData.emv_data,recvStrArrayFS[4]);
                mToCalllbackMap.put(MapData.emv_key_dt,recvStrArrayFS[5]);
                mToCalllbackMap.put(MapData.emv_data_dt,recvStrArrayFS[6]);
                break;
                
            case Utils.TRD_TYPE_REQ_IC_CARD_ISSUER:
            case Utils.TRD_TYPE_REQ_CASH_IC_READ:
            case Utils.TRD_TYPE_REQ_CASH_IC_DATA:
            case Utils.TRD_TYPE_REQ_CASH_IC_ACCOUNT_INFO:
                break;

            case Utils.TRD_TYPE_REQ_IS_INPUT_READER:
//                Header data key >> current_date
//                Data Key >> ic_input_card
                if(recvStrArrayFS == null ||recvStrArrayFS.length != 3 ){
                    return null;
                }

                mToCalllbackMap.put(MapData.ic_input_card,recvStrArrayFS[1]);
                break;
            case Utils.TRD_TYPE_EXCEPTION:
                break;
        }
        
        //Data parsing  ---
        return mToCalllbackMap;
    }

    public int setMapField(String strSRC, String mapKey, int fieldLen, int sStart)
    {
        mToCalllbackMap.put(mapKey, strSRC.substring(sStart, sStart + fieldLen));

        return fieldLen;
    }

    //공통 COMMON response +++
    //trd type, res code, Sirial Num, VersionDate , Version ,CertiNum
    public int getReaderInfoData( String recvStrArray,byte[] recvRdpkt)
    {
        int     count = ICReaderPacketManager.RdiFormat.STX+ICReaderPacketManager.RdiFormat.DATA_LENGTH;

        byte trdtype = Utils.checkTrdType(recvRdpkt);
        Log.v(TAG,"trdtype : "+trdtype);
        count += setMapField(recvStrArray, COMMON.KEY_TRD_TYPE, COMMON.LEN_TRD_TYPE, count);
        count += setMapField(recvStrArray, COMMON.KEY_RES_CODE, COMMON.LEN_RES_CODE, count);

        int     spDataLen = recvStrArray.length() - COMMON.LEN_COMMON;
        Log.v(TAG,"spDataLen : "+spDataLen);

        if(spDataLen != 0)
        {
            switch (trdtype)
            {
                case Utils.TRD_TYPE_REQ_SELF_PROTECTION:
                    count += setMapField(recvStrArray, MapData.integt_verification, spDataLen, count);
                    break;

                case Utils.TRD_TYPE_REQ_CURRENT_TIME:
                case Utils.TRD_TYPE_REQ_GENERAL_TRANSACTION:
                case Utils.TRD_TYPE_REQ_IC_CARD_READ:
                    count += setMapField(recvStrArray, MapData.current_date, spDataLen, count);
                    break;
                case Utils.TRD_TYPE_EXCEPTION:
                    return count;
                default:
                    Log.v(TAG, "@ error : unknown trdtype : " + trdtype);
                    break;
            }
        }

        count += setMapField(recvStrArray, COMMON.KEY_READER_SN, COMMON.LEN_READER_SN, count);
        count += setMapField(recvStrArray, COMMON.KEY_READER_VER_DATE, COMMON.LEN_READER_VER_DATE, count);
        count += setMapField(recvStrArray, COMMON.KEY_READER_VER, COMMON.LEN_READER_VER, count);
        count += setMapField(recvStrArray, COMMON.KEY_READER_CERTI, COMMON.LEN_READER_CERTI, count);

        if(trdtype == Utils.TRD_TYPE_REQ_MUTUAL_AUTHENTICATION ||trdtype == Utils.TRD_TYPE_REQ_ENCRRYPTION_STATE) {
            count += setMapField(recvStrArray, MapData.public_key_ver, MapData.LEN_public_key_ver, count);
        }

        return count;
    }

    //Header IC거래 응답  A8 TRD_TYPE_REQ_IC_CARD_DATA
    //trd type, res code, Sirial Num, VersionDate , Version ,CertiNum
    public int getReaderInfoDataForIC( String recvStrArray,byte[] recvRdpkt) {
        int count = ICReaderPacketManager.RdiFormat.STX+ICReaderPacketManager.RdiFormat.DATA_LENGTH;
//        ic_tr_type tr_type fb_code current_date card_type issuer_country_code
        byte trdtype = Utils.checkTrdType(recvRdpkt);
        Log.v(TAG,"trdtype : "+trdtype);
//        mToCalllbackMap.put(COMMON.KEY_TRD_TYPE, recvStrArray.substring(count, count+COMMON.LEN_TRD_TYPE));
//        count += COMMON.LEN_TRD_TYPE;
        mToCalllbackMap.put(COMMON.KEY_TRD_TYPE, getTrdTypeString(trdtype));
        count += COMMON.LEN_TRD_TYPE;
		count += setMapField(recvStrArray, COMMON.KEY_TRD_TYPE, COMMON.LEN_TRD_TYPE, count);
        count += setMapField(recvStrArray, MapData.ic_tr_type, MapData.LEN_ic_tr_type, count);
        count += setMapField(recvStrArray, MapData.tr_type, MapData.LEN_tr_type, count);
        count += setMapField(recvStrArray, COMMON.KEY_RES_CODE, COMMON.LEN_RES_CODE, count);
        count += setMapField(recvStrArray, MapData.fb_code, MapData.LEN_fb_code, count);
        count += setMapField(recvStrArray, COMMON.KEY_READER_SN, COMMON.LEN_READER_SN, count);
        count += setMapField(recvStrArray, COMMON.KEY_READER_VER_DATE, COMMON.LEN_READER_VER_DATE, count);
        count += setMapField(recvStrArray, COMMON.KEY_READER_VER, COMMON.LEN_READER_VER, count);
        count += setMapField(recvStrArray, COMMON.KEY_READER_CERTI, COMMON.LEN_READER_CERTI, count);
        count += setMapField(recvStrArray, MapData.card_type, MapData.LEN_card_type, count);
        count += setMapField(recvStrArray, MapData.issuer_country_code, MapData.LEN_issuer_country_code, count);

        return count;
    }

    public String  getTrdTypeString( byte trdTypeBuf){
        String trdType="";
        Log.e(TAG,"getTrdTypeString ::"+ Utils.BA2AS(new byte[]{trdTypeBuf}));

        switch (trdTypeBuf){
            case Utils.TRD_TYPE_REQ_INIT:
                trdType = "A0";
                break;
            case Utils.TRD_TYPE_REQ_SELF_PROTECTION:
                trdType = "A1";
                break;
            case Utils.TRD_TYPE_REQ_CURRENT_TIME:
                trdType = "A2";
                break;
            case Utils.TRD_TYPE_REQ_MUTUAL_AUTHENTICATION:
                trdType = "A3";
                break;
            case Utils.TRD_TYPE_REQ_ENCRRYPTION_STATE:
                trdType = "A4";
                break;
            case Utils.TRD_TYPE_REQ_ENCRRYPTION_COMPLETE:
                trdType = "A5";
                break;
            case Utils.TRD_TYPE_REQ_GENERAL_TRANSACTION:
                trdType = "A6";
                break;
            case Utils.TRD_TYPE_REQ_IC_CARD_READ:
                trdType = "A7";
                break;
            case Utils.TRD_TYPE_REQ_IC_CARD_DATA:
                trdType = "A8";
                break;
            case Utils.TRD_TYPE_REQ_IC_CARD_ISSUER:
                trdType = "A9";
                break;
            case Utils.TRD_TYPE_REQ_CASH_IC_READ:
                trdType = "1A";
                break;
            case Utils.TRD_TYPE_REQ_CASH_IC_DATA:
                trdType = "2A";
                break;
            case Utils.TRD_TYPE_REQ_CASH_IC_ACCOUNT_INFO:
                trdType = "3A";
                break;
            case Utils.TRD_TYPE_REQ_IS_INPUT_READER:
                trdType = "4A";
                break;
            case Utils.TRD_TYPE_EXCEPTION:
                trdType = "02";
                break;
        }
        return trdType;
    }

    public byte getTryTypeBuf(String trdType){
        byte trdTypeBuf ;

        if(trdType.equals("A0")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_INIT;
        }else if(trdType.equals("A1")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_SELF_PROTECTION;
        }else if(trdType.equals("A2")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_CURRENT_TIME;
        }else if(trdType.equals("A3")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_MUTUAL_AUTHENTICATION;
        }else if(trdType.equals("A4")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_ENCRRYPTION_STATE;
        }else if(trdType.equals("A5")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_ENCRRYPTION_COMPLETE;
        }else if(trdType.equals("A6")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_GENERAL_TRANSACTION;
        }else if(trdType.equals("A7")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_IC_CARD_READ;
        }else if(trdType.equals("A8")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_IC_CARD_DATA;
        }else if(trdType.equals("A9")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_IC_CARD_ISSUER;
        }else if(trdType.equals("1A")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_CASH_IC_READ;
        }else if(trdType.equals("2A")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_CASH_IC_DATA;
        }else if(trdType.equals("3A")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_CASH_IC_ACCOUNT_INFO;
        }else if(trdType.equals("4A")){
            trdTypeBuf = Utils.TRD_TYPE_REQ_IS_INPUT_READER;
        }else if(trdType.equals("02")){
            trdTypeBuf = Utils.TRD_TYPE_EXCEPTION;
        }else{
            trdTypeBuf = Utils.TRD_TYPE_REQ_INIT;
        }
        return trdTypeBuf;
    }


}
