package com.example.kcpsecutest.cat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.common.AmtoptLayout;
import com.example.kcpsecutest.common.ConmodeLayout;

import java.util.LinkedHashMap;

import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

public class CatPgCashActivity extends Activity implements View.OnClickListener {

    ConmodeLayout conmodeLayout;
    AmtoptLayout amtoptLayout;
    EditText editPgPasswd1, editPgPasswd2, editPgMsdata, editPgPartner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_pg_cash);

        init();
    }

    public void init() {
        Button btnPgAuth = (Button) findViewById(R.id.btn_auth_cat);
        Button btnPgVoid = (Button) findViewById(R.id.btn_void_cat);

        editPgPasswd1 = (EditText) findViewById(R.id.edit_pg_passwd1_catcash);
        editPgPasswd2 = (EditText) findViewById(R.id.edit_pg_passwd2_catcash);
        editPgMsdata = (EditText) findViewById(R.id.edit_pg_msdata_catcash);
        editPgPartner = (EditText) findViewById(R.id.edit_pg_partner_catcash);

        conmodeLayout = new ConmodeLayout();
        conmodeLayout.InitLayout(this);

        amtoptLayout = new AmtoptLayout();
        amtoptLayout.InitLayout(this);

        btnPgAuth.setOnClickListener(this);
        btnPgVoid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_auth_cat) {
            catPgCash("E00018", "0100");
        } else {
            catPgCash("E00018", "0420");
        }
    }

    public void catPgCash(String proc_code, String tr_code) {
        KCPAndroidApi kaa = KCPFactory.getAndroidInstance();
        LinkedHashMap<String, String> _inMap, _outMap;

        _inMap = new LinkedHashMap<String, String>();
        _outMap = new LinkedHashMap<String, String>();

        _inMap.put("con_mode", conmodeLayout.getConmode());
        _inMap.put("con_info1", conmodeLayout.getConinfo1());
        _inMap.put("con_info2", conmodeLayout.getConinfo2());

        _inMap.put("proc_code", proc_code);
        _inMap.put("tr_code", tr_code);

        _inMap.put("amtopt", amtoptLayout.getAmtopt());
        _inMap.put("cpx_flag", amtoptLayout.getCpxflag());

        _inMap.put("ins_mon", amtoptLayout.getInsmon());
        _inMap.put("tot_amt", amtoptLayout.getTotamt());
        _inMap.put("ms_data", editPgMsdata.getText().toString());

        _inMap.put("prt_cd", "1");
        _inMap.put("prtopt", "1");
        _inMap.put("prtmsg1", "이용해주셔서");
        _inMap.put("prtmsg2", "감사합니다.");

        _inMap.put("pg_partner", editPgPartner.getText().toString());

        if (tr_code.equals("0100")) {
            _inMap.put("org_amt", amtoptLayout.getOrgamt());
            _inMap.put("duty_amt", amtoptLayout.getDutyamt());
            _inMap.put("tax_amt", amtoptLayout.getTaxamt());
            _inMap.put("svc_amt", amtoptLayout.getSvcamt());
            _inMap.put("pass_wd", editPgPasswd1.getText().toString() + "0");
        }

        if (tr_code.equals("0420")) {
            _inMap.put("app_no", amtoptLayout.getAppno());
            _inMap.put("otx_dt", amtoptLayout.getOtxdt());
            _inMap.put("pass_wd", editPgPasswd1.getText().toString() + editPgPasswd2.getText().toString());
        }

        kaa.KCPCatQuery(_inMap, _outMap);

        String resResult;
        resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
        resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
        resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");
        if (_outMap.get("res_cd").toString().equals("0000")) {
            amtoptLayout.setAppno(_outMap.get("app_no").toString());
            amtoptLayout.setOtxdt(_outMap.get("tx_dt").toString().substring(0, 6));

            resResult += ("승인날짜	: " + _outMap.get("tx_dt") + "\n");
            resResult += ("거래고유번호	: " + _outMap.get("tx_no") + "\n");
            resResult += ("승인번호	: " + _outMap.get("app_no") + "\n");
            resResult += ("발급용도	: " + _outMap.get("card_gbn") + "\n");
            resResult += ("총거래금액	: " + _outMap.get("tot_amt") + "\n");
        }

        Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();
    }
}
