<?php
if(!defined('HOST'))die('not access');
//系统配置文件		
return array(
	'url'		=> '',		//系统URL
	'localurl'	=> '',			//本地系统URL，用于服务器上浏览地址
	'title'		=> '信呼协同办公系统',	//系统默认标题
	'apptitle'	=> '信呼OA',			//APP上或PC客户端上的标题
//	'db_host'	=> '182.61.51.112',		//数据库地址
	'db_host'	=> '127.0.0.1',		//数据库地址
	'db_user'	=> 'root',		//数据库用户名
	'db_pass'	=> 'root',		//数据库密码
	'db_base'	=> 'rockxinhu',		//数据库名称
	'db_engine'	=> 'MyISAM',	//数据库使用引擎
	'perfix'	=> 'xinhu_',	//数据库表名前缀
	'qom'		=> 'xinhu_',		//session、cookie前缀
	'highpass'	=> '',			//超级管理员密码，可用于登录任何帐号
	'db_drive'	=> 'mysqli',	//操作数据库驱动有mysql,mysqli,pdo三种
	'randkey'	=> 'jsklgxcpozrbaemdqftiyvhwun',		//系统随机字符串密钥
	'asynkey'	=> 'cb7837476a83f0ad145e523ca89f02f6',	//这是异步任务key
	'openkey'	=> '497e75ae4e4b2cb5d066cd965b581392',	//对外接口openkey
	'updir'		=> 'upload',	//默认上传目录
	'sqllog'	=> false,		//是否记录sql日志保存upload/sqllog下
	'asynsend'	=> false,		//是否异步发送提醒消息，为true需开启服务端
	'editpass'	=> '1',			//用户登录修改密码：0不用修改，1强制用户必须修改
	'install'	=> true			//已安装，不要去掉啊
);