<?php if(!defined('HOST'))die('not access');?>
<script >
$(document).ready(function(){
	var a = $('#view_{rand}').bootstable({
		tablename:'admin',sort:'sort',dir:'asc',celleditor:false,fanye:true,
		storebeforeaction:'beforeusershow',storeafteraction:'afterusershow',url:publicstore('{mode}','{dir}'),
		columns:[{
			text:'头像',dataIndex:'face',renderer:function(v){
				if(isempt(v))v='images/noface.png';
				return '<img src="'+v+'" height="24" width="24">';
			}
		},{
			text:'用户名',dataIndex:'user'
		},{
			text:'姓名',dataIndex:'name'
		},{
			text:'部门',dataIndex:'deptname'
		},{
			text:'职位',dataIndex:'ranking'
		},{
			text:'启用',dataIndex:'status',type:'checkbox',sortable:true,editor:true
		},{
			text:'关注状态',dataIndex:'zt',align:'left',renderer:function(v,d){
				var s='',zt=d.wxstatus;
				if(d.iscj==1){
					s='<a href="javascript:;" onclick="upsse{rand}('+d.id+','+d.cjid+')">[更新]</a>';
					if(d.isgc==0){
						if(d.isgz==1){
							s+='<font color=green>已关注</font>';
						}else{
							s+='未关注';
						}
					}else{
						s+='<font color=red>需更新</font>';
					}
				}else{
					s='<a href="javascript:;" style="color:red" onclick="upsse{rand}('+d.id+',0)">[创建]</a>';
				}
				
				return s;
			}
		},{
			text:'手机号',dataIndex:'mobile',editor:true
		},{
			text:'排序号',dataIndex:'sort'
		},{
			text:'ID',dataIndex:'id'	
		}],
		load:function(d){
			var s='创建钉钉上用户时，请先确保组织结构已同步更新了，获取钉钉上的用户只能一个部门一个部门的获取哦，本列表用户是从<font color=red>信呼系统</font>上用户表admin上读取的。';
			if(d.notstr!='')s='钉钉上系统有不存在的用户：<font color=red>'+d.notstr+'</font>，请确认后点按钮删除';
			$('#showmsg{rand}').html(s);
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
			a.setparams({deptid:d.bmid},true);
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
		create:function(id,cjid){
			js.msg('wait','处理中...');
			js.ajax(js.getajaxurl('optuser','{mode}', '{dir}'),{id:id,cjid:cjid}, function(d){
				if(d.errcode==0){
					js.msg('success', '更新成功');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		delaluser:function(){
			js.msg('wait','处理中...');
			js.ajax(js.getajaxurl('delalluser','{mode}', '{dir}'),{}, function(d){
				if(d.errcode==0){
					js.msg('success', '删除成功');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		anaytouser:function(){
			js.confirm('确定要将将钉钉上用户同步到系统上吗？同步了，将覆盖系统原来创建的哦。',function(jg){
				if(jg=='yes')c.anaytousers();
			});
		},
		anaytousers:function(){
			js.msg('wait','同步中...');
			js.ajax(js.getajaxurl('anaytouser','{mode}', '{dir}'),{},function(d){
				if(d.errcode==0){
					js.msg('success', '同步完成成功');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
		dinguser:function(){
			addtabs({name:'钉钉上用户列表','num':'dinguser',url:'system,wding,userd'});
		},
		getdduser:function(){
			var d = a1.getData(),zong=d.length,i,dar=[];
			for(i=0;i<zong;i++)dar.push(js.getajaxurl('reloaduser','{mode}','{dir}',{id:d[i].id}))
			queue.addlist(dar, function(){
				a.reload();
			});
		},
		anaytouser:function(){
			js.confirm('确定要将钉钉上用户同步到系统上吗？同步了，将覆盖系统原来创建的哦。',function(jg){
				if(jg=='yes')c.anaytousers();
			});
		},
		anaytousers:function(){
			js.msg('wait','同步中...');
			js.ajax(js.getajaxurl('anaytouser','{mode}', '{dir}'),{},function(d){
				if(d.errcode==0){
					js.msg('success', '同步完成成功');
					a.reload();
				}else{
					js.msg('msg', d.errcode+':'+d.msg);
				}
			},'get,json');
		},
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
		<div><button class="btn btn-default" click="anaytouser" type="button">将钉钉上用户同步到系统上</button></div>
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
				<input class="form-control" id="key_{rand}"   placeholder="姓名/部门/职位/用户名/手机号/电话">
				<span class="input-group-btn">
					<button class="btn btn-default" click="search" type="button"><i class="icon-search"></i></button>
				</span>
			</div>
		</td>
		<td width="90%" style="padding-left:10px">
			<button class="btn btn-default" click="dinguser" type="button">钉钉上用户</button>
		</td>
		<td align="right" nowrap>
			
			<button class="btn btn-danger" click="delaluser" type="button">删除钉钉上系统不存在的用户</button>
		</td>
		</tr>
		</table>
		<div class="blank10"></div>
		<div id="view_{rand}"></div>
		<div id="showmsg{rand}" class="tishi wrap"></div>

	</td>
</tr>
</table>





</div>