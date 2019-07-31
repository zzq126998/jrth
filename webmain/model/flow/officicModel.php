<?php
//公文查阅
class flow_officicClassModel extends flowModel
{
	public $xiangbordercolor = 'red';//默认边框颜色
	private $readunarr = array();
	
	public function initModel()
	{
		$this->logobj = m('log');
	}
	
	//打开详情时跳转到地理位置显示
	protected function flowchangedata()
	{
		/*
		if(!isajax()){
			$num = ($this->rs['type']=='1') ? 'officib' : 'officia';
			$url = $this->getxiangurl($num, $this->id, A);
			$this->rock->location($url);
			exit();
		}*/
	}
	
	protected function flowbillwhere($uid, $lx)
	{
		
	
		$whyere= "and (`startdt` is null or `startdt`<='{$this->rock->date}')";
		$whyere.= " and (`enddt` is null or `enddt`>='{$this->rock->date}')";
		
		//全部的
		if($lx=='all'){
			$whyere = '';
			$this->rock->setsession('officicatype','all');
		}
		
		return array(

			'where' 	=> $whyere,
		);
	}
	
	public function flowrsreplace($rs, $lx=0)
	{
		if($lx==2){
			$zt = $this->logobj->isread($this->mtable, $rs['id'], $this->adminid);
			if($zt>0)$rs['ishui']=1;
			
			
			//全部/未读统计
			$barr 	= $this->logobj->getreadshu($this->mtable, $rs['id'],$rs['receid'] , $rs['optdt'], $this->adminmodel);
			$rs['chatotal'] = ''.$barr['zzshu'].'/<font color=red>'.$barr['wdshu'].'</font>';
		}
		if($lx==1){
			$showcontent = '';
			//收文详情
			if($rs['type']=='0'){
				if($rs['thid']>'0'){
					$showcontent = m('zheng')->gethongconts($rs['thid']);
					$showcontent = '<div style="padding:15pt 0px;">'.$showcontent.'</div>';
				}else{
					$showcontent = m('zheng')->gethongcont($rs['id'], 1);//默认用第一模版
				}
			}
			
			$rs['showcontent'] = $showcontent;

		}
		return $rs;
	}
	
	protected function flowdatalog($arr)
	{
		
		if($this->rs['type']=='0')$arr['notitle'] = true;//去掉标题
		$arr['title'] 	= $this->moders['name'];
		
		//是否关闭查阅记录
		$arr['isgbcy'] 	= $this->moders['isgbcy'];
		if($this->rock->session('officicatype')=='all')$arr['isgbcy'] ='0'; //有权限看全部
		if($arr['isgbcy'] =='0'){
			$barr	= $this->logobj->getreadshu($this->mtable, $this->id,$this->rs['receid'] , $this->rs['optdt'], $this->adminmodel);
			$arr['readunarr'] 			= $barr['wduarr'];//读取未查阅
		}

		return $arr;
	}
	
}