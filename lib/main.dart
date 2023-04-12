import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:prueba/pantallas/pantallaOTP.dart';
import 'package:prueba/pantallas/pantalla_login.dart';
import './pantallas/pantalla_inicial.dart';
import './pantallas/pantalla_OTPList.dart';

void main() {
  runApp(
    MaterialApp(
      title: 'Named Routes Demo',
      // Start the app with the "/" named route. In this case, the app starts
      // on the FirstScreen widget.
      initialRoute: '/',
      routes: {
        '/': (context) => pantallaInicial(),
        '/second': (context) => pantallaLogin(),
        '/third': (context) => pantallaOTP(),
        '/otpList':(context) => otpList()
      },
    ),
  );
}

class MyApp extends StatefulWidget {
  
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        primarySwatch: Colors.green,
        accentColor: Colors.greenAccent,
      ),
      home: pantallaInicial(),
    );
  }
}
