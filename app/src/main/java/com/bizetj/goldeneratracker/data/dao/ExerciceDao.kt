package com.bizetj.goldeneratracker.data.dao

import androidx.room.*
import com.bizetj.goldeneratracker.data.entity.Exercice
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciceDao {
    @Query("SELECT * FROM exercices WHERE seanceId = :seanceId")
    fun getExercicesBySeance(seanceId: Long): Flow<List<Exercice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercice(exercice: Exercice): Long

    @Update
    suspend fun updateExercice(exercice: Exercice)

    @Delete
    suspend fun deleteExercice(exercice: Exercice)
}