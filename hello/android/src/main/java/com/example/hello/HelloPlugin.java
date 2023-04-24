package com.example.hello;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;
import java.net.InetAddress;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


/** HelloPlugin */
public class HelloPlugin implements FlutterPlugin, MethodCallHandler  {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private Context context;
  private MethodChannel channel;

  newSDKmethods  newSDKmethods ;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "samples.flutter.dev/method");
    channel.setMethodCallHandler(this);
    this.context = flutterPluginBinding.getApplicationContext();
    obtainDNSIP();
    this.newSDKmethods = new newSDKmethods(this.context);


  }
  private void  obtainDNSIP() {

    ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      for (Network network : connectivityManager.getAllNetworks()) {

        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);

        if (networkInfo.isConnected()) {

          LinkProperties linkProperties = connectivityManager.getLinkProperties(network);

          for (InetAddress dns: linkProperties.getDnsServers()) {

            String dnsIP = dns.getHostAddress();


            System.out.println(dnsIP);



          }

        }

      }
    }



  }
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result)  {


    switch ( call.method){
      case "enroll":
        this.newSDKmethods.enrollcall(call , result);
        break;
      case "register":
        this.newSDKmethods.register(call, result);
          break;
      case "login":
        this.newSDKmethods.login(call, result);
        break;
      case "user":
       System.out.println("gjgfhgfghfh");
       obtainDNSIP();
        this.newSDKmethods.getUser(call, result);
        break;
      case "new-otp":
        this.newSDKmethods.newOTP(call, result);
        break;
      case  "send-otp-token":
        this.newSDKmethods.getOTPToken(call, result);
        break;
      case "exist-otp-token":
        this.newSDKmethods.existOTP(call, result);
        break;
      case "otp-pass":
        this.newSDKmethods.getOTPcode(call, result);
        break;
      case  "set-otp":
        this.newSDKmethods.SetOTP(call, result);
        break;
      case "remove-otp":
        this.newSDKmethods.RemoveOTP(call, result);
        break;
      case "get-otp-list":
        this.newSDKmethods.getOTPList(call, result);
        break;
      case "generate-otp-key":
        this.newSDKmethods.generateOTPKey(call, result);
        break;
      case "get-skin":
        this.newSDKmethods.getSkin(call, result);
        break;
      case "get-active-accesses":
        this.newSDKmethods.getActiveAccesses(call, result);
        break;
      case "get-accesses":
        this.newSDKmethods.getAccesses(call, result);
        break;
      case "get-keys":
        this.newSDKmethods.getKeys(call, result);
        break;
      case "mobile-IDS-Configuration":
        this.newSDKmethods.mobileGetIDSConfig(call, result);
        break;
      case "authorize":
        this.newSDKmethods.authorize(call, result);
        break;
      case "new-key":
        this.newSDKmethods.createKey(call, result);
        break;
      case "get-notification":
        this.newSDKmethods.getNotifications(call, result);
        break;
      case "init-mutualTLS":
        this.newSDKmethods.initMutualTLS(call, result);
        break;
      case "set-proxy":
        this.newSDKmethods.setProxy(call, result);
        break;
      case "set-nds":
        this.newSDKmethods.setDNS(call, result);
        break;
      case "unauthorize-set-proxy":
        this.newSDKmethods.unauthorizedSetProxy(call, result);
        break;
      case "unauthorize-set-nds":
        this.newSDKmethods.unauthorizedSetDNS(call, result);
        break;
      default:
        result.notImplemented();
        break;
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }


}
