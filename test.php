<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 2019/7/20 0020
 * Time: 14:43
 */

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
            $aar = explode(',',$v['superpath']);
            foreach ($aar as $kk=>$vv){
                $id = trim($vv,'[]');
                $message = $v['name']."的一级体检快到期了";
                $time = date("Y-m-d H:i:s",time());
                $insert_sql = "insert into xinhu_todo (uid,title,mess,optdt,mid,tododt) VALUES ($id,'一级体检快到期提醒','{$message}','{$time}',$id,'{$time}')";
                $conn->query($insert_sql);
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
                $insert_sql = "insert into xinhu_todo (uid,title,mess,optdt,mid,tododt) VALUES ($id,'二级体检快到期提醒','{$message}','{$time}',$id,'{$time}')";
                $conn->query($insert_sql);
            }
        }
    }
    mysqli_close($conn);

}