package com.bizetj.goldeneratracker.ui.screens.liste

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizetj.goldeneratracker.data.entity.Exercice
import com.bizetj.goldeneratracker.data.entity.HistoriqueSeance
import com.bizetj.goldeneratracker.data.entity.Seance
import com.bizetj.goldeneratracker.data.model.ExerciceHistorique
import com.bizetj.goldeneratracker.data.model.HistoriqueSeanceJson
import com.bizetj.goldeneratracker.data.repository.ExerciceRepository
import com.bizetj.goldeneratracker.data.repository.HistoriqueRepository
import com.bizetj.goldeneratracker.data.repository.SeanceRepository
import com.bizetj.goldeneratracker.util.JsonSerializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ListeSeancesViewModel @Inject constructor(
    private val repository: SeanceRepository,
    private val exerciceRepository: ExerciceRepository,
    private val historiqueRepository: HistoriqueRepository
) : ViewModel() {

    val seances: StateFlow<List<Seance>> = repository.getAllSeances()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteSeance(seance: Seance) {
        viewModelScope.launch {
            repository.deleteSeance(seance)
        }
    }

    fun ajouterSeanceTest() {
        viewModelScope.launch {
            val random = (1..100).random()
            repository.insertSeance(Seance(nom = "Séance Test $random"))
        }
    }

    fun genererDonneesTest() {
        viewModelScope.launch {
            // Créer 4 séances types
            val seancesData = listOf(
                "Push Day" to listOf(
                    "Développé Couché" to (60f to 2.5f),
                    "Développé Incliné" to (50f to 2.5f),
                    "Écartés" to (15f to 2.5f),
                    "Dips" to (20f to 2.5f),
                    "Extensions Triceps" to (12f to 1.25f)
                ),
                "Pull Day" to listOf(
                    "Tractions" to (0f to 2.5f),
                    "Rowing Barre" to (70f to 5f),
                    "Tirage Vertical" to (50f to 2.5f),
                    "Curl Barre" to (30f to 2.5f),
                    "Curl Marteau" to (15f to 1.25f)
                ),
                "Legs Day" to listOf(
                    "Squat" to (100f to 5f),
                    "Presse" to (150f to 10f),
                    "Leg Curl" to (40f to 2.5f),
                    "Leg Extension" to (50f to 2.5f),
                    "Mollets" to (80f to 5f)
                ),
                "Full Body" to listOf(
                    "Développé Militaire" to (40f to 2.5f),
                    "Squat" to (80f to 5f),
                    "Rowing" to (60f to 5f),
                    "Dips" to (15f to 2.5f),
                    "Tractions" to (0f to 2.5f)
                )
            )

            // Créer les séances et leurs exercices avec les IDs réels
            val seancesCreees = mutableListOf<Pair<Long, List<Exercice>>>()

            seancesData.forEach { (nomSeance, exercicesData) ->
                val seance = Seance(nom = nomSeance)
                val seanceId = repository.insertSeance(seance)

                val exercicesAvecIds = mutableListOf<Exercice>()

                exercicesData.forEach { (nomExercice, poidsPair) ->
                    val (poids, increment) = poidsPair
                    val exercice = Exercice(
                        seanceId = seanceId,
                        nom = nomExercice,
                        nombreSeries = 3,
                        borneMin = 8,
                        borneMax = 12,
                        poidsActuel = poids,
                        repsActuelles = 8,
                        incrementPoids = increment
                    )
                    exerciceRepository.insertExercice(exercice)

                    // Récupérer l'exercice avec son vrai ID
                    val exercicesSeance = exerciceRepository.getExercicesBySeance(seanceId).first()
                    val exerciceAvecId = exercicesSeance.find { it.nom == nomExercice }
                    if (exerciceAvecId != null) {
                        exercicesAvecIds.add(exerciceAvecId)
                    }
                }

                seancesCreees.add(seanceId to exercicesAvecIds)
            }

            // Générer 30 séances d'historique avec progression réaliste
            val maintenant = System.currentTimeMillis()
            val unJour = 24 * 60 * 60 * 1000L

            repeat(30) { index ->
                // Sélectionner une séance aléatoire
                val (seanceId, exercices) = seancesCreees.random()

                // 70% de chance de réussite
                val objectifAtteint = Random.nextFloat() < 0.70f

                // Générer des performances pour chaque exercice avec progression
                val exercicesHistorique = exercices.map { exercice ->
                    // Simuler une progression au fil du temps
                    val progressionFactor = index / 30f // 0.0 au début, 1.0 à la fin
                    val poidsProgression = exercice.poidsActuel + (progressionFactor * exercice.incrementPoids * 5)
                    val repsProgression = 8 + (progressionFactor * 4).toInt() // De 8 à 12 reps

                    val objectif = repsProgression.coerceIn(8, 12)
                    val series = List(3) {
                        if (objectifAtteint) {
                            // Réussite : reps >= objectif
                            Random.nextInt(objectif, objectif + 2)
                        } else {
                            // Échec : au moins une série < objectif
                            if (it == 2) { // Dernière série échoue
                                Random.nextInt(objectif - 2, objectif)
                            } else {
                                Random.nextInt(objectif, objectif + 2)
                            }
                        }
                    }

                    ExerciceHistorique(
                        exerciceId = exercice.id, // Utilise le vrai ID
                        nom = exercice.nom,
                        objectif = objectif,
                        poids = poidsProgression,
                        series = series,
                        reussi = series.all { it >= objectif }
                    )
                }

                val historiqueJson = HistoriqueSeanceJson(exercices = exercicesHistorique)
                val jsonString = JsonSerializer.toJson(historiqueJson)

                // Date dans le passé (30 jours en arrière)
                val date = maintenant - (unJour * (30 - index))

                val historique = HistoriqueSeance(
                    seanceId = seanceId,
                    date = date,
                    objectifAtteint = objectifAtteint,
                    donneesJson = jsonString
                )

                historiqueRepository.insertHistorique(historique)
            }
        }
    }
}