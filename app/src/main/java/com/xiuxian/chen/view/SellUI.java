package com.xiuxian.chen.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiuxian.chen.MainActivity;
import com.xiuxian.chen.R;
import com.xiuxian.chen.adapter.MyAdapter;
import com.xiuxian.chen.data.SelectGoods;
import com.xiuxian.chen.include.MyDialog;
import com.xiuxian.chen.other.ConsunablesGoods;
import com.xiuxian.chen.other.EquipGoods;
import com.xiuxian.chen.other.MaterialGoods;
import com.xiuxian.chen.other.Shoping;
import com.xiuxian.chen.set.Consunables;
import com.xiuxian.chen.set.Item;
import com.xiuxian.chen.set.Material;
import com.xiuxian.chen.set.Weapon;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Wildly on 2018/7/28.7:10
 */

public class SellUI extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener
{
    Button btn_ref, btn_sell;

    ListView mListView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.sell);

        btn_ref = (Button) findViewById(R.id.SellUI_ref);

        btn_sell = (Button) findViewById(R.id.SellUI_sell);

        mListView = (ListView) findViewById(R.id.SellUI_list);

        mListView.setOnItemClickListener(this);

        btn_sell.setOnClickListener(this);

        btn_ref.setOnClickListener(this);

        ShowSellGoods();
    }

    //显示以上架的物品
    void ShowSellGoods(){
        adapter = new MyAdapter(getContext());

        mListView.setAdapter(adapter);

        BmobQuery<EquipGoods> query1 = new BmobQuery<>();

        Shoping shop = Shoping.getInstance();

        query1.addWhereEqualTo("id", shop.getId());

        query1.findObjects(new FindListener<EquipGoods>() {
            @Override
            public void done(List<EquipGoods> list, BmobException e) {
                if(e == null){
                    for(EquipGoods eg : list)
                        adapter.add(eg.name);
                }
            }
        });

        BmobQuery<ConsunablesGoods> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("id", shop.getId());
        query2.findObjects(new FindListener<ConsunablesGoods>() {
            @Override
            public void done(List<ConsunablesGoods> list, BmobException e) {
                if(e == null){
                    for(ConsunablesGoods eg : list)
                        adapter.add(eg.name + " ["+eg.number+"]");
                }
            }
        });

        BmobQuery<MaterialGoods> query3 = new BmobQuery<>();
        query3.addWhereEqualTo("id", shop.getId());
        query3.findObjects(new FindListener<MaterialGoods>() {
            @Override
            public void done(List<MaterialGoods> list, BmobException e) {
                if(e == null){
                    for(MaterialGoods eg : list)
                        adapter.add(eg.name + " ["+eg.number+"]");
                }
            }
        });
    }

    MyAdapter adapter;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SellUI_ref:

                break;
            case R.id.SellUI_sell:
                Bundle bundle = new Bundle();
                bundle.putBoolean("sell", true);
                startFragment(new SpaceUI(null), bundle);
                break;
        }

    }

    @Override
    public void Callback(Bundle bundle) {
        super.Callback(bundle);
        String json = bundle.getString("select", null);
        if(json == null) return;
        Gson gson = new GsonBuilder().create();
        final SelectGoods sg = gson.fromJson(json, SelectGoods.class);
        final Shoping shop = Shoping.getInstance();
        final MyDialog myDialog = new MyDialog(getContext())
                .SetTitle("提示")
                .setMessage("上架中...")
                .setCancel(false)
                .Show();
        switch (sg.type){
            case Item.KIND_EQUIP:
                EquipGoods equipGoods = new EquipGoods();
                Weapon wp = BaseActivity.space.weapons.get(sg.index);
                equipGoods.name = wp.name;
                equipGoods.quality = wp.quality;
                equipGoods.price = sg.quality;
                equipGoods.id = shop.getId();
                equipGoods.publisher = BaseActivity.base.name;
                equipGoods.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        myDialog.dismiss();
                        if(e == null) {
                            BaseActivity.space.takeout_weapon(sg.index);
                            MainActivity.MainPressenter.SaveArchive();
                            toast("上架成功！");
                        } else toast("上架失败！");
                    }
                });
                break;
            case Item.KIND_CONSUNABLES:
                ConsunablesGoods cong = new ConsunablesGoods();
                Consunables con = BaseActivity.space.consunables.get(sg.index);
                cong.name = con.name;
                cong.price = sg.quality;
                cong.id = shop.getId();
                cong.number = sg.number;
                cong.publisher = BaseActivity.base.name;
                cong.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        myDialog.dismiss();
                        if(e == null) {
                            BaseActivity.space.takeout_consunables(sg.index, sg.number);
                            MainActivity.MainPressenter.SaveArchive();
                            toast("上架成功！");
                        } else toast("上架失败！");
                    }
                });
                break;
            case Item.KIND_MATERIAL:
                MaterialGoods matg = new MaterialGoods();
                Material mat = BaseActivity.space.materials.get(sg.index);
                matg.name = mat.name;
                matg.price = sg.quality;
                matg.id = shop.getId();
                matg.number = sg.number;
                matg.publisher = BaseActivity.base.name;
                matg.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        myDialog.dismiss();
                        if(e == null) {
                            BaseActivity.space.takeout_material(sg.index, sg.number);
                            MainActivity.MainPressenter.SaveArchive();
                            toast("上架成功！");
                        } else toast("上架失败！");
                    }
                });
                    break;

            default: break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
