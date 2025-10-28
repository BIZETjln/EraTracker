package com.bizetj.goldeneratracker.data.repository

import com.bizetj.goldeneratracker.data.dao.ExerciceDao
import com.bizetj.goldeneratracker.data.entity.Exercice
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciceRepository @Inject constructor(
    private val exerciceDao: ExerciceDao
) {
    suspend fun updateExercice(exercice: Exercice) = exerciceDao.updateExercice(exercice)

    suspend fun deleteExercice(exercice: Exercice) = exerciceDao.deleteExercice(exercice)

    suspend fun insertExercice(exercice: Exercice) = exerciceDao.insertExercice(exercice)

    fun getExercicesBySeance(seanceId: Long): Flow<List<Exercice>> =
        exerciceDao.getExercicesBySeance(seanceId)
}