package com.impact.preshopping.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.R.color;
import com.dudev.util.Utilities;
import com.impact.preshopping.R;
import com.impact.preshopping.activity.VideoListActivity;
import com.impact.preshopping.activity.VideoListActivity.IOnExit;

@SuppressWarnings("rawtypes")
public class VideoListAdapter extends ArrayAdapter<HashMap<String, Object>> implements IOnExit {

	public interface IOnItemClicked {
		public void onItemClicked(int position);
	}

	public interface IOnChecked {
		public void onChecked(int position);
	}

	public VideoListAdapter(Context context, int resource, int textViewResourceId, List<HashMap<String, Object>> list) {
		super(context, resource, textViewResourceId, list);

		mContext = context;
		this.list = list;
	}

	public VideoListAdapter(Context context, int resource, int textViewResourceId, IOnItemClicked iOnItemClicked, IOnChecked iOnChecked, List<HashMap<String, Object>> list) {
		super(context, resource, textViewResourceId, list);

		mContext = context;
		this.list = list;
		this.iOnItemClicked = iOnItemClicked;
		this.iOnChecked = iOnChecked;
	}

	private Context mContext;
	private List<HashMap<String, Object>> list;
	private List<Integer> checkedBoxs = new ArrayList<Integer>();
	private IOnItemClicked iOnItemClicked;
	private IOnChecked iOnChecked;

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		final int index = position;
		View wholeView = null;

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (position == 0) {

			wholeView = inflater.inflate(R.layout.activity_video_product_detail_item, null, false);

			TextView txtTitle = (TextView) wholeView.findViewById(R.id.txtTitle);
			TextView txtLngDesc = (TextView) wholeView.findViewById(R.id.txtDetails);

			HashMap<String, Object> data = list.get(position);
			List<String> detail = (List<String>) data.get("PROD_DETAIL");
			String shortName = detail.get(0);
			String lngDesc = detail.get(1);
			String iconFilePath = detail.get(2);
			txtTitle.setText(shortName);
			txtLngDesc.setText(lngDesc);

			if (!TextUtils.isEmpty(iconFilePath)) {
				int sizeDpi = (int) Utilities.convertPixelsToDp(500, mContext);
				Uri uri = Uri.parse(iconFilePath);
				SquareImageView img = (SquareImageView) wholeView.findViewById(R.id.image1);
				img.getLayoutParams().width = sizeDpi;
				img.getLayoutParams().height = sizeDpi;
				img.setImageURI(uri);
			}
		} else {
			wholeView = inflater.inflate(R.layout.video_list_item, null, false);
			HashMap<String, Object> data = list.get(position);

			String title = data.get("TITLE").toString();
			TextView txtTitle = (TextView) wholeView.findViewById(R.id.txvTitle);
			if (!TextUtils.isEmpty(title)) {
				txtTitle.setText(title);
			}

			String desc = data.get("DESC").toString();
			TextView txtDesc = (TextView) wholeView.findViewById(R.id.textViewExtraInfo);
			if (!TextUtils.isEmpty(desc)) {
				txtDesc.setText(desc);
			}

			VideoClickedListener listener = new VideoClickedListener(position);
			wholeView.setOnClickListener(listener);

			CheckBox check = (CheckBox) wholeView.findViewById(R.id.checkBoxSave);
			String filePath = data.get("FILE_PATH").toString();
			if (!TextUtils.isEmpty(filePath)) {
				if ((new File(Uri.parse(filePath).getPath()).exists())) {
					check.setVisibility(View.GONE);
				} else {
					check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if (isChecked) {
								checkedBoxs.add(Integer.valueOf(index));
								Log.i("info", "checked item number: " + index);

								if (iOnChecked != null) {
									iOnChecked.onChecked(index);
								}

							} else {
								checkedBoxs.remove(Integer.valueOf(index));
								Log.i("info", "unchecked item number: " + index);
								if (iOnChecked != null) {
									iOnChecked.onChecked(-1 * index);
								}
							}
						}
					});
				}

			} else {
				check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							checkedBoxs.add(Integer.valueOf(index));
							Log.i("info", "checked item number: " + index);

							if (iOnChecked != null) {
								iOnChecked.onChecked(index);
							}

						} else {
							checkedBoxs.remove(Integer.valueOf(index));
							Log.i("info", "unchecked item number: " + index);
							if (iOnChecked != null) {
								iOnChecked.onChecked(-1 * index);
							}
						}
					}
				});
			}
		}

//		if (VideoListActivity.seenItemList.contains(position)) {
//			wholeView.setBackgroundColor(color.abs__background_holo_light);
//		}
		return wholeView;
	}

	class VideoClickedListener implements OnClickListener {
		private int position;

		public VideoClickedListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			try {
				if (iOnItemClicked != null) {
					iOnItemClicked.onItemClicked(position);
				}

			} finally {

			}
		}

	}

	@Override
	public void onExitActivity() {

	}
}