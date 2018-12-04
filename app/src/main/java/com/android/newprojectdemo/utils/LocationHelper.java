package com.android.newprojectdemo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.DecimalFormat;

/**
 * 高德定位帮助类
 */

public class LocationHelper {

    private static final long LOCATION_INTERVAL = 5 * 1000;//定位时间间隔
    private static final int MAX_ERROR_TIMES = 2;//定位允许失败次数
    private static final DecimalFormat locationFormat = new DecimalFormat("#0.000000");

    private AMapLocationClient mLocationClient;
    private int mErrorTimes = 0;

    private OnLocationListener mLocationListener;

    public LocationHelper(Context context) {
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        mLocationClient.setLocationOption(getOption(LOCATION_INTERVAL));
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null && 0 == aMapLocation.getErrorCode()) {

                    final String adCode = aMapLocation.getAdCode();

                    if (TextUtils.isEmpty(adCode)) {
                        mErrorTimes++;
                        if (mErrorTimes < MAX_ERROR_TIMES) {
                            return;
                        }
                    }

                    showMessage(aMapLocation);

                    if (mLocationListener != null) {
                        mLocationListener.onSuccess(aMapLocation);
                    }
                } else {
                    mErrorTimes++;
                    if (mErrorTimes < MAX_ERROR_TIMES) {
                        return;
                    }

                    if (mLocationListener != null) {
                        mLocationListener.onError();
                    }
                }

                mErrorTimes = 0;
            }
        });
    }

    private AMapLocationClientOption getOption(long time) {
        final AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationCacheEnable(false);
        option.setInterval(time);
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        return option;
    }

    public void startLocation() {
        mErrorTimes = 0;
        if (mLocationClient != null) mLocationClient.startLocation();
    }

    public void stopLocation() {
        if (mLocationClient != null) mLocationClient.stopLocation();
    }

    public void destroyLocation() {
        if (mLocationClient != null) mLocationClient.onDestroy();
    }

    public void setOnLocationListener(OnLocationListener listener) {
        mLocationListener = listener;
    }

    public void setInterval(long time) {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.setLocationOption(getOption(time));
        }
    }

    private static String getLocationType(AMapLocation aMapLocation) {
        final int locationType = aMapLocation.getLocationType();
        if (AMapLocation.LOCATION_TYPE_CELL == locationType) {
            return "[基站定位]";
        } else if (AMapLocation.LOCATION_TYPE_FIX_CACHE == locationType) {
            return "[缓存定位]";
        } else if (AMapLocation.LOCATION_TYPE_GPS == locationType) {
            return "[GPS定位]";
        } else if (AMapLocation.LOCATION_TYPE_LAST_LOCATION_CACHE == locationType) {
            return "[最后位置缓存]";
        } else if (AMapLocation.LOCATION_TYPE_OFFLINE == locationType) {
            return "[离线定位]";
        } else if (AMapLocation.LOCATION_TYPE_SAME_REQ == locationType) {
            return "[前次定位结果]";
        } else if (AMapLocation.LOCATION_TYPE_WIFI == locationType) {
            return "[WIFI定位]";
        } else {
            return "";
        }
    }

    private static String getLocationAddress(AMapLocation aMapLocation) {
        final String province = aMapLocation.getProvince();//省信息
        final String city = aMapLocation.getCity();//城市信息
        final String district = aMapLocation.getDistrict();//城区信息
        final String street = aMapLocation.getStreet();//街道信息
        final String streetNum = aMapLocation.getStreetNum();//街道门牌号信息
        final String aoiName = aMapLocation.getAoiName();

        return province + city + district + street + streetNum + aoiName;
    }

    private void showMessage(AMapLocation aMapLocation) {
        final String typeName = LocationHelper.getLocationType(aMapLocation);
        final double latitude = aMapLocation.getLatitude();
        final double longitude = aMapLocation.getLongitude();
        final long timeStamp = aMapLocation.getTime();
        final String province = aMapLocation.getProvince();
        final String city = aMapLocation.getCity();
        final String district = aMapLocation.getDistrict();
        final String street = aMapLocation.getStreet();
        final String streetNum = aMapLocation.getStreetNum();
        final String aoiName = aMapLocation.getAoiName();
        final String cityCode = aMapLocation.getCityCode();
        final String adCode = aMapLocation.getAdCode();

        final String message = typeName
                + "\n经度：" + locationFormat.format(longitude)
                + "\n纬度：" + locationFormat.format(latitude)
                + "\n时间戳：" + timeStamp
                + "\n省份信息：" + province
                + "\n城市信息：" + city
                + "\n县区信息：" + district
                + "\n街道信息：" + street
                + "\n街道门牌号信息：" + streetNum
                + "\n纬度：" + aoiName
                + "\n城市编码：" + cityCode
                + "\n地区编码：" + adCode;
        Log.d("高德地图", message);
    }

    public interface OnLocationListener {
        void onSuccess(AMapLocation aMapLocation);

        void onError();
    }
}
