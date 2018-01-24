package com.example.kcpsecutest.cat;

import java.util.LinkedHashMap;

import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.common.AmtoptLayout;
import com.example.kcpsecutest.common.ConmodeLayout;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CatCashActivity extends Activity implements OnClickListener{
	
	//EditText
    private EditText editMsdata, editPasswd1, editPasswd2;
    
    AmtoptLayout amtLayout;
	ConmodeLayout conLayout;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cat_cash);
		
		init();
	}
	
	public void init(){
        Button btnApp = (Button)findViewById(R.id.btn_auth_cat);
        btnApp.setOnClickListener(this);

        Button btnAppcan = (Button)findViewById(R.id.btn_void_cat);
        btnAppcan.setOnClickListener(this);
        
        conLayout = new ConmodeLayout();
		conLayout.InitLayout(this);
		
		amtLayout = new AmtoptLayout();
		amtLayout.InitLayout(this);
		
        editMsdata = (EditText)findViewById(R.id.edit_msdata_catcash);
        editPasswd1 = (EditText)findViewById(R.id.edit_passwd1_catcash);
        editPasswd2 = (EditText)findViewById(R.id.edit_passwd2_catcash);
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_auth_cat) {
			CatCreditAuth("0100");
		} else {
			CatCreditAuth("0420");
		}
	}
	
	public void CatCreditAuth(String tr_code) {
		KCPAndroidApi kaa = KCPFactory.getAndroidInstance();
		LinkedHashMap<String, String> _inMap, _outMap;



		_inMap = new LinkedHashMap<String, String>();
		_outMap = new LinkedHashMap<String, String>();
		
		_inMap.put("con_mode", conLayout.getConmode());
		_inMap.put("con_info1", conLayout.getConinfo1());
		_inMap.put("con_info2", conLayout.getConinfo2());

		_inMap.put("proc_code", "E00005");
		_inMap.put("tr_code", tr_code);
		

		_inMap.put("amtopt", amtLayout.getAmtopt());		
		_inMap.put("cpx_flag", amtLayout.getCpxflag());

		
		_inMap.put("ins_mon", amtLayout.getInsmon());
		
		_inMap.put("tot_amt", amtLayout.getTotamt());
		_inMap.put("org_amt", amtLayout.getOrgamt());
		_inMap.put("duty_amt", amtLayout.getDutyamt());
		_inMap.put("tax_amt", amtLayout.getTaxamt());
		_inMap.put("svc_amt", amtLayout.getSvcamt());
		
		if(tr_code.equals("0420")) {
			_inMap.put("app_no", amtLayout.getAppno());
			_inMap.put("otx_dt", amtLayout.getOtxdt());
		}
		
		_inMap.put("signopt", "N");
		_inMap.put("prt_cd", "1");
		_inMap.put("prtopt", "1");
		_inMap.put("prtmsg1", "이용해주셔서");
		_inMap.put("prtmsg2", "감사합니다.");
		
		if(tr_code.equals("0100")) 
			_inMap.put("pass_wd", editPasswd1.getText().toString() + "0");
		else
			_inMap.put("pass_wd", editPasswd1.getText().toString() + editPasswd2.getText().toString());
		
		if(editMsdata.length() > 1) {
			_inMap.put("ms_data", editMsdata.getText().toString());
			_inMap.put("tx_type", "10");
		}
			
		kaa.KCPCatQuery(_inMap, _outMap);
		
		String resResult;
		resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
		resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
		resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");
		if(_outMap.get("res_cd").toString().equals("0000")) {
			amtLayout.setAppno(_outMap.get("app_no").toString());
			amtLayout.setOtxdt(_outMap.get("tx_dt").toString().substring(0, 6));
			
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
			resResult += ("포인트정보	: " + _outMap.get("point_info") + "\n");
			resResult += ("자동이체코드	: " + _outMap.get("at_type") + "\n");
			resResult += ("서명데이터	: " + _outMap.get("sign_img") + "\n");
		}
		
		Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();
	}
}
