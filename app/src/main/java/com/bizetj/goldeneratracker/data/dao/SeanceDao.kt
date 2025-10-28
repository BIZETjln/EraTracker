package com.bizetj.goldeneratracker.data.dao

import androidx.room.*
import com.bizetj.goldeneratracker.data.entity.Seance
import com.bizetj.goldeneratracker.data.model.SeanceAvecExercices
import kotlinx.coroutines.flow.Flow

@Dao
interface SeanceDao {
    @Query("SELECT * FROM seances ORDER BY dateCreation DESC")
    fun getAllSeances(): Flow<List<Seance>>

    @Query("SELECT * FROM seances WHERE id = :seanceId")
    suspend fun getSeanceById(seanceId: Long): Seance?

    @Transaction
    @Query("SELECT * FROM seances WHERE id = :seanceId")
    suspend fun getSeanceAvecExercices(seanceId: Long): SeanceAvecExercices?

    @Insert
    suspend fun insert(seance: Seance): Long

    @Update
    suspend fun update(seance: Seance)

    @Delete
    suspend fun delete(seance: Seance)
}