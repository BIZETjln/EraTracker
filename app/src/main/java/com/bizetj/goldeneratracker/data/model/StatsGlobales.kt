package com.bizetj.goldeneratracker.data.model

data class StatsGlobales(
    val nombreSeancesTotales: Int = 0,
    val nombreSeancesReussies: Int = 0,
    val tauxReussite: Float = 0f,
    val derniereSeance: HistoriqueSeanceAvecNom? = null
)

data class HistoriqueSeanceAvecNom(
    val id: Long,
    val nomSeance: String,
    val date: Long,
    val objectifAtteint: Boolean
)