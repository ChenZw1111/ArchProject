import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_module/core/hi_flutter_bridge.dart';

class RecommendPage extends StatefulWidget {
  const RecommendPage({super.key});

  @override
  State<RecommendPage> createState() => _RecommendPageState();
}

class _RecommendPageState extends State<RecommendPage> {
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    HiFlutterBridge.getInstance().register('onRefresh', (MethodCall call) {
      print("-----onRefresh");
      return Future.value('Flutter received');
    });
  }

  @override
  void dispose() {
    super.dispose();
    HiFlutterBridge.getInstance().unRegister("onRefresh");
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          const Text(
            '推荐模块',
            style: TextStyle(color: Colors.blue),
          ),
          FloatingActionButton(onPressed: () {
            HiFlutterBridge.getInstance()
                .goToNative({"action": "goToLogin", "username": 'chen'});
          })
        ],
      ),
    );
  }
}
