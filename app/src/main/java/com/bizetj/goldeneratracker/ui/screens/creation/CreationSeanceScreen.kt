package com.bizetj.goldeneratracker.ui.screens.creation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bizetj.goldeneratracker.ui.screens.creation.components.ExerciceFormItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationSeanceScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreationSeanceViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Afficher les erreurs
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            // On va gérer ça avec un Snackbar plus tard
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Créer une Séance") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Champ nom de séance
            OutlinedTextField(
                value = state.nomSeance,
                onValueChange = { viewModel.updateNomSeance(it) },
                label = { Text("Nom de la séance") },
                placeholder = { Text("Ex: Push Day") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Titre section exercices
            Text(
                text = "Exercices",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Liste des exercices
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = state.exercices,
                    key = { it.id }
                ) { exercice ->
                    ExerciceFormItem(
                        exercice = exercice,
                        onDelete = { viewModel.supprimerExercice(exercice.id) },
                        onUpdate = { updatedExercice ->
                            viewModel.updateExercice(exercice.id) { updatedExercice }
                        }
                    )
                }

                // Bouton ajouter exercice
                item {
                    OutlinedButton(
                        onClick = { viewModel.ajouterExercice() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ajouter",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ajouter un exercice")
                    }
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
                        viewModel.sauvegarderSeance(onSuccess = onNavigateBack)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Sauvegarder")
                    }
                }
            }
        }
    }
}