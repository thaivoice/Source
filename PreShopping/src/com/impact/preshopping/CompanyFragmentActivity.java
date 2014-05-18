package com.impact.preshopping;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.impact.preshopping.fragment.BaseFragment;
import com.impact.preshopping.fragment.CategoryFragment;
import com.impact.preshopping.fragment.CategoryFragment.IOnItemClicked_Category;
import com.impact.preshopping.fragment.CompanyFragment;
import com.impact.preshopping.fragment.CompanyFragment.IOnItemClicked_Company;
import com.impact.preshopping.fragment.GroupFragment;
import com.impact.preshopping.fragment.GroupFragment.IOnItemClicked_Group;

public class CompanyFragmentActivity extends SherlockFragmentActivity implements IOnItemClicked_Group, IOnItemClicked_Company, IOnItemClicked_Category {

	private CompanyFragment frgmCompany;
	private CategoryFragment frgmCategory;
	private GroupFragment frgmGroup;
	public static final String TAG = CompanyFragmentActivity.class.getSimpleName();
	private List<WeakReference<Activity>> stack;
	private List<Class<?>> clazz;
	public interface IUpdateFragment {
		public void update(String id);
	}

	public interface IOnItemClicked {
		public void onItemClicked(String id);
	}
	
	
	private IUpdateFragment frgmUpdater;

	public CompanyFragmentActivity() {
		// required....
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {

		if (frgmUpdater != null) {
			outState.putString("LAST_FRGMNT_INFO", frgmUpdater.toString()); //this.id + "," + this.getTag();	
		}		
		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_fragment);

		if (savedInstanceState != null) {
			String info = savedInstanceState.getString("LAST_FRGMNT_INFO");
			if (!TextUtils.isEmpty(info)) {
				String[] data = info.split(",", 2);
				String id = data[0];
				String tag = data[1];
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.show(getSupportFragmentManager().findFragmentByTag(tag));
				transaction.commit();
				frgmUpdater = (IUpdateFragment)getSupportFragmentManager().findFragmentByTag(tag);
				((BaseFragment)getSupportFragmentManager().findFragmentByTag(tag)).resumeUi(id);				
			} else {
//				frgmCategory = new CategoryFragment(this);
//				getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout1, frgmCategory, BaseFragment.TAG_CATEGORY).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
//				getSupportFragmentManager().beginTransaction().hide(frgmCategory).commit();
//
//				frgmGroup = new GroupFragment(this);
//				getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout1, frgmGroup, BaseFragment.TAG_GROUP).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
//				getSupportFragmentManager().beginTransaction().hide(frgmGroup).commit();
//				frgmCompany = new CompanyFragment(this);
//				getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout1, frgmCompany, BaseFragment.TAG_COMPANY).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
////				getSupportFragmentManager().beginTransaction().hide(frgmCompany).commit();
//				frgmUpdater = frgmCompany;
//				frgmUpdater.update("*");
			}
		} else {
//			frgmCategory = new CategoryFragment(this);
//			getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout1, frgmCategory, BaseFragment.TAG_CATEGORY).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
//			getSupportFragmentManager().beginTransaction().hide(frgmCategory).commit();
//
//			frgmGroup = new GroupFragment(this);
//			getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout1, frgmGroup, BaseFragment.TAG_GROUP).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
//			getSupportFragmentManager().beginTransaction().hide(frgmGroup).commit();
//			frgmCompany = new CompanyFragment(this);
//			getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout1, frgmCompany, BaseFragment.TAG_COMPANY).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
////			getSupportFragmentManager().beginTransaction().hide(frgmCompany).commit();
//			frgmUpdater = frgmCompany;
//			frgmUpdater.update("*");
		}
		
		stack = ((PreShoppingApp) getApplication()).getActivityStack();
		clazz = ((PreShoppingApp) getApplication()).getActivityClazz();

		int size = stack.size();
		boolean alreadyInStack = false;
		for (int i = 0; i < size; i++) {
			Activity a = stack.get(i).get();
			if (a != null) {
				if (a.getClass().getName().equals(this.getClass().getName())) {
					alreadyInStack = true;
				}
			}
		}

		if (!alreadyInStack) {
			Log.w(TAG, "Not exist in stack - request to add new ref.");
			((PreShoppingApp) getApplication()).getActivityStack().clear();
			((PreShoppingApp) getApplication()).getActivityStack().add(new WeakReference<Activity>(this));
		}
	}

	
	
	



	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {

		return super.onCreateView(name, context, attrs);
	}

	@Override
	public void onItemClicked_Category(String id) {
		
		getSupportFragmentManager().beginTransaction().hide(frgmCategory).commit();
		getSupportFragmentManager().beginTransaction().show(frgmGroup).commit();
		getFragmentManager().beginTransaction().addToBackStack(BaseFragment.TAG_CATEGORY).commit();
		frgmUpdater = frgmGroup;
		frgmUpdater.update(id);
		counter++;
		setBackBtnLabel("Back");
	}

	


	@Override
	public void onItemClicked_Company(String id) {

		getSupportFragmentManager().beginTransaction().hide(frgmCompany).commit();
		getSupportFragmentManager().beginTransaction().show(frgmCategory).commit();
		getFragmentManager().beginTransaction().addToBackStack(BaseFragment.TAG_COMPANY).commit();
		frgmUpdater = frgmCategory;
		frgmUpdater.update(id);
		setBackBtnLabel("Back");
	}

	@Override
	public void onItemClicked_Group(String id) {
		getSupportFragmentManager().beginTransaction().addToBackStack(BaseFragment.TAG_GROUP).commit();
	}
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu("Option Menu");
		subMenu1.add("Registration").setIntent(new Intent(this, PreLoginActivity.class).putExtra("data", "0"));
		subMenu1.add("Notification Settings").setIntent(new Intent(this, PreLoginActivity.class).putExtra("data", "1"));
		;
		subMenu1.add("Favorites").setIntent(new Intent(this, RegistrationActivity.class).putExtra("data", "2"));
		;
		subMenu1.add("Manage Data").setIntent(new Intent(this, RegistrationActivity.class).putExtra("data", "2"));
		;
		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_dark);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		// SubMenu subMenu2 = menu.addSubMenu("Overflow Item");
		// subMenu2.add("These");
		// subMenu2.add("Are");
		// subMenu2.add("Sample");
		// subMenu2.add("Items");
		//
		// MenuItem subMenu2Item = subMenu2.getItem();
		// subMenu2Item.setIcon(R.drawable.ic_compose);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			onGoback();

		} else {
			Intent launch = item.getIntent();
			int size = stack.size();
			Log.w(TAG, "activity stack size = " + size);
			for (int i = 0; i < size; i++) {
				Activity a = stack.get(i).get();
				if (a != null) {

					if (launch != null) {
						if (a.getClass().getName().equals(launch.getComponent().getClassName())) {
							if (launch.getComponent().getClassName().equals(PreLoginActivity.class.getName())) {
								Log.w(TAG, "MainActivity is about to launch.");
								stack.clear();
								launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							}
							launch.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							break;
						}
					}

				}
			}

			if (launch != null) {
				if (launch.getComponent().getClassName().equals(PreLoginActivity.class.getName())) {
					Log.w(TAG, "MainActivity is about to launch.");
					stack.clear();
					launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				}
				startActivity(launch);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			}
		}
		return true;
		// return super.onOptionsItemSelected(item);
	}


	private void onGoback() {
		if (getFragmentManager().getBackStackEntryCount() == 0) {
			finish();
		} else {
			if (counter == 0) {
				getSupportFragmentManager().beginTransaction().hide(frgmCategory).commit();
				getSupportFragmentManager().beginTransaction().show(frgmCompany).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).commit();
				getFragmentManager().popBackStack();
				
				setBackBtnLabel("Exit");
				
			} else {
				getSupportFragmentManager().beginTransaction().show(frgmCategory).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).commit();
				getSupportFragmentManager().beginTransaction().hide(frgmGroup).commit();
				getFragmentManager().popBackStack();
				counter--;
				setBackBtnLabel("Back");
				
			} 
		}
	}
	
	private int counter = 0;
	private void setBackBtnLabel(String lbl) {
		getActionBar().setTitle(lbl);
	}
}
