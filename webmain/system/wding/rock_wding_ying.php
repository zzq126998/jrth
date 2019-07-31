<?php if(!defined('HOST'))die('not access');?>
<script >
$(document).ready(function(){
	var a = $('#view_{rand}').bootstable({
		tablename:'zding_agent',sort:'sort',dir:'asc',celleditor:true,
		
		columns:[{
			text:'名称',dataIndex:'name',editor:true
		},{
			text:'应用ID',dataIndex:'agentid',editor:true	
		},{
			text:'排序号',dataIndex:'sort',editor:true	
		},{
			text:'是否默认',dataIndex:'ismr',editor:true,type:'checkbox'
		},{
			text:'应用提醒颜色',dataIndex:'color',editor:true
		},{
			text:'空说明，不需要设置任何内容',dataIndex:'description',editor:true
		},{
			text:'ID',dataIndex:'id'
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
		faxiaox:function(){
			var d=a.changedata;
			js.prompt('向应用['+d.name+']发送消息','消息内容',function(lx,txt){
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
		addwin:function(o,lx){
			var h = $.bootsform({
				title:'钉钉微应用',height:400,width:400,
				tablename:'zding_agent',isedit:lx,
				url:js.getajaxurl('publicsave','{mode}','{dir}'),
				submitfields:'name,agentid,sort,ismr,color',
				items:[{
					labelText:'名称',name:'name',required:true,repEmpty:true
				},{
					labelText:'应用ID',name:'agentid',required:true,repEmpty:true
				},{
					labelText:'应用提醒颜色',name:'color',repEmpty:true
				},{
					labelText:'序号',name:'sort',type:'number',value:'0'
				},{
					name:'ismr',labelBox:'是否默认',type:'checkbox',checked:false
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
		del:function(){
			a.del({url:js.getajaxurl('delying','{mode}','{dir}')});
		}
	};
	upsse{rand}=function(id, lx){
		if(lx==0)c.getagent(id);
		if(lx==1)c.updateagent(id);
	}
	js.initbtn(c);
});
</script>
<div>
<table width="100%">
<tr>
<td><button class="btn btn-primary" click="addwin,0" type="button">新增应用</button></td>
<td width="90%" style="padding-left:10px">创建应用需在<a href="https://oa.dingtalk.com/" target="_blank">[钉钉管理后台]</a>创建应用后在来这里添加</td>
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
<div class="tishi">这里应用主要是用来发消息提醒使用，是否默认：当找不到发给哪个应用消息时就会发给默认的应用,应用提醒颜色：必须是6个字符的RGB格式，如FF6600</div>
