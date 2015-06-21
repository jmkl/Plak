package hello.dcsms.plak.Utils;

import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;

/**
 * Created by jmkl on 4/28/2015.
 */
public class LLParam {
    public static LayoutParams WRAP_CONTENT() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
    public static LayoutParams MATCH_PARENT() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
