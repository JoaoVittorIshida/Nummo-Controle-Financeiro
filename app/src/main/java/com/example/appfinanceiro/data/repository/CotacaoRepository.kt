package com.example.appfinanceiro.data.repository

import android.util.Log
import com.example.appfinanceiro.data.dao.CotacaoDao
import com.example.appfinanceiro.data.entity.Cotacao
import com.example.appfinanceiro.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class CotacaoRepository(
    private val cotacaoDao: CotacaoDao,
    private val apiService: ApiService
) {

    val cotacaoMaisRecente: Flow<Cotacao?> = cotacaoDao.getCotacaoMaisRecente()

    suspend fun atualizarCotacoes() {
        withContext(Dispatchers.IO) {
            try {
                val respostaApi = apiService.getCotacoes("USD-BRL,EUR-BRL")
                val cotacaoUSD = respostaApi["USDBRL"]
                val cotacaoEUR = respostaApi["EURBRL"]
                if (cotacaoUSD != null && cotacaoEUR != null) {

                    val novaCotacao = Cotacao(
                        dolar = cotacaoUSD.bid.toDoubleOrNull() ?: 0.0,
                        euro = cotacaoEUR.bid.toDoubleOrNull() ?: 0.0,
                        horario = cotacaoUSD.timestamp.toLongOrNull()?.times(1000)
                            ?: System.currentTimeMillis()
                    )
                    cotacaoDao.limparTodasCotacoes()
                    cotacaoDao.inserirCotacao(novaCotacao)
                    Log.d("CotacaoRepository", "Cotações atualizadas com sucesso no banco!")
                }
            } catch (e: Exception) {
                Log.e("CotacaoRepository", "Falha ao buscar cotações da API: ${e.message}")
            }
        }
    }

}
