package com.example.kcpsecutest.server;

import java.util.LinkedHashMap;

import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.R.id;
import com.example.kcpsecutest.R.layout;
import com.example.kcpsecutest.common.CardInfoLayout;
import com.example.kcpsecutest.common.Util;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PointActivity extends Activity implements OnClickListener {
	private CardInfoLayout cardLayout;
    private EditText editTotamt;
    private EditText editAppDay;
    private EditText editAppno;
    private EditText editPointType;
    private RadioGroup rgPointCode;
    private EditText editConmode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        init();
    }

    public void init(){

        Button btnApp = (Button)findViewById(R.id.btn_poapp_point);
        btnApp.setOnClickListener(this);

        Button btnAppcan = (Button)findViewById(R.id.btn_pocan_point);
        btnAppcan.setOnClickListener(this);

        Button btnCheck = (Button)findViewById(R.id.btn_pocheck_point);
        btnCheck.setOnClickListener(this);

        
        cardLayout = new CardInfoLayout();
		cardLayout.InitLayout(this);
		
		editConmode = (EditText)findViewById(R.id.edit_conmode_point);
		editConmode.setText(Util.getNetwork(this));
		
        editTotamt = (EditText)findViewById(R.id.edit_totamt_point);
        editAppDay = (EditText)findViewById(R.id.edit_appday_point);
        editAppno = (EditText)findViewById(R.id.edit_appno_point);
        editPointType = (EditText)findViewById(R.id.edit_potype_point);
        
        rgPointCode = (RadioGroup)findViewById(R.id.rg_pocode_point);

    }

    @Override
    public void onClick(View v) {
    	
        if(v.getId() == R.id.btn_poapp_point){
        	KCP_AUTH_POINT(1);
        }else if(v.getId() == R.id.btn_pocan_point){
        	KCP_AUTH_POINT(2);
        }else if(v.getId() == R.id.btn_pocheck_point){
        	KCP_AUTH_POINT(3);
        }
    }
    
    public String getTr_code(int mode) {
    	if(mode == 1 && rgPointCode.getCheckedRadioButtonId() == R.id.rbtn_save_point) {
    		return "1100";
    	} else if(mode == 1 && rgPointCode.getCheckedRadioButtonId() == R.id.rbtn_use_point) {
    		return "2100";
    	} else if(mode == 1 && rgPointCode.getCheckedRadioButtonId() == R.id.rbtn_disc_point) {
    		return "3100";
    	} else if(mode == 2 && rgPointCode.getCheckedRadioButtonId() == R.id.rbtn_save_point) {
    		return "1420";
    	} else if(mode == 2 && rgPointCode.getCheckedRadioButtonId() == R.id.rbtn_use_point) {
    		return "2420";
    	} else if(mode == 2 && rgPointCode.getCheckedRadioButtonId() == R.id.rbtn_disc_point) {
    		return "3420";
    	} else {
    		return "4100";
    	}  
    }

    public void KCP_AUTH_POINT(int mode){
    	KCPAndroidApi kja = KCPFactory.getAndroidInstance();
		LinkedHashMap<String, String> _inMap, _outMap;
		_inMap = new LinkedHashMap<String, String>();
		_outMap = new LinkedHashMap<String, String>();
		//byte[] point_type = new byte[2];
		//String tx_type = null;
		String tr_code = getTr_code(mode);
		
		_inMap.put("con_mode", editConmode.getText().toString());
		_inMap.put("proc_code", "M01000");
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
		
		_inMap.put("tx_type", cardLayout.getTxtype());
		
		
		_inMap.put("ksn", cardLayout.getKsn());
		_inMap.put("ms_data", cardLayout.getMsdata());
		
		_inMap.put("ins_mon", editPointType.getText().toString());
		
		if(tr_code.equals("4100") == false){
			int tot_amt = Integer.parseInt(editTotamt.getText().toString());
			int org_amt = (int) ((int)tot_amt/1.1);
			int tax_amt = tot_amt - org_amt;
			_inMap.put("tot_amt", Integer.toString(tot_amt));
			_inMap.put("org_amt", Integer.toString(org_amt));
			_inMap.put("tax_amt", Integer.toString(tax_amt));
			
			_inMap.put("otx_dt", editAppDay.getText().toString());
			_inMap.put("app_no", editAppno.getText().toString());
		}

		kja.KCPServerQuery(_inMap, _outMap);
		
		String resResult;
		resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
		resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
		resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");
		
		if(_outMap.get("res_cd").toString().equals("0000")) {
			editAppno.setText(_outMap.get("app_no").toString());
			editAppDay.setText(_outMap.get("tx_dt").toString().substring(0, 6));
			
			resResult += ("승인날짜	: " + _outMap.get("tx_dt") + "\n");
			resResult += ("거래고유번호	: " + _outMap.get("tx_no") + "\n");
			resResult += ("승인번호	: " + _outMap.get("app_no") + "\n");
			resResult += ("카드구분	: " + _outMap.get("card_gbn") + "\n");
			resResult += ("프린트코드	: " + _outMap.get("prt_cd") + "\n");
			resResult += ("포인트정보	: " + _outMap.get("point_amt") + "\n");
		}
        
		Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();

    }
}
