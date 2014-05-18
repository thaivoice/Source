package com.impact.preshopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.dudev.util.Utilities;
import com.dudev.util.webview.WebViewDemoActivity;
import com.impact.preshopping.adapter.MyListAdapter;
import com.impact.preshopping.model.PromotionDetailInfo;
import com.impact.preshopping.model.PromotionHeaderInfo;

public class PromotionHistoryListActivity extends Activity {

    private ExpandableListView myList;
    private MyListAdapter listAdapter;
    private LinkedHashMap<String, PromotionHeaderInfo> promotionHashMap = new LinkedHashMap<String, PromotionHeaderInfo>();
    private ArrayList<PromotionHeaderInfo> promotionList = new ArrayList<PromotionHeaderInfo>();

    // our child listener
    private OnChildClickListener myListItemClicked = new OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            // get the group header
            PromotionHeaderInfo headerInfo = promotionList.get(groupPosition);
            // get the child info
            PromotionDetailInfo detailInfo = headerInfo.getPromotionDetailInfoList().get(childPosition);
            
            Intent myintent = new Intent(getApplicationContext(), WebViewDemoActivity.class);
            myintent.putExtra(GcmIntentService.PROMOTION_URL, detailInfo.url);
            startActivity(myintent);
            
            return false;
        }

    };

    // our group listener
    private OnGroupClickListener myListGroupClicked = new OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

            // get the group header
//            PromotionHeaderInfo headerInfo = promotionList.get(groupPosition);

            return false;
        }

    };

    // here we maintain our products in various departments
    private int addPromotions(HashMap<String, String> promotion) {

        String endDate = promotion.get(GcmIntentService.PROMOTION_END_DATE);
        String startDate = promotion.get(GcmIntentService.PROMOTION_START_DATE);
        String url = promotion.get(GcmIntentService.PROMOTION_URL);
        String desc = promotion.get(GcmIntentService.PROMOTION_DESC);
        String companyId = promotion.get(GcmIntentService.PROMOTION_COMPANY_ID);
        String companyName = Utilities.getCompanyName(getApplicationContext(), companyId);
        
        int groupPosition = 0;

     // check the hash map if the group already exists
        PromotionHeaderInfo headerInfo = promotionHashMap.get(companyName);
        if (headerInfo == null) {
            headerInfo = new PromotionHeaderInfo();
            headerInfo.setName(companyName);  
            promotionList.add(headerInfo);
            promotionHashMap.put(companyName, headerInfo);
        }

        // create a new child and add that to the group
        PromotionDetailInfo detailInfo = new PromotionDetailInfo();
        detailInfo.startDate = startDate;
        detailInfo.endDate = endDate;
        detailInfo.url = url;
        detailInfo.desc = desc;
        headerInfo.getPromotionDetailInfoList().add(detailInfo);

        // find the group position inside the list
        groupPosition = promotionList.indexOf(headerInfo);
        
        return groupPosition;
    }

    // method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            myList.collapseGroup(i);
        }
    }

    // method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            myList.expandGroup(i);
        }
    }

    // load some initial data into out list
    private void loadData() {

        List<HashMap<String, String>> promotions = Utilities.getAllValidPromotion(getApplicationContext());
        for (int i = 0; i < promotions.size(); i++) {
            HashMap<String, String> promotion = promotions.get(i);
            addPromotions(promotion);
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.promotion_activity);
        
//        getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_action_back));
        getActionBar().setTitle("Back");
//        getActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.black));
        getActionBar().setHomeButtonEnabled(true);
        
        loadData();
        // get reference to the ExpandableListView
        myList = (ExpandableListView) findViewById(R.id.myList);
        // create the adapter by passing your ArrayList data
        listAdapter = new MyListAdapter(PromotionHistoryListActivity.this, promotionList);
        // attach the adapter to the list
        myList.setAdapter(listAdapter);

        // expand all Groups
        expandAll();

        // listener for child row click
        myList.setOnChildClickListener(myListItemClicked);
        // listener for group heading click
        myList.setOnGroupClickListener(myListGroupClicked);
    }
}
