package com.bizetj.goldeneratracker.ui.screens.stats.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.bizetj.goldeneratracker.data.model.ExerciceProgression
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciceProgressionCard(
    exercicesDisponibles: List<Pair<Long, String>>,
    exerciceSelectionne: Long?,
    progression: ExerciceProgression?,
    onExerciceSelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val exerciceActuel = exercicesDisponibles.find { it.first == exerciceSelectionne }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sélecteur d'exercice
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = exerciceActuel?.second ?: "Sélectionner un exercice",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Exercice") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    exercicesDisponibles.forEach { (id, nom) ->
                        DropdownMenuItem(
                            text = { Text(nom) },
                            onClick = {
                                onExerciceSelected(id)
                                expanded = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }

            // Graphique de progression
            if (progression != null && progression.points.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Progression - ${progression.nomExercice}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Graphique simple
                    SimpleLineChart(
                        progression = progression,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    // Légende
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "🟢 Réussi",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "🔴 Échoué",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Stats de l'exercice
                    Divider()
                    val premier = progression.points.first()
                    val dernier = progression.points.last()
                    Text(
                        text = "Début: ${premier.reps} reps @ ${premier.poids} kg",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Actuel: ${dernier.reps} reps @ ${dernier.poids} kg",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else if (exerciceSelectionne != null) {
                Text(
                    text = "Aucune donnée disponible pour cet exercice",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SimpleLineChart(
    progression: ExerciceProgression,
    modifier: Modifier = Modifier
) {
    val points = progression.points

    Canvas(modifier = modifier) {
        if (points.size < 2) return@Canvas

        val width = size.width
        val height = size.height
        val padding = 40f

        // Trouver min/max pour scaling
        val maxPoids = points.maxOf { it.poids }
        val minPoids = points.minOf { it.poids }
        val rangePoids = (maxPoids - minPoids).coerceAtLeast(1f)

        // Dessiner les points et lignes
        points.forEachIndexed { index, point ->
            val x = padding + (index.toFloat() / (points.size - 1)) * (width - 2 * padding)
            val y = height - padding - ((point.poids - minPoids) / rangePoids) * (height - 2 * padding)

            // Point
            val pointColor = if (point.reussi) Color.Green else Color.Red
            drawCircle(
                color = pointColor,
                radius = 8f,
                center = Offset(x, y)
            )

            // Ligne vers le point suivant
            if (index < points.size - 1) {
                val nextPoint = points[index + 1]
                val nextX = padding + ((index + 1).toFloat() / (points.size - 1)) * (width - 2 * padding)
                val nextY = height - padding - ((nextPoint.poids - minPoids) / rangePoids) * (height - 2 * padding)

                drawLine(
                    color = Color.Gray,
                    start = Offset(x, y),
                    end = Offset(nextX, nextY),
                    strokeWidth = 2f
                )
            }
        }

        // Axes
        drawLine(
            color = Color.Gray,
            start = Offset(padding, padding),
            end = Offset(padding, height - padding),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Gray,
            start = Offset(padding, height - padding),
            end = Offset(width - padding, height - padding),
            strokeWidth = 2f
        )
    }
}