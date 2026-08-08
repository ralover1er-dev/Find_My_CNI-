import 'dart:io';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../models/cni_model.dart';

class SupabaseService {
  // Config Supabase Find My CNI
  static const String supabaseUrl = 'https://findmycni-cm.supabase.co';
  static const String supabaseAnonKey =
      'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZpbmRteWNuaS1jbSIsInJvbGUiOiJhbm9uIiwiaWF0IjoxNzE2MzE2ODAwLCJleHAiOjIwMzE4OTI4MDB9.EXAMPLE_SUPABASE_ANON_KEY';

  static const String tableName = 'cni_declarations';

  static SupabaseClient get client => Supabase.instance.client;

  /// Initialisation de Supabase dans main()
  static Future<void> init() async {
    await Supabase.initialize(
      url: supabaseUrl,
      anonKey: supabaseAnonKey,
    );
  }

  /// Récupérer toutes les déclarations (triées par timestamp)
  static Future<List<CniModel>> getDeclarations() async {
    try {
      final response = await client
          .from(tableName)
          .select()
          .order('timestamp', ascending: false);

      return (response as List)
          .map((data) => CniModel.fromJson(data))
          .toList();
    } catch (e) {
      // Fallback données de démonstration en cas d'erreur réseau
      return _getDemoData();
    }
  }

  /// Rechercher par nom ou par les 4 derniers chiffres
  static Future<List<CniModel>> searchDeclarations(String query) async {
    try {
      final response = await client
          .from(tableName)
          .select()
          .or('full_name.ilike.%$query%,last_4_digits.ilike.%$query%')
          .order('timestamp', ascending: false);

      return (response as List)
          .map((data) => CniModel.fromJson(data))
          .toList();
    } catch (e) {
      final all = await getDeclarations();
      return all.where((item) {
        return item.fullName.toLowerCase().contains(query.toLowerCase()) ||
            (item.last4Digits?.contains(query) ?? false);
      }).toList();
    }
  }

  /// Ajouter une déclaration (Perte ou Trouvée)
  static Future<bool> addDeclaration(CniModel cni) async {
    try {
      await client.from(tableName).insert(cni.toJson());
      return true;
    } catch (e) {
      return false;
    }
  }

  /// Upload de photo CNI vers le Storage Supabase
  static Future<String?> uploadImage(File imageFile, String folder) async {
    try {
      final fileName = '${DateTime.now().millisecondsSinceEpoch}.jpg';
      final path = '$folder/$fileName';
      
      await client.storage.from('cni_photos').upload(path, imageFile);
      final publicUrl = client.storage.from('cni_photos').getPublicUrl(path);
      return publicUrl;
    } catch (e) {
      return null;
    }
  }

  /// Données de démonstration initiales
  static List<CniModel> _getDemoData() {
    return [
      CniModel(
        id: 1,
        entryType: 'FOUND',
        fullName: 'ZEBOULOUMO GUY CYRILLE',
        dob: '12/08/1988',
        last4Digits: '4821',
        location: 'Douala, Marché Central',
        description: 'CNI retrouvée près du stand de textile au Marché Central. Remise au poste de gardiennage.',
        contactPhone: '+237 699 88 77 66',
        status: 'TROUVÉE',
        isVerifiedByAuthority: true,
        userEmail: 'leslyzoyem297@gmail.com',
        createdAtFormatted: 'Il y a 2h',
        timestamp: DateTime.now().millisecondsSinceEpoch - 7200000,
      ),
      CniModel(
        id: 2,
        entryType: 'FOUND',
        fullName: 'NGONO MARIE CLAIRE',
        dob: '03/04/1995',
        last4Digits: '1097',
        location: 'Yaoundé, Mvog-Mbi',
        description: 'Carte oubliée sur un comptoir de boutique à Mvog-Mbi, Yaoundé.',
        contactPhone: '+237 677 11 22 33',
        status: 'VÉRIFIÉ',
        isVerifiedByAuthority: true,
        userEmail: 'leslyzoyem297@gmail.com',
        createdAtFormatted: 'Il y a 5h',
        timestamp: DateTime.now().millisecondsSinceEpoch - 18000000,
      ),
      CniModel(
        id: 3,
        entryType: 'LOST',
        fullName: 'KAMGA TCHINDA BERTRAND',
        dob: '18/02/1993',
        last4Digits: '5821',
        location: 'Yaoundé, Marché Mokolo',
        description: 'Portefeuille tombé lors d\'un trajet en taxi vers Mokolo.',
        contactPhone: '+237 655 00 11 22',
        status: 'EN_ATTENTE_VALIDATION',
        isVerifiedByAuthority: false,
        userEmail: 'leslyzoyem297@gmail.com',
        createdAtFormatted: 'Il y a 30 min',
        timestamp: DateTime.now().millisecondsSinceEpoch - 1800000,
      ),
    ];
  }
}
