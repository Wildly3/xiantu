package com.xiuxian.chen.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiuxian.chen.MainActivity;
import com.xiuxian.chen.R;

/**
 * Created by Wildly on 2018/8/27.21:17
 */

public class RoleUI extends Fragment implements View.OnClickListener {

    final int resid[]={
            R.id.roleButton1_attr,
            R.id.roleButton1_space,
            R.id.roleButton1_kill,
            R.id.roleButton1_task
    };

    Button[] btns, attrbtns;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.role);

        btns = new Button[resid.length];
        View.OnClickListener onc = this;
        for(int i=0;i<resid.length;i++){
            btns[i]=(Button)findViewById(resid[i]);
            btns[i].setOnClickListener(onc);
        }

        RoleActivity.activity.showTitle();
    }

    @Override
    public void onClick(View v) {
        boolean isStart = false;
        switch (v.getId()) {
            case R.id.roleButton1_attr:
                //Goin(R.layout.role_attr, new InforMationUI());
                isStart = startFragment(new InforMationUI());
                break;
            case R.id.roleButton1_space:
                //Goin(R.layout.role_space,new SpaceUI());
                isStart = startFragment(new SpaceUI(RoleActivity.activity));
                break;
            case R.id.roleButton1_kill:
                //Goin(R.layout.skill, new SkillUI());
                isStart = startFragment(new SkillUI());
                break;
            case R.id.roleButton1_task:
                //Goin(R.layout.role_task, new TaskUI());
                isStart = startFragment(new TaskUI());
                //MainActivity.tabHost.getTabWidget().setEnabled(true);
                break;
        }

        if (isStart)
            MainActivity.hideBottom();
    }
}
