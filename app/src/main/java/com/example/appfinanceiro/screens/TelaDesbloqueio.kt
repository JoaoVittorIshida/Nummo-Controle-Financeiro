package com.example.appfinanceiro.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfinanceiro.components.CampoSenhaTextField
import com.example.appfinanceiro.di.AppViewModelProvider
import com.example.appfinanceiro.ui.theme.AppFinanceiroTheme
import com.example.appfinanceiro.viewmodel.AuthViewModel

@Composable
fun TelaDesbloqueio(
    activity: FragmentActivity?,
    viewModel: AuthViewModel,
    onDesbloqueado: () -> Unit
) {
    val primeiroUsuario by viewModel.primeiroUsuario.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text( "Bem-vindo de volta!", style = MaterialTheme.typography.headlineLarge )
            Text( "App bloqueado por segurança.", style = MaterialTheme.typography.bodyMedium )
            Spacer(modifier = Modifier.height(32.dp))

            if (primeiroUsuario?.digital == true) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Desbloquear com digital",
                    modifier = Modifier
                        .size(128.dp)
                        .clickable {
                            if (activity != null) {
                                viewModel.onBiometriaClick(
                                    activity = activity,
                                    onSuccess = onDesbloqueado
                                )
                            } else {
                                viewModel.mensagemErro = "Não foi possível iniciar a biometria"
                            }
                        },
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Toque no cadeado para usar a digital",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("... ou digite seu PIN ...")
            Spacer(modifier = Modifier.height(16.dp))

            CampoSenhaTextField(
                label = "Seu PIN",
                valor = viewModel.pinDigitado,
                onValorChange = {
                    viewModel.pinDigitado = it
                    if (viewModel.mensagemErro != null) {
                        viewModel.mensagemErro = null
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            viewModel.mensagemErro?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    viewModel.onDesbloquearClick(onSuccess = onDesbloqueado)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("ENTRAR")
            }
        }
    }
}

@Preview(showBackground = true) @Composable fun PreviewTelaDesbloqueio() {
    AppFinanceiroTheme {
        val viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)

        TelaDesbloqueio(
            activity = null,
            viewModel = viewModel,
            onDesbloqueado = {}
        )
    }
}