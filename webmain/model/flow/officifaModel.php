<?php
//分发模块
class flow_officifaClassModel extends flowModel
{
	
	
	//作废或删除是
	protected function flowzuofeibill($sm)
	{
		m('official')->update(array(
			'ffid' 		=> '0',
			'receid' 	=> '',
			'recename' 	=> '',
			'startdt' 	=> null,
			'enddt' 	=> null,
		), $this->rs['gwid']);//标识未分发
	}
	
	protected function flowsubmit($na, $sm)
	{
		if($this->rs['status']==1)$this->tisongtodo();
	}
	
	//通知个对应人可查阅
	private function tisongtodo()
	{
		//还没到展示时间就不发送提醒
		$zstart= arrvalue($this->rs, 'startdt');
		if(!isempt($zstart) && $zstart>$this->rock->date)return;
		
		$flow = m('flow')->initflow('officic', $this->rs['gwid'], false);
		$flow->pushs($this->rs['receid'], '您有公文“[{num}]{title}”需要查阅');
	}
	
	protected function flowdatalog($arr)
	{
		$arr['title'] = $this->moders['name'];

		return $arr;
	}
	
}