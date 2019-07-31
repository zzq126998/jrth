<?php
class dingding_dakaClassModel extends dingdingModel
{
	/**
	*	获取对应人员打卡记录
	*/
	public function getrecord($uidin='', $startdt='', $endddt='')
	{
		$barr	= $this->backarr;
		$dt 		= $this->rock->date;
		if($startdt=='')$startdt 	= $dt;
		if($endddt=='')$endddt 		= $dt;
		
		//不能超过7天的
		$where 		= '1=1';
		if($uidin!='')$where = 'b.`id` in('.$uidin.')';
		
		$urows	= $this->db->getall("select a.`userid`,b.`id` from `[Q]zding_user` a left join `[Q]admin` b on (a.`userid`=b.`user`) or (a.`mobile`=b.`mobile` and b.`mobile` is not null) or (a.`email`=b.`email` and b.`email` is not null) where $where and a.`status`=1 and b.`status`=1");
		$maxlen	= 49;
		
		$useridarrs = array();
		$useridlist	= '';
		$uarrs 		= array();
		$noi 		= 0;
		foreach($urows as $k=>$rs){
			$noi++;
			$useridlist.=',"'.$rs['userid'].'"';
			if($noi==$maxlen){
				$useridarrs[] 	= substr($useridlist, 1);
				$noi=0;
				$useridlist		= '';
			}
			$uarrs[$rs['userid']]  = $rs['id'];
		}
		
		if($noi<$maxlen && $useridlist!=''){
			$useridarrs[] 	= substr($useridlist, 1);
		}
		
		if(!$useridarrs){
			$msg= '没有相关人员';
			$barr['msg'] = $msg;
			return $barr;
		}
		
		$url = $this->gettourl('URL_attendance');
		$token = $this->gettoken();
		$url.= '?access_token='.$token.'';
		
		foreach($useridarrs as $lists){
			$body	= '{"userIds": ['.$lists.'],"checkDateFrom": "'.$startdt.' 00:00:00","checkDateTo": "'.$endddt.' 23:59:59"}';
			$result = c('curl')->postjson($url, $body);
			$barr	= $this->resultquery($result, $uarrs);
			if($barr['errcode']!='0')return $barr;
		}
		
		return $barr;
	}
	
	private function resultquery($result, $uarrs)
	{
		//打卡来源
		$sourceTypearr	= array(
			'ATM' 		=> '考勤机',
			'BEACON' 	=> 'IBeacon',
			'DING_ATM' 	=> '钉钉考勤机',
			'USER' 		=> '用户打卡',
			'BOSS' 		=> '老板改签',
			'APPROVE' 	=> '审批系统',
			'SYSTEM' 	=> '考勤系统',
			'AUTO_CHECK'=> '自动打卡',
		);
		$okload = 0;
		$fenxiarr	= array();
		if($result!=''){
			$arr	= json_decode($result, true);
			$barr 	= $this->setbackarr($arr['errcode'], $arr['errmsg']);
			$dbs 		= m('kqdkjl');
			if($arr['errcode']==0 && isset($arr['recordresult'])){
				$rows = $arr['recordresult'];
				$barr['checkindata'] = $rows;
				//保存到数据库
				foreach($rows as $k=>$rs){
					$userid 		= $rs['userId'];
					$uid 	= (int)$this->rock->arrvalue($uarrs, $userid, '0');
					if($uid==0)continue;
					if($rs['locationResult']=='Outside')continue; //范围外不保存
					$dkdt 	= date('Y-m-d H:i:s', $rs['userCheckTime']/1000);
					
					$where  = "`uid`='$uid' and `dkdt`='$dkdt' and `type`=8";
					if($dbs->rows($where)==0){
						$okload++;
						$outsideRemark 	= arrvalue($rs,'outsideRemark');
						$explain		= $rs['sourceType'].'('.arrvalue($sourceTypearr, $rs['sourceType']).'),'.arrvalue($rs,'locationMethod').'';
						if(!isempt($outsideRemark))$explain.=','.$outsideRemark.'';
						$dbs->insert(array(
							'uid' 		=> $uid,
							'dkdt' 		=> $dkdt,
							'type' 		=> 8,
							'optdt' 	=> $this->rock->now,
							'address' 	=> arrvalue($rs,'baseAddress'),
							'explain' 	=> $explain,
						));
						$dkdta = explode(' ', $dkdt);
						$fenxiarr[''.$dkdta[0].'|'.$uid] = $uid;
					}
					
				}
				$barr['okload'] = $okload;
			}
		}
		
		//考勤分析
		if($fenxiarr){
			$kqobj = m('kaoqin');
			foreach($fenxiarr as $keys=>$uid){
				$keysa = explode('|', $keys);
				$kqobj->kqanay($uid, $keysa[0]);
			}
		}
		
		return $barr;
	}
}