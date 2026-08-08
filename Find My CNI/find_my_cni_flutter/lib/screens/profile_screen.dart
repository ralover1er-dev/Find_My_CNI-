import 'package:flutter/material.dart';
import '../services/supabase_service.dart';

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Profil & Synchro Cloud'),
        backgroundColor: const Color(0xFF007A3D),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            // User Avatar
            const CircleAvatar(
              radius: 40,
              backgroundColor: Color(0xFF007A3D),
              child: Text('LZ', style: TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.bold)),
            ),
            const SizedBox(height: 12),
            const Text('Lesly Zoyem', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
            const Text('leslyzoyem297@gmail.com', style: TextStyle(fontSize: 13, color: Colors.grey)),

            const SizedBox(height: 24),

            // Supabase Cloud Sync Card
            Card(
              shape: RoundedCornerShape(16),
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: const [
                        Icon(Icons.cloud_done, color: Color(0xFF007A3D), size: 28),
                        SizedBox(width: 12),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text('Serveur Supabase Cloud', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                              Text('Base centralisée: public.cni_declarations', style: TextStyle(fontSize: 11, color: Colors.grey)),
                            ],
                          ),
                        ),
                      ],
                    ),
                    const Divider(height: 24),
                    Container(
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: const Color(0xFFF1F5F9),
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: const [
                          Text('• URL: https://findmycni-cm.supabase.co', style: TextStyle(fontSize: 11, color: Colors.black87)),
                          Text('• Statut: Connecté & Actif', style: TextStyle(fontSize: 11, color: Color(0xFF007A3D), fontWeight: FontWeight.bold)),
                          SizedBox(height: 4),
                          Text(
                            'Note: L\'application Admin (dans les bureaux administratifs DGSN) se connecte directement à ce même serveur Supabase pour valider les déclarations.',
                            style: TextStyle(fontSize: 11, color: Colors.black54),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
