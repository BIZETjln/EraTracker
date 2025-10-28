package com.bizetj.goldeneratracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercices",
    foreignKeys = [
        ForeignKey(
            entity = Seance::class,
            parentColumns = ["id"],
            childColumns = ["seanceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["seanceId"])] // Ajoute l'index ici
)
data class Exercice(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val seanceId: Long,
    val nom: String,
    val nombreSeries: Int,
    val borneMin: Int,
    val borneMax: Int,
    val poidsActuel: Float,
    val repsActuelles: Int = borneMin,
    val incrementPoids: Float = 2.5f
)