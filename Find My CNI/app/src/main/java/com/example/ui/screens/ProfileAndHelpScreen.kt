package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.CniViewModel
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: CniViewModel,
    onNavigateToMySubmissions: () -> Unit
) {
    val googleEmail by viewModel.userGoogleEmail.collectAsState()
    val googleName by viewModel.userGoogleName.collectAsState()
    val isSyncEnabled by viewModel.isGoogleSyncEnabled.collectAsState()
    val isSyncing by viewModel.isSyncingServer.collectAsState()
    val lastSync by viewModel.lastSyncFormatted.collectAsState()
    val allEntries by viewModel.allEntries.collectAsState()

    val myDeclarationsCount = allEntries.count { it.userEmail == googleEmail }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CniBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Profile Card with Default Google Account
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(CniGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "LZ",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = googleName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CniTextPrimary
                )

                Text(
                    text = "Compte Citoyen Camerounais",
                    fontSize = 13.sp,
                    color = CniTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Default Google Badge
                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF4285F4),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "G",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Compte Google par défaut",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                text = googleEmail,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CniTextPrimary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Actif",
                            tint = CniGreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Supabase Cloud Server Sync Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = CniGreenPrimary.copy(alpha = 0.12f),
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = "Supabase Cloud",
                                tint = CniGreenPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Serveur Supabase Cloud",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = CniTextPrimary
                        )
                        Text(
                            text = "Base de données centralisée: cni_declarations",
                            fontSize = 12.sp,
                            color = CniTextSecondary
                        )
                    }
                    Switch(
                        checked = isSyncEnabled,
                        onCheckedChange = { viewModel.toggleGoogleSync(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = CniGreenPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = CniBorderColor)
                Spacer(modifier = Modifier.height(14.dp))

                // Connection details badge
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = CniGreenPrimary,
                                modifier = Modifier.size(8.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Statut : Connecté au serveur Supabase",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CniGreenPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "URL: https://findmycni-cm.supabase.co",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Table: public.cni_declarations",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Dernière synchro : ",
                        fontSize = 13.sp,
                        color = CniTextSecondary
                    )
                    Text(
                        text = lastSync,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CniTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = { viewModel.syncNowWithGoogleServer() },
                    enabled = !isSyncing,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CniGreenPrimary
                    )
                ) {
                    if (isSyncing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = CniGreenPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Synchronisation Supabase...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Synchroniser avec le Serveur Supabase",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Administrative Office Architecture Explanation Card
                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🏛️ ", fontSize = 16.sp)
                            Text(
                                text = "Validation dans les Bureaux Administratifs",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Toutes les déclarations enregistrées sur cette application sont envoyées au serveur Supabase. L'application Admin (réservée exclusivement aux agents dans les bureaux administratifs de la Police) est connectée à cette même base de données Supabase. L'Admin y valide ou rejette les déclarations officielles de perte.",
                            fontSize = 11.sp,
                            color = Color(0xFF78350F),
                            lineHeight = 16.sp
                        )
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Saved Declarations Stats Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Mes Déclarations sur le Serveur",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CniTextPrimary
                        )
                        Text(
                            text = "Liées à $googleEmail",
                            fontSize = 12.sp,
                            color = CniTextSecondary
                        )
                    }

                    Surface(
                        color = CniGreenPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "$myDeclarationsCount dossier(s)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CniGreenPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onNavigateToMySubmissions,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CniGreenPrimary)
                ) {
                    Text(
                        text = "Consulter mes déclarations enregistrées",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun HelpScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CniBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Aide & Assistance WhatsApp",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CniTextPrimary
        )

        Text(
            text = "Besoin d'aide pour retrouver votre CNI ou déclarer une perte ?",
            fontSize = 14.sp,
            color = CniTextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        tint = CniGreenPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Centre d'Assistance CNI",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CniTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Nos agents sont disponibles en direct via vidéo ou messagerie WhatsApp pour vous assister dans l'authentification et la restitution sécurisée des pièces d'identité.",
                    fontSize = 14.sp,
                    color = CniTextSecondary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=237690000000&text=Bonjour,%20j'ai%20besoin%20d'aide%20sur%20FindMyCNI")
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+237690000000"))
                            context.startActivity(callIntent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CniGreenPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Contacter un agent WhatsApp",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
