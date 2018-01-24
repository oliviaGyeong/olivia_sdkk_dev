package com.example.kcpsecutest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
	}
	
	private void init(){
        Button btnServer = (Button)findViewById(R.id.btn_server_main);
        Button btnCat =(Button)findViewById(R.id.btn_cat_main);
        //olivia add btn
        Button btnReader =(Button)findViewById(R.id.btn_reader_test);

        btnServer.setOnClickListener(this);
        btnCat.setOnClickListener(this);
        btnReader.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		Intent intent;

        if(v.getId() == R.id.btn_server_main)
        {
            intent = new Intent(this,ServerMainActivity.class);
            startActivity(intent);
        } 
        else if(v.getId() == R.id.btn_cat_main) {
        	intent = new Intent(this,CatMainActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btn_reader_test) {
            intent = new Intent(this,ReaderActivity.class);
            startActivity(intent);
        }
	}

}
