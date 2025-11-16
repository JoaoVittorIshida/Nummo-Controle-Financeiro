package com.example.appfinanceiro.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfinanceiro.components.CampoSenhaTextField
import com.example.appfinanceiro.ui.theme.AppFinanceiroTheme
import com.example.appfinanceiro.viewmodel.AuthViewModel

@Composable
fun TelaCriarConta(
    viewModel: AuthViewModel
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Cadeado",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text( "Crie sua conta", style = MaterialTheme.typography.headlineLarge )
            Text( "Proteja seus dados financeiros.", style = MaterialTheme.typography.bodyMedium )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = viewModel.nome, // Lendo do VM
                onValueChange = { viewModel.nome = it }, // Escrevendo no VM
                label = { Text("Seu Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            CampoSenhaTextField(
                label = "Senha (PIN de 4 dígitos)",
                valor = viewModel.senha, // Lendo do VM
                onValorChange = { viewModel.senha = it } // Escrevendo no VM
            )
            Spacer(modifier = Modifier.height(16.dp))

            CampoSenhaTextField(
                label = "Confirmar Senha (PIN)",
                valor = viewModel.confirmarSenha, // Lendo do VM
                onValorChange = { viewModel.confirmarSenha = it } // Escrevendo no VM
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable { viewModel.usarBiometria = !viewModel.usarBiometria }
            ) {
                Checkbox(
                    checked = viewModel.usarBiometria, // Lendo do VM
                    onCheckedChange = { viewModel.usarBiometria = it } // Escrevendo no VM
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Usar digital para desbloquear")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.onCriarContaClick() // Avisa o VM
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("CRIAR CONTA")
            }
        }
    }
}

@Preview(showBackground = true) @Composable fun PreviewTelaCriarConta() {
    AppFinanceiroTheme { TelaCriarConta(viewModel()) }
}