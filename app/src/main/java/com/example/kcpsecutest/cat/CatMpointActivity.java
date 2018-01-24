package com.example.kcpsecutest.cat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.common.ConmodeLayout;

import java.util.LinkedHashMap;

import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

public class CatMpointActivity extends Activity implements View.OnClickListener {

    ConmodeLayout conmodeLayout;

    EditText editTotamt, editOtxdt, editAppno, editAppnopoint;

    Button btnAuth, btnCan, btnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_mpoint);

        init();
    }

    public void init() {
        editTotamt = (EditText)findViewById(R.id.edit_totamt_catMpoint);
        editOtxdt = (EditText)findViewById(R.id.edit_otxdt_catMpoint);
        editAppno = (EditText)findViewById(R.id.edit_appno_catMpoint);
        editAppnopoint = (EditText)findViewById(R.id.edit_appnopoint_catMpoint);

        btnAuth = (Button)findViewById(R.id.btn_auth_catMpoint);
        btnCan = (Button)findViewById(R.id.btn_can_catMpoint);
        btnCheck = (Button)findViewById(R.id.btn_check_catMpoint);

        btnAuth.setOnClickListener(this);
        btnCan.setOnClickListener(this);
        btnCheck.setOnClickListener(this);

        conmodeLayout = new ConmodeLayout();
        conmodeLayout.InitLayout(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_auth_catMpoint) {
            mPointAuth("0100");
        } else if(v.getId() == R.id.btn_can_catMpoint) {
            mPointAuth("0420");
        } else {
            mPointAuth("4100");
        }
    }

    public void mPointAuth(String tr_code) {
        KCPAndroidApi kaa = KCPFactory.getAndroidInstance();
        LinkedHashMap<String, String> _inMap, _outMap;

        _inMap = new LinkedHashMap<String, String>();
        _outMap = new LinkedHashMap<String, String>();

        _inMap.put("con_mode", conmodeLayout.getConmode());
        _inMap.put("con_info1", conmodeLayout.getConinfo1());
        _inMap.put("con_info2", conmodeLayout.getConinfo2());

        _inMap.put("proc_code", "E00019");
        _inMap.put("tr_code", tr_code);

        _inMap.put("tot_amt", editTotamt.getText().toString());
        _inMap.put("org_amt", editTotamt.getText().toString());

        if(tr_code.equals("0420")) {
            _inMap.put("otx_dt", editOtxdt.getText().toString());
            _inMap.put("app_no", editAppno.getText().toString());
            _inMap.put("app_no_point", editAppnopoint.getText().toString());
        }

        kaa.KCPCatQuery(_inMap, _outMap);

        String resResult;
        resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
        resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
        resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");

        Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();
    }
}
