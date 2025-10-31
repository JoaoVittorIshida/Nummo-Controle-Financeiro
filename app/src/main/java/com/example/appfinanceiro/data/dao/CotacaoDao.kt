package com.example.appfinanceiro.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appfinanceiro.data.entity.Cotacao
import kotlinx.coroutines.flow.Flow


@Dao
interface CotacaoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirCotacao(cotacao: Cotacao)

    @Query("SELECT * FROM cotacao ORDER BY horario DESC LIMIT 1")
    fun getCotacaoMaisRecente(): Flow<Cotacao?>

    @Query("DELETE FROM cotacao")
    suspend fun limparTodasCotacoes()
}