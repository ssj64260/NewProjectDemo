package com.android.newprojectdemo.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * 获取MAC
 */

public class MacUtils {
    private static final String marshmallowMac = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String getMAC(Context context) {
        final String wifiMac = getMacFromWifi(context);
        if (!TextUtils.isEmpty(wifiMac) && !marshmallowMac.equals(wifiMac)) {
            return wifiMac;
        }

        final String interfaceMac = getMacFromInterface();
        if (!TextUtils.isEmpty(interfaceMac) && !marshmallowMac.equals(interfaceMac)) {
            return interfaceMac;
        }

        final String fileMac = getMacFromFile(context);
        if (!TextUtils.isEmpty(fileMac) && !marshmallowMac.equals(fileMac)) {
            return fileMac;
        }

        final String cmdMac = getMacFromCMD();
        if (!TextUtils.isEmpty(cmdMac) && !marshmallowMac.equals(cmdMac)) {
            return cmdMac;
        }

        return "";
    }

    private static String getMacFromInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }
        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return "";
    }

    private static String getMacFromFile(Context context) {
        String ret = "";
        try {
            final WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(true);
            final int wifiState = wifi.getWifiState();
            final File fl = new File(fileAddressMac);
            final FileInputStream fin = new FileInputStream(fl);
            ret = crunchifyGetStringFromStream(fin);
            fin.close();
            wifi.setWifiEnabled(WifiManager.WIFI_STATE_ENABLED == wifiState);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();
            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "";
        }
    }

    private static String getMacFromCMD() {
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String str = "";
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return macSerial;
    }

    private static String getMacFromWifi(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null) {
                String str = info.getMacAddress();
                if (!TextUtils.isEmpty(str)) {
                    return str;
                }
            }
        }
        return "";
    }
}
