/**
 * QQ地图的定位
 * from http://xh829.com/
 * 来自信呼开发团队
 * */

package com.rock.xinhuapk;


import android.content.Context;


import com.lib.Rock;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;


import java.util.HashMap;
import java.util.Map;


public class QQLocation implements TencentLocationListener {

	private Context mContent;

	public TencentLocation mLocation; //最后得到定位信息
	private TencentLocationManager mLocationManager;
	private CallBack CallBacklocaton 	= null;


	public QQLocation(Context context, CallBack callback)
	{
		mContent = context;
		CallBacklocaton = callback;
	}

	/**
	 * 初始化开始定位
	 * */
	public void start() {
		mLocationManager = TencentLocationManager.getInstance(mContent);
		// 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
		mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);

		TencentLocationRequest request = TencentLocationRequest.create();
		request.setInterval(10000); //当定位周期大于0时, 不论是否有得到新的定位结果, 位置监听器都会按定位周期定时被回调; 当定位周期等于0时, 仅当有新的定位结果时, 位置监听器才会被回调(即, 回调时机存在不确定性). 如果需要周期性回调
		mLocationManager.requestLocationUpdates(request, this);
	}

	/**
	 * 停止定位
	 * */
	public void stop() {
		mLocationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(TencentLocation location, int error, String s) {
		if (TencentLocation.ERROR_OK == error) {
			mLocation = location;

			Map<String, String> a 	= new HashMap<String, String>();

			String address = location.getAddress();
			String accuracy = new StringBuilder().append(location.getAccuracy()).toString();//经度;
			String provider = new StringBuilder().append(location.getProvider()).toString();//来源;

			String lat = new StringBuilder().append(location.getLatitude()).toString(); //纬度
			String lng = new StringBuilder().append(location.getLongitude()).toString(); //经度

			a.put("address",address);
			a.put("accuracy",accuracy);
			a.put("provider",provider);
			a.put("latitude",lat);
			a.put("longitude",lng);

			if(CallBacklocaton!=null)CallBacklocaton.backlocation(a);
		} else {
			Rock.Toast(mContent, "定位失败");
		}
	}



	@Override
	public void onStatusUpdate(String s, int i, String s1) {

	}




}
