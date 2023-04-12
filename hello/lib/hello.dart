import 'dart:ui';
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'hello_platform_interface.dart';

class Hello extends StatefulWidget {
  const Hello({super.key});

  @override
  State<Hello> createState() => HelloState();
}

class HelloState extends State<Hello> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }

  static const platform = MethodChannel('samples.flutter.dev/method');

  Future<String> enroll(
    String apiHost,
    String version,
    //String qrCode,
    //String deviceId,
  ) async {
    try {
      Directory pathDir = await getApplicationDocumentsDirectory();

      var path = pathDir.path;
      final String result = await platform.invokeMethod('enroll', {
        'path': path,
        'host': apiHost,
        'version': version,
        //'QrCode': qrCode,
        //'DeviceId': deviceId,
      });
      return result;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String> register(
    String qrCode,
    String deviceId,
  ) async {
    try {
      final String result2 = await platform.invokeMethod('register', {
        'QrCode': qrCode,
        'DeviceId': deviceId,
      });
      return result2;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String> login(
    String apiHost,
    String version,
    String storageKey,
    String notificationsURL,
    //ScannerInterface scannerInterface
  ) async {
    try {
      Directory pathDir = await getApplicationDocumentsDirectory();

      var path = pathDir.path;
      final String result3 = await platform.invokeMethod('login', {
        'Host': apiHost,
        'Version': version,
        'StoragePath': path,
        'StorageKey': storageKey,
        'NotificationsURL': notificationsURL,
        // 'ScannerInterface' : scannerInterface,
      });
      return result3;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> newotp() async {
    try {
      Directory pathDir = await getApplicationDocumentsDirectory();

      var path = pathDir.path;
      //var otpToken = await storageService().readStorage("registerToken");
      final String result4 = await platform.invokeMethod('newOTP', {
        'Path': path,
        //'OTPToken': otpToken,
      });
      return result4;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> getUser() async {
    try {
      //var otpToken = await storageService().readStorage("registerToken");
      final String result4 = await platform.invokeMethod('user');
      print('Flutter  $result4');
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> sendOTPToken(String otpToken) async {
    try {
      final String result5 =
          await platform.invokeMethod('sendOTPToken', {'OTPToken': otpToken});
      return result5;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> existOTPToken(String otpToken) async {
    try {
      Directory pathDir = await getApplicationDocumentsDirectory();

      var path = pathDir.path;
      final String result6 = await platform.invokeMethod('existOTPToken', {
        'Path': path,
        'OTPToken': otpToken,
      });
      return result6;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> otpPass(String otpCode) async {
    try {
      final String result7 = await platform.invokeMethod('otpPass', {
        'OTPCode': otpCode,
      });
      return result7;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> setOTP(String url) async {
    try {
      final String result8 = await platform.invokeMethod('SetOTP', {
        'URL': url,
      });
      return result8;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> removeOTP(String serviceID, String username) async {
    try {
      final String result9 = await platform.invokeMethod('RemoveOTP', {
        'ServiceID': serviceID,
        'Username': username,
      });
      return result9;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<List> getOTPList() async {
    try {
      final List result10 = await platform.invokeMethod('GetOTPList');
      return result10;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> generateOTPKey(String serviceID, String username) async {
    try {
      final String result11 = await platform.invokeMethod('GenerateOTPKey', {
        'ServiceID': serviceID,
        'Username': username,
      });
      return result11;
    } catch (error) {
      return Future.error(error);
    }
  }
}
/*class Hello {
  static const platform = MethodChannel('samples.flutter.dev/method');

  /*void requestAppDocumentsDirectory() {
    setState(() {
     final directory =  getApplicationDocumentsDirectory();
    });
  }*/

  Future<String> enroll(String apiHost, String version) async {
    try {
      Directory pathDir = await getApplicationDocumentsDirectory();

      var path = pathDir.path;
      final String result = await platform.invokeMethod('enroll', {
        'path': path,
        'host': apiHost,
        'version': version,
      });
      return result;
    } catch (error) {
      return Future.error(error);
    }
  }
}*/