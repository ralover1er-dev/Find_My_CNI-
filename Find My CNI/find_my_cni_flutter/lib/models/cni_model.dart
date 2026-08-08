class CniModel {
  final int? id;
  final String entryType; // 'LOST' or 'FOUND'
  final String fullName;
  final String? dob;
  final String? last4Digits;
  final String location;
  final String? declarationDocUri;
  final String? selfieUri;
  final String? photoUri;
  final String? description;
  final String contactPhone;
  final String status; // 'TROUVÉE', 'PERDUE', 'VÉRIFIÉ', 'EN_ATTENTE_VALIDATION'
  final bool isVerifiedByAuthority;
  final String userEmail;
  final String createdAtFormatted;
  final int timestamp;

  CniModel({
    this.id,
    required this.entryType,
    required this.fullName,
    this.dob,
    this.last4Digits,
    required this.location,
    this.declarationDocUri,
    this.selfieUri,
    this.photoUri,
    this.description,
    required this.contactPhone,
    required this.status,
    this.isVerifiedByAuthority = false,
    required this.userEmail,
    required this.createdAtFormatted,
    required this.timestamp,
  });

  factory CniModel.fromJson(Map<String, dynamic> json) {
    return CniModel(
      id: json['id'] is int ? json['id'] : int.tryParse(json['id']?.toString() ?? ''),
      entryType: json['entry_type'] ?? 'FOUND',
      fullName: json['full_name'] ?? '',
      dob: json['dob'],
      last4Digits: json['last_4_digits'],
      location: json['location'] ?? 'Cameroun',
      declarationDocUri: json['declaration_doc_uri'],
      selfieUri: json['selfie_uri'],
      photoUri: json['photo_uri'],
      description: json['description'],
      contactPhone: json['contact_phone'] ?? '+237 600 00 00 00',
      status: json['status'] ?? 'EN_ATTENTE_VALIDATION',
      isVerifiedByAuthority: json['is_verified_by_authority'] ?? false,
      userEmail: json['user_email'] ?? 'utilisateur@cni.cm',
      createdAtFormatted: json['created_at_formatted'] ?? 'Récemment',
      timestamp: json['timestamp'] is int
          ? json['timestamp']
          : (int.tryParse(json['timestamp']?.toString() ?? '') ?? DateTime.now().millisecondsSinceEpoch),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      'entry_type': entryType,
      'full_name': fullName,
      'dob': dob,
      'last_4_digits': last4Digits,
      'location': location,
      'declaration_doc_uri': declarationDocUri,
      'selfie_uri': selfieUri,
      'photo_uri': photoUri,
      'description': description,
      'contact_phone': contactPhone,
      'status': status,
      'is_verified_by_authority': isVerifiedByAuthority,
      'user_email': userEmail,
      'created_at_formatted': createdAtFormatted,
      'timestamp': timestamp,
    };
  }
}
