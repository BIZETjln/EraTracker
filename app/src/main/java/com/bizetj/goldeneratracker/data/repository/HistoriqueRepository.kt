package com.bizetj.goldeneratracker.data.repository

import com.bizetj.goldeneratracker.data.dao.HistoriqueSeanceDao
import com.bizetj.goldeneratracker.data.entity.HistoriqueSeance
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoriqueRepository @Inject constructor(
    private val historiqueSeanceDao: HistoriqueSeanceDao
) {
    fun getHistoriqueBySeance(seanceId: Long): Flow<List<HistoriqueSeance>> =
        historiqueSeanceDao.getHistoriqueBySeance(seanceId)

    fun getRecentHistorique(limit: Int = 10): Flow<List<HistoriqueSeance>> =
        historiqueSeanceDao.getRecentHistorique(limit)

    suspend fun insertHistorique(historique: HistoriqueSeance): Long =
        historiqueSeanceDao.insert(historique)
}