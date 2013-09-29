package com.quintostdio.test.flickr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageHelper {

	public static Object fetch(String address) throws MalformedURLException,
    IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }  

    private static Bitmap ImageOperations( String url) {
        try {
        	URL urll = new URL(url);
			Bitmap bmp = BitmapFactory.decodeStream(urll.openConnection()
                    .getInputStream());
			
            
            
            
            return bmp;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
    
    
    public static void setImage(ImageView iv,String url){
    	try{
            Bitmap bmp = ImageOperations(url);
    		iv.setImageBitmap(bmp);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
           // iv.setLayoutParams(new Gallery.LayoutParams(150, 120));
            //iv.setBackgroundResource(imageBackground);
    		//String url = "http://farm1.staticflickr.com/36/85217874_b808350c59_m.jpg";           
            
            iv.setImageBitmap(bmp);
           iv.setMinimumWidth(800);
        iv.setMinimumHeight(800);
        /*
         * 
        Image01.setMaxWidth(width);
        Image01.setMaxHeight(height);
             */
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }


        
        
    }
}
