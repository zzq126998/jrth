
var s=["s1","s2","s3","s4","s5"];
var opt0 = ["-请选择-","-请选择-","-请选择-","-请选择-","-请选择-"];
window.onload=function setup()
{
  for(i=0;i<s.length-1;i++)
    document.getElementById(s[i]).onchange=new Function("change("+(i+1)+")");
  change(0);
}

function Dsy()
{
  this.Items = {};
}
Dsy.prototype.add = function(id,iArray)
{
  this.Items[id] = iArray;
}
Dsy.prototype.Exists = function(id)
{
  if(typeof(this.Items[id]) == "undefined")
    return false;
  return true;
}
function change(v)
{
  var str="0";
  for(i=0;i<v;i++)
  {
    str+=("_"+(document.getElementById(s[i]).selectedIndex-1));
  };
  var ss=document.getElementById(s[v]);
  with(ss)
  {
    length = 0;
    options[0]=new Option(opt0[v],opt0[v]);
    if(v && document.getElementById(s[v-1]).selectedIndex>0 || !v)
    {
      if(dsy.Exists(str))
      {
        ar = dsy.Items[str];
        for(i=0;i<ar.length;i++)options[length]=new Option(ar[i],ar[i]);
        if(v)options[1].selected = true;
      }
    }
    if(++v<s.length)
    {
      change(v);
    }
  }
}
var dsy = new Dsy();
dsy.add("0",["商用驾驶员","私用驾驶员","运动驾驶员","模拟驾驶员","航线运输驾驶员"]);
dsy.add("0_0",["直升机","固定翼"]);
dsy.add("0_0_0",["等级"]);
dsy.add("0_0_0_0",["单发","双发","教员","仪表"]);
dsy.add("0_0_0_0_0",["水上单发","陆地单发"]);
dsy.add("0_0_0_0_1",["暂无数据"]);
dsy.add("0_0_0_0_3",["暂无数据"]);
dsy.add("0_0_0_0_2",["基础教员","仪表教员","型别教员"]);

dsy.add("0_0_1",["等级"]);

dsy.add("0_0_1_0",["单发","双发","教员","仪表"]);
dsy.add("0_0_1_0_2",["基础教员","仪表教员","型别教员"]);
dsy.add("0_0_1_0_0",["水上单发","陆地单发"]);
dsy.add("0_0_1_0_1",["暂无数据"]);
dsy.add("0_0_1_0_3",["暂无数据"]);

dsy.add("0_1",["直升机","固定翼"]);
dsy.add("0_1_0",["等级"]);
dsy.add("0_1_0_0",["双发","仪表","单发"]);
dsy.add("0_1_0_0_0",["暂无数据"]);
dsy.add("0_1_0_0_1",["暂无数据"]);
dsy.add("0_1_0_0_2",["陆地单发","水上单发"]);
dsy.add("0_1_1",["等级"]);
dsy.add("0_1_1_0",["双发","仪表","单发"]);
dsy.add("0_1_1_0_0",["暂无数据"]);
dsy.add("0_1_1_0_1",["暂无数据"]);
dsy.add("0_1_1_0_2",["陆地单发","水上单发"]);

dsy.add("0_2",["直升机","固定翼"]);
dsy.add("0_2_0",["等级"]);
dsy.add("0_2_0_0",["教员","水上单发","陆地单发"]);
dsy.add("0_2_0_0_0",["暂无数据"]);
dsy.add("0_2_0_0_1",["暂无数据"]);
dsy.add("0_2_0_0_2",["暂无数据"]);
dsy.add("0_2_1",["等级"]);
dsy.add("0_2_1_0",["教员","水上单发","陆地单发"]);
dsy.add("0_2_1_0_0",["暂无数据"]);
dsy.add("0_2_1_0_1",["暂无数据"]);
dsy.add("0_2_1_0_2",["暂无数据"]);


dsy.add("0_3",["直升机","固定翼"]);
dsy.add("0_3_0",["等级"]);
dsy.add("0_3_0_0",["单发","双发","教员","仪表"]);
dsy.add("0_3_0_0_0",["陆地单发","水上单发"]);
dsy.add("0_3_0_0_1",["暂无数据"]);
dsy.add("0_3_0_0_3",["暂无数据"]);
dsy.add("0_3_0_0_2",["基础教员","仪表教员","型别教员"]);
dsy.add("0_3_1",["等级"]);
dsy.add("0_3_1_0",["单发","双发","教员","仪表"]);
dsy.add("0_3_1_0_0",["陆地单发","水上单发"]);
dsy.add("0_3_1_0_1",["暂无数据"]);
dsy.add("0_3_1_0_3",["暂无数据"]);
dsy.add("0_3_1_0_2",["基础教员","仪表教员","型别教员"]);

dsy.add("0_4",["直升机","固定翼"]);
dsy.add("0_4_0",["等级"]);
dsy.add("0_4_0_0",["单发","双发","教员","仪表"]);
dsy.add("0_4_0_0_0",["陆地单发","水上单发"]);
dsy.add("0_4_0_0_1",["暂无数据"]);
dsy.add("0_4_0_0_3",["暂无数据"]);
dsy.add("0_4_0_0_2",["基础教员","仪表教员","型别教员"]);
dsy.add("0_4_1",["等级"]);
dsy.add("0_4_1_0",["单发","双发","教员","仪表"]);
dsy.add("0_4_1_0_0",["陆地单发","水上单发"]);
dsy.add("0_4_1_0_1",["暂无数据"]);
dsy.add("0_4_1_0_3",["暂无数据"]);
dsy.add("0_4_1_0_2",["基础教员","仪表教员","型别教员"]);

