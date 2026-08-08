package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun LostFormScreen(
    viewModel: CniViewModel,
    onBack: () -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    var last4Digits by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    
    var docPhotoUri by remember { mutableStateOf<String?>(null) }
    var selfiePhotoUri by remember { mutableStateOf<String?>(null) }

    var isSubmitting by remember { mutableStateOf(false) }

    // Simulating file pickers
    val docLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { docPhotoUri = it.toString() }
    }
    val selfieLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selfiePhotoUri = it.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CniBackground)
    ) {
        // Top Banner Green Bar
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

            // Main Title
            Text(
                text = "J'ai perdu",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = CniTextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Déclarer une CNI perdue",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = CniTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Red Info Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CniRedPrimary.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = CniRedLight),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = CniRedDark,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Signalez la perte de votre CNI pour demander un remplacement ou bloquer le document",
                        fontSize = 13.sp,
                        color = CniRedDark,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Form Fields
            Text(
                text = "Nom complet",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary
            )
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("Ex. Jean Dupont", color = Color.Gray, fontSize = 14.sp) },
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
                text = "Date de naissance",
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
                text = "4 derniers chiffres CNI",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary
            )
            OutlinedTextField(
                value = last4Digits,
                onValueChange = { if (it.length <= 4) last4Digits = it },
                placeholder = { Text("____", color = Color.Gray, fontSize = 14.sp) },
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
                text = "Lieu de perte",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                placeholder = { Text("Ex. Yaoundé, Marché Mokolo", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
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
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, autoCorrectEnabled = false, imeAction = ImeAction.Done)
            )

            // Upload Photo déclaration de perte
            Text(
                text = "Upload Photo déclaration de perte",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CniTextPrimary,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            DashedUploadBox(
                borderColor = CniRedPrimary,
                backgroundColor = CniRedLight,
                icon = Icons.Default.CloudUpload,
                iconColor = CniRedPrimary,
                text = if (docPhotoUri == null) "Ajouter la photo de la déclaration" else "Photo ajoutée ✓",
                subtext = "JPG, PNG — max 5 Mo",
                isSelected = docPhotoUri != null,
                onClick = { docLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Upload Photo selfie
            Text(
                text = "Upload Photo selfie",
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
                text = if (selfiePhotoUri == null) "Ajouter votre selfie" else "Selfie ajouté ✓",
                subtext = "Pour vérification d'identité — visage visible",
                isSelected = selfiePhotoUri != null,
                onClick = { selfieLauncher.launch("image/*") }
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
                        text = "Transmis au serveur Supabase Cloud (leslyzoyem297@gmail.com)",
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
                    viewModel.submitLostDeclaration(
                        fullName = fullName,
                        dob = dob,
                        last4Digits = last4Digits,
                        location = location,
                        declarationDocUri = docPhotoUri ?: "simulated_doc_uri",
                        selfieUri = selfiePhotoUri ?: "simulated_selfie_uri"
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
                        text = "Soumettre pour validation",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Text(
                text = "Vos informations sont protégées conformément à la loi camerounaise sur les données personnelles",
                fontSize = 11.sp,
                color = CniTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 20.dp)
            )
        }
    }
}

@Composable
fun DashedUploadBox(
    borderColor: Color,
    backgroundColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    text: String,
    subtext: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .drawWithContent {
                drawContent()
                val stroke = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                )
                drawRoundRect(
                    color = borderColor,
                    style = stroke
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = if (isSelected) Icons.Outlined.Check else icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = iconColor
            )
            Text(
                text = subtext,
                fontSize = 11.sp,
                color = CniTextSecondary
            )
        }
    }
}
