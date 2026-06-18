### Contrato da API:

Gestão de Quartos:

| **Tela Correspondente** | **Método HTTP** | **Rota (Endpoint)** | **O que a API Recebe (Request)** | **O que a API Devolve (Response)** |
| --- | --- | --- | --- | --- |
| **Visualizar quartos** | `GET` | `/api/rooms` | Nada (Opcional: filtro `?status=AVAILABLE`) | Lista resumida com `id`, `room_number`e `status` (200 OK). |
| **Especificação do quarto** | `GET` | `/api/rooms/{id}` | O `id` do quarto na própria URL. | Detalhes completos do quarto e quem está nele (200 OK). |

```json
// Exemplo se ocupado:
{
  "id": 102,
  "number": "204B",
  "status": "OCCUPIED",
  "bed_count": 2,
  "current_guest": {
    "name": "Maria Silva",
    "checkout_date": "2026-06-05"
  }
}
// Exemplo se livre:
{
  "id": 102,
  "number": "204B",
  "status": "AVAILABLE",
  "bed_count": 2,
  "current_guest": null
}
```

Cadastro de clientes:

| **Tela Correspondente** | **Método HTTP** | **Rota (Endpoint)** | **O que a API Recebe (Request Body)** | **O que a API Devolve (Response)** |
| --- | --- | --- | --- | --- |
| Buscar Hóspedes | `GET` | `/api/guests` | Opcional: filtro `?name=Maria` | Lista de clientes encontrados. |
| Cadastrar cliente | `POST` | `/api/guests` | JSON com nome, cpf, email e birth_date. | O mesmo JSON salvo com o id gerado (201 Created). |

```json
{
  "name": "Carlos Almeida",
  "cpf": "111.222.333-44",
  "email": "carlos.almeida@email.com",
  "birth_date": "1995-03-15"
}
```

Fluxo de hospedagem e reserva:

| **Tela Correspondente** | **Método HTTP** | **Rota (Endpoint)** | **O que a API Recebe (Request Body)** | **O que a API Devolve (Response)** |
| --- | --- | --- | --- | --- |
| Criar Nova Reserva | `POST` | `/api/reservations` | IDs do cliente e quarto, datas e valor. | O recibo da reserva gerado (201 Created). |
| Fazer Check-in | `POST` | `/api/reservations/{id}/checkin` | O id da reserva na URL. | Status muda para IN_PROGRESS (200 OK). |
| Fazer Check-out | `POST` | `/api/reservations/{id}/checkout` | O id da reserva na URL. | Quarto volta a ficar livre (200 OK). |
| Buscar Reserva | `GET` | `/api/reservations` | Nada | Retorna Reservas encontradas |

```json
// JSON para criar a reserva
{
  "guest_id": 45,
  "room_id": 102,
  "checkin_date": "2026-06-10",
  "checkout_date": "2026-06-15",
  "total_amount": 750.00
},
// JSON de GET nas reservas
{
  "id": 1,
  "guest_id": 1,
  "room_id": 101,
  "checkin_date": "2026-06-07",
  "checkout_date": "2026-06-08",
  "total_amount": 0,
  "status": "string"
}
```