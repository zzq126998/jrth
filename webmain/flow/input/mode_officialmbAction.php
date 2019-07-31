<?php
/**
*	此文件是流程模块【officialmb.红头文件模版】对应控制器接口文件。
*/ 
class mode_officialmbClassAction extends inputAction{
	
	
	protected function savebefore($table, $arr, $id, $addbo){
		
	}
	
	
	protected function saveafter($table, $arr, $id, $addbo){
		
	}
	
	public function yulanAjax()
	{
		$bodystr = $this->post('bodystr');
		
		echo m('flow:officialmb')->getshowhontcont($bodystr);
	}
}	
			