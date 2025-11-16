package com.example.appfinanceiro.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfinanceiro.components.SeletorDeTipo
import com.example.appfinanceiro.ui.theme.AppFinanceiroTheme
import com.example.appfinanceiro.viewmodel.MainViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaFormularioLancamento(
    viewModel: MainViewModel,
    onFechar: () -> Unit,
    lancamentoId: Int?
) {
    val context = LocalContext.current
    val isEditMode = (lancamentoId != null)
    val titulo = if (isEditMode) "Editar Lançamento" else "Novo Lançamento"

    LaunchedEffect(lancamentoId) {
        if (lancamentoId != null) {
            viewModel.carregarDadosParaEdicao(lancamentoId)
        } else {
            viewModel.resetarFormulario()
        }
    }

    val calendar = Calendar.getInstance().apply {
        timeInMillis = viewModel.formData
    }
    val ano = calendar.get(Calendar.YEAR)
    val mes = calendar.get(Calendar.MONTH)
    val dia = calendar.get(Calendar.DAY_OF_MONTH)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(titulo, style = MaterialTheme.typography.headlineMedium) // MUDANÇA: Título dinâmico
                IconButton(onClick = onFechar) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SeletorDeTipo(
                tipoSelecionado = viewModel.formTipo,
                onTipoChange = { viewModel.formTipo = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.formValor,
                onValueChange = { viewModel.formValor = it },
                label = { Text("Valor (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.formDescricao,
                onValueChange = { viewModel.formDescricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
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
            OutlinedTextField(
                value = viewModel.formObservacoes,
                onValueChange = { viewModel.formObservacoes = it },
                label = { Text("Observações (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            viewModel.mensagemErroLancamento?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            if (isEditMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.onExcluirLancamento()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Text("EXCLUIR")
                    }

                    Button(
                        onClick = {
                            viewModel.onEditarLancamento()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("SALVAR EDIÇÃO")
                    }
                }
            } else {
                Button(
                    onClick = {
                        viewModel.onSalvarLancamento()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("SALVAR LANÇAMENTO")
                }
            }
        }
    }
}

@Preview(showBackground = true) @Composable fun PreviewTelaNovoLancamentoSimples() {
    AppFinanceiroTheme { TelaFormularioLancamento(viewModel(), onFechar = {}, lancamentoId = null) }
}