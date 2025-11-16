package com.example.appfinanceiro


import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appfinanceiro.ui.theme.AppFinanceiroTheme
import androidx.lifecycle.viewmodel.compose.viewModel // IMPORTANTE
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.example.appfinanceiro.viewmodel.AuthViewModel
import com.example.appfinanceiro.viewmodel.MainViewModel
import java.util.Calendar


fun Context.findFragmentActivity(): FragmentActivity? {
    return this as? FragmentActivity
}



// ----- TELA 1: INÍCIO (DASHBOARD) -----
@Composable
fun TelaInicio(
    viewModel: MainViewModel
) {
    val lista4Lancamentos by viewModel.listaDe4Lancamentos.collectAsState()
    val saldoMes by viewModel.saldoDoMes.collectAsState()
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
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
                    Text("Saldo do Mês", style = MaterialTheme.typography.titleMedium)
                    Text("R$ %.2f".format(saldoMes), style = MaterialTheme.typography.displaySmall)
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
        items(lista4Lancamentos) { lancamento ->
            CardLancamento(
                tipo = lancamento.tipo,
                descricao = lancamento.descricao,
                data = lancamento.data,
                valor = lancamento.valor
            )
        }
    }
}


// ----- TELA 2: EXTRATO (COM FILTROS) -----
@Composable
fun TelaExtrato(
    viewModel: MainViewModel
) {
    val listaLancamentosCompleta by viewModel.listaCompleta.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Extrato Completo", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Filtrar por:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropdownFiltro(
                label = "Mês",
                opcoes = listOf("Setembro", "Outubro", "Novembro"),
                selecionado = viewModel.filtroMes,
                onSelect = { viewModel.filtroMes = it },
                modifier = Modifier.weight(1f)
            )
            DropdownFiltro(
                label = "Ano",
                opcoes = listOf("2024", "2025"),
                selecionado = viewModel.filtroAno,
                onSelect = { viewModel.filtroAno = it },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        SeletorFiltroTipo(
            selecionado = viewModel.filtroTipo,
            onTipoChange = { viewModel.filtroTipo = it }
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaLancamentosCompleta) { lancamento ->
                CardLancamento(
                    tipo = lancamento.tipo,
                    descricao = lancamento.descricao,
                    data = lancamento.data,
                    valor = lancamento.valor
                )
            }
        }
    }
}


// ----- TELA 3: CONVERSOR -----
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaConversor(
    viewModel: MainViewModel // Recebe o Cérebro
) {
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
            value = viewModel.convValorEntrada, // Lendo do VM
            onValueChange = { viewModel.convValorEntrada = it }, // Escrevendo no VM
            label = { Text("Valor para converter") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("De:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        SeletorDeMoeda(
            moedaSelecionada = viewModel.convMoedaDe, // Lendo do VM
            onMoedaChange = { viewModel.convMoedaDe = it } // Escrevendo no VM
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Para:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        SeletorDeMoeda(
            moedaSelecionada = viewModel.convMoedaPara, // Lendo do VM
            onMoedaChange = { viewModel.convMoedaPara = it } // Escrevendo no VM
        )

        // Botão para calcular
        Button(
            onClick = { viewModel.onCalcularConversao() }, // Avisa o VM
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("CALCULAR")
        }

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
                    "${viewModel.convResultado} ${viewModel.convMoedaPara}", // Lendo do VM
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }
}

// ----- TELA DE NOVO LANÇAMENTO (FORMULÁRIO) -----
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaNovoLancamento(
    viewModel: MainViewModel,
    onFechar: () -> Unit
) {
    val context = LocalContext.current

    // Resetar o formulário quando abrir a tela
    LaunchedEffect(Unit) {
        viewModel.resetarFormulario()
    }

    // Criando um calendar com a data atual do formData
    val calendar = Calendar.getInstance().apply {
        timeInMillis = viewModel.formData
    }
    val ano = calendar.get(Calendar.YEAR)
    val mes = calendar.get(Calendar.MONTH)
    val dia = calendar.get(Calendar.DAY_OF_MONTH)

    // ---- DatePicker Nativo ----
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val c = Calendar.getInstance()
            c.set(year, month, dayOfMonth)
            viewModel.formData = c.timeInMillis
        },
        ano, mes, dia
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Cabeçalho
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Novo Lançamento", style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = onFechar) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Seletor de tipo
            SeletorDeTipo(
                tipoSelecionado = viewModel.formTipo,
                onTipoChange = { viewModel.formTipo = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Valor
            OutlinedTextField(
                value = viewModel.formValor,
                onValueChange = { viewModel.formValor = it },
                label = { Text("Valor (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descrição
            OutlinedTextField(
                value = viewModel.formDescricao,
                onValueChange = { viewModel.formDescricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Data - abre o DatePicker nativo
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() }
            ) {
                OutlinedTextField(
                    value = viewModel.formatarData(viewModel.formData),
                    onValueChange = {},
                    label = { Text("Data") },
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.Edit, contentDescription = "Editar Data") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Observações
            OutlinedTextField(
                value = viewModel.formObservacoes,
                onValueChange = { viewModel.formObservacoes = it },
                label = { Text("Observações (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botão Salvar
            Button(
                onClick = { viewModel.onSalvarLancamento() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("SALVAR LANÇAMENTO")
            }
        }
    }
}







// ----- TELA DE CRIAR CONTA -----
@Composable
fun TelaCriarConta(
    viewModel: AuthViewModel,
    onContaCriada: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Cadeado",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text( "Crie sua conta", style = MaterialTheme.typography.headlineLarge )
            Text( "Proteja seus dados financeiros.", style = MaterialTheme.typography.bodyMedium )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = viewModel.nome, // Lendo do VM
                onValueChange = { viewModel.nome = it }, // Escrevendo no VM
                label = { Text("Seu Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            CampoSenhaTextField(
                label = "Senha (PIN de 4 dígitos)",
                valor = viewModel.senha, // Lendo do VM
                onValorChange = { viewModel.senha = it } // Escrevendo no VM
            )
            Spacer(modifier = Modifier.height(16.dp))

            CampoSenhaTextField(
                label = "Confirmar Senha (PIN)",
                valor = viewModel.confirmarSenha, // Lendo do VM
                onValorChange = { viewModel.confirmarSenha = it } // Escrevendo no VM
            )
            Spacer(modifier = Modifier.height(16.dp))

            viewModel.mensagemErro?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable { viewModel.usarBiometria = !viewModel.usarBiometria }
            ) {
                Checkbox(
                    checked = viewModel.usarBiometria, // Lendo do VM
                    onCheckedChange = { viewModel.usarBiometria = it } // Escrevendo no VM
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Usar digital para desbloquear")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.onCriarContaClick() // Avisa o VM
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("CRIAR CONTA")
            }
        }
    }
}

// ----- TELA DE DESBLOQUEIO (LOGIN) -----
@Composable
fun TelaDesbloqueio(
    activity: FragmentActivity?,
    viewModel: AuthViewModel,
    onDesbloqueado: () -> Unit
) {
    // Coleta o primeiro usuário como estado para reagir a mudanças
    val primeiroUsuario by viewModel.primeiroUsuario.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text( "Bem-vindo de volta!", style = MaterialTheme.typography.headlineLarge )
            Text( "App bloqueado por segurança.", style = MaterialTheme.typography.bodyMedium )
            Spacer(modifier = Modifier.height(32.dp))

            if (primeiroUsuario?.digital == true) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Desbloquear com digital",
                    modifier = Modifier
                        .size(128.dp)
                        .clickable {
                            if (activity != null) {
                                viewModel.onBiometriaClick(
                                    activity = activity,
                                    onSuccess = onDesbloqueado
                                )
                            } else {
                                viewModel.mensagemErro = "Não foi possível iniciar a biometria"
                            }
                        },
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Toque no cadeado para usar a digital",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("... ou digite seu PIN ...")
            Spacer(modifier = Modifier.height(16.dp))

            CampoSenhaTextField(
                label = "Seu PIN",
                valor = viewModel.pinDigitado,
                onValorChange = {
                    viewModel.pinDigitado = it
                    // Limpa a mensagem de erro quando o usuário digita
                    if (viewModel.mensagemErro != null) {
                        viewModel.mensagemErro = null
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Exibe a mensagem de erro de login
            viewModel.mensagemErro?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    viewModel.onDesbloquearClick(onSuccess = onDesbloqueado)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("ENTRAR")
            }
        }
    }
}


// ----- COMPONENTES DE UI (Não mudam, só foram movidos) -----

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownFiltro(
    label: String,
    opcoes: List<String>,
    selecionado: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) } // OK ter remember aqui (estado de UI)

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selecionado,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true
        )
        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opcoes.forEach { opcao ->
                DropdownMenuItem(
                    text = { Text(opcao) },
                    onClick = {
                        onSelect(opcao)
                        expandido = false
                    }
                )
            }
        }
    }
}

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

@Composable
fun CampoSenhaTextField(
    label: String,
    valor: String,
    onValorChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = PasswordVisualTransformation()
    )
}

// ----- PREVIEWS (Não precisam de ViewModel) -----
// (O Preview agora pode não mostrar todos os dados,
// mas o app rodando vai funcionar)
@Preview(showBackground = true) @Composable fun PreviewTelaCriarConta() {
    AppFinanceiroTheme { TelaCriarConta(viewModel(), onContaCriada = {}) }
}
@Preview(showBackground = true) @Composable fun PreviewTelaDesbloqueio() {
    AppFinanceiroTheme {
        val viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)

        TelaDesbloqueio(
            activity = null,
            viewModel = viewModel,
            onDesbloqueado = {}
        )
    }
}
@Preview(showBackground = true) @Composable fun PreviewTelaInicioSimples() {
    AppFinanceiroTheme { TelaInicio(viewModel()) }
}
@Preview(showBackground = true) @Composable fun PreviewTelaExtrato() {
    AppFinanceiroTheme { TelaExtrato(viewModel()) }
}
@Preview(showBackground = true) @Composable fun PreviewTelaConversor() {
    AppFinanceiroTheme { TelaConversor(viewModel()) }
}
@Preview(showBackground = true) @Composable fun PreviewTelaNovoLancamentoSimples() {
    AppFinanceiroTheme { TelaNovoLancamento(viewModel(), onFechar = {}) }
}