package com.example.kcpsecutest.cat;

import java.util.LinkedHashMap;

import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.common.ConmodeLayout;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CatPointActivity extends Activity implements OnClickListener{

	ConmodeLayout conLayout;
	Button btnAuth, btnVoid;
	EditText editTotamt, editInsmon, editPasswd, editTrcode, editOtxdt, editAppno;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cat_point);
		
		init();
	}

	public void init() {
		conLayout = new ConmodeLayout();
		conLayout.InitLayout(this);
		
		btnAuth = (Button)findViewById(R.id.btn_auth_cat);
		btnVoid = (Button)findViewById(R.id.btn_void_cat);
		
		btnAuth.setOnClickListener(this);
		btnVoid.setOnClickListener(this);
		
		editTotamt = (EditText)findViewById(R.id.edit_totamt_catpoint);
		editInsmon = (EditText)findViewById(R.id.edit_insmon_catpoint);
		editPasswd = (EditText)findViewById(R.id.edit_passwd_catpoint);
		editTrcode = (EditText)findViewById(R.id.edit_trcode_catpoint);
		editOtxdt = (EditText)findViewById(R.id.edit_otxdt_catpoint);
		editAppno = (EditText)findViewById(R.id.edit_appno_catpoint);
	}

	@Override
	public void onClick(View v) {
		String tr_code = editTrcode.getText().toString();
		if(v.getId() == R.id.btn_auth_cat) {
			if(tr_code.equals("0")) {
				tr_code = "4100";
			} else if(tr_code.equals("1")) {
				tr_code = "1100";
			} else if(tr_code.equals("2")) {
				tr_code = "2100";
			} else if(tr_code.equals("3")) {
				tr_code = "3100";
			}
		} else {
			if(tr_code.equals("0")) {
				tr_code = "4100";
			} else if(tr_code.equals("1")) {
				tr_code = "1420";
			} else if(tr_code.equals("2")) {
				tr_code = "2420";
			} else if(tr_code.equals("3")) {
				tr_code = "3420";
			}
		}
		if(tr_code.length() == 4)
			CatPoint(tr_code);
		else
			Toast.makeText(this, "거래구분 확인요망", Toast.LENGTH_SHORT).show();
	}
	
	public void CatPoint(String tr_code) {
		KCPAndroidApi kaa = KCPFactory.getAndroidInstance();
		LinkedHashMap<String, String> _inMap, _outMap;
		
		_inMap = new LinkedHashMap<String, String>();
		_outMap = new LinkedHashMap<String, String>();
		
		_inMap.put("con_mode", conLayout.getConmode());
		_inMap.put("con_info1", conLayout.getConinfo1());
		_inMap.put("con_info2", conLayout.getConinfo2());

		_inMap.put("proc_code", "E00014");
		_inMap.put("tr_code", tr_code);
		
		_inMap.put("tot_mat", editTotamt.getText().toString());
		_inMap.put("org_amt", editTotamt.getText().toString());
		
		_inMap.put("ins_mon", editInsmon.getText().toString());
		_inMap.put("pass_wd", editPasswd.getText().toString());
		
		if(tr_code.substring(1,4).equals("420")) {
			_inMap.put("app_no", editAppno.getText().toString());
			_inMap.put("otx_dt", editOtxdt.getText().toString());
		} 
		
		_inMap.put("signopt", "N");
		_inMap.put("prt_cd", "1");
		_inMap.put("prtopt", "1");
		_inMap.put("prtmsg1", "이용해주셔서");
		_inMap.put("prtmsg2", "감사합니다.");
		
		kaa.KCPCatQuery(_inMap, _outMap);
		
		String resResult;
		resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
		resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
		resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");
		if(_outMap.get("res_cd").toString().equals("0000")) {
			editTotamt.setText(_outMap.get("app_no").toString());
			editTotamt.setText(_outMap.get("tx_dt").toString().substring(0, 6));
			
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
