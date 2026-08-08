package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CniViewModel
import com.example.ui.screens.*
import com.example.ui.theme.CniBackground
import com.example.ui.theme.CniGreenPrimary
import com.example.ui.theme.FindMyCniTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: CniViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FindMyCniTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: CniViewModel) {
    var isSplashVisible by remember { mutableStateOf(true) }

    if (isSplashVisible) {
        SplashScreen(onSplashFinished = { isSplashVisible = false })
        return
    }

    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedCardId by viewModel.selectedCardId.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(msg)
                viewModel.clearSnackbar()
            }
        }
    }

    // Active bottom navigation item determination
    val selectedBottomTab = when (currentScreen) {
        "HOME" -> "HOME"
        "MY_SUBMISSIONS" -> "MY_SUBMISSIONS"
        "HELP" -> "HELP"
        "PROFILE" -> "PROFILE"
        else -> "HOME"
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            // Show bottom navigation bar when on primary tab screens
            if (currentScreen in listOf("HOME", "SEARCH_RESULTS", "MY_SUBMISSIONS", "HELP", "PROFILE")) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = CniGreenPrimary,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    // Accueil / Rechercher
                    NavigationBarItem(
                        selected = selectedBottomTab == "HOME" && currentScreen == "HOME",
                        onClick = { viewModel.navigateTo("HOME") },
                        icon = {
                            Icon(
                                imageVector = if (selectedBottomTab == "HOME") Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Accueil"
                            )
                        },
                        label = {
                            Text(
                                text = "Accueil",
                                fontSize = 12.sp,
                                fontWeight = if (selectedBottomTab == "HOME") FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CniGreenPrimary,
                            selectedTextColor = CniGreenPrimary,
                            indicatorColor = Color(0xFFE6F7EF),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    // Mes suivis / Aide
                    NavigationBarItem(
                        selected = selectedBottomTab == "MY_SUBMISSIONS" || currentScreen == "HELP",
                        onClick = { viewModel.navigateTo("MY_SUBMISSIONS") },
                        icon = {
                            Icon(
                                imageVector = if (selectedBottomTab == "MY_SUBMISSIONS") Icons.Filled.ListAlt else Icons.Outlined.ListAlt,
                                contentDescription = "Mes suivis"
                            )
                        },
                        label = {
                            Text(
                                text = "Mes suivis",
                                fontSize = 12.sp,
                                fontWeight = if (selectedBottomTab == "MY_SUBMISSIONS") FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CniGreenPrimary,
                            selectedTextColor = CniGreenPrimary,
                            indicatorColor = Color(0xFFE6F7EF),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_item_submissions")
                    )

                    // Profil
                    NavigationBarItem(
                        selected = selectedBottomTab == "PROFILE",
                        onClick = { viewModel.navigateTo("PROFILE") },
                        icon = {
                            Icon(
                                imageVector = if (selectedBottomTab == "PROFILE") Icons.Filled.Person else Icons.Outlined.Person,
                                contentDescription = "Profil"
                            )
                        },
                        label = {
                            Text(
                                text = "Profil",
                                fontSize = 12.sp,
                                fontWeight = if (selectedBottomTab == "PROFILE") FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CniGreenPrimary,
                            selectedTextColor = CniGreenPrimary,
                            indicatorColor = Color(0xFFE6F7EF),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_item_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                "HOME" -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToLost = { viewModel.navigateTo("LOST_FORM") },
                        onNavigateToFound = { viewModel.navigateTo("FOUND_FORM") },
                        onNavigateToSearch = { viewModel.navigateTo("SEARCH_RESULTS") },
                        onNavigateToHelp = { viewModel.navigateTo("HELP") }
                    )
                }

                "LOST_FORM" -> {
                    LostFormScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo("HOME") }
                    )
                }

                "FOUND_FORM" -> {
                    FoundFormScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo("HOME") }
                    )
                }

                "SEARCH_RESULTS" -> {
                    SearchResultsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo("HOME") },
                        onSelectCard = { id -> viewModel.selectCard(id) }
                    )
                }

                "CARD_DETAIL" -> {
                    selectedCardId?.let { id ->
                        CardDetailScreen(
                            viewModel = viewModel,
                            cardId = id,
                            onBack = { viewModel.navigateTo("SEARCH_RESULTS") }
                        )
                    } ?: viewModel.navigateTo("HOME")
                }

                "MY_SUBMISSIONS" -> {
                    MySubmissionsScreen(
                        viewModel = viewModel,
                        onNavigateToAdminPortal = { viewModel.navigateTo("ADMIN_PORTAL") },
                        onSelectCard = { id -> viewModel.selectCard(id) }
                    )
                }

                "ADMIN_PORTAL" -> {
                    AdminPortalScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo("MY_SUBMISSIONS") }
                    )
                }

                "HELP" -> {
                    HelpScreen()
                }

                "PROFILE" -> {
                    ProfileScreen(
                        viewModel = viewModel,
                        onNavigateToMySubmissions = { viewModel.navigateTo("MY_SUBMISSIONS") }
                    )
                }
            }
        }
    }
}
