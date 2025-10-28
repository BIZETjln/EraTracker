package com.bizetj.goldeneratracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "historique_seances",
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
data class HistoriqueSeance(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val seanceId: Long,
    val date: Long = System.currentTimeMillis(),
    val objectifAtteint: Boolean,
    val donneesJson: String
)