package com.example.kcpsecutest.cat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

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

public class CatEtcActivity extends Activity implements OnClickListener{
	ConmodeLayout conLayout;
	Button btnStoreInfo, btnLastTrade, btnSignData, btnRePrint, btnCatTot, btnHostTot, btnSignReq, btnMsdata, btnPasswd, btnIcPut;
	EditText editReqmsg1, editReqmsg2, editReqmsg3, editReqmsg4, editStdt, editEndt; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cat_etc);
		
		init();
	}
	
	public void init() {
		conLayout = new ConmodeLayout();
		conLayout.InitLayout(this);
		
		btnStoreInfo = (Button)findViewById(R.id.btn_storeinfo_catetc);
		btnLastTrade = (Button)findViewById(R.id.btn_lasttrade_catetc);
		btnSignData = (Button)findViewById(R.id.btn_signdata_catetc);
		btnRePrint = (Button)findViewById(R.id.btn_reprint_catetc);
		btnCatTot = (Button)findViewById(R.id.btn_cattot_catetc);
		btnHostTot = (Button)findViewById(R.id.btn_hosttot_catetc);
		btnSignReq = (Button)findViewById(R.id.btn_signreq_catetc);
		btnMsdata = (Button)findViewById(R.id.btn_msdata_catetc);
		btnPasswd = (Button)findViewById(R.id.btn_passwd_catetc);
		btnIcPut = (Button)findViewById(R.id.btn_icput_catetc);
		
		editReqmsg1 = (EditText)findViewById(R.id.edit_reqmsg1_catmember);
		editReqmsg2 = (EditText)findViewById(R.id.edit_reqmsg2_catmember);
		editReqmsg3 = (EditText)findViewById(R.id.edit_reqmsg3_catmember);
		editReqmsg4 = (EditText)findViewById(R.id.edit_reqmsg4_catmember);
		editStdt = (EditText)findViewById(R.id.edit_stdt_catmember);
		editEndt = (EditText)findViewById(R.id.edit_endt_catmember);
		
		editStdt.setText(getStartDate());
		editEndt.setText(getEndDate());
		
		btnStoreInfo.setOnClickListener(this);
		btnLastTrade.setOnClickListener(this);
		btnSignData.setOnClickListener(this);
		btnRePrint.setOnClickListener(this);
		btnCatTot.setOnClickListener(this);
		btnHostTot.setOnClickListener(this);
		btnSignReq.setOnClickListener(this);
		btnMsdata.setOnClickListener(this);
		btnPasswd.setOnClickListener(this);
		btnIcPut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		KCPAndroidApi kaa = KCPFactory.getAndroidInstance();
		LinkedHashMap<String, String> _inMap, _outMap;
		
		_inMap = new LinkedHashMap<String, String>();
		_outMap = new LinkedHashMap<String, String>();
		
		_inMap.put("con_mode", conLayout.getConmode());
		_inMap.put("con_info1", conLayout.getConinfo1());
		_inMap.put("con_info2", conLayout.getConinfo2());
		_inMap.put("tr_code", "0100");
		
		if(v.getId() == R.id.btn_storeinfo_catetc) {
			_inMap.put("proc_code", "E00001");
		} else if(v.getId() == R.id.btn_lasttrade_catetc) {
			_inMap.put("proc_code", "E00006");
		} else if(v.getId() == R.id.btn_signdata_catetc) {
			_inMap.put("proc_code", "E00007");
		} else if(v.getId() == R.id.btn_reprint_catetc) {
			_inMap.put("proc_code", "E00012");
		} else if(v.getId() == R.id.btn_cattot_catetc) {
			_inMap.put("proc_code", "E00011");
			_inMap.put("proc_code", "E00011");
			_inMap.put("sum_gbn1", "1");
			_inMap.put("sum_gbn2", "0");
			_inMap.put("sum_gbn3", "0");
			_inMap.put("sum_gbn4", "0");
			_inMap.put("sum_stdt", editStdt.getText().toString());
			_inMap.put("sum_eddt", editEndt.getText().toString());
		} else if(v.getId() == R.id.btn_hosttot_catetc) {
			_inMap.put("proc_code", "E00011");
			_inMap.put("sum_gbn1", "0");
			_inMap.put("sum_gbn2", "1");
			_inMap.put("sum_gbn3", "0");
			_inMap.put("sum_gbn4", "1");
			_inMap.put("sum_stdt", editStdt.getText().toString());
			_inMap.put("sum_eddt", editEndt.getText().toString());
		} else if(v.getId() == R.id.btn_signreq_catetc) {
			_inMap.put("proc_code", "E00008");
			_inMap.put("reqmsg1", editReqmsg1.getText().toString());
			_inMap.put("reqmsg2", editReqmsg2.getText().toString());
			_inMap.put("reqmsg3", editReqmsg3.getText().toString());
			_inMap.put("reqmsg4", editReqmsg4.getText().toString());
		} else if(v.getId() == R.id.btn_msdata_catetc) {
			_inMap.put("proc_code", "E00009");
			_inMap.put("reqmsg1", editReqmsg1.getText().toString());
			_inMap.put("reqmsg2", editReqmsg2.getText().toString());
			_inMap.put("reqmsg3", editReqmsg3.getText().toString());
			_inMap.put("reqmsg4", editReqmsg4.getText().toString());
		} else if(v.getId() == R.id.btn_passwd_catetc) {
			_inMap.put("proc_code", "E00009");
			_inMap.put("reqmsg1", editReqmsg1.getText().toString());
			_inMap.put("reqmsg2", editReqmsg2.getText().toString());
			_inMap.put("reqmsg3", editReqmsg3.getText().toString());
			_inMap.put("reqmsg4", editReqmsg4.getText().toString());
			_inMap.put("ms_data", "P");
		} else if(v.getId() == R.id.btn_icput_catetc) {
			_inMap.put("proc_code", "E00016");
		}
		
		kaa.KCPCatQuery(_inMap, _outMap);
		
		String resResult;
		resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
		resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
		resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");
		if(v.getId() == R.id.btn_storeinfo_catetc) {
			resResult += ("term_id	: " + _outMap.get("term_id") + "\n");
			resResult += ("tx_dt	: " + _outMap.get("tx_dt") + "\n");
			resResult += ("m_name	: " + _outMap.get("m_name") + "\n");
			resResult += ("o_name	: " + _outMap.get("o_name") + "\n");
			resResult += ("m_addr	: " + _outMap.get("m_addr") + "\n");
			resResult += ("mtelno	: " + _outMap.get("mtelno") + "\n");
		} else if(v.getId() == R.id.btn_lasttrade_catetc) {
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
			resResult += ("서명데이터	: " + _outMap.get("sign_img") + "\n");
		} else if(v.getId() == R.id.btn_signdata_catetc) {
			resResult += ("서명데이터	: " + _outMap.get("sign_img") + "\n");
		} else if(v.getId() == R.id.btn_signreq_catetc) {
			resResult += ("서명데이터	: " + _outMap.get("sign_img") + "\n");
		} else if(v.getId() == R.id.btn_msdata_catetc) {
			resResult += ("식별정보	: " + _outMap.get("sign_img") + "\n");
		} else if(v.getId() == R.id.btn_passwd_catetc) {
			resResult += ("패스워드	: " + _outMap.get("sign_img") + "\n");
		}
		Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();
	}
	public String getStartDate() {
		SimpleDateFormat date;
		String resDate;
		date  = new SimpleDateFormat("yyMMdd", Locale.KOREA);
		resDate = date.format(new Date());
		return resDate + "000000";
	}
	
	public String getEndDate() {
		SimpleDateFormat date;
		String resDate;
		date  = new SimpleDateFormat("yyMMddHHmmss", Locale.KOREA);
		resDate = date.format(new Date());
		return resDate;
	}
}
