package com.impact.preshopping.model;

import java.util.ArrayList;

public class PromotionHeaderInfo {

    private String name;
    private ArrayList<PromotionDetailInfo> promotionDetailList = new ArrayList<PromotionDetailInfo>();;

    public String getName() {
        return name;
    }

    public ArrayList<PromotionDetailInfo> getPromotionDetailInfoList() {
        return promotionDetailList;
    }

    public void setName(String name) {
        this.name = name;
    }
}