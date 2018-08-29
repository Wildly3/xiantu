package com.xiuxian.chen.include;

import com.xiuxian.chen.MainActivity;

/**
 * 异常处理
 * Created by Wildly on 2018/8/1.4:31
 */

public final class ErrorHandle {

    public static void doError(Exception e){
        MainActivity.MainPressenter.SaveArchive();
        MyDialog myDialog = MainActivity.event.Alert();
        myDialog.SetTitle("游戏异常")
                .setMessage("游戏已自动保存\n" + e.getMessage())
                .setCancel(false)
                .setSelect("退出游戏", new MyDialog.OnClickListener() {
                    @Override
                    public void onClick(MyDialog dialog) {
                        System.exit(0);
                    }
                })
                .show();
    }
}
