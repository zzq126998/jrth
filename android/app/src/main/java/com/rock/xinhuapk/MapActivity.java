/**
 * 人员详细信息
 * from http://xh829.com/
 * 来自信呼开发团队
 * */

package com.rock.xinhuapk;



import android.view.View;
import android.widget.TextView;


import com.lib.Rock;
import com.lib.RockActivity;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.Circle;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.Map;


public class MapActivity extends RockActivity {


	private MapView mMapView;
	private TencentMap mTencentMap;
	private Marker mLocationMarker;
	private Circle mAccuracyCircle;

	private QQLocation qqLocation;



	/**
	 * 初始化
	 * */
	protected void initCreate() {
		setContentView(R.layout.activity_map);
		findViewById(R.id.back).setOnClickListener(this.OnViewClickListener);

		initMapView();

		qqLocation = new QQLocation(this, new CallBack(){
			public void backlocation(Map<String,String> a){
				backlocationb();
			}
		});
		qqLocation.start();
	}

	//定位完成回调
	private void backlocationb()
	{
		LatLng latLngLocation = new LatLng(qqLocation.mLocation.getLatitude(), qqLocation.mLocation.getLongitude());
		if (mLocationMarker == null) {
			mMapView.getMap().setCenter(latLngLocation);
			mMapView.getMap().setZoom(11);
		}
		if (mLocationMarker == null) {
			mLocationMarker =
					mTencentMap.addMarker(new MarkerOptions().
							position(latLngLocation).
							icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
		} else {
			mLocationMarker.setPosition(latLngLocation);
		}

		if (mAccuracyCircle == null) {
			mAccuracyCircle = mTencentMap.addCircle(new CircleOptions().
					center(latLngLocation).
					radius(qqLocation.mLocation.getAccuracy()).
					fillColor(0x884433ff).
					strokeColor(0xaa1122ee).
					strokeWidth(1));
		} else {
			mAccuracyCircle.setCenter(latLngLocation);
			mAccuracyCircle.setRadius(qqLocation.mLocation.getAccuracy());
		}
	}


	private void initMapView() {
		mMapView = (MapView) findViewById(R.id.mapview);
		mTencentMap = mMapView.getMap();
		mTencentMap.setZoom(9);
	}


	protected void ViewClick(View v) {
		int id = v.getId();
		if(id == R.id.back){
			finish();
		}
	}


	@Override
	protected void onStop() {
		super.onStop();
		qqLocation.stop();
	}
}
