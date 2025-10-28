package com.bizetj.goldeneratracker.data.model

data class SerieRealisee(
    val numeroSerie: Int,
    val repsRealisees: Int? = null // null = pas encore saisi
)

data class ExerciceEnCours(
    val exerciceId: Long,
    val nom: String,
    val objectifReps: Int,
    val poids: Float,
    val nombreSeries: Int,
    val series: List<SerieRealisee>
) {
    val estTermine: Boolean
        get() = series.all { it.repsRealisees != null }

    val objectifAtteint: Boolean
        get() = series.all { (it.repsRealisees ?: 0) >= objectifReps }
}