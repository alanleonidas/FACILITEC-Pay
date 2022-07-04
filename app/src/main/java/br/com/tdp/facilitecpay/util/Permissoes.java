package br.com.tdp.facilitecpay.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissoes {
    private int PERMISSIONS_REQUEST_ACCESS_WIFI_STATE;
    private int PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE;
    private int PERMISSION_REQUEST_ACCESS_INTERNET;
    private int PERMISSAO_REQUEST = 128;
    private Activity activity;

    public Permissoes(Activity activity) {
        this.activity = activity;
    }

    public void HabilitarWifi(){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_WIFI_STATE)) {

            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                        PERMISSIONS_REQUEST_ACCESS_WIFI_STATE);
            }
        }
    }

    public void HabilitarNetWork(){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_NETWORK_STATE)) {

            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        PERMISSIONS_REQUEST_ACCESS_WIFI_STATE);
            }
        }
    }

    public void permissoesINTERNET() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, PERMISSAO_REQUEST);
            }
        }

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, PERMISSAO_REQUEST);
            }
        }
    }
}
