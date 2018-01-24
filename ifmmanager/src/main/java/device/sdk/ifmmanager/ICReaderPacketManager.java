package device.sdk.ifmmanager;

import android.content.Context;
import android.util.Log;

import java.util.LinkedHashMap;


/*
 * make packet to IFM
 */
public class ICReaderPacketManager
{
    private final String TAG = ICReaderPacketManager.class.getSimpleName();

    //load ICReader_packet.c +++
    static {
        try {
            System.loadLibrary("pkt_IFM");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //create ICReader_packet.c ---


    //add native method +++
    private native int pm1100DeviceInfo(byte[] rdPktBuf, byte[] dateTime, int rdBufLen);
    private native int reqSetSystemDateTime(byte[] rdPktBuf, byte[] dateTime, int dateBufLen, int rdBufLen);
   //add native method ---

    private Context     mContext;

    public ICReaderPacketManager(Context context) {
        mContext = context;
    }

    public class RdiFormat {
        public static final int STX = 1;
        public static final int DATA_LENGTH = 2;
        public static final int COMMANDID = 1;
        public static final int ETX = 1;
        public static final int LRC = 1;

        public static final byte CMD_FS = (byte) 0x1c;
        public static final byte CMD_STX = (byte) 0x02;   // Start of Text
        public static final byte CMD_ETX = (byte) 0X03;   // End of Text
    }

    //RDI_FORMAT_BASE_PACKET_LENGTH Size  = 6
    private final int   RDI_FORMAT_BASE_PACKET_LENGTH = RdiFormat.STX +RdiFormat.DATA_LENGTH +
                                                        RdiFormat.COMMANDID +
                                                        RdiFormat.ETX + RdiFormat.LRC;

    public static final int     REQ_DATA_LENGTH_IC_TEST_DEVICE_INFO = 14;
    private final int           REQ_PACKET_LENGTH_IC_DEVICE_INFO = RDI_FORMAT_BASE_PACKET_LENGTH +
                                                                   REQ_DATA_LENGTH_IC_TEST_DEVICE_INFO;

    //AdiminActivity Packet Pram +++

    //olivia 171031 상호인증 요청구분 pk Flag
    public static final int FLAG_MA_REQUEST_BREAKDOWN = 1; //요청구분 0001 :최신펌웨어, 0003 : EMV KEY
    public static final byte[] FLAG_MA_REQUEST_BREAKDOWN_FW = new byte[]{0x30, 0x30, 0x30, 0x31};
    public static final byte[] FLAG_MA_REQUEST_BREAKDOWN_EMVKEY = new byte[]{0x30, 0x30, 0x30, 0x33};
    //Adimin Packet Pram ---


    public byte[] makeICReaderPacket (LinkedHashMap<String, String> reqMapData, byte comdId)
    {
        if (Utils.DEBUG_INFO)
        {
            Log.i(TAG, "makeICReaderPacket() +++ ");
            Log.v(TAG, "comdId :: " + comdId);
        }

        Utils.setSharedPreferenceInt(mContext, Utils.PREF_KEY_IC_COMD_ID, comdId);

        byte[]  rdPktBuf = null;
        byte[]  dataBuf =null;
        int     size = 0;
        int     count;
        String dataTimeStr = Utils.convertCurrentDateToString();
        String termId = "1002189855";

        switch (comdId)
        {
            case Utils.TRD_TYPE_REQ_INIT:
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_INIT]" );
                
                rdPktBuf = makePacket(comdId, new byte[]{});
                break;

            case Utils.TRD_TYPE_REQ_SELF_PROTECTION:
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_SELF_PROTECTION]" );

                dataBuf = new byte[] {(byte)Utils.CMD_FS};
                rdPktBuf = makePacket(comdId, dataBuf);
                break;

            /////////////////////////////////////
            case Utils.TRD_TYPE_REQ_CURRENT_TIME:
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_CURRENT_TIME]" );

                String mapDataTimeStr = reqMapData.get(CallbackDataFormat.MapData.current_date);
                if (dataTimeStr != null) {
                    dataTimeStr = mapDataTimeStr;
                }
                // Time + FS
                dataBuf = new byte[dataTimeStr.getBytes().length + 1];
                System.arraycopy(dataTimeStr.getBytes(), 0, dataBuf, 0, dataTimeStr.getBytes().length);
                dataBuf[dataTimeStr.getBytes().length] = (byte) Utils.CMD_FS;
                rdPktBuf = makePacket(comdId, dataBuf);
                break;

            case Utils.TRD_TYPE_REQ_MUTUAL_AUTHENTICATION:
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_MUTUAL_AUTHENTICATION]");
                dataBuf = new byte[]{(byte) Utils.CMD_FS};
                rdPktBuf = makePacket(comdId, dataBuf);
                break;

            //////////////////////////////////////////
            case Utils.TRD_TYPE_REQ_ENCRRYPTION_STATE:
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_ENCRRYPTION_STATE]");
                //FS+RAND+FS+HASH+FS+SIGN+FS
                String random = reqMapData.get(CallbackDataFormat.MapData.random);
                String hash = reqMapData.get(CallbackDataFormat.MapData.hash);
                String signValue = reqMapData.get(CallbackDataFormat.MapData.sign_value);

                if (!checkMapData(new String[]{random, hash, signValue})) {
                    return null;
                }

                byte[] randomBuf = new byte[64];   //64
                byte[] hashBuf = new byte[64];     //64
                byte[] signValueBuf = signValue.getBytes(); //V512

                System.arraycopy(random.getBytes(), 0, randomBuf, 0, random.getBytes().length);
                System.arraycopy(hash.getBytes(), 0, hashBuf, 0, hash.getBytes().length);

                //FS+RAND+FS+HASH+FS+SIGN+FS
                count = 0;
                dataBuf = new byte[1 + randomBuf.length + 1 + hashBuf.length + 1 + signValueBuf.length + 1];
                dataBuf[count++] = (byte) Utils.CMD_FS;
//                count++;
                System.arraycopy(randomBuf, 0, dataBuf, count, randomBuf.length);
                count += randomBuf.length;
                dataBuf[count++] = (byte) Utils.CMD_FS;
//                count++;
                System.arraycopy(hashBuf, 0, dataBuf, count, hashBuf.length);
                count += hashBuf.length;
                dataBuf[count++] = (byte) Utils.CMD_FS;
//                count++;
                System.arraycopy(signValueBuf, 0, dataBuf, count, signValueBuf.length);
                count += signValueBuf.length;
                dataBuf[count++] = (byte) Utils.CMD_FS;
//                count++;
    
                rdPktBuf = makePacket(comdId, dataBuf);
                break;

            case Utils.TRD_TYPE_REQ_ENCRRYPTION_COMPLETE:
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_ENCRRYPTION_COMPLETE]" );
                
                //ENCKEY + FS
//              String enc_key ="68AC7DD3FF4BF081FA28D1A90E8403F6C9CCAC771F1E6BF8EB99D6B94C2526AFC2DBA3AD50AA6B09122A325B966B03EE58EA887C78C07D9F2CC02B368309042092E23CEB4E592976";
                String  enc_key = reqMapData.get("enc_key");
               
			    if (!checkMapData(new String[]{enc_key})) {
                    return null;
                }

                byte[] encKeyBuf = new byte[144];
                System.arraycopy(enc_key.getBytes(), 0, encKeyBuf, 0, enc_key.getBytes().length);

                //ENCKEY + FS
                count = 0;
                dataBuf = new byte[encKeyBuf.length + 1];
                System.arraycopy(encKeyBuf, 0, dataBuf, count, encKeyBuf.length);
                count += encKeyBuf.length;
                dataBuf[count++] = (byte) Utils.CMD_FS;
//                count++;

                rdPktBuf = makePacket(comdId, dataBuf);
                break;

            case Utils.TRD_TYPE_REQ_GENERAL_TRANSACTION: 
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_GENERAL_TRANSACTION]" );
                
                //거래구분+거래유형+거래일시+ FS+개인식별정보+FS
                String trCode_gt = reqMapData.get(CallbackDataFormat.MapData.tr_code);
                String trType = reqMapData.get(CallbackDataFormat.MapData.tr_type);
                String currentDate = reqMapData.get(CallbackDataFormat.MapData.current_date);
                String trScNum = reqMapData.get(CallbackDataFormat.MapData.tr_sc_num);

                if (!checkMapData(new String[]{trCode_gt, trType, currentDate, trScNum})) {
                    return null;
                }

                byte[] trCodeGtBuf = trCode_gt.getBytes();
                byte[] trTypeBuf = trType.getBytes();
                byte[] currentDateBuf = currentDate.getBytes();
                byte[] trScNumBuf = trScNum.getBytes(); //V512

                count = 0;
                dataBuf = new byte[trCodeGtBuf.length + trTypeBuf.length + currentDateBuf.length + 1 + trScNumBuf.length];
                System.arraycopy(trCodeGtBuf, 0, dataBuf, count, trCodeGtBuf.length);
                count += trCodeGtBuf.length;
                System.arraycopy(trTypeBuf, 0, dataBuf, count, trTypeBuf.length);
                count += trTypeBuf.length;
                System.arraycopy(currentDateBuf, 0, dataBuf, count, currentDateBuf.length);
                count += currentDateBuf.length;
                dataBuf[count++] = (byte) Utils.CMD_FS;
                //count++;
                System.arraycopy(trScNumBuf, 0, dataBuf, count, trScNumBuf.length);
                count += trScNumBuf.length;

                rdPktBuf = makePacket(comdId, dataBuf);
                break;
                
            case Utils.TRD_TYPE_REQ_IC_CARD_READ: //A7 IC 거래요청          --응답 A8
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_IC_CARD_READ]" );
                //거래구분+VAn부여 단말ID+거래금액+거래일시+FS
//                String transcType = "0"; // 거래구분  0 = 승인 , 1 = 취소, 2 = fallback
//                String termId = "1002189855";
//                String amount = "1000";
                String trCode = reqMapData.get("tr_code"); //KCPSenuTest App = 0100 승인 , 0420 취소 //Fallback 없음
                if (Utils.DEBUG_TEST_KCPSECU) {
                    if (trCode.equals("0100")) {
                        trCode = "0";
                    } else if (trCode.equals("0420")) {
                        trCode = "1";
                    } else {
                        trCode = "0";
                    }
                } else {
                    if (trCode.equals("0")) {
                        trCode = "0";
                    } else if (trCode.equals("1")) {
                        trCode = "1";
                    } else {
                        trCode = "0";
                    }
                }
                String termId_ic = reqMapData.get("term_id");
                String amount = reqMapData.get("tot_amt");

                if (!checkMapData(new String[]{trCode, termId_ic, amount})) {
                    return null;
                }

//                dataTime = Utils.convertCurrentDateToByteArray(); //14
                mapDataTimeStr = reqMapData.get(CallbackDataFormat.MapData.current_date);
                if (mapDataTimeStr != null) {
                    dataTimeStr = mapDataTimeStr;
                }

                byte[] transcTypeBuf = new byte[1];
                System.arraycopy(trCode.getBytes(), 0, transcTypeBuf, 0, trCode.getBytes().length);
                byte[] termIdBuf = new byte[10];
                System.arraycopy(termId.getBytes(), 0, termIdBuf, 0, termId.getBytes().length);
                byte[] amountBuf = new byte[9];
                System.arraycopy(amount.getBytes(), 0, amountBuf, amountBuf.length - amount.getBytes().length, amount.getBytes().length);

                //거래구분+VAn부여 단말ID+거래금액+거래일시+FS
                count = 0;
                dataBuf = new byte[transcTypeBuf.length + termIdBuf.length + amountBuf.length + dataTimeStr.getBytes().length + 1];
                System.arraycopy(transcTypeBuf, 0, dataBuf, count, transcTypeBuf.length);
                count += transcTypeBuf.length;
                System.arraycopy(termIdBuf, 0, dataBuf, count, termIdBuf.length);
                count += termIdBuf.length;
                System.arraycopy(amountBuf, 0, dataBuf, count, amountBuf.length);
                count += amountBuf.length;
                System.arraycopy(dataTimeStr.getBytes(), 0, dataBuf, count, dataTimeStr.getBytes().length);
                count += dataTimeStr.getBytes().length;
                dataBuf[count] = (byte) Utils.CMD_FS;
                count++;

                rdPktBuf = makePacket(comdId, dataBuf);

                break;

            case Utils.TRD_TYPE_REQ_IC_CARD_DATA: //TODO: A8 미구현
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_IC_CARD_DATA]" );
            
                //거래구분+VAn부여 단말ID+거래금액+거래일시+FS
                rdPktBuf = makePacket(comdId, null);
                break;
                
            case Utils.TRD_TYPE_REQ_IC_CARD_ISSUER://EMVData
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_IC_CARD_ISSUER]" );
            
                //EMV Data + FS
//              String emv_data =  "MDExM1ZJEEyUOFirXwdRZfjMwBuF+5AAAAUQAAEgb6EtatF28QDtfquTM52NW1eTZ+jlnRhYHcxc9ctZUx42MDc2MjE2MTYyMjUABQUAACAA4CjICAAACABwinjSBwYBCgOgoADCFqLTk1upwgBxPAAAFgMW";
                
                String  emv_data = reqMapData.get("emv_data");
                if(!checkMapData(new String[]{emv_data})){
                    return null;
                }

                byte[] emvDataBuf = emv_data.getBytes();

                //EMV Data + FS
                count = 0;
                dataBuf = new byte[emvDataBuf.length + 1];
                System.arraycopy(emvDataBuf, 0, dataBuf, count, emvDataBuf.length);
                count += emvDataBuf.length;
                dataBuf[count] = (byte) Utils.CMD_FS;

                rdPktBuf = makePacket(comdId, dataBuf);
                break;
                
            case Utils.TRD_TYPE_REQ_CASH_IC_READ: //1A 현금 IC카드 읽기
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_CASH_IC_READ]" );
            
                //VAn부여 단말ID+거래일시+FS
                String  termId_ = "1002189855";

                if(!checkMapData(new String[]{termId_})){
                    return null;
                }

                mapDataTimeStr = reqMapData.get(CallbackDataFormat.MapData.current_date);
                if (mapDataTimeStr != null) {
                    dataTimeStr = mapDataTimeStr;
                }

                byte[]  termIdBuf_ = new byte[10];
                
                System.arraycopy(termId_.getBytes(), 0, termIdBuf_, 0, termId_.getBytes().length);

                //거래구분+VAn부여 단말ID+거래금액+거래일시+FS
                count= 0;
                dataBuf = new byte[termIdBuf_.length+dataTimeStr.getBytes().length+1];
                System.arraycopy(termIdBuf_, 0, dataBuf, count, termIdBuf_.length);
                count+=termIdBuf_.length;
                System.arraycopy(dataTimeStr.getBytes(), 0, dataBuf, count, dataTimeStr.getBytes().length);
                count+=dataTimeStr.getBytes().length;
                dataBuf[count] = (byte)Utils.CMD_FS;
                count++;

                rdPktBuf = makePacket(comdId, dataBuf);
                break;
                
            case Utils.TRD_TYPE_REQ_CASH_IC_DATA:  //TODO: 미구현
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_CASH_IC_DATA]" );
            
                //VAn부여 단말ID+거래일시+FS+ 결제구분+ 계좌 Index+난수+ 암호화된 비밀번호 + FS

                rdPktBuf = makePacket(comdId, null);
                break;
                
            case Utils.TRD_TYPE_REQ_CASH_IC_ACCOUNT_INFO: //3A
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_CASH_IC_ACCOUNT_INFO]" );
            
                //VAn부여 단말ID+거래일시+FS+ 난수 + 암호화된 비밀번호
                rdPktBuf = makePacket(comdId, null);
                break;
                
            case Utils.TRD_TYPE_REQ_IS_INPUT_READER: //4A
                if (Utils.DEBUG_VALUE)
                    Log.v(TAG, "makeICReaderPacket [TRD_TYPE_REQ_IS_INPUT_READER]" );
            
                //VAn부여 단말ID+거래일시+FS

                String termId_4a = reqMapData.get(CallbackDataFormat.MapData.term_id);
                if(!checkMapData(new String[]{termId_4a})){
                    return null;
                }

                mapDataTimeStr = reqMapData.get(CallbackDataFormat.MapData.current_date);
                if (mapDataTimeStr != null) {
                    dataTimeStr = mapDataTimeStr;
                }

                byte[]  termIdBuf_4a = new byte[10];
                System.arraycopy(termId_4a.getBytes(), 0, termIdBuf_4a, 0, termId_4a.getBytes().length);

                //거래구분+VAn부여 단말ID+거래금액+거래일시+FS
                count= 0;
                dataBuf = new byte[termIdBuf_4a.length+dataTimeStr.getBytes().length+1];
                System.arraycopy(termIdBuf_4a, 0, dataBuf, count, termIdBuf_4a.length);
                count+=termIdBuf_4a.length;
                System.arraycopy(dataTimeStr.getBytes(), 0, dataBuf, count, dataTimeStr.getBytes().length);
                count+=dataTimeStr.getBytes().length;
                dataBuf[count] = (byte)Utils.CMD_FS;
                count++;

                rdPktBuf = makePacket(comdId, dataBuf);
                break;

            default:
                rdPktBuf = null;
                break;
        }
        
        Utils.debugTx("makeICReaderPacket() rdPktBuf   ", rdPktBuf, size);

        if (Utils.DEBUG_INFO)
            Log.i(TAG, "makeICReaderPacket() --- ");
        
        return rdPktBuf;
    }



  	/*
     *      ||   STX   ||   LENGTH (2 byte, HH,LL)    ||   CMD   ||   DATA VALUE   ||   ETX   || LRC
     */
    public static byte[] makePacket( byte cmdId, byte[] data )
    {
        if(data == null){
            data = new byte[]{};
        }

        byte[] cmdArray = new byte[6 + data.length];
        cmdArray[0] = RdiFormat.CMD_STX;                              // STX
        int length = data.length + 2;
        cmdArray[1] = (byte) (length >> 8);                  // HH
        cmdArray[2] = (byte) length;                         // LL
        cmdArray[3] = cmdId;                                    // CMD 구분코드
        for (int i = 0; i < data.length; i++) {             // Data Array
            cmdArray[4 + i] = data[i];
        }
        cmdArray[4+data.length] = RdiFormat.CMD_ETX;                  // ETX
        byte LRC = cmdArray[1];
        for(int i=2; i<cmdArray.length-1; i++){
            LRC ^= cmdArray[i];
        }
        cmdArray[cmdArray.length-1] = LRC;                    // LRC

        return cmdArray;
    }



    /*********************** KOCES Make ICREADER +++ ***********************************************/
//        public byte[] pm1100DeviceFactoryReset(byte[] dataPktBuf, byte[] rdPktBuf, int dataBufLen) {
//            Log.i(TAG, "pm1100DeviceFactoryReset() +++");
//            byte[] packet = new byte[REQ_PACKET_LENGTH_IC_DEVICE_FACTORY_RESET];
//            int size = makeICReaderPacket(packet, packet.length, Utils.CMD_ID_REQ_DEVICE_FACTORY_RESET, null);
//            rdPktBuf = new byte[size];
//            System.arraycopy(packet, 0, rdPktBuf, 0, size);
//            Utils.debugTx("DeviceFactoryReset byte[]  ", packet, size);
//            Log.i(TAG, "pm1100DeviceFactoryReset() ---");
//            return rdPktBuf;
//        }
//        private int makeICReaderPacket(byte[] packet, int packetLength, byte cmdId, byte[] data) {
//            Log.i(TAG, "makeICReaderPacket() +++");
//            int i = 0;
//            int length = packetLength - 4;
//
//            packet[i++] = 0x02;                                     //STX
//            packet[i++] = (byte)((length >> 8) & 0xff);       //HL
//            packet[i++] = (byte)(length & 0xff);              //LL
//            packet[i++] = cmdId;                                    //Command ID
//
//            if (data != null && data.length > 0) {
//                System.arraycopy(data, 0, packet, i, data.length);  //Data
//                i += data.length;
//            }
//
//            packet[i++] = 0x03;                                     //ETX
//            packet[i] = makeLRC(packet, i);                         //LRC
//            Log.i(TAG, "makeICReaderPacket() ---");
//            return i+1;                                             //return length
//        }
//
//        private byte makeLRC(byte[] packet, int length) {
//            byte LRC = 0;
//
//            for (int index = 1; index < length; index++) {
//                LRC ^= packet[index];
//            }
//
//            return LRC;
//        }

    /*********************** TEST  ICREADER --- ***********************************************/


    public byte[] getParsingData(byte[] packet) {
        int size = packet.length - 6;
        byte[] data = new byte[size];
        System.arraycopy(packet, 4, data, 0, size);  //Data
        return data;
    }

    public byte getCommandId(byte[] packet) {
        return packet[RdiFormat.STX + RdiFormat.DATA_LENGTH];//STX 1 , DATALEN 2
    }

    private boolean checkMapData(String[] mapData) {
        if(mapData == null || mapData.length ==0){
            Log.e(TAG,"inMap data is empty !");
            return false;
        }
        for(int i=0; i<mapData.length; i++){
           if(mapData[i] == null || mapData[i].length() ==0){
               Log.e(TAG,"inMap data is empty !");
               return false;
           }
        }
        return true;
    }

}
