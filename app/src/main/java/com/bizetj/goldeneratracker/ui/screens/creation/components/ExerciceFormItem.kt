package com.bizetj.goldeneratracker.ui.screens.creation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bizetj.goldeneratracker.ui.screens.creation.ExerciceForm

@Composable
fun ExerciceFormItem(
    exercice: ExerciceForm,
    onDelete: () -> Unit,
    onUpdate: (ExerciceForm) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header avec nom et bouton supprimer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = exercice.nom,
                    onValueChange = { onUpdate(exercice.copy(nom = it)) },
                    label = { Text("Nom de l'exercice") },
                    placeholder = { Text("Ex: Développé Couché") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Supprimer",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre de séries
            OutlinedTextField(
                value = exercice.nombreSeries.toString(),
                onValueChange = { newValue ->
                    newValue.toIntOrNull()?.let { series ->
                        if (series > 0) {
                            onUpdate(exercice.copy(nombreSeries = series))
                        }
                    }
                },
                label = { Text("Nombre de séries") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Plage de répétitions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = exercice.borneMin.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { min ->
                            if (min > 0 && min <= exercice.borneMax) {
                                onUpdate(exercice.copy(borneMin = min))
                            }
                        }
                    },
                    label = { Text("Min reps") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = exercice.borneMax.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.let { max ->
                            if (max >= exercice.borneMin) {
                                onUpdate(exercice.copy(borneMax = max))
                            }
                        }
                    },
                    label = { Text("Max reps") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Poids actuel et incrément
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = if (exercice.poidsActuel == 0f) "" else exercice.poidsActuel.toString(),
                    onValueChange = { newValue ->
                        if (newValue.isEmpty()) {
                            onUpdate(exercice.copy(poidsActuel = 0f))
                        } else {
                            newValue.toFloatOrNull()?.let { poids ->
                                if (poids >= 0) {
                                    onUpdate(exercice.copy(poidsActuel = poids))
                                }
                            }
                        }
                    },
                    label = { Text("Poids (kg)") },
                    placeholder = { Text("Ex: 60.0") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = exercice.incrementPoids.toString(),
                    onValueChange = { newValue ->
                        newValue.toFloatOrNull()?.let { increment ->
                            if (increment > 0) {
                                onUpdate(exercice.copy(incrementPoids = increment))
                            }
                        }
                    },
                    label = { Text("+Poids (kg)") },
                    placeholder = { Text("Ex: 2.5") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            // Résumé visuel
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${exercice.nombreSeries} séries • ${exercice.borneMin}-${exercice.borneMax} reps" +
                        if (exercice.poidsActuel > 0) " • ${exercice.poidsActuel} kg (+${exercice.incrementPoids})" else "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}