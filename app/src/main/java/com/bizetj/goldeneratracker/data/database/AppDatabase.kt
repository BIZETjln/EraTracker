package com.bizetj.goldeneratracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bizetj.goldeneratracker.data.dao.ExerciceDao
import com.bizetj.goldeneratracker.data.dao.HistoriqueSeanceDao
import com.bizetj.goldeneratracker.data.dao.SeanceDao
import com.bizetj.goldeneratracker.data.entity.Exercice
import com.bizetj.goldeneratracker.data.entity.HistoriqueSeance
import com.bizetj.goldeneratracker.data.entity.Seance

@Database(
    entities = [Seance::class, Exercice::class, HistoriqueSeance::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun seanceDao(): SeanceDao
    abstract fun exerciceDao(): ExerciceDao
    abstract fun historiqueSeanceDao(): HistoriqueSeanceDao
}