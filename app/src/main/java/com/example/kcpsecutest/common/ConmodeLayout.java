package com.example.kcpsecutest.common;

import com.example.kcpsecutest.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ConmodeLayout extends Fragment{
	private EditText editConmode, editConinfo1, editConinfo2;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.conmode_layout_cat, container, false);
    }
	
	public void InitLayout(Activity activity) {
		editConmode = (EditText)activity.findViewById(R.id.edit_conmode_cat);
        editConinfo1 = (EditText)activity.findViewById(R.id.edit_coninfo1_cat);
        editConinfo2 = (EditText)activity.findViewById(R.id.edit_coninfo2_cat);
	}
	
	public String getConmode() {
		return editConmode.getText().toString();
	}
	public String getConinfo1() {
		return editConinfo1.getText().toString();
	}
	public String getConinfo2() {
		return editConinfo2.getText().toString();
	}
}
