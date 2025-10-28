package com.bizetj.goldeneratracker.ui.screens.seance.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bizetj.goldeneratracker.data.model.ExerciceEnCours

@Composable
fun ExerciceCard(
    exercice: ExerciceEnCours,
    onSerieUpdate: (numeroSerie: Int, reps: Int?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (exercice.estTermine) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header avec nom et objectif
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "💪 ${exercice.nom}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Objectif: ${exercice.nombreSeries}×${exercice.objectifReps} @ ${exercice.poids} kg",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Badge de statut
                if (exercice.estTermine) {
                    val badge = if (exercice.objectifAtteint) "✅" else "⚠️"
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Liste des séries
            exercice.series.forEach { serie ->
                SerieInput(
                    serie = serie,
                    objectif = exercice.objectifReps,
                    onRepsChange = { reps ->
                        onSerieUpdate(serie.numeroSerie, reps)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}