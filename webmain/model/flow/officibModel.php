<?php
class flow_officibClassModel extends flowModel
{
	public $xiangbordercolor = 'red';//默认边框颜色
	
	public function initModel()
	{
		$this->wherejoin['officifa'] = 'gwid';
	}

	protected function flowdatalog($arr)
	{
		$arr['title'] = $this->moders['name'];
		
		return $arr;
	}
	
	public function flowxiangfields(&$fields)
	{
		$fields['base_name'] 	= '登记人';
		$fields['base_deptname'] = '登记人部门';
		return $fields;
	}
	
	public function flowsearchfields()
	{
		$arr[] = array('name'=>'登记人...','fields'=>'uid');
		return $arr;
	}
	
	//录入页面标题
	public function inputtitle()
	{
		return '收文登记';
	}
	
	public function flowrsreplace($rs, $lx=0)
	{

		
		if(isempt($rs['recename']))$rs['recename']='<font color="blue">未分发</font>';
		
		return $rs;
	}
}