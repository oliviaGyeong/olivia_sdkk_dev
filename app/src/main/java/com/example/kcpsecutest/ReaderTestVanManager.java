package com.example.kcpsecutest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.kcpsecutest.common.Util;

import java.util.LinkedHashMap;

import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

/**
 * Created by olivia on 18. 1. 22.
 */

public class ReaderTestVanManager {
    private final String TAG = ReaderTestVanManager.class.getSimpleName();
    private Context mContext;
    public ReaderTestVanManager(Context context){
        mContext = context;

    }



    public LinkedHashMap<String, String> KeyCert(LinkedHashMap<String, String> mLinkedHashMap)
    {
        KCPAndroidApi kja = KCPFactory.getAndroidInstance();
        LinkedHashMap<String, String> _inMap, _outMap;

        /**< 각 packet 별로 timeout 설정을 달리 하기위해 만든 method :
         *  random number generate 의 시간이 많이 소요되는 경우에 대한 대비 */
//		ifmManager.Req_IFMex(null,TRD_TYPE_REQ_MUTUAL_AUTHENTICATION, Utils.TIMEOUT_7SEC);

        _inMap = new LinkedHashMap<String, String>();
        _outMap = new LinkedHashMap<String, String>();

        _inMap.put("con_mode", Util.getNetwork(mContext));
        _inMap.put("proc_code", "D03000");
        _inMap.put("tr_code", "0100");
        _inMap.put("busi_cd", "TEST");
        _inMap.put("reader_sn", "1000000000");     //from IFM
        _inMap.put("pos_sn", "POS1234567890");
        _inMap.put("reader_cert", "#DUALPAY633C"); //from IFM
        _inMap.put("reader_ver", "C101");
        _inMap.put("sw_cert", "###SUPER-POS");
        _inMap.put("sw_ver", "V100");
        _inMap.put("term_id", "1002189855");
        _inMap.put("trace_no", "1");
        _inMap.put("signpad_yn", "N");

        _inMap.put("busi_no", "1138521083");
//		_inMap.put("public_key_ver", editPublicKey.getText().toString());

        /**< set the key-version from the IFM */
        if(mLinkedHashMap != null) {
            _inMap.put("public_key_ver", mLinkedHashMap.get("public_key_ver"));
        }else{
            _inMap.put("public_key_ver", "00");
        }

/* check code ::
		String	pubKeyVer = _inMap.get("public_key_ver").toString();
		Log.v("public_key_ver", pubKeyVer);
*/
        ////////////////////////////////////
        kja.KCPServerQuery(_inMap, _outMap);
        ////////////////////////////////////

        String	res_cd	= _outMap.get("res_cd").toString();
        String	resmsg1 = _outMap.get("resmsg1").toString();
        String	resmsg2 = _outMap.get("resmsg2").toString();

        String resBuff = "응답코드	: " + res_cd + "\n";
        resBuff += "응답메시지1	: " + resmsg1 + "\n";
        resBuff += "응답메시지2	: " + resmsg2 + "\n";

        if(res_cd.equals("0000")) {
            resBuff += "random	: " + _outMap.get("random").toString() + "\n";
            resBuff += "hash	: " + _outMap.get("hash").toString() + "\n";
            resBuff += "SingValue : " + _outMap.get("sign_value").toString() + "\n";
        }

        Toast.makeText(mContext, resBuff, Toast.LENGTH_LONG).show();
        Log.v(TAG,"Van MSG  :: "+resBuff);

        return _outMap;
    }




    public LinkedHashMap<String, String> Key(LinkedHashMap<String, String> mLinkedHashMap)
    {
        KCPAndroidApi kja = KCPFactory.getAndroidInstance();
        LinkedHashMap<String, String> _inMap, _outMap;

        _inMap = new LinkedHashMap<String, String>();
        _outMap = new LinkedHashMap<String, String>();

        _inMap.put("con_mode", Util.getNetwork(mContext));
        _inMap.put("proc_code", "D04000");
        _inMap.put("tr_code", "0100");
        _inMap.put("busi_cd", "TEST");
        _inMap.put("reader_sn", "1000000000");
        _inMap.put("pos_sn", "POS1234567890");
        _inMap.put("reader_cert", "#DUALPAY633C");
        _inMap.put("reader_ver", "C101");
        _inMap.put("sw_cert", "###SUPER-POS");
        _inMap.put("sw_ver", "V100");
        _inMap.put("term_id", "1002189855");
        _inMap.put("trace_no", "1");
        _inMap.put("signpad_yn", "N");

        /**< IFM에서 읽은 data의 처리 */
//        _inMap.put("busi_no", editBusino.getText().toString());
//        _inMap.put("public_key_ver", editPublicKey.getText().toString());
//        _inMap.put("input_enc", editEnc.getText().toString());
//        _inMap.put("ksn", editKsn.getText().toString());

        //TODO: 리더기로 부터 받은 응답 넣어줌
        if(mLinkedHashMap != null) {
            _inMap.put("public_key_ver", mLinkedHashMap.get("public_key_ver"));
            _inMap.put("input_enc", mLinkedHashMap.get("input_enc"));
            _inMap.put("ksn", mLinkedHashMap.get("ksn"));
        } else{
            Log.v(TAG,"RDI로부터 요청 데이터가 없음. \n public_key_ver\n input_enc\n,ksn");
        }
        ////////////////////////////////////
        kja.KCPServerQuery(_inMap, _outMap);
        ////////////////////////////////////

        String	res_cd  = _outMap.get("res_cd").toString();
        String	resmsg1 = _outMap.get("resmsg1").toString();
        String	resmsg2 = _outMap.get("resmsg2").toString();

        String resBuff = "응답코드	: " + res_cd + "\n";
        resBuff += "응답메시지1	: " + resmsg1 + "\n";
        resBuff += "응답메시지2	: " + resmsg2 + "\n";

        if(res_cd.equals("0000")) {
            resBuff += "enc_key	: " + _outMap.get("enc_key").toString() + "\n";
//            //TODO: 리더기 요청
//            ifmManager.reqIFM(_outMap,TRD_TYPE_REQ_ENCRRYPTION_COMPLETE);//(byte)0xA5;    // 암호화키 D/N 완료요청
        }

//        /**< 리더기에 전송   enc_key 암호화 키 */
        Toast.makeText(mContext, resBuff, Toast.LENGTH_SHORT).show();
        Log.v(TAG,"Van MSG  :: "+resBuff);
        return _outMap;
    }




//IC거래 To Van
    public LinkedHashMap<String, String>  CreditAuth(LinkedHashMap<String, String> mLinkedHashMap) {
        KCPAndroidApi kja = KCPFactory.getAndroidInstance();
        LinkedHashMap<String, String> _inMap, _outMap;
        _inMap = new LinkedHashMap<String, String>();
        _outMap = new LinkedHashMap<String, String>();

        _inMap.put("con_mode", Util.getNetwork(mContext));
        _inMap.put("proc_code", "A01001");

        _inMap.put("busi_cd", "TEST");
        _inMap.put("reader_sn", "1000000000");
        _inMap.put("pos_sn", "POS1234567890");
        _inMap.put("reader_cert", "#DUALPAY633C");
        _inMap.put("reader_ver", "C101");
        _inMap.put("sw_cert", "###SUPER-POS");
        _inMap.put("sw_ver", "V100");
        _inMap.put("term_id", "1002189855");
        _inMap.put("trace_no", "1");
        _inMap.put("signpad_yn", "Y");

//        _inMap.put("tx_type", getTxtype(3));
//        _inMap.put("ksn", cardLayout.getKsn());
//        _inMap.put("ms_data", cardLayout.getMsdata());
//        _inMap.put("emv_data", editEmv.getText().toString());
//        _inMap.put("emv_key_dt", "160530");
//        _inMap.put("emv_data_dt", "160530");

        _inMap.put("pass_wd", "");

        _inMap.put("cpx_flag", "N");
        _inMap.put("ins_mon", "00");
        _inMap.put("tot_amt", "1004");
        _inMap.put("org_amt", "1000");
        _inMap.put("duty_amt", "0");
        _inMap.put("tax_amt", "0");
        _inMap.put("otx_dt", "");
        _inMap.put("app_no", "");


        //Bitmap
//        if (m_bmpSign != null) {
//            String tmpSign = kja.KCPSignCrypt(m_bmpSign);
//            _inMap.put("sign_img", tmpSign);
//        }


            //TODO: 리더기로 부터 받은 응답
        //     con_mode::TEST_WIFI
        //     tx_type::10
        //     ksn::hjEwAAAAACAAAw==
        //     ms_data::V2TxBoqW0fCfQKp4GSfh+3fHQUPSAVf5iMZZ+XxVsKLlUeYxvMxSStZ6JpkT81sQ
        //     emv_data::MDExM1ZJEEyUOFirXwdRZfjMwBuF+5AAAAUQAAEgb6EtatF28QDtfquTM52NW1eTZ+jlnRhYHcxc9ctZUx42MDc2MjE2MTYyMjUABQUAACAA4CjICAAACABwinjSBwYBCgOgoADCFqLTk1upwgBxPAAAFgMW
        _inMap.put("tr_code",getTrCode(mLinkedHashMap.get(ReaderActivity.MapData.tr_type)));
        _inMap.put("tx_type",getTxtype(mLinkedHashMap.get(ReaderActivity.MapData.ic_tr_type)));
            _inMap.put("ksn", mLinkedHashMap.get(ReaderActivity.MapData.ksn));
            _inMap.put("ms_data", mLinkedHashMap.get(ReaderActivity.MapData.enc_track2_data));

            _inMap.put("emv_data", mLinkedHashMap.get(ReaderActivity.MapData.emv_data));
            _inMap.put("emv_key_dt", mLinkedHashMap.get(ReaderActivity.MapData.emv_key_dt));
            _inMap.put("emv_data_dt", mLinkedHashMap.get(ReaderActivity.MapData.emv_data_dt));

//                ic_tr_type tr_type fb_code current_date card_type issuer_country_code
//                  ms_data ksn enc_track2_data emv_data emv_key_dt emv_data_dt
            //fallback data _inMap이 없음.


            kja.KCPServerQuery(_inMap, _outMap); // VAN 요청 & 응답

            String resResult;
            resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
            resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
            resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");

            if (_outMap.get("res_cd").toString().equals("0000")) {
//                amtLayout.setAppno(_outMap.get("app_no").toString());
//                amtLayout.setOtxdt(_outMap.get("tx_dt").toString().substring(0, 6));

                resResult += ("승인날짜	: " + _outMap.get("tx_dt") + "\n");
                resResult += ("거래고유번호	: " + _outMap.get("tx_no") + "\n");
                resResult += ("승인번호	: " + _outMap.get("app_no") + "\n");
                resResult += ("카드구분	: " + _outMap.get("card_gbn") + "\n");
                resResult += ("EMV	: " + _outMap.get("emv_data") + "\n");
                resResult += ("자동이체코드	: " + _outMap.get("at_type") + "\n");
                resResult += ("프린트코드	: " + _outMap.get("prt_cd") + "\n");
                resResult += ("매입사코드	: " + _outMap.get("ac_code") + "\n");
                resResult += ("매입사명	: " + _outMap.get("ac_name") + "\n");
                resResult += ("발급사코드	: " + _outMap.get("cc_cd") + "\n");
                resResult += ("발급사명	: " + _outMap.get("cc_name") + "\n");
                resResult += ("가맹점번호	: " + _outMap.get("mcht_no") + "\n");
            }
//            bitmapData = null;
//            m_bmpSign = null;
//            img.setImageBitmap(null);

            Toast.makeText(mContext, resResult, Toast.LENGTH_SHORT).show();
            Log.v(TAG,"Van MSG  :: "+resResult);
            //TODO : IFM manager request  //TrdType A9 발급사 요청
//            IFM_RESPONSE_FLAG = TRD_TYPE_REQ_IC_CARD_ISSUER;
//            ifmManager.reqIFM(_outMap, TRD_TYPE_REQ_IC_CARD_ISSUER); //TrdType A9 발급사 요청
        return _outMap;
    }




    public String getTrCode(String rgTrCode) {
        if(rgTrCode== null){
            return null;
        }

        if (rgTrCode.equals("0")) {       //승인
            return "0100";
        } else if (rgTrCode.equals("1")) { //취소
            return "0420";
        }
        return null;
    }

    public String getTxtype(String rgTxType) {
        if(rgTxType== null){
            return null;
        }

        if (rgTxType.equals("0")) {       //ic
            return "30";
        } else if (rgTxType.equals("1")) { //fallback
            return "60";
        } else if (rgTxType.equals("2")) { //ms
            return "20";
        }

//            if(rgTxType.equals("0" ) {//rbtn_keyin_server = 1
//                return "10";
//            } else if(rgTxType == 2) {//rbtn_swipe_server = 2
//                return "20";
//            } else if(rgTxType == 3) {// rbtn_ic_server =3
//                return "30";
//            } else if(rgTxType == 4) { //rbtn_fb_server =4
//                return "60";
//            } else
//                return "10";
        return null;
    }

    public String getAmtopt(boolean check) {
        if(check)
            return "Y";
        else
            return "N";
    }
    public String getCpxflag(boolean check) {
        if(check)
            return "Y";
        else
            return "N";
    }
//


// A6 일반거래 요청  후  VAN +++
    public LinkedHashMap<String, String> KCP_AUTH_CASH(LinkedHashMap<String, String> mLinkedHashMap) {
        KCPAndroidApi kja = KCPFactory.getAndroidInstance();
        LinkedHashMap<String, String> _inMap, _outMap;
        _inMap = new LinkedHashMap<String, String>();
        _outMap = new LinkedHashMap<String, String>();
        //String tx_type = null;

//        byte[] cashType = new byte[2];
//        if(rgDivision.getCheckedRadioButtonId() == R.id.rbtn_one_cash) {        //개인소득공제
//            cashType[0] = '0';
//        } else if(rgDivision.getCheckedRadioButtonId() == R.id.rbtn_two_cash) { //사업자소득공제
//            cashType[0] = '1';
//        }
//
//        if(tr_code.equals("0100"))
//            cashType[1] = '0';
//        else {
//            if(rgVoidtype.getCheckedRadioButtonId() == R.id.rbtn_dealcancel_cash) {
//                cashType[1] = '1';
//            } else if(rgVoidtype.getCheckedRadioButtonId() == R.id.rbtn_errorcancel_cash) {
//                cashType[1] = '2';
//            } else if(rgVoidtype.getCheckedRadioButtonId() == R.id.rbtn_ordercancel_cash) {
//                cashType[1] = '3';
//            }
//        }


        _inMap.put("con_mode", Util.getNetwork(mContext));
        _inMap.put("proc_code", "C01000");
        _inMap.put("tr_code", getTrCode("0")); //0= 승인, 1= 취소

        _inMap.put("busi_cd", "TEST");
        _inMap.put("reader_sn", "1000000000");
        _inMap.put("pos_sn", "POS1234567890");
        _inMap.put("reader_cert", "#DUALPAY633C");
        _inMap.put("reader_ver", "C101");
        _inMap.put("sw_cert", "###SUPER-POS");
        _inMap.put("sw_ver", "V100");
        _inMap.put("term_id", "1002189855");
        _inMap.put("trace_no", "1");
        _inMap.put("cpx_flag", "N");
        _inMap.put("signpad_yn", "N");


        _inMap.put("prtopt", "1");
        _inMap.put("tx_type", getTxtype("2")); //IC ,fallback, ms

        //TODO: 리더기로 부터 받은 응답
        _inMap.put("emv_data", mLinkedHashMap.get(ReaderActivity.MapData.emv_data));
        _inMap.put("ksn", mLinkedHashMap.get(ReaderActivity.MapData.ksn));
        _inMap.put("ms_data", mLinkedHashMap.get(ReaderActivity.MapData.ms_data));


        _inMap.put("pass_wd", "");

        _inMap.put("ins_mon", "00");
        _inMap.put("tot_amt", "1004");
        _inMap.put("org_amt", "1000");
        _inMap.put("duty_amt", "0");
        _inMap.put("tax_amt", "0");
        _inMap.put("otx_dt", "");
        _inMap.put("app_no", "");

        kja.KCPServerQuery(_inMap, _outMap);

        String res_cd = _outMap.get("res_cd").toString();
        String resmsg1 = _outMap.get("resmsg1").toString();
        String resmsg2 = _outMap.get("resmsg2").toString();

        String resBuff = "응답코드	: " + res_cd + "\n";
        resBuff += "응답메시지1	: " + resmsg1 + "\n";
        resBuff += "응답메시지2	: " + resmsg2 + "\n";

        if(_outMap.get("res_cd").toString().equals("0000")) {
//            amtLayout.setAppno(_outMap.get("app_no").toString());
//            amtLayout.setOtxdt(_outMap.get("tx_dt").toString().substring(0, 6));

            resBuff += ("승인날짜	: " + _outMap.get("tx_dt") + "\n");
            resBuff += ("거래고유번호	: " + _outMap.get("tx_no") + "\n");
            resBuff += ("승인번호	: " + _outMap.get("app_no") + "\n");
            resBuff += ("카드구분	: " + _outMap.get("card_gbn") + "\n");
            resBuff += ("프린트코드	: " + _outMap.get("prt_cd") + "\n");
        }

        Toast.makeText(mContext, resBuff, Toast.LENGTH_SHORT).show();
        return _outMap;
    }
    // A6 일반거래 요청  후  VAN ---
}
