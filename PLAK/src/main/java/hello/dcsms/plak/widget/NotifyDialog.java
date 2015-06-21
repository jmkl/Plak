package hello.dcsms.plak.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hello.dcsms.plak.R;

public class NotifyDialog extends Dialog {

	private LinearLayout view;
	private Context mContext;
	private TextView tvtitle, tvmsg;
	private String msg, title;
	RelativeLayout backView;
    private boolean cancleable=true;

	public NotifyDialog(Context context) {
		super(context, android.R.style.Theme_Translucent);
		mContext = context;
	}

    public NotifyDialog isCancelable(boolean cancancle){
        this.cancleable = cancancle;
        return this;
    }

	public NotifyDialog setTitle(String title) {
		this.title = title;
		return this;
	}

	public NotifyDialog setMessage(String msg) {
		this.msg = msg;
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(cancleable);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_dialog);
		view = (LinearLayout) findViewById(R.id.notif_dialog_ContentDialog);

		tvtitle = (TextView) findViewById(R.id.notif_dialog_title);
		tvmsg = (TextView) findViewById(R.id.notif_dialog_msg);
		tvtitle.setText(title);
		tvmsg.setText(msg);
		backView = (RelativeLayout) findViewById(R.id.notif_dialog_C_rootView);
		backView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if ( event.getX() < view.getLeft()
						|| event.getX() > view.getRight()
						|| event.getY() > view.getBottom()
						|| event.getY() < view.getTop()) {
					if(cancleable)
                        Animate(view, false);

				}
				return false;
			}
		});


	}

	@Override
	public void show() {
		super.show();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Animate(view, true);
				
			}
		}, 1000);
		
	}



	private void Animate(final View view, final boolean in) {
		if (in){
			view.setVisibility(View.VISIBLE);
		
		}AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y,
				in ? new float[] { view.getHeight()*2, 0f } : new float[] { 0f,
						view.getHeight() }));
		set.setDuration(100);
		set.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator paramAnimator) {
				if (in)
					view.setTranslationY(view.getHeight());

			}

			@Override
			public void onAnimationRepeat(Animator paramAnimator) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator paramAnimator) {
				if (!in)
					dismiss();

			}

			@Override
			public void onAnimationCancel(Animator paramAnimator) {
				// TODO Auto-generated method stub

			}
		});
		set.start();
	}

}
