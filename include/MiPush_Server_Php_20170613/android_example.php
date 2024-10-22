<?php
use xmpush\Builder;
use xmpush\HttpBase;
use xmpush\Sender;
use xmpush\Constants;
use xmpush\Stats;
use xmpush\Tracer;
use xmpush\Feedback;
use xmpush\DevTools;
use xmpush\Subscription;
use xmpush\TargetedMessage;

include_once(dirname(__FILE__) . '/autoload.php');

$secret = 'iMCZE90XEte7KFnmXzTi8g==';
$package = 'com.rock.xinhuapk';

// 常量设置必须在new Sender()方法之前调用
Constants::setPackage($package);
Constants::setSecret($secret);

$aliasList = array('faxc0xur');
$title = '你好';
$desc = '这是一条mipush推送消息';

$sarr	= array(
	'gname'		=> "通知公告",
	'optdt'		=> "2017-08-29 00:00:00",
	'type'		=> 'agent',
	'pushtype'	=> 'agent',
	'title'		=> "这是一个通知",
	'gid'		=> 1,
	'cont'		=> "嗯嗯嗯额",
	'url'		=> ""
);

$payload = '{"gname":"通知公告","title":"通知'.time().'","url":"","cont":"5aSn5a625pyJ5ZWl6Zeu6aKY6L!Z6YeM6K!05ZWK77yB","gname":"通知公告","type":"agent","pushtype":"agent"}';

$sender = new Sender();

// message1 演示自定义的点击行为
$message1 = new Builder();
$message1->title($title);  // 通知栏的title
$message1->description($desc); // 通知栏的descption
$message1->passThrough(0);  // 这是一条通知栏消息，如果需要透传，把这个参数设置成1,同时去掉title和descption两个参数
$message1->payload($payload); // 携带的数据，点击后将会通过客户端的receiver中的onReceiveMessage方法传入。
$message1->extra(Builder::notifyForeground, 0); // 应用在前台是否展示通知，如果不希望应用在前台时候弹出通知，则设置这个参数为0

$message1->extra(Builder::notifyEffect, Constants::NOTIFY_ACTIVITY);// 此处设置预定义点击行为，1为打开app
//$message1->extra(Builder::notifyEffect, 'intent:#Intent;component='.$package.'/.IndexActivity;end');// 此处设置预定义点击行为
$message1->extra(Builder::intentUri, 'intent:#Intent;component='.$package.'/.IndexActivity;end');// 此处设置预定义点击行为

$message1->extra(Builder::soundUri, 'android.resource://'.$package.'/raw/todo');// 声音地址

$message1->notifyId(2); // 通知类型。最多支持0-4 5个取值范围，同样的类型的通知会互相覆盖，不同类型可以在通知栏并存
$message1->notifyType(1); //-1 所有 1只声音，2使用默认震动提示 4 使用默认led灯光提示
$message1->build();
/*
$targetMessage = new TargetedMessage();
$targetMessage->setTarget('alias1', TargetedMessage::TARGET_TYPE_ALIAS); // 设置发送目标。可通过regID,alias和topic三种方式发送
$targetMessage->setMessage($message1);*/

// message2 演示预定义点击行为中的点击直接打开app行为
/*
$message2 = new Builder();
$message2->title($title);
$message2->description($desc);
$message2->passThrough(0);
$message2->payload($payload); // 对于预定义点击行为，payload会通过点击进入的界面的intent中的extra字段获取，而不会调用到onReceiveMessage方法。
$message2->extra(Builder::notifyEffect, 1); // 此处设置预定义点击行为，1为打开app
$message2->extra(Builder::notifyForeground, 1);
$message2->notifyId(0);
$message2->build();
$targetMessage2 = new TargetedMessage();
$targetMessage2->setTarget('alias2', TargetedMessage::TARGET_TYPE_ALIAS);
$targetMessage2->setMessage($message2);

$targetMessageList = array($targetMessage, $targetMessage2);
//print_r($sender->multiSend($targetMessageList,TargetedMessage::TARGET_TYPE_ALIAS)->getRaw());
*/
print_r($sender->sendToAliases($message1, $aliasList)->getRaw());
//$stats = new Stats();
//$startDate = '20140301';
//$endDate = '20140312';
//print_r($stats->getStats($startDate,$endDate)->getData());
//$tracer = new Tracer();
//print_r($tracer->getMessageStatusById('t1000270409640393266xW')->getRaw());

?>
