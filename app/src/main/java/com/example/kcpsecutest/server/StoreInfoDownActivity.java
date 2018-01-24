package com.example.kcpsecutest.server;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kcpsecutest.R;

import java.util.LinkedHashMap;

import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

public class StoreInfoDownActivity extends Activity implements OnClickListener{
	EditText editBusicd;
    EditText editBusino;
    EditText editTermid;
    EditText editAraeno;
 
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);

        init();

    }

    private void init(){
        Button btnOk = (Button)findViewById(R.id.btn_ok_store);
        btnOk.setOnClickListener(this);

        Button btnCan = (Button)findViewById(R.id.btn_cancel_store);
        btnCan.setOnClickListener(this);

        editBusicd = (EditText)findViewById(R.id.edit_busicd_store);
        editBusino = (EditText)findViewById(R.id.edit_busino_store);
        editTermid = (EditText)findViewById(R.id.edit_termid_store);
        editAraeno = (EditText)findViewById(R.id.edit_areano_store);
    }
    
	@Override
	public void onClick(View v) { //TODO: down UI
		if(v.getId() == R.id.btn_ok_store){
            if(editBusicd.length() != 4){
                Toast.makeText(this,"업체코드 재입력",Toast.LENGTH_SHORT).show();
                editBusicd.setText("");
            }else if(editBusino.length() != 10){
                Toast.makeText(this,"사업자번호 재입력",Toast.LENGTH_SHORT).show();
                editBusino.setText("");
            }else if(editTermid.length() != 10){
                Toast.makeText(this,"단말기ID 재입력",Toast.LENGTH_SHORT).show();
                editTermid.setText("");
            }else if(editAraeno.length() > 4){
                Toast.makeText(this,"지역번호 재입력",Toast.LENGTH_SHORT).show();
                editAraeno.setText("");
            }else{
            	//TODO : IFM manager request
//				reqIFM();
            	Download();
            }
        } else if(v.getId() == R.id.btn_cancel_store){
            finish();
        }
		
	}
	
	//IFM SDK 호출
/*
	IFMManager ifmManager;
	public void reqIFM( ){
		ifmManager = new IFMManager();
		ifmManager.initController(this, recvCallback);
		//TODO : IFM manager  ON
		ifmManager.powerOnRdi();
//		ifmManager.Req_IFM(); //TrdType
	}

*/

/*

	IFMControllerInterface.OnRecvCallback recvCallback = new IFMControllerInterface.OnRecvCallback() {
		@Override
		public void onCallback(int callbackMsg, byte[] recvBuf, LinkedHashMap<String, String> linkedHashMap) {
//          TEST -callback
			Log.i("ifm_test", "onCallback"+callbackMsg);
			if( recvBuf== null){
				Log.i("ifm_test", "onCallback == null");
			}else{
				Log.i("ifm_test", "onCallback != null");
				Utils.debugTx("ifm_test", recvBuf, recvBuf.length);

				Download(linkedHashMap);
			}
		}
	};

*/


//	public void Download(LinkedHashMap<String, String> linkedHashMap)
	public void Download()
	{
		KCPAndroidApi kja = KCPFactory.getAndroidInstance();    
		LinkedHashMap<String, String> _inMap, _outMap;

		_inMap = new LinkedHashMap<String, String>();
		_outMap = new LinkedHashMap<String, String>();
		
		String	ver=kja.KCPApiVersion();
		
//		_inMap.put("con_mode", Util.getNetwork(this));
		_inMap.put("con_mode", "TEST_LAN");
		_inMap.put("proc_code", "D01000");
		_inMap.put("tr_code", "0100");
		_inMap.put("busi_cd", editBusicd.getText().toString());
		_inMap.put("reader_sn", "1000000000");
		_inMap.put("pos_sn", "POS1234567890");
		_inMap.put("reader_cert", "#DUALPAY633C");
		_inMap.put("reader_ver", "C101");
		_inMap.put("sw_cert", "###SUPER-POS");
		_inMap.put("sw_ver", "V100");
		_inMap.put("term_id", editTermid.getText().toString());
		_inMap.put("trace_no", "1");
		_inMap.put("signpad_yn", "N");

		_inMap.put("busi_no", editBusino.getText().toString());
		_inMap.put("area_no", editAraeno.getText().toString());

		//TODO: 리더기로 부터 받은 응답 넣어줌
//		_inMap.put(key,linkedHashMap.get(key));
//		_inMap.put(key,linkedHashMap.get(key));
//		_inMap.put(key,linkedHashMap.get(key));


		kja.KCPServerQuery(_inMap, _outMap); //VAN 요청

		String resBuff = "응답코드	: " + _outMap.get("res_cd").toString() + "\n";
		resBuff += "응답메시지1	: " + _outMap.get("resmsg1").toString() + "\n";
		resBuff += "응답메시지2	: " + _outMap.get("resmsg2").toString() + "\n";
		
		if(_outMap.get("res_cd").toString().equals("0000")) {
			resBuff += "다운로드날짜	: " + _outMap.get("tx_dt").toString() + "\n";
			resBuff += "대표자명	: " + _outMap.get("o_name").toString() + "\n";
			resBuff += "가맹점명	: " + _outMap.get("m_name").toString() + "\n";
			resBuff += "가맹점 주소	: " + _outMap.get("m_addr").toString() + "\n";
			resBuff += "가맹점전화번호 : " + _outMap.get("mtelno").toString() + "\n";
			resBuff += "콜센터		: " + _outMap.get("ctelno").toString() + "\n";
		}
		Toast.makeText(this, resBuff, Toast.LENGTH_SHORT).show();

		//TODO : IFM manager  OFF
/*
		ifmManager.powerDownRdi();
*/
	}
}
