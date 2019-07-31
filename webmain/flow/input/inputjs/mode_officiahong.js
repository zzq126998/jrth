//流程模块【officiahong.发文套红】下录入页面自定义js页面,初始函数
function initbodys(){

	imgbase64 = '';
	if(mid==0){
		form('gwtitle').value=c.getseltext('gwid');
	}else{
		getshowcont();
	}
	$(form('gwid')).change(function(){
		form('gwtitle').value=c.getseltext(this.name);
		getshowcont();
	});
	$(form('mbid')).change(function(){
		form('mbtitle').value=c.getseltext(this.name);
		getshowcont();
	});
	form('officiatitle').readOnly=false;
	$(form('officiatitle')).blur(function(){
		getshowcont();
	});
}

function changesubmit(d){
	return {'imgbase64':imgbase64};
}

function getshowcont(){
	var gwid = form('gwid').value,mbid=form('mbid').value;
	if(gwid==''||mbid==''||!get('showcont'))return;
	js.ajax(geturlact('getcont'),{gwid:gwid,mbid:mbid,taititle:form('officiatitle').value}, function(ret){
		$('#showcont').html('<div style="background:white">'+ret+'</div>');
		/*
		html2canvas($('#showcont'),{
			onrendered: function(canvas){
				imgbase64 = canvas.toDataURL().split(',')[1];
			}
		});*/
	},'post');
}