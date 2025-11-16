package com.example.appfinanceiro.data.network

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("last/{moedas}")
    suspend fun getCotacoes(
        @Path("moedas") moedas: String = "USD-BRL,EUR-BRL"
    ): Map<String, CotacaoInfo>
}