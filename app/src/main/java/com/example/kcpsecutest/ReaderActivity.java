package com.example.kcpsecutest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.os.Process;


import com.example.kcpsecutest.common.Util;

import java.util.LinkedHashMap;
import device.sdk.ifmmanager.IFMControllerInterface;
import device.sdk.ifmmanager.IFMManager;
import device.sdk.ifmmanager.Utils;

public class ReaderActivity extends Activity implements View.OnClickListener {
    private final String TAG = ReaderActivity.class.getSimpleName();
    String TerminalID = "1002189855";

    //*****IFM Manager +++
    public static final byte TRD_TYPE_REQ_INIT = (byte) 0xA0;                   //초기화 요청
    public static final byte TRD_TYPE_REQ_SELF_PROTECTION = (byte) 0xA1;         //무결성 검증 요청
    public static final byte TRD_TYPE_REQ_CURRENT_TIME = (byte) 0xA2;            // 시간전송 요청
    public static final byte TRD_TYPE_REQ_MUTUAL_AUTHENTICATION = (byte) 0xA3;   //상호인증 요청
    public static final byte TRD_TYPE_REQ_ENCRRYPTION_STATE = (byte) 0xA4;       // 암호화키 D/N 상태변경요청
    public static final byte TRD_TYPE_REQ_ENCRRYPTION_COMPLETE = (byte) 0xA5;    // 암호화키 D/N 완료요청
    public static final byte TRD_TYPE_REQ_GENERAL_TRANSACTION = (byte) 0xA6;     // 일반거래요청
    public static final byte TRD_TYPE_REQ_IC_CARD_READ = (byte) 0xA7;            // IC 카드읽기 요청
    public static final byte TRD_TYPE_REQ_IC_CARD_DATA = (byte) 0xA8;            // IC 카드 데이터 요청
    public static final byte TRD_TYPE_REQ_IC_CARD_ISSUER = (byte) 0xA9;          // IC 카드 발급사 요청
    //	public static final byte TRD_TYPE_REQ_CASH_IC_READ = (byte)0x1A;            // 현금IC 읽기 요청
//	public static final byte TRD_TYPE_REQ_CASH_IC_DATA = (byte)0x2A;            // 현금IC 데이터 요청
//	public static final byte TRD_TYPE_REQ_CASH_IC_ACCOUNT_INFO = (byte)0x3A;    // 현금IC 계좌정보 요청
    public static final byte TRD_TYPE_REQ_IS_INPUT_READER = (byte) 0x4A;         // 리더기 카드 삽입여부 요청


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_test);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ifmManager.powerOnRdi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ifmManager.powerDownRdi();
    }
    ReaderTestVanManager mTestVanManager;
    private void init() {
        /**< IFM을 구동하기 위한 필요 조건 :: IFM power on */
        ifmManager = new IFMManager();
        ifmManager.initController(this, recvCallback);

        //forTestVAN
        mTestVanManager = new ReaderTestVanManager(this);
        mLinkedHashMap= new LinkedHashMap<>();

        Button btn_a0 = (Button) findViewById(R.id.btn_a0);
        Button btn_a1 = (Button) findViewById(R.id.btn_a1);
        Button btn_a2 = (Button) findViewById(R.id.btn_a2);
        Button btn_a3 = (Button) findViewById(R.id.btn_a3);
        Button btn_a4 = (Button) findViewById(R.id.btn_a4);
        Button btn_a5 = (Button) findViewById(R.id.btn_a5);
        Button btn_a6 = (Button) findViewById(R.id.btn_a6);
        Button btn_a7 = (Button) findViewById(R.id.btn_a7);
        Button btn_a8 = (Button) findViewById(R.id.btn_a8);
        Button btn_a9 = (Button) findViewById(R.id.btn_a9);
        Button btn_4a = (Button) findViewById(R.id.btn_4a);

        btn_a0.setOnClickListener(this);
        btn_a1.setOnClickListener(this);
        btn_a2.setOnClickListener(this);
        btn_a3.setOnClickListener(this);
        btn_a4.setOnClickListener(this);
        btn_a5.setOnClickListener(this);
        btn_a6.setOnClickListener(this);
        btn_a7.setOnClickListener(this);
        btn_a8.setOnClickListener(this);
        btn_a9.setOnClickListener(this);
        btn_4a.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;

        LinkedHashMap<String, String> reqMapData= new LinkedHashMap<>();
//        byte[] dataTime = Utils.convertCurrentDateToByteArray();
        String dataTimeStr = Utils.convertCurrentDateToString();

        LinkedHashMap<String, String> outMap  = new LinkedHashMap<>();


        if (v.getId() == R.id.btn_a0) {
            ifmManager.reqIFM(null, TRD_TYPE_REQ_INIT);

        } else if (v.getId() == R.id.btn_a1) {
            ifmManager.reqIFM(null, TRD_TYPE_REQ_SELF_PROTECTION);

        } else if (v.getId() == R.id.btn_a2) {
            reqMapData.put(MapData.current_date, dataTimeStr);
            ifmManager.reqIFM(reqMapData, TRD_TYPE_REQ_CURRENT_TIME);

        } else if (v.getId() == R.id.btn_a3) {

            ifmManager.reqIFM(null, TRD_TYPE_REQ_MUTUAL_AUTHENTICATION);

        } else if (v.getId() == R.id.btn_a4) {
            //FS+RAND+FS+HASH+FS+SIGN+FS
//                String random = "6FB6B0121D127A43772D62F589711AF5249224F9683DE9396776C6A36B222E71";
//                String hash	= "C7F3EFD31B0ABD4952DF1E6D296C74DDF52117146C1D8D7407D5398C5F5F076F";
//                String SignValue  = "C8124BD77D9175410ADFCEA9DD8B10ED098AD6839125950820F6CDA384EAB8A728B";
            String public_key_ver = mLinkedHashMap.get(MapData.public_key_ver);
            if(public_key_ver == null || public_key_ver.length() == 0 ){
                Utils.showToastMessage(this,"","A3 상호 인증 요청을 실행하세요.");
                return ;
            }
            //To VAN public_key_ver
            outMap = mTestVanManager.KeyCert(mLinkedHashMap);

            //From VAN random, hash, sign_value
            reqMapData.put(MapData.random,outMap.get(MapData.random));
            reqMapData.put(MapData.hash,outMap.get(MapData.hash));
            reqMapData.put(MapData.sign_value,outMap.get(MapData.sign_value));

            ifmManager.reqIFM(reqMapData, TRD_TYPE_REQ_ENCRRYPTION_STATE);

        } else if (v.getId() == R.id.btn_a5) {

            String public_key_ver = mLinkedHashMap.get(MapData.public_key_ver);
            String ksn = mLinkedHashMap.get(MapData.ksn);
            String input_enc = mLinkedHashMap.get(MapData.input_enc);
            if ((public_key_ver == null || public_key_ver.length() == 0) ||
                    (ksn == null || ksn.length() == 0) ||
                    (input_enc == null || input_enc.length() == 0)) {
                Utils.showToastMessage(this, "", "A4 암호화키 상태변경 요청을 실행하세요.");
                return;
            }

            //To VAN public_key_ver, ksn, input_enc
            outMap = mTestVanManager.Key(mLinkedHashMap);
            reqMapData.put(MapData.enc_key,outMap.get(MapData.enc_key));

            //From VAN enc_key
            ifmManager.reqIFM(reqMapData, TRD_TYPE_REQ_ENCRRYPTION_COMPLETE);

        } else if (v.getId() == R.id.btn_a6) {

            popupForApproval(1);
//            ifmManager.reqIFM(null, TRD_TYPE_REQ_GENERAL_TRANSACTION);

        } else if (v.getId() == R.id.btn_a7) {

            popupForApproval(2);
//            ifmManager.reqIFM(null, TRD_TYPE_REQ_IC_CARD_READ);

        } else if (v.getId() == R.id.btn_a8) { //TODO: 미구현

            ifmManager.reqIFM(null, TRD_TYPE_REQ_IC_CARD_DATA);

        } else if (v.getId() == R.id.btn_a9) {

            String tr_code = mLinkedHashMap.get(MapData.tr_type);
            String tr_type = mLinkedHashMap.get(MapData.ic_tr_type);
            String ms_data = mLinkedHashMap.get(MapData.ms_data);
            String ksn = mLinkedHashMap.get(MapData.ksn);
            String enc_track2_data = mLinkedHashMap.get(MapData.enc_track2_data);
            String emv_data = mLinkedHashMap.get(MapData.emv_data);
            String emv_key_dt = mLinkedHashMap.get(MapData.emv_key_dt);
            String emv_data_dt = mLinkedHashMap.get(MapData.emv_data_dt);


            if ((tr_code == null || tr_code.length() == 0) ||
                    (tr_type == null || tr_type.length() == 0) ||
                    (ms_data == null || ms_data.length() == 0) ||
                    (ksn == null || ksn.length() == 0) ||
                    (enc_track2_data == null || enc_track2_data.length() == 0) ||
                    (emv_data == null || emv_data.length() == 0) ||
                    (emv_key_dt == null || emv_key_dt.length() == 0) ||
                    (emv_data_dt == null || emv_data_dt.length() == 0)
                    ) {
                Utils.showToastMessage(this, "", "A7 결제 요청을 실행하세요.");
                return;
            }
            //To VAN emv_data
            outMap = mTestVanManager.CreditAuth(mLinkedHashMap);
            if(outMap == null){
                Utils.showToastMessage(this,"","VAN응답 실패");
                return;
            }
            reqMapData.put(MapData.emv_data,outMap.get(MapData.emv_data));


            //From VAN emv_data
            ifmManager.reqIFM(reqMapData, TRD_TYPE_REQ_IC_CARD_ISSUER);

        } else if (v.getId() == R.id.btn_4a) {
            reqMapData.put(MapData.term_id, TerminalID);
            reqMapData.put(MapData.current_date, dataTimeStr);
            ifmManager.reqIFM(reqMapData, TRD_TYPE_REQ_IS_INPUT_READER);
        }
    }


    //IFM SDK 호출
    IFMManager ifmManager;
    LinkedHashMap<String, String> mLinkedHashMap; //IFM 응답 hashmap

    IFMControllerInterface.OnRecvCallback recvCallback = new IFMControllerInterface.OnRecvCallback() {
        @Override
        public void onCallback(int callbackMsg, byte[] recvBuf, LinkedHashMap<String, String> linkedHashMap) {
//          TEST -callback
            Log.i(TAG, "callbackMsg " + callbackMsg);
            if (recvBuf == null) {
                Log.i(TAG, "onCallback byte[] == null");
            } else {
                Log.i(TAG, "onCallback byte[] != null");
                if(linkedHashMap == null){
                    Log.i(TAG, "onCallback HashMap == null");
                }else {
                    Log.i(TAG, "onCallback HashMap != null");
                    mLinkedHashMap = linkedHashMap;
                    Utils.debugTx(TAG, recvBuf, recvBuf.length);
                    printMap(linkedHashMap);
                }
            }


        }
    };


    public void printMap(LinkedHashMap<String, String> linkedHashMap) {
        String resultStr= "***********result hash map *************\n";
        resultStr +=" KEY_TRD_TYPE : " +linkedHashMap.get(MapData.KEY_TRD_TYPE)+"\n";
        resultStr +=" KEY_RES_CODE : " +linkedHashMap.get(MapData.KEY_RES_CODE)+"\n";
        resultStr +=" KEY_READER_SN : " +linkedHashMap.get(MapData.KEY_READER_SN)+"\n";
        resultStr +=" KEY_READER_VER_DATE : " +linkedHashMap.get(MapData.KEY_READER_VER_DATE)+"\n";
        resultStr +=" KEY_READER_VER : " +linkedHashMap.get(MapData.KEY_READER_VER)+"\n";
        resultStr +=" KEY_READER_CERTI : " +linkedHashMap.get(MapData.KEY_READER_CERTI)+"\n";

        resultStr +=" current_date : " +linkedHashMap.get(MapData.current_date)+"\n";
        resultStr +=" integt_verification : " +linkedHashMap.get(MapData.integt_verification)+"\n";
        resultStr +=" public_key_ver : " +linkedHashMap.get(MapData.public_key_ver)+"\n";
        resultStr +=" ksn : " +linkedHashMap.get(MapData.ksn)+"\n";
        resultStr +=" input_enc : " +linkedHashMap.get(MapData.input_enc)+"\n";
        resultStr +=" ms_data : " +linkedHashMap.get(MapData.ms_data)+"\n";
        resultStr +=" enc_track2_data : " +linkedHashMap.get(MapData.enc_track2_data)+"\n";
        resultStr +=" ic_tr_type : " +linkedHashMap.get(MapData.ic_tr_type)+"\n";
        resultStr +=" tr_type : " +linkedHashMap.get(MapData.tr_type)+"\n";
        resultStr +=" fb_code : " +linkedHashMap.get(MapData.fb_code)+"\n";
        resultStr +=" card_type : " +linkedHashMap.get(MapData.card_type)+"\n";
        resultStr +=" issuer_country_code : " +linkedHashMap.get(MapData.issuer_country_code)+"\n";
        resultStr +=" emv_data : " +linkedHashMap.get(MapData.emv_data)+"\n";
        resultStr +=" emv_key_dt : " +linkedHashMap.get(MapData.emv_key_dt)+"\n";
        resultStr +=" emv_data_dt : " +linkedHashMap.get(MapData.emv_data_dt)+"\n";
        resultStr +=" term_vr : " +linkedHashMap.get(MapData.term_vr)+"\n";
        resultStr +=" app_crypt : " +linkedHashMap.get(MapData.app_crypt)+"\n";
        resultStr +=" issuer_sr : " +linkedHashMap.get(MapData.issuer_sr)+"\n";
        resultStr +=" ic_input_card : " +linkedHashMap.get(MapData.ic_input_card)+"\n";
        resultStr += "****************************************";

        Utils.SimpleDailog(this,"",resultStr);
        Log.v(TAG,resultStr);
    }

    public class MapData {
        //    From App Map Data
        public static final String current_date = "current_date";
        public static final String random = "random";
        public static final String hash = "hash";
        public static final String sign_value = "sign_value";
        public static final String enc_key = "enc_key";
        public static final String tr_code = "tr_code";
        public static final String term_id = "term_id";
        public static final String tot_amt = "tot_amt";
        public static final String card_aid_num = "card_aid_num";
        public static final String env_data = "env_data";
        public static final String cash_type = "cash_type";
        public static final String acc_index = "acc_index";
        public static final String rand_num = "rand_num";
        public static final String encrpt_pwd = "encrpt_pwd";
        public static final String current_date_rn = "current_date_rn";

        //    To App Callback  header Data
        public static final String KEY_TRD_TYPE = "trd_type";
        public static final String KEY_RES_CODE = "res_code";
        public static final String KEY_READER_SN = "reader_sn";        //"1000000000");
        public static final String KEY_READER_VER_DATE = "reader_ver_date";
        public static final String KEY_READER_VER = "reader_ver";      //"C101");
        public static final String KEY_READER_CERTI = "reader_cert";   //"#DUALPAY633C");

//    String curren_date = "curren_date";
        public static final String integt_verification = "integt_verification";
        public static final String public_key_ver = "public_key_ver";
        public static final String ksn = "ksn";
        public static final String input_enc = "input_enc";
        public static final String ms_data = "ms_data";
        public static final String enc_track2_data = "enc_track2_data";
        public static final String ic_tr_type = "ic_tr_type";
        public static final String tr_type = "tr_type";
        public static final String tr_date = "tr_date";
        public static final String tr_sc_num = "tr_sc_num";
        public static final String fb_code = "fb_code";
        public static final String card_type = "card_type";
        public static final String issuer_country_code = "issuer_country_code";
        public static final String emv_data = "emv_data";
        public static final String emv_key_dt = "emv_key_dt";
        public static final String emv_data_dt = "emv_data_dt";
        public static final String term_vr = "term_vr";
        public static final String app_crypt = "app_crypt";
        public static final String issuer_sr = "issuer_sr";
        public static final String ic_input_card ="ic_input_card";

    }


    public void popupForApproval(final int flag) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Context context = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_tr_popup, (ViewGroup) findViewById(R.id.all_llayout));

        final RelativeLayout a6Layout = (RelativeLayout) layout.findViewById(R.id.a6Layout);
        final RelativeLayout a7Layout = (RelativeLayout) layout.findViewById(R.id.a7Layout);


        RadioGroup  mRadioGroup = (RadioGroup) layout.findViewById(R.id.radioGroupApproval);
        mRadioGroup.setVisibility(View.GONE);
        if(flag == 1){  //1 = A6
            a6Layout.setVisibility(View.VISIBLE);
            a7Layout.setVisibility(View.GONE);
        }else{          //2 = A7
            a6Layout.setVisibility(View.GONE);
            a7Layout.setVisibility(View.VISIBLE);
        }


        final EditText  mEdit_keyin = (EditText) layout.findViewById(R.id.edit_trscnum);
        final String[] trcodeA6 = new String[1];
        trcodeA6[0]="0";
        RadioGroup  mRadio_trcode_a6 = (RadioGroup) layout.findViewById(R.id.trcode_a6_RadioBtn);
        mRadio_trcode_a6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.RadioBtn_0:   //신용
                        trcodeA6[0] = "0";
                        break;
                    case R.id.RadioBtn_1:   //기타서비스
                        trcodeA6[0] = "1";
                        break;
                    case R.id.RadioBtn_2:   //회원카드
                        trcodeA6[0] = "2";
                        break;
                }
            }
        });

        final String[] teTypeA6 = new String[1];
        teTypeA6[0]="0";
        RadioGroup mRadio_tetype_a6 = (RadioGroup) layout.findViewById(R.id.tetype_RadioBtn);
        mRadio_tetype_a6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.RadioBtn_msr:     //msr
                        teTypeA6[0] = "0";
                        break;
                    case R.id.RadioBtn_keyIn:   //keyIn
                        teTypeA6[0] = "1";
                        break;

                }
            }
        });



        //A7
        final EditText  mEdit_term_id = (EditText) layout.findViewById(R.id.edit_term_id);
        final EditText  mEdit_amount = (EditText) layout.findViewById(R.id.edit_amount);
        mEdit_term_id.setText(TerminalID);
        mEdit_amount.setText("");
        final String[] trcodeA7 = new String[1];
        trcodeA7[0]="0";
        final RadioGroup  mRadio_trcode_a7 = (RadioGroup) layout.findViewById(R.id.trcode_a7_RadioBtn);
        mRadio_trcode_a7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.RadioBtn_ok:
                        trcodeA7[0] = "0";
                        break;
                    case R.id.RadioBtn_cancel:
                        trcodeA7[0] = "1";
                        break;
                    case R.id.RadioBtn_fallback:
                        trcodeA7[0] = "2";
                        break;
                }
            }
        });


        builder.setTitle("거래");
        builder.setView(layout);


        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel, null);

        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button okBtn = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                okBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if(flag == 1){  //1 = A6
                            //거래구분+거래유형+거래일시+ FS+개인식별정보+FS
                            //tr_code tr_type  tr_date tr_sc_num
                            String trcode = trcodeA6[0];
                            String teType = teTypeA6[0];
                            String tr_sc_num = mEdit_keyin.getText().toString();

                            LinkedHashMap<String, String> reqMapData= new LinkedHashMap<>();
                            reqMapData.put(MapData.tr_code,trcode);
                            reqMapData.put(MapData.tr_type,teType);
                            reqMapData.put(MapData.current_date,Utils.convertCurrentDateToString());
                            reqMapData.put(MapData.tr_sc_num,tr_sc_num );
                            ifmManager.reqIFM(reqMapData, TRD_TYPE_REQ_GENERAL_TRANSACTION);

                        }else{          //2 = A7
                            //거래구분+VAn부여 단말ID+거래금액+거래일시+FS
                            // String transcType = "0"; // 거래구분  0 = 승인 , 1 = 취소, 2 = fallback
                            String trcode = trcodeA7[0];
                            String termId = mEdit_term_id.getText().toString();
                            String amount = mEdit_amount.getText().toString();

                            switch (mRadio_trcode_a7.getCheckedRadioButtonId()) {
                                case R.id.RadioBtn_ok:
                                    trcode = "0";
                                    break;
                                case R.id.RadioBtn_cancel:
                                    trcode = "1";
                                    break;
                                case R.id.RadioBtn_fallback:
                                    trcode = "2";
                                    break;
                            }
                            LinkedHashMap<String, String> reqMapData= new LinkedHashMap<>();
                            reqMapData.put(MapData.tr_code,trcode);
                            reqMapData.put(MapData.term_id,termId);
                            reqMapData.put(MapData.tot_amt,amount);
                            reqMapData.put(MapData.current_date,Utils.convertCurrentDateToString());

                            ifmManager.reqIFM(reqMapData, TRD_TYPE_REQ_IC_CARD_READ);
                        }
                        mAlertDialog.dismiss();
                    }
                });
                Button cancelBtn = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAlertDialog.dismiss();
                    }
                });
            }
        });
        mAlertDialog.show();
    }
}
