package com.example.appfinanceiro.di

import android.content.Context
import com.example.appfinanceiro.data.AppDatabase
import com.example.appfinanceiro.data.repository.LancamentoRepository
import com.example.appfinanceiro.data.repository.UsuarioRepository

interface AppContainer {
    val usuarioRepository: UsuarioRepository
    val lancamentoRepository: LancamentoRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val db by lazy {
        AppDatabase.Companion.getDatabase(context)
    }

    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(db.usuarioDao())
    }

    override val lancamentoRepository: LancamentoRepository by lazy {
        LancamentoRepository(db.lancamentoDao())
    }
}