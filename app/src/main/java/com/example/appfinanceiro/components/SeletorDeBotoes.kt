package com.example.appfinanceiro.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SeletorFiltroTipo(
    selecionado: String,
    onTipoChange: (String) -> Unit
) {
    val tipos = listOf("Todos", "Receita", "Despesa")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tipos.forEach { tipo ->
            OutlinedButton(
                onClick = { onTipoChange(tipo) },
                modifier = Modifier.weight(1f),
                colors = if (selecionado == tipo) {
                    ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                } else {
                    ButtonDefaults.outlinedButtonColors()
                }
            ) { Text(tipo) }
        }
    }
}

@Composable
fun SeletorDeMoeda(
    moedaSelecionada: String,
    onMoedaChange: (String) -> Unit
) {
    val moedas = listOf("BRL", "USD", "EUR")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        moedas.forEach { moeda ->
            OutlinedButton(
                onClick = { onMoedaChange(moeda) },
                modifier = Modifier.weight(1f),
                colors = if (moedaSelecionada == moeda) {
                    ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                } else {
                    ButtonDefaults.outlinedButtonColors()
                }
            ) { Text(moeda) }
        }
    }
}

@Composable
fun SeletorDeTipo(
    tipoSelecionado: String,
    onTipoChange: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { onTipoChange("Despesa") },
            modifier = Modifier.weight(1f),
            colors = if (tipoSelecionado == "Despesa") {
                ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            } else {
                ButtonDefaults.outlinedButtonColors()
            }
        ) { Text("Despesa") }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedButton(
            onClick = { onTipoChange("Receita") },
            modifier = Modifier.weight(1f),
            colors = if (tipoSelecionado == "Receita") {
                ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            } else {
                ButtonDefaults.outlinedButtonColors()
            }
        ) { Text("Receita") }
    }
}