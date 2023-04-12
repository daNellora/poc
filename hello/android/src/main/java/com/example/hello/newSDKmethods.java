package com.example.hello;

import android.os.Build;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;


import ironchipauthenticatorgolangsdk.*;

//import /ironchipauthenticatorgolangsdk-sources.jar/ironchipauthenticatorgolangsdk;

public class newSDKmethods {
    UnauthorizedLBAuthSDKMobile u;
    AuthorizedLBAuthSDKMobile e;
    OTPMobileInitializer o;
    OTPMobileInterface i;
    OTPIterator iterator;



    public void enrollcall(MethodCall call, MethodChannel.Result result){

        String version = call.argument("version");
        String apiHost = call.argument("host");
        String path = call.argument("path");


        System.out.println("[Java] version: " + version);
        System.out.println("[Java] apiHost: " + apiHost);
        System.out.println("[Java] PATH: " + path);


        this.u = new UnauthorizedLBAuthSDKMobile("android", apiHost, version,path + "/db.db");


        result.success("Client Init");
    }

    public  void  register(MethodCall call, MethodChannel.Result result)  {



        try {

            String deviceId = call.argument("DeviceId");
            String qrCode = call.argument("QrCode");

            System.out.println("[Java] DeviceId: " + deviceId);
            System.out.println("[Java] QRCode: " + qrCode);
            System.out.println("[Java] device name: "+getDeviceName());

            if (this.u != null){
                String result2 =  this.u.register(getDeviceName(),deviceId,qrCode) ;
                System.out.println("JAVA register TOKEN: " + result2);
                result.success(result2);
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
            ScannerInterface emScanner = new EMScanner(new EMScannerCallback() {
                @Override
                public NetworkSignals getNetwork() throws Exception {
                    return null;
                }

                @Override
                public Signals getWifi() throws Exception {
                    return null;
                }
            });
            System.out.println("[JAVA] Host " + apiHost);
            System.out.println("[JAVA] Version " + version);
            System.out.println("[JAVA] path " + path);
            System.out.println("[JAVA] storageKey " + storageKey);
            System.out.println("[JAVA] notificationsURL " + notificationsURL);

            this.e = new AuthorizedLBAuthSDKMobile(apiHost, version, path + "/db.db", storageKey, notificationsURL, emScanner);
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

           this.i = new OTPMobileInterface( path + "/otp.db", otpToken);

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
            this.i.setOTP(url);
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

            this.i.removeOTP(ServiceID, username);

        } catch (Exception i){
            result.error("500", i.toString(), null);
        }
        result.success("OTP Removed");
    }

    public  void getOTPList(MethodCall call, MethodChannel.Result result){

        List<Object> otpList = new ArrayList<>();

        try {
            if (this.i.getOTPs().hasNext()){
                otpList.add(this.i.getOTPs().next().getServiceID());
                otpList.add(this.i.getOTPs().next().getUsername());
                result.success(otpList);
            }else {
                System.out.println("No list available");

                result.success(otpList);
            }
            System.out.println("[JAVA] OTP List: "+ this.i.getOTPs().next().toString());


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

            
            otpCode = this.i.generateOTPKey(ServiceID, username).getCode();

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
      try {
          User  user = this.e.getUser();
          System.out.println(user.getUserName());
          System.out.println(user.getSurname());
          System.out.println(user.getEmail());

          result.success("JAVA user");

      } catch (Exception e){
          result.error("500", e.toString(), null);
      }

    }
}
//otpauth://hotp/fdfg:rtfgfv?secret=cmfg6cnn6johemi5wnfvujgcuod2ogyliob5te26ooxnskrezabs4rvd&algorithm=SHA256&digits=6&period=30&lock=false&counter=0