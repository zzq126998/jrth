//流程模块【officialmb.红头文件模版】下录入页面自定义js页面,初始函数
function initbodys(){
	if(ismobile==0){
		$('#btnmoren').click(yinrumode);
		$('#yulanmoren').click(yulanshow);
		$('body').append('<link rel="stylesheet" type="text/css" href="webmain/css/csshong.css"/>');
	}
}
function yinrumode(){
	var str = '<div class="hongfile_toptitle">{officiatitle}</div><div class="hongfile_num">{num}</div><div class="hongfile_heng">&nbsp;</div><div class="hongfile_title">	{title}</div><div class="hongfile_chenhu">	{zuncheng}</div><div class="hongfile_content">	{content}</div>{fujian}<div class="hongfile_zhangd">	<div class="hongfile_zhangs">{zhangimg}	</div>	<div class="hongfile_right0">		{unitsame}</div>	<div class="hongfile_right1">		{applydt}	</div></div><div class="hongfile_zhuti">	主题词：<span class="hongfile_base">{titles}</span></div><div class="hongfile_zhusong">	主送：{unitname}</div><div class="hongfile_chaosong">	抄送：{chaoname}</div><div class="hongfile_yinfa">	{unitsame}<span>{applydt}印发</span></div>';
	c.editorobj['content'].html(str);
	yulanshow();
	
}
function yulanshow(){
	var str = c.editorobj['content'].html();
	$('#yulanshow').html(str);
	js.ajax(geturlact('yulan'),{bodystr:str}, function(ret){
		$('#yulanshow').html(ret);
	},'post');
}