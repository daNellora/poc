import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:hello/hello.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:otp/otp.dart';
import 'package:provider/provider.dart';
import 'dart:math';

import 'package:prueba/services/storage.dart';

class pantallaOTP extends StatefulWidget {
  const pantallaOTP({super.key});

  @override
  State<pantallaOTP> createState() => _MyWidgetState();
}

class _MyWidgetState extends State<pantallaOTP> {
  final myController = TextEditingController();
  var otpList;
  static const routeName = '/pantallaOTP';
  late HelloState plugin;

  @override
  void initState() {
    plugin = HelloState();
  }

  @override
  Widget build(BuildContext context) {
    int index;
    final deviceSize = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Color.fromRGBO(149, 193, 31, 1),
        title: const Text('OTP'),
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
                    padding: const EdgeInsets.all(50.50),
                    child: TextFormField(
                      controller: myController,
                      decoration: InputDecoration(labelText: 'Code'),
                      validator: (value) {
                        if (value!.isEmpty) {
                          return 'Invalid code';
                        }
                        return null;
                      },
                      onChanged: (value) {
                        print(value);
                      },
                    ),
                  ),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    ElevatedButton(
                      child: Text('Create new OTP'),
                      style: ElevatedButton.styleFrom(
                          backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                      onPressed: () async {
                        String code = myController.text;

                        try {
                          if (code.isEmpty) {
                            ScaffoldMessenger.of(context).showSnackBar(
                                SnackBar(content: Text('Enter a valid code')));
                            return;
                          } else {
                            await plugin.setOTP(code);
                          }
                        } on Exception catch (e) {
                          print('FLUTTER error creating OTP $e');
                        }
                      },
                    ),
                    SizedBox(
                      width: 25,
                    ),
                    ElevatedButton(
                      child: Text('Display secret'),
                      style: ElevatedButton.styleFrom(
                          backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                      onPressed: () async {
                        try {
                          if (otpList == null) {
                            ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                                content: Text('No secret has been created')));
                          } else {
                            var otpCode = await plugin.generateOTPKey(
                                otpList[0], otpList[1]);
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(
                                content: Text('Secret:  $otpCode'),
                              ),
                            );
                          }
                        } on Exception catch (e) {
                          print('FLUTTER error displaying secret $e');
                        }

                        //Navigator.pushNamed(context, '/');
                      },
                    ),
                  ],
                ),
                Padding(padding: EdgeInsets.all(10)),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    ElevatedButton(
                      child: Text('OTP List'),
                      style: ElevatedButton.styleFrom(
                          backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                      onPressed: () async {
                        try {
                          if (otpList == null){
                               ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(
                                content: Text('No OTP to show'),
                              ),
                            );
                          } else {
                            otpList = await plugin.getOTPList();

                          print('FLUTTER otp: $otpList');
                          }
                          
                        } catch (error) {
                          print('error listing otps');
                        }

                        //Navigator.pushNamed(context, '/otpList');
                      },
                    ),
                    SizedBox(
                      width: 25,
                    ),
                    ElevatedButton(
                      child: Text('Delete OTP'),
                      style: ElevatedButton.styleFrom(
                          backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                      onPressed: () async {
                        try {
                          var delete =
                              await plugin.removeOTP(otpList[0], otpList[1]);
                          print('FLUTTER delete: $delete');

                          ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('OTP Deleted')));
                        } catch (error) {
                          print('error deleting otp');
                        }

                        //Navigator.pushNamed(context, '/');
                      },
                    ),
                  ],
                ),
              ],
            ),
          )),
        ],
      ),
    );
  }
}
