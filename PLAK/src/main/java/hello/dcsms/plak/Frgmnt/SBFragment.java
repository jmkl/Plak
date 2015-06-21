package hello.dcsms.plak.Frgmnt;

import android.app.Fragment;
import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hello.dcsms.plak.R;

public class SBFragment extends Fragment implements OnLongClickListener {
	TextView jam, ntf, sgnl, btrai;
	RelativeLayout host;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.hello, null);
		findView(v);
		return v;
	}

	private void findView(View v) {
		jam = (TextView) v.findViewById(R.id.plak_jam);
		ntf = (TextView) v.findViewById(R.id.plak_notificon);
		sgnl = (TextView) v.findViewById(R.id.plak_signal);
		btrai = (TextView) v.findViewById(R.id.plak_batrai);
		host = (RelativeLayout) v.findViewById(R.id.host_layout);
		jam.setTag(jam.getText());
		ntf.setTag(ntf.getText());
		sgnl.setTag(sgnl.getText());
		btrai.setTag(btrai.getText());

		setOnLongClick(jam, ntf, sgnl, btrai);
		host.setOnDragListener(new MDragEvent());
	}

	void setOnLongClick(View... v) {
		for (View view : v)
			view.setOnLongClickListener(this);
	}

	private class MDragEvent implements View.OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				v.setBackgroundColor(Color.RED);
				v.invalidate();
				break;

			case DragEvent.ACTION_DRAG_ENTERED:
			case DragEvent.ACTION_DRAG_LOCATION:
				v.setBackgroundColor(Color.GREEN);
				v.invalidate();
				break;

			case DragEvent.ACTION_DRAG_EXITED:

				v.setBackgroundColor(Color.BLUE);
				v.invalidate();

				break;

			case DragEvent.ACTION_DROP:

				ClipData.Item item = event.getClipData().getItemAt(0);
				String dragData = (String) item.getText();

				v.setBackgroundColor(Color.BLACK);
				v.invalidate();

				// Returns true. DragEvent.getResult() will return true.

				break;

			case DragEvent.ACTION_DRAG_ENDED:

				v.setBackgroundColor(Color.BLACK);

				v.invalidate();

				break;

			// An unknown action type was received.
			default:

				break;
			}
			return false;
		}

	}

	private static class MDragShadowBuilder extends DragShadowBuilder {
		public static Drawable shadow;

		public MDragShadowBuilder(View v) {
			super(v);
			shadow = new ColorDrawable(Color.GREEN);
		}

		@Override
		public void onProvideShadowMetrics(Point size, Point touch) {
			int width, height;
			width = getView().getWidth() / 2;
			height = getView().getHeight() / 2;

			// The drag shadow is a ColorDrawable. This sets its dimensions to
			// be the same as the
			// Canvas that the system will provide. As a result, the drag shadow
			// will fill the
			// Canvas.
			shadow.setBounds(0, 0, width, height);

			// Sets the size parameter's width and height values. These get back
			// to the system
			// through the size parameter.
			size.set(width, height);

			// Sets the touch point's position to be in the middle of the drag
			// shadow
			touch.set(width / 2, height / 2);
		}

		@Override
		public void onDrawShadow(Canvas canvas) {
			shadow.draw(canvas);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
		ClipData dragData = new ClipData((CharSequence) v.getTag(),
				new String[] { "text/plain" }, item);
		View.DragShadowBuilder myShadow = new MDragShadowBuilder(v);
		v.startDrag(dragData, myShadow, null, 0);
		switch (v.getId()) {
		case R.id.plak_jam:

			break;

		case R.id.plak_notificon:

			break;

		case R.id.plak_signal:

			break;

		case R.id.plak_batrai:

			break;
		}
		return false;
	}
}
