package com.impact.preshopping.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.dudev.util.Utilities;

public class GalleryImageAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Uri> list;
	private List<WeakReference<Bitmap>> weakRef = new ArrayList<WeakReference<Bitmap>>();

	public GalleryImageAdapter(Context context, List<Uri> list) {
		mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int index, View view, ViewGroup viewGroup) {
		SquareImageView img = new SquareImageView(mContext);

		int width = (int)Utilities.convertDpToPixel(100, mContext);
		Bitmap b = Utilities.decodeSampledBitmapFromFile(list.get(index).getPath(), width, width);
//		img.setImageURI(list.get(index));
		int width2 = (int)Utilities.convertDpToPixel(100, mContext);
		img.setLayoutParams(new Gallery.LayoutParams(width2, width2));
		img.setImageBitmap(b);

		weakRef.add(new WeakReference<Bitmap>(b));
		
		return img;
	}
}