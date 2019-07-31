<?php if(!defined('HOST'))die('not access');?>
<script >
$(document).ready(function(){
	var a = $('#view_{rand}').bootstable({
		tablename:'wxqy_agent',sort:'sort',dir:'asc',celleditor:true,
		bodyStyle:'height:'+(viewheight-130)+'px;overflow:auto',
		columns:[{
			text:'应用Logo',dataIndex:'square_logo_url',renderer:function(v){
				if(isempt(v))v='images/noface.png';
				return '<img src="'+v+'" height="30" width="30">';
			}
		},{
			text:'名称',dataIndex:'name',editor:true
		},{
			text:'应用ID',dataIndex:'agentid'	
		},{
			text:'可信任域名',dataIndex:'redirect_domain',editor:true
		},{
			text:'简介',dataIndex:'description',align:'left',editor:true,type:'textarea',width:200
		},{
			text:'主页地址',dataIndex:'home_url',align:'left',type:'textarea',width:200,renderer:function(v){
				return '<div style="width:180px;word-wrap:break-word;">'+v+'</div>';
			}
		},{
			text:'可用部门',dataIndex:'allow_partys'
		},{
			text:'是否禁用',dataIndex:'close',type:'checkbox'
		},{
			text:'操作',dataIndex:'opt',renderer:function(v,d){
				var s='<a href="javascript:;" onclick="upsse{rand}('+d.agentid+',0)">获取信息</a>';
				s+='<br><a href="javascript:;" onclick="upsse{rand}('+d.agentid+',1)">[更新]</a>';
				return s;
			}
		},{
			text:'应用菜单',dataIndex:'opt',renderer:function(v,d){
				var s='<a href="javascript:;" onclick="upssemenu{rand}('+d.agentid+',0)">[设置菜单]</a>';
				s+='<br><a href="javascript:;" onclick="upssemenu{rand}('+d.agentid+',2)">[更新]</a> <a href="javascript:;" onclick="upssemenu{rand}('+d.agentid+',3)">[清空]</a>';
				return s;
			}
		}],
		itemclick:function(){
			btn(false);
		}
	});
	
	function btn(bo){
		get('faxis_{rand}').disabled = bo;
		get('del_{rand}').disabled = bo;
		get('edit_{rand}').disabled = bo;
	}

	
	var c = {
		del:function(){
			a.del({url:js.getajaxurl('delying','{mode}','{dir}')});
		},
		faxiaox:function(){
			var d=a.changedata;
			js.prompt('向应用['+d.name+']发送消息','消息内容，接收人是“<b>'+adminname+'</b>”',function(lx,txt){
				if(lx=='yes'&&txt)c.sheniokx(d.name,txt)
			});
		},
		sheniokx:function(na,txt){
			js.msg('wait','发送中...');
			js.ajax(js.getajaxurl('sendagent','{mode}', '{dir}'),{name:na,msg:txt}, function(d){
				if(d.errcode==0){
					js.msg('success', '成功:'+d.msg+'');
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'post,json');
		},
		getagent:function(id){
			js.msg('wait','获取中...');
			js.ajax(js.getajaxurl('getagent','{mode}', '{dir}'),{agentid:id}, function(d){
				if(d.errcode==0){
					js.msg('success', '获取成功');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		updateagent:function(id){
			js.msg('wait','更新中...');
			js.ajax(js.getajaxurl('setagent','{mode}', '{dir}'),{agentid:id}, function(d){
				if(d.errcode==0){
					js.msg('success', '更新成功');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		addwin:function(o,lx){
			var h = $.bootsform({
				title:'企业微信应用',height:400,width:400,
				tablename:'wxqy_agent',isedit:lx,
				url:js.getajaxurl('publicsave','{mode}','{dir}'),
				submitfields:'name,agentid,secret,home_url',
				items:[{
					labelText:'名称',name:'name',required:true,repEmpty:true
				},{
					labelText:'应用ID',name:'agentid',required:true,repEmpty:true
				},{
					labelText:'secret管理密钥',name:'secret',required:true,type:'textarea',height:80,repEmpty:true
				},{
					labelText:'主页地址',name:'home_url',type:'textarea',height:80,repEmpty:true
				}],
				success:function(){
					a.reload();
				}
			});
			if(lx==1){
				h.setValues(a.changedata);
			}
			h.getField('name').focus();
		},
		daoru:function(){
			js.confirm('导入官网应用将会覆盖新增的应用哦，首次使用可直接导入！',function(jg){
				if(jg=='yes')c.daoruss();
			});
		},
		daoruss:function(){
			js.msg('wait','导入中...');
			js.ajax(js.getajaxurl('daoruagent','{mode}', '{dir}'),{}, function(d){
				if(d.errcode==0){
					js.msg('success', '导入成功');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		menuget:function(id){
			js.msg('wait','获取中...');
			js.ajax(js.getajaxurl('menuget','{mode}', '{dir}'),{agentid:id}, function(d){
				if(d.errcode==0){
					js.msg('success', '获取成功');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		menuupdate:function(id){
			js.msg('wait','菜单更新中...');
			js.ajax(js.getajaxurl('menuupdate','{mode}', '{dir}'),{agentid:id}, function(d){
				if(d.errcode==0){
					js.msg('success', '更新成功');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		menuset:function(){
			var d = a.changedata,i,lxs;
			var str = '<table width="100%"><tr><td align="center"  height="30" nowrap>菜单名称</td><td>菜单类型</td><td>对应值(URL/key)</td></tr>';
			for(i=1;i<=12;i++){
				var lxs = 'style="padding-left:30px"';
				if(i==1 || i==5 || i==9)lxs='';
				str+='<tr><td width="25%" '+lxs+'><input maxlength="8" id="abc_xtname'+i+'" class="form-control"></td><td width="30%"><select class="form-control"  id="abc_xttype'+i+'"><option value=""></option><option value="view">URL地址跳转</option><option value="click">点击推事件</option><option value="scancode_push">扫码推事件</option><option value="pic_sysphoto">弹出系统拍照发图</option><option value="pic_photo_or_album">弹出拍照或者相册发图</option><option value="pic_weixin">弹出企业微信相册发图器</option><option value="location_select">弹出地理位置选择器</option></select></td><td><input id="abc_xturl'+i+'" class="form-control"></td></tr>';
			}
			str+='</table>';
			js.tanbody('wxmenu','设置['+d.name+']菜单',650,300,{
				html:'<div style="height:460px;overflow:auto;padding:5px">'+str+'</div>',
				btn:[{text:'确定'}]
			});
			$('#msgview_wxmenu').html('3个一级菜单，有凹进去的行是二级菜单');
			if(isempt(d.menujson))d.menujson='[]';
			var menu = js.decode(d.menujson);
			var btna = menu.button,oi,d1,d2,oj;
			if(btna)for(i=0;i<btna.length;i++){
				oi = i+1;
				if(oi==2)oi=5;
				if(oi==3)oi=9;
				d1 = btna[i];
				get('abc_xtname'+oi+'').value=d1.name;
				get('abc_xttype'+oi+'').value=d1.type;
				get('abc_xturl'+oi+'').value=(d1.url) ? d1.url: d1.key;
				d2 = d1.sub_button;
				if(d2)for(var j=0;j<d2.length;j++){
					oj = oi+1+j;
					d1 = d2[j];
					get('abc_xtname'+oj+'').value=d1.name;
					get('abc_xttype'+oj+'').value=d1.type;
					get('abc_xturl'+oj+'').value=(d1.url) ? d1.url: d1.key;
				}
			}
			$('#wxmenu_btn0').click(function(){
				c.menusetok();
			});
		},
		menugetur:function(ty,url){
			if(ty=='view'){
				if(url.indexOf('http')!=0)url='http://'+url+'';
				if(url.indexOf('agentid')<0){
					var lx = url.indexOf('?')<0 ? '?' : '&';
					url+=''+lx+'agentid='+a.changedata.agentid+''
				}
				return url;
			}else{
				return url;
			}
		},
		menusetok:function(){
			var ne,ty,url,str='',es,sustr;
			for(var i=1;i<=12;i++){
				ne = get('abc_xtname'+i+'').value;
				ty = get('abc_xttype'+i+'').value;
				url = get('abc_xturl'+i+'').value;
				if(i==1 || i==5 || i==9)if(ne && ty && url){
					es = (ty=='view')?'url':'key';
					str+=',{"name":"'+ne+'","type":"'+ty+'","'+es+'":"'+this.menugetur(ty,url)+'"';
					sustr = this.menusetoksb(i);
					if(sustr)str+=',"sub_button":'+sustr+'';
					str+='}';
				}
			}
			if(str!=''){
				str='{"button":['+str.substr(1)+']}';
			}else{
				str= '[]';
			}
			js.msg('wait','菜单保存中...');
			js.ajax(js.getajaxurl('savemenu','{mode}', '{dir}'),{id:a.changedata.id,menujson:str}, function(s){
				js.msg('success', '菜单设置保存成功，请点[更新]');
				a.reload();
				js.tanclose('wxmenu');
			},'post');
		},
		menusetoksb:function(j){
			var ne,ty,url,str='',es,sustr;
			for(var i=j+1;i<=j+3;i++){
				ne = get('abc_xtname'+i+'').value;
				ty = get('abc_xttype'+i+'').value;
				url = get('abc_xturl'+i+'').value;
				if(ne && ty && url){
					es = (ty=='view')?'url':'key';
					str+=',{"name":"'+ne+'","type":"'+ty+'","'+es+'":"'+this.menugetur(ty,url)+'"}';
				}
			}
			if(str!='')str='['+str.substr(1)+']';
			return str;
		},
		menuclear:function(id){
			js.confirm('确定要清空对应应用菜单吗？',function(jg){
				if(jg=='yes')c.menuclears(id);
			});
		},
		menuclears:function(id){
			js.msg('wait','菜单清空中...');
			js.ajax(js.getajaxurl('menudelete','{mode}', '{dir}'),{agentid:id}, function(d){
				if(d.errcode==0){
					js.msg('success', '菜单已清空');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		}
	};
	upsse{rand}=function(id, lx){
		if(lx==0)c.getagent(id);
		if(lx==1)c.updateagent(id);
	}
	upssemenu{rand}=function(id, lx){
		if(lx==0)setTimeout(function(){c.menuset(id)},10);
		if(lx==1)c.menuget(id);
		if(lx==3)c.menuclear(id);
		if(lx==2)c.menuupdate(id);
	}
	js.initbtn(c);
});
</script>
<div>
<table width="100%">
<tr>
<td nowrap>
<button class="btn btn-primary" click="addwin,0" type="button">新增应用</button>&nbsp;&nbsp;
<button class="btn btn-default" click="daoru" type="button">导入官网应用</button>
</td>
<td width="90%" style="padding-left:10px">创建应用需在<a href="https://work.weixin.qq.com/wework_admin/frame#apps" target="_blank">[企业微信后台]</a>创建，创建后这这里新增才能发消息等。</td>
<td align="right" nowrap>
	<button class="btn btn-danger" id="del_{rand}" click="del" disabled type="button"><i class="icon-trash"></i> 删除</button> &nbsp; 
			<button class="btn btn-info" id="edit_{rand}" click="addwin,1" disabled type="button"><i class="icon-edit"></i> 编辑 </button> &nbsp; 
	<button class="btn btn-info" click="faxiaox" disabled id="faxis_{rand}" type="button">发送消息</button>
</td>
</tr>
</table>
</div>
<div class="blank10"></div>
<div id="view_{rand}"></div>
<div class="tishi">首次使用步骤：1、导入官网应用，2、到企业微信后台添加应用，并设置接收消息，3、选中应用编辑输入应用ID和secret管理密钥，4、操作拦点[获取信息]，在点[更新]，5、菜单拦点[更新]。6、这样一个应用就创建好。</div>
