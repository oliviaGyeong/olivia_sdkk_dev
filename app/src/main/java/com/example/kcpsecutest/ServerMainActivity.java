package com.example.kcpsecutest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.kcpsecutest.server.CashActivity;
import com.example.kcpsecutest.server.CreditActivity;
import com.example.kcpsecutest.server.CryptKeyActivity;
import com.example.kcpsecutest.server.MemberActivity;
import com.example.kcpsecutest.server.PgCashActivity;
import com.example.kcpsecutest.server.PgCreditActivity;
import com.example.kcpsecutest.server.PinCryptActivity;
import com.example.kcpsecutest.server.PointActivity;
import com.example.kcpsecutest.server.StoreInfoDownActivity;

public class ServerMainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_main);
		
		init();
	}
	
	private void init(){
        Button btnDown = (Button)findViewById(R.id.btn_down);
        Button btnCredit =(Button)findViewById(R.id.btn_credit);
        Button btnCash = (Button)findViewById(R.id.btn_cash);
        Button btnKey = (Button)findViewById(R.id.btn_key);
        Button btnPin = (Button)findViewById(R.id.btn_pincrypt);
        Button btnPoint = (Button)findViewById(R.id.btn_point);
        Button btnMember = (Button)findViewById(R.id.btn_member);
        Button btnPgcredit = (Button)findViewById(R.id.btn_pg_credit);
        Button btnPgcash = (Button)findViewById(R.id.btn_pg_cash);
        //Button btnPayon = (Button)findViewById(R.id.btn_payon);

        btnDown.setOnClickListener(this);
        btnCredit.setOnClickListener(this);
        btnCash.setOnClickListener(this);
        btnKey.setOnClickListener(this);
        btnPin.setOnClickListener(this);
        btnPoint.setOnClickListener(this);
        btnMember.setOnClickListener(this);
        btnPgcredit.setOnClickListener(this);
        btnPgcash.setOnClickListener(this);
        //btnPayon.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		Intent intent;

        if(v.getId() == R.id.btn_down)
        {
            intent = new Intent(this,StoreInfoDownActivity.class);
            startActivity(intent);
        } 
        else if(v.getId() == R.id.btn_key) {
        	intent = new Intent(this,CryptKeyActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btn_credit)
        {
            intent = new Intent(this,CreditActivity.class);
            this.startActivity(intent);
        }
        else if(v.getId() == R.id.btn_cash)
        {
            intent = new Intent(this,CashActivity.class);
            this.startActivity(intent);
        }
        else if(v.getId() == R.id.btn_point){
            intent = new Intent(this, PointActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btn_member){
            intent = new Intent(this, MemberActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btn_pincrypt){
            intent = new Intent(this,PinCryptActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btn_pg_credit) {
            intent = new Intent(this, PgCreditActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btn_pg_cash) {
            intent = new Intent(this, PgCashActivity.class);
            startActivity(intent);
        }
	}
}

