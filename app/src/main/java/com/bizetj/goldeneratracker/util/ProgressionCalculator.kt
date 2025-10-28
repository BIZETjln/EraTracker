package com.bizetj.goldeneratracker.util

import com.bizetj.goldeneratracker.data.entity.Exercice
import com.bizetj.goldeneratracker.data.model.ObjectifExercice

object ProgressionCalculator {

    fun calculerProchainObjectif(
        exercice: Exercice,
        seriesRealisees: List<Int>
    ): ObjectifExercice {
        val objectifAtteint = seriesRealisees.all { it >= exercice.repsActuelles }

        return when {
            !objectifAtteint -> {
                ObjectifExercice(
                    reps = exercice.repsActuelles,
                    poids = exercice.poidsActuel
                )
            }

            exercice.repsActuelles >= exercice.borneMax -> {
                ObjectifExercice(
                    reps = exercice.borneMin,
                    poids = exercice.poidsActuel + exercice.incrementPoids // Utilise le champ
                )
            }

            else -> {
                ObjectifExercice(
                    reps = exercice.repsActuelles + 1,
                    poids = exercice.poidsActuel
                )
            }
        }
    }

    fun objectifSeanceAtteint(
        exercicesRealisations: List<Pair<Int, List<Int>>>
    ): Boolean {
        return exercicesRealisations.all { (objectif, series) ->
            series.all { it >= objectif }
        }
    }

    fun calculerResumeSeries(objectif: Int, seriesRealisees: List<Int>): String {
        val seriesReussies = seriesRealisees.count { it >= objectif }
        val totalSeries = seriesRealisees.size
        return "$seriesReussies/$totalSeries séries réussies"
    }
}