package com.example.appfinanceiro.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.appfinanceiro.ui.theme.AppFinanceiroTheme
import com.example.appfinanceiro.viewmodel.MainViewModel

@Composable
fun TelaInicio(
    viewModel: MainViewModel
) {
    val lista3Lancamentos by viewModel.listaDe3Lancamentos.collectAsState()
    val saldoMes by viewModel.saldoTotal.collectAsState()
    val saldoPrevisto by viewModel.saldoPrevistoDoMes.collectAsState()
    val usuarioLogado = viewModel.usuarioLogado

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Text(
                text = "Bem vindo $usuarioLogado!",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 2.dp ,bottom = 8.dp)
            )
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Saldo Total Atual", style = MaterialTheme.typography.titleMedium)
                    Text("R$ %.2f".format(saldoMes), style = MaterialTheme.typography.displaySmall)
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Saldo Previsto no Final do Mês", style = MaterialTheme.typography.titleMedium)
                    Text("R$ %.2f".format(saldoPrevisto), style = MaterialTheme.typography.displaySmall)
                }
            }
        }
        item {
            Text(
                text = "Lançamentos Recentes",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(lista3Lancamentos) { lancamentoUi ->
            CardLancamento(
                lancamento = lancamentoUi,
                onClick = { idDoLancamento ->
                    viewModel.onAbrirModalDeEdicao(idDoLancamento)
                }
            )
        }
    }
}

@Preview(showBackground = true) @Composable fun PreviewTelaInicioSimples() {
    AppFinanceiroTheme { TelaInicio(viewModel()) }
}