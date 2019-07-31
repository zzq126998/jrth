<?php if(!defined('HOST'))die('not access');?>
<script >
$(document).ready(function(){
	
	var c={
		init:function(){
			$.get(js.getajaxurl('getset','{mode}','{dir}'), function(s){
				var a=js.decode(s);
				get('dingdingqiyeid_{rand}').value=a.qiyeid;
				get('dingdingcorpid_{rand}').value=a.corpid;
				get('dingdingsecret_{rand}').value=a.secret;
				get('dingdinghuitoken_{rand}').value=a.huitoken;
				get('dingdingaeskey_{rand}').value=a.aeskey;
				get('dingdinghuiurl_{rand}').value=a.huiurl;
			});
		},
		save:function(o){
			var d={};
			d.qiyeid = get('dingdingqiyeid_{rand}').value;
			d.corpid = get('dingdingcorpid_{rand}').value;
			d.secret = get('dingdingsecret_{rand}').value;
			d.huitoken = get('dingdinghuitoken_{rand}').value;
			d.aeskey   = get('dingdingaeskey_{rand}').value;
			js.msg('wait','保存中...');
			js.ajax(js.getajaxurl('setsave','{mode}','{dir}'), d, function(s){
				js.msg('success','保存成功');
			},'post');
		},
		testss:function(o1,lx){
			if(ISDEMO){js.msg('success','demo上就不要测试，我们都测试通过的');return;}
			js.msg('wait','测试发送中...');
			js.ajax(js.getajaxurl('testsend','{mode}','{dir}'),{lx:lx}, function(a){
				if(a.success){
					js.msg('success',a.msg);
				}else{
					js.msg('msg',a.msg);
				}
			},'get,json');
		}
	};
	js.initbtn(c);
	c.init();
	if(!ISDEMO)$('#showddd_{rand}').append('<a href="<?=URLY?>view_dingvideo.html" target="_blank">[观看视频帮助]</a>');
});
</script>

<div align="left">
<div  style="padding:10px;">
	
	
		
		<table cellspacing="0" width="600" border="0" cellpadding="0">
		
		<tr>
			<td  align="right"><font color=red>*</font> 企业corpid：</td>
			<td class="tdinput"><input id="dingdingqiyeid_{rand}" onblur="this.value=strreplace(this.value)" class="form-control"></td>
		</tr>
		
		<tr>
			<td  align="right"><font color=red>*</font> 钉钉微应用AppKey：</td>
			<td class="tdinput"><input id="dingdingcorpid_{rand}" onblur="this.value=strreplace(this.value)" class="form-control"></td>
		</tr>
		
		<tr>
			<td  align="right" width="180"><font color=red>*</font> 钉钉微应用AppSecret：</td>
			<td class="tdinput">
			<textarea id="dingdingsecret_{rand}" onblur="this.value=strreplace(this.value)" style="height:60px" class="form-control"></textarea>
			<font id="showddd_{rand}"  color="#888888">请到<a href="http://open-dev.dingtalk.com/" target="_blank">[钉钉开发者后台]</a>下获取。</font>
			</td>
		</tr>
		
		<tr>
			<td  align="right"></td>
			<td  class="tdinput" align="left"><button click="testss,0" class="btn btn-default" type="button">测试发送</button>
		</td>
		</tr>
		
		
		<tr>
			<td  colspan="2"><div class="inputtitle">回调Token设置(没有回调就不需要设置)</div></td>
		</tr>
		
		<tr>
			<td  align="right">回调URL：</td>
			<td class="tdinput"><input onfocus="this.select()" id="dingdinghuiurl_{rand}" readonly class="form-control"></td>
		</tr>
		
		<tr>
			<td  align="right">回调Token：</td>
			<td class="tdinput"><input id="dingdinghuitoken_{rand}" onblur="this.value=strreplace(this.value)" class="form-control"></td>
		</tr>

		<tr>
			<td  align="right">回调EncodingAESKey：</td>
			<td class="tdinput">
			<textarea id="dingdingaeskey_{rand}" style="height:60px" onblur="this.value=strreplace(this.value)" class="form-control"></textarea>
			
			</td>
		</tr>
		
		
		
		<tr>
			<td  align="right"></td>
			<td style="padding:15px 0px" colspan="3" align="left"><button click="save" class="btn btn-success" type="button"><i class="icon-save"></i>&nbsp;保存</button>
			</span>
		</td>
		</tr>
		
		</table>
	
</div>
</div>