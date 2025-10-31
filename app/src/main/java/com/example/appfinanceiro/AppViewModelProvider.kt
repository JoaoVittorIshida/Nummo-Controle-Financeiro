package com.example.appfinanceiro

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.appfinanceiro.data.FinanceiroApplication
import com.example.appfinanceiro.data.repository.UsuarioRepository

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AuthViewModel(
                usuarioRepository = financeiroApplication().container.usuarioRepository
            )
        }
    }
}

fun CreationExtras.financeiroApplication(): FinanceiroApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FinanceiroApplication)
