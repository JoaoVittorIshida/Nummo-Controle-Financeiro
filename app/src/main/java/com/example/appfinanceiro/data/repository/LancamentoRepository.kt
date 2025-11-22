package com.example.appfinanceiro.data.repository


import com.example.appfinanceiro.data.dao.LancamentoDao
import com.example.appfinanceiro.data.entity.Lancamento
import kotlinx.coroutines.flow.Flow


class LancamentoRepository(private val lancamentoDao: LancamentoDao) {

    val todosLancamentos: Flow<List<Lancamento>> = lancamentoDao.getTodosLancamentos()

    val tresLancamentosRecentes: Flow<List<Lancamento>> = lancamentoDao.getTresLancamentosMaisRecentes()

    suspend fun inserirLancamento(lancamento: Lancamento) {
        lancamentoDao.inserirLancamento(lancamento)
    }

    suspend fun atualizarLancamento(lancamento: Lancamento) {
        lancamentoDao.atualizarLancamento(lancamento)
    }

    suspend fun deletarLancamento(lancamento: Lancamento) {
        lancamentoDao.deletarLancamento(lancamento)
    }

    suspend fun getLancamentoPorId(id: Int): Lancamento? {
        return lancamentoDao.getLancamentoPorId(id)
    }
}