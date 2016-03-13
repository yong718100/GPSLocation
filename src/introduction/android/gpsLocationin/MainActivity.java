package introduction.android.gpsLocationin;

import introduction.android.gpsLocation.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;   //
import android.location.LocationListener;
import android.location.LocationManager;  //
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private TextView mLocationListenerInfo, mNmeaListenerInfo, mNmeaListenerInfoGprmc, mNmeaListenerInfoFixGpgsa;
	
	private LocationManager mLocationManager;  
	
	public final static String TAG = "MainActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mLocationListenerInfo = (TextView) findViewById(R.id.location_listener_info);
        mNmeaListenerInfoGprmc = (TextView) findViewById(R.id.nmea_listener_info_gprmc);
        mNmeaListenerInfoFixGpgsa = (TextView) findViewById(R.id.nmea_listener_info_fix_gpgsa);
        mNmeaListenerInfo = (TextView) findViewById(R.id.nmea_listener_info);
        
        init_gps();
    }

	private void init_gps() {
		mLocationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //若未打开GPS
			Log.e(TAG, " need open  Gps...");
			Toast.makeText(MainActivity.this, "need open  Gps...", Toast.LENGTH_LONG).show();
			Intent myintent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    		startActivity(myintent);  //运行手机的设置程序
			return;
		}

		Log.i(TAG, "Gps.open...");
		
		// register NmeaListener
		mLocationManager.addNmeaListener(new NmeaListener() {

			@Override
			public void onNmeaReceived(long timestamp, String nmea) {
				// TODO Auto-generated method stub
				Log.i(TAG, "onNmeaReceived() nmea=" + nmea);
				mNmeaListenerInfo.setText("nmea: " + nmea + "\n");
				
				/*
				 * $GPRMC
					例：$GPRMC,024813.640,A,3158.4608,N,11848.3737,E,10.05,324.27,150706,,,A*50
					字段0：$GPRMC，语句ID，表明该语句为Recommended Minimum Specific GPS/TRANSIT Data（RMC）推荐最小定位信息
					字段1：UTC时间，hhmmss.sss格式
					字段2：状态，A=定位，V=未定位
					字段3：纬度ddmm.mmmm，度分格式（前导位数不足则补0）
					字段4：纬度N（北纬）或S（南纬）
					字段5：经度dddmm.mmmm，度分格式（前导位数不足则补0）
					字段6：经度E（东经）或W（西经）
					字段7：速度，节，Knots
					字段8：方位角，度
					字段9：UTC日期，DDMMYY格式
					字段10：磁偏角，（000 - 180）度（前导位数不足则补0）
					字段11：磁偏角方向，E=东W=西
					字段16：校验值
				 */
				if (nmea.startsWith("$GPRMC")) {
					String[] nmeaGPRMC = nmea.split(",", 0);
					if (nmeaGPRMC.length < 12) {
						Log.i(TAG, "onNmeaReceived() nmeaGPRMC.length<12, invalid data, so return!!!");
						return;
					}
					
					String nmeaListenerGpsData =  "$GPRMC nmea: " + nmea + "\n"
							+ "Longitude: " + nmeaGPRMC[5] + "\n"
							+ "Latitude: " + nmeaGPRMC[3] + "\n" 
							+ "Speed: " + nmeaGPRMC[7] + "\n" 
							+ "getBearing: " + nmeaGPRMC[8] + "\n";
					
					if (nmeaGPRMC[2].equals("A")) {
						nmeaListenerGpsData += "$GPRMC Fix: Fix" + "\n";
					} else {
						nmeaListenerGpsData += "$GPRMC Fix: No Fix" + "\n";
					}
					
					mNmeaListenerInfoGprmc.setText(nmeaListenerGpsData);
				} 
				/*
				 * $GPGSA
					例：$GPGSA,A,3,01,20,19,13,,,,,,,,,40.4,24.4,32.2*0A
					字段0：$GPGSA，语句ID，表明该语句为GPS DOP and Active Satellites（GSA）当前卫星信息
					字段1：定位模式，A=自动2D/3D，M=手动2D/3D
					字段2：定位类型，1=未定位，2=2D定位，3=3D定位
					字段3：PRN码（伪随机噪声码），第1信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段4：PRN码（伪随机噪声码），第2信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段5：PRN码（伪随机噪声码），第3信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段6：PRN码（伪随机噪声码），第4信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段7：PRN码（伪随机噪声码），第5信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段8：PRN码（伪随机噪声码），第6信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段9：PRN码（伪随机噪声码），第7信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段10：PRN码（伪随机噪声码），第8信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段11：PRN码（伪随机噪声码），第9信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段12：PRN码（伪随机噪声码），第10信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段13：PRN码（伪随机噪声码），第11信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段14：PRN码（伪随机噪声码），第12信道正在使用的卫星PRN码编号（00）（前导位数不足则补0）
					字段15：PDOP综合位置精度因子（0.5 - 99.9）
					字段16：HDOP水平精度因子（0.5 - 99.9）
					字段17：VDOP垂直精度因子（0.5 - 99.9）
					字段18：校验值
				 */
				else if (nmea.startsWith("$GPGSA")) {
					String[] nmeaGPGSA = nmea.split(",", 0);
					if (nmeaGPGSA.length < 12) {
						Log.i(TAG, "onNmeaReceived() nmeaGPGSA.length<12, invalid data, so return!!!");
						return;
					}
					
					String nmeaListenerGpsData =  "$GPGSA nmea: " + nmea + "\n";
							
					if (nmeaGPGSA[2].equals("2") || nmeaGPGSA[2].equals("3")) {
						nmeaListenerGpsData += "$GPGSA Fix: Fix" + "\n";
					} else {
						nmeaListenerGpsData += "$GPGSA Fix: No Fix" + "\n";
					}
					
					mNmeaListenerInfoFixGpgsa.setText(nmeaListenerGpsData);
				}
			}
			
		});
		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
			
			public void onLocationChanged(Location location) {
				Log.e(TAG, "onLocationChanged............................");
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
				switch (status) {

				case LocationProvider.AVAILABLE:
					Log.e(TAG, "GPS: AVAILABLE");
					Toast.makeText(MainActivity.this, "GPS: AVAILABLE", Toast.LENGTH_SHORT).show();
					break;

				case LocationProvider.OUT_OF_SERVICE:
					Log.e(TAG, "GPS: OUT_OF_SERVICE");
					Toast.makeText(MainActivity.this, "GPS: OUT_OF_SERVICE", Toast.LENGTH_SHORT).show();
					break;

				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					Log.e(TAG, "GPS: TEMPORARILY UNAVAILABLE");
					Toast.makeText(MainActivity.this, "GPS: TEMPORARILY_UNAVAILABLE", Toast.LENGTH_SHORT).show();
					break;
				}
			}

			/**
			 * GPS Enabled
			 */
			public void onProviderEnabled(String provider) {
				Log.e(TAG, "onProviderEnabled............................");
				Toast.makeText(MainActivity.this, "GPS: onProviderEnabled()", Toast.LENGTH_SHORT).show();
			}

			/**
			 * GPS Disabled
			 */
			public void onProviderDisabled(String provider) {
				Log.e(TAG, "onProviderDisabled............................");
				Toast.makeText(MainActivity.this, "GPS: onProviderDisabled()", Toast.LENGTH_SHORT).show();
			}
		});
	}
}