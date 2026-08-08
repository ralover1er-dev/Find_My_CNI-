package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CniEntity
import com.example.ui.CniViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySubmissionsScreen(
    viewModel: CniViewModel,
    onNavigateToAdminPortal: () -> Unit = {},
    onSelectCard: (Long) -> Unit
) {
    val allEntries by viewModel.allEntries.collectAsState()
    
    // Filter for my lost complaints or items
    val mySubmissions = allEntries.filter { it.entryType == "LOST" || it.status == "EN_ATTENTE_VALIDATION" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CniBackground)
    ) {
        // Top Banner Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CniGreenPrimary)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Mes suivis de déclaration",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Suivez l'état de validation de vos plaintes par les autorités compétentes",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Server Status Badge
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = CniGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Serveur Central DGSN & Google Cloud",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CniTextPrimary
                        )
                        Text(
                            text = "Vos déclarations sont transmises directement aux services de sécurité.",
                            fontSize = 12.sp,
                            color = CniTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (mySubmissions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Aucune déclaration enregistrée pour le moment",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CniTextPrimary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(mySubmissions) { cni ->
                        SubmissionTrackerCard(
                            cni = cni,
                            onClick = { onSelectCard(cni.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubmissionTrackerCard(
    cni: CniEntity,
    onClick: () -> Unit
) {
    val (statusBg, statusText, statusIcon, textColor) = when (cni.status) {
        "APPROUVÉE" -> Quadruple(CniGreenLight, "APPROUVÉE PAR L'AUTORITÉ", Icons.Default.CheckCircle, CniGreenDark)
        "REJETÉE" -> Quadruple(CniRedLight, "DÉCLARATION REJETÉE", Icons.Default.ReportProblem, CniRedDark)
        else -> Quadruple(Color(0xFFFEF3C7), "EN ATTENTE DE VALIDATION", Icons.Default.HourglassEmpty, Color(0xFF92400E))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Plainte CNI - ${cni.fullName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CniTextPrimary
                )

                Surface(
                    color = statusBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            tint = textColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = statusText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = textColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Lieu déclaré: ${cni.location}",
                fontSize = 13.sp,
                color = CniTextSecondary
            )
            Text(
                text = "4 derniers chiffres: ${cni.last4Digits}",
                fontSize = 13.sp,
                color = CniTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CniGreenPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Voir la fiche", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
