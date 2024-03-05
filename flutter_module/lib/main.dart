import 'package:flutter/material.dart';
import 'package:flutter_module/pages/favorite_page.dart';
import 'package:flutter_module/pages/recommend_page.dart';

//至少有一个入口
void main() => runApp(const MyApp(
      page: FavoritePage(),
    ));

@pragma('vm:entry-point')
void recommend() => runApp(const MyApp(page: RecommendPage()));

class MyApp extends StatelessWidget {
  final Widget page;
  const MyApp({super.key, required this.page});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(
          // This is the theme of your application.
          //
          // Try running your application with "flutter run". You'll see the
          // application has a blue toolbar. Then, without quitting the app, try
          // changing the primarySwatch below to Colors.green and then invoke
          // "hot reload" (press "r" in the console where you ran "flutter run",
          // or press Run > Flutter Hot Reload in a Flutter IDE). Notice that the
          // counter didn't reset back to zero; the application is not restarted.
          primarySwatch: Colors.blue,
        ),
        home: Scaffold(
          body: page,
        ));
  }
}
