# Banco Tranquilo Simulator — CP5 Fase 2B

API REST Java 17/Spring Boot independente que simula autorizacao de pagamentos de forma deterministica e idempotente.

## Endpoint e regra deterministica

`POST /api/v1/payments`

- `purchaseId` impar: `APPROVED` e `authorizationCode` deterministico a partir do `paymentAttemptId`;
- `purchaseId` par: `REJECTED`, com motivo `PURCHASE_ID_EVEN`;
- repetir o mesmo `paymentAttemptId` com o mesmo corpo devolve o mesmo resultado;
- repetir o mesmo `paymentAttemptId` com dados diferentes devolve `409 Conflict`;
- entrada incompleta ou invalida devolve `400 Bad Request` com detalhes.

Exemplo aprovado:

```json
{
  "paymentAttemptId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
  "purchaseId": 11,
  "correlationId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
  "amount": 39.90,
  "currency": "BRL"
}
```

O cache de idempotencia e local ao processo, apropriado ao simulador. Reiniciar a aplicacao limpa os resultados anteriores.

## Como executar

Clone este repositorio ao lado de `mercado-payment-service`. O RabbitMQ/Docker Compose e a colecao Postman ficam no repositorio irmao:

```powershell
git clone https://github.com/GusCrevelari/mercado-payment-service.git
git clone https://github.com/GusCrevelari/banco-tranquilo-simulator.git
cd banco-tranquilo-simulator
```

```powershell
.\mvnw.cmd spring-boot:run
```

- API: `http://localhost:8091/api/v1/payments`
- Health: `http://localhost:8091/actuator/health`
- Porta alternativa: defina `PORT` antes de iniciar.

Os corpos e respostas esperadas para os dois cenarios estao em `examples/`. A colecao Postman completa esta em `..\mercado-payment-service\postman\phase-2b.postman_collection.json`.

## Testes e build

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

Os testes cobrem aprovacao, rejeicao, duplicata idempotente, conflito, validacao HTTP e os dois formatos de resposta.
