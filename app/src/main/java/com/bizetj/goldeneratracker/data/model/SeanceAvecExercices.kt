package com.bizetj.goldeneratracker.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.bizetj.goldeneratracker.data.entity.Exercice
import com.bizetj.goldeneratracker.data.entity.Seance

data class SeanceAvecExercices(
    @Embedded val seance: Seance,
    @Relation(
        parentColumn = "id",
        entityColumn = "seanceId"
    )
    val exercices: List<Exercice>
)