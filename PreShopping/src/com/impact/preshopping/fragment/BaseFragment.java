package com.impact.preshopping.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

	public static final String TAG_CATEGORY = "categoryFragment";
	public static final String TAG_COMPANY = "companyFragment";
	public static final String TAG_GROUP = "groupFragment";
	
	public abstract String getPreviousFragmentTag();
	public abstract String getCurrentId();
	public abstract void resumeUi(String id);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		enableBackBtn();
	}

	private void enableBackBtn() {
		getActivity().getActionBar().setHomeButtonEnabled(true);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setDisplayShowHomeEnabled(false);
		
		if (this.getClass().getSimpleName().equals(CompanyFragment.class.getSimpleName())) {
			getActivity().getActionBar().setTitle("Exit");
		} else {
			getActivity().getActionBar().setTitle("Back");
		}
		
	}

}
