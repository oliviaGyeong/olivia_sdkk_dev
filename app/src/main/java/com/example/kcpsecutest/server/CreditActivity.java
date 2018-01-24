package com.example.kcpsecutest.server;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.common.AmtoptLayout;
import com.example.kcpsecutest.common.CardInfoLayout;
import com.example.kcpsecutest.common.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;

import device.sdk.ifmmanager.IFMControllerInterface;
import device.sdk.ifmmanager.IFMManager;
import device.sdk.ifmmanager.Utils;
import kr.co.kcp.api.DrawPanelView;
import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

public class CreditActivity extends Activity implements OnClickListener, OnTouchListener
{
	private CardInfoLayout cardLayout;
	private AmtoptLayout amtLayout;
	
	private EditText editConmode;
    private EditText editEmv;
    private EditText editPasswd;
    private Button btnPasswd;
    
    private ScrollView sv;
    private byte[] bitmapData = null;
    private Bitmap m_bmpSign = null;
    private ImageView img;
    
    private DrawPanelView m_viewSignPad;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        
        init();
        
        SignPadInit();
    }
//*****IFM Manager +++
//	public static final byte TRD_TYPE_REQ_INIT  = (byte)0xA0;                   //초기화 요청
//	public static final byte TRD_TYPE_REQ_SELF_PROTECTION = (byte)0xA1;         //무결성 검증 요청
//	public static final byte TRD_TYPE_REQ_CURRENT_TIME = (byte)0xA2;            // 시간전송 요청
//	public static final byte TRD_TYPE_REQ_MUTUAL_AUTHENTICATION = (byte)0xA3;   //상호인증 요청
//	public static final byte TRD_TYPE_REQ_ENCRRYPTION_STATE = (byte)0xA4;       // 암호화키 D/N 상태변경요청
//	public static final byte TRD_TYPE_REQ_ENCRRYPTION_COMPLETE = (byte)0xA5;    // 암호화키 D/N 완료요청
//	public static final byte TRD_TYPE_REQ_GENERAL_TRANSACTION = (byte)0xA6;     // 일반거래요청
	public static final byte TRD_TYPE_REQ_IC_CARD_READ = (byte)0xA7;            // IC 카드읽기 요청
//	public static final byte TRD_TYPE_REQ_IC_CARD_DATA = (byte)0xA8;            // IC 카드 데이터 요청
	public static final byte TRD_TYPE_REQ_IC_CARD_ISSUER = (byte)0xA9;          // IC 카드 발급사 요청
//	public static final byte TRD_TYPE_REQ_CASH_IC_READ = (byte)0x1A;            // 현금IC 읽기 요청
//	public static final byte TRD_TYPE_REQ_CASH_IC_DATA = (byte)0x2A;            // 현금IC 데이터 요청
//	public static final byte TRD_TYPE_REQ_CASH_IC_ACCOUNT_INFO = (byte)0x3A;    // 현금IC 계좌정보 요청
//	public static final byte TRD_TYPE_REQ_IS_INPUT_READER = (byte)0x4A;         // 리더기 카드 삽입여부 요청
	
	//IFM SDK 호출
	IFMManager						ifmManager;
	LinkedHashMap<String, String>	mLinkedHashMap; //IFM 응답 hashmap
	byte							IFM_RESPONSE_FLAG = 0;

	IFMControllerInterface.OnRecvCallback recvCallback = new IFMControllerInterface.OnRecvCallback() {
		@Override
		public void onCallback(int callbackMsg, byte[] recvBuf, LinkedHashMap<String, String> linkedHashMap) {
//          TEST -callback
			Log.i("ifm_test", "callbackMsg "+ callbackMsg);
			if( recvBuf== null){
				Log.i("ifm_test", "onCallback == null");
			}
			else
			{
				Log.i("ifm_test", "onCallback != null");
				Utils.debugTx("ifm_test", recvBuf, recvBuf.length);
			
				if(IFM_RESPONSE_FLAG ==TRD_TYPE_REQ_IC_CARD_READ )
				{
					mLinkedHashMap = linkedHashMap;
					CreditAuth(TR_CODE);
				}
				else
				if(IFM_RESPONSE_FLAG ==TRD_TYPE_REQ_IC_CARD_ISSUER)
				{
					//TODO : IFM manager  OFF
					ifmManager.powerDownRdi();
				}
			}
		}
	};


//*****IFM Manager ---

	public void init(){
		
		sv = (ScrollView)findViewById(R.id.sv_credit);
		
		cardLayout = new CardInfoLayout();
		cardLayout.InitLayout(this);
		
		amtLayout = new AmtoptLayout();
		amtLayout.InitLayout(this);
        
		editConmode = (EditText)findViewById(R.id.edit_conmode_credit);
		editConmode.setText(Util.getNetwork(this));
		
        Button btnApp = (Button)findViewById(R.id.btn_creditapp_credit);
        btnApp.setOnClickListener(this);

        Button btnAppcan = (Button)findViewById(R.id.btn_creditcan_credit);
        btnAppcan.setOnClickListener(this);
        
        editEmv = (EditText)findViewById(R.id.edit_emv_credit);
        
        editPasswd = (EditText)findViewById(R.id.edit_passwd_credit);
              
        img = (ImageView)findViewById(R.id.img_sign_credit);
        
        btnPasswd = (Button)findViewById(R.id.btn_passwd_credit);
        btnPasswd.setOnClickListener(this);

		//TODO : IFM manager  ON
		ifmManager = new IFMManager();
		ifmManager.initController(this, recvCallback);
//		ifmManager = new IFMManager(this, recvCallback);
		ifmManager.powerOnRdi();
    }
	
	public void SignPadInit() {
		m_viewSignPad = (DrawPanelView)findViewById( R.id.view_sign_credit );
		m_viewSignPad.setOnTouchListener(this);

        // 2���� �����̺�Ʈó���� �޼ҵ� ���
        m_viewSignPad.setHandler(this, "touchUpSignPad");
        m_viewSignPad.setPaintColor(0xFFFF0000);
        clearSignView(true);
	}
	
	// ���� �� 2���Ŀ��� ���� �ڵ� ȣ�� ��
    public void touchUpSignPad()
    {
        try{
            // 2���� view�� �׷��� ������ �����´�.
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            m_bmpSign = Bitmap.createScaledBitmap( m_viewSignPad.getBitmap(), 128, 64, true);
            m_bmpSign.compress(Bitmap.CompressFormat.PNG,100,stream);
            stream.flush();
            stream.close();
            bitmapData = stream.toByteArray();
            
            ByteArrayInputStream in = new ByteArrayInputStream(bitmapData,0,bitmapData.length);
            m_bmpSign = BitmapFactory.decodeStream(in);
            img.setImageBitmap(m_bmpSign);
            //���� Ŭ���� Method
            clearSignView(true);
                
        }catch(Exception e){

        }

    }
	
	private void clearSignView(boolean bRecvEvnet)
    {
        // ȭ���� �������� touchUpSingPad �̺�Ʈ�� ��� ����
        m_viewSignPad.clearPanel(bRecvEvnet);
        // ����Ǿ� �ִ� �̹� �ʱ�ȭ
        if ( m_bmpSign != null )
        {
            m_bmpSign.eraseColor( 0x000000  );

            m_bmpSign = null;
        }
    }
	
	protected void onActivityResult(int requestCode, int resultCome,Intent data)
    {
		String passwd = data.getStringExtra("passwd");
		if(passwd.equals("") == false || passwd != null)
			editPasswd.setText(data.getStringExtra("passwd"));
    }

	String TR_CODE= "";
	@Override
    public void onClick(View v) {
		if(v.getId() == R.id.btn_creditapp_credit){
            CreditAuth("0100");
			TR_CODE ="0100";
        } else if(v.getId() == R.id.btn_creditcan_credit){
        	CreditAuth("0420");
			TR_CODE ="0420";
        } else if(v.getId() == R.id.btn_passwd_credit) {
        	Intent intent = new Intent(this,PinCryptActivity.class);
        	startActivityForResult(intent,1);
        }
    }

    public void CreditAuth(String tr_code) {
    	KCPAndroidApi kja = KCPFactory.getAndroidInstance();
		LinkedHashMap<String, String> _inMap, _outMap;
		_inMap = new LinkedHashMap<String, String>();
		_outMap = new LinkedHashMap<String, String>();

//		_inMap.put("con_mode", editConmode.getText().toString());
		_inMap.put("con_mode", "TEST_LAN");
		_inMap.put("proc_code", "A01001");
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
		_inMap.put("signpad_yn", "Y");
		
		_inMap.put("tx_type", cardLayout.getTxtype());		
		_inMap.put("ksn", cardLayout.getKsn());
		_inMap.put("ms_data", cardLayout.getMsdata());

		_inMap.put("emv_data", editEmv.getText().toString());
		_inMap.put("emv_key_dt", "160530");
		_inMap.put("emv_data_dt", "160530");
		_inMap.put("pass_wd", editPasswd.getText().toString());
		
		_inMap.put("cpx_flag", amtLayout.getCpxflag());
		_inMap.put("ins_mon",amtLayout.getInsmon());
		_inMap.put("tot_amt", amtLayout.getTotamt());
		_inMap.put("org_amt", amtLayout.getOrgamt());
		_inMap.put("duty_amt", amtLayout.getDutyamt());
		_inMap.put("tax_amt", amtLayout.getTaxamt());
		_inMap.put("otx_dt", amtLayout.getOtxdt());
		_inMap.put("app_no", amtLayout.getAppno());


		Log.v("TEST" ," con_mode::"+_inMap.get("con_mode"));
		Log.v("TEST" ," tx_type::"+_inMap.get("tx_type"));
		Log.v("TEST" ," ksn::"+_inMap.get("ksn"));
		Log.v("TEST" ," ms_data::"+_inMap.get("ms_data"));
		Log.v("TEST" ," emv_data::"+_inMap.get("emv_data"));
		Log.v("TEST" ," pass_wd::"+_inMap.get("pass_wd"));
		Log.v("TEST" ," cpx_flag::"+_inMap.get("cpx_flag"));
		Log.v("TEST" ," ins_mon::"+_inMap.get("ins_mon"));
		Log.v("TEST" ," org_amt::"+_inMap.get("org_amt"));
		Log.v("TEST" ," duty_amt::"+_inMap.get("duty_amt"));
		Log.v("TEST" ," tax_amt::"+_inMap.get("tax_amt"));
		Log.v("TEST" ," otx_dt::"+_inMap.get("otx_dt"));
		Log.v("TEST" ," app_no::"+_inMap.get("app_no"));



		
		//Bitmap
		if(m_bmpSign != null) {
			String tmpSign = kja.KCPSignCrypt(m_bmpSign);
			_inMap.put("sign_img", tmpSign);
		}


		if(IFM_RESPONSE_FLAG == TRD_TYPE_REQ_IC_CARD_READ) {
			//TODO: 리더기로 부터 받은 응답 넣어줌
			_inMap.put("ksn", mLinkedHashMap.get("ksn"));
			_inMap.put("ms_data", mLinkedHashMap.get("enc_track2_data"));
			_inMap.put("emv_data", mLinkedHashMap.get("emv_data"));
			_inMap.put("emv_key_dt", mLinkedHashMap.get("emv_key_dt"));
			_inMap.put("emv_data_dt", mLinkedHashMap.get("emv_data_dt"));
			//fallback data _inMap이 없음.

			kja.KCPServerQuery(_inMap, _outMap); // VAN 요청 & 응답

			String resResult;
			resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
			resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
			resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");

			if (_outMap.get("res_cd").toString().equals("0000")) {
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

				IFM_RESPONSE_FLAG =TRD_TYPE_REQ_IC_CARD_ISSUER;
				ifmManager.reqIFM(_outMap,TRD_TYPE_REQ_IC_CARD_ISSUER); //TrdType A9 발급사 요청
			}

			bitmapData = null;
			m_bmpSign = null;
			img.setImageBitmap(null);

			Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();

/*
			IFM_RESPONSE_FLAG =TRD_TYPE_REQ_IC_CARD_ISSUER;
			ifmManager.reqIFM(_outMap,TRD_TYPE_REQ_IC_CARD_ISSUER); //TrdType A9 발급사 요청
*/

		}
		else{
			//TODO : IFM manager request
			IFM_RESPONSE_FLAG= TRD_TYPE_REQ_IC_CARD_READ;
			ifmManager.reqIFM(_inMap,TRD_TYPE_REQ_IC_CARD_READ); //TrdType A7 IC카드거래요청
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.getId() == R.id.view_sign_credit) {
			v.getParent().requestDisallowInterceptTouchEvent(true);
		}
		return false;
	}
}
