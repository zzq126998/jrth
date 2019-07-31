<?php
class flow_officiaClassModel extends flowModel
{
	public $xiangbordercolor = 'red';//默认边框颜色
	
	public function initModel()
	{
		$this->wherejoin['officifa'] 	= 'gwid';
		$this->wherejoin['officiahong'] = 'gwid';
	}

	protected function flowdatalog($arr)
	{
		$arr['title'] = $this->moders['name'];

		return $arr;
	}
	
	public function flowxiangfields(&$fields)
	{
		$fields['base_name'] 	= '拟办人';
		$fields['base_deptname'] = '拟办人部门';
		return $fields;
	}
	
	public function flowsearchfields()
	{
		$arr[] = array('name'=>'拟办人...','fields'=>'uid');
		return $arr;
	}
	
	public function inputtitle()
	{
		return '拟办发文稿纸';
	}
	
	public function flowrsreplace($rs, $lx=0)
	{

		if(isset($rs['thid'])){
			if($lx==1){
				if($rs['thid']=='0'){
					$hongfilecont='还未套红';
				}else{
					$hongfilecont=m('zheng')->gethongconts($rs['thid']);
				}
				$rs['hongfilecont'] = $hongfilecont;
			}
			
			$str = '<font color="blue">未套红</font>';
			if($rs['thid']>0)$str = '<font color="red">已套红</font>';
			$rs['thid'] = $str;
		}
		if(isset($rs['yzid'])){
			$str = '<font color="blue">未盖章</font>';
			if($rs['yzid']=='-1')$str = '<font color="#aaaaaa">无需盖章</font>';
			if($rs['yzid']>0){
				$onrs= $this->billmodel->getone("`table`='sealapl' and `mid`='".$rs['yzid']."'");
				$str = $this->getstatus($onrs, '待?处理,已盖章,未通过','', 1);
			}
			$rs['yzid'] = $str;
		}
		
		if(isempt($rs['recename']))$rs['recename']='<font color="blue">未分发</font>';
		
		return $rs;
	}
}