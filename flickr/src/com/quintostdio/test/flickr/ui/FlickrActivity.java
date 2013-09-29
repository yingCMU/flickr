package com.quintostdio.test.flickr.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.quintostdio.test.flickr.R;
import com.quintostdio.test.flickr.data.FetchAlbums;
import com.quintostdio.test.flickr.data.FetchPhotos;
import com.quintostdio.test.flickr.utils.ImageHelper;
import com.quintostdio.test.flickr.utils.Internet;
import com.quintostdio.test.flickr.utils.Parser;
import com.quintostdio.test.flickr.utils.MyGestureDetector;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class FlickrActivity extends Activity {
	private final int[] timeout={3,10};
	private String thePhotos;
	private final String urlpre= "http://www.flickr.com/services/rest/?method=";
	
	private Context context;
	private ProgressDialog pd;
	private String limit ="10";

	public static final String API_KEY="6d8c78b5ab409e08e4eba8d718de4f27";
	public static final String USER_ID="97433628@N06";
	public static String lat ="37.407554";
	public static String lon="-122.056968";
	private boolean exit=true;
	private Photos thePhotoList;
	public static String[] pics = null;
		
	private int photoNum = 0;
    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;
    
    ImageView imageView;
	
    public String constructURL(String method, String limit,String lat,String lon){
		//"flickr.photosets.getList
		return urlpre+method+"&api_key="+this.API_KEY+"&lat="+lat+"&lon="+lon+"&nojsoncallback=1&has_geo=true&format=json";
		
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        /*retrieving list of places*/
        String method = "flickr.photos.search";//"flickr.photosets.getList"
		String url = constructURL(method,this.limit ,this.lat, this.lon);
		try {
			
			
			 
			/*this.pics is an array of imale urls*/
			if(url!=null)
				this.pics = Parser.JA2SA(url);
			
		} catch (Exception e) {}
        
        Gallery ga = (Gallery)findViewById(R.id.Gallery01);
        ga.setAdapter(new ImageAdapter(this));
        
        imageView = (ImageView)findViewById(R.id.ImageView01);
        ga.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getBaseContext(), 
						"You have selected picture " + (arg2+1) + " of ??", 
						Toast.LENGTH_SHORT).show();
				//imageView.setImageResource(R.drawable.antartica8);
				if(pics[arg2]!=null)
				ImageHelper.setImage(imageView,pics[arg2]);
				
			}
        	
        });
        
        new Albums().execute();
    }
    
    private class Albums extends FetchAlbums{
		@Override
		public void onPhotoCategoryClick(String id) {
			setContentView(R.layout.pics);
			exit=false;
			try{
				((ViewAnimator)findViewById(R.id.PictureAnimator)).removeAllViews();
			}catch (Exception e) {}
			String url="http://www.flickr.com/services/rest/?method=flickr.photosets.getPhotos&format=json&api_key="+API_KEY+"&photoset_id="+id;
			thePhotoList = new Photos(url);
			thePhotoList.execute();
			
			photoNum=0;
			
			gestureDetector = new GestureDetector(new TheGestureDetector());
	        gestureListener = new View.OnTouchListener() {
	            public boolean onTouch(View v, MotionEvent event) {
	                return gestureDetector.onTouchEvent(event);
	            }
	        };
	        ((ViewAnimator)FlickrActivity.this.findViewById(R.id.PictureAnimator)).setOnTouchListener(gestureListener);
	        
		}
		public Albums() {super(FlickrActivity.this);}	
    }
    
    private class Photos extends FetchPhotos{
		@Override
		public void onFetchError() {}
		public Photos(String url) {super(FlickrActivity.this, url);}	
    }
    
	private class TheGestureDetector extends MyGestureDetector{

		@Override
		public void rightToLeft() {
			photoNum++;
			if(photoNum==((ViewAnimator)findViewById(R.id.PictureAnimator)).getChildCount()){
				photoNum=0;
			}
			thePhotoList.LoadPhoto(thePhotoList.thePics.get(photoNum));
			((ViewAnimator)findViewById(R.id.PictureAnimator)).showNext();			
		}

		@Override
		public void leftToRight() {
			photoNum--;			
			thePhotoList.LoadPhoto(thePhotoList.thePics.get(photoNum));
			((ViewAnimator)findViewById(R.id.PictureAnimator)).showPrevious();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		if(exit){
			super.onBackPressed();
		}else{
	        setContentView(R.layout.main);
	        ((LinearLayout) (FlickrActivity.this).findViewById(R.id.llTaPantaOla)).removeAllViews();
	        new Albums().execute();
			exit=true;
		}
	}
    public class ImageAdapter extends BaseAdapter {

    	private Context ctx;
    	int imageBackground;
    	
    	public ImageAdapter(Context c) {
			ctx = c;
			TypedArray ta = obtainStyledAttributes(R.styleable.Gallery1);
			imageBackground = ta.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 1);
			ta.recycle();
		}

		@Override
    	public int getCount() {
    		
    		return pics.length;
    	}

    	@Override
    	public Object getItem(int arg0) {
    		
    		return arg0;
    	}

    	@Override
    	public long getItemId(int arg0) {
    		
    		return arg0;
    	}

    	@Override
    	public View getView(int arg0, View arg1, ViewGroup arg2) {
    		ImageView iv = new ImageView(ctx);
    		
            URL url;
            if(pics !=null)
               try {
				url = new URL(pics[arg0]);
				Bitmap bmp = BitmapFactory.decodeStream(url.openConnection()
                        .getInputStream());
				iv.setImageBitmap(bmp);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
               // iv.setLayoutParams(new Gallery.LayoutParams(150, 120));
                iv.setBackgroundResource(imageBackground);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
            return iv;
    	}

    }
    
}