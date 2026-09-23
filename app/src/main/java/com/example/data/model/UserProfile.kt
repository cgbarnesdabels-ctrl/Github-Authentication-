package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: String, // Unique user ID (e.g. usr_alex_01)
    val name: String,
    val email: String = "",
    val age: Int = 28,
    val bloodType: String = "O+",
    val avatarColorHex: String = "#10B981", // Emerald accent
    val syncTarget: String = "Primary Health Ledger",
    val createdAt: Long = System.currentTimeMillis()
)
