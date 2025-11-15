package com.example.appfinanceiro.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfinanceiro.data.repository.LancamentoRepository
import com.example.appfinanceiro.viewmodel.toUI
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.appfinanceiro.data.entity.Lancamento
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel(
    private val lancamentoRepository: LancamentoRepository
) : ViewModel() {

    // --- Navegação ---
    var abaSelecionada by mutableStateOf(0)
        private set

    var exibeModalNovoLancamento by mutableStateOf(false)
        private set

    fun onAbaSelecionada(index: Int) {
        abaSelecionada = index
        if (exibeModalNovoLancamento) {
            onFecharModal()
        }
    }
    fun onFabClick() { exibeModalNovoLancamento = true }
    fun onFecharModal() {
        exibeModalNovoLancamento = false
        resetarFormulario()
    }

    // --- Flows de UI ---
    val listaDe3Lancamentos = lancamentoRepository.tresLancamentosRecentes
        .map { it.map { item -> item.toUI() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val listaCompleta = lancamentoRepository.todosLancamentos
        .map { it.map { item -> item.toUI() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val saldoDoMes: StateFlow<Double> = lancamentoRepository.todosLancamentos
        .map { lista ->
            val calendar = Calendar.getInstance()
            val mesSelecionado = calendar.get(Calendar.MONTH)
            val anoSelecionado = calendar.get(Calendar.YEAR)

            lista.filter { lancamento ->
                calendar.timeInMillis = lancamento.data
                val mes = calendar.get(Calendar.MONTH)
                val ano = calendar.get(Calendar.YEAR)
                mes == mesSelecionado && ano == anoSelecionado
            }.sumOf { lancamento ->
                if (lancamento.tipo == "Receita") lancamento.valor
                else -lancamento.valor
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    // --- Filtros ---
    var filtroMes by mutableStateOf("Outubro")
    var filtroAno by mutableStateOf("2025")
    var filtroTipo by mutableStateOf("Todos")


    // --- Conversor ---
    var convValorEntrada by mutableStateOf("")
    var convMoedaDe by mutableStateOf("BRL")
    var convMoedaPara by mutableStateOf("USD")
    var convResultado by mutableStateOf("0.00")
        private set


    // --- Formulário ---
    var formTipo by mutableStateOf("Entrada")
    var formValor by mutableStateOf("")
    var formDescricao by mutableStateOf("")
    var formData by mutableStateOf(System.currentTimeMillis())
    var formObservacoes by mutableStateOf("")

    private fun getDataAtual(): Long {
        return Calendar.getInstance().timeInMillis
    }

    fun resetarFormulario() {
        formTipo = "Despesa"
        formValor = ""
        formDescricao = ""
        formData = System.currentTimeMillis() // já retorna timestamp atual
        formObservacoes = ""
    }

    fun formatarData(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }


    // --- Salvar ---
    fun onSalvarLancamento() {
        if (formValor.isBlank() || formDescricao.isBlank()) return

        val valorNumerico = formValor.replace(",", ".").toDoubleOrNull() ?: return

        val lancamento = Lancamento(
            tipo = formTipo,
            valor = valorNumerico,
            descricao = formDescricao,
            data = formData,
            observacoes = formObservacoes
        )

        viewModelScope.launch {
            lancamentoRepository.inserirLancamento(lancamento)
        }

        resetarFormulario() // Limpa todos os campos
        onFecharModal()
    }

    // --- Conversor ---
    fun onCalcularConversao() {
        val taxa = 0.18
        val valor = convValorEntrada.toDoubleOrNull() ?: 0.0
        convResultado = "%.2f".format(valor * taxa)
    }
}

