<?php 
class loginClassAction extends ActionNot{
	
	public function defaultAction()
	{
		$this->tpltype	= 'html';
		$this->smartydata['ca_adminuser']	= $this->getcookie('ca_adminuser');
		$this->smartydata['ca_rempass']		= $this->getcookie('ca_rempass');
		$this->smartydata['ca_adminpass']	= $this->getcookie('ca_adminpass');
		$this->smartydata['loginyzm']		= (int)getconfig('loginyzm','0'); //登录类型
	}
	
	public function checkAjax()
	{
		$user 	= $this->jm->base64decode($this->post('adminuser'));
		$user	= str_replace(' ','',$user);
		$pass	= $this->jm->base64decode($this->post('adminpass'));
		$rempass= $this->post('rempass');
		$jmpass	= $this->post('jmpass');
		$cfrom	= $this->post('cfrom','pc');
		if($jmpass == 'true')$pass=$this->jm->uncrypt($pass);
		$userp	= $user;
		$arr 	= m('login')->start($user, $pass, $cfrom);
		$barr 	= array();
		if(is_array($arr)){
			
			if(isset($arr['mobile'])){
				$barr = $arr;
				$barr['success'] = false;
				return $barr;
			}
			
			$uid 	= $arr['uid'];
			$name 	= $arr['name'];
			$user 	= $arr['user'];
			$token 	= $arr['token'];
			$face 	= $arr['face'];
			m('login')->setsession($uid, $name, $token, $user);
			$this->rock->savecookie('ca_adminuser', $userp);


            $_confpath		= $this->rock->strformat('?0/?1/?1Config.php', ROOT_PATH, PROJECT);
            $arr 			= require($_confpath);
			if($userp=="admin"){
                $arr['companymode']=false;

            }else{
                $arr['companymode']=true;
            }
            $smarr['url']			= '系统URL';
            $smarr['localurl']		= '本地系统URL，用于服务器上浏览地址';
            $smarr['title']			= '系统默认标题';
            $smarr['neturl']		= '系统外网地址，用于公网';
            $smarr['apptitle']		= 'APP上和手机网页版上的标题';
            $smarr['reimtitle']		= 'REIM即时通信上标题';
            $smarr['weblogo']		= 'PC客户端上的logo图片';
            $smarr['db_host']		= '数据库地址';
            $smarr['db_user']		= '数据库用户名';
            $smarr['db_pass']		= '数据库密码';
            $smarr['db_base']		= '数据库名称';
            $smarr['perfix']		= '数据库表名前缀';
            $smarr['qom']			= 'session、cookie前缀';
            $smarr['highpass']		= '超级管理员密码，可用于登录任何帐号';
            $smarr['db_drive']		= '操作数据库驱动有mysql,mysqli,pdo三种';
            $smarr['randkey']		= '系统随机字符串密钥';
            $smarr['asynkey']		= '这是异步任务key';
            $smarr['openkey']		= '对外接口openkey';
            $smarr['sqllog']		= '是否记录sql日志保存'.UPDIR.'/sqllog下';
            $smarr['asynsend']		= '是否异步发送提醒消息，0同步，1自己服务端异步，2官网VIP用户异步';
            $smarr['install']		= '已安装，不要去掉啊';
            $smarr['xinhukey']		= '信呼官网key，用于在线升级使用';
            $smarr['bcolorxiang']	= '单据详情页面上默认展示线条的颜色';
            $smarr['debug']			= '为true调试开发模式,false上线模式';
            $smarr['reim_show']		= '首页是否显示REIM';
            $smarr['mobile_show']	= '首页是否显示手机版';
            $smarr['loginyzm']		= '登录方式:0仅使用帐号+密码,1帐号+密码/手机+验证码,2帐号+密码+验证码,3仅使用手机+验证码';
            $smarr['officeyl']		= '文档Excel.Doc预览类型,0自己部署插件，1使用官网支持任何平台';
            $smarr['officedk']		= '文件预览打开方式1新窗口打开';
            $smarr['useropt']		= '1记录用户操作保存到日志里,空不记录';
            $smarr['defstype']		= 'PC后台主题皮肤，可以设置1到34';
            $smarr['editpass']		= '用户登录修改密码：0不用修改，1强制用户必须修改';
            $smarr['companymode']	= '多单位模式，true就是开启';
            $smarr['outurl']		= '这个地址当你内网地址访问时向手机推送消息的地址';

            $str1 = '';
            foreach($arr as $k=>$v){
                $bz = '';
                if(isset($smarr[$k]))$bz='	//'.$smarr[$k].'';
                if(is_bool($v)){
                    $v = $v ? 'true' : 'false';
                }else{
                    $v = "'$v'";
                }
                $str1.= "	'$k'	=> $v,$bz\n";
            }

            $str = '<?php
if(!defined(\'HOST\'))die(\'not access\');
//['.$this->adminname.']在'.$this->now.'通过[系统→系统工具→系统设置]，保存修改了配置文件
return array(
'.$str1.'
);';
            file_put_contents($_confpath, $str);


			$this->rock->savecookie('ca_rempass', $rempass);
			$ca_adminpass	= $this->jm->encrypt($pass);
			if($rempass=='0')$ca_adminpass='';
			$this->rock->savecookie('ca_adminpass', $ca_adminpass);
			$barr['success'] = true;
			$barr['face'] 	 = $face;
		}else{
			$barr['success'] = false;
			$barr['msg'] 	 = $arr;
		}
		return $barr;
	}
	
	public function exitAction()
	{
		m('dept')->online(0);//离线
		m('login')->exitlogin('pc',$this->admintoken);
		$this->rock->location('?m=login');
	}
}