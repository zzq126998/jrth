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

        $("#credentialsType").val(val1 +"-"+ val2+"-"+ val3+"-"+ val4+"-"+ val5);
    });


}
