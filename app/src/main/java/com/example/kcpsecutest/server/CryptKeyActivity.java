package com.example.kcpsecutest.server;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.common.Util;

import java.util.LinkedHashMap;

import device.sdk.ifmmanager.ICReaderPacketManager;
import device.sdk.ifmmanager.IFMControllerInterface;
import device.sdk.ifmmanager.IFMManager;
import device.sdk.ifmmanager.Utils;
import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

public class CryptKeyActivity extends Activity implements OnClickListener{
	EditText editBusino;
	EditText editPublicKey;
	EditText editEnc;
	EditText editKsn;
	
	Button	btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crypt_key);
		
		btn = (Button)findViewById(R.id.btn_key_cert);
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplication(), "clicked button.", Toast.LENGTH_LONG).show();
			}
		});
		
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crypt_key, menu);
		return true;
	}
	
	public void init() {
		editBusino		= (EditText)findViewById(R.id.edit_busino_key);
		editPublicKey	= (EditText)findViewById(R.id.edit_public_key);
		editEnc			= (EditText)findViewById(R.id.edit_input_enc);
		editKsn			= (EditText)findViewById(R.id.edit_ksn);
		
		Button	btnKeyCert	=(Button)findViewById(R.id.btn_key_cert);
		Button	btnKey		=(Button)findViewById(R.id.btn_key);
		
		btnKeyCert.setOnClickListener(this);
        btnKey.setOnClickListener(this);

        /**< IFM을 구동하기 위한 필요 조건 :: IFM power on */
		ifmManager = new IFMManager();
		ifmManager.initController(this, recvCallback);
		ifmManager.powerOnRdi();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_key_cert) {
			/**< 각 packet 별로 timeout 설정을 달리 하기위해 만든 method :
			 *  random number generate 의 시간이 많이 소요되는 경우에 대한 대비 */
			ifmManager.reqIFM(null,TRD_TYPE_REQ_MUTUAL_AUTHENTICATION);// (byte)0xA3;   //상호인증 요청
//			KeyCert();
		} else if(v.getId() == R.id.btn_key) {
//			Key();

		}
	}

	/**< IFM Manager ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
//	public static final byte TRD_TYPE_REQ_INIT  				= (byte)0xA0;   // 초기화 요청
//	public static final byte TRD_TYPE_REQ_SELF_PROTECTION		= (byte)0xA1;   // 무결성 검증 요청
//	public static final byte TRD_TYPE_REQ_CURRENT_TIME			= (byte)0xA2;   // 시간전송 요청
	public static final byte TRD_TYPE_REQ_MUTUAL_AUTHENTICATION	= (byte)0xA3;	// 상호인증 요청
	public static final byte TRD_TYPE_REQ_ENCRRYPTION_STATE		= (byte)0xA4;   // 암호화키 D/N 상태요청
	public static final byte TRD_TYPE_REQ_ENCRRYPTION_COMPLETE	= (byte)0xA5;   // 암호화키 D/N 완료요청
//	public static final byte TRD_TYPE_REQ_GENERAL_TRANSACTION	= (byte)0xA6;   // 일반거래요청
//	public static final byte TRD_TYPE_REQ_IC_CARD_READ			= (byte)0xA7;   // IC 카드읽기 요청
//	public static final byte TRD_TYPE_REQ_IC_CARD_DATA			= (byte)0xA8;   // IC 카드 데이터 요청
//	public static final byte TRD_TYPE_REQ_IC_CARD_ISSUER		= (byte)0xA9;   // IC 카드 발급사 요청
//	public static final byte TRD_TYPE_REQ_CASH_IC_READ			= (byte)0x1A;   // 현금IC 읽기 요청
//	public static final byte TRD_TYPE_REQ_CASH_IC_DATA			= (byte)0x2A;   // 현금IC 데이터 요청
//	public static final byte TRD_TYPE_REQ_CASH_IC_ACCOUNT_INFO	= (byte)0x3A;   // 현금IC 계좌정보 요청
//	public static final byte TRD_TYPE_REQ_IS_INPUT_READER		= (byte)0x4A;   // 리더기 카드 삽입여부 요청

	/**< IFM SDK 호출 */
	IFMManager						ifmManager;
	LinkedHashMap<String, String>	mLinkedHashMap; //IFM 응답 hashmap

	IFMControllerInterface.OnRecvCallback recvCallback = new IFMControllerInterface.OnRecvCallback() {
		@Override
		public void onCallback(int callbackMsg, byte[] recvBuf, LinkedHashMap<String, String> linkedHashMap) {

//          TEST -callback
			Log.i("ifm_test", "callbackMsg "+callbackMsg);
			if( recvBuf== null){
				Log.i("ifm_test", "Receiving buffer to receive from the IFM is null.");
			}
			else
			{
				Log.i("ifm_test", "onCallback != null");
				
				Utils.debugTx("ifm_test", recvBuf, recvBuf.length);

				switch (getRecvTrdType(recvBuf))
				{
					case TRD_TYPE_REQ_MUTUAL_AUTHENTICATION://상호인증 응답
						KeyCert(linkedHashMap);
						break;
					case TRD_TYPE_REQ_ENCRRYPTION_STATE:	// 암호화키 D/N 상태응답
						Key(linkedHashMap);
						break;
					case TRD_TYPE_REQ_ENCRRYPTION_COMPLETE:	// 암호화키 D/N 완료응답
						//TODO : IFM manager  OFF
						ifmManager.powerDownRdi();
						break;
				}

				mLinkedHashMap = linkedHashMap;
			}
		}
	};

	public static byte getRecvTrdType(byte[] data)
	{
		if (Utils.DEBUG_INFO)
			Log.i("ifm_test", "getRecvTrdType() +-");
		
		byte[]	trdTypeBuf=new byte[ICReaderPacketManager.RdiFormat.COMMANDID];

		int		strPos=ICReaderPacketManager.RdiFormat.STX +
					   ICReaderPacketManager.RdiFormat.DATA_LENGTH;

		System.arraycopy(data, strPos , trdTypeBuf, 0, trdTypeBuf.length); //resultCode
		
		byte	trdType = trdTypeBuf[0];
		
		return trdType;
	}
/**< IFM Manager -----------------------------------------------------------------------------------*/
	
	public void KeyCert(LinkedHashMap<String, String> mLinkedHashMap)
	{
		KCPAndroidApi kja = KCPFactory.getAndroidInstance();
		LinkedHashMap<String, String> _inMap, _outMap;
		
		/**< 각 packet 별로 timeout 설정을 달리 하기위해 만든 method :
		 *  random number generate 의 시간이 많이 소요되는 경우에 대한 대비 */
//		ifmManager.Req_IFMex(null,TRD_TYPE_REQ_MUTUAL_AUTHENTICATION, Utils.TIMEOUT_7SEC);
		
		_inMap = new LinkedHashMap<String, String>();
		_outMap = new LinkedHashMap<String, String>();
		
		_inMap.put("con_mode", Util.getNetwork(this));
		_inMap.put("proc_code", "D03000");
		_inMap.put("tr_code", "0100");
		_inMap.put("busi_cd", "TEST");
		_inMap.put("reader_sn", "1000000000");     //from IFM
		_inMap.put("pos_sn", "POS1234567890");
		_inMap.put("reader_cert", "#DUALPAY633C"); //from IFM
		_inMap.put("reader_ver", "C101");
		_inMap.put("sw_cert", "###SUPER-POS");
		_inMap.put("sw_ver", "V100");
		_inMap.put("term_id", "1002189855");
		_inMap.put("trace_no", "1");
		_inMap.put("signpad_yn", "N");
		
		_inMap.put("busi_no", editBusino.getText().toString());
//		_inMap.put("public_key_ver", editPublicKey.getText().toString());
		
		/**< set the key-version from the IFM */
		_inMap.put("public_key_ver", mLinkedHashMap.get("public_key_ver"));

/* check code ::
		String	pubKeyVer = _inMap.get("public_key_ver").toString();
		Log.v("public_key_ver", pubKeyVer);
*/
		////////////////////////////////////
		kja.KCPServerQuery(_inMap, _outMap);
		////////////////////////////////////

		String	res_cd	= _outMap.get("res_cd").toString();
		String	resmsg1 = _outMap.get("resmsg1").toString();
		String	resmsg2 = _outMap.get("resmsg2").toString();
		
		String resBuff = "응답코드	: " + res_cd + "\n";
		resBuff += "응답메시지1	: " + resmsg1 + "\n";
		resBuff += "응답메시지2	: " + resmsg2 + "\n";
		
		if(res_cd.equals("0000")) {
			resBuff += "random	: " + _outMap.get("random").toString() + "\n";
			resBuff += "hash	: " + _outMap.get("hash").toString() + "\n";
			resBuff += "SingValue : " + _outMap.get("sign_value").toString() + "\n";
			
			//TODO: should have to request to IFM
			///////////////////////////////////////////////////////////
			ifmManager.reqIFM(_outMap,TRD_TYPE_REQ_ENCRRYPTION_STATE); //(byte)0xA4;       // 암호화키 D/N 상태요청
			///////////////////////////////////////////////////////////
		}
		
		Log.v("MSG 1",""+resBuff);
		Toast.makeText(this, resBuff, Toast.LENGTH_LONG).show();
	}
	
	public void Key(LinkedHashMap<String, String> mLinkedHashMap)
	{
		KCPAndroidApi kja = KCPFactory.getAndroidInstance();
		LinkedHashMap<String, String> _inMap, _outMap;
	
		_inMap = new LinkedHashMap<String, String>();
		_outMap = new LinkedHashMap<String, String>();
		
		_inMap.put("con_mode", Util.getNetwork(this));
		_inMap.put("proc_code", "D04000");
		_inMap.put("tr_code", "0100");
		_inMap.put("busi_cd", "TEST");
		_inMap.put("reader_sn", "1000000000");
		_inMap.put("pos_sn", "POS1234567890");
		_inMap.put("reader_cert", "#DUALPAY633C");
		_inMap.put("reader_ver", "C101");
		_inMap.put("sw_cert", "###SUPER-POS");
		_inMap.put("sw_ver", "V100");
		_inMap.put("term_id", "1002189855");
		_inMap.put("trace_no", "1");
		_inMap.put("signpad_yn", "N");

		/**< IFM에서 읽은 data의 처리 */
		_inMap.put("busi_no", editBusino.getText().toString());
		_inMap.put("public_key_ver", editPublicKey.getText().toString());
		_inMap.put("input_enc", editEnc.getText().toString());
		_inMap.put("ksn", editKsn.getText().toString());

		//TODO: 리더기로 부터 받은 응답 넣어줌
		_inMap.put("public_key_ver", mLinkedHashMap.get("public_key_ver"));
		_inMap.put("input_enc", mLinkedHashMap.get("input_enc"));
		_inMap.put("ksn", mLinkedHashMap.get("ksn"));

		////////////////////////////////////
		kja.KCPServerQuery(_inMap, _outMap);
		////////////////////////////////////

		String	res_cd  = _outMap.get("res_cd").toString();
		String	resmsg1 = _outMap.get("resmsg1").toString();
		String	resmsg2 = _outMap.get("resmsg2").toString();
		
		String resBuff = "응답코드	: " + res_cd + "\n";
		resBuff += "응답메시지1	: " + resmsg1 + "\n";
		resBuff += "응답메시지2	: " + resmsg2 + "\n";
		
		if(res_cd.equals("0000")) {
			resBuff += "enc_key	: " + _outMap.get("enc_key").toString() + "\n";
			//TODO: 리더기 요청
			ifmManager.reqIFM(_outMap,TRD_TYPE_REQ_ENCRRYPTION_COMPLETE);//(byte)0xA5;    // 암호화키 D/N 완료요청
		}

		/**< 리더기에 전송   enc_key 암호화 키 */
		Toast.makeText(this, resBuff, Toast.LENGTH_SHORT).show();
		Log.v("MSG",""+resBuff);
	}

}
