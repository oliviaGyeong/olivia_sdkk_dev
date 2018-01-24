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

public class CatPrintActivity extends Activity implements OnClickListener{
	ConmodeLayout conLayout;
	Button btnInit, btnMessage, btnBarcode, btnQrcode, btnBitmap, btnLinefeed, btnCut, btnCashdrawer;
	EditText editMessage, editOption, editPos, editSize, editQuality, editLinefeed, editPath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cat_print);
		
		init();
	}
	
	public void init() {
		conLayout = new ConmodeLayout();
		conLayout.InitLayout(this);
		
		btnInit = (Button)findViewById(R.id.btn_init_catprint);
		btnMessage = (Button)findViewById(R.id.btn_message_catprint);
		btnBarcode = (Button)findViewById(R.id.btn_barcode_catprint);
		btnQrcode = (Button)findViewById(R.id.btn_qrcode_catprint);
		btnBitmap = (Button)findViewById(R.id.btn_bitmap_catprint);
		btnLinefeed = (Button)findViewById(R.id.btn_linefeed_catprint);
		btnCut = (Button)findViewById(R.id.btn_cut_catprint);
		btnCashdrawer = (Button)findViewById(R.id.btn_cashdrawer_catprint);
		
		editMessage = (EditText)findViewById(R.id.edit_message_catprint);
		editOption = (EditText)findViewById(R.id.edit_option_catprint);
		editPos = (EditText)findViewById(R.id.edit_pos_catprint);
		editSize = (EditText)findViewById(R.id.edit_size_catprint);
		editQuality = (EditText)findViewById(R.id.edit_quality_catprint);
		editLinefeed = (EditText)findViewById(R.id.edit_linefeed_catprint);
		editPath = (EditText)findViewById(R.id.edit_path_catprint);
		
		btnInit.setOnClickListener(this);
		btnMessage.setOnClickListener(this);
		btnBarcode.setOnClickListener(this);
		btnQrcode.setOnClickListener(this);
		btnBitmap.setOnClickListener(this);
		btnLinefeed.setOnClickListener(this);
		btnCut.setOnClickListener(this);
		btnCashdrawer.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		KCPAndroidApi kaa = KCPFactory.getAndroidInstance();
		LinkedHashMap<String, String> _inMap, _outMap;
		String proc_code = null;
		String tr_code = "0100";
		
		if(v.getId() == R.id.btn_init_catprint) {
			proc_code = "P00000";
		} else if(v.getId() == R.id.btn_message_catprint) {
			proc_code = "P00001";
		} else if(v.getId() == R.id.btn_barcode_catprint) {
			proc_code = "P00002";
		} else if(v.getId() == R.id.btn_qrcode_catprint) {
			proc_code = "P00003";
		} else if(v.getId() == R.id.btn_bitmap_catprint) {
			proc_code = "P00004";
		} else if(v.getId() == R.id.btn_linefeed_catprint) {
			proc_code = "P00005";
		} else if(v.getId() == R.id.btn_cut_catprint) {
			proc_code = "P00006";
		} else if(v.getId() == R.id.btn_cashdrawer_catprint) {
			proc_code = "P00007";
		}
		
		_inMap = new LinkedHashMap<String, String>();
		_outMap = new LinkedHashMap<String, String>();
		
		_inMap.put("con_mode", conLayout.getConmode());

		_inMap.put("con_info1", conLayout.getConinfo1());
		_inMap.put("con_info2", conLayout.getConinfo2());

		_inMap.put("proc_code", proc_code);
		_inMap.put("tr_code", tr_code);
		
		_inMap.put("prt_msg", editMessage.getText().toString());
		_inMap.put("prtopt", editOption.getText().toString());
		_inMap.put("prt_pos", editPos.getText().toString());
		_inMap.put("prt_size", editSize.getText().toString());
		_inMap.put("prt_quality", editQuality.getText().toString());
		_inMap.put("prt_line", editLinefeed.getText().toString());
		_inMap.put("prt_path", editPath.getText().toString());
		
		kaa.KCPCatQuery(_inMap, _outMap);
		
		String resResult;
		resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
		resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
		resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");
		
		Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();
	}
}
