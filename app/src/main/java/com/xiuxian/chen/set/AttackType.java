package com.xiuxian.chen.set;

/**
 * 伤害类型对象
 * Created by Wildly on 2018/8/2.7:56
 */

public final class AttackType {
    public String flag;
    public int hurt_type;

    public static AttackType getInstance(String flag, int hurt_type){
        AttackType attackType = new AttackType();
        attackType.flag = flag;
        attackType.hurt_type = hurt_type;
        return attackType;
    }
}
