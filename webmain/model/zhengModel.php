<?php
/**
*	政务系统使用
*/
class zhengClassModel extends Model
{
	public function initModel()
	{
		$this->settable('official');
	}
	
	//套红后得到的文件内容
	public function gethongcont($gwid, $mbid, $iscs=true, $offtit='')
	{
		if($gwid==0 || $mbid==0)return '';
		$gwrs = $this->getone($gwid);
		if(arrvalue($gwrs, 'status')!='1')return '';
		$mbrs = m('officialmb')->getone($mbid);
		if(!$mbrs)return '';
		if(isempt($offtit))$offtit= m('option')->getval('officiatitle');
		$gwrs['officiatitle'] = str_replace(array('，',','),'<br>', $offtit);
		$gwrs['applydt']	  = date('Y年m月d日', strtotime($gwrs['applydt']));
		//印章读取
		$yzid = $gwrs['yzid'];
		$sealid = m('sealapl')->getmou('sealid', "`id`='$yzid' and `status`=1");
		$zhang	= '';
		if(!isempt($sealid)){
			$sealrow = m('seal')->getall("`id` in($sealid)");
			foreach($sealrow as $k1=>$rs1)$zhang .= '<img src="'.$rs1['sealimg'].'">';
		}
		$gwrs['zhangimg'] = $zhang;
		
		//附件读取
		$fujian 		  = '';
		$filecontid	= $gwrs['filecontid'];
		$fobj 		= m('file');
		$zfrs		= array();
		if(!isempt($filecontid))$zfrs = $fobj->getfile('',0,"`id` in($filecontid)");
		$zfrs1		= $fobj->getfile('official',$gwid);
		$zfarr		= array_merge($zfrs, $zfrs1);
		foreach($zfarr as $k1=>$rs1){
			$fstr1 = $fobj->getfilestr($rs1, 3);
			if($k1==0){
				$fujian='<div class="hongfile_fujian1">附件：1.'.$fstr1.'</div>';
			}else{
				$fujian.='<div class="hongfile_fujian2">'.($k1+1).'.'.$fstr1.'</div>';
			}
		}
		$gwrs['fujian'] = $fujian;
		
		$cont = $this->rock->reparr($mbrs['content'], $gwrs);
		if($iscs)$cont='<link rel="stylesheet" type="text/css" href="'.$this->rock->getouturl().'webmain/css/csshong.css"><div class="hongfile_main">'.$cont.'</div>';
		return $cont;
	}
	
	public function gethongconts($thid)
	{
		$thrs = m('officialhong')->getone($thid);
		if(!$thrs)return '';
		return $this->gethongcont($thrs['gwid'], $thrs['mbid'], true, $thrs['officiatitle']);
	}
	
}