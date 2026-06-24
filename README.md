# 🏨 Sistema de Gestão Hoteleira - Frontend (Desktop Client)

Este repositório contém a interface gráfica desktop de um **Sistema de Gestão Hoteleira** desenvolvido em **Java 21** com **Swing**. O projeto foi projetado com foco em excelente experiência de usuário, implementando um Design System próprio com visual moderno (estilo *Tailwind Slate/Blue*), carregamento assíncrono de requisições à API e arquitetura limpa com injeção de dependência manual.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** [Java 21](https://www.oracle.com/java/technologies/downloads/) (uso extensivo de recursos modernos como `CompletableFuture` e `HttpClient` nativo para chamadas não bloqueantes).
- **Interface Gráfica:** [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing/) (customização avançada de componentes e renderizadores de tabelas/cronogramas).
- **Gerenciador de Dependências:** [Maven](https://maven.apache.org/) para build e gestão de bibliotecas.
- **Processamento de JSON:** [Google Gson](https://github.com/google/gson) para serialização/deserialização na comunicação com a API REST.

---

## 🏗️ Arquitetura do Software

O projeto segue um padrão arquitetural baseado em **MVC (Model-View-Controller)** adaptado para interfaces desktop corporativas, separado em camadas bem definidas sob o diretório `src`:

```
src/
├── DTO/           # Objetos de Transferência de Dados para desserialização de JSON
├── components/    # Design System: Componentes customizados Swing (botões, modais, etc.) com prefixo "Ds"
├── config/        # Configuração da URL da API (ApiConfig) e Container de Injeção de Dependências (DIContainer)
├── controllers/   # Controladores que manipulam a lógica das telas e coordenam serviços (BaseController, etc.)
├── enums/         # Definições de enums de controle interno
├── main/          # Ponto de entrada do sistema (Main.java que inicia a StartupView)
├── models/        # Modelos customizados, como o modelo de dados do cronograma (ReservaTimelineTableModel)
├── resources/     # Recursos estáticos (fontes customizadas TTF, avatares e imagens)
├── services/      # Integração com a API Backend de forma assíncrona (HttpClient, Gson)
├── theme/         # Definição visual (DesignTokens - Paleta de cores, tipografia, bordas e espaçamentos)
└── views/         # Interfaces da aplicação (DashboardView, TelaReservas, TelaHospedes, MainLayoutView, etc.)
```

### Destaques Arquiteturais:
1. **Injeção de Dependências Manual:** Centralizada em [DIContainer.java](file:///c:/Users/corre/Downloads/demo/hotelaria-front/src/config/DIContainer.java) (implementado como Singleton), garantindo que as Views, Controllers e Services sejam desacoplados e facilmente testáveis.
2. **Comunicação Assíncrona:** As chamadas HTTP externas são efetuadas usando `sendAsync` do `HttpClient` nativo do Java, retornando `CompletableFuture`. Isso evita o travamento da Thread principal do Swing (EDT - Event Dispatch Thread) durante requisições de rede.

---

## 🌟 Design System & Estilização

A aplicação não utiliza o visual padrão "cinza" do Swing clássico. Foi desenvolvido um conjunto de **Design Tokens** inspirados no ecossistema moderno de CSS (como Tailwind Slate):

- **Paleta de Cores (`DesignTokens.ColorPalette`):** Tons de Slate escuro para o menu lateral (Sidebar), Azul Premium para cores primárias/botões, e cores de feedback semântico (Success, Warning, Danger) para status e alertas.
- **Tipografia (`DesignTokens.Typography`):** Carregamento automático e uso da fonte moderna **Archivo** (Regular/Bold).
- **Espaçamento (`DesignTokens.Spacing`):** Definições estritas (`XS`, `SM`, `MD`, `LG`, `XL`, `XXL`) para alinhamento e margens.
- **Cantos Arredondados (`DesignTokens.Radius`):** Bordas arredondadas e suavizadas para painéis e botões.

Os componentes customizados do Design System estão localizados na pasta `components/` e incluem:
- `DsButton`: Botão moderno com efeitos de hover ativos.
- `DsCard`: Cartão elevado com visualização de informações limpas.
- `DsDialog` / `DsModal`: Caixas de diálogo modais estilizadas.
- `DsTimelineTable` / `DsTimelineCell`: Grid interativo personalizado para exibição de cronogramas.

---

## 🚀 Funcionalidades Principais

Conforme as regras de negócio do sistema (`business-rules.md`), a interface fornece três fluxos de operação hoteleira essenciais:

### 1. Página Inicial (Dashboard Operacional)
- Painel para recepção com ações rápidas de entradas e saídas previstas para o dia atual.
- Listas de **Check-ins do dia** e **Check-outs do dia**.
- Modais rápidos de transição contendo resumos de consumo extra (frigobar/restaurante) e acerto de contas antes da confirmação.

### 2. Mapa de Reservas (Cronograma Interativo)
- Matriz visual de timeline. O eixo vertical lista os quartos do hotel e o eixo horizontal exibe os dias do mês.
- Blocos coloridos de status de ocupação do quarto com o nome do hóspede associado.
- Botão direto para criação de novas reservas.

### 3. Wizard de Criação de Reserva (Jornada em 4 Passos)
Um assistente interativo em etapas para evitar erros operacionais:
1. **Passo 1 (Identificação):** Busca de hóspede existente por nome/CPF ou link direto para cadastro de novo cliente.
2. **Passo 2 (Período):** Seleção das datas de entrada e saída com cálculo dinâmico da diária e dias da estadia.
3. **Passo 3 (Seleção do Quarto):** Lista filtrada apenas com quartos disponíveis para o período desejado.
4. **Passo 4 (Confirmação):** Resumo completo dos dados da reserva e confirmação financeira antes de finalizar.

### 4. Diretório de Hóspedes
- Lista de clientes cadastrados no sistema (Nome, CPF formatado, e-mail e data de nascimento).
- Campo de busca em tempo real com filtros rápidos.
- Modal de inclusão de novos hóspedes com validação local/remota.

---

## 📡 Integração com a API REST

A interface integra-se com uma API backend rodando localmente. O arquivo de configuração principal é o [ApiConfig.java](file:///c:/Users/corre/Downloads/demo/hotelaria-front/src/config/ApiConfig.java).

- **URL Base Padrão:** `http://localhost:8080/api`

### Endpoints Consumidos:
- **Quartos:**
  - `GET /api/rooms` - Lista resumida de quartos.
  - `GET /api/rooms/{id}` - Especificação detalhada e hóspede atual.
- **Hóspedes:**
  - `GET /api/guests?name={filtro}` - Buscar hóspedes por nome.
  - `POST /api/guests` - Cadastrar novo cliente.
- **Hospedagem & Reservas:**
  - `GET /api/reservations` - Listagem de reservas cadastradas.
  - `POST /api/reservations` - Criar nova reserva.
  - `POST /api/reservations/{id}/checkin` - Efetivar o check-in.
  - `POST /api/reservations/{id}/checkout` - Efetivar o check-out.

---

## ⚙️ Instalação e Execução

### Pré-requisitos
1. **Java Development Kit (JDK) 21** instalado e configurado nas variáveis de ambiente (`JAVA_HOME`).
2. **Apache Maven** instalado.
3. Certifique-se de que a **API Backend** está sendo executada e escutando requisições na porta `8080`.

### Como rodar a aplicação:
Navegue até a raiz do projeto (onde está o arquivo `pom.xml`) e execute o seguinte comando no terminal:

```bash
mvn exec:java
```

Este comando utilizará o plugin `exec-maven-plugin` para compilar e iniciar a classe principal `main.Main`, abrindo a tela inicial do sistema.

### Como compilar o projeto (gerar o artefato JAR):
Para compilar todas as classes e empacotar a aplicação:

```bash
mvn clean package
```
Os arquivos gerados estarão no diretório `target/`.
