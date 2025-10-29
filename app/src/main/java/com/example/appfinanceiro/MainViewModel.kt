package com.example.appfinanceiro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    // --- Estados de Navegação (que estavam no MainActivity) ---
    var abaSelecionada by mutableStateOf(0)
        private set // A tela não pode mudar isso, só o ViewModel

    var exibeModalNovoLancamento by mutableStateOf(false)
        private set

    // --- Estados da TelaInicio e TelaExtrato ---
    var listaDeLancamentos by mutableStateOf(listaDeLancamentosFalsa) // Vem dos seus dados falsos
        private set
    var listaCompleta by mutableStateOf(listaCompletaFalsa) // Vem dos seus dados falsos
        private set
    // (O dev de lógica vai trocar isso por uma chamada ao Room)

    // --- Estados dos Filtros (TelaExtrato) ---
    var filtroMes by mutableStateOf("Outubro")
    var filtroAno by mutableStateOf("2025")
    var filtroTipo by mutableStateOf("Todos")

    // --- Estados do Conversor (TelaConversor) ---
    var convValorEntrada by mutableStateOf("")
    var convMoedaDe by mutableStateOf("BRL")
    var convMoedaPara by mutableStateOf("USD")
    var convResultado by mutableStateOf("0.00")
        private set

    // --- Estados do Formulário (TelaNovoLancamento) ---
    var formTipo by mutableStateOf("Despesa")
    var formValor by mutableStateOf("")
    var formDescricao by mutableStateOf("")
    var formData by mutableStateOf("28/10/2025")
    var formObservacoes by mutableStateOf("")


    // --- EVENTOS / LÓGICA (O que a sua UI vai chamar) ---

    fun onAbaSelecionada(index: Int) {
        abaSelecionada = index
    }

    fun onFabClick() {
        exibeModalNovoLancamento = true
    }

    fun onFecharModal() {
        exibeModalNovoLancamento = false
    }

    fun onSalvarLancamento() {
        // TODO: O dev de lógica vai salvar isso no Room

        // Por enquanto, vamos só adicionar na lista (para o design funcionar)
        val novoLanc = Lancamento(
            id = (listaDeLancamentos.size + 1),
            tipo = formTipo,
            desc = formDescricao,
            data = formData,
            valor = formValor.toDoubleOrNull() ?: 0.0
        )
        listaDeLancamentos = listOf(novoLanc) + listaDeLancamentos

        // Limpar o formulário e fechar
        formTipo = "Despesa"
        formValor = ""
        formDescricao = ""
        formObservacoes = ""
        exibeModalNovoLancamento = false
    }

    fun onCalcularConversao() {
        // TODO: O dev de lógica vai chamar uma API de cotação aqui
        val cotacaoBrlUsd = 0.18
        val valor = convValorEntrada.toDoubleOrNull() ?: 0.0
        val resultado = valor * cotacaoBrlUsd
        convResultado = "%.2f".format(resultado)
    }
}