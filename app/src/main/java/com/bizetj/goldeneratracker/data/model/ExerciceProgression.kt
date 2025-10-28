package com.bizetj.goldeneratracker.data.model

data class ExerciceProgression(
    val exerciceId: Long,
    val nomExercice: String,
    val points: List<PointProgression>
)

data class PointProgression(
    val date: Long,
    val reps: Int,
    val poids: Float,
    val reussi: Boolean
)