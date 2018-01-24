package com.example.kcpsecutest.server;

import java.util.LinkedHashMap;


import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.common.AmtoptLayout;
import com.example.kcpsecutest.common.CardInfoLayout;
import com.example.kcpsecutest.common.Util;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CashActivity extends Activity implements View.OnClickListener {

	private CardInfoLayout cardLayout;
	private AmtoptLayout amtLayout;

	private EditText editConmode;
	
    private RadioGroup rgDivision;
    private RadioGroup rgVoidtype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        init();
    }
    public void init(){
        Button btnAuth = (Button)findViewById(R.id.btn_auth_cash);
        btnAuth.setOnClickListener(this);

        Button btnVoid = (Button)findViewById(R.id.btn_void_cash);
        btnVoid.setOnClickListener(this);

        cardLayout = new CardInfoLayout();
		cardLayout.InitLayout(this);
		
		amtLayout = new AmtoptLayout();
		amtLayout.InitLayout(this);
		
		editConmode = (EditText)findViewById(R.id.edit_conmode_cash);
		editConmode.setText(Util.getNetwork(this));
        
        rgDivision = (RadioGroup)findViewById(R.id.rg_division_cash);
        rgVoidtype = (RadioGroup)findViewById(R.id.rg_void_cash);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_auth_cash){
            KCP_AUTH_CASH("0100"); // 승인
        }else if(v.getId() == R.id.btn_void_cash){
            KCP_AUTH_CASH("0420"); // 취소
        }
    }

	public void KCP_AUTH_CASH(String tr_code) {
		KCPAndroidApi kja = KCPFactory.getAndroidInstance();
		LinkedHashMap<String, String> _inMap, _outMap;
		_inMap = new LinkedHashMap<String, String>();
		_outMap = new LinkedHashMap<String, String>();
		//String tx_type = null;
		
		byte[] cashType = new byte[2];
		if(rgDivision.getCheckedRadioButtonId() == R.id.rbtn_one_cash) {
			cashType[0] = '0';
		} else if(rgDivision.getCheckedRadioButtonId() == R.id.rbtn_two_cash) {
			cashType[0] = '1';
		}
		
		if(tr_code.equals("0100"))
			cashType[1] = '0';
		else {
			if(rgVoidtype.getCheckedRadioButtonId() == R.id.rbtn_dealcancel_cash) {
				cashType[1] = '1';
			} else if(rgVoidtype.getCheckedRadioButtonId() == R.id.rbtn_errorcancel_cash) {
				cashType[1] = '2';
			} else if(rgVoidtype.getCheckedRadioButtonId() == R.id.rbtn_ordercancel_cash) {
				cashType[1] = '3';
			}
		}
		
		
		_inMap.put("con_mode", editConmode.getText().toString());
		_inMap.put("proc_code", "C01000");
		_inMap.put("tr_code", tr_code);
		
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
		
		_inMap.put("tx_type", cardLayout.getTxtype());
		_inMap.put("ksn", cardLayout.getKsn());
		_inMap.put("ms_data", cardLayout.getMsdata());
		
		_inMap.put("pass_wd", new String(cashType));
		
		_inMap.put("ins_mon", amtLayout.getInsmon());
		_inMap.put("tot_amt", amtLayout.getTotamt());
		_inMap.put("org_amt", amtLayout.getOrgamt());
		_inMap.put("duty_amt", amtLayout.getDutyamt());
		_inMap.put("tax_amt", amtLayout.getTaxamt());
		_inMap.put("otx_dt", amtLayout.getOtxdt());
		_inMap.put("app_no", amtLayout.getAppno());

		kja.KCPServerQuery(_inMap, _outMap);
		
		String res_cd = _outMap.get("res_cd").toString();
		String resmsg1 = _outMap.get("resmsg1").toString();
		String resmsg2 = _outMap.get("resmsg2").toString();
		
		String resBuff = "응답코드	: " + res_cd + "\n";
		resBuff += "응답메시지1	: " + resmsg1 + "\n";
		resBuff += "응답메시지2	: " + resmsg2 + "\n";
		
		if(_outMap.get("res_cd").toString().equals("0000")) {
			amtLayout.setAppno(_outMap.get("app_no").toString());
			amtLayout.setOtxdt(_outMap.get("tx_dt").toString().substring(0, 6));
			
			resBuff += ("승인날짜	: " + _outMap.get("tx_dt") + "\n");
			resBuff += ("거래고유번호	: " + _outMap.get("tx_no") + "\n");
			resBuff += ("승인번호	: " + _outMap.get("app_no") + "\n");
			resBuff += ("카드구분	: " + _outMap.get("card_gbn") + "\n");
			resBuff += ("프린트코드	: " + _outMap.get("prt_cd") + "\n");
		}
        
		Toast.makeText(this, resBuff, Toast.LENGTH_SHORT).show();
	}
	
}
