package com.example.kcpsecutest.common;

import com.example.kcpsecutest.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AmtoptLayout extends Fragment implements OnCheckedChangeListener {
	private EditText editInsmon, editTotamt, editOrgamt, editDutyamt, editTaxamt, editSvcamt, editAppno, editOtxdt;
	private CheckBox chkAmtopt, chkCpxflag;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.amtopt_layout_cat, container, false);
    }

	public void InitLayout(Activity activity){
		editInsmon = (EditText)activity.findViewById(R.id.edit_insmon_cat);
	    editTotamt = (EditText)activity.findViewById(R.id.edit_totamt_cat);
	    editOrgamt = (EditText)activity.findViewById(R.id.edit_orgamt_cat);
	    editDutyamt = (EditText)activity.findViewById(R.id.edit_dutyamt_cat);
	    editTaxamt = (EditText)activity.findViewById(R.id.edit_taxamt_cat);
	    editSvcamt = (EditText)activity.findViewById(R.id.edit_svcamt_cat);
	    editAppno = (EditText)activity.findViewById(R.id.edit_appno_cat);
	    editOtxdt = (EditText)activity.findViewById(R.id.edit_otxdt_cat);
	    
	    chkAmtopt = (CheckBox)activity.findViewById(R.id.chk_amtopt_cat);
	    chkCpxflag = (CheckBox)activity.findViewById(R.id.chk_cpxflag_cat);
	    
	    editDutyamt.setEnabled(false);
	    editTaxamt.setEnabled(false);
	    editSvcamt.setEnabled(false);
	    
	    
	    chkAmtopt.setOnCheckedChangeListener(this);
	    chkCpxflag.setOnCheckedChangeListener(this);
	}
	@Override
	public void onCheckedChanged(CompoundButton cb, boolean arg1) {
		if(cb.getId() == R.id.chk_amtopt_cat) {
			editTaxamt.setText("0");
			editSvcamt.setText("0");
			if(chkAmtopt.isChecked()){
				editTaxamt.setEnabled(true);
				editSvcamt.setEnabled(true);
			} else {
				editTaxamt.setEnabled(false);
				editSvcamt.setEnabled(false);
			}
		} else if(cb.getId() == R.id.chk_cpxflag_cat) {
			editDutyamt.setText("0");
			if(chkCpxflag.isChecked()){
				editDutyamt.setEnabled(true);
			} else {
				editDutyamt.setEnabled(false);
			}
		}
		
	}
	public String getAmtopt() {
		if(chkAmtopt.isChecked())
			return "Y";
		else
			return "N";
	}
	public String getCpxflag() {
		if(chkCpxflag.isChecked())
			return "Y";
		else
			return "N";
	}
	public String getInsmon() {
		return editInsmon.getText().toString();
	}

	public String getTotamt() {
		return editTotamt.getText().toString();
	}

	public String getOrgamt() {
		return editOrgamt.getText().toString();
	}

	public String getDutyamt() {
		return editDutyamt.getText().toString();
	}

	public String getTaxamt() {
		return editTaxamt.getText().toString();
	}

	public String getSvcamt() {
		return editSvcamt.getText().toString();
	}

	public String getAppno() {
		return editAppno.getText().toString();
	}

	public String getOtxdt() {
		return editOtxdt.getText().toString();
	}
	
	public void setAppno(String data) {
		this.editAppno.setText(data);
	}

	public void setOtxdt(String data) {
		this.editOtxdt.setText(data);
	}
}
