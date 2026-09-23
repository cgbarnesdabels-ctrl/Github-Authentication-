package com.example.data.database

import androidx.room.*
import com.example.data.model.HealthMetric
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthMetricDao {
    @Query("SELECT * FROM health_metrics WHERE userId = :userId ORDER BY timestamp DESC")
    fun getMetricsForUser(userId: String): Flow<List<HealthMetric>>

    @Query("SELECT * FROM health_metrics ORDER BY timestamp DESC")
    fun getAllMetrics(): Flow<List<HealthMetric>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetric(metric: HealthMetric): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetrics(metrics: List<HealthMetric>)

    @Query("DELETE FROM health_metrics WHERE id = :id")
    suspend fun deleteMetric(id: Int)

    @Query("DELETE FROM health_metrics WHERE userId = :userId")
    suspend fun deleteMetricsForUser(userId: String)

    @Query("DELETE FROM health_metrics")
    suspend fun clearAllMetrics()

    @Query("SELECT COUNT(*) FROM health_metrics")
    suspend fun getMetricsCount(): Int

    @Query("SELECT COUNT(*) FROM health_metrics WHERE userId = :userId")
    suspend fun getMetricsCountForUser(userId: String): Int
}
