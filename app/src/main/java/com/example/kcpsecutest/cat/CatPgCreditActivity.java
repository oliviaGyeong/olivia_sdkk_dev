package com.example.kcpsecutest.cat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.common.AmtoptLayout;
import com.example.kcpsecutest.common.ConmodeLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;

import kr.co.kcp.api.DrawPanelView;
import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

public class CatPgCreditActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
    //서명관련
    private ScrollView sv;
    private byte[] bitmapData = null;
    private Bitmap m_bmpSign = null;
    private ImageView img;
    private DrawPanelView m_viewSignPad;

    ConmodeLayout conmodeLayout;
    AmtoptLayout amtoptLayout;

    private EditText editPgPartner, editPgBalAmt, editPgCanAmt;
    private CheckBox checkPgPartCan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_pg_credit);

        init();

        SignPadInit();
    }

    public void init() {
        sv = (ScrollView)findViewById(R.id.sv_catpgcredit);
        img = (ImageView)findViewById(R.id.img_sign_catcredit);

        Button btnPgAuth = (Button)findViewById(R.id.btn_auth_pg);
        Button btnPgCan = (Button)findViewById(R.id.btn_can_pg);
        Button btnPgPartCan = (Button)findViewById(R.id.btn_partcan_pg);

        editPgPartner = (EditText)findViewById(R.id.edit_pg_partner_cat);
        editPgBalAmt = (EditText)findViewById(R.id.edit_pg_bal_amt_cat);
        editPgCanAmt = (EditText)findViewById(R.id.edit_pg_can_amt_cat);

        editPgBalAmt.setEnabled(false);
        editPgCanAmt.setEnabled(false);

        checkPgPartCan = (CheckBox)findViewById(R.id.chk_partcan_cat);

        checkPgPartCan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                if(cb.getId() == R.id.chk_partcan_cat) {
                    editPgBalAmt.setText("0");
                    editPgCanAmt.setText("0");
                    if(checkPgPartCan.isChecked()) {
                        editPgBalAmt.setEnabled(true);
                        editPgCanAmt.setEnabled(true);
                    }
                }
            }
        });

        conmodeLayout = new ConmodeLayout();
        conmodeLayout.InitLayout(this);

        amtoptLayout = new AmtoptLayout();
        amtoptLayout.InitLayout(this);

        btnPgAuth.setOnClickListener(this);
        btnPgCan.setOnClickListener(this);
        btnPgPartCan.setOnClickListener(this);
    }

    public void SignPadInit() {
        m_viewSignPad = (DrawPanelView)findViewById( R.id.view_sign_catcredit );
        m_viewSignPad.setOnTouchListener(this);

        // 응답받을 메서드 설정
        m_viewSignPad.setHandler(this, "touchUpSignPad");
        m_viewSignPad.setPaintColor(0xFFFF0000);
        clearSignView(true);
    }

    // 서명응답 함수 실행
    public void touchUpSignPad()
    {
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            m_bmpSign = Bitmap.createScaledBitmap( m_viewSignPad.getBitmap(), 128, 64, true);
            m_bmpSign.compress(Bitmap.CompressFormat.PNG,100,stream);
            stream.flush();
            stream.close();
            bitmapData = stream.toByteArray();

            ByteArrayInputStream in = new ByteArrayInputStream(bitmapData,0,bitmapData.length);
            m_bmpSign = BitmapFactory.decodeStream(in);
            img.setImageBitmap(m_bmpSign);
            //서명초기화 Method
            clearSignView(true);

        }catch(Exception e){

        }
    }

    private void clearSignView(boolean bRecvEvnet)
    {
        // 패널 초기화
        m_viewSignPad.clearPanel(bRecvEvnet);
        // ����Ǿ� �ִ� �̹� �ʱ�ȭ
        if ( m_bmpSign != null )
        {
            m_bmpSign.eraseColor( 0x000000  );

            m_bmpSign = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.view_sign_catcredit) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_auth_pg) {
            CatPgCredit("E00017", "0100");
        } else if(v.getId() == R.id.btn_can_pg) {
            CatPgCredit("E00017", "0420");
        } else {
            CatPgCredit("E00017", "0420");
        }
    }

    public void CatPgCredit(String proc_code, String tr_code) {
        KCPAndroidApi kaa = KCPFactory.getAndroidInstance();
        LinkedHashMap<String, String> _inMap, _outMap;

        _inMap = new LinkedHashMap<String, String>();
        _outMap = new LinkedHashMap<String, String>();

        _inMap.put("con_mode", conmodeLayout.getConmode());
        _inMap.put("con_info1", conmodeLayout.getConinfo1());
        _inMap.put("con_info2", conmodeLayout.getConinfo2());

        _inMap.put("proc_code", proc_code);
        _inMap.put("tr_code", tr_code);

        _inMap.put("amtopt", amtoptLayout.getAmtopt());
        _inMap.put("cpx_flag", amtoptLayout.getCpxflag());

        _inMap.put("ins_mon", amtoptLayout.getInsmon());

        _inMap.put("tot_amt", amtoptLayout.getTotamt());

        _inMap.put("signopt", "N");
        _inMap.put("prt_cd", "1");
        _inMap.put("prtopt", "0");
        _inMap.put("prtmsg1", "이용해주셔서");
        _inMap.put("prtmsg2", "감사합니다.");

        _inMap.put("pg_partner", editPgPartner.getText().toString());

        if(checkPgPartCan.isChecked()) {
            _inMap.put("pg_part_can", "Y");
            _inMap.put("pg_bal_amt", editPgBalAmt.getText().toString());
            _inMap.put("pg_can_amt", editPgCanAmt.getText().toString());
        }

        if(tr_code.equals("0420")) {
            _inMap.put("app_no", amtoptLayout.getAppno());
            _inMap.put("otx_dt", amtoptLayout.getOtxdt());
        } else {
            _inMap.put("org_amt", amtoptLayout.getOrgamt());
            _inMap.put("duty_amt", amtoptLayout.getDutyamt());
            _inMap.put("tax_amt", amtoptLayout.getTaxamt());
            _inMap.put("svc_amt", amtoptLayout.getSvcamt());
        }

        kaa.KCPCatQuery(_inMap, _outMap);

        String resResult;
        resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
        resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
        resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");

        if(_outMap.get("res_cd").toString().equals("0000")) {
            amtoptLayout.setAppno(_outMap.get("app_no").toString());
            amtoptLayout.setOtxdt(_outMap.get("tx_dt").toString().substring(0, 6));

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
            resResult += ("자동이체코드	: " + _outMap.get("at_type") + "\n");
            resResult += ("서명데이터	: " + _outMap.get("sign_img") + "\n");
            resResult += "----PG 부분취소----";
            resResult += ("원승인금액	: " + _outMap.get("pg_org_amt") + "\n");
            resResult += ("취소전남은금액	: " + _outMap.get("pg_pre_amt") + "\n");
            resResult += ("취소적용금액	: " + _outMap.get("pg_can_amt") + "\n");
            resResult += ("취소후남은금액	: " + _outMap.get("pg_bal_amt") + "\n");
        }

        Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();
    }
}
