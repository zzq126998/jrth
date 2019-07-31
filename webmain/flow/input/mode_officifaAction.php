<?php
/**
*	此文件是流程模块【officifa.公文分发传阅】对应控制器接口文件。
*/ 
class mode_officifaClassAction extends inputAction{
	
	
	protected function savebefore($table, $arr, $id, $addbo){
		
	}
	
	
	protected function saveafter($table, $arr, $id, $addbo){

		m('official')->update(array(
			'ffid' => $id,
			'receid' => $arr['receid'],
			'recename' => $arr['recename'],
			'startdt' => $arr['startdt'],
			'enddt' => $arr['enddt'],
		),$arr['gwid']);
	}
	
	//获取要分发的公文，发文需要套红和盖章才可以分发
	public function getgongwen()
	{
		$sysmid = (int)$this->get('sysmid','0');
		$gwid 	= 0;
		if($sysmid>0){
			$gwid 	= (int)m('officialfa')->getmou('gwid', $sysmid);
		}
		$rows = m('official')->getall("`uid`='$this->adminid' and `status`=1 and ((`thid`>0 and (`yzid`>0 or `yzid`=-1) and `type`=0) or (`type`=1)) and (`ffid`=0 or `id`='$gwid')");
		$barr = array();
		foreach($rows as $k=>$rs){
			$barr[] = array(
				'name' => $rs['title'],
				'subname' => $rs['num'],
				'num' => $rs['num'],
				'unitsame' => $rs['unitsame'],
				'value' => $rs['id'],
			);
		}
		return $barr;
	}
	
	public function getgwinfoAjax()
	{
		$gwid = (int)$this->get('gwid','0');
		$rs = m('official')->getone("`id`='$gwid'",'id,num,title,unitsame');
		
		return $rs;
	}
}	
			