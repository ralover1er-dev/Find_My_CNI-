import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import '../models/cni_model.dart';
import '../services/supabase_service.dart';

class DeclarationTrouveScreen extends StatefulWidget {
  const DeclarationTrouveScreen({Key? key}) : super(key: key);

  @override
  State<DeclarationTrouveScreen> createState() => _DeclarationTrouveScreenState();
}

class _DeclarationTrouveScreenState extends State<DeclarationTrouveScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _dobController = TextEditingController();
  final _digitsController = TextEditingController();
  final _locationController = TextEditingController();
  final _phoneController = TextEditingController();
  final _descController = TextEditingController();

  File? _cniPhoto;
  bool _isSubmitting = false;

  Future<void> _pickImage() async {
    final picker = ImagePicker();
    final picked = await picker.pickImage(source: ImageSource.gallery, imageQuality: 70);
    if (picked != null) {
      setState(() => _cniPhoto = File(picked.path));
    }
  }

  Future<void> _submitForm() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isSubmitting = true);

    String? imageUrl;
    if (_cniPhoto != null) {
      imageUrl = await SupabaseService.uploadImage(_cniPhoto!, 'cni_trouvees');
    }

    final newCni = CniModel(
      entryType: 'FOUND',
      fullName: _nameController.text.trim().toUpperCase(),
      dob: _dobController.text.trim(),
      last4Digits: _digitsController.text.trim(),
      location: _locationController.text.trim(),
      photoUri: imageUrl,
      description: _descController.text.trim(),
      contactPhone: _phoneController.text.trim(),
      status: 'TROUVÉE',
      isVerifiedByAuthority: false,
      userEmail: 'citoyen.deposant@cni.cm',
      createdAtFormatted: 'À l\'instant',
      timestamp: DateTime.now().millisecondsSinceEpoch,
    );

    final success = await SupabaseService.addDeclaration(newCni);

    setState(() => _isSubmitting = false);

    if (mounted) {
      if (success) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Post CNI retrouvée publié sur Supabase ! Merci pour votre civisme.'),
            backgroundColor: Color(0xFF007A3D),
          ),
        );
        Navigator.pop(context, true);
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Erreur lors de la publication. Réessayez.'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Poster une CNI Retrouvée'),
        backgroundColor: const Color(0xFF007A3D),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                'Informations figurant sur la CNI retrouvée',
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Color(0xFF007A3D)),
              ),
              const SizedBox(height: 12),

              TextFormField(
                controller: _nameController,
                style: const TextStyle(color: Colors.black87),
                decoration: _inputDecoration('Nom et Prénom (tels qu\'écrits sur la CNI)'),
                validator: (v) => v == null || v.isEmpty ? 'Champ obligatoire' : null,
              ),
              const SizedBox(height: 12),

              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: _dobController,
                      style: const TextStyle(color: Colors.black87),
                      decoration: _inputDecoration('Date de Naissance'),
                      validator: (v) => v == null || v.isEmpty ? 'Champ requis' : null,
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: TextFormField(
                      controller: _digitsController,
                      keyboardType: TextInputType.number,
                      maxLength: 4,
                      style: const TextStyle(color: Colors.black87),
                      decoration: _inputDecoration('4 derniers chiffres CNI'),
                      validator: (v) => v == null || v.length != 4 ? '4 chiffres ex: 1097' : null,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),

              TextFormField(
                controller: _locationController,
                style: const TextStyle(color: Colors.black87),
                decoration: _inputDecoration('Lieu exact de découverte (ex: Yaoundé, Mvog-Mbi)'),
                validator: (v) => v == null || v.isEmpty ? 'Champ obligatoire' : null,
              ),
              const SizedBox(height: 12),

              TextFormField(
                controller: _phoneController,
                keyboardType: TextInputType.phone,
                style: const TextStyle(color: Colors.black87),
                decoration: _inputDecoration('Numéro du déposant / commissariat (+237...)'),
                validator: (v) => v == null || v.isEmpty ? 'Champ obligatoire' : null,
              ),
              const SizedBox(height: 12),

              TextFormField(
                controller: _descController,
                maxLines: 2,
                style: const TextStyle(color: Colors.black87),
                decoration: _inputDecoration('Lieu de garde de la CNI / Instructions'),
              ),
              const SizedBox(height: 16),

              const Text('Photo de la CNI (Flouter le numéro de pièce si possible)',
                  style: TextStyle(fontSize: 13, fontWeight: FontWeight.w600)),
              const SizedBox(height: 8),

              InkWell(
                onTap: _pickImage,
                child: Container(
                  height: 140,
                  width: double.infinity,
                  decoration: BoxDecoration(
                    color: Colors.grey.shade100,
                    borderRadius: BorderRadius.circular(12),
                    border: Border.all(color: Colors.grey.shade300),
                  ),
                  child: _cniPhoto != null
                      ? ClipRRect(
                          borderRadius: BorderRadius.circular(12),
                          child: Image.file(_cniPhoto!, fit: BoxFit.cover),
                        )
                      : Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: const [
                            Icon(Icons.camera_alt, color: Color(0xFF007A3D), size: 40),
                            SizedBox(height: 6),
                            Text('Prendre en photo / Importer la CNI', style: TextStyle(color: Colors.grey, fontSize: 12)),
                          ],
                        ),
                ),
              ),

              const SizedBox(height: 24),

              SizedBox(
                width: double.infinity,
                height: 50,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFF007A3D),
                    shape: RoundedCornerShape(12),
                  ),
                  onPressed: _isSubmitting ? null : _submitForm,
                  child: _isSubmitting
                      ? const CircularProgressIndicator(color: Colors.white)
                      : const Text('PUBLIER LA CNI TROUVÉE', style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold, color: Colors.white)),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  InputDecoration _inputDecoration(String label) {
    return InputDecoration(
      labelText: label,
      labelStyle: const TextStyle(fontSize: 12, color: Colors.black54),
      filled: true,
      fillColor: Colors.white,
      contentPadding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
      border: OutlineInputBorder(borderRadius: BorderRadius.circular(10), borderSide: BorderSide(color: Colors.grey.shade300)),
      enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(10), borderSide: BorderSide(color: Colors.grey.shade300)),
      focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(10), borderSide: const BorderSide(color: Color(0xFF007A3D))),
    );
  }
}
