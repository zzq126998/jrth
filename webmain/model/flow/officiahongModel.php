<?php
class flow_officiahongClassModel extends flowModel
{
	//替换
	public function flowrsreplace($rs, $lx=0)
	{
		if($lx==1){
			
			$rs['temp_hongcont']= m('zheng')->gethongconts($rs['id']);
		}
		return $rs;
	}
	
	//作废或删除是
	protected function flowzuofeibill($sm)
	{
		m('official')->update('thid=0', $this->rs['gwid']);//标识未套红
	}
	
	public function iseditqx()
	{
		$bo = parent::iseditqx();
		if($bo){
			$gwid 	= $this->rs['gwid'];
			$to 	= m('official')->rows("`id`='$gwid' and (`yzid`=-1 or `yzid`>0)");
			if($to>0)return false;//已盖章就不能编辑
		}
		return $bo;
	}
}