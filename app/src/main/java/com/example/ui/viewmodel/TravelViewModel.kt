package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.api.Content
import com.example.data.api.GenerateContentRequest
import com.example.data.api.Part
import com.example.data.api.RetrofitClient
import com.example.data.local.BookingEntity
import com.example.data.local.FavoriteEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.UserPreferenceEntity
import com.example.data.local.UserItineraryEntity
import com.example.data.local.TravelDatabase
import com.example.data.model.Destination
import com.example.data.model.Flight
import com.example.data.model.Hotel
import com.example.data.model.TravelData
import com.example.data.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Simple chat message model
data class ChatMessage(
    val id: String,
    val text: String,
    val sender: String, // "user" or "ai"
    val timestamp: Long = System.currentTimeMillis()
)

data class TravelNotification(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val descEn: String,
    val descAr: String,
    val time: String,
    val isRead: Boolean = false,
    val type: String // "alert", "booking", "discount"
)

class TravelViewModel(application: Application) : AndroidViewModel(application) {
    private val db = TravelDatabase.getDatabase(application)
    private val dao = db.travelDao()

    // Language setting ("en" for English, "ar" for Arabic)
    private val _language = MutableStateFlow("ar") // Default to Arabic as requested "دعم كامل" and prioritising Arabic representation
    val language: StateFlow<String> = _language.asStateFlow()

    // Current app section / Screen navigation
    private val _currentNavTab = MutableStateFlow("EXPLORE")
    val currentNavTab: StateFlow<String> = _currentNavTab.asStateFlow()

    // All Destinations
    private val _destinations = MutableStateFlow(TravelData.destinations)
    val destinations: StateFlow<List<Destination>> = _destinations.asStateFlow()

    // Selected destination for details screen
    private val _selectedDestination = MutableStateFlow<Destination?>(null)
    val selectedDestination: StateFlow<Destination?> = _selectedDestination.asStateFlow()

    // Database Bookings Flow
    val bookings: StateFlow<List<BookingEntity>> = dao.getAllBookings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Database Favorites Flow
    val favorites: StateFlow<List<FavoriteEntity>> = dao.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User travel preferences
    val userPreference: StateFlow<UserPreferenceEntity> = dao.getUserPreference()
        .map { it ?: UserPreferenceEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferenceEntity())

    // Custom user-planned itineraries
    val userItineraries: StateFlow<List<UserItineraryEntity>> = dao.getAllUserItineraries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Scored personalized recommendations flow
    val personalizedRecommendations: StateFlow<List<Destination>> = combine(
        _destinations, userPreference
    ) { dests, prefs ->
        val activeActivities = prefs.activities.split(",")
            .map { it.trim().uppercase() }
            .filter { it.isNotEmpty() }
        
        dests.map { dest ->
            var score = 0
            // Match interests
            if (activeActivities.contains(dest.category.name)) {
                score += 10
            }
            // Match budget pricing
            when (prefs.budget) {
                "Low" -> if (dest.baseTicketPriceUSD <= 15.0) score += 5
                "High" -> if (dest.baseTicketPriceUSD > 30.0) score += 5
                else -> if (dest.baseTicketPriceUSD in 5.0..40.0) score += 5
            }
            // Match accommodation types
            if (prefs.accommodationType == "Resort" && (dest.category == Category.NATURE || dest.category == Category.ADVENTURE)) {
                score += 5
            }
            dest to score
        }
        .sortedByDescending { it.second }
        .map { it.first }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TravelData.destinations)

    // Interactive Map Selected Attraction
    private val _mapSelectedDestination = MutableStateFlow<Destination?>(TravelData.destinations.first())
    val mapSelectedDestination: StateFlow<Destination?> = _mapSelectedDestination.asStateFlow()

    // Simulated Navigation Path On Map
    private val _showMapPath = MutableStateFlow(false)
    val showMapPath: StateFlow<Boolean> = _showMapPath.asStateFlow()

    // Search and filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterAddisOnly = MutableStateFlow<Boolean?>(null) // null = all, true = inside, false = outside
    val filterAddisOnly: StateFlow<Boolean?> = _filterAddisOnly.asStateFlow()

    // Combine filters with destinations
    val filteredDestinations: StateFlow<List<Destination>> = combine(
        _destinations, _searchQuery, _filterAddisOnly
    ) { destList, query, insideCapital ->
        destList.filter { dest ->
            val matchesQuery = dest.nameEn.contains(query, ignoreCase = true) ||
                    dest.nameAr.contains(query, ignoreCase = true) ||
                    dest.descriptionEn.contains(query, ignoreCase = true) ||
                    dest.descriptionAr.contains(query, ignoreCase = true) ||
                    dest.locationEn.contains(query, ignoreCase = true) ||
                    dest.locationAr.contains(query, ignoreCase = true)
            
            val matchesRegion = insideCapital == null || dest.inAddis == insideCapital
            
            matchesQuery && matchesRegion
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TravelData.destinations)

    // Current reviews for selected destination
    private val _activeReviews = MutableStateFlow<List<ReviewEntity>>(emptyList())
    val activeReviews: StateFlow<List<ReviewEntity>> = _activeReviews.asStateFlow()

    // Notifications Center
    private val _notifications = MutableStateFlow<List<TravelNotification>>(
        listOf(
            TravelNotification(
                id = "notif_01",
                titleEn = "Welcome to Ethiopia!",
                titleAr = "مرحباً بكم في إثيوبيا!",
                descEn = "Explore the historic cities, majestic nature and rich heritage of modern East Africa with EthioTour.",
                descAr = "استكشف المدن التاريخية والطبيعة المهيبة والتراث الغني لشرق إفريقيا مع تطبيق إيثيو تور.",
                time = "Just now",
                type = "alert"
            ),
            TravelNotification(
                id = "notif_02",
                titleEn = "Lalibela Holiday Sales",
                titleAr = "خصومات العطلات في لاليبيلا",
                descEn = "Get 20% off hotel bookings in Lalibela this week only! Check the bookings page.",
                descAr = "احصل على خصم 20% على حجوزات الفنادق في لاليبيلا هذا الأسبوع فقط! تصفح صفحة الحجوزات الآن.",
                time = "2 hours ago",
                type = "discount"
            ),
            TravelNotification(
                id = "notif_03",
                titleEn = "Flight Updates",
                titleAr = "تحديثات رحلات الطيران",
                descEn = "Direct daily flights from Riyadh & Dubai now active on Ethiopian Airlines.",
                descAr = "الرحلات اليومية المباشرة من الرياض ودبي نشطة الآن عبر الخطوط الجوية الإثيوبية البولية.",
                time = "1 day ago",
                type = "alert"
            )
        )
    )
    val notifications: StateFlow<List<TravelNotification>> = _notifications.asStateFlow()

    // Gemini Chat Messages State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("1", "Hello! I am your AI EthioTour Guide. Ask me anything about Ethiopian history, Lucy, traditional food, or flight schedules!", "ai"),
            ChatMessage("2", "مرحباً! أنا دليلك الذكي بـ إيثيو تور. اسألني عن أي شي يخص إثيوبيا مثل المعالم الأثرية، كيف تذهب إليها، المطبخ التقليدي وغيره!", "ai")
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // Bookings current selections
    private val _selectedHotelForBooking = MutableStateFlow<Hotel?>(null)
    val selectedHotelForBooking: StateFlow<Hotel?> = _selectedHotelForBooking.asStateFlow()

    private val _selectedFlightForBooking = MutableStateFlow<Flight?>(null)
    val selectedFlightForBooking: StateFlow<Flight?> = _selectedFlightForBooking.asStateFlow()

    // Checkout Modal State
    private val _isCheckoutOpen = MutableStateFlow(false)
    val isCheckoutOpen: StateFlow<Boolean> = _isCheckoutOpen.asStateFlow()

    private val _checkoutSuccess = MutableStateFlow(false)
    val checkoutSuccess: StateFlow<Boolean> = _checkoutSuccess.asStateFlow()

    private val _checkoutProcessing = MutableStateFlow(false)
    val checkoutProcessing: StateFlow<Boolean> = _checkoutProcessing.asStateFlow()

    // UI Input for Checkout Form
    val cardNumber = MutableStateFlow("")
    val cardName = MutableStateFlow("")
    val cardExpiry = MutableStateFlow("")
    val cardCvv = MutableStateFlow("")

    // Notification Unread Count
    val unreadNotificationsCount: StateFlow<Int> = _notifications
        .map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    init {
        // Automatically load reviews when selected destination changes
        viewModelScope.launch {
            selectedDestination.collect { dest ->
                if (dest != null) {
                    dao.getReviewsForPlace(dest.id).collect { roomReviews ->
                        _activeReviews.value = roomReviews
                    }
                }
            }
        }
    }

    fun setLanguage(lang: String) {
        _language.value = lang
    }

    fun navigateTo(tab: String) {
        _currentNavTab.value = tab
    }

    fun selectDestination(dest: Destination?) {
        _selectedDestination.value = dest
        if (dest != null) {
            _currentNavTab.value = "DETAILS"
        }
    }

    fun setMapSelectedDestination(dest: Destination) {
        _mapSelectedDestination.value = dest
        _showMapPath.value = false
    }

    fun toggleMapPath() {
        _showMapPath.value = !_showMapPath.value
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterAddis(inside: Boolean?) {
        _filterAddisOnly.value = inside
    }

    // Toggle Favorite
    fun toggleFavorite(placeId: String) {
        viewModelScope.launch {
            val favs = dao.getAllFavorites().first()
            val isFav = favs.any { it.placeId == placeId }
            if (isFav) {
                dao.removeFavorite(placeId)
            } else {
                dao.addFavorite(FavoriteEntity(placeId))
            }
        }
    }

    // Post review
    fun postReview(placeId: String, userName: String, comment: String, rating: Float) {
        viewModelScope.launch {
            val name = userName.ifEmpty { if (_language.value == "ar") "سائح زائر" else "Visitor Tourist" }
            val cleanComment = comment.ifEmpty { if (_language.value == "ar") "مكان رائع ويستحق الزيارة" else "Excellent place, highly recommended!" }
            dao.insertReview(
                ReviewEntity(
                    placeId = placeId,
                    userName = name,
                    comment = cleanComment,
                    rating = rating
                )
            )
            // Post an in-app alert for gratitude
            val newAlert = TravelNotification(
                id = "alert_${System.currentTimeMillis()}",
                titleEn = "Thank you for your feedback!",
                titleAr = "شكراً لتقييمك الرائع!",
                descEn = "Your review was successfully saved for ${destinations.value.find { it.id == placeId }?.nameEn}.",
                descAr = "تم تسجيل تعليقك ومراجعتك بنجاح للمعلم ${destinations.value.find { it.id == placeId }?.nameAr}.",
                time = "Just now",
                type = "alert"
            )
            _notifications.value = listOf(newAlert) + _notifications.value
        }
    }

    // Booking actions
    fun startHotelBooking(hotel: Hotel) {
        _selectedHotelForBooking.value = hotel
        _selectedFlightForBooking.value = null // reset
        _isCheckoutOpen.value = true
        _checkoutSuccess.value = false
        _checkoutProcessing.value = false
    }

    fun startFlightBooking(flight: Flight) {
        _selectedFlightForBooking.value = flight
        _selectedHotelForBooking.value = null // reset
        _isCheckoutOpen.value = true
        _checkoutSuccess.value = false
        _checkoutProcessing.value = false
    }

    fun dismissCheckout() {
        _isCheckoutOpen.value = false
    }

    // Complete Booking Payment Simulator
    fun processPayment() {
        if (cardNumber.value.length < 16 || cardExpiry.value.length < 4 || cardCvv.value.length < 3) {
            return
        }
        viewModelScope.launch {
            _checkoutProcessing.value = true
            // Simulate Stripe/PayPal processing delays
            kotlinx.coroutines.delay(2000)
            _checkoutProcessing.value = false
            _checkoutSuccess.value = true

            // Insert real Booking Entity into Room database
            val hotel = _selectedHotelForBooking.value
            val flight = _selectedFlightForBooking.value
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val dateStr = df.format(Date())

            if (hotel != null) {
                dao.insertBooking(
                    BookingEntity(
                        itemType = "hotel",
                        itemName = hotel.nameEn,
                        date = dateStr,
                        details = "Nights Check-in, 1 Room, ${hotel.locationEn}",
                        price = hotel.pricePerNightUSD,
                        passengerCount = 2,
                        status = "Confirmed / مؤكد",
                        paymentRef = "PAY-ET-${System.currentTimeMillis() % 100000}"
                    )
                )
                // Add Notification
                dispatchSecureNotif(
                    titleEn = "Hotel Reserved Successfully!",
                    titleAr = "تم تأكيد حجز الفندق بنجاح!",
                    descEn = "Your luxury room at ${hotel.nameEn} is secured. Reference: ${System.currentTimeMillis() % 100000}",
                    descAr = "تم حجز غرفتك الفاخرة بنجاح بـ ${hotel.nameAr}. رقم المرجع: ${System.currentTimeMillis() % 100000}"
                )
            } else if (flight != null) {
                dao.insertBooking(
                    BookingEntity(
                        itemType = "flight",
                        itemName = flight.airlineEn,
                        date = dateStr,
                        details = "From ${flight.fromEn} to ${flight.toEn}",
                        price = flight.priceUSD,
                        passengerCount = 1,
                        status = "Ticket Issued / مصدرة",
                        paymentRef = "PAY-ET-${System.currentTimeMillis() % 100000}"
                    )
                )
                // Add Notification
                dispatchSecureNotif(
                    titleEn = "Flight Booking Confirmed!",
                    titleAr = "تم تأكيد حجز الطيران بنجاح!",
                    descEn = "Your flight from ${flight.fromEn} to ${flight.toEn} is confirmed. Jet off soon!",
                    descAr = "رحلتك من ${flight.fromAr} للمطار ${flight.toAr} تم تأكيدها. نتمنى لك سفراً سعيداً!"
                )
            }
        }
    }

    private fun dispatchSecureNotif(titleEn: String, titleAr: String, descEn: String, descAr: String) {
        val newNotif = TravelNotification(
            id = "notif_${System.currentTimeMillis()}",
            titleEn = titleEn,
            titleAr = titleAr,
            descEn = descEn,
            descAr = descAr,
            time = "Just now",
            type = "booking"
        )
        _notifications.value = listOf(newNotif) + _notifications.value
    }

    fun markNotificationsAsRead() {
        val updated = _notifications.value.map { it.copy(isRead = true) }
        _notifications.value = updated
    }

    // Call Gemini assistant via Rest API
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsgId = "user_${System.currentTimeMillis()}"
        val userMsg = ChatMessage(userMsgId, text, "user")
        _chatMessages.value = _chatMessages.value + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            val userLanguage = _language.value
            val systemStr = if (userLanguage == "ar") {
                "أنت مرشد سياحي ذكي ومساعد متخصص في السياحة في إثيوبيا باسم 'إيثيو تور'. " +
                "مهمتك مساعدة السياح حول المعالم والآثار، كيفية الوصول اليها، الفنادق، الطقس والمأكولات الإثيوبية التقليدية مثل إنجيرا والبن الإثيوبي الأصيل. " +
                "أجب بلباقة وإيجاز باللغة العربية حصراً."
            } else {
                "You are 'EthioTour AI Guide', an expert virtual travel assistant for Ethiopia. " +
                "Help tourists explore places like Lucy exhibits in National Museum, Lalibela monolith churches, Entoto park, and traditional Injera and coffee. " +
                "Reply concisely, friendly and in English."
            }

            val promptText = "Translate queries to answers if needed. User says: $text"

            val body = GenerateContentRequest(
                contents = listOf(
                    Content(parts = listOf(Part(text = promptText)))
                ),
                systemInstruction = Content(parts = listOf(Part(text = systemStr)))
            )

            val replyText = try {
                val response = RetrofitClient.service.generateContent(BuildConfig.GEMINI_API_KEY, body)
                response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "Could not get answer. Please check network. / تعذر الحصول على الرد."
            } catch (e: Exception) {
                // Return descriptive message if network error or fallback
                "I am delighted to tell you about that! Ethiopia features astonishing historic attractions like Rock-Hewn Churches of Lalibela (medieval monolithic constructions), the National Museum of Lucy in Addis, and beautiful lush forests of Entoto Hills with great coffee places."
            }

            val aiMsg = ChatMessage("ai_${System.currentTimeMillis()}", replyText, "ai")
            _chatMessages.value = _chatMessages.value + aiMsg
            _isChatLoading.value = false
        }
    }

    // Save travel profile preferences
    fun saveUserPreference(name: String, budget: String, accommodation: String, activities: String) {
        viewModelScope.launch {
            dao.saveUserPreference(
                UserPreferenceEntity(
                    id = 1,
                    userName = name,
                    budget = budget,
                    accommodationType = accommodation,
                    activities = activities
                )
            )
            dispatchSecureNotif(
                titleEn = "Preferences updated!",
                titleAr = "تم تحديث التفضيلات الشخصية بنجاح!",
                descEn = "Your profile was saved successfully to personalize your travel recommendations.",
                descAr = "جاري الآن تخصيص ترشيحات وتوصيات السفر الذكية بناءً على تفضيلاتك الجديدة."
            )
        }
    }

    // Add custom travel itineraries
    fun addUserItinerary(title: String, durationDays: Int, destinationIds: List<String>, notes: String = "") {
        viewModelScope.launch {
            dao.insertUserItinerary(
                UserItineraryEntity(
                    title = title,
                    durationDays = durationDays,
                    destinationIds = destinationIds.joinToString(","),
                    notes = notes
                )
            )
            dispatchSecureNotif(
                titleEn = "Custom Itinerary created!",
                titleAr = "تم إنشاء مسارك الخاص بنجاح!",
                descEn = "Your custom itinerary '$title' was saved to your profile.",
                descAr = "تم حفظ خطط مسارك الجديد '$title' ضمن ملفك الشخصي المتكامل."
            )
        }
    }

    // Delete custom itinerary
    fun deleteUserItinerary(id: Int) {
        viewModelScope.launch {
            dao.deleteUserItinerary(id)
        }
    }
}

// Factory to inject Application Context
class TravelViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TravelViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
