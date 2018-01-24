package com.example.kcpsecutest.server;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kcpsecutest.R;
import com.example.kcpsecutest.common.AmtoptLayout;
import com.example.kcpsecutest.common.CardInfoLayout;
import com.example.kcpsecutest.common.PgButtonLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;

import kr.co.kcp.api.DrawPanelView;
import kr.co.kcp.api.KCPAndroidApi;
import kr.co.kcp.api.KCPFactory;

public class PgCreditActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    //public final static byte SI = (byte) 0x0F;
    public final static int SI = 0x0F;

    private EditText editConmode, editEmvdata, editEmvkeydt, editEmvdatadt, editPgpartner, editPgbalamt, editPgcanamt;

    private AmtoptLayout amtoptLayout;
    private CardInfoLayout cardInfoLayout;
    private PgButtonLayout pgButtonLayout;

    private CheckBox checkPgPartCan;

    private byte[] bitmapData = null;
    private Bitmap m_bmpSign = null;
    private ImageView img;

    private DrawPanelView m_viewSignPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_credit);

        init();

        SignPadInit();
    }

    public void init() {
        amtoptLayout = new AmtoptLayout();
        amtoptLayout.InitLayout(this);

        cardInfoLayout = new CardInfoLayout();
        cardInfoLayout.InitLayout(this);

        Button btnPgAuth = (Button) findViewById(R.id.btn_auth_pg);
        Button btnPgCan = (Button) findViewById(R.id.btn_can_pg);
        Button btnPgPartCan = (Button) findViewById(R.id.btn_partcan_pg);

        editConmode = (EditText) findViewById(R.id.edit_conmode_credit);
        editEmvdata = (EditText) findViewById(R.id.edit_emv_credit);
        /*editEmvkeydt = (EditText)findViewById(R.id.edit_emv_key_dt);
        editEmvdatadt = (EditText)findViewById(R.id.edit_emv_data_dt);*/
        editPgpartner = (EditText) findViewById(R.id.edit_pg_partner_credit);
        editPgbalamt = (EditText) findViewById(R.id.edit_pg_bal_amt_credit);
        editPgcanamt = (EditText) findViewById(R.id.edit_pg_can_amt_credit);

        editPgbalamt.setEnabled(false);
        editPgcanamt.setEnabled(false);

        checkPgPartCan = (CheckBox) findViewById(R.id.chk_partcan_credit);

        checkPgPartCan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.chk_partcan_credit) {
                    editPgbalamt.setText("0");
                    editPgcanamt.setText("0");
                    if (checkPgPartCan.isChecked()) {
                        editPgbalamt.setEnabled(true);
                        editPgcanamt.setEnabled(true);
                    }
                }
            }
        });
		
		img = (ImageView)findViewById(R.id.img_sign_credit);

        btnPgAuth.setOnClickListener(this);
        btnPgCan.setOnClickListener(this);
        btnPgPartCan.setOnClickListener(this);
    }

    public void SignPadInit() {
        m_viewSignPad = (DrawPanelView) findViewById(R.id.view_sign_credit);
        m_viewSignPad.setOnTouchListener(this);

        // 2���� �����̺�Ʈó���� �޼ҵ� ���
        m_viewSignPad.setHandler(this, "touchUpSignPad");
        m_viewSignPad.setPaintColor(0xFFFF0000);
        clearSignView(true);
    }

    public void touchUpSignPad() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            m_bmpSign = Bitmap.createScaledBitmap(m_viewSignPad.getBitmap(), 128, 64, true);
            m_bmpSign.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            bitmapData = stream.toByteArray();

            ByteArrayInputStream in = new ByteArrayInputStream(bitmapData, 0, bitmapData.length);
            m_bmpSign = BitmapFactory.decodeStream(in);
            img.setImageBitmap(m_bmpSign);
            //���� Ŭ���� Method
            clearSignView(true);

        } catch (Exception e) {

        }
    }

    public void clearSignView(boolean bRecvEvent) {
        m_viewSignPad.clearPanel(bRecvEvent);
        if (m_bmpSign != null) {
            m_bmpSign.eraseColor(0x000000);

            m_bmpSign = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_auth_pg) {
            serverPgCredit("A01000", "0100");
        } else if (v.getId() == R.id.btn_can_pg) {
            serverPgCredit("A01000", "0420");
        } else {
            serverPgCredit("A01000", "0420");
        }
    }

    public void serverPgCredit(String proc_code, String tr_code) {
        KCPAndroidApi kaa = KCPFactory.getAndroidInstance();
        LinkedHashMap<String, String> _inMap, _outMap;

        _inMap = new LinkedHashMap<String, String>();
        _outMap = new LinkedHashMap<String, String>();

        _inMap.put("con_mode", editConmode.getText().toString());
        _inMap.put("proc_code", proc_code);
        _inMap.put("tr_code", tr_code);
        _inMap.put("busi_cd", "TEST");
        _inMap.put("reader_sn", "1000000000");
        _inMap.put("pos_sn", "POS1234567890");
        _inMap.put("reader_cert", "#DUALPAY633C");
        _inMap.put("reader_ver", "C101");
        _inMap.put("sw_cert", "###SUPER-POS");
        _inMap.put("sw_ver", "V100");
        _inMap.put("term_id", "1002189853");
        _inMap.put("trace_no", "1");
        _inMap.put("signpad_yn", "Y");
        _inMap.put("ipek_yn", "P");
        _inMap.put("cpx_flag", "N");

        _inMap.put("tx_type", cardInfoLayout.getTxtype());
        /*_inMap.put("ksn", cardInfoLayout.getKsn());
        _inMap.put("ms_data", cardInfoLayout.getMsdata());*/
        _inMap.put("ksn", "flRHFRCZmAAAKQ==");
        _inMap.put("ms_data", "VKBTEZ/2amn4YA6qHlVloDX1kmXlPgEkUV/HO2nD8h0lEvYD7hfbDWgBmPXDEnvj");
        _inMap.put("ins_mon", amtoptLayout.getInsmon());
        _inMap.put("tot_amt", amtoptLayout.getTotamt());
        _inMap.put("org_amt", amtoptLayout.getOrgamt());
        //_inMap.put("fallback", "");
        _inMap.put("emv_key_dt", "160530");
        _inMap.put("emv_data_dt", "160530");
        //_inMap.put("emv_data", editEmvdata.getText().toString());
        _inMap.put("emv_data", "");

        byte[] userData = new byte[256];
        byte[] start = "0002".getBytes();
        byte[] zero = "000000000".getBytes();

        int balAmt, canAmt;

        balAmt = Integer.parseInt(editPgbalamt.getText().toString());
        canAmt = Integer.parseInt(editPgcanamt.getText().toString());

        byte[] balAmtByte = String.format("%09d", balAmt).getBytes();
        byte[] canAmtByte = String.format("%09d", canAmt).getBytes();

        //userData = "0002" + editPgpartner.getText().toString() + SI + SI + SI + SI + SI + "000000000" + SI + "000000000";

        if (checkPgPartCan.isChecked()) {
            System.arraycopy(start, 0, userData, 0, start.length);
            userData[start.length] = (char) 0x0F;
            userData[start.length + 1] = (char) SI;
            userData[start.length + 2] = (char) SI;
            userData[start.length + 3] = (char) SI;
            userData[start.length + 4] = (char) SI;
            System.arraycopy(balAmtByte, 0, userData, start.length + 5, balAmtByte.length);
            userData[start.length + balAmtByte.length + 5] = (char) SI;
            System.arraycopy(canAmtByte, 0, userData, start.length + balAmtByte.length + 6, canAmtByte.length);

            _inMap.put("user_data", new String(userData, 0, start.length + balAmtByte.length + canAmtByte.length + 6));
        } else {
            System.arraycopy(start, 0, userData, 0, start.length);
            userData[start.length] = (char) SI;
            userData[start.length + 1] = (char) SI;
            userData[start.length + 2] = (char) SI;
            userData[start.length + 3] = (char) SI;
            userData[start.length + 4] = (char) SI;
            System.arraycopy(zero, 0, userData, start.length + 5, zero.length);
            userData[start.length + zero.length + 5] = (char) SI;
            System.arraycopy(zero, 0, userData, start.length + zero.length + 6, zero.length);
            _inMap.put("user_data", new String(userData, 0, start.length + zero.length + zero.length + 6));
        }

        if (tr_code.equals("0420")) {
            _inMap.put("otx_dt", amtoptLayout.getOtxdt());
            _inMap.put("app_no", amtoptLayout.getAppno());
        }

        kaa.KCPServerQuery(_inMap, _outMap);

        String resResult;
        resResult = "응답코드	: " + _outMap.get("res_cd") + "\n";
        resResult += ("응답메시지1	: " + _outMap.get("resmsg1") + "\n");
        resResult += ("응답메시지2	: " + _outMap.get("resmsg2") + "\n");

        if (_outMap.get("res_cd").toString().equals("0000")) {
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
            resResult += ("user_data : " + _outMap.get("user_data") + "\n");
            resResult += ("prtmsg1 : " + _outMap.get("prtmsg1") + "\n");
            resResult += ("prtmsg2 : " + _outMap.get("prtmsg2") + "\n");
            resResult += ("prtmsg3 : " + _outMap.get("prtmsg3") + "\n");
            resResult += ("prtmsg4 : " + _outMap.get("prtmsg4") + "\n");
        }

        Toast.makeText(this, resResult, Toast.LENGTH_SHORT).show();

        Log.d("outMap : ", resResult);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.view_sign_credit) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }
}
