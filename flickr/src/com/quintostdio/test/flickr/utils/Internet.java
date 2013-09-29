package com.quintostdio.test.flickr.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;



public class Internet {
	public static String retrieve(String url){
		System.out.println(url);
		HttpURLConnection connection = null;
		BufferedReader rd;
		StringBuilder sb;
		try {
			URL address = new URL(url);
			connection = (HttpURLConnection) address.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			rd= new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			sb = new StringBuilder();
			String line = null;
			while ((line = rd.readLine()) != null) {
				if (!line.trim().equals("")) {
					sb.append(line + '\n');
				}
			}

			String jsp = sb.toString().trim();
			
			return jsp;
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		finally {
			/* close the connection, set all objects to null */
			connection.disconnect();
			rd = null;
			sb = null;
			connection = null;

		}
		
	}
	public String GetRequest(String url, List<NameValuePair>  params, int[] timouts) throws Exception{
		InputStream is = null;
		String serverResponce = ""; 
		if(params!=null){
			String paramString = URLEncodedUtils.format(params, "utf-8");
			url += paramString;
		}
		
		HttpClient httpclient = new DefaultHttpClient(timeOuts(timouts[0],timouts[1]));
		HttpGet get = new HttpGet(url);
        HttpResponse responseGet = httpclient.execute(get);  
        HttpEntity resEntityGet = responseGet.getEntity();  
        if (resEntityGet != null) {  
        	is = resEntityGet.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");	
			}
			is.close();
			serverResponce=sb.toString();
        }
	
		return serverResponce;		
	}
	
	public String PostRequest(String url, List<NameValuePair>  params, int[] timouts) throws Exception{
		InputStream is = null;
		String serverResponce = ""; 
		
		HttpClient httpclient = new DefaultHttpClient(timeOuts(timouts[0],timouts[1]));
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if(!(entity.equals(null))){
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");	
			}
			is.close();
			serverResponce=sb.toString();
		}
		return serverResponce;
	}
	
	
	public HttpParams timeOuts(int connectionSecs, int socketSecs){
	    HttpParams httpParameters = new BasicHttpParams();
	    int timeoutConnection = connectionSecs*1000;
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	    int timeoutSocket = socketSecs*1000;
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
	    return httpParameters;
	}

}
