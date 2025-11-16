package com.example.appfinanceiro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardLancamento(
    tipo: String, //"Receita" ou "Despesa"
    descricao: String,
    data: String,
    valor: Double
) {
    val (corValor, icone) = if (tipo == "Receita") {
        Pair(Color(0xFF2E8B57), Icons.Default.KeyboardArrowUp)
    } else {
        Pair(Color(0xFFD94350), Icons.Default.KeyboardArrowDown)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,

                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icone,
                    contentDescription = tipo,
                    tint = corValor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = descricao,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = data,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "R$ ${"%.2f".format(valor)}",
                fontWeight = FontWeight.Bold,
                color = corValor,
                fontSize = 16.sp,
                // BÔNUS: Garante que o *valor* nunca quebre em duas linhas
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardReceita() {
    CardLancamento(
        tipo = "Receita",
        descricao = "Salário do Mês",
        data = "28/10/2025",
        valor = 3500.50
    )
}