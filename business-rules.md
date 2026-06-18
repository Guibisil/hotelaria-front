Aqui está a arquitetura de informação e a descrição das interações do sistema de hotelaria, estruturadas no formato de um sitemap detalhado.

## Sitemap: Sistema de Gestão Hoteleira

A navegação principal do sistema é dividida em três seções principais, acessíveis pelo menu lateral: **Página Inicial**, **Mapa de Reservas** e **Hóspedes**.

---

### 1. Página Inicial (Dashboard Operacional)

Atua como o painel principal de controle diário para a recepção, focando em ações imediatas de entrada e saída.

* **Informações Exibidas:** Duas listas principais separando os "Check-ins do dia" e "Check-outs do dia". Cada item da lista apresenta o nome do hóspede e o número do quarto.
* **Interações Principais:** Botões de ação direta (`Realizar check-in` e `Realizar check-out`) em cada card de hóspede.

#### Fluxos Secundários (Modais) originados nesta página:

* **Modal: Realizar Check-in:**
* *Informações:* Exibe a confirmação dos dados de entrada (Hóspede, Quarto, Saída prevista) e o cálculo do pagamento inicial (Valor da diária, Quantidade de dias e Total a pagar).
* *Interações:* Botão `Confirmar pagamento e realizar Check-in` e ícone de fechar (`X`).


* **Modal: Realizar Check-out:**
* *Informações:* Exibe os dados de saída e o acerto de contas final. Apresenta o status das diárias (ex: "Pago") e soma os consumos extras (Frigobar, Restaurante) para compor o Total a pagar final.
* *Interações:* Botão `Confirmar pagamento e realizar Check-out` e ícone de fechar (`X`).



---

### 2. Mapa de Reservas

Uma visão em formato de cronograma (timeline) para controle visual da ocupação do hotel.

* **Informações Exibidas:** Uma matriz em formato de tabela onde o eixo vertical lista os quartos (ex: 101, 102, 201) e o eixo horizontal representa os dias do mês. Blocos coloridos indicam o período de ocupação e o nome do hóspede alocado naquele quarto.
* **Interações Principais:** Botão primário `Nova Reserva`.

#### Fluxo Secundário: Criação de Nova Reserva (Wizard/Passo a Passo em Modal)

A ação de criar uma nova reserva é dividida em uma jornada de 4 etapas dentro de modais dinâmicos:

1. **Passo 1: Identificação do Hóspede**
* *Informações/Interações:* Campo de busca (Search) para encontrar hóspedes já cadastrados, lista de resultados (Nome, CPF, E-mail) selecionáveis via clique, e um botão `Cadastrar hóspede` caso seja um cliente novo.


2. **Passo 2: Selecione o quarto**
* *Informações/Interações:* Campo de busca por quartos e lista de opções disponíveis exibindo o Número do quarto, Categoria/Camas (ex: Suite Premium - 2 camas) e o valor da diária. O usuário seleciona o quarto clicando na linha correspondente.


3. **Passo 3: Período da estadia**
* *Informações/Interações:* Date pickers para definir a "Data de entrada" e "Data de saída". O sistema calcula dinamicamente e exibe o resumo de dias da estadia e o valor total previsto. Botão `Continuar`.


4. **Passo 4: Confirmação e Finalização**
* *Informações/Interações:* Tela de revisão contendo as "Informações da estadia" (Hóspede, Quarto, Saída) e "Informações de pagamento" (Diária, Dias, Total). Botão final `Finalizar reserva` para concluir o fluxo.



---

### 3. Hóspedes (Diretório de Clientes)

Área de gestão do banco de dados de clientes do hotel.

* **Informações Exibidas:** Uma lista (Data Table/List View) contendo o registro de todos os hóspedes. Cada linha apresenta Nome, CPF mascarado, E-mail e Data de nascimento.
* **Interações Principais:** Barra de pesquisa (`Busque por hóspedes`) para filtrar a lista e botão primário `Cadastrar hóspede`.

#### Fluxo Secundário originado nesta página:

* **Modal: Novo Hóspede**
* *Informações/Interações:* Formulário de cadastro contendo campos de texto (Inputs) para `Nome completo`, `CPF`, `Data de nascimento` (com ícone de calendário) e `E-mail`. Botão `Cadastrar hóspede` para salvar o registro no sistema. *(Nota: Este mesmo modal pode ser acionado durante o Passo 1 do fluxo de Nova Reserva).*