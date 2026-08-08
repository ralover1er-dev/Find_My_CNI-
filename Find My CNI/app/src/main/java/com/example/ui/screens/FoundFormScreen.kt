package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CniViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundFormScreen(
    viewModel: CniViewModel,
    onBack: () -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    var last4Digits by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var contactPhone by rememberSaveable { mutableStateOf("") }
    var cardPhotoUri by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    val cardPhotoLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { cardPhotoUri = it.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CniBackground)
    ) {
        // Top Banner Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CniGreenPrimary)
                .padding(horizontal = 12.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Find My CNI",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "🇨🇲", fontSize = 20.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = "J'ai trouvé",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = CniTextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Poster une CNI retrouvée",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = CniTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Info Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CniGreenPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = CniGreenLight),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = CniGreenDark,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Merci de votre civisme ! Publiez la CNI trouvée pour aider son propriétaire à la récupérer rapidement.",
                        fontSize = 13.sp,
                        color = CniGreenDark,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Form
            Text(
                text = "Nom complet inscrit sur la CNI",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary
            )
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("Ex. ZEBOULOUMO GUY CYRILLE", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CniTextPrimary,
                    unfocusedTextColor = CniTextPrimary,
                    cursorColor = CniGreenPrimary,
                    focusedBorderColor = CniGreenPrimary,
                    unfocusedBorderColor = CniBorderColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, autoCorrectEnabled = false, imeAction = ImeAction.Next)
            )

            Text(
                text = "Date de naissance (si visible)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary
            )
            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                placeholder = { Text("JJ/MM/AAAA", color = Color.Gray, fontSize = 14.sp) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar",
                        tint = CniGreenPrimary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CniTextPrimary,
                    unfocusedTextColor = CniTextPrimary,
                    cursorColor = CniGreenPrimary,
                    focusedBorderColor = CniGreenPrimary,
                    unfocusedBorderColor = CniBorderColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, autoCorrectEnabled = false, imeAction = ImeAction.Next)
            )

            Text(
                text = "4 derniers chiffres de la CNI",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary
            )
            OutlinedTextField(
                value = last4Digits,
                onValueChange = { if (it.length <= 4) last4Digits = it },
                placeholder = { Text("Ex. 4821", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CniTextPrimary,
                    unfocusedTextColor = CniTextPrimary,
                    cursorColor = CniGreenPrimary,
                    focusedBorderColor = CniGreenPrimary,
                    unfocusedBorderColor = CniBorderColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, autoCorrectEnabled = false, imeAction = ImeAction.Next)
            )

            Text(
                text = "Lieu où vous l'avez trouvée",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                placeholder = { Text("Ex. Douala, Marché Central", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CniTextPrimary,
                    unfocusedTextColor = CniTextPrimary,
                    cursorColor = CniGreenPrimary,
                    focusedBorderColor = CniGreenPrimary,
                    unfocusedBorderColor = CniBorderColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, autoCorrectEnabled = false, imeAction = ImeAction.Next)
            )

            Text(
                text = "Numéro de téléphone de contact",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary
            )
            OutlinedTextField(
                value = contactPhone,
                onValueChange = { contactPhone = it },
                placeholder = { Text("Ex. +237 690 12 34 56", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CniTextPrimary,
                    unfocusedTextColor = CniTextPrimary,
                    cursorColor = CniGreenPrimary,
                    focusedBorderColor = CniGreenPrimary,
                    unfocusedBorderColor = CniBorderColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, autoCorrectEnabled = false, imeAction = ImeAction.Next)
            )

            Text(
                text = "Description / Précisions",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Trouvée sur un banc près de la gare...", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(top = 4.dp, bottom = 16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CniTextPrimary,
                    unfocusedTextColor = CniTextPrimary,
                    cursorColor = CniGreenPrimary,
                    focusedBorderColor = CniGreenPrimary,
                    unfocusedBorderColor = CniBorderColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, autoCorrectEnabled = false)
            )

            // Upload photo
            Text(
                text = "Photo de la CNI retrouvée",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            DashedUploadBox(
                borderColor = CniGreenPrimary,
                backgroundColor = CniGreenLight,
                icon = Icons.Default.PhotoCamera,
                iconColor = CniGreenPrimary,
                text = if (cardPhotoUri == null) "Prendre / Choisir une photo" else "Photo de la CNI sélectionnée ✓",
                subtext = "La photo sera automatiquement floutée pour des raisons de sécurité",
                isSelected = cardPhotoUri != null,
                onClick = { cardPhotoLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Supabase Cloud Server saving note
            Surface(
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚡ ", fontSize = 16.sp)
                    Text(
                        text = "Publié et synchronisé sur le serveur Supabase Cloud (leslyzoyem297@gmail.com)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CniTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    if (fullName.isBlank()) return@Button
                    isSubmitting = true
                    viewModel.submitFoundPost(
                        fullName = fullName,
                        dob = dob,
                        last4Digits = last4Digits,
                        location = location,
                        photoUri = cardPhotoUri ?: "simulated_card_photo_uri",
                        description = description,
                        contactPhone = contactPhone
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CniGreenPrimary),
                enabled = fullName.isNotBlank() && !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Poster la CNI trouvée",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
