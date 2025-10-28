package com.bizetj.goldeneratracker.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizetj.goldeneratracker.data.model.*
import com.bizetj.goldeneratracker.data.repository.ExerciceRepository
import com.bizetj.goldeneratracker.data.repository.HistoriqueRepository
import com.bizetj.goldeneratracker.data.repository.SeanceRepository
import com.bizetj.goldeneratracker.util.JsonSerializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatsState(
    val statsGlobales: StatsGlobales = StatsGlobales(),
    val historique: List<HistoriqueSeanceAvecNom> = emptyList(),
    val exercicesDisponibles: List<Pair<Long, String>> = emptyList(), // (id, nom)
    val exerciceSelectionne: Long? = null,
    val progression: ExerciceProgression? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val historiqueRepository: HistoriqueRepository,
    private val seanceRepository: SeanceRepository,
    private val exerciceRepository: ExerciceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatsState())
    val state: StateFlow<StatsState> = _state.asStateFlow()

    init {
        chargerStats()
    }

    private fun chargerStats() {
        viewModelScope.launch {
            // Charger l'historique récent
            historiqueRepository.getRecentHistorique(50).collectLatest { historiques ->
                // Enrichir avec les noms de séances
                val historiquesAvecNoms = historiques.mapNotNull { historique ->
                    val seanceAvecExercices = seanceRepository.getSeanceAvecExercices(historique.seanceId)
                    seanceAvecExercices?.let {
                        HistoriqueSeanceAvecNom(
                            id = historique.id,
                            nomSeance = it.seance.nom,
                            date = historique.date,
                            objectifAtteint = historique.objectifAtteint
                        )
                    }
                }

                // Calculer stats globales
                val nombreTotal = historiques.size
                val nombreReussies = historiques.count { it.objectifAtteint }
                val tauxReussite = if (nombreTotal > 0) {
                    (nombreReussies.toFloat() / nombreTotal) * 100
                } else 0f

                val statsGlobales = StatsGlobales(
                    nombreSeancesTotales = nombreTotal,
                    nombreSeancesReussies = nombreReussies,
                    tauxReussite = tauxReussite,
                    derniereSeance = historiquesAvecNoms.firstOrNull()
                )

                // Récupérer tous les exercices disponibles
                val seances = seanceRepository.getAllSeances().first()
                val exercices = mutableListOf<Pair<Long, String>>()
                seances.forEach { seance ->
                    val seanceAvecExercices = seanceRepository.getSeanceAvecExercices(seance.id)
                    seanceAvecExercices?.exercices?.forEach { exercice ->
                        if (exercices.none { it.first == exercice.id }) {
                            exercices.add(exercice.id to exercice.nom)
                        }
                    }
                }

                _state.value = _state.value.copy(
                    statsGlobales = statsGlobales,
                    historique = historiquesAvecNoms,
                    exercicesDisponibles = exercices,
                    isLoading = false
                )
            }
        }
    }

    fun selectionnerExercice(exerciceId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Récupérer l'historique et extraire les données de cet exercice
            val historiques = historiqueRepository.getRecentHistorique(50).first()
            val points = mutableListOf<PointProgression>()

            historiques.forEach { historique ->
                val json = JsonSerializer.fromJson(historique.donneesJson)
                json?.exercices?.find { it.exerciceId == exerciceId }?.let { exerciceHisto ->
                    // Calculer la moyenne des reps réalisées
                    val repsRealisees = exerciceHisto.series.average().toInt()
                    points.add(
                        PointProgression(
                            date = historique.date,
                            reps = repsRealisees,
                            poids = exerciceHisto.poids,
                            reussi = exerciceHisto.reussi
                        )
                    )
                }
            }

            // Trier par date (plus ancien au plus récent)
            points.sortBy { it.date }

            val nomExercice = _state.value.exercicesDisponibles.find { it.first == exerciceId }?.second ?: ""

            val progression = ExerciceProgression(
                exerciceId = exerciceId,
                nomExercice = nomExercice,
                points = points
            )

            _state.value = _state.value.copy(
                exerciceSelectionne = exerciceId,
                progression = progression,
                isLoading = false
            )
        }
    }
}