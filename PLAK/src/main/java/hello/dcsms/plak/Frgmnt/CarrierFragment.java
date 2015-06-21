package hello.dcsms.plak.Frgmnt;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hello.dcsms.plak.C;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.StringUtils;
import hello.dcsms.plak.widget.CarrierImageButton;
import hello.dcsms.plak.widget.HoriListMenu;
import hello.dcsms.plak.widget.MToast;

/**
 * Created by jmkl on 5/15/2015.
 */
public class CarrierFragment extends Fragment {
    @InjectView(R.id.carrier_listmenu)
    HoriListMenu mlist;
    @InjectView(R.id.carrier_gridmenu)
    CarrierImageButton gridcard;
    String[] allmenus = new String[]{"SIM1 Dark", "SIM2 Dark", "SIM1 Light", "SIM2 Light"};


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.carrier_config, null);
        ButterKnife.inject(this, view);
       gridcard.setCardClickListener(new CarrierImageButton.onCardClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                gridcard.setAllChildText(allmenus);
                gridcard.inisiateImageIfPossible(allmenus);
            }
        }, 2000);
        mlist.setShowCount(4).setUpMenu(getActivity(), allmenus);
        mlist.setOnSortirMenuListener(new HoriListMenu.onHoriListItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                getActivity().startActivityForResult(intent, 100 + position);
            }
        });
        return view;
    }


    int MAXSIZE=500;


    private void setFilePermision(File f) {
        if(!f.exists())
            return;
        f.setReadable(true, false);
        f.setWritable(true, false);
        f.setExecutable(true, false);
    }

    public void sendIntentData(int requestCode,Intent data) {
        Uri uri = data.getData();
        String url = StringUtils.getPathFromURI(getActivity(), uri);
        new Debugger().log(url);
        Bitmap bmp = BitmapFactory.decodeFile(url);
        if (bmp == null) return;
        if(bmp.getWidth() > MAXSIZE || bmp.getHeight()> MAXSIZE){
            MToast.show(getActivity(), "File to large :{");
            return;
        }
        File f = new File(C.PLAK_CARRIERIMAGEFOLDER);
        if (!f.exists()) {
            f.mkdirs();
            setFilePermision(f);
        }

        File carrierimage = null;
        switch (requestCode) {
            case 100://SIM1
                carrierimage = new File(C.PLAK_CARRIERIMAGE_SIM1_DARK);
                break;
            case 101://SIM2
                carrierimage = new File(C.PLAK_CARRIERIMAGE_SIM2_DARK);
                break;
            case 102://SIM1 Light
                carrierimage = new File(C.PLAK_CARRIERIMAGE_SIM1);
                break;
            case 103://SIM2 Light
                carrierimage = new File(C.PLAK_CARRIERIMAGE_SIM2);
                break;
        }
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(carrierimage);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            bmp.recycle();
            synchronized (carrierimage){
                setFilePermision(carrierimage);
            }
            getActivity().sendBroadcast(new Intent().setAction(C.MODSTATUSBARUPDATECARRIERONLY));
            gridcard.setImageIconIfPossible(requestCode - 100, carrierimage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
