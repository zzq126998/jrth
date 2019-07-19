<?php
/**
*	此文件是流程模块【jsuser.技术类人员】对应控制器接口文件。
*/ 
class mode_jsuserClassAction extends inputAction{
	
	/**
	*	重写函数：保存前处理，主要用于判断是否可以保存
	*	$table String 对应表名
	*	$arr Array 表单参数
	*	$id Int 对应表上记录Id 0添加时，大于0修改时
	*	$addbo Boolean 是否添加时
	*	return array('msg'=>'错误提示内容','rows'=> array()) 可返回空字符串，或者数组 rows 是可同时保存到数据库上数组
	*/
	protected function savebefore($table, $arr, $id, $addbo){
		
	}
	
	/**
	*	重写函数：保存后处理，主要保存其他表数据
	*	$table String 对应表名
	*	$arr Array 表单参数
	*	$id Int 对应表上记录Id
	*	$addbo Boolean 是否添加时
	*/	
	protected function saveafter($table, $arr, $id, $addbo){
		
	}
    public function flylimit(){
        $arr[]=array(
            "name"=>"121"
        );
        $arr[]=array(
            "name"=>"135"
        );
        $arr[]=array(
            "name"=>"91"
        );
        return $arr;
    }
    public function bflylimit(){
        $arr[]=array(
            "name"=>"91"
        );
        return $arr;
    }
    public function permittype(){
        $str="<span id=\"div_temp_dwid\" class=\"divinput\"><table width=\"98%\" cellpadding=\"0\" border=\"0\"><tbody><tr><td width=\"100%\"><input  class=\"inputs\" style=\"width:99%\" value=\"\" readonly=\"\" type=\"text\" name=\"permittype\"></td><td nowrap=\"\"><a href=\"javascript:;\"  class=\"webbtn\" data-toggle=\"modal\" data-target=\"#myModal\">选择</a></td></tr></tbody></table></span>";
//        $str="<span id=\"div_temp_dwid\" class=\"divinput\"><table width=\"98%\" cellpadding=\"0\" border=\"0\"><tbody><tr><td width=\"100%\"><input  class=\"inputs\" style=\"width:99%\" value=\"\" readonly=\"\" type=\"text\" name=\"permittype\"></td><td nowrap=\"\"><a href=\"javascript:;\"  class=\"webbtn\" id=\"permit\">选择</a></td></tr></tbody></table></span>";
        return $str;
    }
}
			