package com.quintostdio.test.flickr.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;
 
public class Parser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    // constructor
    public Parser() {
 
    }
    public static String jpg(JSONObject jo){
    	//http://farm1.staticflickr.com/36/85217874_b808350c59.jpg
    	///{server-id}/{id}_{o-secret}_o.(jpg|gif|png)
    	String url = null;
    	try {
    		url = "http://farm"+jo.getString("farm")+".staticflickr.com/"
    		+jo.getString("server")+"/"+jo.getString("id")+"_"+
    jo.getString("secret")+"_m.jpg";
    	} catch (JSONException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	//jo.get("server");
    	//System.out.println("test+"+url);
    	return url;
    	
    }
    /*
     * takes a searching url string 
     * outputs an array of url strings
     */
    public static String[] JA2SA(String url){
    	System.out.println(url);
    	JSONObject json = getJSONFromUrl(url);
    	JSONObject jobject;
    	String[] res = null;
		try {
			jobject = json.getJSONObject("photos");
			JSONArray ja = jobject.getJSONArray("photo");
	    	res = new String[ja.length()];
	    	for(int i=0;i<ja.length();i++){
	    		
	    			res[i]=jpg(ja.getJSONObject(i));
	    		
	    			// TODO Auto-generated catch block
	    			
	    	
	    	}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
    	//for(String cur:res)
    		//System.out.println("!!!   "+cur);
    	return res;
    	
    }

    public static JSONObject getJSONFromUrl(String url) {
 
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(url);
            httpPost.addHeader("accept", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();           
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
            	//System.out.println(json);
                sb.append(line + "\n");
            }
            is.close();
            
            json = sb.toString();
            //System.out.println("\n\n"+json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
       // System.out.println(jObj);
        return jObj;
 
    }
}