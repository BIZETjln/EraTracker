package com.bizetj.goldeneratracker.data.repository

import com.bizetj.goldeneratracker.data.dao.SeanceDao
import com.bizetj.goldeneratracker.data.entity.Seance
import com.bizetj.goldeneratracker.data.model.SeanceAvecExercices
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeanceRepository @Inject constructor(
    private val seanceDao: SeanceDao
) {
    fun getAllSeances(): Flow<List<Seance>> = seanceDao.getAllSeances()

    suspend fun getSeanceAvecExercices(seanceId: Long): SeanceAvecExercices? =
        seanceDao.getSeanceAvecExercices(seanceId)

    suspend fun insertSeance(seance: Seance): Long = seanceDao.insert(seance)

    suspend fun updateSeance(seance: Seance) = seanceDao.update(seance)

    suspend fun deleteSeance(seance: Seance) = seanceDao.delete(seance)
}