import 'package:flutter/material.dart';
import '../models/cni_model.dart';
import '../services/supabase_service.dart';
import 'declaration_perte_screen.dart';
import 'declaration_trouve_screen.dart';
import 'card_detail_screen.dart';
import 'profile_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  List<CniModel> _allDeclarations = [];
  List<CniModel> _filteredDeclarations = [];
  bool _isLoading = true;
  String _selectedFilter = 'TOUTES'; // 'TOUTES', 'TROUVÉES', 'PERDUES'
  final TextEditingController _searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    setState(() => _isLoading = true);
    final data = await SupabaseService.getDeclarations();
    setState(() {
      _allDeclarations = data;
      _applyFilter();
      _isLoading = false;
    });
  }

  void _applyFilter() {
    final query = _searchController.text.trim().toLowerCase();
    setState(() {
      _filteredDeclarations = _allDeclarations.where((item) {
        final matchesQuery = query.isEmpty ||
            item.fullName.toLowerCase().contains(query) ||
            (item.last4Digits?.contains(query) ?? false);

        if (_selectedFilter == 'TROUVÉES') {
          return matchesQuery && item.entryType == 'FOUND';
        } else if (_selectedFilter == 'PERDUES') {
          return matchesQuery && item.entryType == 'LOST';
        }
        return matchesQuery;
      }).toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8FAFC),
      appBar: AppBar(
        backgroundColor: const Color(0xFF007A3D),
        elevation: 0,
        title: Row(
          children: [
            Container(
              padding: const EdgeInsets.all(6),
              decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.2),
                shape: BoxShape.circle,
              ),
              child: const Icon(Icons.badge_outlined, color: Color(0xFFFCD116)),
            ),
            const SizedBox(width: 10),
            const Text(
              'Find My CNI',
              style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white, fontSize: 18),
            ),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.person, color: Colors.white),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const ProfileScreen()),
              );
            },
          ),
          IconButton(
            icon: const Icon(Icons.refresh, color: Colors.white),
            onPressed: _loadData,
          ),
        ],
      ),
      body: Column(
        children: [
          // Banner Recherche
          Container(
            padding: const EdgeInsets.all(16),
            decoration: const BoxDecoration(
              color: Color(0xFF007A3D),
              borderRadius: BorderRadius.only(
                bottomLeft: Radius.circular(20),
                bottomRight: Radius.circular(20),
              ),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text(
                  'Retrouvez rapidement votre CNI au Cameroun',
                  style: TextStyle(color: Colors.white, fontSize: 13, fontWeight: FontWeight.w500),
                ),
                const SizedBox(height: 12),
                TextField(
                  controller: _searchController,
                  onChanged: (_) => _applyFilter(),
                  decoration: InputDecoration(
                    hintText: 'Rechercher par Nom ou 4 derniers chiffres...',
                    hintStyle: const TextStyle(fontSize: 13, color: Colors.grey),
                    prefixIcon: const Icon(Icons.search, color: Color(0xFF007A3D)),
                    fillColor: Colors.white,
                    filled: true,
                    contentPadding: const EdgeInsets.symmetric(vertical: 12),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(12),
                      borderSide: BorderSide.none,
                    ),
                  ),
                ),
              ],
            ),
          ),

          const SizedBox(height: 12),

          // Filtres par Onglets
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Row(
              children: [
                _buildFilterChip('TOUTES', 'Toutes'),
                const SizedBox(width: 8),
                _buildFilterChip('TROUVÉES', 'CNI Trouvées'),
                const SizedBox(width: 8),
                _buildFilterChip('PERDUES', 'CNI Perdues'),
              ],
            ),
          ),

          const SizedBox(height: 12),

          // Liste des CNI
          Expanded(
            child: _isLoading
                ? const Center(child: CircularProgressIndicator(color: Color(0xFF007A3D)))
                : _filteredDeclarations.isEmpty
                    ? Center(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(Icons.search_off, size: 64, color: Colors.grey.shade400),
                            const SizedBox(height: 12),
                            Text(
                              'Aucune CNI correspondante trouvée',
                              style: TextStyle(color: Colors.grey.shade600, fontSize: 14),
                            ),
                          ],
                        ),
                      )
                    : RefreshIndicator(
                        onRefresh: _loadData,
                        color: const Color(0xFF007A3D),
                        child: ListView.builder(
                          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                          itemCount: _filteredDeclarations.length,
                          itemBuilder: (context, index) {
                            final item = _filteredDeclarations[index];
                            return _buildCniCard(item);
                          },
                        ),
                      ),
          ),
        ],
      ),

      // Boutons d'Action Rapide : Déclaration Perte & Déclaration CNI Trouvée
      bottomNavigationBar: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: Colors.white,
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.05),
              blurRadius: 10,
              offset: const Offset(0, -4),
            )
          ],
        ),
        child: Row(
          children: [
            Expanded(
              child: ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFFCE1126),
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 14),
                  shape: RoundedCornerShape(12),
                ),
                icon: const Icon(Icons.report_problem, size: 18),
                label: const Text('Déclarer Perte', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                onPressed: () async {
                  final result = await Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const DeclarationPerteScreen()),
                  );
                  if (result == true) _loadData();
                },
              ),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFF007A3D),
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 14),
                  shape: RoundedCornerShape(12),
                ),
                icon: const Icon(Icons.add_a_photo, size: 18),
                label: const Text('Post CNI Trouvée', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                onPressed: () async {
                  final result = await Navigator.push(
                    context,
                    MaterialPageRoute(builder: (_) => const DeclarationTrouveScreen()),
                  );
                  if (result == true) _loadData();
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildFilterChip(String key, String label) {
    final isSelected = _selectedFilter == key;
    return Expanded(
      child: GestureDetector(
        onTap: () {
          setState(() {
            _selectedFilter = key;
            _applyFilter();
          });
        },
        child: Container(
          padding: const EdgeInsets.symmetric(vertical: 8),
          decoration: BoxDecoration(
            color: isSelected ? const Color(0xFF007A3D) : Colors.white,
            borderRadius: BorderRadius.circular(10),
            border: Border.all(color: isSelected ? const Color(0xFF007A3D) : Colors.grey.shade300),
          ),
          child: Center(
            child: Text(
              label,
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.bold,
                color: isSelected ? Colors.white : Colors.black87,
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildCniCard(CniModel item) {
    final isLost = item.entryType == 'LOST';
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      shape: RoundedCornerShape(16),
      elevation: 2,
      child: InkWell(
        borderRadius: BorderRadius.circular(16),
        onTap: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => CardDetailScreen(cni: item)),
          );
        },
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                    decoration: BoxDecoration(
                      color: isLost ? const Color(0xFFFEE2E2) : const Color(0xFFDCFCE7),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: Text(
                      isLost ? 'CNI PERDUE' : 'CNI TROUVÉE',
                      style: TextStyle(
                        color: isLost ? const Color(0xFF991B1B) : const Color(0xFF166534),
                        fontSize: 11,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  if (item.isVerifiedByAuthority)
                    Row(
                      children: const [
                        Icon(Icons.verified, color: Color(0xFF007A3D), size: 16),
                        SizedBox(width: 4),
                        Text(
                          'DGSN Vérifié',
                          style: TextStyle(color: Color(0xFF007A3D), fontSize: 11, fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                ],
              ),
              const SizedBox(height: 10),
              Text(
                item.fullName,
                style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.black87),
              ),
              const SizedBox(height: 6),
              Row(
                children: [
                  const Icon(Icons.numbers, size: 14, color: Colors.grey),
                  const SizedBox(width: 4),
                  Text('Fin N° CNI: *** ${item.last4Digits ?? "N/A"}', style: const TextStyle(fontSize: 12, color: Colors.grey)),
                  const SizedBox(width: 16),
                  const Icon(Icons.location_on, size: 14, color: Colors.grey),
                  const SizedBox(width: 4),
                  Expanded(
                    child: Text(
                      item.location,
                      style: const TextStyle(fontSize: 12, color: Colors.grey),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
