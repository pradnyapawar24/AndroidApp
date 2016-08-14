package com.example.myapp;

import android.graphics.Bitmap;

public class Teasers {
	
	private String title;
	private String small_teaser_image;
	private String teaser_image;
	private String url;
	private boolean has_video;
    private boolean is_live;
    private boolean is_breaking;
	
	private String public_url;
	//private Bitmap btImage;
	
	public String getTitle() {
		return title;
	}

	public String getSmall_teaser_image() {
		return small_teaser_image;
	}
	
	public boolean getIs_live()
	{
		return is_live;
	}
	
	public boolean getIs_breaking()
	{
		return is_breaking;
	}
	
	public boolean getHas_video()
	{
		return has_video;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public Teasers(String title, String small_teaser_image, String url, boolean has_video, boolean is_breaking, boolean is_live)
	{
		super();
		this.title = title;
		this.small_teaser_image = small_teaser_image;
		this.url = url;
		this.is_breaking = is_breaking;
		this.is_live = is_live;
		this.has_video = has_video;
	} 	
}
