package hello.dcsms.plak.Utils;

import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class RLParam {

    public static LayoutParams NULL() {
        LayoutParams par = new LayoutParams(0,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        return par;
    }
	public static LayoutParams WRAP_KONTEN() {
		LayoutParams par = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
	
		return par;
	}
    /*
    * new shit
    * */
    public static LayoutParams RIGHT_OF_LEFT_OF(int anchorID_R,int anchorID_L) {
        LayoutParams par = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        par.addRule(RelativeLayout.RIGHT_OF, anchorID_R);
        par.addRule(RelativeLayout.LEFT_OF, anchorID_L);

        return par;
    }


	public static LayoutParams RIGHT_OF(int anchorID) {
		LayoutParams par = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		par.addRule(RelativeLayout.RIGHT_OF, anchorID);
		return par;
	}

	public static LayoutParams LEFT_OF(int anchorID) {
		LayoutParams par = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		par.addRule(RelativeLayout.LEFT_OF, anchorID);

		return par;
	}

	public static LayoutParams RIGHT_OF(int anchorID, int w) {
		LayoutParams par = new LayoutParams(w, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		par.addRule(RelativeLayout.RIGHT_OF, anchorID);
		return par;
	}

	public static LayoutParams LEFT_OF(int anchorID, int w) {
		LayoutParams par = new LayoutParams(w, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		par.addRule(RelativeLayout.LEFT_OF, anchorID);
		return par;
	}

	public static LayoutParams KIRI(int w) {
		LayoutParams kiri = new LayoutParams(w, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		kiri.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		return kiri;
	}

	public static LayoutParams KANAN(int w) {
		LayoutParams kanan = new LayoutParams(w, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		kanan.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		return kanan;
	}

	public static LayoutParams TENGAH(int w) {
		LayoutParams tengah = new LayoutParams(w, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		tengah.addRule(RelativeLayout.CENTER_IN_PARENT);

		return tengah;
	}

	public static LayoutParams KIRI() {
		LayoutParams kiri = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		kiri.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		return kiri;
	}

	public static LayoutParams KANAN() {
		LayoutParams kanan = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		kanan.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		return kanan;
	}

	public static LayoutParams TENGAH() {
		LayoutParams tengah = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		tengah.addRule(RelativeLayout.CENTER_IN_PARENT);

		return tengah;
	}


}
