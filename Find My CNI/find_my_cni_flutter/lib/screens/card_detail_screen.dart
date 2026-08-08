import 'package:flutter/material.dart';
import '../models/cni_model.dart';

class CardDetailScreen extends StatelessWidget {
  final CniModel cni;

  const CardDetailScreen({Key? key, required this.cni}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final isLost = cni.entryType == 'LOST';

    return Scaffold(
      appBar: AppBar(
        title: const Text('Détails de la CNI'),
        backgroundColor: isLost ? const Color(0xFFCE1126) : const Color(0xFF007A3D),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Badge Statut
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
              decoration: BoxDecoration(
                color: isLost ? const Color(0xFFFEE2E2) : const Color(0xFFDCFCE7),
                borderRadius: BorderRadius.circular(20),
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(
                    isLost ? Icons.warning_amber : Icons.check_circle,
                    color: isLost ? const Color(0xFF991B1B) : const Color(0xFF166534),
                    size: 18,
                  ),
                  const SizedBox(width: 8),
                  Text(
                    isLost ? 'DÉCLARATION DE PERTE' : 'CNI RETROUVÉE',
                    style: TextStyle(
                      color: isLost ? const Color(0xFF991B1B) : const Color(0xFF166534),
                      fontWeight: FontWeight.bold,
                      fontSize: 12,
                    ),
                  ),
                ],
              ),
            ),

            const SizedBox(height: 16),

            // Carte d'identité visuelle
            Card(
              shape: RoundedCornerShape(16),
              elevation: 3,
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text('RÉPUBLIQUE DU CAMEROUN',
                            style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: Colors.grey)),
                        if (cni.isVerifiedByAuthority)
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                            decoration: BoxDecoration(
                              color: const Color(0xFF007A3D),
                              borderRadius: BorderRadius.circular(10),
                            ),
                            child: const Text('DGSN OK', style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                          ),
                      ],
                    ),
                    const Divider(height: 20),
                    _buildInfoRow('Nom & Prénom :', cni.fullName),
                    _buildInfoRow('Date de Naissance :', cni.dob ?? 'N/A'),
                    _buildInfoRow('Numéro CNI (Fin) :', '*** *** ${cni.last4Digits ?? "N/A"}'),
                    _buildInfoRow('Lieu enregistré :', cni.location),
                  ],
                ),
              ),
            ),

            const SizedBox(height: 16),

            // Description / Lieu de Garde
            if (cni.description != null && cni.description!.isNotEmpty)
              Card(
                shape: RoundedCornerShape(12),
                child: Padding(
                  padding: const EdgeInsets.all(14),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text('Details & Instructions :', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                      const SizedBox(height: 6),
                      Text(cni.description!, style: const TextStyle(fontSize: 13, color: Colors.black87)),
                    ],
                  ),
                ),
              ),

            const SizedBox(height: 24),

            // Contact
            SizedBox(
              width: double.infinity,
              height: 50,
              child: ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFF007A3D),
                  shape: RoundedCornerShape(12),
                ),
                icon: const Icon(Icons.phone, color: Colors.white),
                label: Text(
                  'Contacter (${cni.contactPhone})',
                  style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.white, fontSize: 14),
                ),
                onPressed: () {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text('Appel vers le ${cni.contactPhone}')),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildInfoRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 130,
            child: Text(label, style: const TextStyle(fontSize: 13, color: Colors.grey, fontWeight: FontWeight.w500)),
          ),
          Expanded(
            child: Text(value, style: const TextStyle(fontSize: 13, fontWeight: FontWeight.bold, color: Colors.black87)),
          ),
        ],
      ),
    );
  }
}
