package com.example.appfinanceiro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    // --- Estados para a TelaCriarConta ---
    var nome by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")
    var usarBiometria by mutableStateOf(true)

    // --- Estados para a TelaDesbloqueio ---
    var pinDigitado by mutableStateOf("")

    // --- Eventos (Lógica que o Dev vai implementar) ---
    fun onCriarContaClick() {
        // TODO: O dev de lógica vai:
        // 1. Validar se senha == confirmarSenha
        // 2. Salvar o 'nome', 'senha' e 'usarBiometria' no banco de dados (Room)
        // 3. Chamar a função 'onContaCriada()' que veio da tela
    }

    fun onDesbloquearClick() {
        // TODO: O dev de lógica vai:
        // 1. Buscar o PIN salvo no banco
        // 2. Validar se pinDigitado == pinSalvo
        // 3. Chamar a função 'onDesbloqueado()' que veio da tela
    }

    fun onBiometriaClick() {
        // TODO: O dev de lógica vai chamar o sensor de biometria aqui
    }
}