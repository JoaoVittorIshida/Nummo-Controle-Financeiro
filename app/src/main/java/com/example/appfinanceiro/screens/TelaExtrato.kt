package com.example.appfinanceiro.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfinanceiro.components.CardLancamento
import com.example.appfinanceiro.components.DropdownFiltro
import com.example.appfinanceiro.components.SeletorFiltroTipo
import com.example.appfinanceiro.ui.theme.AppFinanceiroTheme
import com.example.appfinanceiro.viewmodel.MainViewModel

@Composable
fun TelaExtrato(
    viewModel: MainViewModel
) {
    val mesSelecionado by viewModel.filtroMes.collectAsState()
    val anoSelecionado by viewModel.filtroAno.collectAsState()
    val tipoSelecionado by viewModel.filtroTipo.collectAsState()
    val listaFiltrada by viewModel.extratoFiltrado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Extrato Completo", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Filtrar por:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropdownFiltro(
                label = "Mês",
                opcoes = viewModel.nomeDosMeses.toList(),
                selecionado = mesSelecionado, // <-- MUDANÇA 3
                onSelect = { novoMes ->
                    viewModel.onFiltroMesChange(novoMes)
                },
                modifier = Modifier.weight(1f)
            )
            DropdownFiltro(
                label = "Ano",
                opcoes = listOf("2023", "2024", "2025"),
                selecionado = anoSelecionado,
                onSelect = { novoAno ->
                    viewModel.onFiltroAnoChange(novoAno)
                },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        SeletorFiltroTipo(
            selecionado = tipoSelecionado,
            onTipoChange = { novoTipo ->
                viewModel.onFiltroTipoChange(novoTipo)
            }
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaFiltrada) { lancamentoUi ->
                CardLancamento(
                    lancamento = lancamentoUi,
                    onClick = { idDoLancamento ->
                        viewModel.onAbrirModalDeEdicao(idDoLancamento)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true) @Composable fun PreviewTelaExtrato() {
    AppFinanceiroTheme { TelaExtrato(viewModel()) }
}