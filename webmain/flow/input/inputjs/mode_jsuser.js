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
        $("#credentialsType").val(s1Val +"-"+ s2Val+"-"+ s3Val+"-"+ s4Val+"-"+ s5Val);
       console.log(156465)
    });


}
