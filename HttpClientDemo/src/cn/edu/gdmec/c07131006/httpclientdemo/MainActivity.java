package cn.edu.gdmec.c07131006.httpclientdemo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView mytv;
	private Button mybtn;
	private HttpClient client;
	private HttpResponse response;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mytv = (TextView) findViewById(R.id.textView1);
		
		mybtn = (Button) findViewById(R.id.button1);
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				
				mytv.setText((CharSequence) msg.obj);
				
			}
		};
		
		mybtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new Thread() {
					@Override
					public void run() {

						HttpGet httpGet = new HttpGet("http://www.baidu.com");

						client = new DefaultHttpClient();
						InputStream stream = null;

						try {
							response = client.execute(httpGet);

							if (response.getStatusLine().getStatusCode() == 200) {

								HttpEntity entity = response.getEntity();

								stream = entity.getContent();

								BufferedReader reader = new BufferedReader(
										new InputStreamReader(stream));

								StringBuilder builder = new StringBuilder();

								String text = "";

								while ((text = reader.readLine()) != null) {
									
									builder.append(text);

								}
								
								Message msg =  Message.obtain();
								
								msg.obj = builder.toString();
								
								handler.sendMessage(msg);
								
								reader.close();

							}
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();

			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
