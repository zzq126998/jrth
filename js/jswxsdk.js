/**
*	微信jssdk引入初始化等
*/

js.jssdkstate = 0;
//初始化成功就回调
js.jssdkcall  = function(bo){
	
}

//初始化jssdk
js.jssdkinit  = function(afe, qxlist){
	if(!afe){
		$.getScript('https://res.wx.qq.com/open/js/jweixin-1.2.0.js', function(){
			js.jssdkinit(true, qxlist);
		});
		return;
	}
	if(!qxlist)qxlist= ['openLocation','getLocation','chooseImage','getLocalImgData','startRecord','stopRecord','onVoiceRecordEnd','uploadVoice'];
	$.ajax({
		'type':'post','url':'api.php?m=weixin&a=getsign',dataType:'json',
		data:{url:jm.base64encode(location.href),agentid:js.request('agentid')},
		success:function(da){
			var ret = da.data;
			if(!ret.appId)return js.jssdkcall(false);
			wx.config({
				debug: false,
				appId: ret.appId,
				timestamp:ret.timestamp,
				nonceStr: ret.nonceStr,
				signature: ret.signature,
				jsApiList:qxlist
			});
			wx.ready(function(){
				if(js.jssdkstate==0)js.jssdkstate = 1;
				js.jssdkcall(true);
			});
			wx.error(function(res){
				js.jssdkstate = 2;
				js.jssdkcall(false);
			});
		},
		error:function(){
			js.jssdkcall(false);
		}
	});
}

/**
*	微信录音的
*	js.wxRecord.startLuyin(this);
*/
js.wxRecord = {
	
	//开始录音
	startLuyin:function(o1){
		this.btnobj = o1;
		this.btnobj.disabled = true;
		this.shijianstr=js.now('time');
		this.shijianst1=0;
		js.loading('录音中(<span id="luyintime">0</span>)<br>点我结束录音...');
		wx.startRecord();
		get('rockmodelmsg').onclick=false;
		$('#rockmodelmsg').click(function(){
			js.wxRecord.stopLuyin();
		});
		this.localId = '';
		this.startLuyintime = setInterval(function(){js.wxRecord.startLuyinss();},200);
		wx.onVoiceRecordEnd({
			complete: function (res) {
				js.wxRecord.localId = res.localId;
				clearInterval(js.wxRecord.startLuyintime);
				js.wxRecord.stopLuyinok();
			}
		});
	},
	stopLuyin:function(){
		js.loading('录音结束...');
		clearInterval(this.startLuyintime);
		wx.stopRecord({
			success: function (res) {
				js.wxRecord.localId = res.localId;
				clearTimeout(js.wxRecord.startLuyintime);
				js.wxRecord.stopLuyinok();
			}
		});
		this.startLuyintime=setTimeout(function(){js.wxRecord.stopLuyinok();},1000);
	},
	stopLuyinok:function(){
		var lid = this.localId;
		this.btnobj.disabled = false;
		if(!lid){
			js.msgerror('录音失败');
		}else{
			js.loading('录音上传中...');
			wx.uploadVoice({
				localId: lid,
				isShowProgressTips: 1,
				success: function (res) {
					js.wxRecord.getFileid(res.serverId);
				},
				fail:function(){
					js.msgerror('上传失败');
				}
			});
		}
	},
	startLuyinss:function(){
		var stme = js.now('time');
		var sj 	 = js.float((stme - this.shijianstr)/1000,1);
		this.shijianst1 = sj;
		$('#luyintime').html(sj);
		if(sj>=50)this.stopLuyin();//最多录50秒
	},
	getFileid:function(serid){
		js.loading('拉取录音文件中...');
		$.ajax({
			'type':'get','url':'api.php?m=weixin&a=getmedia',dataType:'json',
			data:{media_id:serid,type:'amr'},
			success:function(da){
				if(!da.success){
					js.msgerror(da.msg);
				}else{
					js.unloading();
					js.wxRecord.success(da.data);
				}
			},
			error:function(e){
				js.msgerror('拉取录音文件失败:'+e.responseText+'');
			}
		});
	},
	success:function(){
		
	}
}