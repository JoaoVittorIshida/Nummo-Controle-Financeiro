# Nummo - Controle Financeiro

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)

> **Nummo** (*latim: moeda*) é um aplicativo Android moderno para gestão de finanças pessoais, desenvolvido com foco em arquitetura limpa e tecnologias nativas atuais.

---

## 📱 Sobre o Projeto

O **Nummo** foi desenvolvido para simplificar o controle financeiro diário. Ele permite que o usuário registre receitas e despesas, visualize o saldo mensal, filtre lançamentos históricos e realize conversões de moedas em tempo real.

O projeto também serviu como base para estudo aprofundado de desenvolvimento Android Nativo, aplicando conceitos de **MVVM**, **Injeção de Dependência Manual**, **Clean Code** e **Padrões de Projeto**.


<div align="center">
  <img src="https://github.com/user-attachments/assets/820db7ba-a96d-48fe-99b7-78d8ba131058" width="250" alt="Tela Inicial" style="margin: 5px;"/>
  <img src="https://github.com/user-attachments/assets/f534e55f-44f7-4677-9600-da49c388168b" width="250" alt="Tela de Extrato" style="margin: 5px;"/>
  <img src="https://github.com/user-attachments/assets/346ebd8b-780b-4704-a9eb-5ba148989e23" width="250" alt="Tela de Conversor" style="margin: 5px;"/>
</div>

---

## ✨ Funcionalidades

* **Gestão de Lançamentos:** Adicionar, editar e excluir receitas e despesas.
* **Dashboard:** Visualização rápida do saldo do mês atual e últimos lançamentos.
* **Filtros Avançados:** Filtragem de extrato por Mês, Ano e Tipo (Receita/Despesa) de forma reativa.
* **Conversor de Moedas:** Calculadora integrada que busca cotações em tempo real (Dólar e Euro) via API.
* **Segurança:** Login com autenticação biométrica (digital) ou PIN.
* **Persistência:** Todos os dados funcionam offline (exceto atualização de cotação).

---

## 🛠️ Tecnologias e Arquitetura

O projeto segue a arquitetura **MVVM (Model-View-ViewModel)** com **Clean Architecture**.

### Tech Stack
* **Linguagem:** Kotlin
* **UI:** Jetpack Compose (Material Design 3)
* **Banco de Dados:** Room Database (SQLite)
* **Rede (API):** Retrofit + Gson
* **Assincronismo:** Coroutines & StateFlow

### Padrões de Projeto Aplicados
* **Repository Pattern:** Para abstrair a fonte de dados (Local vs Remoto) e isolar o ViewModel da lógica de dados.
* **Dependency Injection (Manual):** Uso de `AppContainer` para gerenciar dependências de forma centralizada.
* **Single Source of Truth:** O Banco de Dados (Room) é a única fonte de verdade para a UI.

---

## 🔌 API Externa

O módulo de conversão monetária utiliza a API pública da [AwesomeAPI](https://docs.awesomeapi.com.br/api-de-moedas) para obter cotações atualizadas de USD-BRL e EUR-BRL.

---


## 🚀 Como rodar o projeto

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/SEU-USUARIO/nummo-financeiro.git](https://github.com/SEU-USUARIO/nummo-financeiro.git)
Abra no Android Studio: Certifique-se de usar a versão mais recente (Koala ou Ladybug recomendadas).

Sincronize o Gradle: Aguarde o download das dependências.

Execute: Selecione um emulador ou dispositivo físico e clique em "Run".
