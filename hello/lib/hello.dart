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
      final String result4 = await platform.invokeMethod('new-otp', {
        'Path': path,
        //'OTPToken': otpToken,
      });
      return result4;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<List?> getUser() async {
    try {
      //var otpToken = await storageService().readStorage("registerToken");
      final List result4 = await platform.invokeMethod('user');
      print('Flutter  $result4');
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> sendOTPToken(String otpToken) async {
    try {
      final String result5 =
          await platform.invokeMethod('sendOTPToken', {'otp-token': otpToken});
      return result5;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> existOTPToken(String otpToken) async {
    try {
      Directory pathDir = await getApplicationDocumentsDirectory();

      var path = pathDir.path;
      final String result6 = await platform.invokeMethod('exist-otp-token', {
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
      final String result7 = await platform.invokeMethod('otp-pass', {
        'OTPCode': otpCode,
      });
      return result7;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> setOTP(String url) async {
    try {
      final String result8 = await platform.invokeMethod('set-otp', {
        'URL': url,
      });
      return result8;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> removeOTP(String serviceID, String username) async {
    try {
      final String result9 = await platform.invokeMethod('remove-otp', {
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
      final List result10 = await platform.invokeMethod('get-otp-list');
      return result10;
    } catch (error) {
      return Future.error(error);
    }
  }

  Future<String?> generateOTPKey(String serviceID, String username) async {
    try {
      final String result11 = await platform.invokeMethod('generate-otp-key', {
        'ServiceID': serviceID,
        'Username': username,
      });
      return result11;
    } catch (error) {
      return Future.error(error);
    }
  }
  Future<List?> getSkin() async {
    try {
      final List result12 = await platform.invokeMethod('get-skin');
      return result12;
    } catch (error) {
      return Future.error(error);
    }
  }
  Future<List?> getKeys() async {
    try {
      final List result13 = await platform.invokeMethod('get-keys');
      return result13;
    } catch (error) {
      return Future.error(error);
    }
  }
   Future<List?> getActiveAccesses() async {
    try {
      final List result14 = await platform.invokeMethod('get-active-accesses');
      return result14;
    } catch (error) {
      return Future.error(error);
    }
  }
    Future<List?> getAccesses() async {
    try {
      final List result15 = await platform.invokeMethod('get-accesses');
      return result15;
    } catch (error) {
      return Future.error(error);
    }
  }
    Future<List?> mobileIDSConfig() async {
    try {
      final List result16 = await platform.invokeMethod('mobile-IDS-Configuration');
      return result16;
    } catch (error) {
      return Future.error(error);
    }
  }
  Future<String?> authorize(String accessID) async {
    try {
      final String result17 = await platform.invokeMethod('authorize', {
        'AccessID': accessID,
      });
      return result17;
    } catch (error) {
      return Future.error(error);
    }
  }
  Future<String?> newKey(String newKey) async {
    try {
      final String result18 = await platform.invokeMethod('new-key', {
        'NewKey': newKey,
      });
      return result18;
    } catch (error) {
      return Future.error(error);
    }
  }
  Future<String?> getNotifications() async {
    try {
      final String result19 = await platform.invokeMethod('get-notification', {
        
      });
      return result19;
    } catch (error) {
      return Future.error(error);
    }
  }
   Future<String?> initMutualTLS() async {
    try {
      final String result20 = await platform.invokeMethod('init-mutualTLS', {
        
      });
      return result20;
    } catch (error) {
      return Future.error(error);
    }
  }
  Future<String?> setProxy() async {
    try {
      final String result21 = await platform.invokeMethod('set-proxy', {
        
      });
      return result21;
    } catch (error) {
      return Future.error(error);
    }
  }
  Future<String?> setDNS() async {
    try {
      final String result22 = await platform.invokeMethod('set-nds', {
        
      });
      return result22;
    } catch (error) {
      return Future.error(error);
    }
  }
    Future<String?> unauthorizeSetDNS() async {
    try {
      final String result23 = await platform.invokeMethod('unauthorize-set-nds', {
        
      });
      return result23;
    } catch (error) {
      return Future.error(error);
    }
  }
    Future<String?> unauthorizeSetProxy() async {
    try {
      final String result24 = await platform.invokeMethod('unauthorize-set-proxy', {
        
      });
      return result24;
    } catch (error) {
      return Future.error(error);
    }
  }
}
