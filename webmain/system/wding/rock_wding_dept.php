<?php if(!defined('HOST'))die('not access');?>
<script >
$(document).ready(function(){	
	var a = $('#view1_{rand}').bootstable({
		url:js.getajaxurl('deptwxdata','{mode}','{dir}'),
		tree:true,celleditor:true,tablename:'zding_dept',
		columns:[{
			text:'名称',dataIndex:'name',align:'left'
		},{
			text:'上级id',dataIndex:'parentid'
		},{
			text:'排序号',dataIndex:'order'
		},{
			text:'状态',dataIndex:'zt',renderer:function(v,d){
				var s='&nbsp;';
				if(v==0)s='<font color="red">不存在</font><a href="javascript:;" onclick="delupd{rand}('+d.id+')">[删]</a>';
				return s;
			}
		},{
			text:'ID',dataIndex:'id'	
		},{
			text:'系统部门ID',dataIndex:'bmid',editor:true
		}]
	});

	var b = $('#view2_{rand}').bootstable({
		url:js.getajaxurl('deptdata','{mode}','{dir}'),
		tree:true,
		columns:[{
			text:'名称',dataIndex:'name',align:'left'
		},{
			text:'上级id',dataIndex:'pid'
		},{
			text:'负责人',dataIndex:'headman'
		},{
			text:'排序号',dataIndex:'sort'
		},{
			text:'状态',dataIndex:'zt',renderer:function(v,d){
				var s='<font color="red">未同步</font>';
				if(v>0)s='<font color="green">已同步</font>';
				s+='<a href="javascript:;" onclick="upsse{rand}('+d.id+')">[更新]</a>';
				return s;
			}
		},{
			text:'ID',dataIndex:'id'	
		}]
	});
	var c = {
		relad:function(){
			a.reload();
			b.reload();
		},
		updatedept:function(){
			js.msg('wait','获取中...');
			js.ajax(js.getajaxurl('updatedept','{mode}', '{dir}'),{}, function(d){
				if(d.errcode==0){
					js.msg('success', '获取成功');
					a.reload();
					b.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		gengxin:function(id){
			js.msg('wait','同步中...');
			js.ajax(js.getajaxurl('optdept','{mode}', '{dir}'),{ids:id},function(d){
				if(d.errcode==0){
					js.msg('success', '同步成功');
					a.reload();
					b.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		delfe:function(id){
			js.msg('wait','删除中...');
			js.ajax(js.getajaxurl('deletedept','{mode}', '{dir}'),{ids:id},function(d){
				if(d.errcode==0){
					js.msg('success', '删除成功');
					a.reload();
					b.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		anaytodept:function(){
			js.confirm('确定要将将钉钉上组织结构同步到系统上吗？同步了，将覆盖系统原来创建的哦。',function(jg){
				if(jg=='yes')c.anaytodepts();
			});
		},
		anaytodepts:function(){
			js.msg('wait','同步中...');
			js.ajax(js.getajaxurl('anaytodept','{mode}', '{dir}'),{},function(d){
				if(d.errcode==0){
					js.msg('success', '同步完成成功');
					a.reload();
					b.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		}
	}
	upsse{rand}=function(id){
		c.gengxin(id);
	}
	delupd{rand}=function(id){
		c.delfe(id);
	}
	js.initbtn(c);
});
</script>



<table width="100%">
<tr valign="top">
	<td width="50%">
		<div>
			<button class="btn btn-default" click="updatedept" type="button">重新获取列表</button>&nbsp;&nbsp;
			<button class="btn btn-default" click="anaytodept" type="button">将钉钉上组织结构同步到系统上</button>
		</div>
		<div class="blank10"></div>
		<div class="panel panel-info">
			<div class="panel-heading"><h3 class="panel-title">钉钉上组织结构</h3></div>
			<div id="view1_{rand}"></div>
			
		</div>
	</td>
	<td width="10"><div style="width:10px;overflow:hidden"></div></td>
	<td width="50%">
		<div><button class="btn btn-default" click="relad,0" type="button">刷新</button></div>
		<div class="blank10"></div>
		<div class="panel panel-success">
			<div class="panel-heading"><h3 class="panel-title">系统上组织结构</h3></div>
			
			<div id="view2_{rand}"></div>
		</div>
	</td>
</tr>
</table>
<div class="tishi">数据是需要与系统上的一致的，钉钉上组织结构上需要手动设置对应信呼系统上部门ID</div>