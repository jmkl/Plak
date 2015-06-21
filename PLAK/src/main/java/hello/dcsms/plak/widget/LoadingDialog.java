package hello.dcsms.plak.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import android.animation.AnimatorSet;

import hello.dcsms.plak.R;

/**
 * Created by jmkl on 5/14/2015.
 */
public class LoadingDialog extends Dialog {
    AnimatorSet set;

    public LoadingDialog(Context context) {
        this(context, 0);
    }

    public LoadingDialog(Context context, int theme) {

        super(context, R.style.loading_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingdialog);
        ImageView iv = ((ImageView) findViewById(R.id.ic_x));
        iv.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.rotation));

    }



}
