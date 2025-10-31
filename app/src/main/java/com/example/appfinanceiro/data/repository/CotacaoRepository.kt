package com.example.appfinanceiro.data.repository

import com.example.appfinanceiro.data.dao.CotacaoDao
import com.example.appfinanceiro.data.entity.Cotacao
import kotlinx.coroutines.flow.Flow


class CotacaoRepository(private val cotacaoDao: CotacaoDao) {

    val cotacaoMaisRecente: Flow<Cotacao?> = cotacaoDao.getCotacaoMaisRecente()

    suspend fun inserirCotacao(cotacao: Cotacao) {
        cotacaoDao.inserirCotacao(cotacao)
    }

    suspend fun limparTodasCotacoes() {
        cotacaoDao.limparTodasCotacoes()
    }

}
