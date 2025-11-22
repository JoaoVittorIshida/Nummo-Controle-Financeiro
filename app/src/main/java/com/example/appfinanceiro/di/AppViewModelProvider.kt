package com.example.appfinanceiro.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.appfinanceiro.di.FinanceiroApplication
import com.example.appfinanceiro.viewmodel.AuthViewModel
import com.example.appfinanceiro.viewmodel.MainViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {

        initializer {
            AuthViewModel(
                usuarioRepository = financeiroApplication().container.usuarioRepository
            )
        }

        initializer {
            MainViewModel(
                lancamentoRepository = financeiroApplication().container.lancamentoRepository,
                cotacaoRepository = financeiroApplication().container.cotacaoRepository,
                usuarioRepository = financeiroApplication().container.usuarioRepository
            )
        }
    }
}

fun CreationExtras.financeiroApplication(): FinanceiroApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FinanceiroApplication)
