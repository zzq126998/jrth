<?php if(!defined('HOST'))die('not access');?>
<script >
$(document).ready(function(){
	
	var c={
		init:function(){
			$.get(js.getajaxurl('getset','{mode}','{dir}'), function(s){
				var a=js.decode(s);
				get('weixinqycorpid_{rand}').value=a.corpid;
				get('weixinqydeptid_{rand}').value=a.deptid;
				get('weixinqysecret_{rand}').value=a.secret;
				get('weixinqyhuitoken_{rand}').value=a.huitoken;
				get('weixinqyaeskey_{rand}').value=a.aeskey;
				get('weixinqyhuiurl_{rand}').value=a.huiurl;
				get('weixinqydevagent_{rand}').value=a.devagent;
			});
		},
		save:function(o){
			var d={};
			d.corpid = get('weixinqycorpid_{rand}').value;
			d.deptid = get('weixinqydeptid_{rand}').value;
			d.secret = get('weixinqysecret_{rand}').value;
			d.huitoken = get('weixinqyhuitoken_{rand}').value;
			d.aeskey = get('weixinqyaeskey_{rand}').value;
			d.devagent = get('weixinqydevagent_{rand}').value;
			js.msg('wait','保存中...');
			js.ajax(js.getajaxurl('setsave','{mode}','{dir}'), d, function(s){
				if(s!='ok'){
					js.msg('msg',s);
				}else{
					js.msg('success','保存成功');
				}
			},'post');
		},
		cleareshe:function(){
			if(ISDEMO){js.msg('success','demo上就不要操作');return;}
			js.msg('wait','清除中...');
			js.ajax(js.getajaxurl('clearwxqih','{mode}','{dir}'), false, function(s){
				js.msg('success','清除成功');
			});
		}
	};
	js.initbtn(c);
	c.init();
	if(!ISDEMO)$('#showddd_{rand}').append('<a href="<?=URLY?>?a=down&id=23" target="_blank">[下载帮助文档]</a>');
});
</script>

<div align="left">
<div  style="padding:10px;">
	
	
		
		<table cellspacing="0" width="600" border="0" cellpadding="0">
		
		
		<tr>
			<td  align="right"><font color=red>*</font> 企业微信corpid：</td>
			<td class="tdinput"><input onblur="this.value=strreplace(this.value)" id="weixinqycorpid_{rand}" class="form-control"></td>
		</tr>
		
		<tr>
			<td  align="right" width="190"><font color=red>*</font> 通讯录secret：</td>
			<td class="tdinput">
			<textarea id="weixinqysecret_{rand}"  onblur="this.value=strreplace(this.value)" style="height:60px" class="form-control"></textarea>
			<font id="showddd_{rand}"  color="#888888">请到<a href="http://work.weixin.qq.com/" target="_blank">[企业微信]</a>个管理后台下获取[管理工具→通讯录同步助手]下获取。</font>
			</td>
		</tr>
		<tr>
			<td  align="right">企业微信顶级部门Id：</td>
			<td class="tdinput"><input placeholder="可不填"  onblur="this.value=strreplace(this.value)" id="weixinqydeptid_{rand}" class="form-control"></td>
		</tr>
		
		<tr>
			<td  align="right">默认推送应用名称：</td>
			<td class="tdinput"><input placeholder="当企业微信应用不存在时推送，默认是办公助手"  onblur="this.value=strreplace(this.value)" id="weixinqydevagent_{rand}" class="form-control"></td>
		</tr>
		
		<!--
		<tr>
			<td  align="right">企业微信用户更新信息时：</td>
			<td class="tdinput"><select id="weixinqyuptype_{rand}" class="form-control"></select></td>
		</tr>
		-->
		
		<tr>
			<td  colspan="2"><div class="inputtitle">回调Token设置(没有回调就不需要设置)</div></td>
		</tr>
		
		
		<tr>
			<td  align="right">回调URL(URL在系统设置下)：</td>
			<td class="tdinput"><input onfocus="this.select()" id="weixinqyhuiurl_{rand}" readonly class="form-control"></td>
		</tr>
		
		<tr>
			<td  align="right">回调Token：</td>
			<td class="tdinput"><input id="weixinqyhuitoken_{rand}" onblur="this.value=strreplace(this.value)" class="form-control"></td>
		</tr>

		<tr>
			<td  align="right">回调EncodingAESKey：</td>
			<td class="tdinput">
			<textarea id="weixinqyaeskey_{rand}" style="height:60px"  onblur="this.value=strreplace(this.value)" class="form-control"></textarea>
			
			</td>
		</tr>
		
		
		
		<tr>
			<td  align="right"></td>
			<td style="padding:15px 0px" colspan="3" align="left"><button click="save" class="btn btn-success" type="button"><i class="icon-save"></i>&nbsp;保存</button>
			&nbsp;<button click="cleareshe" class="btn btn-default" type="button">清空微信企业号设置</button>
			</span>
		</td>
		</tr>
		</table>

	
</div>
<div class="tishi">如果你原来使用微信企业号，升级到了企业微信，必须先清空[微信企业号设置]哦。</div>
</div>