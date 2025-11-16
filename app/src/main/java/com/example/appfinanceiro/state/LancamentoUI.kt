package com.example.appfinanceiro.state

import com.example.appfinanceiro.data.entity.Lancamento
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class LancamentoUI(
    val id_lancamento: Int,
    val tipo: String,
    val descricao: String,
    val data: String,
    val valor: Double
)
fun Lancamento.toUI(): LancamentoUI {

    // Conversor Long → "dd/MM/yyyy"
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val dataFormatada = Instant.ofEpochMilli(data)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)

    return LancamentoUI(
        id_lancamento = id_lancamento,
        tipo = tipo,
        descricao = descricao,
        data = dataFormatada,
        valor = valor
    )
}