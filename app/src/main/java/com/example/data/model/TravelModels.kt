package com.example.data.model

enum class Category {
    NATURE, HISTORY, CULTURE, ADVENTURE
}

data class Destination(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val descriptionEn: String,
    val descriptionAr: String,
    val locationEn: String,
    val locationAr: String,
    val howToGoEn: String,
    val howToGoAr: String,
    val distanceTextEn: String,
    val distanceTextAr: String,
    val category: Category,
    val inAddis: Boolean, // true for inside Addis Ababa, false for outside
    val imageUrl: String,
    val baseTicketPriceUSD: Double,
    val latitude: Float, // Mock map coordinates x-offset (%)
    val longitude: Float, // Mock map coordinates y-offset (%)
    val initialRating: Float,
    val bestTimeToVisitEn: String = "All year round",
    val bestTimeToVisitAr: String = "طوال العام"
)

data class Hotel(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val locationEn: String,
    val locationAr: String,
    val pricePerNightUSD: Double,
    val imageUrl: String,
    val starRating: Float,
    val amenitiesEn: List<String>,
    val amenitiesAr: List<String>
)

data class Flight(
    val id: String,
    val airlineEn: String,
    val airlineAr: String,
    val fromEn: String,
    val fromAr: String,
    val toEn: String,
    val toAr: String,
    val durationEn: String,
    val durationAr: String,
    val priceUSD: Double,
    val time: String
)

data class SuggestedItinerary(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val durationEn: String,
    val durationAr: String,
    val durationDays: Int,
    val interestTypeEn: String, // "Historical Sites", "Natural Beauty", "Wildlife & Adventure"
    val interestTypeAr: String, // "المواقع التاريخية", "الجمال الطبيعي", "الحياة البرية والمغامرة"
    val descriptionEn: String,
    val descriptionAr: String,
    val destinationsIncluded: List<String>, // list of destination ids
    val accommodationOptions: List<String>, // list of hotel/resort ids
    val transitEn: String,
    val transitAr: String
)

data class MediaItem(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val placeNameEn: String,
    val placeNameAr: String,
    val imageUrl: String,
    val mediaType: String, // "VIDEO", "PANORAMA"
    val durationOrAngle: String, // e.g., "02:15" or "360°"
    val descriptionEn: String = "",
    val descriptionAr: String = ""
)

object TravelData {
    val destinations = listOf(
        Destination(
            id = "lalibela",
            nameEn = "Rock-Hewn Churches of Lalibela",
            nameAr = "كنائس لاليبيلا الصخرية",
            descriptionEn = "Known as the eighth wonder of the world, these 11 magnificent medieval monolithic churches were carved out of solid volcanic rock in the 12th century.",
            descriptionAr = "تُعرف باعتبارها الأعجوبة الثامنة في العالم، وهي عبارة عن 11 كنيسة مغليثية رائعة من العصور الوسطى نحتت بالكامل في الصخور البركانية الصلبة في القرن الثاني عشر.",
            locationEn = "Lalibela, Amhara Region",
            locationAr = "لاليبيلا، إقليم أمهرة",
            howToGoEn = "Fly from Addis Ababa Bole Airport to Lalibela Airport (1 hr), then take a shuttle (20 mins). Direct domestic daily flights are operated by Ethiopian Airlines.",
            howToGoAr = "طيران مباشر من مطار بولي الدولي بأديس أبابا إلى مطار لاليبيلا (ساعة واحدة)، ثم حافلة نقل (20 دقيقة). تسير الخطوط الإثيوبية رحلات يومية.",
            distanceTextEn = "640 km from Addis Ababa",
            distanceTextAr = "640 كم من أديس أبابا",
            category = Category.HISTORY,
            inAddis = false,
            imageUrl = "https://images.unsplash.com/photo-1544013919-4b4bac8720f1?auto=format&fit=crop&w=800&q=80",
            baseTicketPriceUSD = 50.0,
            latitude = 0.45f,
            longitude = 0.35f,
            initialRating = 4.9f,
            bestTimeToVisitEn = "October to March",
            bestTimeToVisitAr = "من أكتوبر إلى مارس"
        ),
        Destination(
            id = "national_mus",
            nameEn = "National Museum of Ethiopia",
            nameAr = "المتحف الوطني الإثيوبي",
            descriptionEn = "Home to 'Lucy' (Dinknesh), a 3.2 million-year-old hominid fossil. The museum offers an incredible collection of archeological, artistic, and historical treasures of East Africa.",
            descriptionAr = "موطن 'لوسي' (دينقنش)، وهي أحفورة بشرية يعود تاريخها إلى 3.2 مليون سنة. يقدم المتحف مجموعة مذهلة من الكنوز الأثرية والفنية والتاريخية لشرق إفريقيا.",
            locationEn = "King George VI Street, Addis Ababa",
            locationAr = "شارع الملك جورج السادس، أديس أبابا",
            howToGoEn = "Easily accessible by taxi, Uber-equivalent (Ride), or the capital's light rail train to Amist Kilo station.",
            howToGoAr = "يمكن الوصول إليه بسهولة عن طريق سيارات الأجرة أو تطبيقات التوصيل الشهيرة (Ride)، أو القطار الخفيف في العاصمة وصولاً لمحطة 'أمست كيلو'.",
            distanceTextEn = "Within Addis Ababa Main Center",
            distanceTextAr = "داخل وسط أديس أبابا الرئيسي",
            category = Category.HISTORY,
            inAddis = true,
            imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80",
            baseTicketPriceUSD = 5.0,
            latitude = 0.51f,
            longitude = 0.52f,
            initialRating = 4.7f,
            bestTimeToVisitEn = "Year round",
            bestTimeToVisitAr = "طوال العام"
        ),
        Destination(
            id = "entoto_park",
            nameEn = "Entoto Hill & Natural Park",
            nameAr = "جبل وحديقة إنتوتو الطبيعية",
            descriptionEn = "A lush mountain forest offering stunning panoramic views of Addis Ababa, historical palaces, walking trails, horse riding, and a world-class space observatory center.",
            descriptionAr = "غابة جبلية مورقة توفر إطلالات بانورامية خلابة على أديس أبابا، وتضم قصوراً تاريخية وممرات مشي ومراكز ركوب خيل ومرصد فضائي عالمي المستوى.",
            locationEn = "Northern Ridge, Addis Ababa",
            locationAr = "المرتفعات الشمالية، أديس أبابا",
            howToGoEn = "Take a taxi (Ride) north of Addis Ababa or standard minibus to the foothill of Entoto Hill, then ride up.",
            howToGoAr = "استقل سيارة أجرة شمال أديس أبابا أو حافلة صغيرة إلى سفح جبل إنتوتو، ثم اصعد إلى المدخل الرئيسي للحديقة.",
            distanceTextEn = "10 km north of city center",
            distanceTextAr = "10 كم شمال وسط المدينة",
            category = Category.NATURE,
            inAddis = true,
            imageUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=800&q=80",
            baseTicketPriceUSD = 8.0,
            latitude = 0.49f,
            longitude = 0.47f,
            initialRating = 4.8f,
            bestTimeToVisitEn = "October to May",
            bestTimeToVisitAr = "من أكتوبر إلى مايو"
        ),
        Destination(
            id = "simien_mts",
            nameEn = "Simien Mountains National Park",
            nameAr = "منتزه جبال سيميان الوطني",
            descriptionEn = "A breathtaking UNESCO World Heritage site known for its deep valleys, jagged peaks, and rare endemic wildlife including the Walia Ibex and Gelada baboons.",
            descriptionAr = "موقع مذهل مدرج ضمن قائمة التراث العالمي لليونسكو، ويشتهر بوديانه العميقة وقممه المتعرجة والحياة البرية المتوطنة والنادرة مثل وعل واليا وقرد الجيلادا الشهير.",
            locationEn = "Gondar/Debark, Amhara Region",
            locationAr = "غوندار/ديبارك، إقليم أمهرة",
            howToGoEn = "Fly from Addis to Gondar Airport (1 hr), then take a private 4x4 vehicle (2 hrs) north to the Debark Park gateway.",
            howToGoAr = "طيران من أديس أبابا لغوندار (ساعة واحدة)، ثم سيارة دفع رباعي خاصة (ساعتين) شمالاً حتى بوابة المتنزه في منطقة ديبارك.",
            distanceTextEn = "780 km from Addis Ababa",
            distanceTextAr = "780 كم من أديس أبابا",
            category = Category.NATURE,
            inAddis = false,
            imageUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80",
            baseTicketPriceUSD = 40.0,
            latitude = 0.38f,
            longitude = 0.31f,
            initialRating = 4.9f,
            bestTimeToVisitEn = "September to November",
            bestTimeToVisitAr = "من سبتمبر إلى نوفمبر"
        ),
        Destination(
            id = "gondar_castle",
            nameEn = "Fasil Ghebbi Castles",
            nameAr = "قلاع فاسيل غيبي في غوندار",
            descriptionEn = "Known as the Camelot of Africa, this 17th-century fortress city hosts unique stone castles of Emperor Fasilides, blending European and African medieval architecture.",
            descriptionAr = "تُعرف باسم 'كاميلوت إفريقيا'، وهي مدينة محصنة من القرن السابع عشر تضم قلاعاً حجرية فريدة للإمبراطور فاسيليداس، تجمع بين الهندسة الأوروبية والإفريقية في العصور الوسطى.",
            locationEn = "Gondar, North West Area",
            locationAr = "غوندار، المنطقة الشمالية الغربية",
            howToGoEn = "Take a daily direct flight (1 hr) with Ethiopian Airlines or travel via Bahir Dar along a beautifully paved highway.",
            howToGoAr = "استقل رحلة طيران يومية مباشرة (ساعة واحدة) مع الخطوط الإثيوبية، أو سافر براً عبر بحر دار عبر طريق معبّد وخلاب.",
            distanceTextEn = "730 km from Addis Ababa",
            distanceTextAr = "730 كم من أديس أبابا",
            category = Category.HISTORY,
            inAddis = false,
            imageUrl = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?auto=format&fit=crop&w=800&q=80",
            baseTicketPriceUSD = 25.0,
            latitude = 0.40f,
            longitude = 0.33f,
            initialRating = 4.8f,
            bestTimeToVisitEn = "October to February",
            bestTimeToVisitAr = "من أكتوبر إلى فبراير"
        ),
        Destination(
            id = "danakil_dep",
            nameEn = "Danakil Depression & Erta Ale",
            nameAr = "منخفض الدناكل وبركان إرتا ألي",
            descriptionEn = "One of the most extreme, hottest, and lowest land formations on earth. Explore glowing yellow sulfur spring fields, massive boiling salt flats, and the fiery permanent lava lake of Erta Ale Volcano.",
            descriptionAr = "واحد من أكثر التكوينات الأرضية تطرفاً وحرارة وانخفاضاً في العالم. استكشف حقول الكبريت الصفراء وحقول الملح الشاسعة وبحيرة الحمم البركانية الدائمة لبركان إرتا ألي الثائر.",
            locationEn = "Afar Triangle, North-Eastern Region",
            locationAr = "المثلث العفري، الإقليم الشمالي الشرقي",
            howToGoEn = "Fly to Semera or Mekelle Airport, then participate in a strictly mandatory guided 4x4 expedition with security escorts and professional guides.",
            howToGoAr = "سافر بالطائرة إلى مطار سميرة أو ميكيلي، ثم شارك في رحلة استكشافية إلزامية بسيارات الدفع الرباعي برفقة مرشدين محليين وحراسة أمنية.",
            distanceTextEn = "580 km from city center",
            distanceTextAr = "580 كم من العاصمة أديس",
            category = Category.ADVENTURE,
            inAddis = false,
            imageUrl = "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?auto=format&fit=crop&w=800&q=80",
            baseTicketPriceUSD = 120.0,
            latitude = 0.58f,
            longitude = 0.28f,
            initialRating = 5.0f,
            bestTimeToVisitEn = "November to January",
            bestTimeToVisitAr = "من نوفمبر إلى يناير"
        ),
        Destination(
            id = "trinity_cath",
            nameEn = "Holy Trinity Cathedral",
            nameAr = "كاتدرائية الثالوث المقدس",
            descriptionEn = "One of the highest orthodox cathedral architecture styles in East Africa, containing beautiful frescoes, stained glass windows, and the imperial tombs of Emperor Haile Selassie and Empress Menen Asfaw.",
            descriptionAr = "واحدة من أرقى تصاميم الكاتدرائيات الأرثوذكسية في شرق إفريقيا، وتضم لوحات جدارية جميلة ونوافذ زجاجية ملونة، والأضرحة الإمبراطورية للإمبراطور هيلا سيلاسي وزوجته الإمبراطورة مينين ونخب عظيمة.",
            locationEn = "Arat Kilo, Addis Ababa",
            locationAr = "أرات كيلو، أديس أبابا",
            howToGoEn = "Central location, easy to access by any light rail transit, taxi (Ride) or city bus within minutes.",
            howToGoAr = "موقع مركزي يتوسط المدينة، يسهل الوصول إليه بأي قطار خفيف، أو سيارة أجرة (Ride) أو حافلات النقل الحضري.",
            distanceTextEn = "In Addis Ababa City Core",
            distanceTextAr = "في قلب العاصمة أديس أبابا",
            category = Category.CULTURE,
            inAddis = true,
            imageUrl = "https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=800&q=80",
            baseTicketPriceUSD = 10.0,
            latitude = 0.52f,
            longitude = 0.53f,
            initialRating = 4.6f,
            bestTimeToVisitEn = "Dry season (September to May)",
            bestTimeToVisitAr = "موسم الجفاف (سبتمبر إلى مايو)"
        )
    )

    val hotels = listOf(
        Hotel(
            id = "kuriftu_resort",
            nameEn = "Kuriftu Resort & Spa Bishoftu",
            nameAr = "منتجع وسبا كورفتو بيشوفتو الفاخر",
            locationEn = "Bishoftu (45 km from Addis Ababa)",
            locationAr = "بيشوفتو (على بعد 45 كم من أديس أبابا)",
            pricePerNightUSD = 210.0,
            imageUrl = "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=800&q=80",
            starRating = 5.0f,
            amenitiesEn = listOf("Lake View", "Swimming Pool", "Premium Spa", "Water Park", "Free WiFi"),
            amenitiesAr = listOf("إطلالة على البحيرة", "مسبح خارجي", "سبا فاخر", "ألعاب مائية", "واي فاي")
        ),
        Hotel(
            id = "babogaya_resort",
            nameEn = "Babogaya Lake Sunrise Resort",
            nameAr = "منتجع بابوجايا ذو الإطلالة الساحرة للبحيرة",
            locationEn = "Lake Babogaya, Bishoftu (45 km from Addis)",
            locationAr = "بحيرة بابوجايا، بيشوفتو (على بعد 45 كم من أديس)",
            pricePerNightUSD = 140.0,
            imageUrl = "https://images.unsplash.com/photo-1584132967334-10e028bd69f7?auto=format&fit=crop&w=800&q=80",
            starRating = 4.5f,
            amenitiesEn = listOf("Lakeside Dining", "Kayaking", "Free WiFi", "Garden Terrace"),
            amenitiesAr = listOf("مطعم ضفاف البحيرة", "تجديف قوارب", "واي فاي مجاني", "تراس حديقة")
        ),
        Hotel(
            id = "negash_resort",
            nameEn = "Negash Resort Wolliso",
            nameAr = "منتجع نجاش وليسّو ذو الينابيع الدافئة",
            locationEn = "Wolliso (110 km from Addis Ababa)",
            locationAr = "وليسّو (على بعد 110 كم من أديس أبابا)",
            pricePerNightUSD = 120.0,
            imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=800&q=80",
            starRating = 4.2f,
            amenitiesEn = listOf("Hot Springs", "Tropical Gardens", "Swimming Pool", "Traditional Restaurant"),
            amenitiesAr = listOf("ينابيع حارة طبيعية", "حدائق استوائية", "مسبح دافئ", "مطعم بلدي")
        ),
        Hotel(
            id = "sheraton_addis",
            nameEn = "Sheraton Addis luxury Collection",
            nameAr = "شيراتون أديس - فاخر ومتكامل",
            locationEn = "Taitu Street, Addis Ababa",
            locationAr = "شارع تايتو، أديس أبابا",
            pricePerNightUSD = 240.0,
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80",
            starRating = 5.0f,
            amenitiesEn = listOf("Free WiFi", "Swimming Pool", "Premium Spa", "Airport Shuttle"),
            amenitiesAr = listOf("واي فاي مجاني", "مسبح خارجي", "سبا فاخر", "توصيل للمطار")
        ),
        Hotel(
            id = "skylight_hotel",
            nameEn = "Ethiopian Skylight Hotel",
            nameAr = "فندق سكاي لايت الإثيوبي",
            locationEn = "Bole Road, Addis Ababa (Near Airport)",
            locationAr = "طريق بولي، أديس أبابا (جوار المطار)",
            pricePerNightUSD = 180.0,
            imageUrl = "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=800&q=80",
            starRating = 5.0f,
            amenitiesEn = listOf("Free WiFi", "Modern Gym", "Fine Dining", "Free Bole shuttle"),
            amenitiesAr = listOf("واي فاي مجاني", "صالة رياضية", "مطاعم راقية", "نقل مجاني للمطار")
        ),
        Hotel(
            id = "lasta_lalibela",
            nameEn = "Lasta Mountain Lodge",
            nameAr = "نزل دير لاستي الجبلي لاليبيلا",
            locationEn = "Historic Cliff Ridge, Lalibela",
            locationAr = "منحدر لاساتا الجبلي، لاليبيلا",
            pricePerNightUSD = 110.0,
            imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=800&q=80",
            starRating = 4.5f,
            amenitiesEn = listOf("Panoramic View", "Traditional Food", "Trekking Guide", "Free WiFi"),
            amenitiesAr = listOf("إطلالة بانورامية", "طعام إثيوبي تقليدي", "مرشد للتسلق", "واي فاي")
        ),
        Hotel(
            id = "goha_gondar",
            nameEn = "Goha Hotel Castle View",
            nameAr = "فندق غوها - إطلالة القلاع غوندار",
            locationEn = "Castle Hillside, Gondar",
            locationAr = "تلة القلاع التاريخية، غوندار",
            pricePerNightUSD = 95.0,
            imageUrl = "https://images.unsplash.com/photo-1495365200479-c4ed1d35e1aa?auto=format&fit=crop&w=800&q=80",
            starRating = 4.0f,
            amenitiesEn = listOf("Historic Views", "Cozy Fireplace", "Terrace Cafe", "WiFi"),
            amenitiesAr = listOf("إطلالة تاريخية", "مدفأة دافئة", "مقهى وتراس خارجي", "واي فاي")
        )
    )

    val flights = listOf(
        Flight(
            id = "et_flight_01",
            airlineEn = "Ethiopian Airlines (ET304)",
            airlineAr = "الخطوط الجوية الإثيوبية (ET304)",
            fromEn = "London Heathrow (LHR)",
            fromAr = "لندن هيثرو (LHR)",
            toEn = "Addis Ababa Bole (ADD)",
            toAr = "أديس أبابا بولي (ADD)",
            durationEn = "7 hrs 20 mins (Direct)",
            durationAr = "7 ساعات و20 دقيقة (مباشر)",
            priceUSD = 720.0,
            time = "10:15 AM - 08:35 PM"
        ),
        Flight(
            id = "et_flight_02",
            airlineEn = "Ethiopian Airlines (ET120)",
            airlineAr = "الخطوط الجوية الإثيوبية (ET120)",
            fromEn = "Cairo Intl (CAI)",
            fromAr = "مطار القاهرة الدولي (CAI)",
            toEn = "Addis Ababa Bole (ADD)",
            toAr = "أديس أبابا بولي (ADD)",
            durationEn = "3 hrs 50 mins (Direct)",
            durationAr = "3 ساعات و50 دقيقة (مباشر)",
            priceUSD = 450.0,
            time = "02:20 AM - 07:10 AM"
        ),
        Flight(
            id = "et_flight_03",
            airlineEn = "Ethiopian Airlines (ET202)",
            airlineAr = "الخطوط الجوية الإثيوبية (ET202)",
            fromEn = "Dubai Intl (DXB)",
            fromAr = "مطار دبي الدولي (DXB)",
            toEn = "Addis Ababa Bole (ADD)",
            toAr = "أديس أبابا بولي (ADD)",
            durationEn = "4 hrs 10 mins (Direct)",
            durationAr = "4 ساعات و10 دقيقة (مباشر)",
            priceUSD = 480.0,
            time = "04:30 PM - 08:40 PM"
        ),
        Flight(
            id = "et_flight_04",
            airlineEn = "Ethiopian Airlines (ET501)",
            airlineAr = "الخطوط الجوية الإثيوبية (ET501)",
            fromEn = "Washington Dulles (IAD)",
            fromAr = "واشنطن دولس (IAD)",
            toEn = "Addis Ababa Bole (ADD)",
            toAr = "أديس أبابا بولي (ADD)",
            durationEn = "13 hrs 15 mins (Direct)",
            durationAr = "13 ساعة و15 دقيقة (مباشر)",
            priceUSD = 1150.0,
            time = "11:00 AM - 07:15 AM (+1)"
        )
    )

    val suggestedItineraries = listOf(
        SuggestedItinerary(
            id = "itinerary_3d_history",
            titleEn = "Historical Wonders Quick Tour",
            titleAr = "جولة عجائب التاريخ السريعة",
            durationEn = "3 Days / 2 Nights",
            durationAr = "3 أيام / ليلتان",
            durationDays = 3,
            interestTypeEn = "Historical Sites",
            interestTypeAr = "المواقع التاريخية",
            descriptionEn = "Explore the amazing medieval monolithic churches of Lalibela and the rich cultural treasures of the National Museum in standard capital routes.",
            descriptionAr = "استكشف الكنائس المغليثية الفريدة من العصور الوسطى في لاليبيلا والمواهب والكنوز الثقافية في المتحف الوطني والمحطات التاريخية بالعاصمة.",
            destinationsIncluded = listOf("lalibela", "national_mus", "trinity_cath"),
            accommodationOptions = listOf("sheraton_addis", "lasta_lalibela"),
            transitEn = "Domestic Flight (Ethiopian Airlines LHR/ADD/LLI) + High quality Coaster Bus Shuttle",
            transitAr = "طيران داخلي (الخطوط الإثيوبية) + حافلات سياحية مكيفة وحديثة"
        ),
        SuggestedItinerary(
            id = "itinerary_7d_nature",
            titleEn = "Highland Nature & Escape Resorts",
            titleAr = "طبيعة المرتفعات ومنتجعات الاستجمام",
            durationEn = "7 Days / 6 Nights",
            durationAr = "7 أيام / 6 ليالٍ",
            durationDays = 7,
            interestTypeEn = "Natural Beauty",
            interestTypeAr = "الجمال الطبيعي",
            descriptionEn = "Immerse in breathtaking scenery. Hike Entoto Hill, explore the jagged peaks of Simien Mountains, and wind down at premium resorts close to the capital (Bishoftu lakeside).",
            descriptionAr = "رحلة خلابة لعشاق الطبيعة والهواء النقي والاستجمام الفاخر. تسلق غابات إنتوتو وقمم سيميان، تليها إقامة فاخرة وهادئة بمنتجعات البحيرة القريبة من العاصمة بيشوفتو.",
            destinationsIncluded = listOf("entoto_park", "simien_mts"),
            accommodationOptions = listOf("kuriftu_resort", "babogaya_resort", "negash_resort"),
            transitEn = "Comfortable Private 4x4 Land Cruiser + Dedicated lakeside resort shuttles",
            transitAr = "سيارة جيب لاند كروزر دفع رباعي حديثة وخاصة + حافلات النقل المخصصة للمنتجعات"
        ),
        SuggestedItinerary(
            id = "itinerary_7d_adventure",
            titleEn = "Afar Triangle & Endemic Wildlife Safari",
            titleAr = "سفاري مغامرة منخفض الدناكل والحياة البرية بالمرتفعات",
            durationEn = "7 Days / 6 Nights",
            durationAr = "7 أيام / 6 ليالٍ",
            durationDays = 7,
            interestTypeEn = "Wildlife & Adventure",
            interestTypeAr = "الحياة البرية والمغامرة",
            descriptionEn = "Expedition to Erta Ale boiling lava lake, glowing sulfur dunes of Dallol in Danakil Depression, and dynamic trekking close to unique Gelada Baboons in Simien Mountains.",
            descriptionAr = "جولة استثنائية لأصحاب القلوب الشجاعة وعشاق الاكتشاف. استكشف بحيرة بركان إرتا ألي وحقول الكبريت الملونة بد Dallol بالدناكل، وتسلّق جبال سيميان لرصد قردة الجيلادا والوعل الإثيوبي النادر.",
            destinationsIncluded = listOf("danakil_dep", "simien_mts"),
            accommodationOptions = listOf("skylight_hotel", "goha_gondar"),
            transitEn = "Strictly guided 4x4 convoy + Regional scouts escorts",
            transitAr = "قافلة جيب دفع رباعي سياحية مرافقة بمرشدين محليين وحراسة أمنية منظمة"
        )
    )

    val mediaItems = listOf(
        MediaItem(
            id = "media_lalibela_pano",
            titleEn = "Lalibela Mono-church virtual walk",
            titleAr = "جولة افتراضية بانورامية في لاليبيلا",
            placeNameEn = "Lalibela, Amhara Region",
            placeNameAr = "لاليبيلا، إقليم أمهرة",
            imageUrl = "https://images.unsplash.com/photo-1544013919-4b4bac8720f1?auto=format&fit=crop&w=1200&q=80",
            mediaType = "PANORAMA",
            durationOrAngle = "360°",
            descriptionEn = "Experience what it feels like standing at the foot of the monolithic Rock-Hewn Churches. Slide left or right to explore.",
            descriptionAr = "اختبر روعة الوقوف تحت أقدام المعلمين التاريخيين المنحوتين في الصخر بالكامل. اسحب يميناً أو يساراً للاستكشاف البانورامي."
        ),
        MediaItem(
            id = "media_entoto_video",
            titleEn = "Entoto Canopy Walking Experience",
            titleAr = "فيديو تجربة المشي الفاخر بمنتزه إنتوتو",
            placeNameEn = "Entoto Park, Addis Ababa",
            placeNameAr = "منتزه إنتوتو، أديس أبابا",
            imageUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=1200&q=80",
            mediaType = "VIDEO",
            durationOrAngle = "02:15",
            descriptionEn = "Fly over the high lush canopies, walking trails, and space observatory in our exclusive high-definition video show.",
            descriptionAr = "حلق فوق غابات الأوكالبتوس المورقة ومسارات المشي المشوقة والمرصد الفلكي العالمي في عرض حي ومميز عالي الدقة."
        ),
        MediaItem(
            id = "media_erta_pano",
            titleEn = "Erta Ale Boiling Lava Edge Glow",
            titleAr = "بانوراما توهج بركان إرتا ألي النشط",
            placeNameEn = "Erta Ale Volcano, Afar Triangle",
            placeNameAr = "بركان إرتا ألي، منخفض الدناكل عفر",
            imageUrl = "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?auto=format&fit=crop&w=1200&q=80",
            mediaType = "PANORAMA",
            durationOrAngle = "360°",
            descriptionEn = "Safely peek inside the active crater lake of Erta Ale, glowing in fiery golden patterns. Pan left/right.",
            descriptionAr = "ألقِ نظرة آمنة لقلب الفوهة البركانية الثائرة والمتوهجة في إرتا ألي. اسحب الشاشة لاستعراض المشهد كاملاً بزاوية 360 درجة."
        ),
        MediaItem(
            id = "media_resorts_video",
            titleEn = "Lakeside Luxury & Spas",
            titleAr = "لقطات سينمائية لمنتجعات البحيرة بيشوفتو",
            placeNameEn = "Bishoftu Resorts near Capital",
            placeNameAr = "منتجعات بيشوفتو القريبة من العاصمة",
            imageUrl = "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1200&q=80",
            mediaType = "VIDEO",
            durationOrAngle = "01:30",
            descriptionEn = "Watch the serene morning mist rise over Kuriftu lakes and spa facilities, perfect weekend getaways.",
            descriptionAr = "شاهد الضباب الصباحي الهادئ يرتفع فوق مياه بحيرة كورفتو الخلابة والمسابح وجلسات التدليك الفارهة على بعد دقائق من قلب العاصمة."
        )
    )
}
