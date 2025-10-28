package com.bizetj.goldeneratracker.ui.screens.stats.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bizetj.goldeneratracker.data.model.HistoriqueSeanceAvecNom
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoriqueList(historique: List<HistoriqueSeanceAvecNom>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        historique.forEach { item ->
            HistoriqueItem(item)
        }
    }
}

@Composable
private fun HistoriqueItem(item: HistoriqueSeanceAvecNom) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                Text(
                    text = dateFormat.format(Date(item.date)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = item.nomSeance,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = if (item.objectifAtteint) "✅" else "⚠️",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}