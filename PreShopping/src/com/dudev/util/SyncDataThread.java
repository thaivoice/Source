package com.dudev.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.impact.preshopping.model.Category;
import com.impact.preshopping.model.Company;
import com.impact.preshopping.model.Group;
import com.impact.preshopping.model.ImMedia;
import com.impact.preshopping.model.Product;

public class SyncDataThread implements Callable {

	public static final int FLAG_SHOW_PROGRESS_DIALOG_LOGIN = 1;
	public static final int FLAG_DISMISS_PROGRESS_DIALOG_AND_START_DOWNLOAD = 2;
	public static final int FLAG_SHOW_IMAGE = 3;
	public static final String TAG = SyncDataThread.class.getSimpleName();

	private Handler handler;
	private Context context;
	private String url;
	private String method;
	private HashMap<String, Object> data;
	private Object result = new Object();
	private boolean success;

	public SyncDataThread(Context context, Handler handler, String url, String method, HashMap<String, Object> data) {
		this.context = context;
		this.handler = handler;
		this.url = url;
		this.method = method;
		this.data = data;
	}


	public boolean run() {

		if (handler != null) {
			handler.sendEmptyMessage(FLAG_SHOW_PROGRESS_DIALOG_LOGIN);	
		}
		
		try {
			this.getClass().getMethod(method).invoke(this);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "" + e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "" + e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "" + e);
		} catch (InvocationTargetException e) {
			Log.e(TAG, "" + e);
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}

		success = true;
		
		if (handler != null) {
			Message msg = Message.obtain(handler, FLAG_DISMISS_PROGRESS_DIALOG_AND_START_DOWNLOAD, new Object());
			handler.sendMessage(msg);	
		}
		
		return success;
	}

	public boolean syncData() {
		
		try {
			HashMap<String, Object> result = callWebService();
			StringBuilder data = (StringBuilder) result.get("DATA");
//			String wellFormatData = data.toString().replace("\\/", "/");
//			InputStreamReader is = new InputStreamReader(context.getResources().openRawResource(R.raw.json_sample));
//			List<String> lines = IOUtils.readLines(is);
//			StringBuffer buff = new StringBuffer();
//			for (String line : lines) {
//				buff.append(line);
//			}
//			JSONArray objArr = new JSONArray(data.toString());
			JSONObject response = new JSONObject(data.toString());
			String responseCode = response.getString("status");
			String responseDesc = response.getString("msg");
//			if (!responseCode.equals("200")) {
//				return false;
//			}
			
			
			JSONArray objArr = response.getJSONArray("data");
			List<JSONObject> compList = new ArrayList<JSONObject>();
			for (int i = 0; i < objArr.length(); i++) {
				compList.add(objArr.getJSONObject(i));
			}
			
			List<Company> companyList = getCompanyList(objArr);
            Utilities.insertNewCompanies (companyList, context);
//			ServerResponse_Login loginInfo = new ServerResponse_Login(responseCode, responseDesc);
//			loginInfo.setCompanies(companyList);
			
			List<JSONArray> catList = new ArrayList<JSONArray>();
			List<JSONArray> grpList = new ArrayList<JSONArray>();
			List<JSONArray> prodList = new ArrayList<JSONArray>();
			
			List<List<Category>> listOfCats = new ArrayList<List<Category>>();
			for (int i = 0; i < compList.size(); i++) {
				JSONObject obj = compList.get(i);
				String companyId = obj.getString("companyID");
				JSONArray catArr = obj.getJSONArray("CatagoryList");
				listOfCats.add(getCategories(companyId, catArr));
				catList.add(catArr);
			}
			
			
            Utilities.interNewCategories(listOfCats, context);

			List<List<Group>> listOfGrps = new ArrayList<List<Group>>();
			for (int i = 0; i < catList.size(); i++) {
				JSONArray cArr = catList.get(i);
				for (int a = 0; a <cArr.length(); a++) {
					JSONObject c = cArr.getJSONObject(a);
					JSONArray gArr = c.getJSONArray("GroupList");
					List<Group> grps = getGroups(gArr);
					listOfGrps.add(grps);
					grpList.add(gArr);
				}
			}
			Utilities.insertNewGroups(listOfGrps, context);
			
			List<List<Product>> listOfProds = new ArrayList<List<Product>>();
			for (int i = 0; i < grpList.size(); i++) {
				JSONArray gArr = grpList.get(i);
				for (int a = 0; a < gArr.length(); a++) {
					JSONObject g = gArr.getJSONObject(a);
					JSONArray pArr = g.getJSONArray("ProductList");
					listOfProds.add(getProducts(pArr));
					prodList.add(pArr);
				}
			}
            Utilities.insertNewProducts(listOfProds, context);
			
			List<List<ImMedia>> listOfMedias = new ArrayList<List<ImMedia>>();
			for (int i = 0; i < prodList.size(); i++) {
				JSONArray pArr = prodList.get(i);
				for (int a = 0; a < pArr.length(); a++) {
					JSONObject p = pArr.getJSONObject(a);
					
					JSONArray mArr = p.getJSONArray("MediaList");
					listOfMedias.add(getMedias(mArr));
				}
			}
            Utilities.insertNewMedias(context, listOfMedias);

//			JsonReader reader = new JsonReader(new StringReader(data.toString()));
//			JsonReader reader = new JsonReader(new StringReader(buff.toString()));
//			reader.setLenient(true);
//			list = gson.fromJson(reader, ArrayList.class);
		} catch (Exception e) {
			Log.e(TAG, "" + e);
		}
		
		return true;
	}

	private List<Company> getCompanyList(JSONArray objArr) {
		
		List<Company> list = new ArrayList<Company>();
		
		try {
			for (int i = 0; i < objArr.length(); i++) {
				JSONObject obj = objArr.getJSONObject(i);
				String companyId = obj.getString("companyID");
				String companyName = obj.getString("comName");
				String taxId = obj.getString("taxID");
				String contactPerson = obj.getString("contactPerson");
				String phoneNumber = obj.getString("phoneNumber");
				String faxNumber = obj.getString("faxNumber");
				String logoIconUrl = obj.getString("logoIcon");
				
				Company c = new Company(companyId, companyName, taxId, contactPerson, phoneNumber, faxNumber, logoIconUrl);
				list.add(c);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		
		return list;
	}
	
	private List<Category> getCategories(String companyId, JSONArray objArr) {
		List<Category> categories = new ArrayList<Category>();
		
//		catID": "1",
//        "catName": "Books",
//        "catIcon": "http:\\/\\/mobile.bgnsolutions.com\\/media\\/image\\/thumbnail\\/1a.png",
//        "catImage"
		try {
			for (int i = 0; i < objArr.length(); i++) {
				JSONObject obj = objArr.getJSONObject(i);
				String catId = obj.getString("catID");
				String catName = obj.getString("catName");
				String catIconUrl = obj.getString("catIcon");
				
				Category c = new Category(companyId, catId, catName, catIconUrl);
				categories.add(c);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return categories;
	}
	
	private List<Group> getGroups (JSONArray objArr) {
		List<Group> groups = new ArrayList<Group>();
		
//		"grpID": "1",
//        "catID": "1",
//        "grpName": "Magazine",
//        "grpIcon": "http:\\/\\/mobile.bgnsolutions.com\\/media\\/image\\/thumbnail\\/1b.png",
        
		try {
			for (int i = 0; i < objArr.length(); i++) {
				JSONObject obj = objArr.getJSONObject(i);
				String grpId = obj.getString("grpID");
				String catId = obj.getString("catID");
				String grpName = obj.getString("grpName");
				String grpIconUrl = obj.getString("grpIcon");
				
				Group g = new Group(grpId, catId, grpName, grpIconUrl);
				groups.add(g);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return groups;
	}
	
	private List<Product> getProducts(JSONArray objArr) {
		List<Product> products = new ArrayList<Product>();
		
//		"prodID": "1",
//        "grpID": "1",
//        "prodName": "Time",
//        "prodLongName": "TIME Magazine",
//        "prodShortDesc": "TIME Magazine",
//        "prodDesc": "TIME Magazine",
//        "rating": "5",
//        "prodIcon": "itime.png",
//        "qrData": "",
//        "isActive": "\\u0001",
//        "maker": "",
//        "model": "",
		
		try {
			for (int i = 0; i < objArr.length(); i++) {
				JSONObject obj = objArr.getJSONObject(i);
				String prodId = obj.getString("prodID");
				String grpId = obj.getString("grpID");
				String prodName = obj.getString("prodName");
				String prodLongName = obj.getString("prodLongName");
				String desc = obj.getString("prodDesc");
				String shortDesc = obj.getString("prodShortDesc");
				String rating = obj.getString("rating");
				String prodIconUrl = obj.getString("prodIcon");
				String barcode = obj.getString("Barcode");
				String maker = obj.getString("maker");
				String model = obj.getString("model");
				
				Product p = new Product(prodId, grpId, prodName, prodLongName, shortDesc, desc, rating, prodIconUrl, barcode, maker, model);
				products.add(p);
			}
		} catch (Exception e) {
			Log.e("ERR", "" + e);
		}
		
		return products;
	}
	
	private List<ImMedia> getMedias (JSONArray objArr) {
		List<ImMedia> medias = new ArrayList<ImMedia>();
		
//		 "mediaID": "1",
//         "prodID": "1",
//         "mediaURL": "http:\\/\\/mobile.bgnsolutions.com\\/media\\/image\\/20a.jpg",
//         "fileSize": "0",
//         "shortDesc": "",
//         "longDesc": "",
//         "mediaType": "1"
		
		try {
			for (int i = 0; i < objArr.length(); i++) {
				
				JSONObject obj = objArr.getJSONObject(i);
				String mediaId = obj.getString("mediaID");
				String prodId = obj.getString("prodID");
				String url = obj.getString("mediaURL");
				String size = obj.getString("fileSize");
				String desc = obj.getString("shortDesc");
				String longDesc = obj.getString("longDesc");
				String type = obj.getString("mediaType");
				
				ImMedia m = new ImMedia(mediaId, prodId, url, size, desc, longDesc, type);
				medias.add(m);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return medias;
	}
	
	
	
	private HashMap<String, Object> callWebService() {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			RestClient rest = new RestClient(url);
			rest.Execute(RequestType.POST);
			String data = rest.GetResponse();
			StringBuilder b = new StringBuilder(data);
			b.deleteCharAt(0);
			b.deleteCharAt(b.length() - 1);

			result.put("DATA", b);

		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public Object call() throws Exception {
		
		boolean success = run();
		
		return Boolean.valueOf(success);
	}

}
