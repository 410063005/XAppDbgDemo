package cn.edu.hust.cm.xappdbg.demo;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sonymobile.tools.xappdbg.XAppDbgServer;
import com.sonymobile.tools.xappdbg.properties.XAppDbgPropDescr;
import com.sonymobile.tools.xappdbg.properties.XAppDbgPropertiesModule;

public class ToastPosActivity extends Activity {

	private static final int UPDATE_PERIOD = 2000;

	private static final List<String> ACCEPTED_POS = Arrays.asList("top",
			"center", "bottom");

	public static class ToastPos {
		@XAppDbgPropDescr("control toast's position(top, center, bottom)")
		public static String positsion;
		
		public static void doPrintLog() {
			Log.i("ToastPosActivity", "log from XAppDbg PC");
		}
	}

	protected TextView mPosText;

	private XAppDbgServer mDbgServer;

	private final Runnable mUpdateTask = new Runnable() {
		public void run() {
			if (TextUtils.isEmpty(ToastPos.positsion)
					|| !ACCEPTED_POS.contains(ToastPos.positsion)) {
				mPosText.setText(R.string.demo_activity_tv_pos_default);
			} else {
				mPosText.setText(ToastPos.positsion);
			}
			mPosText.postDelayed(mUpdateTask, UPDATE_PERIOD);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPosText = (TextView) findViewById(R.id.demo_activity_tv_pos_text);

		mPosText.postDelayed(mUpdateTask, UPDATE_PERIOD);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPosText.removeCallbacks(mUpdateTask);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mDbgServer = new XAppDbgServer();
		mDbgServer.addModule(new XAppDbgPropertiesModule(ToastPos.class));
		mDbgServer.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mDbgServer.stop();
	}

	public void onClick(View view) {
		Toast toast = Toast.makeText(this, "toast", Toast.LENGTH_SHORT);
		if ("top".equals(ToastPos.positsion)) {
			toast.setGravity(Gravity.TOP, 0, 0);
		} else if ("center".equals(ToastPos.positsion)) {
			toast.setGravity(Gravity.CENTER, 0, 0);
		} else if ("bottom".equals(ToastPos.positsion)) {
			toast.setGravity(Gravity.BOTTOM, 0, 0);
		} else {
			// NO OP
		}
		toast.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
