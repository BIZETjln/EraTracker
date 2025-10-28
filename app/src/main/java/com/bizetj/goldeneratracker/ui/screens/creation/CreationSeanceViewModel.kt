package com.bizetj.goldeneratracker.ui.screens.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizetj.goldeneratracker.data.entity.Exercice
import com.bizetj.goldeneratracker.data.entity.Seance
import com.bizetj.goldeneratracker.data.repository.ExerciceRepository
import com.bizetj.goldeneratracker.data.repository.SeanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciceForm(
    val id: String = java.util.UUID.randomUUID().toString(),
    val nom: String = "",
    val nombreSeries: Int = 3,
    val borneMin: Int = 8,
    val borneMax: Int = 12,
    val poidsActuel: Float = 0f,
    val incrementPoids: Float = 2.5f // Nouveau champ
)

data class CreationSeanceState(
    val nomSeance: String = "",
    val exercices: List<ExerciceForm> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class CreationSeanceViewModel @Inject constructor(
    private val seanceRepository: SeanceRepository,
    private val exerciceRepository: ExerciceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreationSeanceState())
    val state: StateFlow<CreationSeanceState> = _state.asStateFlow()

    fun updateNomSeance(nom: String) {
        _state.value = _state.value.copy(nomSeance = nom)
    }

    fun ajouterExercice() {
        val nouveauxExercices = _state.value.exercices + ExerciceForm()
        _state.value = _state.value.copy(exercices = nouveauxExercices)
    }

    fun supprimerExercice(exerciceId: String) {
        val nouveauxExercices = _state.value.exercices.filter { it.id != exerciceId }
        _state.value = _state.value.copy(exercices = nouveauxExercices)
    }

    fun updateExercice(exerciceId: String, update: (ExerciceForm) -> ExerciceForm) {
        val nouveauxExercices = _state.value.exercices.map { exercice ->
            if (exercice.id == exerciceId) update(exercice) else exercice
        }
        _state.value = _state.value.copy(exercices = nouveauxExercices)
    }

    fun sauvegarderSeance(onSuccess: () -> Unit) {
        val currentState = _state.value

        // Validation
        if (currentState.nomSeance.isBlank()) {
            _state.value = currentState.copy(errorMessage = "Le nom de la séance est requis")
            return
        }
        if (currentState.exercices.isEmpty()) {
            _state.value = currentState.copy(errorMessage = "Ajoutez au moins un exercice")
            return
        }
        if (currentState.exercices.any { it.nom.isBlank() }) {
            _state.value = currentState.copy(errorMessage = "Tous les exercices doivent avoir un nom")
            return
        }

        _state.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                // Créer la séance
                val seance = Seance(nom = currentState.nomSeance)
                val seanceId = seanceRepository.insertSeance(seance)

                // Créer les exercices
                currentState.exercices.forEach { exerciceForm ->
                    val exercice = Exercice(
                        seanceId = seanceId,
                        nom = exerciceForm.nom,
                        nombreSeries = exerciceForm.nombreSeries,
                        borneMin = exerciceForm.borneMin,
                        borneMax = exerciceForm.borneMax,
                        poidsActuel = exerciceForm.poidsActuel,
                        repsActuelles = exerciceForm.borneMin,
                        incrementPoids = exerciceForm.incrementPoids // Nouveau
                    )
                    exerciceRepository.insertExercice(exercice)
                }

                _state.value = CreationSeanceState() // Reset
                onSuccess()
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Erreur lors de la sauvegarde: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}