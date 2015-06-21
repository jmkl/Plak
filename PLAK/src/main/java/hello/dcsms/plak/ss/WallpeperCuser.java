package hello.dcsms.plak.ss;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hello.dcsms.plak.BaseFragmentActivity;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.StringUtils;
import hello.dcsms.plak.widget.CustomButton;
import hello.dcsms.plak.widget.MToast;

/**
 * Created by jmkl on 5/3/2015.
 */
public class WallpeperCuser extends BaseFragmentActivity {
    public static String BUNDLE_KEY = "wall_chooser_bundle";
    public static String KEY_W = "wall_chooser_width";
    public static String KEY_H = "wall_chooser_height";
    public static String KEY_WALLPIMAGE = "image_file";

    @InjectView(R.id.wall_pick)
    CustomButton wallPick;
    @InjectView(R.id.wall_crop_iv)
    CropImageView wallCropIv;
    int w, h;


    String path = null;

    @Override
    protected void doOnCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getBundleExtra(BUNDLE_KEY);
        if (b == null)
            return;
        w = b.getInt(KEY_W);
        h = b.getInt(KEY_H);
        path = b.getString(KEY_WALLPIMAGE);
        setContentView(R.layout.wallpaper_cropper_view);
        ButterKnife.inject(this);
        wallCropIv.setAspectRatio(w, h);
        wallCropIv.setFixedAspectRatio(true);

//        List<HashMap<String, String>> images = getIMG();
//        imageGallery.setAdapter(new GalleryAdapter(images,getApplicationContext()));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 666) {
                Uri uri = data.getData();
                String url = StringUtils.getPathFromURI(this, uri);
                wallCropIv.setImageBitmap(BitmapFactory.decodeFile(url));

            }
        }
    }


    @OnClick({R.id.wall_pick, R.id.wall_save})
    public void onClick(CustomButton btn) {
        if (btn == wallPick) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 666);
        } else {
            Bitmap bmp = wallCropIv.getCroppedImage();
            new AsyncTask<Bitmap, Void, String>() {
                int width = w;
                int height = h;
                String p = path;

                @Override
                protected void onPostExecute(String v) {
                    super.onPostExecute(v);
                    MToast.show(getApplicationContext(), "Success");
                    if(v!=null)
                    setResult(RESULT_OK);
                    WallpeperCuser.this.finish();
                }

                @Override
                protected String doInBackground(Bitmap... b) {
                    Bitmap newb = Bitmap.createScaledBitmap(b[0], width, height, true);
                    b[0].recycle();
                    File filetosave = new File(p);
                    FileOutputStream outStream = null;
                    try {
                        outStream = new FileOutputStream(filetosave);
                        newb.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
                        outStream.flush();
                        outStream.close();

                        //MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());

                        newb.recycle();
                        System.gc();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }

                    return "ok kok";
                }
            }.execute(bmp);
        }
    }


    private List<HashMap<String, String>> getIMG() {
        File dir = Environment.getExternalStorageDirectory();
        List<HashMap<String, String>> items = new ArrayList<>();
        String[] STAR = {"*"};

        final String orderBy = MediaStore.Images.Media.DEFAULT_SORT_ORDER;
        Cursor imagecursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STAR, null, null, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int count = imagecursor.getCount();
        for (int i = 0; i < count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);

            if (new File(imagecursor.getString(imagecursor.getColumnIndex(MediaStore.Images.Media.DATA))).length() <= 10485760) {
                HashMap<String, String> item = new HashMap<>();
                item.put("path", imagecursor.getString(imagecursor.getColumnIndex(MediaStore.Images.Media.DATA)));
//                imageItem.id = id;
//                imageItem.selection = false; //newly added item will be selected by default
                items.add(item);

            }
        }
        return items;

    }


}
