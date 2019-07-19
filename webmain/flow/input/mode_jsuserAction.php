<?php
/**
*	此文件是流程模块【jsuser.技术类人员】对应控制器接口文件。
*/ 
class mode_jsuserClassAction extends inputAction{
	
	/**
	*	重写函数：保存前处理，主要用于判断是否可以保存
	*	$table String 对应表名
	*	$arr Array 表单参数
	*	$id Int 对应表上记录Id 0添加时，大于0修改时
	*	$addbo Boolean 是否添加时
	*	return array('msg'=>'错误提示内容','rows'=> array()) 可返回空字符串，或者数组 rows 是可同时保存到数据库上数组
	*/
    protected function savebefore($table, $cans, $id, $addbo){
        if(getconfig('systype')=='demo' && $id>0)return '演示请勿编辑';
        //$user = strtolower(trimstr($cans['user']));
        $user = trimstr($cans['user']);
        $name = trimstr($cans['name']);
        $num  = trimstr(arrvalue($cans,'num'));
        $email= trimstr($cans['email']);
        $pass = $cans['pass'];
        $check= c('check');
        $mobile 	= $cans['mobile'];
        $weixinid 	= $cans['weixinid'];
        $pingyin 	= $cans['pingyin'];
        if(!isempt($pass)){
            if(strlen($pass)<4)return '密码至少要4位数';
        }
        $msg  = '';
//        var_dump($addbo);exit;
        if($check->isincn($user))return '用户名不能有中文';
        if(strlen($user)<2)return '用户名必须2位字符以上';
        if(!isempt($email) && !$check->isemail($email))return '邮箱格式有误';
        if(!isempt($pingyin) && $check->isincn($pingyin))return '名字拼音不能有中文';
        if(!isempt($num) && $check->isincn($num))return '编号不能有中文';
        if(!isempt($mobile)){
            if(!$check->ismobile($mobile))return '手机格式有误';
        }
        if(isempt($mobile) && isempt($email))return '邮箱/手机号不能同时为空';
        if(!isempt($weixinid)){
            if(is_numeric($weixinid))return '微信号不能是数字';
            if($check->isincn($weixinid))return '微信号不能有中文';
        }
        $db  = m($table);

        if($msg=='')if($db->rows("`user`='$user' and `id`<>'$id'")>0)$msg ='用户名['.$user.']已存在';


        $rows = array();
        if($msg == ''){
            $did  = $cans['deptid'];
            $sup  = $cans['superid'];
            $rows = $db->getpath($did, $sup);
        }
        if(isempt($pingyin))$pingyin = c('pingyin')->get($name,1);
        $rows['pingyin'] = $pingyin;
        $rows['user'] 	= $user;
        $rows['name'] 	= $name;
        $rows['email'] 	= $email;
        $notsave		= '';

        if($addbo){
            if(isempt($pass))$pass = '123456';
            $rows['pass'] 	= md5($pass);
        }else{
            if(isempt($pass)){
                $notsave	= 'pass';
            }else{
                $rows['pass'] 	= md5($pass);
            }
        }
        $arr = array('msg'=>$msg, 'rows'=>$rows,'notsave'=>$notsave);
        return $arr;
    }
    public function companydata()
    {
        return m('company')->getselectdata(1);
    }


    /**
	*	重写函数：保存后处理，主要保存其他表数据
	*	$table String 对应表名
	*	$arr Array 表单参数
	*	$id Int 对应表上记录Id
	*	$addbo Boolean 是否添加时
	*/	
	protected function saveafter($table, $cans, $id, $addbo){
        m($table)->record(array('superman'=>$cans['name']), "`superid`='$id'");
        $mygroup = $cans['groupname'];
        m('sjoin')->addgroupuid($id, $mygroup);
        return m('admin')->updateinfo('and a.id='.$id.'');
	}
    public function flylimit(){
        $arr[]=array(
            "name"=>"121"
        );
        $arr[]=array(
            "name"=>"135"
        );
        $arr[]=array(
            "name"=>"91"
        );
        return $arr;
    }
    public function bflylimit(){
        $arr[]=array(
            "name"=>"91"
        );
        return $arr;
    }
    public function permittype(){
//        $str="<span id=\"div_temp_dwid\" class=\"divinput\"><table width=\"98%\" cellpadding=\"0\" border=\"0\"><tbody><tr><td width=\"100%\"><input  class=\"inputs\" style=\"width:99%\" value=\"\" readonly=\"\" type=\"text\" name=\"permittype\"></td><td nowrap=\"\"><a href=\"javascript:;\"  class=\"webbtn\" data-toggle=\"modal\" data-target=\"#myModal\">选择</a></td></tr></tbody></table></span>";
        $str="<span id=\"div_temp_dwid\" class=\"divinput\"><table width=\"98%\" cellpadding=\"0\" border=\"0\"><tbody><tr><td width=\"100%\"><input id='credentialsType'  class=\"inputs\" style=\"width:99%\" value=\"\" readonly=\"\" type=\"text\" name=\"permittype\"></td><td nowrap=\"\"><a href=\"javascript:;\"  class=\"webbtn\" id=\"permit\">选择</a></td></tr></tbody></table></span><div  id=\"bjblock\"></div>";
        return $str;
    }
    //组数据源
    public function groupdata()
    {
        return m('sjoin')->getgrouparrs();
    }

    //生成列表页，数据读取后处理
    protected function storeafter($table, $rows)
    {
        $barr['rows'] = $rows;
        if($this->loadci==1 && $this->post('atype')=='txlmy'){
            $this->depta = array();
            $drows = m('dept')->getdata($rows);
            $barr['deptdata'] = $this->depttreeshu($drows,'0');
        }
        return $barr;
    }

    //更新在线的状态，token10分钟内都是在线
    protected function storebefore($table)
    {
        m('login')->updateallonline();
    }

    //组织结构活动得到树形数据
    private function depttreeshu($rows, $pid)
    {
        $barr = array();
        foreach($rows as $k=>$rs){
            if($rs['pid']==$pid){
                $rs['children'] = $this->depttreeshu($rows, $rs['id']);
                $rs['expanded'] = $pid=='0';
                $barr[] = $rs;
            }
        }
        return $barr;
    }

    //修改上级
    public function editsuperAjax()
    {
        $sna	= $this->post('sna');
        $sid	= $this->post('sid');
        $xid	= $this->post('xid');
        m('admin')->update(array(
            'superid' => $sid,
            'superman' => $sna,
        ),"`id` in($xid) and id not in($sid)");

        m('admin')->updateinfo(); //更新
        return 'ok';
    }
}
			
