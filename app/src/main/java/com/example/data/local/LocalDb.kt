package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemType: String, // "flight" or "hotel"
    val itemName: String,
    val date: String,
    val details: String,
    val price: Double,
    val passengerCount: Int,
    val status: String,
    val paymentRef: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val placeId: String,
    val userName: String,
    val comment: String,
    val rating: Float,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val placeId: String
)

@Entity(tableName = "user_preference")
data class UserPreferenceEntity(
    @PrimaryKey val id: Int = 1,
    val userName: String = "",
    val budget: String = "Medium", // "Low", "Medium", "High"
    val accommodationType: String = "Resort", // "Resort", "Hotel", "Lodge"
    val activities: String = "NATURE,HISTORY" // Comma-separated Categories
)

@Entity(tableName = "user_itineraries")
data class UserItineraryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val durationDays: Int,
    val destinationIds: String, // Comma-separated destination product IDs
    val notes: String = ""
)

@Dao
interface TravelDao {
    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("SELECT * FROM reviews WHERE placeId = :placeId ORDER BY timestamp DESC")
    fun getReviewsForPlace(placeId: String): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE placeId = :placeId")
    suspend fun removeFavorite(placeId: String)

    @Query("SELECT * FROM user_preference WHERE id = 1")
    fun getUserPreference(): Flow<UserPreferenceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserPreference(prefs: UserPreferenceEntity)

    @Query("SELECT * FROM user_itineraries ORDER BY id DESC")
    fun getAllUserItineraries(): Flow<List<UserItineraryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserItinerary(itinerary: UserItineraryEntity)

    @Query("DELETE FROM user_itineraries WHERE id = :id")
    suspend fun deleteUserItinerary(id: Int)
}

@Database(
    entities = [
        BookingEntity::class,
        ReviewEntity::class,
        FavoriteEntity::class,
        UserPreferenceEntity::class,
        UserItineraryEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun travelDao(): TravelDao

    companion object {
        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getDatabase(context: Context): TravelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java,
                    "ethio_tour_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

