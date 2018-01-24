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

public class CatCashCheckActivity extends Activity implements View.OnClickListener {

    ConmodeLayout conmodeLayout;

    private EditText editTotamt, editOilcreditdt;
    private EditText editSChkType, editSChkNo, editSChkId, editSChkAcc;
    private EditText editOtxdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_cash_check);

        init();
    }

    public void init() {
        conmodeLayout = new ConmodeLayout();
        conmodeLayout.InitLayout(this);

        Button btnCheck = (Button)findViewById(R.id.btn_check_cashCheck);
        btnCheck.setOnClickListener(this);

        editTotamt = (EditText)findViewById(R.id.edit_totamt_catcashcheck);
        editOilcreditdt = (EditText)findViewById(R.id.edit_checkdate_catcashcheck);
        editSChkType = (EditText)findViewById(R.id.edit_checktype_catcashcheck);
        editSChkNo = (EditText)findViewById(R.id.edit_checkno_catcashcheck);
        editSChkId = (EditText)findViewById(R.id.edit_checkid_catcashcheck);
        editSChkAcc = (EditText)findViewById(R.id.edit_checkacc_catcashcheck);
    }

    @Override
    public void onClick(View v) {
        CatCashCheck("E00021", "4100");
    }

    public void CatCashCheck(String proc_code, String tr_code) {
        KCPAndroidApi kaa = KCPFactory.getAndroidInstance();
        LinkedHashMap<String, String> _inMap, _outMap;
        String msData;

        _inMap = new LinkedHashMap<String, String>();
        _outMap = new LinkedHashMap<String, String>();

        if(editSChkType.getText().toString().equals("0")) {
            msData = editSChkType.getText().toString() + editSChkNo.getText().toString();
        } else {
            msData = editSChkType.getText().toString() + editSChkNo.getText().toString() + editSChkAcc.getText().toString();
        }

        _inMap.put("con_mode", conmodeLayout.getConmode());
        _inMap.put("con_info1", conmodeLayout.getConinfo1());
        _inMap.put("con_info2", conmodeLayout.getConinfo2());
        _inMap.put("proc_code", proc_code);
        _inMap.put("tr_code", tr_code);
        _inMap.put("ms_data", msData);
        _inMap.put("tot_amt", editTotamt.getText().toString());
        _inMap.put("oil_credit_dt", editOilcreditdt.getText().toString());

        kaa.KCPCatQuery(_inMap, _outMap);

        String resResult;

        resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
        resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
        resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");

        Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();
    }
}
