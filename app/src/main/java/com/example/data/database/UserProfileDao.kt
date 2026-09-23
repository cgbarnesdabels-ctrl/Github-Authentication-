package com.example.data.database

import androidx.room.*
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles ORDER BY createdAt ASC")
    fun getAllProfiles(): Flow<List<UserProfile>>

    @Query("SELECT * FROM user_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: String): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<UserProfile>)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("DELETE FROM user_profiles WHERE id = :id")
    suspend fun deleteProfile(id: String)

    @Query("SELECT COUNT(*) FROM user_profiles")
    suspend fun getProfileCount(): Int
}
