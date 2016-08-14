package com.example.myapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private List<Teasers> myTeasers = new ArrayList<Teasers>();
	HttpClient client;
	final static String URL = "http://app-backend.tv2.dk/articles/v1/?section_identifier=2";
	//String classes[] = new String[25];
	JSONObject obj;
	ListView listView1;
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView1 = (ListView) findViewById(R.id.listView1);
		
		client = new DefaultHttpClient();
		new Read().execute("title");
	}
	
	private void registerClickCallback() {
	listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
			
			Teasers selectedTeaser = myTeasers.get(position);
			String url = selectedTeaser.getUrl();
			Bundle b = new Bundle();
			b.putString("urlToNavigate", url);
			Intent i = new Intent(MainActivity.this, WebViewActivity.class);
			i.putExtras(b);
			startActivity(i);
		}
	});
	}
		
	
	public void allObjects() throws ClientProtocolException, IOException, JSONException
	{
		//String url = new StringBuilder(URL);
		
		HttpPost get = new HttpPost(URL);
		HttpResponse r = client.execute(get);
		int status = r.getStatusLine().getStatusCode();
		if(status == 200)
		{
			HttpEntity e = r.getEntity();
			String data = EntityUtils.toString(e);
			JSONArray timeline = new JSONArray(data);
			
			for(int i = 0;i<timeline.length();i++)
			{
				JSONObject object = timeline.getJSONObject(i);			
				String title = object.getString("title");
				String smallImageURL = object.getString("small_teaser_image");
				String url = object.getString("url");
				boolean isLive = object.getBoolean("is_live");
				boolean isBreaking = object.getBoolean("is_breaking");
				boolean hasVideo = object.getBoolean("has_video");
				
				myTeasers.add(new Teasers(title,smallImageURL,url, hasVideo, isBreaking, isLive));
				//classes[i] = id;
			}
		}
		else
		{
			Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT);
		}
	}
	public class Read extends AsyncTask<String, Integer, String>{

		 @Override
	     protected void onPreExecute() {
	            super.onPreExecute();
	            // Showing progress dialog
	            pDialog = new ProgressDialog(MainActivity.this);
	            pDialog.setMessage("Please wait...");
	            pDialog.setCancelable(false);
	            pDialog.show();
	 
	     }
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				allObjects();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			 // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            
			ArrayAdapter<Teasers> adapter = new MyListAdapter();
	        
	        listView1.setAdapter(adapter);
	        registerClickCallback();
		}
	}
	
	private class MyListAdapter extends ArrayAdapter<Teasers> {

		public MyListAdapter() {
			super(MainActivity.this, R.layout.list_items, myTeasers);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if(itemView == null)
			{
				itemView = getLayoutInflater().inflate(R.layout.list_items, parent, false);
			}
			
			Teasers currentTeaser = myTeasers.get(position);
			
			//set image
			ImageView imageView = (ImageView)itemView.findViewById(R.id.item_image);
			int loader = R.drawable.loader;
			ImageLoader imgLoader = new ImageLoader(getApplicationContext());
	        String image_url = currentTeaser.getSmall_teaser_image();
	        imgLoader.DisplayImage(image_url, loader, imageView);
				
			//set title
			TextView txtView = (TextView)itemView.findViewById(R.id.item_title);
			txtView.setText(currentTeaser.getTitle());
			
			//set text if breaking news
			TextView txtViewOfBreakingNews = (TextView)itemView.findViewById(R.id.tv_isBreakingNews);
			if(currentTeaser.getIs_breaking() == true)
				txtViewOfBreakingNews.setText("Breaking News");
			else
				txtViewOfBreakingNews.setText("");
			//set text for is live
			TextView txtViewOfIsLive = (TextView)itemView.findViewById(R.id.tv_isLive);
			if(currentTeaser.getIs_live() == true)
				txtViewOfIsLive.setText("Live");
			else
				txtViewOfIsLive.setText("");
			
			//set text for has video
			TextView txtViewHasVideo = (TextView)itemView.findViewById(R.id.tv_hasVideo);
			if(currentTeaser.getHas_video() == true)
				txtViewHasVideo.setText("Has Video");
			else
				txtViewHasVideo.setText("");
			
			return itemView;
		}
	}
}
