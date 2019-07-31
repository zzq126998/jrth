<?php
class flow_officialmbClassModel extends flowModel
{
	public $defaultorder = 'sort,asc';

	//替换
	public function flowrsreplace($rs, $lx=0)
	{
		if($lx==1){
			$rs['status'] = $this->getstatusstr($rs);

			$rs['content']= $this->getshowhontcont($rs['content']);
		}
		return $rs;
	}
	
	public function getshowhontcont($cont)
	{
		$btdt	= m('option')->getval('officiatitle');
		if(isempt($btdt))$btdt = '信呼团队文件';
		$cont 	= $this->rock->reparr($cont, array(
			'title' => '关于任命磐石为董事长通知(范文模版)',
			'titles' => '任命 磐石 通知',
			'officiatitle' => $btdt,
			
			'num' => '信呼〔2019〕1号',
			'zuncheng' => '各位同事：',
			'miji' => '机密',
			'content' => '<p>由于磐石认真工作，工作认证勤勤恳恳，主动亲民，特别在对项目上认证对待各个细节。</p><p>经各位领导研究决定任命为董事长，从2019年01月01日起生效。</p>',
			'unitsame' => '信呼开发团队',
			'applydt' => '2019年4月18日',
			'unitname' => '信呼开发团队',
			'zhangimg' => '<img src="http://www.rockoa.com/images/gz.png">',
			'chaoname' => '管理层',
			'fujian'	=> '<div class="hongfile_fujian1">附件：1.平时功能内容.doc</div><div class="hongfile_fujian2">2.领导讨论会议纪要内容.doc</div>'
		));	
		$cont	= '<link rel="stylesheet" type="text/css" href="'.URL.''.P.'/css/csshong.css"/><div style="padding:30px 0px">'.$cont.'</div>';
		return $cont;
	}
}