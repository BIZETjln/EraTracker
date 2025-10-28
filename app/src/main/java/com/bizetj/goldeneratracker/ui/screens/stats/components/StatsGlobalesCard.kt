package com.bizetj.goldeneratracker.ui.screens.stats.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bizetj.goldeneratracker.data.model.StatsGlobales
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StatsGlobalesCard(stats: StatsGlobales) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📈 Stats Globales",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider()

            // Nombre de séances
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "🏋️ Séances réalisées :",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${stats.nombreSeancesTotales}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Taux de réussite
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "✅ Taux de réussite :",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${String.format("%.1f", stats.tauxReussite)}%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (stats.tauxReussite >= 70) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }

            // Dernière séance
            stats.derniereSeance?.let { derniere ->
                HorizontalDivider()
                Column {
                    Text(
                        text = "💪 Dernière séance :",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    Text(
                        text = "${derniere.nomSeance} - ${dateFormat.format(Date(derniere.date))}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = if (derniere.objectifAtteint) "✅ Objectif atteint" else "⚠️ Objectif raté",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (derniere.objectifAtteint) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                }
            }
        }
    }
}