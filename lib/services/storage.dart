import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class storageService {
  final storage = new FlutterSecureStorage();
  

  Future<String?> readStorage(String readKey) async {
    String? value = await storage.read(key: readKey);
    return value;
  }

  Future<void> writeStorage(String readKey, String valueKey) async {
    await storage.write(key: readKey, value: valueKey);
    
  }

  Future<String?> deleteStorage(String deleteKey) async {
    await storage.delete(key: deleteKey);
    return deleteKey;
  }
}
