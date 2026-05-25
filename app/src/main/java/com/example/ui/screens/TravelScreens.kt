package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.local.BookingEntity
import com.example.data.local.ReviewEntity
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.TravelNotification
import com.example.ui.viewmodel.TravelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelAppMainScreen(viewModel: TravelViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentNavTab.collectAsStateWithLifecycle()
    val unreadNotifsCount by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()

    // Enforce Layout direction on the outer skeleton
    val layoutDirection = if (language == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Place,
                                contentDescription = "Logo",
                                tint = EthioGreen,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == "ar") "إيثيو تور" else "EthioTour",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    actions = {
                        // Quick Language select button
                        IconButton(
                            onClick = {
                                viewModel.setLanguage(if (language == "ar") "en" else "ar")
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(36.dp)
                                .widthIn(min = 48.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = if (language == "ar") "EN" else "عربي",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        // Notifications Icon with badge
                        IconButton(onClick = { viewModel.navigateTo("NOTIFICATIONS") }) {
                            BadgedBox(
                                badge = {
                                    if (unreadNotifsCount > 0) {
                                        Badge(containerColor = EthioRed) {
                                            Text(unreadNotifsCount.toString(), color = Color.White)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications"
                                )
                            }
                        }

                        // Bookmark Favorites Quick Filter Tab
                        IconButton(onClick = { viewModel.navigateTo("FAVORITES") }) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorites",
                                tint = EthioRed
                            )
                        }

                        // Personal Profile / Customizer Tab Button
                        IconButton(onClick = { viewModel.navigateTo("PROFILE") }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile Preferences & Customizer",
                                tint = EthioGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ) {
                    NavigationBarItem(
                        selected = currentTab == "EXPLORE" || currentTab == "DETAILS",
                        onClick = { viewModel.navigateTo("EXPLORE") },
                        label = { Text(if (language == "ar") "اكتشف" else "Explore") },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Explore") }
                    )
                    NavigationBarItem(
                        selected = currentTab == "BOOKINGS",
                        onClick = { viewModel.navigateTo("BOOKINGS") },
                        label = { Text(if (language == "ar") "الحجوزات" else "Bookings") },
                        icon = { Icon(Icons.Filled.DateRange, contentDescription = "Bookings") }
                    )
                    NavigationBarItem(
                        selected = currentTab == "MAP",
                        onClick = { viewModel.navigateTo("MAP") },
                        label = { Text(if (language == "ar") "الخريطة" else "Map") },
                        icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Map") }
                    )
                    NavigationBarItem(
                        selected = currentTab == "AI_CHAT",
                        onClick = { viewModel.navigateTo("AI_CHAT") },
                        label = { Text(if (language == "ar") "المرشد الذكي" else "AI Guide") },
                        icon = { Icon(Icons.Filled.Face, contentDescription = "AI Guide") }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (currentTab) {
                    "EXPLORE" -> ExploreScreenTab(viewModel)
                    "DETAILS" -> DestinationDetailScreen(viewModel)
                    "BOOKINGS" -> BookingsScreenTab(viewModel)
                    "MAP" -> InteractiveMapScreenTab(viewModel)
                    "AI_CHAT" -> AIChatScreenTab(viewModel)
                    "NOTIFICATIONS" -> NotificationsScreenTab(viewModel)
                    "FAVORITES" -> FavoritesScreenTab(viewModel)
                    "PROFILE" -> ProfileScreenTab(viewModel)
                    else -> ExploreScreenTab(viewModel)
                }

                // Global secure Checkout Modal Dialog
                CheckoutPaymentDialog(viewModel)
            }
        }
    }
}

// ==========================================
// SCREEN 1: EXPLORE / HOME TAB
// ==========================================
@Composable
fun ExploreScreenTab(viewModel: TravelViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedRegionFilter by viewModel.filterAddisOnly.collectAsStateWithLifecycle()
    val filteredDestinations by viewModel.filteredDestinations.collectAsStateWithLifecycle()
    
    // Core Subsections state
    var activeSubTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Hero Header Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            EthioGreen.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    text = if (language == "ar") "اكتشف سحر إثيوبيا الفريد" else "Explore Mystical Ethiopia",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    ),
                    color = EthioGreen
                )
                Text(
                    text = if (language == "ar") "منتجعات فاخرة، مسارات تفصيلية، وبانوراما ٣٦٠ درجة غامرة" else "Luxury lakeside resorts, complete suggested routes, and interactive media",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        // Sub-tabs Selection Segment
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf(
                Pair(if (language == "ar") "🌍 المعالم والمدن" else "🌍 Landmarks", 0),
                Pair(if (language == "ar") "🗺️ مسارات سياحية" else "🗺️ Suggested Itineraries", 1),
                Pair(if (language == "ar") "🏖️ منتجعات العاصمة" else "🏖️ Capital Resorts", 2),
                Pair(if (language == "ar") "📸 وسائط غامرة 360°" else "📸 Immersive 360°", 3)
            )
            items(tabs) { tab ->
                val isSelected = activeSubTab == tab.second
                FilterChip(
                    selected = isSelected,
                    onClick = { activeSubTab = tab.second },
                    label = { Text(tab.first, fontWeight = FontWeight.SemiBold, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EthioGreen,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Content Display based on activeSubTab
        when (activeSubTab) {
            0 -> {
                // LANDMARKS / ORIGINAL ALL PLACES
                // Search Bar Row
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text(if (language == "ar") "ابحث عن معالم، قلاع، كنائس، فنادق..." else "Search places, castles, paths, guides...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "SearchIcon") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EthioGreen,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Advanced filter chips (Inside Addis vs Outside Addis vs All)
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedRegionFilter == null,
                            onClick = { viewModel.setFilterAddis(null) },
                            label = { Text(if (language == "ar") "كل المعالم 🌍" else "All Landmarks 🌍") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EthioGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedRegionFilter == true,
                            onClick = { viewModel.setFilterAddis(true) },
                            label = { Text(if (language == "ar") "داخل العاصمة (أديس أبابا) 🏙️" else "Inside Addis Ababa 🏙️") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EthioGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedRegionFilter == false,
                            onClick = { viewModel.setFilterAddis(false) },
                            label = { Text(if (language == "ar") "خارج العاصمة (الولايات والأقاليم) 🏞️" else "Outside Capital (Regions) 🏞️") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EthioGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Destinations grid/list
                if (filteredDestinations.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Empty",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (language == "ar") "عذراً، لم نجد نتائج تطابق بحثك!" else "No matching landmarks found!",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredDestinations) { destination ->
                            DestinationCard(destination, viewModel, language)
                        }
                    }
                }
            }
            1 -> {
                SuggestedItinerariesView(viewModel, language)
            }
            2 -> {
                CapitalResortsView(viewModel, language)
            }
            3 -> {
                ImmersiveMediaView(viewModel, language)
            }
        }
    }
}

@Composable
fun DestinationCard(dest: Destination, viewModel: TravelViewModel, language: String) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val isFav = favorites.any { it.placeId == dest.id }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.selectDestination(dest) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
    ) {
        Column {
            // Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                ImageWithFallback(
                    imageUrl = dest.imageUrl,
                    contentDescription = dest.nameEn,
                    modifier = Modifier.fillMaxSize()
                )

                // Ethiopia Flag Accent (Subtle Ribbon at top corner)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(EthioGreen, EthioYellow, EthioRed)
                            ),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (dest.inAddis) {
                            if (language == "ar") "العاصمة 🏙️" else "Addis Ababa 🏙️"
                        } else {
                            if (language == "ar") "الولايات 🏞️" else "Regions 🏞️"
                        },
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // Favorite Toggle button on card
                IconButton(
                    onClick = { viewModel.toggleFavorite(dest.id) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.35f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "FavToggle",
                        tint = if (isFav) EthioRed else Color.White
                    )
                }

                // Visual Gradient shadow on image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                )

                // Title overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (language == "ar") dest.nameAr else dest.nameEn,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = "LocPin", modifier = Modifier.size(14.dp), tint = EthioYellow)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == "ar") dest.locationAr else dest.locationEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Description summary & rating row at bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == "ar") dest.descriptionAr else dest.descriptionEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Score block
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = "Star", tint = EthioYellow, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = dest.initialRating.toString(),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = if (language == "ar") "تفاصيل ➜" else "Details ➜",
                        color = EthioGreen,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun ImageWithFallback(imageUrl: String, contentDescription: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Background fallback decorative gradient in case image fails to fetch
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                PassportDarkBG,
                                PassportDarkSurface
                            )
                        )
                    )
                    // Draw cultural flag lines in canvas
                    val h = size.height
                    val w = size.width
                    drawRect(color = EthioGreen.copy(alpha = 0.1f), size = size.copy(height = h * 0.33f))
                    drawRect(
                        color = EthioYellow.copy(alpha = 0.1f),
                        topLeft = Offset(0f, h * 0.33f),
                        size = size.copy(height = h * 0.33f)
                    )
                    drawRect(
                        color = EthioRed.copy(alpha = 0.1f),
                        topLeft = Offset(0f, h * 0.66f),
                        size = size.copy(height = h * 0.34f)
                    )
                }
        )
    }
}

// ==========================================
// SCREEN 1B: DETAILED VIEW WITH DIRECTION & REVIEWS
// ==========================================
@Composable
fun DestinationDetailScreen(viewModel: TravelViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val dest by viewModel.selectedDestination.collectAsStateWithLifecycle()

    val reviews by viewModel.activeReviews.collectAsStateWithLifecycle()

    // Temporary values for user reviews input
    var commentText by remember { mutableStateOf("") }
    var userNameText by remember { mutableStateOf("") }
    var userRating by remember { mutableFloatStateOf(5f) }

    if (dest == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Destination selected!")
        }
        return
    }

    val finalDest = dest!!

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            // Header Image Backdrops
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                ImageWithFallback(imageUrl = finalDest.imageUrl, contentDescription = finalDest.nameEn, modifier = Modifier.fillMaxSize())

                // Custom Transparent Overlays
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.Black.copy(alpha = 0.8f))
                            )
                        )
                )

                // Back Button
                IconButton(
                    onClick = { viewModel.navigateTo("EXPLORE") },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.45f), shape = CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                // Heading Overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (language == "ar") finalDest.nameAr else finalDest.nameEn,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = "PinOnDetail", tint = EthioYellow, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == "ar") finalDest.locationAr else finalDest.locationEn,
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Location & Price Stats Row
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == "ar") "تذكرة الدخول الأساسية" else "Base Ticket Price",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = if (finalDest.baseTicketPriceUSD == 0.0) {
                                if (language == "ar") "مجاني" else "Free"
                            } else {
                                "$${finalDest.baseTicketPriceUSD}"
                            },
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = EthioGreen
                            )
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (language == "ar") "أفضل وقت للزيارة" else "Best Time to Visit",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = if (language == "ar") finalDest.bestTimeToVisitAr else finalDest.bestTimeToVisitEn,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = EthioBlue
                        )
                    }
                }
            }
        }

        // Full Description Card
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = if (language == "ar") "نبذة تاريخية وتفاصيل" else "About & Overview",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (language == "ar") finalDest.descriptionAr else finalDest.descriptionEn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    lineHeight = 22.sp
                )
            }
        }

        // How To Go / Transportation Guide (المواصلات وكيفية الذهاب)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                border = BorderStroke(1.dp, EthioYellow.copy(alpha = 0.4f)),
                colors = CardDefaults.cardColors(containerColor = EthioYellow.copy(alpha = 0.04f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, contentDescription = "Transit", tint = EthioGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == "ar") "كيفية الذهاب والوصول للمكان" else "How to Get There & Transit",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EthioGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == "ar") finalDest.howToGoAr else finalDest.howToGoEn,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Icon(Icons.Filled.Info, contentDescription = "#", modifier = Modifier.size(14.dp), tint = EthioRed)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == "ar") finalDest.distanceTextAr else finalDest.distanceTextEn,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = EthioRed
                        )
                    }
                }
            }
        }

        // Reviews Section
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == "ar") "الآراء والتعليقات الحقيقية ⭐" else "Visitor Reviews & Ratings ⭐",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Post a review Mini card form
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == "ar") "أضف تعليقك وتقييمك الشخصي" else "Add Your Rating and Comment",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Score selecting stars UI
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(if (language == "ar") "التقييم: " else "Rating: ", style = MaterialTheme.typography.bodySmall)
                            (1..5).forEach { star ->
                                IconButton(
                                    onClick = { userRating = star.toFloat() },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Star$star",
                                        tint = if (star <= userRating) EthioYellow else Color.LightGray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        // Name field
                        OutlinedTextField(
                            value = userNameText,
                            onValueChange = { userNameText = it },
                            placeholder = { Text(if (language == "ar") "اسمك الكريم (اختياري)" else "Your Name (Optional)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Comment text area
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            placeholder = { Text(if (language == "ar") "اكتب تجربتك في زيارة هذا المعلم الجميل..." else "Write about your experience visiting this landmark...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            maxLines = 3,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.postReview(
                                    finalDest.id,
                                    userNameText,
                                    commentText,
                                    userRating
                                )
                                // Clear fields
                                userNameText = ""
                                commentText = ""
                                userRating = 5f
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                            modifier = Modifier.fillMaxWidth().height(44.dp)
                        ) {
                            Text(if (language == "ar") "إرسال التقييم بأمان" else "Submit Secure Review")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // List of Reviews
                if (reviews.isEmpty()) {
                    Text(
                        text = if (language == "ar") "لا توجد تعليقات إضافية بعد. كن أول من يضع تقييماً!" else "No user reviews yet. Be the first to add one!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        reviews.forEach { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.userName,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Row {
                                            (1..5).forEach { star ->
                                                Icon(
                                                    imageVector = Icons.Filled.Star,
                                                    contentDescription = "star",
                                                    tint = if (star <= item.rating) EthioYellow else Color.LightGray,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.comment,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 2: INTEGRATED BOOKINGS TAB
// ==========================================
@Composable
fun BookingsScreenTab(viewModel: TravelViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    var selectedServiceType by remember { mutableStateOf("HOTELS") } // HOTELS or FLIGHTS or MY_TRIPS

    val activeBookings by viewModel.bookings.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        // Toggle Buttons row at top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(4.dp)
        ) {
            Button(
                onClick = { selectedServiceType = "HOTELS" },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedServiceType == "HOTELS") EthioGreen else Color.Transparent,
                    contentColor = if (selectedServiceType == "HOTELS") Color.White else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    if (language == "ar") "🏨 فنادق" else "🏨 Hotels",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = { @Suppress("UNUSED_EXPRESSION") selectedServiceType = "FLIGHTS" },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedServiceType == "FLIGHTS") EthioGreen else Color.Transparent,
                    contentColor = if (selectedServiceType == "FLIGHTS") Color.White else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    if (language == "ar") "✈️ طيران" else "✈️ Flights",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = { selectedServiceType = "MY_TRIPS" },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedServiceType == "MY_TRIPS") EthioGreen else Color.Transparent,
                    contentColor = if (selectedServiceType == "MY_TRIPS") Color.White else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    if (language == "ar") "💼 حـجوزاتي" else "💼 My Trips",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        when (selectedServiceType) {
            "HOTELS" -> HotelsBookingFeed(viewModel, language)
            "FLIGHTS" -> FlightsBookingFeed(viewModel, language)
            "MY_TRIPS" -> MyTripsListFeed(activeBookings, language)
        }
    }
}

@Composable
fun HotelsBookingFeed(viewModel: TravelViewModel, language: String) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(TravelData.hotels) { hotel ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    ImageWithFallback(
                        imageUrl = hotel.imageUrl,
                        contentDescription = hotel.nameEn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == "ar") hotel.nameAr else hotel.nameEn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.weight(1f)
                            )
                            Row {
                                Icon(Icons.Filled.Star, contentDescription = "*", tint = EthioYellow, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(hotel.starRating.toString(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Pin", tint = Color.Gray, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == "ar") hotel.locationAr else hotel.locationEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Amenities tags
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val list = if (language == "ar") hotel.amenitiesAr else hotel.amenitiesEn
                            list.take(3).forEach { item ->
                                Box(
                                    modifier = Modifier
                                        .background(EthioGreen.copy(alpha = 0.08f), shape = RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(item, style = MaterialTheme.typography.labelSmall, color = EthioGreen)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(if (language == "ar") "السعر لليلة واحدة" else "Price per night", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                Text("$${hotel.pricePerNightUSD}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = EthioGreen)
                            }

                            Button(
                                onClick = { viewModel.startHotelBooking(hotel) },
                                colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (language == "ar") "احـجز الآن" else "Book Room")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlightsBookingFeed(viewModel: TravelViewModel, language: String) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(TravelData.flights) { flight ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Send, contentDescription = "Flight", tint = EthioBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == "ar") flight.airlineAr else flight.airlineEn,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(EthioBlue.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (language == "ar") "مؤكدة" else "Confirmed",
                                style = MaterialTheme.typography.labelSmall,
                                color = EthioBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated Boarding passes hubs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                if (language == "ar") flight.fromAr.substringBefore(" ") else flight.fromEn.substringAfter("(").removeSuffix(")"),
                                fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleLarge
                            )
                            Text(if (language == "ar") "مطار المغادرة" else "Departure Hub", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }

                        // Dashed flying line
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(if (language == "ar") flight.durationAr else flight.durationEn, style = MaterialTheme.typography.labelSmall, color = EthioBlue)
                            Spacer(modifier = Modifier.height(4.dp))
                            Canvas(modifier = Modifier.fillMaxWidth().height(2.dp)) {
                                drawLine(
                                    color = EthioBlue,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                                    strokeWidth = 2f
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                if (language == "ar") flight.toAr.substringBefore(" ") else flight.toEn.substringAfter("(").removeSuffix(")"),
                                fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleLarge
                            )
                            Text(if (language == "ar") "مطار الوصول" else "Destination Hub", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = "Time", tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(flight.time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }

                        Text(
                            text = if (language == "ar") "درجة سياحية" else "Cabin: Economy",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(if (language == "ar") "تذكرة شاملة كلياً" else "Total Fare (Incl. Tax)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Text("$${flight.priceUSD}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = EthioBlue)
                        }

                        Button(
                            onClick = { viewModel.startFlightBooking(flight) },
                            colors = ButtonDefaults.buttonColors(containerColor = EthioBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (language == "ar") "احـجز الرحـلة" else "Book Ticket")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyTripsListFeed(activeBookings: List<BookingEntity>, language: String) {
    if (activeBookings.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                Icon(Icons.Default.Info, contentDescription = "Empty", modifier = Modifier.size(72.dp), tint = Color.Gray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (language == "ar") "لا توجد حـجوزات مؤكدة للرحلات بعد!" else "No active itineraries found!",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (language == "ar") "عزز ثقتك واحـجز فندقك أو رحلتك بالطائرة مع بوابتنا الرقمية الآمنة الآن." else "Launch payments securely with our international Stripe checklist to record dynamic bookings.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(activeBookings) { booking ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (booking.itemType == "hotel") {
                        EthioGreen.copy(alpha = 0.05f)
                    } else {
                        EthioBlue.copy(alpha = 0.05f)
                    }
                ),
                border = BorderStroke(1.dp, if (booking.itemType == "hotel") EthioGreen.copy(alpha = 0.2f) else EthioBlue.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (booking.itemType == "hotel") Icons.Default.Home else Icons.Default.Send,
                                contentDescription = "Type",
                                tint = if (booking.itemType == "hotel") EthioGreen else EthioBlue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = booking.itemName,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(
                                    if (booking.itemType == "hotel") EthioGreen else EthioBlue,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = booking.status,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = booking.details, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${if (language == "ar") "تاريخ المعاملة والتأكيد" else "Issued on"}: ${booking.date}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ref: ${booking.paymentRef}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                        Text(
                            text = "$${booking.price}",
                            fontWeight = FontWeight.ExtraBold,
                            color = if (booking.itemType == "hotel") EthioGreen else EthioBlue,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 3: INTERACTIVE MAP TAB (WITHOUT ADB / GOOGLE PLAY DEPENDENCIES FALLBACKS)
// ==========================================
@Composable
fun InteractiveMapScreenTab(viewModel: TravelViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val mapSelectedDest by viewModel.mapSelectedDestination.collectAsStateWithLifecycle()
    val showMapPath by viewModel.showMapPath.collectAsStateWithLifecycle()
    val destinations by viewModel.destinations.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(EthioGreen.copy(alpha = 0.1f))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = if (language == "ar") "الخريطة التفاعلية لإكتشاف إثيوبيا 🗺️" else "Interactive Topography Map 🗺️",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = EthioGreen
                )
                Text(
                    text = if (language == "ar") "اضغط على أي دبوس لتمرير البيانات ومحاكاة مسارات السفر." else "Tap any coordinate pin to overlay distance & simulate real plane lines.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f)
                )
            }
        }

        // Custom drawn map canvas using flag colors representing physical geography of Ethiopia!
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
                .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Drawing a premium textured stylized silhouette of regional boundary lines of Ethiopia
                val mapOutlinePath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.25f, h * 0.15f)
                    lineTo(w * 0.70f, h * 0.10f)
                    lineTo(w * 0.85f, h * 0.35f)
                    lineTo(w * 0.95f, h * 0.65f)
                    lineTo(w * 0.75f, h * 0.85f)
                    lineTo(w * 0.50f, h * 0.90f)
                    lineTo(w * 0.35f, h * 0.75f)
                    lineTo(w * 0.12f, h * 0.50f)
                    lineTo(w * 0.15f, h * 0.25f)
                    close()
                }

                // Fill backdrop
                drawPath(
                    path = mapOutlinePath,
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PassportDarkSurface,
                            PassportDarkBG
                        ),
                        center = Offset(w * 0.5f, h * 0.5f),
                        radius = w * 0.5f
                    )
                )

                // Draw regional border stroke
                drawPath(
                    path = mapOutlinePath,
                    color = EthioYellow.copy(alpha = 0.4f),
                    style = Stroke(
                        width = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f),
                        cap = StrokeCap.Round
                    )
                )

                // DRAW SIMULATED FLIGHT/DRIVING PATH (Dashed Line)
                if (showMapPath && mapSelectedDest != null) {
                    val addisX = w * 0.51f
                    val addisY = h * 0.52f
                    val targetX = w * mapSelectedDest!!.latitude
                    val targetY = h * mapSelectedDest!!.longitude

                    // Draw connecting line
                    drawLine(
                        color = EthioRed,
                        start = Offset(addisX, addisY),
                        end = Offset(targetX, targetY),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f)
                    )

                    // Draw pulse icons on both side endpoints
                    drawCircle(color = EthioRed.copy(alpha = 0.3f), radius = 24f, center = Offset(addisX, addisY))
                    drawCircle(color = EthioRed, radius = 10f, center = Offset(addisX, addisY))
                }
            }

            // Overlay buttons and pins to maintain full Android-native click states
            destinations.forEach { dest ->
                // Pin placing based on mock coordinates relative to layout width and height
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        val pinX = (maxWidth.value * dest.latitude).dp
                        val pinY = (maxHeight.value * dest.longitude).dp

                        Box(
                            modifier = Modifier
                                .offset(x = pinX - 18.dp, y = pinY - 18.dp)
                                .size(36.dp)
                                .background(
                                    if (mapSelectedDest?.id == dest.id) EthioRed else EthioGreen,
                                    shape = CircleShape
                                )
                                .border(
                                    2.dp,
                                    if (mapSelectedDest?.id == dest.id) EthioYellow else Color.White,
                                    CircleShape
                                )
                                .clickable { viewModel.setMapSelectedDestination(dest) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (dest.inAddis) "🏙️" else "🏞️",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Bottom drawer selected destination details overlay card
            if (mapSelectedDest != null) {
                val select = mapSelectedDest!!
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, EthioGreen.copy(alpha = 0.25f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ImageWithFallback(
                            imageUrl = select.imageUrl,
                            contentDescription = select.nameEn,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == "ar") select.nameAr else select.nameEn,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (language == "ar") select.distanceTextAr else select.distanceTextEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = EthioRed,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Star, contentDescription = "StarRating", tint = EthioYellow, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(select.initialRating.toString(), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Navigation path trigger
                        IconButton(
                            onClick = { viewModel.toggleMapPath() },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = if (showMapPath) EthioRed else EthioGreen
                            )
                        ) {
                            Icon(
                                imageVector = if (showMapPath) Icons.Filled.Check else Icons.Filled.LocationOn,
                                contentDescription = "Draw Path",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 4: GEMINI TOURIST AI CHAT ASSISTANT
// ==========================================
@Composable
fun AIChatScreenTab(viewModel: TravelViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isChatLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()

    var inputMessageText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Welcome strip
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            ),
            border = BorderStroke(1.dp, EthioGreen.copy(alpha = 0.15f))
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(EthioGreen.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Face, contentDescription = "AIIcon", tint = EthioGreen)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (language == "ar") "دليل إيثيو تور الذكي (Gemini)" else "EthioTour AI Assistant (Gemini)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (language == "ar") "مستعد لإجابتك عن تاريخ إثيوبيا، الفنادق، المعالم السياحية وكيف تحجز." else "Ask anything about Ethiopian historic ruins, Lucy facts or coffee setups.",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }
            }
        }

        // Messages Feed list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            reverseLayout = false // standard flow
        ) {
            items(chatMessages) { msg ->
                ChatBubble(msg, language)
            }

            if (isChatLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = EthioGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == "ar") "دليلك الذكي يفكر..." else "Thinking of guide answers...",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

        // Suggestion prompt bubbles row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val prompts = if (language == "ar") {
                listOf(
                    "ما هي كنيسة لاليبيلا؟ ⛪",
                    "أين توجد أحفورة لوسي؟ 🦴",
                    "كيف أصل لبركان إرتا ألي؟ 🌋"
                )
            } else {
                listOf(
                    "What makes Lalibela special? ⛪",
                    "Explain Ethiopia Coffee Ceremony ☕",
                    "Distance to Entoto Hill 🌲"
                )
            }

            items(prompts) { textPrompt ->
                Card(
                    modifier = Modifier.clickable { viewModel.sendChatMessage(textPrompt) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
                ) {
                    Text(
                        text = textPrompt,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        // Search sending slot at bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputMessageText,
                onValueChange = { inputMessageText = it },
                placeholder = { Text(if (language == "ar") "اسأل دليل الذكاء الاصطناعي..." else "Type query for AI guide...") },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EthioGreen
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (inputMessageText.isNotBlank()) {
                        viewModel.sendChatMessage(inputMessageText)
                        inputMessageText = ""
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .background(EthioGreen, shape = CircleShape)
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage, language: String) {
    val isUser = msg.sender == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 0.dp,
                bottomEnd = if (isUser) 0.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) EthioGreen else MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.widthIn(max = 290.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = msg.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ==========================================
// SCREEN 5: NOTIFICATIONS INBOX
// ==========================================
@Composable
fun NotificationsScreenTab(viewModel: TravelViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val list by viewModel.notifications.collectAsStateWithLifecycle()

    // Mark all as read as soon as user opens this tab
    LaunchedEffect(Unit) {
        viewModel.markNotificationsAsRead()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(EthioRed.copy(alpha = 0.1f))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = if (language == "ar") "صندوق التنبيهات الفورية 🔔" else "Instant Push Notifications 🔔",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = EthioRed
                )
                Text(
                    text = if (language == "ar") "تنبيهات فورية لمعاملاتك المالية الدولية أو حجوزاتك." else "Live alerts of your transaction reference & safety guidelines.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f)
                )
            }
        }

        if (list.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(if (language == "ar") "لا توجد تنبيهات حالياً." else "Notification inbox is empty.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(list) { notif ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(
                            1.dp,
                            if (notif.isRead) {
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)
                            } else {
                                EthioRed.copy(alpha = 0.35f)
                            }
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (notif.isRead) MaterialTheme.colorScheme.surface else EthioRed.copy(alpha = 0.03f)
                        )
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        if (notif.type == "booking") EthioGreen.copy(alpha = 0.1f) else EthioRed.copy(alpha = 0.1f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (notif.type == "booking") Icons.Default.CheckCircle else Icons.Default.Notifications,
                                    contentDescription = "Notif",
                                    tint = if (notif.type == "booking") EthioGreen else EthioRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == "ar") notif.titleAr else notif.titleEn,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (language == "ar") notif.descAr else notif.descEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = notif.time,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 6: FAVORITES / BOOKMARKS SCREENS
// ==========================================
@Composable
fun FavoritesScreenTab(viewModel: TravelViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val destinations by viewModel.destinations.collectAsStateWithLifecycle()

    val favDestinations = destinations.filter { d -> favorites.any { it.placeId == d.id } }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(EthioRed.copy(alpha = 0.1f))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = if (language == "ar") "المعالم المفضلة لديك ❤️" else "My Saved Bookmarks ❤️",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = EthioRed
                )
                Text(
                    text = if (language == "ar") "مكان مخصص للوصول السريع إلى معالمك المفضلة." else "Your personal list of saved Ethiopian wonders.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f)
                )
            }
        }

        if (favDestinations.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Heart", modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == "ar") "لا توجد معالم محفوظة في المفضلة بعد." else "No bookmarked places saved yet.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favDestinations) { dest ->
                    DestinationCard(dest, viewModel, language)
                }
            }
        }
    }
}

// ==========================================
// SECURE PAYMENTS gate SYSTEM (VISA/MASTERCARD MODAL)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutPaymentDialog(viewModel: TravelViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val isOpen by viewModel.isCheckoutOpen.collectAsStateWithLifecycle()
    val success by viewModel.checkoutSuccess.collectAsStateWithLifecycle()
    val processing by viewModel.checkoutProcessing.collectAsStateWithLifecycle()

    val hotel by viewModel.selectedHotelForBooking.collectAsStateWithLifecycle()
    val flight by viewModel.selectedFlightForBooking.collectAsStateWithLifecycle()

    val number by viewModel.cardNumber.collectAsStateWithLifecycle()
    val name by viewModel.cardName.collectAsStateWithLifecycle()
    val expiry by viewModel.cardExpiry.collectAsStateWithLifecycle()
    val cvv by viewModel.cardCvv.collectAsStateWithLifecycle()

    if (!isOpen) return

    val totalCost = hotel?.pricePerNightUSD ?: flight?.priceUSD ?: 0.0
    val checkoutTitle = if (hotel != null) hotel!!.nameEn else flight!!.airlineEn

    Dialog(onDismissRequest = { if (!processing) viewModel.dismissCheckout() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (success) {
                    // SUCCESS TICK ANIMATION SCREEN
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = EthioGreen,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (language == "ar") "تم تفعيل وحفظ الحجز بنجاح!" else "Payment Authorized Successfully!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = EthioGreen,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (language == "ar") "بوابة الدفع الآمن الدولية قامت بتمرير المعاملة المالية. رقم مرجع المعاملة نشط الآن!" else "Secure Stripe engine has captured dynamic transaction keys. Booking reference active.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.dismissCheckout() },
                            colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (language == "ar") "متابعة" else "Dismiss")
                        }
                    }
                } else {
                    // PAYMENT FORM SCREEN WITH VISA CARD GRAPHIC
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == "ar") "بوابة الدفع الدولية الآمنة 🔒" else "Secure Stripe Gateway 🔒",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall,
                            color = EthioGreen
                        )
                        IconButton(onClick = { viewModel.dismissCheckout() }, enabled = !processing) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // CARD PREVIEW GRAPHIC
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = PassportDarkSurface),
                        border = BorderStroke(1.dp, EthioYellow.copy(alpha = 0.35f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = if (language == "ar") "بطاقة دولية" else "INTERNATIONAL TRAVEL PASS",
                                    color = EthioYellow,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Visa",
                                    tint = EthioYellow,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            // Card Number Preview text
                            Text(
                                text = number.ifEmpty { "•••• •••• •••• ••••" }.chunked(4).joinToString(" "),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                letterSpacing = 2.sp,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("CARD HOLDER", fontSize = 8.sp, color = Color.Gray)
                                    Text(name.ifEmpty { "STUDENT TRAVELER" }.uppercase(), fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("EXPIRES", fontSize = 8.sp, color = Color.Gray)
                                    Text(expiry.ifEmpty { "MM/YY" }, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Booking Target Summary
                    Text(
                        text = "${if (language == "ar") "الحجز المطلق" else "Target Booking"}: $checkoutTitle",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${if (language == "ar") "المبلغ المستحق للدفع" else "Amount Due"}: $$totalCost USD",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = EthioRed
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // INPUT FIELDS
                    OutlinedTextField(
                        value = number,
                        onValueChange = { if (it.length <= 16) viewModel.cardNumber.value = it },
                        label = { Text(if (language == "ar") "رقم البطاقة (16 رقم)" else "Card Number (16 digits)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.cardName.value = it },
                        label = { Text(if (language == "ar") "الاسم المكتوب على البطاقة" else "Cardholder Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = expiry,
                            onValueChange = { if (it.length <= 5) viewModel.cardExpiry.value = it },
                            label = { Text("MM/YY") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = cvv,
                            onValueChange = { if (it.length <= 3) viewModel.cardCvv.value = it },
                            label = { Text("CVV") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Payment submission btn
                    val isFormValid = number.length == 16 && expiry.length >= 4 && cvv.length == 3 && name.isNotBlank()

                    Button(
                        onClick = { viewModel.processPayment() },
                        enabled = isFormValid && !processing,
                        colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        if (processing) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (language == "ar") "جاري تشفير المعاملة..." else "Securing Authorisation...")
                        } else {
                            Text(
                                if (language == "ar") "ادفع بأمان الآن ($$totalCost)" else "Secure Payment ($$totalCost)"
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SUB-VIEW: SUGGESTED ITINERARIES (المسارات المقترحة)
// ==========================================
@Composable
fun SuggestedItinerariesView(viewModel: TravelViewModel, language: String) {
    val itineraries = TravelData.suggestedItineraries
    var expandedItineraryId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = EthioYellow.copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, EthioYellow.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Idea",
                        tint = EthioYellow,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (language == "ar") 
                            "المسارات الذكية مُعدة ومُنظمة لمساعدتك على قضاء رحلة أحلامك في إثيوبيا بالتفصيل." 
                            else "Smart curated itineraries prepared to help you experience your dream trip to Ethiopia.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }
            }
        }

        items(itineraries) { iti ->
            val isExpanded = expandedItineraryId == iti.id
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedItineraryId = if (isExpanded) null else iti.id }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Title and Duration Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == "ar") iti.titleAr else iti.titleEn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Duration",
                                    tint = EthioGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == "ar") iti.durationAr else iti.durationEn,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EthioGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        // Expand/Collapse icon
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description text
                    Text(
                        text = if (language == "ar") iti.descriptionAr else iti.descriptionEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = if (isExpanded) 100 else 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    AnimatedVisibility(visible = isExpanded) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            // 1. Target Theme / Interest
                            Text(
                                text = if (language == "ar") "نوع الرحلة والاهتمامات:" else "Travel Theme:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            SuggestionChip(
                                onClick = {},
                                label = { Text(if (language == "ar") iti.interestTypeAr else iti.interestTypeEn) },
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // 2. Destinations Included (معالم سياحية)
                            Text(
                                text = if (language == "ar") "المعالم السياحية المشمولة:" else "Landmarks Visited:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Map place IDs to real destinations
                            val matchingPlaces = TravelData.destinations.filter { it.id in iti.destinationsIncluded }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                matchingPlaces.forEach { place ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = EthioGreen.copy(alpha = 0.08f)),
                                        border = BorderStroke(1.dp, EthioGreen.copy(alpha = 0.3f)),
                                        modifier = Modifier.clickable { viewModel.selectDestination(place) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            AsyncImage(
                                                model = place.imageUrl,
                                                contentDescription = place.nameEn,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (language == "ar") place.nameAr else place.nameEn,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = EthioGreen
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 3. Transit Details
                            Text(
                                text = if (language == "ar") "طريقة الذهاب ووسيلة المواصلات:" else "Transit Method & Details:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = "Transit",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (language == "ar") iti.transitAr else iti.transitEn,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 4. Accommodation Linked list
                            Text(
                                text = if (language == "ar") "الفنادق والمنتجعات المقترحة للحجز:" else "Suggested Accommodations:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            val matchingHotels = TravelData.hotels.filter { it.id in iti.accommodationOptions }
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                matchingHotels.forEach { hotel ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (language == "ar") hotel.nameAr else hotel.nameEn,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Button(
                                            onClick = { viewModel.startHotelBooking(hotel) },
                                            colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                                            modifier = Modifier.height(28.dp)
                                        ) {
                                            Text(if (language == "ar") "احجز" else "Book", fontSize = 10.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Adopt Itinerary BTN
                            Button(
                                onClick = {
                                    val interestsStr = when (iti.id) {
                                        "itinerary_3d_history" -> "HISTORY"
                                        "itinerary_7d_nature" -> "NATURE"
                                        "itinerary_7d_adventure" -> "ADVENTURE"
                                        else -> "HISTORY,NATURE"
                                    }
                                    viewModel.saveUserPreference(
                                        name = "",
                                        budget = if (iti.id.contains("adventure")) "High" else "Medium",
                                        accommodation = if (iti.id.contains("nature")) "Resort" else "Hotel",
                                        activities = interestsStr
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EthioYellow),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Done, contentDescription = "Adopt", tint = Color.Black)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    if (language == "ar") "تبنّي وتفضيل هذا المسار" else "Adopt & Persona-link This Route",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SUB-VIEW: CAPITAL HIGH LUXURY RESORTS نزدیک العاصمة
// ==========================================
@Composable
fun CapitalResortsView(viewModel: TravelViewModel, language: String) {
    // Curate Resorts close to Addis Ababa (Kuriftu Bishoftu, Babogaya Lakeside, Negash Wolliso Hotsprings)
    val resorts = TravelData.hotels.filter { 
        it.id in listOf("kuriftu_resort", "babogaya_resort", "negash_resort") 
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = if (language == "ar") "منتجعات فاخرة خارج صخب العاصمة" else "Luxury Resorts Near Addis Ababa",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = EthioGreen
                )
                Text(
                    text = if (language == "ar") "على بعد دقائق فقط من أديس، ببحيرات بركانية وينابيع دافئة مريحة." else "Just minutes away from Addis, featuring gorgeous volcanic lakes and hot springs.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        items(resorts) { resort ->
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                        AsyncImage(
                            model = resort.imageUrl,
                            contentDescription = resort.nameEn,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        // Budget / Pricing Tag
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(EthioGreen, EthioGreen.copy(alpha = 0.8f))
                                    ),
                                    RoundedCornerShape(topStart = 16.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "$${resort.pricePerNightUSD} USD / ${if (language == "ar") "ليلة" else "Night"}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        // Close to Capital Badge label
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                                .background(EthioRed, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (language == "ar") "قريب من العاصمة" else "Minutes to Capital",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == "ar") resort.nameAr else resort.nameEn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = "*", tint = EthioYellow, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    resort.starRating.toString(),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Loc", tint = Color.Gray, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == "ar") resort.locationAr else resort.locationEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Amenities List row
                        Text(
                            text = if (language == "ar") "الميزات الفاخرة المشمولة:" else "Premium Luxury Amenities included:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val amenities = if (language == "ar") resort.amenitiesAr else resort.amenitiesEn
                            amenities.forEach { am ->
                                Box(
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = am,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.startHotelBooking(resort) },
                            colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = "Booking")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (language == "ar") "احجز إقامتك الفورية بالمنتجع الآن" else "Book This Premium Resort Stay",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SUB-VIEW: IMMERSIVE GALLERIES (فيديو وبانوراما ٣٦٠ درجة)
// ==========================================
@Composable
fun ImmersiveMediaView(viewModel: TravelViewModel, language: String) {
    val mediaList = TravelData.mediaItems
    var activeVideoId by remember { mutableStateOf<String?>(null) }
    var videoProgress by remember { mutableFloatStateOf(0.35f) }
    var isVideoMuted by remember { mutableStateOf(false) }

    // Panorama degree swipe custom simulator states
    var activePanoId by remember { mutableStateOf<String?>(null) }
    var panoDragOffset by remember { mutableFloatStateOf(180f) } // 0° to 360°

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = if (language == "ar") "الوسائط الغامرة التفاعلية" else "Immersive Travel Media Gallery",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = EthioGreen
                )
                Text(
                    text = if (language == "ar") "شاهد لقطات فيديو سينمائية، أو اسحب أصابعك لاستكشاف بانوراما 360 درجة." else "Watch high-definition cinematic preview videos or swipe to interact with 360° panoramic views.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        items(mediaList) { item ->
            val isVideo = item.mediaType == "VIDEO"
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        // If Panoramic Interactive mode is ON, shift scale representation!
                        if (item.id == activePanoId) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = "Pano",
                                    contentScale = ContentScale.FillHeight,
                                    alignment = androidx.compose.ui.BiasAlignment(
                                        horizontalBias = (panoDragOffset - 180f) / 180f, // Map 0-360 to bias -1.0 to 1.0!
                                        verticalBias = 0.0f
                                    ),
                                    modifier = Modifier.fillMaxSize()
                                )
                                
                                // Compass direction Overlay HUD
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.65f)),
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .padding(12.dp)
                                ) {
                                    val directionLetter = when {
                                        panoDragOffset in 0.0f..45.0f -> "N"
                                        panoDragOffset in 45.0f..135.0f -> "E"
                                        panoDragOffset in 135.0f..225.0f -> "S"
                                        panoDragOffset in 225.0f..315.0f -> "W"
                                        else -> "N"
                                    }
                                    Text(
                                        text = "${panoDragOffset.toInt()}° $directionLetter",
                                        color = EthioYellow,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        } else {
                            // Standard preview image
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = item.titleEn,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Gradient protection overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                    )
                                )
                        )

                        // MEDIA TYPE BADGE TAG
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                                .background(
                                    if (isVideo) EthioRed else EthioGreen,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isVideo) "HD VIDEO 🎬" else "PANORAMA 360° 📸",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                fontSize = 8.sp
                            )
                        }

                        // Play / Simulator overlays
                        if (isVideo) {
                            if (activeVideoId == item.id) {
                                // Full Custom simulated media controls Overlay
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.5f))
                                ) {
                                    // Pause icon in center
                                    IconButton(
                                        onClick = { activeVideoId = null },
                                        modifier = Modifier
                                            .size(56.dp)
                                            .align(Alignment.Center)
                                            .background(Color.White.copy(alpha = 0.3f), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Pause", tint = Color.White, modifier = Modifier.size(32.dp))
                                    }

                                    // Controls Bottom hud
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.BottomCenter)
                                            .background(Color.Black.copy(alpha = 0.6f))
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = "Playing", tint = EthioGreen)
                                        
                                        // Slider progress simulator
                                        Slider(
                                            value = videoProgress,
                                            onValueChange = { videoProgress = it },
                                            colors = SliderDefaults.colors(
                                                thumbColor = EthioGreen,
                                                activeTrackColor = EthioGreen
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )

                                        // Mute Toggle btn
                                        IconButton(onClick = { isVideoMuted = !isVideoMuted }, modifier = Modifier.size(24.dp)) {
                                            Icon(
                                                imageVector = if (isVideoMuted) Icons.Default.Close else Icons.Default.Share, 
                                                contentDescription = "Mute", 
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }

                                        Text(
                                            text = item.durationOrAngle,
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            } else {
                                // Video Play CTA button
                                IconButton(
                                    onClick = { activeVideoId = item.id },
                                    modifier = Modifier
                                        .size(64.dp)
                                        .align(Alignment.Center)
                                        .background(EthioRed.copy(alpha = 0.9f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play Video",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        } else {
                            // PANORAMA 360 INTERACTION CONTROLS
                            if (activePanoId == item.id) {
                                // Slider custom simulator to drag 360 degree
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.2f))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.BottomCenter)
                                            .background(Color.Black.copy(alpha = 0.7f))
                                            .padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = if (language == "ar") "اسحب المنزلق أدناه لتدوير المشهد بزاوية ٣٦٠ درجة" else "Drag the slider to spin the panorama 360°:",
                                            color = Color.White,
                                            fontSize = 9.sp
                                        )
                                        Slider(
                                            value = panoDragOffset,
                                            onValueChange = { panoDragOffset = it },
                                            valueRange = 0f..360f,
                                            colors = SliderDefaults.colors(
                                                thumbColor = EthioYellow,
                                                activeTrackColor = EthioYellow
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Button(
                                            onClick = { activePanoId = null },
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                            modifier = Modifier.height(28.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp)
                                        ) {
                                            Text(if (language == "ar") "إغلاق العرض" else "Exit View", fontSize = 10.sp)
                                        }
                                    }
                                }
                            } else {
                                // Button to spin
                                Button(
                                    onClick = { activePanoId = item.id },
                                    colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                                    modifier = Modifier.align(Alignment.Center)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "360")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (language == "ar") "دوران واستكشاف ٣٦٠° تفاعلي" else "Spin Interactive 360°")
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == "ar") item.titleAr else item.titleEn,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Place", tint = Color.Gray, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == "ar") item.placeNameAr else item.placeNameEn,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (language == "ar") item.descriptionAr else item.descriptionEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN: PERSONALIZATIONS & CUSTOM PROFILE (تخصيص تجربة المستخدم والمسارات الخاصة)
// ==========================================
@Composable
fun ProfileScreenTab(viewModel: TravelViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val preferences by viewModel.userPreference.collectAsStateWithLifecycle()
    val customItineraries by viewModel.userItineraries.collectAsStateWithLifecycle()
    val personalizedRecommendations by viewModel.personalizedRecommendations.collectAsStateWithLifecycle()

    // Preferences Form Input States
    var userNameInput by remember(preferences) { mutableStateOf(preferences.userName) }
    var selectedBudget by remember(preferences) { mutableStateOf(preferences.budget) }
    var selectedAccommodation by remember(preferences) { mutableStateOf(preferences.accommodationType) }
    var activitiesStr by remember(preferences) { mutableStateOf(preferences.activities) }

    // Custom Itinerary Fields
    var itiTitle by remember { mutableStateOf("") }
    var itiDays by remember { mutableFloatStateOf(3f) }
    var itiNotes by remember { mutableStateOf("") }
    val chosenDestinationIds = remember { mutableStateListOf<String>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header Profile Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = EthioGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.White.copy(alpha = 0.25f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = if (language == "ar") 
                                "مرحباً: ${userNameInput.ifEmpty { "مسافر دائم" }}" 
                                else "Welcome, ${userNameInput.ifEmpty { "Frequent Flyer" }}!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = if (language == "ar") "مستشار السياحة الذكي مخصّص لخطط اهتماماتك" else "AI assistant customized for your exact travel profile",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Section A: Preferences Customiser (تفضيلات السفر الذكية)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Settings, contentDescription = "Pref", tint = EthioGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == "ar") "الملف الشخصي وتفضيلات السفر" else "Smart Travel Persona Preferences",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    // Name Input
                    OutlinedTextField(
                        value = userNameInput,
                        onValueChange = { userNameInput = it },
                        label = { Text(if (language == "ar") "اسمك الكريم" else "Your Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Budget level selection
                    Text(
                        text = if (language == "ar") "الميزانية المفضلة للأماكن والتذاكر:" else "Preferred Budget Level:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Low", "Medium", "High").forEach { b ->
                            val isSel = selectedBudget == b
                            ElevatedFilterChip(
                                selected = isSel,
                                onClick = { selectedBudget = b },
                                label = { 
                                    Text(
                                        text = when(b) {
                                            "Low" -> if (language == "ar") "محدودة 💵" else "Low 💵"
                                            "Medium" -> if (language == "ar") "متوسطة 💳" else "Medium 💳"
                                            "High" -> if (language == "ar") "مفتوحة 💎" else "High 💎"
                                            else -> b
                                        }
                                    ) 
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Accommodation preferences
                    Text(
                        text = if (language == "ar") "نوع الإقامة المفضل لديك:" else "Accommodation Style Preference:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Hotel", "Resort", "Lodge").forEach { a ->
                            val isSel = selectedAccommodation == a
                            ElevatedFilterChip(
                                selected = isSel,
                                onClick = { selectedAccommodation = a },
                                label = { 
                                    Text(
                                        text = when(a) {
                                            "Hotel" -> if (language == "ar") "فنادق حديثة" else "Hotels"
                                            "Resort" -> if (language == "ar") "منتجعات فاخرة" else "Resorts"
                                            "Lodge" -> if (language == "ar") "أكواخ جبلية" else "Nature Lodges"
                                            else -> a
                                        }
                                    ) 
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Desired travel interests activities checklist
                    Text(
                        text = if (language == "ar") "الأنشطة الممتعة والمجالات المفضلة (انقر للتحديد):" else "Interests & Desired Activities (Tap to toggle):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val activeCategoriesList = activitiesStr.split(",").map { it.trim().uppercase() }.filter { it.isNotEmpty() }
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Category.values().forEach { cat ->
                            val catName = cat.name.uppercase()
                            val isSel = activeCategoriesList.contains(catName)
                            FilterChip(
                                selected = isSel,
                                onClick = {
                                    val updated = if (isSel) {
                                        activeCategoriesList.filter { it != catName }
                                    } else {
                                        activeCategoriesList + catName
                                    }
                                    activitiesStr = updated.joinToString(",")
                                },
                                label = {
                                    Text(
                                        text = when (cat) {
                                            Category.HISTORY -> if (language == "ar") "تاريخ وآثار" else "History"
                                            Category.NATURE -> if (language == "ar") "طبيعة ومرتفعات" else "Nature"
                                            Category.CULTURE -> if (language == "ar") "ثقافة وفنون" else "Culture"
                                            Category.ADVENTURE -> if (language == "ar") "مغامرات شيقة" else "Adventure"
                                        }
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { 
                            viewModel.saveUserPreference(userNameInput, selectedBudget, selectedAccommodation, activitiesStr) 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.Done, contentDescription = "Save")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (language == "ar") "حفظ تفضيلات الملف الشخصي" else "Save Travel Profile Persona", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section B: AI Recommended Destinations based on Profile custom scores! (توصيات الذكاء المخصصة لملفك)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, EthioYellow.copy(alpha = 0.4f)),
                colors = CardDefaults.cardColors(containerColor = EthioYellow.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rec", tint = EthioYellow)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == "ar") "ترشيحات ذكاء اصطناعي مفضلة ومخصصة لك" else "Your Personalized AI Matches",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = EthioGreen
                        )
                    }
                    Text(
                        text = if (language == "ar") "قائمة معالم سياحية مرتبة تلقائياً بنسب تطابق تامة مع تفضيلاتك." else "EthioTour auto-scored and prioritized these based on your active profile constraints.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (personalizedRecommendations.isEmpty()) {
                        Text(if (language == "ar") "قم بتحديد تفضيلاتك للحصول على ترشيحات مخصصة." else "Save your profile constraints to compute customized AI matches.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Render top 2 scored recommendations
                            personalizedRecommendations.take(2).forEachIndexed { index, place ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                        .clickable { viewModel.selectDestination(place) }
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = place.imageUrl,
                                        contentDescription = "PImage",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (language == "ar") place.nameAr else place.nameEn,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = if (language == "ar") place.locationAr else place.locationEn,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.Gray
                                        )
                                    }
                                    
                                    // Match confidence tag!
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (index == 0) EthioGreen else EthioYellow,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        val matchPercent = when(index) {
                                            0 -> "98%"
                                            else -> "85%"
                                        }
                                        Text(
                                            text = matchPercent,
                                            color = if (index == 0) Color.White else Color.Black,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section C: Custom Route Planner (تخطيط مسارك الخاص وحفظه بالمستودع)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add", tint = EthioGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == "ar") "خطط مسارك السياحي الخاص" else "Design Custom Travel Itinerary",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = if (language == "ar") "قم بتسمية وتحديد معالم رحلتك المشمولة وتخزينها محلياً." else "Set high quality customized stops, name it, and save offline in SQL database.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Title
                    OutlinedTextField(
                        value = itiTitle,
                        onValueChange = { itiTitle = it },
                        label = { Text(if (language == "ar") "عنوان مسارك (مثال: عطلة الطبيعة)" else "Custom Itinerary Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Duration Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${if (language == "ar") "المدة الزمنية المقدرة" else "Curated Duration"}: ${itiDays.toInt()} ${if (language == "ar") "أيام" else "Days"}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Slider(
                        value = itiDays,
                        onValueChange = { itiDays = it },
                        valueRange = 1f..14f,
                        colors = SliderDefaults.colors(
                            thumbColor = EthioGreen,
                            activeTrackColor = EthioGreen
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Checkbox Multi-selection of real destinations
                    Text(
                        text = if (language == "ar") "اختر المحطات والمعالم المشمولة (متعدد):" else "Select the Landmarks and Stops:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        val scrollState = rememberScrollState()
                        Column(modifier = Modifier.verticalScroll(scrollState)) {
                            TravelData.destinations.forEach { place ->
                                val placeId = place.id
                                val isChecked = chosenDestinationIds.contains(placeId)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { 
                                            if (isChecked) chosenDestinationIds.remove(placeId) 
                                            else chosenDestinationIds.add(placeId)
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { 
                                            if (isChecked) chosenDestinationIds.remove(placeId) 
                                            else chosenDestinationIds.add(placeId)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (language == "ar") place.nameAr else place.nameEn,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Notes
                    OutlinedTextField(
                        value = itiNotes,
                        onValueChange = { itiNotes = it },
                        label = { Text(if (language == "ar") "ملاحظات إضافية (مثال: رفقة العائلة)" else "Personal Itinerary Notes") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (itiTitle.isNotBlank()) {
                                viewModel.addUserItinerary(itiTitle, itiDays.toInt(), chosenDestinationIds.toList(), itiNotes)
                                // Clear inputs
                                itiTitle = ""
                                itiDays = 3f
                                itiNotes = ""
                                chosenDestinationIds.clear()
                            }
                        },
                        enabled = itiTitle.isNotBlank() && chosenDestinationIds.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = EthioGreen),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (language == "ar") "إنشاء وحفظ مساري المخصص" else "Save Custom Route Itinerary", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section D: All Saved Custom Itineraries (مساراتي المخزنة في قاعدة البيانات)
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = "Love", tint = EthioRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == "ar") "مساراتي المحفوظة" else "My Saved Custom Itineraries",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (language == "ar") "المسارات الشخصية التي قمت بتخطيطها وتخزينها في مستودع البيانات المحلي." else "Custom routes planned, stored, and managed securely in SQLite DB.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        if (customItineraries.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Box(modifier = Modifier.padding(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (language == "ar") "لا تملك مسارات مخصصة محفوظة بعد." else "No custom saved itineraries yet. Create one above!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(customItineraries) { ct ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = ct.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = EthioGreen
                                )
                                Text(
                                    text = "${ct.durationDays} ${if (language == "ar") "أيام" else "Days"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            
                            // Delete from DB btn
                            IconButton(onClick = { viewModel.deleteUserItinerary(ct.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }

                        if (ct.notes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${if (language == "ar") "ملاحظاتي" else "Notes"}: ${ct.notes}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }

                        // Targets
                        if (ct.destinationIds.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            val listIds = ct.destinationIds.split(",").filter { it.isNotEmpty() }
                            val destinationObjects = TravelData.destinations.filter { it.id in listIds }
                            
                            Text(
                                text = if (language == "ar") "المحطات المحددة للمرور بها:" else "Planned Stops:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                destinationObjects.forEach { place ->
                                    Box(
                                        modifier = Modifier
                                            .background(EthioYellow.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                            .border(1.dp, EthioYellow, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = if (language == "ar") place.nameAr else place.nameEn,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = EthioGreen
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
