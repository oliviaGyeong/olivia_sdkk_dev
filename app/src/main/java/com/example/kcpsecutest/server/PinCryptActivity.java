package com.example.kcpsecutest.server;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.R.id;
import com.example.kcpsecutest.R.layout;

import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PinCryptActivity extends Activity implements OnClickListener{
	EditText editCardno;
	EditText editPasswd;
	EditText editWorkKey;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pincrypt);
		
		Init();
	}
	
	public void Init() {
		editCardno = (EditText)findViewById(R.id.edit_cardno_pincrypt);
		editPasswd = (EditText)findViewById(R.id.edit_passwd_pincrypt);
		editWorkKey = (EditText)findViewById(R.id.edit_workkey_pincrypt);
		
		Button btnOk = (Button)findViewById(R.id.btn_ok_pincrypt);
		btnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {                                                                                                               
		if(v.getId() == R.id.btn_ok_pincrypt) {
			PinCrypt();
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent();
			intent.putExtra("passwd", "");
			setResult(0, intent);
			finish();
			break;
		}
		return true;
	}
	
	public void PinCrypt() {
		String resCrypt;
		KCPAndroidApi kja = KCPFactory.getAndroidInstance();
		resCrypt = kja.KCPPinEncCrypt(editCardno.getText().toString(), editCardno.getText().length(), Integer.parseInt(editWorkKey.getText().toString()), editPasswd.getText().toString(), editPasswd.getText().length());
		Toast.makeText(this, resCrypt, Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent();
		intent.putExtra("passwd", resCrypt);
		setResult(0, intent);
		finish();
	}

}
