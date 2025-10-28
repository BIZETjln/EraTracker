package com.bizetj.goldeneratracker.ui.screens.seance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bizetj.goldeneratracker.ui.screens.seance.components.ExerciceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeanceEnCoursScreen(
    onNavigateBack: () -> Unit,
    viewModel: SeanceEnCoursViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(state.nomSeance)
                        if (state.exercices.isNotEmpty()) {
                            val exercicesTermines = state.exercices.count { it.estTermine }
                            Text(
                                text = "$exercicesTermines/${state.exercices.size} exercices terminés",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.errorMessage != null && state.exercices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = state.errorMessage ?: "Erreur",
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Liste des exercices
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = state.exercices,
                        key = { it.exerciceId }
                    ) { exercice ->
                        ExerciceCard(
                            exercice = exercice,
                            onSerieUpdate = { numeroSerie, reps ->
                                viewModel.updateSerie(exercice.exerciceId, numeroSerie, reps)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Message d'erreur
                state.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Boutons actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f),
                        enabled = !state.isLoading
                    ) {
                        Text("Annuler")
                    }

                    Button(
                        onClick = {
                            viewModel.terminerSeance(onSuccess = onNavigateBack)
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Terminer la Séance")
                        }
                    }
                }
            }
        }
    }
}