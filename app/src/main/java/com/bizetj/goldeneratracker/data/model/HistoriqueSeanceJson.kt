package com.bizetj.goldeneratracker.data.model

data class HistoriqueSeanceJson(
    val exercices: List<ExerciceHistorique>
)

data class ExerciceHistorique(
    val exerciceId: Long,
    val nom: String,
    val objectif: Int,
    val poids: Float,
    val series: List<Int>,
    val reussi: Boolean
)