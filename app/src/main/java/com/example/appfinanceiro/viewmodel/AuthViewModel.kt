package com.example.appfinanceiro.viewmodel

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfinanceiro.data.entity.Usuario
import com.example.appfinanceiro.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    var nome by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")
    var usarBiometria by mutableStateOf(true)
    var mensagemErro by mutableStateOf<String?>(null)

    var pinDigitado by mutableStateOf("")

    val primeiroUsuario: StateFlow<Usuario?> = usuarioRepository.primeiroUsuario.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = null
    )

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

    fun onBiometriaClick(activity: FragmentActivity, onSuccess: () -> Unit) {

        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                mensagemErro = null
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                    mensagemErro = "Erro na validação da biometria"
                }
            }
        }

        val biometricPrompt = BiometricPrompt(activity, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação")
            .setSubtitle("Use sua digital para desbloquear o app")
            .setNegativeButtonText("Cancelar")
            .build()

        val biometricManager = BiometricManager.from(activity)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {

            BiometricManager.BIOMETRIC_SUCCESS ->
                biometricPrompt.authenticate(promptInfo)

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                mensagemErro = "Nenhuma biometria cadastrada neste dispositivo."

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                mensagemErro = "Este dispositivo não possui sensor biométrico."

            else ->
                mensagemErro = "Sensor biométrico indisponível ou com erro."
        }
    }
}