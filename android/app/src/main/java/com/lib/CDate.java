package com.lib;


import java.util.Calendar;






/**
 * 说明：Android基础使用
 * 创建：chenxihu
 * 时间：2014-02-10
 * */


public final class CDate{

	public static int SPLITTIME = 0;//跟服务器时间差

	public static String now(String type,String dt, long gtime)
	{
		Calendar c1=Calendar.getInstance();
		if(gtime ==0 )gtime = System.currentTimeMillis();
		if(Rock.equals(type,"server")){
			gtime = gtime - SPLITTIME*1000;
			type  = "Y-m-d H:i:s";
		}
		c1.setTimeInMillis(gtime);
		int Y=2014,m=1,d=1,H=0,i=0,s=0,w=0;
		String weeknday[]= {"日","一","二","三","四","五","六"};
		String W = "";
		if(!Rock.isEmpt(dt)){
			String[] a1,a2,a3;
			a1	= dt.split(" ");
			a2	= a1[0].split("-");
			Y	= Integer.parseInt(a2[0]);
			m	= Integer.parseInt(a2[1]);
			d	= Integer.parseInt(a2[2]);
			if(a1.length==2){
				a3	= a1[1].split(":");
				H	= Integer.parseInt(a3[0]);
				i	= Integer.parseInt(a3[1]);
				s	= Integer.parseInt(a3[2]);
			}
			c1.set(Calendar.YEAR,Y);
			c1.set(Calendar.MONTH,m-1);
			c1.set(Calendar.DATE,d);
			c1.set(Calendar.HOUR_OF_DAY,H);
			c1.set(Calendar.MINUTE,i);
			c1.set(Calendar.SECOND,s);
			c1.set(Calendar.MILLISECOND,0);
		}else{
			Y	= c1.get(Calendar.YEAR);
			m 	= c1.get(Calendar.MONTH)+1;
			d	= c1.get(Calendar.DATE);
			H	= c1.get(Calendar.HOUR_OF_DAY);
			i	= c1.get(Calendar.MINUTE);
			s	= c1.get(Calendar.SECOND);
		}	
	    long time= c1.getTimeInMillis();
		w		= c1.get(Calendar.DAY_OF_WEEK)-1;
		W		= weeknday[w];
		type	= type.replaceAll("time", ""+time+"");
		type	= type.replaceAll("Y", ""+Y+"");
		type	= type.replaceAll("W", ""+W+"");
		type	= type.replaceAll("w", ""+w+"");
		type	= type.replaceAll("m", ""+sa(m)+"");
		type	= type.replaceAll("d", ""+sa(d)+"");
		type	= type.replaceAll("H", ""+sa(H)+"");
		type	= type.replaceAll("i", ""+sa(i)+"");
		type	= type.replaceAll("s", ""+sa(s)+"");		
		return type;
	}
	public static String now(){return now("Y-m-d H:i:s","",0);}
	public static String now(String type,String dt){return now(type,dt,0);}
	public static String date(){return now("Y-m-d","",0);}
	public static String time(){return now("H:i:s","",0);}
	public static String gettime(){return now("time","",0);}

	//服务器时间
	public static String getserverdt(){return now("server","",0);}


	/**
	 * 事件添加
	 * */
	public static String adddate(String lx,int oi,String dt,String type){
		String str	= "",a1 = "";
		a1	= now("time",dt,0);
		int jo = 0;
		if(lx=="d"){
			jo=24*3600*1000*oi;
		}else if(lx=="w"){
			jo=24*3600*1000*oi*7;
		}else if(lx=="H"){
			jo=3600*1000*oi;
		}else if(lx=="i"){
			jo=60*1000*oi;
		}else if(lx=="s"){
			jo=1000*oi;
		}
		Long tic = Long.parseLong(a1)+jo;
		str		 = now(type,"",tic);
		return str;
	}
	public static String adddate(String lx,int oi,String dt){
		return adddate(lx,oi,dt,"Y-m-d");
	}
	
	
	
	
	//两个时间间隔
	public static int datediff(String lx,String dt1, String dt2){
		long d1 = Long.parseLong(now("time",dt1));
		long d2	= Long.parseLong(now("time",dt2));
		long jg = (d2-d1);
		long xiaoshi = 0, ag = 0;
		xiaoshi	=jg/1000/3600;
		if(lx.equals("s")){
			ag	= jg/1000;
		}
		if(lx.equals("i")){
			ag	= jg/1000/60;
		}
		if(lx.equals("H")){
			ag	= xiaoshi;
		}
		if(lx.equals("d")){
			ag = (long) Math.floor(xiaoshi/24);	
		}
		return (int) ag;
	}	
	
	//倒计时返回天，时，分，秒
	public static String countdown(String lx,String dt1, String dt2){
		long d1 = Long.parseLong(now("time",dt1));
		long d2	= Long.parseLong(now("time",dt2));
		long jg = (d2-d1);
		if(jg<0)return "";
		long xiaoshi,t,s,f,m;
		xiaoshi	= jg/1000/3600;				//总小时
		t 	= (long) Math.floor(xiaoshi/24);//天
		xiaoshi=jg-24*3600*1000*t;
		
		s	= (long) Math.floor(xiaoshi/1000/3600);
		xiaoshi=xiaoshi-s*3600*1000;
		
		f	= (long) Math.floor(xiaoshi/1000/60);
		xiaoshi=xiaoshi-f*1000*60;
		
		m	= (long) Math.floor(xiaoshi/1000);
		String str = lx;
		str	= str.replaceAll("d", ""+t+"");
		str	= str.replaceAll("H", ""+sa((int)s)+"");
		str	= str.replaceAll("i", ""+sa((int)f)+"");
		str	= str.replaceAll("s", ""+sa((int)m)+"");
		return str;
	}
	
	public static String sa(int time){ 
        String str = "" + time; 
        if(str.length() == 1){ 
            str = "0" + str; 
        } 
        return str; 
    }

	public static String sa(String time){
		return sa(Integer.parseInt(time));
	}

	//毫秒转秒
	public static String long2String(long time){
		int sec = (int) time / 1000 ;
		int min = sec / 60 ;	//分钟
		sec = sec % 60 ;		//秒
		if(min < 10){	//分钟补0
			if(sec < 10){	//秒补0
				return "0"+min+":0"+sec;
			}else{
				return "0"+min+":"+sec;
			}
		}else{
			if(sec < 10){	//秒补0
				return min+":0"+sec;
			}else{
				return min+":"+sec;
			}
		}
	}
}