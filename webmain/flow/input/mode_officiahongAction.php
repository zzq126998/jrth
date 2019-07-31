<?php
/**
*	此文件是流程模块【officiahong.发文套红】对应控制器接口文件。
*/ 
class mode_officiahongClassAction extends inputAction{
	
	
	protected function savebefore($table, $arr, $id, $addbo){
		$gwid = $arr['gwid'];
		$to 	= m('official')->rows("`id`='$gwid' and (`yzid`=-1 or `yzid`>0)");
		if($to>0)return '已盖章操作了不允许在操作';
		
		$imgbase64 = $this->post('imgbase64');
		$saveimg = '';
		//保存的成图，方便申请印章时使用
		if(!isempt($imgbase64)){
			$saveimg = ''.UPDIR.'/'.date('Y-m').'/officia'.str_shuffle('abcdefg').''.$gwid.'.png';
			$this->rock->createtxt($saveimg, base64_decode($imgbase64));
		}
		$oldsaveimg = arrvalue($this->rs, 'saveimg');
		if(!isempt($oldsaveimg) && file_exists($oldsaveimg))@unlink($oldsaveimg);//删除原来的图片
		return array(
			'rows' => array(
				'saveimg' => $saveimg
			)
		);
	}
	
	
	protected function saveafter($table, $arr, $id, $addbo){
		m('official')->update('`thid`='.$id.'', $arr['gwid']);//更新到公文表里
	}
	
	//获取我需要套好的公文
	public function getmygongwen()
	{
		$gwid 	= (int)$this->rock->arrvalue($this->rs, 'gwid','0');
		$rows  	= m('official')->getall('uid='.$this->adminid.' and `type`=0 and `status`=1 and (`thid`=0 or id='.$gwid.')');
		$arr 	= array();
		foreach($rows as $k=>$rs){
			$arr[]=array(
				'value' => $rs['id'],
				'name' => '['.$rs['num'].']'.$rs['title'].'',
			);
		}
		return $arr;
	}
	
	public function getmoban()
	{
		$mbid 	= (int)$this->rock->arrvalue($this->rs, 'mbid','0');
		$strs1	= m('admin')->getjoinstr('`receid`', $this->adminid);
		$rows  	= m('officialmb')->getall('`status`=1 '.$strs1.'','*','`sort`');
		$arr 	= array();
		foreach($rows as $k=>$rs){
			$arr[]=array(
				'value' => $rs['id'],
				'name' => '['.$rs['fenlei'].']'.$rs['title'].'',
			);
		}
		return $arr;
	}
	
	//显示陶文后的模版
	public function getcontAjax()
	{
		$gwid = (int)$this->post('gwid','0');
		$mbid = (int)$this->post('mbid','0');
		$offtit = $this->post('taititle');
		
		return m('zheng')->gethongcont($gwid, $mbid, true, $offtit);
	}
	
	public function getofficiatitle()
	{
		return $this->option->getdata('officiatitle');
	}
}	
			