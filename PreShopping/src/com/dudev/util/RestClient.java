package com.dudev.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class RestClient {

	private ArrayList<NameValuePair> params;
	private ArrayList<NameValuePair> headers;
	private String url;
	private String response;
	private int responseCode;

	public RestClient(String url) {
		this.url = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	public void AddHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public void AddParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	private String ConvertStreamToString(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				Log.e("REST_CLIENT", "ConvertStreamToString: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public void Execute(RequestType requestType) throws Exception {
		switch (requestType) {
		case GET: {
			String combinedParams = "";
			if (!params.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : params) {
					String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");

					if (combinedParams.length() > 1)
						combinedParams += "&" + paramString;
					else
						combinedParams += paramString;
				}
			}
			HttpGet request = new HttpGet(url + combinedParams);

			for (NameValuePair h : headers)
				request.addHeader(h.getName(), h.getValue());

			ExecuteRequest(request, url);
			break;
		}
		case POST: {
			HttpPost request = new HttpPost(url);

			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			if (!params.isEmpty()) {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}

			ExecuteRequest(request, url);
			break;
		}
		}
	}

	public void ExecuteRequest(HttpUriRequest request, String url) {
	    
	    Log.i("endpoint", url);
	    
		HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				InputStream in = entity.getContent();
				response = ConvertStreamToString(in);
				in.close();
			}
		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("REST_CLIENT", "Execute Request: " + e.getMessage());
			client.getConnectionManager().shutdown();

			e.printStackTrace();
		}
	}
	
	public void login(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse;
		HttpPost request = new HttpPost(url);
		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				InputStream in = entity.getContent();
				response = ConvertStreamToString(in);
				in.close();
			}
		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("REST_CLIENT", "Execute Request: " + e.getMessage());
			client.getConnectionManager().shutdown();

			e.printStackTrace();
		}
	}

	public String GetResponse() {
		return response;
	}

	public int GetResponseCode() {
		return responseCode;
	}
}