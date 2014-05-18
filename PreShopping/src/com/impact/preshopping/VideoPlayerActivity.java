package com.impact.preshopping;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.VideoView;

import com.stickmanventures.android.example.immersive_videoplayer.ImmersiveVideoplayer;
import com.stickmanventures.android.example.immersive_videoplayer.entities.Video;

public class VideoPlayerActivity extends Activity {

	private Uri uri;
	private VideoView videoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_player);
		
		try {
			uri = (Uri)getIntent().getExtras().get("URI");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		videoView = (VideoView) findViewById(R.id.videoView1);
		if (uri != null) {
//			MediaController mediaController= new MediaController(this);
//			videoView.setMediaController(mediaController);
//			videoView.setVideoURI(uri);
//			videoView.requestFocus();
//			videoView.start();
			Intent intent = null;
			                  
			      try {
			          intent = new Intent(this,
			                      Class.forName("com.stickmanventures.android.example.immersive_videoplayer.ui.activities.VideoPlayerActivity")
			                 );
			      } catch (ClassNotFoundException e) {
			         e.printStackTrace();
			      } finally {     
			         // Create a video object to be passed to the activity
			         Video video = new Video(uri.toString());
			         
//			         video.setTitle("Big Buck Bunny");
//			         video.setAuthor("the Blender Institute");
//			         video.setDescription("A short computer animated film by the Blender Institute, part of the Blender Foundation. Like the foundation's previous film Elephants Dream, the film was made using Blender, a free software application for animation made by the same foundation. It was released as an Open Source film under Creative Commons License Attribution 3.0.");
			                     
			         // Launch the activity with some extras
			         intent.putExtra(ImmersiveVideoplayer.EXTRA_LAYOUT, "0");
			         intent.putExtra(Video.class.getName(), video);
			         startActivity(intent);
			    }
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video_player, menu);
		return true;
	}

}
