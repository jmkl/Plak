package hello.dcsms.plak;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Applican extends Activity {

	Handler h;
	Button b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thanks);
		((TextView) findViewById(R.id.thankyou)).setText(" Hello,"
				+ "if you do like this apps?,\n"
				+ "please consider do some donation\n\n"
				+ ""
				+ "+6289647075271\n"
				+ "http://fb.com/jimikill666\n\n\n\n"
				+ ""
				+ ""
				+ "thank you, god bless...\n\n"
				+ ""
				+ ""
				+ "/jmkl");
		setFinishOnTouchOutside(false);

		b = (Button) findViewById(R.id.exitbutton);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (b.getText().equals("Exit Now"))
					System.exit(1);

			}
		});
		h = new Handler();
		h.postDelayed(run, 1000);
	}

	@Override
	public void onBackPressed() {

	}

	// TODO FIXME

	int x = 5;
	private Runnable run = new Runnable() {

		@Override
		public void run() {
			h.postDelayed(run, 1000);
			if (x == 0) {
				b.setText("Exit Now");
				h.removeCallbacks(run);
				System.exit(1);
			} else
				b.setText("Exit in " + x + " seconds");
			x--;

		}
	};

}
