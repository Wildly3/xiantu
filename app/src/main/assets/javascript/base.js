var Weapon = com.xiuxian.chen.set.Weapon;

var Buffer = com.xiuxian.chen.set.Buffer;

var Player = com.xiuxian.chen.set.Player;

var Enemy = com.xiuxian.chen.set.Enemy;

var BeBuffer = com.xiuxian.chen.set.Weapon.BeBuffer;

var MyMap = com.xiuxian.chen.map.MyMap;

var MapItem = com.xiuxian.chen.map.MyMap.MapItem;

var Goods = com.xiuxian.chen.map.MyMap.Goods;

var Color = android.graphics.Color;

var Item = com.xiuxian.chen.set.Item;

var Consunables = com.xiuxian.chen.set.Consunables;

var ConsunablesUse = com.xiuxian.chen.set.ConsunablesUse;

var Material = com.xiuxian.chen.set.Material;

var Chance = Enemy.Chance;

var Skill = com.xiuxian.chen.set.Skill;

var SkillAttackAction = com.xiuxian.chen.set.SkillAttackAction;

var SkillPassiveAction = com.xiuxian.chen.set.SkillPassiveAction;

var AtkType = com.xiuxian.chen.set.AttackType;

//扫描装备并返回
function ScanWeapon(name){
	return com.xiuxian.chen.view.BaseActivity.ScanWeapon(name);
}

//扫描敌人并返回
function ScanEnemy(name)
{
	return com.xiuxian.chen.view.BaseActivity.ScanEnemy(name)
}

//扫描技能并返回
function ScanSkill(name){
	return com.xiuxian.chen.view.BaseActivity.ScanSkill(name);
}

//随机数
function random(max)
{
return Math.ceil(Math.random()*max);
}

//转为整数
function toint(x)
{
return Math.ceil(x);
}

//随机数判定
function odds(rand)
{
if(random(1000)<(rand*1000)) return true;
return false;
}

//获取攻击类型
function attacktype(type)
{
if(type.equals("commonly"))
return 1;
else
if(type.equals("consunables"))
return 2;
else
if(type.equals("skill"))
return 3;
else return 0;
}

//设置攻击类型
function setattacktype(pall, type)
{
pall.setAttackType(type);
}

//设置中毒效果
function setbuff_atk(name, holder, target, atk, round)
{
var buf=new Buffer();
buf.name=name;
buf.setBaseBuffer(holder);
buf.setTargetBuffer(target);
buf.atk = atk;
buf.rounds=round;
buf.type=Buffer.POISONING;
if(target!=null)
target.addBuffer(buf);
}

//中毒效果
function Poisoning(name, holder, target, atk, round)
{
var buf=new Buffer();
buf.name=name;
buf.setBaseBuffer(holder);
buf.setTargetBuffer(target);
buf.atk = atk;
buf.rounds=round;
buf.type=Buffer.POISONING;
if(target!=null)
target.addBuffer(buf);
}

//添加效果
function addBuff(buf, holder, target)
{
buf.setBaseBuffer(holder);
buf.setTargetBuffer(target);
if(target!=null)
target.addBuffer(buf);
}

//设置防御效果
function setbuff_def(name, holder, target, def, round)
{
var buf=new Buffer();
buf.name=name;
buf.setBaseBuffer(holder);
buf.setTargetBuffer(target);
buf.setRounds(round);
buf.def = def;
buf.type=Buffer.PROMOTE;
if(target!=null)
target.addBuffer(buf);
}

//提示位置
var center = 17;  //中心

//物品类型

var kind_null = -1;  //无

var kind_equip = 1;  //装备

var kind_consunables = 2;  //消耗品

var kind_material = 3;  //材料

//品质
	var quality_infelior = 0;   //下品
	
	var quality_medium = 1;  //中品
	
	var quality_first_class = 2;  //商品
	
	var quality_nonsuch = 3;  //极品
	
	//攻击类型
	
	var attack_null = 0;  //空
	
	var attack_commonly = 1;  //普通攻击
	
	var attack_consunables = 2;  //消耗品攻击
	
	var attack_skill = 3;  //技能攻击
	
	var attack_buffer = 4;  //buff伤害
	
	//伤害类型
	
	var hurt_commonly="commonly";
	
	var hurt_consunables="consunables";
	
	var hurt_skill="skill";
	
	var hurt_buffer="buffer";