package com.bizetj.goldeneratracker.ui.screens.liste

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizetj.goldeneratracker.data.entity.Seance
import com.bizetj.goldeneratracker.data.repository.SeanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListeSeancesViewModel @Inject constructor(
    private val repository: SeanceRepository
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
}