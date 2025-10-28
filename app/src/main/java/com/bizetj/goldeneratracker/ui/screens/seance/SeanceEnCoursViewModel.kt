package com.bizetj.goldeneratracker.ui.screens.seance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizetj.goldeneratracker.data.entity.HistoriqueSeance
import com.bizetj.goldeneratracker.data.model.ExerciceEnCours
import com.bizetj.goldeneratracker.data.model.ExerciceHistorique
import com.bizetj.goldeneratracker.data.model.HistoriqueSeanceJson
import com.bizetj.goldeneratracker.data.model.SerieRealisee
import com.bizetj.goldeneratracker.data.repository.ExerciceRepository
import com.bizetj.goldeneratracker.data.repository.HistoriqueRepository
import com.bizetj.goldeneratracker.data.repository.SeanceRepository
import com.bizetj.goldeneratracker.util.JsonSerializer
import com.bizetj.goldeneratracker.util.ProgressionCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SeanceEnCoursState(
    val nomSeance: String = "",
    val exercices: List<ExerciceEnCours> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class SeanceEnCoursViewModel @Inject constructor(
    private val seanceRepository: SeanceRepository,
    private val exerciceRepository: ExerciceRepository,
    private val historiqueRepository: HistoriqueRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val seanceId: Long = savedStateHandle.get<String>("seanceId")?.toLongOrNull() ?: 0L

    private val _state = MutableStateFlow(SeanceEnCoursState())
    val state: StateFlow<SeanceEnCoursState> = _state.asStateFlow()

    init {
        chargerSeance()
    }

    private fun chargerSeance() {
        viewModelScope.launch {
            try {
                val seanceAvecExercices = seanceRepository.getSeanceAvecExercices(seanceId)

                if (seanceAvecExercices == null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Séance introuvable"
                    )
                    return@launch
                }

                // Transformer les exercices en ExerciceEnCours
                val exercicesEnCours = seanceAvecExercices.exercices.map { exercice ->
                    ExerciceEnCours(
                        exerciceId = exercice.id,
                        nom = exercice.nom,
                        objectifReps = exercice.repsActuelles,
                        poids = exercice.poidsActuel,
                        nombreSeries = exercice.nombreSeries,
                        series = (1..exercice.nombreSeries).map { numeroSerie ->
                            SerieRealisee(numeroSerie = numeroSerie, repsRealisees = null)
                        }
                    )
                }

                _state.value = SeanceEnCoursState(
                    nomSeance = seanceAvecExercices.seance.nom,
                    exercices = exercicesEnCours,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Erreur: ${e.message}"
                )
            }
        }
    }

    fun updateSerie(exerciceId: Long, numeroSerie: Int, reps: Int?) {
        val exercices = _state.value.exercices.map { exercice ->
            if (exercice.exerciceId == exerciceId) {
                val seriesMisesAJour = exercice.series.map { serie ->
                    if (serie.numeroSerie == numeroSerie) {
                        serie.copy(repsRealisees = reps)
                    } else {
                        serie
                    }
                }
                exercice.copy(series = seriesMisesAJour)
            } else {
                exercice
            }
        }
        _state.value = _state.value.copy(exercices = exercices)
    }

    fun terminerSeance(onSuccess: () -> Unit) {
        val currentState = _state.value

        // Vérifier que toutes les séries sont remplies
        val toutesSeriesRemplies = currentState.exercices.all { it.estTermine }

        if (!toutesSeriesRemplies) {
            _state.value = currentState.copy(
                errorMessage = "Veuillez remplir toutes les séries"
            )
            return
        }

        _state.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                // 1. Préparer les données pour l'historique
                val exercicesHistorique = currentState.exercices.map { exerciceEnCours ->
                    val seriesRealisees = exerciceEnCours.series.mapNotNull { it.repsRealisees }
                    ExerciceHistorique(
                        exerciceId = exerciceEnCours.exerciceId,
                        nom = exerciceEnCours.nom,
                        objectif = exerciceEnCours.objectifReps,
                        poids = exerciceEnCours.poids,
                        series = seriesRealisees,
                        reussi = seriesRealisees.all { it >= exerciceEnCours.objectifReps }
                    )
                }

                val historiqueJson = HistoriqueSeanceJson(exercices = exercicesHistorique)
                val jsonString = JsonSerializer.toJson(historiqueJson)

                // Déterminer si l'objectif global de la séance est atteint
                val objectifSeanceAtteint = exercicesHistorique.all { it.reussi }

                // 2. Sauvegarder l'historique
                val historique = HistoriqueSeance(
                    seanceId = seanceId,
                    date = System.currentTimeMillis(),
                    objectifAtteint = objectifSeanceAtteint,
                    donneesJson = jsonString
                )
                historiqueRepository.insertHistorique(historique)

                // 3. Mettre à jour chaque exercice avec la nouvelle progression
                val seanceAvecExercices = seanceRepository.getSeanceAvecExercices(seanceId)
                seanceAvecExercices?.exercices?.forEach { exercice ->
                    // Trouver les performances correspondantes
                    val exerciceEnCours = currentState.exercices.find { it.exerciceId == exercice.id }
                    if (exerciceEnCours != null) {
                        val seriesRealisees = exerciceEnCours.series.mapNotNull { it.repsRealisees }

                        // Calculer le prochain objectif
                        val prochainObjectif = ProgressionCalculator.calculerProchainObjectif(
                            exercice = exercice,
                            seriesRealisees = seriesRealisees
                        )

                        // Mettre à jour l'exercice
                        val exerciceMisAJour = exercice.copy(
                            repsActuelles = prochainObjectif.reps,
                            poidsActuel = prochainObjectif.poids
                        )
                        exerciceRepository.updateExercice(exerciceMisAJour)
                    }
                }

                // Retour à la liste
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