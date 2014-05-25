package com.impact.preshopping.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dudev.util.Utilities;
import com.impact.preshopping.CompanyFragmentActivity.IOnItemClicked;
import com.impact.preshopping.R;
import com.impact.preshopping.fragment.CategoryFragment.IOnItemClicked_Category;
import com.impact.preshopping.fragment.CompanyFragment.IOnItemClicked_Company;
import com.impact.preshopping.fragment.GroupFragment.IOnItemClicked_Group;
import com.impact.preshopping.model.GridViewItem;

public class ImageAdapter extends BaseAdapter {

	public enum ItemType {
		NONE, COMPANY, CATEGORY, GROUP, PRODUCT
	}

	private Context context;
	private List<WeakReference<Bitmap>> weakRef = new ArrayList<WeakReference<Bitmap>>();
	public static final String TAG = ImageAdapter.class.getSimpleName();
	private List<HashMap<String, String>> data;
	private ItemType type;
	public static final String NAME = "name";
	public static final String DESC = "desc";
	public static final String ID = "id";
	public static final String IMG_FILE_PATH = "filePath";
	public static final String IMG_URL_PATH = "icon";
	

	private List<ProgressBar> progressBars = new ArrayList<ProgressBar>();
	private IOnItemClicked_Group iGroup;
	private IOnItemClicked_Company iCompany;

	private IOnItemClicked_Category iCategory;
	
	private IOnItemClicked iOnItemClicked;
	
	
	public void setiOnItemClicked(IOnItemClicked iOnItemClicked) {
		this.iOnItemClicked = iOnItemClicked;
	}
	public ImageAdapter(Context context, ItemType type, List<HashMap<String, String>> data) {
		this.context = context;
		this.data = data;
		this.type = type;
	}
	@Override
	public int getCount() {
		return data.size();
	}

	public IOnItemClicked_Category getiCategory() {
		return iCategory;
	}

	public IOnItemClicked_Company getiCompany() {
		return iCompany;
	}

	public IOnItemClicked_Group getiGroup() {
		return iGroup;
	}
	@Override
	public Object getItem(int position) {

		if (progressBars.size() > 0)
			return progressBars.get(position);
		else
			return new ProgressBar(context);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		gridView = inflater.inflate(R.layout.gridview_item, null);

		TextView name = (TextView) gridView.findViewById(R.id.name);
		name.setText(data.get(position).get(NAME));
		TextView desc = (TextView) gridView.findViewById(R.id.desc);
		desc.setText(data.get(position).get(DESC));
		ImageView imageView = (ImageView) gridView.findViewById(R.id.image1);
		
		if (!TextUtils.isEmpty(data.get(position).get(IMG_FILE_PATH))) {
			
			Bitmap b = getImageAsBitmap(Uri.parse(data.get(position).get(IMG_FILE_PATH)).getPath());
			
			if (b != null) {
			    imageView.setImageBitmap(b);
			    weakRef.add(new WeakReference<Bitmap>(b));
			}
		} 
		
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (type == ItemType.COMPANY) {
//					Toast.makeText(context, "Go to category page...", Toast.LENGTH_SHORT).show();
					if (iCompany != null) {
						iCompany.onItemClicked_Company(data.get(position).get("id"));
					}
				} else if (type == ItemType.CATEGORY) {
//					Toast.makeText(context, "Go to product CATEGORY page...", Toast.LENGTH_SHORT).show();
					if (iCategory != null) {
						iCategory.onItemClicked_Category(data.get(position).get("id"));
					}
				} else if (type == ItemType.GROUP) {
//					Toast.makeText(context, "Go to product page...", Toast.LENGTH_SHORT).show();
					if (iGroup != null) {
						iGroup.onItemClicked_Group(data.get(position).get("id"));
					}
				}

				if (iOnItemClicked != null) {
					iOnItemClicked.onItemClicked(data.get(position).get("id"));
				}
			}
		});
		
		return gridView;
	}

	private Bitmap getImageAsBitmap(String path) {
	    
	    if (!new File(path).exists()) {
	        Log.e(TAG, "file not exist.");
	        return null;
	    }
	    
	    float width = Utilities.convertDpToPixel(Utilities.ICON_MAX_SIZE, context);
        Options options = Utilities.getOptions(path, (int) width, (int) width);
        Bitmap b = BitmapFactory.decodeFile(path, options);

        return b;
    }
    public void setiCategory(IOnItemClicked_Category iCategory) {
		this.iCategory = iCategory;
	}

	public void setiCompany(IOnItemClicked_Company iCompany) {
		this.iCompany = iCompany;
	}
	public void setiGroup(IOnItemClicked_Group iGroup) {
		this.iGroup = iGroup;
	}
}
