package com.example.appfinanceiro.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfinanceiro.data.entity.Cotacao
import com.example.appfinanceiro.data.repository.LancamentoRepository
import com.example.appfinanceiro.state.toUI
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.appfinanceiro.data.entity.Lancamento
import com.example.appfinanceiro.data.entity.Usuario
import com.example.appfinanceiro.data.repository.CotacaoRepository
import com.example.appfinanceiro.data.repository.UsuarioRepository
import com.example.appfinanceiro.state.LancamentoUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel(
    private val lancamentoRepository: LancamentoRepository,
    private val cotacaoRepository: CotacaoRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    var abaSelecionada by mutableStateOf(0)
        private set

    var exibeModalLancamento by mutableStateOf(false)
        private set

    var idLancamentoAberto by mutableStateOf<Int?>(null)
        private set

    var mensagemErroLancamento by mutableStateOf<String?>(null)
        private set

    var usuarioLogado by mutableStateOf<String?>("")
        private set

    fun carregarUsuarioLogado(){
        viewModelScope.launch {
            val usuarioObtido = usuarioRepository.getUsuarioLogado()

            if(usuarioObtido != null){
                usuarioLogado = usuarioObtido.nome
            }
        }
    }

    fun onAbaSelecionada(index: Int) {
        abaSelecionada = index
        if (exibeModalLancamento) {
            onFecharModal()
        }
    }
    fun onFabClick() {
        idLancamentoAberto = null
        exibeModalLancamento = true
        resetarFormulario()
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


    val listaDe3Lancamentos = lancamentoRepository.tresLancamentosRecentes
        .map { it.map { item -> item.toUI() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val saldoTotal: StateFlow<Double> = lancamentoRepository.todosLancamentos
        .map { lista ->
            val diaAtual = (Calendar.getInstance()).timeInMillis

            lista.filter { lancamento ->
                lancamento.data <= diaAtual
            }.sumOf { lancamento ->
                if (lancamento.tipo == "Receita") lancamento.valor
                else -lancamento.valor
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    val saldoPrevistoDoMes: StateFlow<Double> = lancamentoRepository.todosLancamentos
        .map { lista ->
            val calendar = Calendar.getInstance()
            val mesSelecionado = calendar.get(Calendar.MONTH)
            val anoSelecionado = calendar.get(Calendar.YEAR)
            val diaAtual = (Calendar.getInstance()).timeInMillis


            lista.filter { lancamento ->
                calendar.timeInMillis = lancamento.data
                val mes = calendar.get(Calendar.MONTH)
                val ano = calendar.get(Calendar.YEAR)

                if(lancamento.data > diaAtual)
                    mes == mesSelecionado && ano == anoSelecionado
                else
                    true

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
                else -> true
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

    var formTipo by mutableStateOf("Entrada")
    var formValor by mutableStateOf("")
    var formDescricao by mutableStateOf("")
    var formData by mutableStateOf(System.currentTimeMillis())
    var formObservacoes by mutableStateOf("")

    fun resetarFormulario() {
        formTipo = "Despesa"
        formValor = ""
        formDescricao = ""
        formData = System.currentTimeMillis()
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

    private val _convValorEntrada = MutableStateFlow("")
    val convValorEntrada: StateFlow<String> = _convValorEntrada

    private val _convMoedaDe = MutableStateFlow("BRL")
    val convMoedaDe: StateFlow<String> = _convMoedaDe

    private val _convMoedaPara = MutableStateFlow("USD")
    val convMoedaPara: StateFlow<String> = _convMoedaPara

    init {
        buscarCotacoesEmSegundoPlano()
        carregarUsuarioLogado()
    }

    private fun buscarCotacoesEmSegundoPlano() {
        viewModelScope.launch {
            cotacaoRepository.atualizarCotacoes()
        }
    }

    val cotacaoAtual: StateFlow<Cotacao?> = cotacaoRepository.cotacaoMaisRecente
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )


    fun onConvValorChange(novoValor: String) {
        _convValorEntrada.value = novoValor
    }

    fun onConvMoedaDeChange(novaMoeda: String) {
        _convMoedaDe.value = novaMoeda
    }

    fun onConvMoedaParaChange(novaMoeda: String) {
        _convMoedaPara.value = novaMoeda
    }
    val convResultado: StateFlow<String> = combine(
        _convValorEntrada,
        _convMoedaDe,
        _convMoedaPara,
        cotacaoAtual
    ) { valorStr, moedaDe, moedaPara, cotacoes ->

        val valor = valorStr.toDoubleOrNull() ?: 0.0

        if (valor == 0.0) {
            "0.00"
        } else if (cotacoes == null || cotacoes.dolar == 0.0 || cotacoes.euro == 0.0) {
            "..."
        } else {
            val taxaDolar = cotacoes.dolar
            val taxaEuro = cotacoes.euro

            val valorEmBRL = when (moedaDe) {
                "USD" -> valor * taxaDolar
                "EUR" -> valor * taxaEuro
                "BRL" -> valor
                else -> 0.0
            }

            val resultadoFinal = when (moedaPara) {
                "USD" -> valorEmBRL / taxaDolar
                "EUR" -> valorEmBRL / taxaEuro
                "BRL" -> valorEmBRL
                else -> 0.0
            }
            "%.2f".format(resultadoFinal)
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "0.00"
    )
}

