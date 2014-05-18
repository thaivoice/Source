package com.impact.preshopping.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.impact.preshopping.R;
import com.impact.preshopping.model.PromotionDetailInfo;
import com.impact.preshopping.model.PromotionHeaderInfo;

public class MyListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<PromotionHeaderInfo> deptList;

    public MyListAdapter(Context context, ArrayList<PromotionHeaderInfo> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<PromotionDetailInfo> promotionDetailInfoList = deptList.get(groupPosition).getPromotionDetailInfoList();
        return promotionDetailInfoList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<PromotionDetailInfo> productList = deptList.get(groupPosition).getPromotionDetailInfoList();
        return productList.size();

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        PromotionDetailInfo detailInfo = (PromotionDetailInfo) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.promotion_item, null);
        }

        TextView textViewRowNumber = (TextView) view.findViewById(R.id.textViewRowNumber);
        TextView textViewPromotionDescription = (TextView) view.findViewById(R.id.textViewPromotionDescription);
        TextView textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        
        textViewRowNumber.setText("" + (childPosition + 1));
        textViewPromotionDescription.setText(TextUtils.isEmpty(detailInfo.desc) ? "" : detailInfo.desc);
        textViewDate.setText("Only from: " + detailInfo.startDate + " to " + detailInfo.endDate);
        
        return view;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {

        PromotionHeaderInfo headerInfo = (PromotionHeaderInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.promotion_item_header, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(headerInfo.getName().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}