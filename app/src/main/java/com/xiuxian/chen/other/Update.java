package com.xiuxian.chen.other;

import cn.bmob.v3.BmobObject;

/**
 * Created by Wildly on 2018/7/30.7:21
 */

public final class Update extends BmobObject {

    //类型
    public int type;

    //版本
    public String version;

    //更新信息
    public String msg;

    //下载地址
    public String url;
}
