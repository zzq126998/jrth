<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 2019/7/20 0020
 * Time: 14:43
 */
ini_set("max_execution_time", "0");//代码运行时间不限制  防止备份失败
ini_set('memory_limit', '128M');//设置内存 根据需求可以修改
$conn=mysqli_connect("182.61.51.112","root","root","rockxinhu") or die("数据库连接错误：".mysql_error());//连接数据库
mysqli_query($conn,'set names utf8');


$select_sql = "select a.*,b.superpath,b.name from rockxinhu.xinhu_tjnews AS a LEFT JOIN rockxinhu.xinhu_admin AS b ON a.mid = b.id";
$list = $conn->query($select_sql);
$oderlist = $list->fetch_all(MYSQLI_ASSOC);

if ($oderlist) {
    foreach ($oderlist as $k=>$v){
        /*一级有效期
          atermvalidity
        */
        $strtotime_atermvalidity = strtotime($v['atermvalidity']);
        if (time()<bcsub($strtotime_atermvalidity,7776000)) {//
            /*
             * superpath上级主管数组id
             */
            $a_mid = $v['mid'];
            $aar = explode(',',$v['superpath']);
            foreach ($aar as $kk=>$vv){
                $id = trim($vv,'[]');
                $message = $v['name']."的一级体检快到期了";
                $time = date("Y-m-d H:i:s",time());
                $s = "select * from xinhu_todo WHERE uid = $id AND mid = $a_mid AND title = '一级体检快到期提醒' ORDER BY id DESC limit 1";
                $find = $conn->query($s);
                $findres = $find->fetch_assoc();
                if (!$findres) {
                    $insert_sql = "insert into xinhu_todo (uid,title,mess,optdt,mid,tododt) VALUES ($id,'一级体检快到期提醒','{$message}','{$time}',$a_mid,'{$time}')";
                    $conn->query($insert_sql);
                }elseif (bcadd(strtotime($findres['tododt']),604800) < time()){
                    $insert_sql = "insert into xinhu_todo (uid,title,mess,optdt,mid,tododt) VALUES ($id,'一级体检快到期提醒','{$message}','{$time}',$a_mid,'{$time}')";
                    $conn->query($insert_sql);
                }

            }
        }
        /*二级有效期
          btermvalidity
        */
        $strtotime_btermvalidity = strtotime($v['btermvalidity']);
        if (time()<bcsub($strtotime_btermvalidity,7776000)) {
            $aar = explode(',',$v['superpath']);
            foreach ($aar as $kk=>$vv){
                $id = trim($vv,'[]');
                $message = $v['name']."的二级体检快到期了";
                $time = date("Y-m-d H:i;s",time());
                $s = "select * from xinhu_todo WHERE uid = $id AND mid = $a_mid AND title = '二级体检快到期提醒' ORDER BY id DESC limit 1";
                $find = $conn->query($s);
                $findres = $find->fetch_assoc();
                if (!$findres) {
                    $insert_sql = "insert into xinhu_todo (uid,title,mess,optdt,mid,tododt) VALUES ($id,'二级体检快到期提醒','{$message}','{$time}',$a_mid,'{$time}')";
                    $conn->query($insert_sql);
                }elseif(bcadd(strtotime($findres['tododt']),604800) < time()){
                    $insert_sql = "insert into xinhu_todo (uid,title,mess,optdt,mid,tododt) VALUES ($id,'二级体检快到期提醒','{$message}','{$time}',$a_mid,'{$time}')";
                    $conn->query($insert_sql);
                }

            }
        }
    }
    mysqli_close($conn);

}