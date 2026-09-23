package com.example.data.repository

import com.example.data.database.HealthMetricDao
import com.example.data.database.SyncEventDao
import com.example.data.database.UserProfileDao
import com.example.data.model.HealthMetric
import com.example.data.model.SyncEvent
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

class SyncRepository(
    private val syncEventDao: SyncEventDao,
    private val healthMetricDao: HealthMetricDao,
    private val userProfileDao: UserProfileDao
) {
    val allEvents: Flow<List<SyncEvent>> = syncEventDao.getAllSyncEvents()
    val allMetrics: Flow<List<HealthMetric>> = healthMetricDao.getAllMetrics()
    val allProfiles: Flow<List<UserProfile>> = userProfileDao.getAllProfiles()

    fun getMetricsForUser(userId: String): Flow<List<HealthMetric>> {
        return healthMetricDao.getMetricsForUser(userId)
    }

    suspend fun insertEvent(event: SyncEvent) {
        syncEventDao.insertSyncEvent(event)
    }

    suspend fun clearAllEvents() {
        syncEventDao.clearAllSyncEvents()
    }

    suspend fun insertMetric(metric: HealthMetric) {
        healthMetricDao.insertMetric(metric)
    }

    suspend fun insertMetrics(metrics: List<HealthMetric>) {
        healthMetricDao.insertMetrics(metrics)
    }

    suspend fun deleteMetricById(id: Int) {
        healthMetricDao.deleteMetric(id)
    }

    suspend fun clearAllMetrics() {
        healthMetricDao.clearAllMetrics()
    }

    suspend fun insertProfile(profile: UserProfile) {
        userProfileDao.insertProfile(profile)
    }

    suspend fun insertProfiles(profiles: List<UserProfile>) {
        userProfileDao.insertProfiles(profiles)
    }

    suspend fun deleteProfile(id: String) {
        userProfileDao.deleteProfile(id)
        healthMetricDao.deleteMetricsForUser(id)
    }

    suspend fun getProfileCount(): Int {
        return userProfileDao.getProfileCount()
    }

    suspend fun getMetricsCount(): Int {
        return healthMetricDao.getMetricsCount()
    }
}
