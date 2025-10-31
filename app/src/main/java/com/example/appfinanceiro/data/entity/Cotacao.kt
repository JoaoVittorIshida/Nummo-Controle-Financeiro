package com.example.appfinanceiro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cotacao")
data class Cotacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dolar: Double,
    val euro: Double,
    val horario: Long
)