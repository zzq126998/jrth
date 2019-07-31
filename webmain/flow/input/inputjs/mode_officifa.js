//流程模块【officifa.公文分发传阅】下录入页面自定义js页面,初始函数
function initbodys(){
	
	if(mid==0)changeginfo();
	
	c.onselectdata['title']=function(da){
		changeginfoshow(da);
	}
}
function changeginfo(){
	var gwid = form('gwid').value;
	if(gwid && gwid>0){
		js.ajax(geturlact('getgwinfo'),{'gwid':gwid},function(ret){
			changeginfoshow(ret);
		},'get,json');
	}
}
function changeginfoshow(da){
	if(da.title)form('title').value=da.title;
	form('num').value=da.num;
	form('unitsame').value=da.unitsame;
}

function changesubmit(d){
	if(d.startdt && d.enddt && d.startdt>d.enddt)return '可查阅截止日期必须大于开始日期';
}