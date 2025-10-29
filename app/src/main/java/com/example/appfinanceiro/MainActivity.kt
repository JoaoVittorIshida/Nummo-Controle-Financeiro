package com.example.appfinanceiro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel // IMPORTANTE
import com.example.appfinanceiro.ui.theme.AppFinanceiroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppFinanceiroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ControladorDeNavegacao()
                }
            }
        }
    }
}

@Composable
fun ControladorDeNavegacao(
    // O ViewModel de Auth é criado aqui e controlado pelo Controlador
    authViewModel: AuthViewModel = viewModel()
) {
    // Simulando a lógica de login (o dev vai trocar isso por uma checagem real)
    val simulacaoUsuarioJaCadastrado = false

    var usuarioCadastrado by remember { mutableStateOf(simulacaoUsuarioJaCadastrado) }
    var appDesbloqueado by remember { mutableStateOf(false) }


    if (!usuarioCadastrado) {
        // Passa o ViewModel para a TelaCriarConta
        TelaCriarConta(
            viewModel = authViewModel,
            onContaCriada = {
                usuarioCadastrado = true
                appDesbloqueado = true
            }
        )
    } else if (usuarioCadastrado && !appDesbloqueado) {
        // Passa o mesmo ViewModel para a TelaDesbloqueio
        TelaDesbloqueio(
            viewModel = authViewModel,
            onDesbloqueado = {
                appDesbloqueado = true
            }
        )
    } else {
        // Quando o login passa, chama o AppPrincipal
        // O MainViewModel será criado DENTRO do AppPrincipal
        AppPrincipal()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPrincipal(
    // O AppPrincipal cria e gerencia o Cérebro Principal
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        floatingActionButton = {
            // Lendo o estado de navegação direto do ViewModel
            if (viewModel.abaSelecionada == 0 && !viewModel.exibeModalNovoLancamento) {
                FloatingActionButton(onClick = { viewModel.onFabClick() }) { // Avisa o VM
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Lançamento")
                }
            }
        },
        bottomBar = {
            NavigationBar {
                val itensDaBarra = listOf("Início", "Extrato", "Conversor")
                itensDaBarra.forEachIndexed { index, texto ->
                    NavigationBarItem(
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.Home, contentDescription = texto)
                                1 -> Icon(Icons.Default.List, contentDescription = texto)
                                2 -> Icon(Icons.Default.Settings, contentDescription = texto)
                            }
                        },
                        label = { Text(texto) },
                        selected = viewModel.abaSelecionada == index, // Lendo do VM
                        onClick = { viewModel.onAbaSelecionada(index) } // Avisando o VM
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // O "when" agora lê o estado do ViewModel
            when (viewModel.abaSelecionada) {
                0 -> TelaInicio(viewModel = viewModel)
                1 -> TelaExtrato(viewModel = viewModel)
                2 -> TelaConversor(viewModel = viewModel)
            }

            // O modal também lê o estado do ViewModel
            if (viewModel.exibeModalNovoLancamento) {
                TelaNovoLancamento(
                    viewModel = viewModel,
                    onFechar = { viewModel.onFecharModal() } // Avisa o VM
                )
            }
        }
    }
}