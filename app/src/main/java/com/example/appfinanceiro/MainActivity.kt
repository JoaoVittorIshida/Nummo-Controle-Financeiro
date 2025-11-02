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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfinanceiro.ui.theme.AppFinanceiroTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppFinanceiroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ControladorDeNavegacao(activity = this)
                }
            }
        }
    }
}

@Composable
fun ControladorDeNavegacao(
    activity: FragmentActivity? = null,
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val primeiroUsuario by authViewModel.primeiroUsuario.collectAsState()
    var appDesbloqueado by remember { mutableStateOf(false) }

    LaunchedEffect(primeiroUsuario) {
        if (primeiroUsuario == null) {
            appDesbloqueado = false
        }
    }

    if (primeiroUsuario == null) {
        TelaCriarConta(
            viewModel = authViewModel,
            onContaCriada = {
                // ...
            }
        )
    } else if (!appDesbloqueado) {
        // --- CORREÇÃO 2: Passar a activity adiante ---
        TelaDesbloqueio(
            activity = activity, // Passe a activity recebida
            viewModel = authViewModel,
            onDesbloqueado = {
                appDesbloqueado = true
            }
        )
    } else {
        AppPrincipal()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPrincipal(
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        floatingActionButton = {
            if (viewModel.abaSelecionada == 0 && !viewModel.exibeModalNovoLancamento) {
                FloatingActionButton(onClick = { viewModel.onFabClick() }) {
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
                        selected = viewModel.abaSelecionada == index,
                        onClick = { viewModel.onAbaSelecionada(index) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (viewModel.abaSelecionada) {
                0 -> TelaInicio(viewModel = viewModel)
                1 -> TelaExtrato(viewModel = viewModel)
                2 -> TelaConversor(viewModel = viewModel)
            }

            if (viewModel.exibeModalNovoLancamento) {
                TelaNovoLancamento(
                    viewModel = viewModel,
                    onFechar = { viewModel.onFecharModal() }
                )
            }
        }
    }
}