package com.example.appfinanceiro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "lancamentos")
data class Lancamento(
    @PrimaryKey(autoGenerate = true)
    val id_lancamento: Int = 0,
    val tipo: String,
    val descricao: String,
    val valor: Double,
    val observacoes: String?,
    val data: Long
)
