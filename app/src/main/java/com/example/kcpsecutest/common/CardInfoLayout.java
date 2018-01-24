package com.example.kcpsecutest.common;

import com.example.kcpsecutest.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

public class CardInfoLayout extends Fragment{
	private RadioGroup rgTxType;
	private EditText editKsn, editMsData;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.cardinfo_layout_server, container, false);
    }
	
	public void InitLayout(Activity activity) {
		rgTxType  = (RadioGroup)activity.findViewById(R.id.rg_txtype_server);
		editKsn = (EditText)activity.findViewById(R.id.edit_ksn_server);
		editMsData = (EditText)activity.findViewById(R.id.edit_msdata_server);
	}
	
	public String getTxtype() {
		if(rgTxType.getCheckedRadioButtonId() == R.id.rbtn_keyin_server) {
			return "10";
		} else if(rgTxType.getCheckedRadioButtonId() == R.id.rbtn_swipe_server) {
			return "20";
		} else if(rgTxType.getCheckedRadioButtonId() == R.id.rbtn_ic_server) {
			return "30";
		} else if(rgTxType.getCheckedRadioButtonId() == R.id.rbtn_fb_server) {
			return "60";
		} else
			return "10";
		
	}
	
	public String getKsn() {
		return editKsn.getText().toString();
	}
	
	public String getMsdata() {
		return editMsData.getText().toString();
	}
}
