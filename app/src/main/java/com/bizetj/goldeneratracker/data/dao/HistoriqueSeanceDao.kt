package com.bizetj.goldeneratracker.data.dao

import androidx.room.*
import com.bizetj.goldeneratracker.data.entity.HistoriqueSeance
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoriqueSeanceDao {
    @Query("SELECT * FROM historique_seances WHERE seanceId = :seanceId ORDER BY date DESC")
    fun getHistoriqueBySeance(seanceId: Long): Flow<List<HistoriqueSeance>>

    @Query("SELECT * FROM historique_seances ORDER BY date DESC LIMIT :limit")
    fun getRecentHistorique(limit: Int): Flow<List<HistoriqueSeance>>

    @Insert
    suspend fun insert(historique: HistoriqueSeance): Long
}