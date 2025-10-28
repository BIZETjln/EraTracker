package com.bizetj.goldeneratracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seances")
data class Seance(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nom: String,
    val dateCreation: Long = System.currentTimeMillis()
)