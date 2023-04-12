package com.example.hello;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


/** HelloPlugin */
public class HelloPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "samples.flutter.dev/method");
    channel.setMethodCallHandler(this);
  }
  newSDKmethods  newSDKmethods = new  newSDKmethods();
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result)  {


    switch ( call.method){
      case "enroll":
        newSDKmethods.enrollcall(call , result);
        break;
      case "register":
        newSDKmethods.register(call, result);
          break;
      case "login":
        newSDKmethods.login(call, result);
        break;
      case "user":
        newSDKmethods.getUser(call, result);
        break;
      case "newOTP":
        newSDKmethods.newOTP(call, result);
        break;
      case  "sendOTPToken":
        newSDKmethods.getOTPToken(call, result);
        break;
      case "existOTPToken":
        newSDKmethods.existOTP(call, result);
        break;
      case "otpPass":
        newSDKmethods.getOTPcode(call, result);
        break;
      case  "SetOTP":
        newSDKmethods.SetOTP(call, result);
        break;
      case "RemoveOTP":
        newSDKmethods.RemoveOTP(call, result);
        break;
      case "GetOTPList":
        newSDKmethods.getOTPList(call, result);
        break;
      case "GenerateOTPKey":
        newSDKmethods.generateOTPKey(call, result);
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
