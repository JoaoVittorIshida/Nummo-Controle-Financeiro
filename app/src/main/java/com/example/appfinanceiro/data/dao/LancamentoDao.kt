package com.example.appfinanceiro.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.appfinanceiro.data.entity.Lancamento
import kotlinx.coroutines.flow.Flow


@Dao
interface LancamentoDao {
    @Insert
    suspend fun inserirLancamento(lancamento: Lancamento)

    @Update
    suspend fun atualizarLancamento(lancamento: Lancamento)

    @Delete
    suspend fun deletarLancamento(lancamento: Lancamento)

    @Query("SELECT * FROM lancamentos WHERE id_lancamento = :id")
    suspend fun getLancamentoPorId(id: Int): Lancamento?

    @Query("SELECT * FROM lancamentos ORDER BY data DESC")
    fun getTodosLancamentos(): Flow<List<Lancamento>>
}