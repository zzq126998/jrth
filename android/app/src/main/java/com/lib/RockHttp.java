/**
 * 说明：http请求
 * 创建：雨中磐石  from www.rili123.cn
 * 时间：2014-11-28
 * 邮箱：qqqq2900@126.com
 * QQ：290802026/1073744729
 * */

package com.lib;



import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public final class RockHttp{
	
	public static String ResultString;
	public static int ResultCode;
	
	private static boolean bool = false;
	private static String suburl = "";
	private static Handler sHandler;
	private static List <BasicNameValuePair> sendparams;

	public static boolean isbool()
	{
		return bool;
	}
	
	private static void sendPost(String url, int gcode, Handler shandler)
	{
		HttpPost mHttpPost = new HttpPost(url);
		suburl			= url;
		mHttpPost.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
		mHttpPost.addHeader(HTTP.USER_AGENT, getuseragent());
		ResultString	= "";//返回的结果
		int backcode	= 0;
		try {
			mHttpPost.setEntity(new UrlEncodedFormEntity(sendparams,HTTP.UTF_8));
			HttpClient client 			= new DefaultHttpClient();
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, A.HTTPG_CONNTIMEOUT);// 请求超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, A.HTTPG_SOTIMEOUT);// 读取超时
			HttpResponse httpResponse	= client.execute(mHttpPost);
			backcode 	= httpResponse.getStatusLine().getStatusCode();//返回状态
			if(backcode == HttpStatus.SC_OK){
				ResultString = EntityUtils.toString(httpResponse.getEntity());
				backcode = A.HTTPB_SUCCESS;
			}else{
				ResultString = "Error:"+httpResponse.getStatusLine().toString();
				backcode = A.HTTPB_ERROR;
			}
		}catch (ConnectTimeoutException e) {
			ResultString = "请求超时";	
			backcode = A.HTTPB_TIMEOUT;
		}catch(Exception e){
			e.printStackTrace();
			ResultString = "无法访问网络";
			backcode = A.HTTPB_NOTWEN;
		}
		ResultCode	= gcode;
		if(shandler != null){
			Message message = new Message();
			message.what = gcode;
			Bundle mBundle = new Bundle();
			mBundle.putString("result", ResultString);
			message.setData(mBundle);
			message.arg1 = backcode;
			message.arg2 = A.HTTPB_BACKCODE;
			shandler.sendMessage(message);
		}
		bool	= false;
	}
	
	public static boolean post(final String url, final Handler ssHandler, List<String[]> params, final int getcode)
	{
		suburl 	 	= url;
		sHandler 	= ssHandler;
		sendparams	= new ArrayList<BasicNameValuePair>();
		sendparams.add(new BasicNameValuePair("apppage", A.APPPAGE));
		for(int i=0; i<params.size(); i++){
			String[] paramsss = params.get(i);
			sendparams.add(new BasicNameValuePair(paramsss[0], paramsss[1]));
		}
		bool		= true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				sendPost(url, getcode, ssHandler);
			}
		}).start();
		return true;
	}

	public static boolean post(String url, Handler ssHandler, List<String[]> params)
	{
		return post(url, ssHandler, params, 0);
	}

	private static String getuseragent()
	{
		return String.format("%s/%s (Linux; Android %s; Brand %s/Model %s)", "XINHUAPP", A.VERSION,
				Build.VERSION.RELEASE,Build.BRAND.toUpperCase(),Build.MODEL.toUpperCase());
	}
	
	private static void sendget(String url, int gcode, Handler shandler)
	{
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader(HTTP.USER_AGENT, getuseragent());
		suburl			= url;
		ResultString	= "";
		int backcode	= 0;
		try {
			HttpClient client 			= new DefaultHttpClient();
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, A.HTTPG_CONNTIMEOUT);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, A.HTTPG_SOTIMEOUT);
			HttpResponse httpResponse 			= client.execute(httpGet);
			backcode 	= httpResponse.getStatusLine().getStatusCode();//返回状态
			if(backcode == HttpStatus.SC_OK){
				ResultString = EntityUtils.toString(httpResponse.getEntity());
				backcode = A.HTTPB_SUCCESS;
			}else{
				ResultString = "Error:"+httpResponse.getStatusLine().toString();
				backcode = A.HTTPB_ERROR;
			}
		}catch (ConnectTimeoutException e) {
			ResultString = "请求超时";	
			backcode = A.HTTPB_TIMEOUT;
		}catch(Exception e){
			e.printStackTrace();
			ResultString = "无法访问网络";
			backcode = A.HTTPB_NOTWEN;
		}
		ResultCode	= gcode;
		if(shandler != null) {
			Message message = new Message();
			message.what = gcode;
			Bundle mBundle = new Bundle();
			mBundle.putString("result", ResultString);
			message.setData(mBundle);
			message.arg1 = backcode;
			message.arg2 = A.HTTPB_BACKCODE;
			shandler.sendMessage(message);
		}
		bool	= false;
	}
	
	public static boolean get(final String url, final Handler ssHandler, final int getcode)
	{
		suburl 	 	= url;
		sHandler 	= ssHandler;
		bool		= true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				sendget(url, getcode, ssHandler);
			}
		}).start();
		return true;
	}
	public static boolean get(String url, Handler ssHandler)
	{
		return get(url, ssHandler);
	}

	//获取当前请求URl
	public static String getnowurl()
	{
		return suburl;
	}

	/**
	 * 上传文件到服务器
	 * */
	public static boolean upload(final String url, final String fpath, final Handler ssHandler, final int getcode)
	{
		suburl 	 	= url;
		sHandler 	= ssHandler;
		new Thread(new Runnable() {
			@Override
			public void run() {
				uploadFile(fpath,url, getcode, ssHandler);
			}
		}).start();
		return true;
	}

	private static void uploadFile(String fpath, String RequestURL, int gcode, Handler shandler){
		ResultString	= "";
		File file 		= new File(fpath);
		if(!file.exists())ResultString = "["+fpath+"]文件不存在";
		long totalSize = file.length(),writesize=0;
		if(Rock.isEmpt(ResultString)){
			if(totalSize>A.MAXUPSIZE*1024*1024){
				ResultString = "文件超过"+A.MAXUPSIZE+"MB，当前文件大小"+RockFile.formatsize(totalSize)+"";
			}
		}

		String BOUNDARY	=  UUID.randomUUID().toString();  //边界标识   随机生成
		String PREFIX 	= "--" , LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data";   //内容类型
		String CHARSET 	= "utf-8";
		int backcode 	= 0;
		if(Rock.isEmpt(ResultString)){
			try {
				URL url = new URL(RequestURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(A.HTTPG_SOTIMEOUT);
				conn.setConnectTimeout(A.HTTPG_UPLOADTIMEOUT);
				conn.setDoInput(true);  //允许输入流
				conn.setDoOutput(true); //允许输出流
				conn.setUseCaches(false);  //不允许使用缓存
				conn.setRequestMethod("POST");  //请求方式
				conn.setRequestProperty("Charset", CHARSET);  //设置编码
				conn.setRequestProperty("connection", "keep-alive");
				conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
				conn.connect();
				int bufferSize		= 4*1024; //每次写入

				//当文件不为空，把文件包装并且上传
				DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);

				//设置上传name，和文件名
				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""+file.getName()+"\""+LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is 	= new FileInputStream(file);
				byte[] bytes 	= new byte[bufferSize];
				int len = 0,bili;

				//写入进度条
				while((len=is.read(bytes))!=-1){
					dos.write(bytes, 0, len);
					writesize+=bufferSize;
					if(writesize>totalSize)writesize=totalSize;

					bili 	= Integer.parseInt((writesize*100/totalSize)+"");
					//发送进度条状态
					String str = "{\"code\":200,\"data\":{\"total\":\""+totalSize+"\",\"write\":\""+writesize+"\",\"bili\":\""+bili+"\"}}";
					if(shandler != null){
						Message message = new Message();
						message.what = gcode+1;
						Bundle mBundle = new Bundle();
						mBundle.putString("result", str);
						message.setData(mBundle);
						message.arg1 = A.HTTPB_SUCCESS;
						message.arg2 = A.HTTPB_BACKCODE;
						shandler.sendMessage(message);
					}
					Thread.sleep(10);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码  200=成功
				 * 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				if(res==200){
					InputStream input =  conn.getInputStream();
					StringBuffer sb1= new StringBuffer();
					int ss ;
					while((ss=input.read())!=-1){
						sb1.append((char)ss);
					}
					ResultString 	= sb1.toString();
					backcode 		= A.HTTPB_SUCCESS;
				}else{
					ResultString 	= "有错误";
					backcode 		= A.HTTPB_ERROR;
				}
			}catch (ConnectTimeoutException e) {
				ResultString = "请求超时";
				backcode = A.HTTPB_TIMEOUT;
			}catch(Exception e){
				ResultString = "错误:"+e.getMessage()+"";
				e.printStackTrace();
				backcode = A.HTTPB_NOTWEN;
			}
		}
		ResultCode	= gcode;
		if(shandler != null) {
			Message message = new Message();
			message.what = gcode;
			Bundle mBundle = new Bundle();
			mBundle.putString("result", ResultString);
			message.setData(mBundle);
			message.arg1 = backcode;
			message.arg2 = A.HTTPB_BACKCODE;
			shandler.sendMessage(message);
		}
	}

}