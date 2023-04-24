import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter/src/widgets/placeholder.dart';
import 'package:flutter/material.dart';
import 'package:hello/hello.dart';
import 'package:prueba/services/storage.dart';

class pantallaLogin extends StatefulWidget {
  const pantallaLogin({super.key});

  @override
  State<pantallaLogin> createState() => _pantallaLoginState();
}

class _pantallaLoginState extends State<pantallaLogin> {
  final myController = TextEditingController();
  static const routeName = '/pantallaLogin';
  late HelloState plugin;

  @override
  void initState() {
    plugin = HelloState();
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
                  child: Text('user info'),
                  style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () async {
                    try {
                      final userResult = await plugin.getUser();
                      print('FLUTTER user: $userResult');
                    } on Exception catch (e) {
                      print('FLUTTER user error $e');
                    }
                  },
                ),
                ElevatedButton(
                  child: Text('get accesses'),
                  style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () async {
                    try {
                      final accesses = await plugin.getAccesses();
                      print('FLUTTER accesses: $accesses');
                      var accessID = accesses![3];
                      print(accessID);
                    } on Exception catch (e) {
                      print('FLUTTER skin error $e');
                    }
                  },
                ),
                ElevatedButton(
                  child: Text('authorize active accesses'),
                  style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () async {
                    try {
                      final activeAccesses = await plugin.getActiveAccesses();
                      print('FLUTTER active accesses: $activeAccesses');
                      await plugin.authorize(activeAccesses![0]);
                      await plugin.mobileIDSConfig();
                      await plugin.getNotifications();
                    } on Exception catch (e) {
                      print('FLUTTER skin error $e');
                    }
                  },
                ),
                 ElevatedButton(
                  child: Text('Set MutualTLS'),
                  style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () async {
                    try {
                      await plugin.initMutualTLS();
                    } on Exception catch (e) {
                      print('FLUTTER skin error $e');
                    }
                  },
                ),
                  ElevatedButton(
                  child: Text('Set proxy'),
                  style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () async {
                    try {
                      await plugin.setProxy();
                    } on Exception catch (e) {
                      print('FLUTTER skin error $e');
                    }
                  },
                ),
                   ElevatedButton(
                  child: Text('Set proxy'),
                  style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () async {
                    try {
                      await plugin.setDNS();
                    } on Exception catch (e) {
                      print('FLUTTER skin error $e');
                    }
                  },
                ),
                ElevatedButton(
                  child: Text('Get Keys'),
                  style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () async {
                    try {
                      final keyList = await plugin.getKeys();
                      print('FLUTTER keys: $keyList');
                    } on Exception catch (e) {
                      print('FLUTTER key error $e');
                    }
                  },
                ),
                Flexible(
                  child: Padding(
                    padding: const EdgeInsets.only(left: 50, right: 50),
                    child: TextFormField(
                      controller: myController,
                      decoration: InputDecoration(labelText: 'New key name'),
                      validator: (value) {
                        if (value!.isEmpty) {
                          return 'Invalid key name';
                        }
                        return null;
                      },
                      onChanged: (value) {},
                      onSaved: (value) {},
                    ),
                  ),
                ),
                ElevatedButton(
                  child: Text('Create new key'),
                  style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromRGBO(149, 193, 31, 1)),
                  onPressed: () async {
                    try {
                      String createKey = myController.text;
                      print(createKey);
                      if (createKey.isEmpty) {
                        ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(content: Text('Enter a valid name')));
                        return;
                      } else {
                        await plugin.newKey(createKey);
                        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                            duration: const Duration(seconds: 15),
                            content: Text('Creating key')));
                        ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(content: Text('Key created')));
                      }
                    } on Exception catch (e) {
                      print('FLUTTER key error $e');
                    }
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
