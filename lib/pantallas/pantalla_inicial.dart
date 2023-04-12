import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:hello/hello.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:otp/otp.dart';
import 'package:provider/provider.dart';
import 'dart:math';

import 'package:prueba/services/storage.dart';

class pantallaInicial extends StatefulWidget {
  static const routeName = '/pantallaInicial';

  @override
  State<pantallaInicial> createState() => _pantallaInicialState();
}

class _pantallaInicialState extends State<pantallaInicial> {
  final myController = TextEditingController();

  late HelloState plugin;
  late PackageInfo packageInfo;

  @override
  void initState() {
    initPackageInfo();
    print('oninit call');
    plugin = HelloState();
  }

  void initPackageInfo() async {
    packageInfo = await PackageInfo.fromPlatform();
    var loadToken = await storageService().readStorage("registerToken");
    var loadedOTPcode = await storageService().readStorage("OTPPass");
    var loadedOTPToken = await storageService().readStorage("OTPKey");
    if (kDebugMode) {
      print("FLUTTER token from database: $loadToken");

      print("FLUTTER OTP Token: $loadedOTPToken");
    }
  }

  @override
  Widget build(BuildContext context) {
    Map<String, String> _userData = {
      'username': '',
    };
    final deviceSize = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Color.fromRGBO(149, 193, 31, 1),
        title: const Text('Register'),
      ),
      body: Stack(
        children: <Widget>[
          SingleChildScrollView(
            child: Container(
              height: deviceSize.height,
              width: deviceSize.width,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  Flexible(
                    child: Padding(
                      padding: const EdgeInsets.only(left: 50, right: 50),
                      child: TextFormField(
                        controller: myController,
                        decoration: InputDecoration(labelText: 'username'),
                        validator: (value) {
                          if (value!.isEmpty) {
                            return 'Invalid username';
                          }
                          return null;
                        },
                        onChanged: (value) {
                          print(value);
                        },
                        onSaved: (value) {
                          _userData['username'] = value!;
                        },
                      ),
                    ),
                  ),
                  ElevatedButton(
                    child: Text('Register'),
                    style: ElevatedButton.styleFrom(
                        primary: myController.text.isEmpty? Colors.grey : Color.fromRGBO(149, 193, 31, 1)),
                       
                    onPressed: () async {
                      //String packageName = packageInfo.packageName;

                      String version = packageInfo.version;

                      String qrCode = myController.text;
                      String deviceId = 'emulated phone';
                      if (qrCode.isNotEmpty) {
                        try {
                          var result = await plugin.enroll(
                            "https://testing.api.ironchip.com",
                            version,
                          );
                          print('FLUTTER result enroll: $result');
                          var token = await plugin.register(qrCode, deviceId);

                          print("FLUTTER token $token");
                          await storageService()
                              .writeStorage("registerToken", token);
                          var loadToken = await storageService()
                              .readStorage("registerToken");
                          if (kDebugMode) {
                            print("FLUTTER token from database: $loadToken");
                          }
                        } catch (errot) {
                          print('FLUTTER error: $errot');
                        }
                      } else {
                        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                          content: Text('Enter a valid code'),
                        ));
                      }
                    },
                  ),
                  ElevatedButton(
                    child: Text('Login'),
                    style: ElevatedButton.styleFrom(
                        primary: Color.fromRGBO(149, 193, 31, 1)),
                    onPressed: () async {
                      String version = packageInfo.version;
                      String? loadedToken =
                          await storageService().readStorage("registerToken");
                      var result = await plugin.login(
                        "https://testing.api.ironchip.com",
                        version,
                        loadedToken!,
                        "wss://testing.api.ironchip.com/notifications",
                      );
                      print(
                        'FLUTTER result login: $result',
                      );
                      //Navigator.of(context).pushReplacementNamed('/pantallaLogin');
                      Navigator.pushNamed(context, '/second');
                    },
                  ),
                  ElevatedButton(
                    child: Text('OTP'),
                    style: ElevatedButton.styleFrom(
                        primary: Color.fromRGBO(149, 193, 31, 1)),
                    onPressed: () async {
                      String? loadOTPToken =
                          await storageService().readStorage("OTPKey");
                      if (loadOTPToken == null) {
                        print('otpToken is null');

                        var otpToken = await plugin.newotp();
                        await storageService()
                            .writeStorage("OTPKey", otpToken!);
                        loadOTPToken =
                            await storageService().readStorage("OTPKey");
                        await plugin.sendOTPToken(loadOTPToken!);
                        await plugin.existOTPToken(loadOTPToken);
                        /* String optPass = OTP.generateHOTPCodeString(
                            loadOTPToken, 1362302550000);
                        await storageService().writeStorage("OTPPass", optPass);
                        await plugin.otpPass(optPass);*/
                        Navigator.pushNamed(context, '/third');
                        //return;
                      } else {
                        await plugin.existOTPToken(loadOTPToken);
                        String optPass =
                            OTP.generateHOTPCodeString(loadOTPToken, 60);
                        await storageService().writeStorage("OTPPass", optPass);
                        await plugin.otpPass(optPass);
                        Navigator.pushNamed(context, '/third');
                      }

                      //String? loadOTPToken = await storageService().readStorage("OTPKey");

                      //Navigator.of(context).pushReplacementNamed('/pantallaOTP');
                    },
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
