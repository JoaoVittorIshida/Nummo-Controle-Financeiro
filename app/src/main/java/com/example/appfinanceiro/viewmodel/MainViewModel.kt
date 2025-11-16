package com.example.appfinanceiro.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfinanceiro.data.repository.LancamentoRepository
import com.example.appfinanceiro.state.toUI
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.appfinanceiro.data.entity.Lancamento
import com.example.appfinanceiro.state.LancamentoUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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

    var exibeModalLancamento by mutableStateOf(false)
        private set

    var idLancamentoAberto by mutableStateOf<Int?>(null)
        private set

    var mensagemErroLancamento by mutableStateOf<String?>(null)
        private set


    fun onAbaSelecionada(index: Int) {
        abaSelecionada = index
        if (exibeModalLancamento) {
            onFecharModal()
        }
    }
    fun onFabClick() {
        idLancamentoAberto = null // Garante que é um *novo* lançamento
        exibeModalLancamento = true
        resetarFormulario() // Limpa os campos antes de mostrar
    }
    fun onFecharModal() {
        exibeModalLancamento = false
        idLancamentoAberto = null
        resetarFormulario()
    }

    fun onAbrirModalDeEdicao(id: Int) {
        idLancamentoAberto = id
        exibeModalLancamento = true
        carregarDadosParaEdicao(id)
    }

    // --- Flows de UI ---
    val listaDe4Lancamentos = lancamentoRepository.quatroLancamentosRecentes
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


    private val calendar = Calendar.getInstance()
    val nomeDosMeses = arrayOf(
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    )
    val mapaDosMeses = nomeDosMeses.mapIndexed { index, nome -> nome to index }.toMap()

    private val _filtroMes = MutableStateFlow(nomeDosMeses[calendar.get(Calendar.MONTH)])
    val filtroMes: StateFlow<String> = _filtroMes

    private val _filtroAno = MutableStateFlow(calendar.get(Calendar.YEAR).toString())
    val filtroAno: StateFlow<String> = _filtroAno

    private val _filtroTipo = MutableStateFlow("Todos")
    val filtroTipo: StateFlow<String> = _filtroTipo

    fun onFiltroMesChange(novoMes: String) {
        _filtroMes.value = novoMes
    }

    fun onFiltroAnoChange(novoAno: String) {
        _filtroAno.value = novoAno
    }

    fun onFiltroTipoChange(novoTipo: String) {
        _filtroTipo.value = novoTipo
    }

    val extratoFiltrado: StateFlow<List<LancamentoUI>> = combine(
        lancamentoRepository.todosLancamentos,
        _filtroMes,
        _filtroAno,
        _filtroTipo
    ) { listaPura, mesString, anoString, tipo ->

        val mesInt = mapaDosMeses[mesString] ?: -1
        val anoInt = anoString.toIntOrNull() ?: -1
        val calendarInstance = Calendar.getInstance()

        listaPura.filter { lancamento ->
            val filtroTipoPassou = when (tipo) {
                "Receita" -> lancamento.tipo == "Receita"
                "Despesa" -> lancamento.tipo == "Despesa"
                else -> true // "Todos"
            }
            calendarInstance.timeInMillis = lancamento.data
            val mesDoLancamento = calendarInstance.get(Calendar.MONTH)
            val anoDoLancamento = calendarInstance.get(Calendar.YEAR)
            val filtroDataPassou = (mesDoLancamento == mesInt && anoDoLancamento == anoInt)

            filtroTipoPassou && filtroDataPassou

        }.map { lancamentoFiltrado ->
            lancamentoFiltrado.toUI()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


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
        mensagemErroLancamento = ""
    }

    fun formatarData(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }


    fun onSalvarLancamento() {
        if (formValor.isBlank()){
            mensagemErroLancamento = "O valor é obrigatório."
            return
        }

        if (formDescricao.isBlank()){
            mensagemErroLancamento = "A descrição é obrigatória."
            return
        }

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
        onFecharModal()
    }

    fun onEditarLancamento() {
        if (formValor.isBlank()){
            mensagemErroLancamento = "O valor é obrigatório."
            return
        }

        if (formDescricao.isBlank()){
            mensagemErroLancamento = "A descrição é obrigatória."
            return
        }

        val valorNumerico = formValor.replace(",", ".").toDoubleOrNull() ?: return

        viewModelScope.launch {
            val lancamentoAtualizado = Lancamento(
                id_lancamento = idLancamentoAberto!!,
                tipo = formTipo,
                descricao = formDescricao,
                valor = valorNumerico,
                observacoes = formObservacoes.takeIf { it.isNotBlank() },
                data = formData
            )

            lancamentoRepository.atualizarLancamento(lancamentoAtualizado)
            onFecharModal()
        }
    }

    fun onExcluirLancamento() {
        viewModelScope.launch {
            val lancamentoParaExcluir = Lancamento(
                id_lancamento = idLancamentoAberto!!,
                tipo = formTipo,
                descricao = formDescricao,
                valor = formValor.toDoubleOrNull() ?: 0.0,
                observacoes = formObservacoes,
                data = formData
            )

            lancamentoRepository.deletarLancamento(lancamentoParaExcluir)
            onFecharModal()
        }
    }

    fun carregarDadosParaEdicao(id: Int) {
        viewModelScope.launch {
            val lancamento = lancamentoRepository.getLancamentoPorId(id)

            if (lancamento != null) {
                formTipo = lancamento.tipo
                formValor = lancamento.valor.toString()
                formDescricao = lancamento.descricao
                formData = lancamento.data
                formObservacoes = lancamento.observacoes ?: ""
            }
        }
    }

    fun onCalcularConversao() {
        val taxa = 0.18
        val valor = convValorEntrada.toDoubleOrNull() ?: 0.0
        convResultado = "%.2f".format(valor * taxa)
    }
}

