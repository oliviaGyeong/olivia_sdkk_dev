package com.example.kcpsecutest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kcpsecutest.cat.CatCashActivity;
import com.example.kcpsecutest.cat.CatCashCheckActivity;
import com.example.kcpsecutest.cat.CatCashICActivity;
import com.example.kcpsecutest.cat.CatCreditActivity;
import com.example.kcpsecutest.cat.CatEtcActivity;
import com.example.kcpsecutest.cat.CatMemberActivity;
import com.example.kcpsecutest.cat.CatMpointActivity;
import com.example.kcpsecutest.cat.CatPgCashActivity;
import com.example.kcpsecutest.cat.CatPgCreditActivity;
import com.example.kcpsecutest.cat.CatPointActivity;
import com.example.kcpsecutest.cat.CatPrintActivity;

public class CatMainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_main);

        init();
    }

    private void init() {
        Button btnCredit = (Button) findViewById(R.id.btn_credit_catmain);
        Button btnCash = (Button) findViewById(R.id.btn_cash_catmain);
        Button btnPoint = (Button) findViewById(R.id.btn_point_catmain);
        Button btnMember = (Button) findViewById(R.id.btn_member_catmain);
        Button btnCashic = (Button) findViewById(R.id.btn_cashic_catmain);
        Button btnETC = (Button) findViewById(R.id.btn_etc_catmain);
        Button btnPrint = (Button) findViewById(R.id.btn_print_catmain);
        Button btnPgCash = (Button) findViewById(R.id.btn_pgcash_catmain);
        Button btnPgCredit = (Button) findViewById(R.id.btn_pgcredit_catmain);
        Button btnCashCheck = (Button) findViewById(R.id.btn_cashcheck_catmain);
        Button btnMPoint = (Button) findViewById(R.id.btn_mpoint_catmain);

        btnCredit.setOnClickListener(this);
        btnCash.setOnClickListener(this);
        btnPoint.setOnClickListener(this);
        btnMember.setOnClickListener(this);
        btnCashic.setOnClickListener(this);
        btnETC.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnPgCredit.setOnClickListener(this);
        btnPgCash.setOnClickListener(this);
        btnCashCheck.setOnClickListener(this);
        btnMPoint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        if (v.getId() == R.id.btn_credit_catmain) {
            intent = new Intent(this, CatCreditActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_cash_catmain) {
            intent = new Intent(this, CatCashActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_point_catmain) {
            intent = new Intent(this, CatPointActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_member_catmain) {
            intent = new Intent(this, CatMemberActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_cashic_catmain) {
            intent = new Intent(this, CatCashICActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_etc_catmain) {
            intent = new Intent(this, CatEtcActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_print_catmain) {
            intent = new Intent(this, CatPrintActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_pgcash_catmain) {
            intent = new Intent(this, CatPgCashActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_pgcredit_catmain) {
            intent = new Intent(this, CatPgCreditActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_cashcheck_catmain) {
            intent = new Intent(this, CatCashCheckActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_mpoint_catmain) {
            intent = new Intent(this, CatMpointActivity.class);
            startActivity(intent);
        }
    }
}
