package com.example.appfinanceiro

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.appfinanceiro.data.FinanceiroApplication
import com.example.appfinanceiro.viewmodel.AuthViewModel
import com.example.appfinanceiro.viewmodel.MainViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {

        // AuthViewModel
        initializer {
            AuthViewModel(
                usuarioRepository = financeiroApplication().container.usuarioRepository
            )
        }

        // MainViewModel
        initializer {
            MainViewModel(
                lancamentoRepository = financeiroApplication().container.lancamentoRepository
            )
        }
    }
}

fun CreationExtras.financeiroApplication(): FinanceiroApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FinanceiroApplication)
