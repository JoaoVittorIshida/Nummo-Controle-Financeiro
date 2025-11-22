package com.example.appfinanceiro.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfinanceiro.components.SeletorDeMoeda
import com.example.appfinanceiro.ui.theme.AppFinanceiroTheme
import com.example.appfinanceiro.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaConversor(
    viewModel: MainViewModel
) {
    val valorEntrada by viewModel.convValorEntrada.collectAsState()
    val moedaDe by viewModel.convMoedaDe.collectAsState()
    val moedaPara by viewModel.convMoedaPara.collectAsState()
    val resultado by viewModel.convResultado.collectAsState()
    val cotacaoAtual by viewModel.cotacaoAtual.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Conversor de Moedas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        OutlinedTextField(
            value = valorEntrada,
            onValueChange = { novoValor ->
                viewModel.onConvValorChange(novoValor)
            },
            label = { Text("Valor para converter") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("De:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        SeletorDeMoeda(
            moedaSelecionada = moedaDe,
            onMoedaChange = { novaMoeda ->
                viewModel.onConvMoedaDeChange(novaMoeda)
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("Para:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        SeletorDeMoeda(
            moedaSelecionada = moedaPara,
            onMoedaChange = { novaMoeda ->
                viewModel.onConvMoedaParaChange(novaMoeda)
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Resultado da Conversão",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "$resultado $moedaPara",
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
        if(cotacaoAtual == null){
            Text(
                text = "Não há cotações disponíveis. Conecte-se a internet e reinicie o aplicativo!",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        else{
            Text(
                text = "Última cotação obtida: " + viewModel.formatarData(cotacaoAtual!!.horario),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true) @Composable fun PreviewTelaConversor() {
    AppFinanceiroTheme { TelaConversor(viewModel()) }
}