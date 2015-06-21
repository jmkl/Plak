package hello.dcsms.plak.Frgmnt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.adapter.ManualItemListAdapter;
import hello.dcsms.plak.data.ErrCode;
import hello.dcsms.plak.manual.AutoCompleteData;
import hello.dcsms.plak.manual.ManualDataJsonHelper;
import hello.dcsms.plak.manual.ManualItemDB;
import hello.dcsms.plak.manual.ManualItemData;
import hello.dcsms.plak.task.LoadAutoComplete;
import hello.dcsms.plak.task.SaveJsonTask;
import hello.dcsms.plak.task.iface.OnLoadAutoComplete;
import hello.dcsms.plak.widget.AddItemDialog;
import hello.dcsms.plak.widget.CustomButton;
import hello.dcsms.plak.widget.MToast;
import hello.dcsms.plak.widget.PlakConf;
import hello.dcsms.plak.widget.HoriListMenu;

/**
 * Created by jmkl on 5/3/2015.
 */
public class MSChildFragmentList extends MSChildFragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    ManualItemDB dbhelper;
    ListView lv;
    ManualItemListAdapter adapter;
    List<ManualItemData> data;
    private CustomButton save;
    private CustomButton apply_selected;
    private String current_setting;
    private HoriListMenu mHoriListMenu, listmenuatas;
    private String PlakFilesRoot = Environment.getDataDirectory()
            + "/data/hello.dcsms.plak/config/";

    float width;
    private ManualSettingFragment listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (dbhelper == null) {
            dbhelper = new ManualItemDB(getActivity());
            dbhelper.Open();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (dbhelper != null)
            dbhelper.Close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.manual_setting_layout, null);
        width = v.getWidth();
        setHasOptionsMenu(true);


        save = (CustomButton) v.findViewById(R.id.man_conf_save);
        apply_selected = (CustomButton) v.findViewById(R.id.man_con_apply_selected);
        listmenuatas = (HoriListMenu) v.findViewById(R.id.man_conf_listmenu);
        listmenuatas
                .setShowCount(2)
                .setUpMenu(getActivity(), new String[]{"Save Selected", "Apply Selected", "Delete Selected", "Select All"});
        listmenuatas.setOnSortirMenuListener(new HoriListMenu.onHoriListItemClickListener() {
            @Override
            public void onClick(View c, int position) {
                switch (position) {
                    case 0:
                        SaveJsonTask task = new SaveJsonTask(data, null);
                        task.setListener(new SaveJsonTask.onSaveTaskListener() {
                            @Override
                            public void onComplete(String res) {
                                Toast.makeText(getActivity(), "Saved in " + res, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                        task.execute();
                        break;

                    case 1:
                        SaveJsonTask task2 = new SaveJsonTask(data, PlakFilesRoot);
                        task2.setListener(new SaveJsonTask.onSaveTaskListener() {
                            @Override
                            public void onComplete(String res) {
                                Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT)
                                        .show();
                                if (listener != null)
                                    listener.onUpdate();
                            }
                        });
                        task2.execute(getActivity());
                        break;
                    case 2://TODO DELETE SELECTED
                        break;
                    case 3://TODO SELECT ALL VICEVERSA

                        break;

                }

            }
        });

        save.setOnClickListener(this);
        apply_selected.setOnClickListener(this);
        dbhelper = new ManualItemDB(getActivity());
        dbhelper.Open();

        lv = (ListView) v.findViewById(R.id.manual_list);
        lv.setBackgroundColor(Color.WHITE);
        registerForContextMenu(lv);
        data = dbhelper.getAllData();
        adapter = new ManualItemListAdapter(getActivity(), data);
        lv.setDividerHeight(0);
        lv.setAdapter(adapter);
        DoLoadAutoCompleteShit();

        mHoriListMenu = (HoriListMenu) v.findViewById(R.id.sortirmenu);
        mHoriListMenu.setUpMenu(getActivity(), new String[]{"Name", "Package", "Checked", "Type"});
        mHoriListMenu.setOnSortirMenuListener(new HoriListMenu.onHoriListItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                adapter.sortBy(position);
            }
        });


        return v;
    }


    List<AutoCompleteData> autocompletedata = null;

    void DoLoadAutoCompleteShit() {
        LoadAutoComplete task = new LoadAutoComplete(getActivity());
        task.setOnLoadListener(new OnLoadAutoComplete() {

            @Override
            public void onComplete(List<AutoCompleteData> completedata) {
                autocompletedata = completedata;


            }
        });

        task.execute();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent datax) {
        if (requestCode == 666) {

            if (resultCode == Activity.RESULT_OK) {
                String path = datax.getStringExtra(PlakConf.RESULT);

                File f = new File(path);
                if (f.exists()) {
                    List<ManualItemData> lmid = ManualDataJsonHelper
                            .ReadJson(path);
                    if (lmid.size() > 0) {
                        for (ManualItemData mid : lmid) {
                            dbhelper = new ManualItemDB(getActivity());
                            dbhelper.Open();
                            if (!dbhelper.isItemExist(mid.getNamaField())) {
                                ManualItemData res = dbhelper.createItemData(mid);
                                if (data != null) {
                                    data.add(res);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    MToast.show(getActivity(), "Config file not support ERROR : " + ErrCode.CONFIGFILENOTSUPPORT);
                                }
                            }

                        }
                    }
                }
            }
        }
    }


    SearchView searchView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_manual, menu);
        MenuItem add_bool = menu.findItem(R.id.manual_add_boolean);
        searchView = (SearchView) menu.findItem(R.id.action_filter).getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        final ManualItemData md = (ManualItemData) lv.getItemAtPosition(info.position);
        AddItemDialog.ITEMTYPE type = ManualItemData.getTypeFromDATA(md);

        switch (item.getItemId()) {
            case R.id.cm_delete:
                dbhelper.DeleteData(md.getId());
                data.remove(md);
                adapter.notifyDataSetChanged();
                break;

            case R.id.cm_edit:
                AddItemDialog dialog = new AddItemDialog(getActivity());
                dialog.setAutoCompleteData(autocompletedata);
                dialog.setTitle("Edit " + ManualItemData.getTypeFromDATA(md))
                        .setType(type).setItem(md.getNamaField())
                        .setValue(md.getNilai()).setDesc(md.getCatatan())
                        .setPackage(md.getNamaPaket());
                dialog.setOnItemAddListener(new AddItemDialog.OnItemAddListener() {

                    @Override
                    public void getValue(AddItemDialog.ITEMTYPE type, String item,
                                         String itemvalue, String desc, String pkgname) {
                        ManualItemData mdnew = new ManualItemData();
                        mdnew.setNamaField(item);
                        mdnew.setItemType(ManualItemData.getTypeFromDialog(type));
                        mdnew.setItemValue(itemvalue);
                        mdnew.setCek(0);
                        mdnew.setCatatan(desc);
                        mdnew.setNamaPaket(pkgname);
                        mdnew.setId(md.getId());
                        dbhelper.updateData(mdnew);
                        data.set(info.position, mdnew);
                        adapter.notifyDataSetChanged();

                    }
                });
                dialog.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.contextmenu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.imporfile) {
            Intent ichoose = new Intent(getActivity(), PlakConf.class);
            ichoose.putExtra(PlakConf.FILE_TYPE, PlakConf.TYPE_CONF);
            startActivityForResult(ichoose, 666);
        } else if (id == R.id.action_filter) {

        } else {
            AddItemDialog dialog = new AddItemDialog(getActivity());
            dialog.setAutoCompleteData(autocompletedata);
            switch (id) {
                case R.id.manual_add_dimen:
                    dialog.setTitle(getResources()
                            .getString(R.string.man_add_dimen));
                    dialog.setType(AddItemDialog.ITEMTYPE.DIMEN).setValueHint(
                            "input number between 0-500");
                    break;
                case R.id.manual_add_integer:
                    dialog.setTitle(getResources().getString(
                            R.string.man_add_integer));
                    dialog.setType(AddItemDialog.ITEMTYPE.INTEGER);
                    break;
                case R.id.manual_add_string:
                    dialog.setTitle(getResources().getString(
                            R.string.man_add_string));
                    dialog.setType(AddItemDialog.ITEMTYPE.STRING);

                    break;
                case R.id.manual_add_boolean:
                    dialog.setTitle(getResources().getString(R.string.man_add_bool));
                    dialog.setType(AddItemDialog.ITEMTYPE.BOOLEAN).setValueHint(
                            "if true type 1 if false type 0");

                    break;
                case R.id.manual_add_color:
                    dialog.setTitle(getResources()
                            .getString(R.string.man_add_color));
                    dialog.setType(AddItemDialog.ITEMTYPE.COLOR).setValueHint("#FFFFFFFF");

                    break;
            }
            dialog.setOnItemAddListener(new AddItemDialog.OnItemAddListener() {

                @Override
                public void getValue(AddItemDialog.ITEMTYPE type, String item, String value,
                                     String desc, String pkgname) {
                    String t = null;
                    String v = null;
                    switch (type) {
                        case DIMEN:
                            t = "dimen";
                            break;

                        case INTEGER:
                            t = "integer";
                            break;
                        case STRING:
                            t = "string";
                            break;
                        case BOOLEAN:
                            t = "bool";
                            break;
                        case COLOR:
                            t = "color";
                            break;
                    }
                    ManualItemData md = new ManualItemData();
                    md.setNamaField(item);
                    md.setItemType(t);
                    md.setItemValue(value);
                    md.setCek(0);
                    md.setCatatan(desc);
                    md.setNamaPaket(pkgname);
                    ManualItemData res = dbhelper.createItemData(md);
                    data.add(res);
                    adapter.notifyDataSetChanged();

                }
            });
            dialog.show();
        }
        return true;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (dbhelper != null)
            dbhelper.Close();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dbhelper != null)
            dbhelper.Close();
        dbhelper = new ManualItemDB(getActivity());
        dbhelper.Open();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbhelper != null)
            dbhelper.Close();
    }

    @Override
    public void onClick(View c) {
        switch (c.getId()) {
            case R.id.man_con_apply_selected:
                SaveJsonTask task2 = new SaveJsonTask(data, PlakFilesRoot);
                task2.setListener(new SaveJsonTask.onSaveTaskListener() {
                    @Override
                    public void onComplete(String res) {
                        Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT)
                                .show();
                        if (listener != null)
                            listener.onUpdate();
                    }
                });
                task2.execute(getActivity());
                break;
            case R.id.man_conf_save:
                SaveJsonTask task = new SaveJsonTask(data, null);
                task.setListener(new SaveJsonTask.onSaveTaskListener() {
                    @Override
                    public void onComplete(String res) {
                        Toast.makeText(getActivity(), "Saved in " + res, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
                task.execute();
                break;

        }

    }




    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        return false;
    }

    public MSChildFragment setListener(ManualSettingFragment listener) {
        this.listener = listener;
        return this;
    }


}
