<?php 
/**
*	移动端应用控制器页面
*	主页：http://www.rockoa.com/
*	软件：信呼
*	作者：雨中磐石(rainrock)
*	日期：2016-10-10
*/

class yingClassAction extends ActionNot{
	
	public function initAction()
	{
		$this->mweblogin(0, true);
	}
	
	public function defaultAction()
	{
		$num = $this->get('num');
		$reim= m('reim');
		$arr = $reim->getagent(0, "and `num`='$num'");
		if(!$arr)exit('应用['.$num.']不存在');
		$rs  = $arr[0];
		$this->title = $rs['name'];
		$yyurl 	= ''.P.'/we/ying/yingyong/'.$num.'.html';
		if(!file_exists($yyurl))$yyurl='';
		$yyurljs 	= ''.P.'/we/ying/yingyong/'.$num.'.js';
		if(!file_exists($yyurljs))$yyurljs='';
		
		$rs['iscy'] = $this->iscy($num);
		$this->assign('arr', $rs);
		$this->assign('num', $num);
		$this->assign('openfrom', $this->get('openfrom'));
		$this->assign('yyurl', $yyurl);
		$this->assign('yyurljs', $yyurljs);
		$this->assign('searchmsg', '输入关键词搜索');
		$gid 	= $rs['id'];
		$reim->setallyd('agent', $this->adminid, $gid);
		
		
		$clasne 	= 'ying_'.$num.'Class';
		$classpath  = ''.P.'/we/ying/yingyong/'.$clasne.'.php';
		if(file_exists($classpath)){
			include_once($classpath);
			$yingobj = new $clasne();
			$yingobj->initYing($this);
		}
		
		//记录打开应用日志
		if(getconfig('useropt')=='1')m('log')->addlog('打开应用', '应用['.$num.'.'.$this->title.']');
		
		
		//以下是新版应用页面，不想用可以删掉
		//if($rs['url']=='auto' || $yyurl=='')$this->displayfile = ''.P.'/we/agent/tpl_agent.html';
	}
	
	private function iscy($num)
	{
		$myyyid= $this->option->getval('yinghomeshow_'.$this->adminid.'');
		$iscy  = 0;
		if(!isempt($myyyid) && contain(','.$myyyid.',',','.$num.','))$iscy=1;
		return $iscy;
	}
	
	public function locationAction()
	{
		$this->title = '考勤定位';
		$kq 	= m('kaoqin');
		$arr 	= m('waichu')->getoutrows($this->date,$this->adminid);
		$this->assign('rows', $arr);
		$dt 	= $this->rock->date;
		$dwarr	= m('location')->getrows("uid='$this->adminid' and `optdt` like '$dt%'",'*','`id` desc');
		$this->assign('dwarr', $dwarr);
		$kqrs 	= $kq->dwdkrs($this->adminid, $this->date);
		$isgzh	= m('wxgzh:index')->isusegzh();
		$this->assign('isgzh', $isgzh);
		$this->assign('kqrs', $kqrs);
		$dwids	= arrvalue($kqrs, 'dwids');
		$kqors	= array();
		if(!isempt($dwids)){
			$kqors = m('kqdw')->getrows("id in($dwids) and `id`<>".$kqrs['id']."");
		}
		$this->assign('kqors', $kqors);
		$this->smartydata['qqmapkey']	= getconfig('qqmapkey','55QBZ-JGYLO-BALWX-SZE4H-5SV5K-JCFV7');
	}
	
	/**
	*	最新打卡使用
	*/
	public function dakaAction()
	{
		$this->title = '考勤打卡';
		
		$kq 	= m('kaoqin');
		$dt 	= $this->rock->date;
		$dwarr	= m('location')->getrows("uid='$this->adminid' and `optdt` like '$dt%'",'*','`id` desc');
		$this->assign('dwarr', $dwarr);
		$kqrs 	= $kq->dwdkrs($this->adminid, $this->date);
		$isgzh	= m('wxgzh:index')->isusegzh();
		$this->assign('isgzh', $isgzh);
		$this->assign('iscy', $this->iscy('kqdaka'));
		$this->assign('kqrs', $kqrs);
		$dwids	= arrvalue($kqrs, 'dwids');
		$kqors	= array();
		if(!isempt($dwids)){
			$kqors = m('kqdw')->getrows("id in($dwids) and `id`<>".$kqrs['id']."");
		}
		$this->assign('kqors', $kqors);
		$this->smartydata['qqmapkey']	= getconfig('qqmapkey','55QBZ-JGYLO-BALWX-SZE4H-5SV5K-JCFV7');
	}
}