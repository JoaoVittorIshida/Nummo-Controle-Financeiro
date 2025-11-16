package com.example.appfinanceiro.di

import android.content.Context
import com.example.appfinanceiro.data.AppDatabase
import com.example.appfinanceiro.data.network.ApiService
import com.example.appfinanceiro.data.repository.CotacaoRepository
import com.example.appfinanceiro.data.repository.LancamentoRepository
import com.example.appfinanceiro.data.repository.UsuarioRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val usuarioRepository: UsuarioRepository
    val lancamentoRepository: LancamentoRepository
    val cotacaoRepository: CotacaoRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val db by lazy {
        AppDatabase.getDatabase(context)
    }

    private val BASE_URL = "https://economia.awesomeapi.com.br/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(db.usuarioDao())
    }

    override val lancamentoRepository: LancamentoRepository by lazy {
        LancamentoRepository(db.lancamentoDao())
    }

    override val cotacaoRepository: CotacaoRepository by lazy {
        CotacaoRepository(db.cotacaoDao(), apiService)
    }
}