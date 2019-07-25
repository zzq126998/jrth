//流程模块【jsuser.技术类人员】下录入页面自定义js页面,初始函数

function initbodys(){

    $('#permit').click(function(){
        $('#myModal').show();
        $('#myModal').attr('class','modal fade in');
        $('#bjblock').attr('class','modal-backdrop fade in');
    });
    $('.close').click(function(){
        $('#myModal').hide();
        $('#myModal').attr('class','modal fade ');
        $('#bjblock').attr('class','');
    });
    $('.btn-default').click(function(){
        $('#myModal').hide();
        $('#myModal').attr('class','modal fade ');
        $('#bjblock').attr('class','');
    });
    // 提交
    $('.btn-primary').click(function(){
        var s1Val = $('#s1 option:selected').val();
        var s2Val = $('#s2 option:selected').val();
        var s3Val = $('#s3 option:selected').val();
        var s4Val = $('#s4 option:selected').val();
        var s5Val = $('#s5 option:selected').val();
        if(s1Val == "-请选择-" || s2Val == "-请选择-" || s3Val == "-请选择-" || s4Val == "-请选择-" || s5Val == "-请选择-"){
            console.log(51111);
            alert("请确认已选择完");
            return false;
        }
        $('#myModal').hide();
        $('#myModal').attr('class','modal fade ');
        $('#bjblock').attr('class','');
        var val1,val2,val3,val4,val5;
        if(s1Val == "暂无数据"){val1 = "";} else {val1 = s1Val;}
        if(s2Val == "暂无数据"){val2 = "";} else {val2 = s2Val;}
        if(s3Val == "暂无数据"){val3 = "";} else {val3 = s3Val;}
        if(s4Val == "暂无数据"){val4 = "";} else {val4 = s4Val;}
        if(s5Val == "暂无数据"){val5 = "";} else {val5 = s5Val;}

        if(val5 == ""){
            $("#credentialsType").val(val1 +"-"+ val2+"-"+ val3+"-"+ val4);
        }else{
            $("#credentialsType").val(val1 +"-"+ val2+"-"+ val3+"-"+ val4+"-"+ val5);
        }
    });
    $(form('age1_10')).change(function(res){
        var val = this.value;
        if(val=='')return;
    });
    // $('#AltS').click(function(){
    //    var val=form('age1_10').value
    //     if(val=='')return false;
    //     console.log(val);return
    // });

}
function changesubmit(d){
   // if(!d.permittype)return '请选择执照类型';
   if(!d.num0_4)return '执照编号不能为空';
   if(!d.name0_4)return '执照姓名不能为空';
   if(!d.nationality0_4)return '执照国籍不能为空';
   if(!d.physicalprove1_10)return '请选择合格证类型';
   if(d.physicalprove1_10==1){
       if(!d.anum1_10)return '一级体检编号为空';
       if(!d.aname1_10)return '一级姓名为空';
       if(!d.asex1_10)return '一级性别选项不能为空';
       if(!d.adate1_10)return '一级出生年月不能为空';
       if(!d.anationality1_10)return '一级国籍不能为空';
       if(!d.alimit1_10)return '一级限制选项不能为空';
       if(!d.adateissue1_10)return '一级发证日期项不能为空';
       if(!d.atermvalidity1_10)return '一级有效期不能为空';
   }
    if(d.physicalprove1_10==2){
        if(!d.bnum1_10)return '二级体检编号为空';
        if(!d.bname1_10)return '二级姓名为空';
        if(!d.bsex1_10)return '二级性别选项不能为空';
        if(!d.bdate1_10)return '二级出生年月不能为空';
        if(!d.bnationality1_10)return '二级国籍不能为空';
        if(!d.blimit1_10)return '二级限制选项不能为空';
        if(!d.bdateissue1_10)return '二级发证日期项不能为空';
        if(!d.btermvalidity1_10)return '二级有效期不能为空';
    }
}
//4、保存提交成功触发
function savesuccess(){
    alert('111111111');
    $('.modal-backdrop').attr('class','');
    $($('.modal-backdrop')[0]).remove();
    $(".modal-backdrop").hide();
};
