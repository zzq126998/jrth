<?php
/**
*	来自：信呼开发团队
*	作者：磐石(rainrock)
*	网址：http://www.rockoa.com/
*	系统文件
*/
class dingding_chatClassModel extends dingdingModel { public function send($toid='') { $body = '{"sender":"manager4962","cid":"manager4962","msgtype":"text","text":{"content":"测试"}}'; $token = $this->gettoken(); $url = ''.$this->gettourl('URL_chatsend').'?access_token='.$token.''; $result = c('curl')->postjson($url, $body); $barr = $this->backarr; if($result!=''){ $arr = json_decode($result); $barr['errcode'] = $arr->errcode; $barr['msg'] = $arr->errmsg; } return $barr; } public function sendyibu() { $url = 'https://eco.taobao.com/router/rest'; $token = $this->gettoken(); $arr['method'] = 'dingtalk.corp.message.corpconversation.asyncsend'; $arr['session'] = $token; $arr['timestamp'] = $this->rock->now; $arr['format'] = 'json'; $arr['v'] = '2.0'; $arr['msgtype'] = 'text'; $arr['agent_id'] = '105648030'; $arr['userid_list'] = 'manager4962'; $arr['msgcontent'] = '{"content":"测试\n时间：'.$this->rock->now.'"}'; $result = c('curl')->postcurl($url, $arr); echo $result; } }