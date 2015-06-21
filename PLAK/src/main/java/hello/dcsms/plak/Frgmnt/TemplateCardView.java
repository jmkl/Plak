package hello.dcsms.plak.Frgmnt;

public class TemplateCardView{}
/**
 * Created by jmkl on 5/1/2015.
 */
//public class TemplateCardView extends CardView implements View.OnClickListener, IonTemplateApply {
//
//    LocalImageLoader imgLoader;
//    HashMap<String, Object> data;
//    ImageView img;
//    RelativeLayout mainlayout, rootlayout;
//    TextView tv_title;
//    TextView tv_info;
//    TextView tv_apply, tv_wallp;
//    ImageView tv_cek;
//    Display display;
//    Point point;
//    PrefUtils prefutils;
//    SharedPreferences pref;
//    // Helper plakHelper= new Helper();
//    IonTemplateApply listener;
//    private Context mContext;
//    boolean visible = false;
//    SSFrameData ssframe = null;
//    String path;
//
//
//    public TemplateCardView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.mContext = context;
//
//    }
//
//    public TemplateCardView(Context context) {
//        super(context);
//        this.mContext = context;
//
//    }
//    public void setListener(IonTemplateApply listener) {
//        this.listener = listener;
//    }
//
//    public void init(HashMap<String, Object> data) {
//        this.data =data;
//        View v = onCreateView();
//        if(v!=null);
//        addView(v);
//
//
//    }
//
//
//    public View onCreateView() {
//        prefutils = new PrefUtils(getActivity());
//        pref = prefutils.getPref();
//        ssframe = (SSFrameData) data.get("data");
//        path = (String) data.get("path");
//        new Debugger().log("PATH",path);
//        if(ssframe==null)return null;
//        if(path==null)return null;
//        display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        point = new Point();
//        display.getSize(point);
//        View convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mcardview, null);
//
//        imgLoader = new LocalImageLoader(getActivity());
//        img = (ImageView) convertView.findViewById(R.id.templ_img);
//        rootlayout = (RelativeLayout) convertView.findViewById(R.id.templ_root);
//        mainlayout = (RelativeLayout) convertView.findViewById(R.id.templ_layout);
//        tv_title = (TextView) convertView.findViewById(R.id.templ_title);
//        tv_info = (TextView) convertView.findViewById(R.id.templ_info);
//        tv_apply = (TextView) convertView.findViewById(R.id.templ_apply);
//        tv_wallp = (TextView) convertView.findViewById(R.id.templ_wallp);
//        tv_cek = (ImageView) convertView.findViewById(R.id.templ_applycek);
//        setListener(this);
//        setViewInfo(data, mainlayout, img, tv_title, tv_info, tv_apply, tv_cek);
//        return convertView;
//    }
//
//
//    private void setViewInfo(HashMap<String, Object> data, final RelativeLayout mainlayout, ImageView img, TextView tv_title, TextView tv_info, TextView tv_apply, ImageView tv_cek) {
//
//
//        //plakHelper.storeBitmap(BitmapFactory.decodeFile(path+"/preview.jpg"));
//        //img.setImageBitmap(plakHelper.getBitmapAndFree());
//        imgLoader.DisplayImage(path, img);
//
//        tv_title.setText(ssframe.getSsframe());
//        String tambahan = "";
//
//        if (ssframe.getSs_x() != point.x || ssframe.getSs_y() != point.y)
//            tambahan = "your screensize is not equal with this template";
//        tv_info.setText(String.format("screenshot size : %d x %d\n%s", ssframe.getSs_x(), ssframe.getSs_y(), tambahan));
//        tv_apply.setOnClickListener(this);
//        if (listener != null)
//            listener.onApply();
//
//        tv_wallp.setOnClickListener(this);
//        img.setOnClickListener(this);
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.templ_apply:
//                PrefUtils pref = new PrefUtils(getActivity());
//                pref.edit(C.MOCKUP_PATH, data.get("path"));
//                MToast.show(getActivity(), "Success");
//                if (listener != null)
//                    listener.onApply();
//                break;
//            case R.id.templ_wallp:
//                if (ssframe == null) return;
//                Intent it = new Intent(getActivity(), WallpeperCuser.class);
//                Bundle b = new Bundle();
//                b.putInt(WallpeperCuser.KEY_H, ssframe.getSsframedata().getBg_height());
//                b.putInt(WallpeperCuser.KEY_W, ssframe.getSsframedata().getBg_width());
//                b.putString(WallpeperCuser.KEY_WALLPIMAGE, path + "/" + ssframe.getSsframedata().getBackground());
//                it.putExtra(WallpeperCuser.BUNDLE_KEY, b);
//                getActivity().startActivity(it);
//                break;
//        }
//    }
//
//
//    @Override
//    public void onApply() {
//
//        if (pref == null || prefutils == null)
//            return;
//        String curpath = pref.getString(C.MOCKUP_PATH, null);
//        if (curpath == null)
//            return;
//        tv_cek.setVisibility(curpath.equals(path) ? View.VISIBLE : View.GONE);
//        tv_apply.setVisibility(curpath.equals(path) ? View.GONE : View.VISIBLE);
//
//    }
//
//    public Context getActivity() {
//        return mContext;
//    }
//}
