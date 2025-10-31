package com.example.appfinanceiro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfinanceiro.data.entity.Usuario
import com.example.appfinanceiro.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    // --- Estados para a TelaCriarConta ---
    var nome by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")
    var usarBiometria by mutableStateOf(true)
    var mensagemErro by mutableStateOf<String?>(null)
        // O setter é público para que a UI possa limpá-lo

    // --- Estados para a TelaDesbloqueio ---
    var pinDigitado by mutableStateOf("")

    val primeiroUsuario: StateFlow<Usuario?> = usuarioRepository.primeiroUsuario.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    // --- Eventos (Lógica que o Dev vai implementar) ---
    fun onCriarContaClick() {
        if (nome.isBlank() || senha.isBlank() || confirmarSenha.isBlank()) {
            mensagemErro = "Todos os campos são obrigatórios."
            return
        }
        if (senha != confirmarSenha) {
            mensagemErro = "As senhas não coincidem."
            return
        }
        if (senha.length != 4) {
            mensagemErro = "A senha deve ser um PIN de 4 dígitos."
            return
        }

        // Se todas as validações passaram, limpa a mensagem de erro e insere o usuário
        mensagemErro = null
        viewModelScope.launch {
            val novoUsuario = Usuario(
                nome = nome,
                senha = senha,
                digital = usarBiometria
            )
            usuarioRepository.inserirUsuario(novoUsuario)
        }
    }

    fun onDesbloquearClick(onSuccess: () -> Unit) {
        if (pinDigitado == primeiroUsuario.value?.senha) {
            mensagemErro = null
            onSuccess()
        } else {
            mensagemErro = "PIN incorreto. Tente novamente."
        }
    }

    fun onBiometriaClick() {
        // TODO: O dev de lógica vai chamar o sensor de biometria aqui
    }
}