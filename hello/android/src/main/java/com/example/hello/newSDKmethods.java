package com.example.hello;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.ProxyInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.telephony.CellIdentity;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ironchip.androidcommons.signals.SignalsResultCallback;
import com.ironchip.androidcommons.signals.SignalsService;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;


import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.transform.Result;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;


import ironchipauthenticatorgolangsdk.*;

//import /ironchipauthenticatorgolangsdk-sources.jar/ironchipauthenticatorgolangsdk;

public class newSDKmethods {
    UnauthorizedLBAuthSDKMobile unauthorizedLBAuthSDKMobile;
    AuthorizedLBAuthSDKMobile authorizedLBAuthSDK;

    OTPMobileInterface mobileInterfaceOTP;
    private static final int DNSPORT = 53;
    private String proxyHost = "";

    private int proxyPort = 0;
    Context context;



    public newSDKmethods(Context context) {
        this.context = context;
    }


    public void enrollcall(MethodCall call, MethodChannel.Result result){

        String version = call.argument("version");
        String apiHost = call.argument("host");
        String path = call.argument("path");


        System.out.println("[Java] version: " + version);
        System.out.println("[Java] apiHost: " + apiHost);
        System.out.println("[Java] PATH: " + path);



        this.unauthorizedLBAuthSDKMobile = new UnauthorizedLBAuthSDKMobile("android", apiHost, version,path + "/db.db");


        result.success("Client Init");
    }

    public  void  register(MethodCall call, MethodChannel.Result result)  {



        try {

            String deviceId = call.argument("DeviceId");
            String qrCode = call.argument("QrCode");

            System.out.println("[Java] DeviceId: " + deviceId);
            System.out.println("[Java] QRCode: " + qrCode);
            System.out.println("[Java] device name: "+getDeviceName());

            if (this.unauthorizedLBAuthSDKMobile != null){
                try {

                    String result2 =  this.unauthorizedLBAuthSDKMobile.register(getDeviceName(),deviceId,qrCode) ;
                    System.out.println("JAVA register TOKEN: " + result2);
                    result.success(result2);

                }catch (Exception e) {
                    Log.e("Enrollment Error", e.toString());
                }
            }else {
                System.out.println("JAVA client null");
                result.error("500", "client is null", null);
            }

        } catch (Exception u){
            result.error("500", u.toString(), null);
        }
        result.success("JAVA client register");
    }



    String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }

    }


    public void login(MethodCall call, MethodChannel.Result result){

        try {
            String apiHost = call.argument("Host");
            String version = call.argument("Version");
            String path = call.argument("StoragePath");
            String storageKey = call.argument("StorageKey");
            String notificationsURL = call.argument("NotificationsURL");

            GEOScanner geoScanner = new GEOScanner(() -> {
                GEOSignal geoSignal = new GEOSignal(43.30055843586947, -2.998840489331882, 11.719466209411621);
                System.out.println("JAVA geo: "+ geoSignal);
                return geoSignal;
            });

            ScannerInterface emScanner = new EMScanner(new EMScannerCallback() {

                SignalsService signalsService = SignalsService.getInstance(context);

                @Override
                public NetworkSignals getNetwork() throws Exception {

                    List<CellInfo> network = signalsService.obtainMobileNetworks();
                    NetworkSignals networkSignals = new NetworkSignals();

                    Signals signalsLte = new Signals("lte");
                    Signals signalsGsm = new Signals("gsm");
                    Signals signalsCdma = new Signals("cdma");
                    Signals signalsWcdma = new Signals("wcdma");

                    //System.out.println("JAVA Network: +" + network);
                    for (CellInfo cellInfo : network) {
                        if (cellInfo instanceof CellInfoGsm) {
                            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                            String gsmBssid = cellInfoGsm.getCellIdentity().getPsc() + "-" + cellInfoGsm.getCellIdentity().getArfcn();

                            signalsGsm.setSignal(gsmBssid, cellInfoGsm.getCellSignalStrength().getDbm());
                            continue;
                        }
                        if (cellInfo instanceof CellInfoCdma) {
                            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
                            String cdmaBssid = String.valueOf(cellInfoCdma.getCellIdentity().getBasestationId());

                            signalsCdma.setSignal(cdmaBssid, cellInfoCdma.getCellSignalStrength().getDbm());
                            continue;
                        }
                        if (cellInfo instanceof CellInfoWcdma) {
                            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                            String wcdmaBssid = cellInfoWcdma.getCellIdentity().getPsc() + "-" + cellInfoWcdma.getCellIdentity().getUarfcn();

                            signalsWcdma.setSignal(wcdmaBssid, cellInfoWcdma.getCellSignalStrength().getDbm());

                            continue;
                        }
                        if (cellInfo instanceof CellInfoLte) {
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                            String lteBssid = cellInfoLte.getCellIdentity().getPci() + "-" + cellInfoLte.getCellIdentity().getEarfcn();

                            signalsLte.setSignal(lteBssid, cellInfoLte.getCellSignalStrength().getDbm());
                            continue;
                        }
                    }

                    networkSignals.newSignals("lte", signalsLte);
                    networkSignals.newSignals("cdma", signalsCdma);
                    networkSignals.newSignals("gsm", signalsGsm);
                    networkSignals.newSignals("wcdma", signalsWcdma);

                    return networkSignals;
                }

                @Override
                public Signals getWifi() throws Exception {
                    List<ScanResult> wifiList = signalsService.obtainWiFis();

                    String wifiBssid;
                    long wifiRssid;
                    Signals signals = new Signals("wifi");

                    for (ScanResult scanResult : wifiList){
                         wifiBssid = scanResult.BSSID;
                         wifiRssid = scanResult.level;
                         signals.setSignal(wifiBssid, wifiRssid);
                    }


                    //System.out.println("JAVA WIFI: +" + wifiList);
                    //signals.setSignal(bssid, rssi);

                    return signals;
                }

            });

            System.out.println("[JAVA] Host " + apiHost);
            System.out.println("[JAVA] Version " + version);
            System.out.println("[JAVA] path " + path);
            System.out.println("[JAVA] storageKey " + storageKey);
            System.out.println("[JAVA] notificationsURL " + notificationsURL);

            this.authorizedLBAuthSDK = new AuthorizedLBAuthSDKMobile(apiHost, version, path + "/db.db", storageKey, notificationsURL, emScanner);
        }catch (Exception e){
            result.error("500", e.toString(), null);
        }
        result.success("JAVA client login");
    }

    public  void newOTP(MethodCall call, MethodChannel.Result result){
        try {
            String path = call.argument("Path");
           // String otpToken = call.argument("OTPToken");
            System.out.println("[JAVA] Hosted path: " + path );
           // System.out.println("[JAVA] Version " + otpToken);

           OTPMobileInitializer otpMobileInitializer=  new OTPMobileInitializer(path + "/otp.db");

           result.success(otpMobileInitializer.getToken());
        } catch (Exception o){
            result.error("500", o.toString(), null);
        }

    }
    public void existOTP(MethodCall call, MethodChannel.Result result){
        try {
            String path = call.argument("Path");
            String otpToken = call.argument("OTPToken");

            System.out.println("[JAVA] Host " + path );
            System.out.println("[JAVA] OTP token " + otpToken );

           this.mobileInterfaceOTP = new OTPMobileInterface( path + "/otp.db", otpToken);

            result.success("JAVA OTP token exist");
        } catch (Exception i){
            result.error("500", i.toString(), null);
        }
    }
    public void getOTPToken(MethodCall call, MethodChannel.Result result){
        String otpToken = call.argument("OTPToken");
        System.out.println("[JAVA] OTP Token send: " + otpToken);
        result.success("token get");
    }

    public void SetOTP(MethodCall call, MethodChannel.Result result){
        try {
            String url = call.argument("URL");
            System.out.println("[JAVA] URL: "+ url);
            this.mobileInterfaceOTP.setOTP(url);
        } catch (Exception i){
            result.error("500", i.toString(), null);
        }

        result.success("URL get");
    }

    public void RemoveOTP(MethodCall call, MethodChannel.Result result){
        try {
            String username = call.argument("Username");
            String ServiceID = call.argument("ServiceID");

            System.out.println("[JAVA] Username: "+username);
            System.out.println("[JAVA] Service ID: "+ServiceID);

            this.mobileInterfaceOTP.removeOTP(ServiceID, username);

        } catch (Exception i){
            result.error("500", i.toString(), null);
        }
        result.success("OTP Removed");
    }

    public  void getOTPList(MethodCall call, MethodChannel.Result result){

        List<Object> otpList = new ArrayList<>();

        try {
            if (this.mobileInterfaceOTP.getOTPs().hasNext()){
                otpList.add(this.mobileInterfaceOTP.getOTPs().next().getServiceID());
                otpList.add(this.mobileInterfaceOTP.getOTPs().next().getUsername());
                result.success(otpList);
            }else {
                System.out.println("No list available");

                result.success(otpList);
            }
            System.out.println("[JAVA] OTP List: "+ this.mobileInterfaceOTP.getOTPs().next().toString());


        } catch (Exception i){
            result.error("500", i.toString(), null);
        }

    }
    public void generateOTPKey(MethodCall call, MethodChannel.Result result) {

        String otpCode = null;
        try {
            String username = call.argument("Username");
            String ServiceID = call.argument("ServiceID");

            System.out.println("[JAVA] Username: " + username);
            System.out.println("[JAVA] Service ID: " + ServiceID);


            otpCode = this.mobileInterfaceOTP.generateOTPKey(ServiceID, username).getCode();

        } catch (Exception i) {
            result.error("500", i.toString(), null);
        }
        result.success(otpCode);

    }
    public  void  getOTPcode(MethodCall call, MethodChannel.Result result){
        try {
        String otpCode = call.argument("OTPCode");
        System.out.println("JAVA OTP code: "+ otpCode);

        } catch (Exception i){
            result.error("500", i.toString(), null);
        }
        result.success("JAVA OTP code");
    }

    public void getUser(MethodCall call, MethodChannel.Result result){
        List<Object> userInfo = new ArrayList<>();
      try {
          User  user = this.authorizedLBAuthSDK.getUser();
          userInfo.add(user.getUserName());
          userInfo.add(user.getSurname());
          userInfo.add(user.getEmail());
          System.out.println("JAVA username: "+user.getUserName());
          System.out.println("JAVA surname: "+user.getSurname());
          System.out.println("JAVA eMail: "+user.getEmail());

          result.success(userInfo);

      } catch (Exception e){
          result.error("500", e.toString(), null);
      }

    }
    public void getSkin(MethodCall call, MethodChannel.Result result){
        List<Object> skinarray = new ArrayList<>();
        try {
            Skin skin = this.authorizedLBAuthSDK.getSkin();
            skinarray.add(skin.getLaunchIcon());
            skinarray.add(skin.getLogoUrl());
            skinarray.add(skin.getMainColor());
            skinarray.add(skin.getNotiIcon());
            skinarray.add(skin.getSecondaryColor());
            skinarray.add(skin.getTrayIcon());
            System.out.println("JAVA get skin: "+skin);
            result.success(skinarray);
        } catch (Exception e) {
            result.error("500", e.toString(), null);
        }

    }
    public void getActiveAccesses(MethodCall call, MethodChannel.Result result){
        List<Object> activeAccesses = new ArrayList<>();
        try {
            if (this.authorizedLBAuthSDK.getActiveAccesses().hasNext()){
                activeAccesses.add(this.authorizedLBAuthSDK.getActiveAccesses().next().getAccessID());
                activeAccesses.add(this.authorizedLBAuthSDK.getActiveAccesses().next().getExpirationTime());
                activeAccesses.add(this.authorizedLBAuthSDK.getActiveAccesses().next().getState());
                activeAccesses.add(this.authorizedLBAuthSDK.getActiveAccesses().next().getExternalServiceID());
                result.success(activeAccesses);
            }else {
                System.out.println("No active accesses available");

                result.success(activeAccesses);
            }
        } catch (Exception e){
            result.error("500", e.toString(), null);
        }
    }
    public void getAccesses(MethodCall call, MethodChannel.Result result){
        List<Object> accesses = new ArrayList<>();
        try {
            if (this.authorizedLBAuthSDK.getAccesses().hasNext()){
                accesses.add(this.authorizedLBAuthSDK.getAccesses().next().getUser());
                accesses.add(this.authorizedLBAuthSDK.getAccesses().next().getService());
                accesses.add(this.authorizedLBAuthSDK.getAccesses().next().getExpirationTime());
                accesses.add(this.authorizedLBAuthSDK.getAccesses().next().getID());
                accesses.add(this.authorizedLBAuthSDK.getAccesses().next().getSpecificLocation());


                result.success(accesses);
            }else {
                System.out.println("No accesses available");

                    result.success(accesses);
            }
        } catch (Exception e){
            result.error("500", e.toString(), null);
        }
    }
    public void getKeys(MethodCall call, MethodChannel.Result result){
        List<Object> userKeys = new ArrayList<>();

        try {


            KeyIterator keyIterator= this.authorizedLBAuthSDK.getKeys();
            do {

               //System.out.println("JAVA keys: " + keyIterator.hasNext());

                Key key = keyIterator.next();
                userKeys.add(key.getKeyName());
                userKeys.add(key.getKeyType());

                System.out.println("JAVA keys: " + key.getKeyName());

            }while ( keyIterator.hasNext()  );
            System.out.println("JAVA keys: " + userKeys);


        } catch (Exception e){
            result.success(userKeys);
        }
        result.success(userKeys);
    }
    public void mobileGetIDSConfig(MethodCall call, MethodChannel.Result result){

        try {
            String host = this.authorizedLBAuthSDK.mobileGetIDSConfiguration().getHost();
            String apiKey = this.authorizedLBAuthSDK.mobileGetIDSConfiguration().getAPIKey();
            System.out.println("JAVA host :"+host);
            System.out.println("JAVA API Key: "+apiKey);
            result.success("[JAVA] mobileIDSConfig");
        } catch (Exception e){
            result.error("500", e.toString(), null);
            System.out.println("No IDS Configuration available");


        }
    }
    public void createKey(MethodCall call, MethodChannel.Result result){
       try {
           String newKey = call.argument("NewKey");

           this.authorizedLBAuthSDK.createKey(newKey, new ChannelCallback() {
               @Override
               public void onError(Exception e) {
                   System.out.println("[JAVA]  Error creating new key" + e);

               }

               @Override
               public void onFinish() {
                   System.out.println("[JAVA]  New key created" );
               }

           });
       } catch (Exception e){
           result.error("500", e.toString(), null);
       }
        result.success("JAVA Key created");
    }
    public void authorize(MethodCall call, MethodChannel.Result result){
        try {

            String accessID = call.argument("AccessID");
            this.authorizedLBAuthSDK.authorize(accessID, new ChannelCallback() {
                @Override
                public void onError(Exception e) {
                    System.out.println("[JAVA] Authorization error");
                }

                @Override
                public void onFinish() {
                    System.out.println("[JAVA] Authorized");
                }
            });
            System.out.println("JAVA Access ID: "+ accessID);

        } catch (Exception e){
            result.error("500", e.toString(), null);
        }
        result.success("JAVA Access ID");
    }
    public void getNotifications(MethodCall call, MethodChannel.Result result) {

        try {
            this.authorizedLBAuthSDK.getNotifications(new NotificationCallback() {
                @Override
                public void onError(Exception e) {
                    System.out.println("[JAVA] Notification error");
                }

                @Override
                public void onNotification(Notification notification) {
                    System.out.println("[JAVA] Notification");
                }
            } );
        } catch (Exception e) {
            result.error("500", e.toString(), null);
        }
        result.success("JAVA notifications");
    }
    public void initMutualTLS(MethodCall call, MethodChannel.Result result){
        try {
            this.authorizedLBAuthSDK.initMutualTLS();
            result.success("JAVA MutualTLS init");
        } catch (Exception e){
            result.error("500", e.toString(), null);
        }
    }
    public void setProxy(MethodCall call, MethodChannel.Result result){
        try {
            setProxy();
            System.out.println(this.proxyHost);
            System.out.println(this.proxyPort);
            //this.authorizedLBAuthSDK.setProxy(this.proxyHost, this.proxyPort);
            result.success("JAVA setProxy");
        } catch (Exception e){
            result.error("500", e.toString(), null);
        }
    }
    public void setDNS(MethodCall call, MethodChannel.Result result){
        try {
            System.out.println(obtainDNSIP());
            //this.authorizedLBAuthSDK.setDNS(obtainDNSIP(), "tcp");
            result.success("JAVA setDNS");
        } catch (Exception e){
            result.error("500", e.toString(), null);
        }
    }
    private String obtainDNSIP()  {

        ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    for (Network network : connectivityManager.getAllNetworks()) {

                        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);

                        if (networkInfo.isConnected()) {

                            LinkProperties linkProperties = connectivityManager.getLinkProperties(network);

                            for (InetAddress dns: linkProperties.getDnsServers()) {

                                String dnsIP = dns.getHostAddress();
                                System.out.println("JAVA newSDK"+ dnsIP);
                                try {
                                    System.out.println(dns.isReachable(1000));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                if(!dnsIP.equals("")) {

                                    continue;

                                }


                                try {
                                    if(!dns.isReachable(1000)){

                                        continue;

                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                //return dnsIP + ":" + String.valueOf(DNSPORT);

                            }

                        }

                    }
                }

                //throw new Exception("DNS not found");
            }
        });
        thread.start();
    return " ";

    }
    private void setProxy() {
            System.out.println("[JAVA] set proxy"+ this.context);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService(

                Context.CONNECTIVITY_SERVICE

        );

        ProxyInfo proxyInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            proxyInfo = connectivityManager.getDefaultProxy();
            this.proxyHost = proxyInfo.getHost();

            this.proxyPort = proxyInfo.getPort();
        }


        if (proxyInfo == null) {

            Log.e("Proxy", "No proxy detected");

            return;

        }




    }
    public void unauthorizedSetDNS(MethodCall call, MethodChannel.Result result){
        try {
            obtainDNSIP();
            System.out.println(obtainDNSIP());
            //this.unauthorizedLBAuthSDKMobile.setDNS(obtainDNSIP(), "tcp");
            result.success("[JAVA] unauthorized DNS");
        } catch (Exception e){
            result.error("500", e.toString(), null);
        }
    }
    public void unauthorizedSetProxy(MethodCall call, MethodChannel.Result result){
        try {
            setProxy();
            System.out.println(this.proxyHost);
            System.out.println(this.proxyPort);
            //this.unauthorizedLBAuthSDKMobile.setProxy(this.proxyHost , this.proxyPort);
            result.success("[JAVA] unauthorized proxy");
        } catch (Exception e){
            result.error("500", e.toString(), null);
        }
    }
}
//otpauth://hotp/fdfg:rtfgfv?secret=cmfg6cnn6johemi5wnfvujgcuod2ogyliob5te26ooxnskrezabs4rvd&algorithm=SHA256&digits=6&period=30&lock=false&counter=0