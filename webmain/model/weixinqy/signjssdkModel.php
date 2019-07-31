<?php
/**
*	来自：信呼开发团队
*	作者：磐石(rainrock)
*	网址：http://www.rockoa.com/
*	系统文件
*/
class weixinqy_signjssdkClassModel extends weixinqyModel { private function createNonceStr($length = 16) { $chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; $str = ""; for ($i = 0; $i < $length; $i++) { $str .= substr($chars, mt_rand(0, strlen($chars) - 1), 1); } return $str; } public function getsignsdk($url,$agid) { $this->readwxset(); $jsapiTicket = $this->getticket($agid); $timestamp = time(); $nonceStr = $this->createNonceStr(); $string = "jsapi_ticket=$jsapiTicket&noncestr=$nonceStr&timestamp=$timestamp&url=$url"; $signature = sha1($string); $signPackage = array( "appId" => $this->corpid, "nonceStr" => $nonceStr, "timestamp" => $timestamp, "url" => $url, "signature" => $signature, "jsapiTicket" => $jsapiTicket, "rawString" => $string ); return $signPackage; } }