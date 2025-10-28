package com.bizetj.goldeneratracker.util

import com.bizetj.goldeneratracker.data.model.HistoriqueSeanceJson
import com.google.gson.Gson

object JsonSerializer {
    private val gson = Gson()

    fun toJson(historique: HistoriqueSeanceJson): String {
        return gson.toJson(historique)
    }

    fun fromJson(json: String): HistoriqueSeanceJson? {
        return try {
            gson.fromJson(json, HistoriqueSeanceJson::class.java)
        } catch (e: Exception) {
            null
        }
    }
}