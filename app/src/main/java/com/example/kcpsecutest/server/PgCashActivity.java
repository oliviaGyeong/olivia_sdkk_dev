package com.example.kcpsecutest.server;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.common.AmtoptLayout;
import com.example.kcpsecutest.common.CardInfoLayout;

import java.util.LinkedHashMap;

import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

public class PgCashActivity extends Activity implements View.OnClickListener {

    AmtoptLayout amtoptLayout;
    CardInfoLayout cardInfoLayout;

    EditText editPassWd1, editPassWd2, editMsData, editConmode, editPgPartner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_cash);
        init();
    }

    public void init() {
        editPassWd1 = (EditText)findViewById(R.id.edit_passwd1_cash);
        editPassWd2 = (EditText)findViewById(R.id.edit_passwd2_cash);
        editMsData = (EditText)findViewById(R.id.edit_msdata_cash);
        editConmode = (EditText)findViewById(R.id.edit_conmode_credit);
        editPgPartner = (EditText)findViewById(R.id.edit_pg_partner_cash);

        Button btnPgAuth = (Button)findViewById(R.id.btn_auth_pg);
        Button btnPgCan = (Button)findViewById(R.id.btn_can_pg);

        amtoptLayout = new AmtoptLayout();
        amtoptLayout.InitLayout(this);
        cardInfoLayout = new CardInfoLayout();
        cardInfoLayout.InitLayout(this);

        btnPgAuth.setOnClickListener(this);
        btnPgCan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_auth_pg) {
            serverPgCash("C01000", "0100");
        } else {
            serverPgCash("C01000", "0420");
        }
    }

    public void serverPgCash(String proc_code, String tr_code) {
        KCPAndroidApi kaa = KCPFactory.getAndroidInstance();
        LinkedHashMap<String, String> _inMap, _outMap;

        _inMap = new LinkedHashMap<String, String>();
        _outMap = new LinkedHashMap<String, String>();

        _inMap.put("con_mode", editConmode.getText().toString());
        _inMap.put("proc_code", proc_code);
        _inMap.put("tr_code", tr_code);
        _inMap.put("busi_cd", "TEST");
        _inMap.put("reader_sn", "1000000000");
        _inMap.put("pos_sn", "POS1234567890");
        _inMap.put("reader_cert", "#DUALPAY633C");
        _inMap.put("reader_ver", "C101");
        _inMap.put("sw_cert", "###SUPER-POS");
        _inMap.put("sw_ver", "V100");
        _inMap.put("term_id", "1002189853");
        _inMap.put("trace_no", "1");
        _inMap.put("signpad_yn", "N");
        _inMap.put("ipek_yn", "P");

        _inMap.put("tx_type", cardInfoLayout.getTxtype());
        _inMap.put("ksn", cardInfoLayout.getKsn());
        _inMap.put("ms_data", cardInfoLayout.getMsdata());
        _inMap.put("emv_data", "");

        if(tr_code.equals("0100")) {
            _inMap.put("pass_wd", editPassWd1.getText().toString() + "1");
        } else {
            _inMap.put("pass_wd", editPassWd1.getText().toString() + editPassWd2.getText().toString());
        }

        _inMap.put("ins_mon", amtoptLayout.getInsmon());
        _inMap.put("tot_amt", amtoptLayout.getTotamt());
        _inMap.put("org_amt", amtoptLayout.getOrgamt());
        _inMap.put("duty_amt", amtoptLayout.getDutyamt());
        _inMap.put("svc_amt", amtoptLayout.getSvcamt());
        _inMap.put("tax_amt", amtoptLayout.getTaxamt());

        if(tr_code.equals("0420")) {
            _inMap.put("otx_dt", amtoptLayout.getOtxdt());
            _inMap.put("app_no", amtoptLayout.getAppno());
        }

        kaa.KCPServerQuery(_inMap, _outMap);

        String resResult;

        resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
        resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
        resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");

        if(_outMap.get("res_cd").toString().equals("0000")) {
            amtoptLayout.setAppno(_outMap.get("app_no").toString());
            amtoptLayout.setOtxdt(_outMap.get("tx_dt").toString().substring(0, 6));

            resResult += ("신분정보	: " + _outMap.get("ms_data") + "\n");
            resResult += ("거래승인일시	: " + _outMap.get("tx_dt") + "\n");
            resResult += ("거래고유번호	: " + _outMap.get("tx_no") + "\n");
            resResult += ("승인번호	: " + _outMap.get("app_no") + "\n");
            resResult += ("발급용도	: " + _outMap.get("card_gbn") + "\n");
        }

        Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();
    }
}