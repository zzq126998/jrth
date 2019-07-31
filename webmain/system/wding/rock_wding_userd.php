<?php if(!defined('HOST'))die('not access');?>
<script >
$(document).ready(function(){
	var a = $('#view_{rand}').bootstable({
		tablename:'zding_user',sort:'`order`',dir:'asc',celleditor:true,fanye:true,statuschange:false,
		url:publicstore('{mode}','{dir}'),storebeforeaction:'beforeuserdshow',storeafteraction:'aftereuserdshow',
		columns:[{
			text:'头像',dataIndex:'avatar',renderer:function(v){
				if(isempt(v))v='images/noface.png';
				return '<img src="'+v+'" height="24" width="24">';
			}
		},{
			text:'用户名',dataIndex:'userid'
		},{
			text:'姓名',dataIndex:'name'
		},{
			text:'部门',dataIndex:'department'
		},{
			text:'职位',dataIndex:'position'
		},{
			text:'启用',dataIndex:'enable',type:'checkbox'
		},{
			text:'手机号',dataIndex:'mobile',renderer:function(v){
				if(ISDEMO)v='&nbsp;';
				return v;
			}
		},{
			text:'电话/分机号',dataIndex:'tel'
		},{
			text:'邮箱',dataIndex:'email'
		},{
			text:'排序号',dataIndex:'order'
		},{
			text:'ID',dataIndex:'id'	
		}],
		itemclick:function(){
			get('delbtn{rand}').disabled=false;
		},
		beforeload:function(){
			get('delbtn{rand}').disabled=true;
		}
	});
	
	var a1 = $('#view1_{rand}').bootstable({
		url:js.getajaxurl('deptwxdata','{mode}','{dir}'),
		tree:true,tablename:'zding_dept',
		columns:[{
			text:'名称',dataIndex:'name',align:'left'
		},{
			text:'',dataIndex:'idated',renderer:function(v,d){
				return '<a href="javascript:;" onclick="huoqud{rand}('+d.id+')">获取</a>';
			}
		}],
		itemdblclick:function(d){
			a.setparams({deptid:d.id},true);
		}
	});
	
	huoqud{rand}=function(id){
		c.getlist(id);
	}
	
	var c = {
		search:function(){
			var s=get('key_{rand}').value;
			a.setparams({key:s},true);
		},
		getlist:function(id){
			js.msg('wait','获取中...');
			js.ajax(js.getajaxurl('reloaduser','{mode}', '{dir}'),{id:id}, function(d){
				if(d.errcode==0){
					js.msg('success', '成功获取'+d.count+'记录');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		delaluser:function(){
			a.del({url:js.getajaxurl('delduser','{mode}','{dir}')});
		},
		getdduser:function(){
			var d = a1.getData(),zong=d.length,i,dar=[];
			for(i=0;i<zong;i++)dar.push(js.getajaxurl('reloaduser','{mode}','{dir}',{id:d[i].id}))
			queue.addlist(dar, function(){
				a.reload();
			});
		}
	};
	upsse{rand}=function(id,cjid){
		c.create(id, cjid);
	}
	js.initbtn(c);
});
</script>
<div>



<table width="100%">
<tr valign="top">
	<td>
		<div><button class="btn btn-default" click="getdduser" type="button">获取钉钉上用户</button></div>
		<div class="blank10"></div>
		<div style="width:300px" class="panel panel-info">
			<div class="panel-heading"><h3 class="panel-title">钉钉上组织结构</h3></div>
			<div id="view1_{rand}"></div>
			
		</div>
	</td>
	<td width="10"><div style="width:10px;overflow:hidden"></div></td>
	<td width="100%">
		<table width="100%">
		<tr>
		<td>
			<div class="input-group" style="width:250px;">
				<input class="form-control" id="key_{rand}"   placeholder="姓名/职位/用户名/手机号/电话">
				<span class="input-group-btn">
					<button class="btn btn-default" click="search" type="button"><i class="icon-search"></i></button>
				</span>
			</div>
		</td>
		<td width="90%" style="padding-left:10px">
			
		</td>
		<td align="right" nowrap>
			<button class="btn btn-danger" disabled id="delbtn{rand}" click="delaluser" type="button">删除选中钉钉用户</button>
		</td>
		</tr>
		</table>
		<div class="blank10"></div>
		<div id="view_{rand}"></div>
		<div id="showmsg{rand}" class="tishi">本列表用户从用<font color=red>钉钉</font>上用户表zding_user表上读取的。</div>

	</td>
</tr>
</table>





</div>