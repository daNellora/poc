import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter/src/widgets/placeholder.dart';
import 'package:flutter/material.dart';
import 'package:hello/hello.dart';

class pantallaLogin extends StatefulWidget {
  const pantallaLogin({super.key});

  @override
  State<pantallaLogin> createState() => _pantallaLoginState();
}

class _pantallaLoginState extends State<pantallaLogin> {
  static const routeName = '/pantallaLogin';
  late HelloState plugin;
  Future<void> getUser() async {
    plugin.getUser();
  }

  @override
  void initState() {
    plugin = HelloState();
    getUser();
  }

  @override
  Widget build(BuildContext context) {
    final deviceSize = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Color.fromRGBO(149, 193, 31, 1),
        title: const Text('Login'),
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
                ElevatedButton(
                  child: Text('A'),
                  style: ElevatedButton.styleFrom(primary: Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () {},
                ),
                ElevatedButton(
                  child: Text('B'),
                  style: ElevatedButton.styleFrom(primary: Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () {
                    Navigator.pushNamed(context, '/');
                  },
                ),
                ElevatedButton(
                  child: Text('C'),
                  style: ElevatedButton.styleFrom(primary: Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () {
                    Navigator.pushNamed(context, '/');
                  },
                ),
              ],
            ),
          )),
        ],
      ),
    );
  }
}
