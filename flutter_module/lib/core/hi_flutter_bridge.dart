import 'package:flutter/services.dart';

class HiFlutterBridge {
  static HiFlutterBridge _instance = HiFlutterBridge._();
  MethodChannel _bridge = const MethodChannel("HiFlutterBridge");
  var _listeners = {};

  HiFlutterBridge._() {
    _bridge.setMethodCallHandler((MethodCall call) {
      String method = call.method;
      if (_listeners[method] != null) {
        return _listeners[method](call);
      }
      return Future.value();
    });
  }

  static HiFlutterBridge getInstance() {
    return _instance;
  }

  register(String method, Function(MethodCall) callBack) {
    _listeners[method] = callBack;
  }

  unRegister(String method) {
    _listeners.remove(method);
  }

  goToNative(Map params) {
    _bridge.invokeMethod("goToNative", params);
  }

  MethodChannel bridge() {
    return _bridge;
  }
}
