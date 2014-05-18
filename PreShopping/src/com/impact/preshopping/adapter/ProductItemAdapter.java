package com.impact.preshopping.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dudev.util.Utilities;
import com.impact.preshopping.R;

public class ProductItemAdapter extends BaseAdapter {

	public static final String TAG = ProductItemAdapter.class.getSimpleName();
	public static final String PROD_TITLE = "title";
	public static final String PROD_DESC = "desc";
	public static final String PROD_DETAILS = "details";
	public static final String PROD_ID = "_id";
	public static final String PROD_FILE_PATH = "filePath";

	public static final int MAX_ICON_SIZE = 200;

	public interface IOnProductIconClicked {
		public void onIconClicked(int position, String id);
	}

	public interface IOnProductTextClicked {
		public void onTextClicked(int position, String id);
	}

	private Context context;
	private List<HashMap<String, String>> products;
	private IOnProductIconClicked iOnIconClicked;
	private IOnProductTextClicked iOnTextClicked;
	private float MAX_ICON_SIZE_IN_DPI = 200;
	private List<WeakReference<Bitmap>> weakRef = new ArrayList<WeakReference<Bitmap>>();

	public ProductItemAdapter(Context context, IOnProductIconClicked iOnIconClicked, IOnProductTextClicked iOnTextClicked, List<HashMap<String, String>> products) {
		this.context = context;
		this.products = products;
		this.iOnIconClicked = iOnIconClicked; // Don't forget to check a null
												// value.
		this.iOnTextClicked = iOnTextClicked; // Don't forget to check a null
												// value.
		MAX_ICON_SIZE_IN_DPI = Utilities.convertPixelsToDp(MAX_ICON_SIZE, context);
	}

	@Override
	public int getCount() {

		return products.size();
	}

	@Override
	public Object getItem(int position) {

		return products.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(products.get(position).get(PROD_ID));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View wholeView = inflater.inflate(R.layout.product_item_row, null, false);
		SquareImageView imgView = (SquareImageView) wholeView.findViewById(R.id.imageViewProdIcon);
		TextView txtTitle = (TextView) wholeView.findViewById(R.id.textViewProdTitle);
		TextView txtDesc = (TextView) wholeView.findViewById(R.id.textViewProdDesc);

		HashMap<String, String> prodMap = products.get(position);
		String title = prodMap.get(PROD_TITLE);
		String desc = prodMap.get(PROD_DESC);
		String details = prodMap.get(PROD_DETAILS);
		final String id = prodMap.get(PROD_ID);
		String filePath = prodMap.get(PROD_FILE_PATH);

		txtTitle.setText(TextUtils.isEmpty(title) ? "" : title);
		txtDesc.setText(TextUtils.isEmpty(desc) ? "" : desc);

		Bitmap img = getBitmap(filePath);
		if (img != null) {
			imgView.setImageBitmap(img);
			weakRef.add(new WeakReference<Bitmap>(img));
		}

		imgView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (iOnIconClicked != null) {
					iOnIconClicked.onIconClicked(pos, id);
				}
			}
		});

		LinearLayout videoDetailsContainer = (LinearLayout) wholeView.findViewById(R.id.videoDetailsContainer);
		videoDetailsContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (iOnTextClicked != null) {
					iOnTextClicked.onTextClicked(pos, id);
				}
			}
		});

		int totalImage = Utilities.getTotalNumImageByProdId(context, id);
		int totalVideo = Utilities.getTotalNumVideoByProdId(context, id);
		
		TextView txvTotalImage = (TextView) wholeView.findViewById(R.id.textViewProdTotalImage);
		TextView txvTotalVideo = (TextView) wholeView.findViewById(R.id.textViewProdTotalVideo);
		
		txvTotalImage.setText(totalImage == 0 ? "" : String.format(txvTotalImage.getText().toString(), "" + totalImage));
		txvTotalVideo.setText(totalVideo == 0 ? "" : String.format(txvTotalVideo.getText().toString(), "" + totalVideo));
		
		return wholeView;
	}

	private Bitmap getBitmap(String filePath) {
		Bitmap b = null;

		try {
			String path = Uri.parse(filePath).getPath();
			b = Utilities.decodeSampledBitmapFromFile(path, (int) MAX_ICON_SIZE_IN_DPI, (int) MAX_ICON_SIZE_IN_DPI);
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		return b;
	}

}
