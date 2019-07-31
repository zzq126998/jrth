<?php 
include_once(ROOT_PATH.'/include/MiPush_Server_Php_20170613/autoload.php');

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

class xiaomiPushChajian extends Chajian{

	
	public function push($secret, $package, $xmalias, $title, $desc, $cont)
	{

		// 常量设置必须在new Sender()方法之前调用
		Constants::setPackage($package);
		Constants::setSecret($secret);

		$aliasList = $xmalias;
		
		if(!$aliasList)return false;
	
		$payload = $cont;

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

		return $sender->sendToAliases($message1, $aliasList)->getRaw();
	}
}