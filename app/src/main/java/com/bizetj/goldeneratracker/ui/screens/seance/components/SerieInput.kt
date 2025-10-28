package com.bizetj.goldeneratracker.ui.screens.seance.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bizetj.goldeneratracker.data.model.SerieRealisee

@Composable
fun SerieInput(
    serie: SerieRealisee,
    objectif: Int,
    onRepsChange: (Int?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label série
        Text(
            text = "Série ${serie.numeroSerie}:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(80.dp)
        )

        // Input reps
        OutlinedTextField(
            value = serie.repsRealisees?.toString() ?: "",
            onValueChange = { newValue ->
                if (newValue.isEmpty()) {
                    onRepsChange(null)
                } else {
                    newValue.toIntOrNull()?.let { reps ->
                        if (reps >= 0) {
                            onRepsChange(reps)
                        }
                    }
                }
            },
            placeholder = { Text("__") },
            suffix = { Text("reps") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Indicateur de réussite
        val icon = when {
            serie.repsRealisees == null -> "⏳"
            serie.repsRealisees >= objectif -> "✅"
            else -> "⚠️"
        }
        Text(
            text = icon,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.width(40.dp)
        )
    }
}