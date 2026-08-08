import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'services/supabase_service.dart';
import 'screens/home_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Initialisation Supabase
  await SupabaseService.init();

  runApp(const FindMyCniApp());
}

class FindMyCniApp extends StatelessWidget {
  const FindMyCniApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Find My CNI',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF007A3D),
          primary: const Color(0xFF007A3D),
          secondary: const Color(0xFFFCD116),
          error: const Color(0xFFCE1126),
        ),
        textTheme: GoogleFonts.plusJakartaSansTextTheme(),
      ),
      home: const HomeScreen(),
    );
  }
}
