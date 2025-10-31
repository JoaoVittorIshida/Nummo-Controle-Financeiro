package com.example.appfinanceiro.data

import android.content.Context
import com.example.appfinanceiro.data.repository.UsuarioRepository

interface AppContainer {
    val usuarioRepository: UsuarioRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val db by lazy {
        AppDatabase.getDatabase(context)
    }

    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(db.usuarioDao())
    }
}