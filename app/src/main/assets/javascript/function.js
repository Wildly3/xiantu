/*添加掉落物
@name 物品名称
@type 物品类型 item型
@odds 掉落几率
@low 掉落最低数量
@hight 掉落最大数量
*/
function addenegoods(name, type, odds, low, hight)
{
var chance=new Chance(name, type);
chance.chance=odds;
chance.quanlity_low=low;
chance.quanlity_hight=hight;
main_ene.addGod(chance);
}

//穿戴装备
function wearequip(equip)
{
main_ene.WearEquip(equip);
}

//设置技能
function wearskill(skill)
{
main_ene.WearSkill(skill);
}

//获取当前攻击力
function getatk()
{
return main_atk;
}

//获取当前发起对象
function getholder()
{
return main_holder;
}

//获取敌人
function getenemy()
{
return main_enemy;
}

//获取目标对象
function gettarget()
{
return main_enemy;
}

//获取技能对象
function getskill()
{
return main_skill;
}

//获取空间
function getspace()
{
return main_space;
}

//获取管理器
function getplayall()
{
return main_playerpall;
}

//获取玩家等级
function getplaylevel()
{
return game.getPlayLevel();
}

//获取玩家姓名
function getplayname()
{
return game.getPlayName();
}

//提示消息
function message(msg, flag)
{
game.Message(msg, flag);
}

//显示对话框
function say(title, str, type)
{
game.Alert(title, str, type);
}

//对话框
function Dialog()
{
  this.dialog = game.Alert();
  
  this.setTitle = function(title)
  {
    this.dialog.SetTitle(title);
  }
  
  this.setMessage = function(msg)
  {
    this.dialog.setMessage(msg);
  }
  
  this.setSelect = function(name, click)
  {
    var obj=
    {
    onClick:function(dia)
    {
      click();
    }
    };
    this.dialog.setSelect(name, click == null ? null : (new com.xiuxian.chen.include.MyDialog.OnClickListener(obj)) );
  }
  
  this.setCancel = function(name, click)
  {
    var obj=
    {
    onClick:function(dia)
    {
      click();
    }
    };
    this.dialog.setCancel(name, click == null ? null : (new com.xiuxian.chen.include.MyDialog.OnClickListener(obj)) );
  }
  
  this.show = function()
  {
    this.dialog.Show();
  }
  
}

function _say(title, str, end)
{
game.Alert(title, str, end);
}

//挑战信息对话框
function fightsay(name, msg, posend)
{
game.EnemyMsg(name, msg, posend);
}

//获得对话
function says()
{
return game.ListAlert();
}

//停止当前人物移动
function stopmove()
{
game.StopMove();
}

//移动到指定地图坐标
function move(id, x, y)
{
game.MoveMap(id, x, y);
}

//移动到指定坐标
function moverole(x, y)
{
game.MoveRoleMap(x, y);
}

//获取x坐标
function getx()
{
return geme.getX();
}

//获取y坐标
function gety()
{
return game.getY();
}

//添加装备到空间
function addequip(name, quality)
{
return game.addEquip(name, quality);
}

//添加消耗品到空间
function addconsunables(name, num)
{
return game.addConsunables(name, num);
}

//添加材料到空间
function addmaterial(name, num)
{
return game.addMaterial(name, num);
}

//扩大空间
function expandspace(expand)
{
game.ExpandSpace(expand);
}

//增加玩家金币
function addmoney(m)
{
game.addMoney(m);
}

//增加玩家经验
function addexp(e)
{
return game.addExp(e);
}

//添加数据
function put(key, value)
{
game.put(key, value);
}

//获取数据
function get(key)
{
return game.get(key, 0);
}

//战斗
function fight(names)
{
game.FightName(names);
}

//测试战斗
function testfight(names)
{
game.TestFight(names);
}

//获取空间物品数量
function getgoodsnumber(name)
{
return game.getGoodsNumber(name);
}

//取出空间物品，没有该物品或者数量不足返回false
function takeoutgoods(name, num)
{
return game.TakeOutGoods(name, num);
}

//判断一个任务是否完成
function istask(name)
{
return game.isCompleteTask(name);
}

//判断一个任务是否接取
function isaccepttask(name)
{
return game.isAcceptTask(name);
}

//添加一个任务
function addtask(name)
{
return game.addTask(name);
}