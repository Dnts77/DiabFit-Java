# DiabFit - Aplicativo Android

## Descrição

DiabFit é um aplicativo Android desenvolvido como um projeto de aprendizado. O objetivo principal é criar uma ferramenta simples para ajudar os usuários a monitorar métricas de saúde essenciais relacionadas ao diabetes, como níveis de açúcar no sangue, peso e altura.

Este projeto serve como um exercício prático para aplicar conceitos fundamentais do desenvolvimento Android, incluindo a criação de interfaces de usuário, validação de dados, navegação entre telas e persistência de dados locais.

**Importante:** Este aplicativo foi criado para fins educacionais e não deve ser utilizado para monitoramento médico real.

## Funcionalidades

- **Cadastro e Autenticação de Usuário:** Permite que os usuários criem uma conta e façam login (utilizando Firebase Authentication).
- **Entrada de Dados de Saúde:** Campos para inserir informações como nível de açúcar, peso e altura.
- **Validação de Dados:** Garante que os dados inseridos pelo usuário estejam em um formato válido e dentro dos limites esperados.
- **Persistência de Dados:** As informações do usuário são salvas localmente no dispositivo usando `SharedPreferences`.
- **Interface de Usuário Simples:** Telas intuitivas construídas com XML.

## Tecnologias e Conceitos Aplicados

- **Linguagem:** Java
- **Arquitetura:** Model-View-ViewModel (MVVM) básico com `AppCompatActivity`.
- **Interface de Usuário (UI):**
  - Layouts XML (LinearLayout, ScrollView).
  - Componentes de Material Design (Button, EditText, TextView).
- **Navegação:** `Intent` para transição entre as diferentes telas (Activities).
- **Persistência de Dados:** `SharedPreferences` para armazenar dados simples de forma local.
- **Backend as a Service (BaaS):**
  - **Firebase Authentication** para gerenciamento de login e cadastro.
- **Gradle:** Gerenciamento de dependências.

## Como Executar o Projeto

1.  Clone este repositório.
2.  Abra o projeto no Android Studio.
3.  Configure um projeto no Firebase e adicione o arquivo `google-services.json` no diretório `app/`.
4.  Compile e execute o aplicativo em um emulador ou dispositivo Android.
