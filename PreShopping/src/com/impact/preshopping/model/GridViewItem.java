package com.impact.preshopping.model;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GridViewItem {
	
	public ImageView imageView;
	public ProgressBar progressBar;
	public TextView textViewName;
	public TextView textViewDesc;
	
	public GridViewItem(ImageView imageView, ProgressBar progressBar, TextView textViewName, TextView textViewDesc) {
		super();
		this.imageView = imageView;
		this.progressBar = progressBar;
		this.textViewName = textViewName;
		this.textViewDesc = textViewDesc;
	}

}
