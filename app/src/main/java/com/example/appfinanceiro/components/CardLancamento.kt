package com.example.appfinanceiro.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appfinanceiro.state.LancamentoUI

@Composable
fun CardLancamento(
    lancamento: LancamentoUI,
    onClick: (Int) -> Unit
) {
    val (corValor, icone) = if (lancamento.tipo == "Receita") {
        Pair(Color(0xFF2E8B57), Icons.Default.KeyboardArrowUp)
    } else {
        Pair(Color(0xFFD94350), Icons.Default.KeyboardArrowDown)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick(lancamento.id_lancamento) },
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
                    contentDescription = lancamento.tipo,
                    tint = corValor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = lancamento.descricao,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = lancamento.data,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "R$ ${"%.2f".format(lancamento.valor)}",
                fontWeight = FontWeight.Bold,
                color = corValor,
                fontSize = 16.sp,
                maxLines = 1
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewCardReceita() {
    //CardLancamento(
        //tipo = "Receita",
        //descricao = "Salário do Mês",
        //data = "28/10/2025",
        //valor = 3500.50
    //)
//}