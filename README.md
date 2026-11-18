# Fav Filmes

[![CI](https://github.com/WilsonCLima/fav-filmes/actions/workflows/ci.yml/badge.svg)](https://github.com/WilsonCLima/fav-filmes/actions/workflows/ci.yml)
[![Licença: MIT](https://img.shields.io/badge/licen%C3%A7a-MIT-blue.svg)](LICENSE)

Aplicação web para descobrir filmes e salvar favoritos. O catálogo vem da API do [TMDB](https://www.themoviedb.org/) e cada usuário mantém a própria lista.

## Stack

| Camada | Tecnologias |
|---|---|
| Backend | Java 21, Spring Boot 4, Spring Security (sessão HTTP + CSRF), Spring Data JPA, Flyway |
| Banco | PostgreSQL 17 |
| Frontend | Vue 3, TypeScript, Vite, Vue Router |
| Infra | Docker Compose, GitHub Actions |

## Funcionalidades

- Cadastro com senha BCrypt e e-mail único; login e logout por sessão (`JSESSIONID`, HttpOnly).
- Filmes populares, busca por título e detalhes em português (TMDB).
- Favoritar e desfavoritar por usuário, com lista pessoal.
- Validação de entrada, erros no formato `ProblemDetail`, CORS configurável e proteção CSRF.
- Interface minimalista, responsiva, com tema claro e escuro automático.

## Estrutura

```
.
├── src/                 Backend (Spring Boot)
│   └── main/resources/db/migration   Migrations Flyway
├── frontend/            Interface Vue + TypeScript
├── docker-compose.yaml  PostgreSQL e backend
└── .github/workflows    Integração contínua
```

## Como executar

Pré-requisitos: Docker, Java 21+, Node.js 20.19+ e uma chave gratuita da API do TMDB (https://www.themoviedb.org/settings/api). Use a **chave v3** (32 caracteres).

1. **Banco de dados** (porta 5433 no host):

   ```bash
   docker compose up -d db
   ```

2. **Backend** (http://localhost:8080). O `mvnw` não lê o arquivo `.env`; defina a chave no terminal.

   PowerShell:

   ```powershell
   $env:TMDB_API_KEY = "sua_chave_v3"
   .\mvnw spring-boot:run
   ```

   Bash:

   ```bash
   export TMDB_API_KEY="sua_chave_v3"
   ./mvnw spring-boot:run
   ```

3. **Frontend** (http://localhost:5173):

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

Alternativa para subir banco e API juntos em containers: copie `.env.example` para `.env`, informe `TMDB_API_KEY` e rode `docker compose up --build`.

Mais detalhes do frontend, variáveis e solução de problemas em [frontend/README.md](frontend/README.md).

### Variáveis do backend

| Variável | Padrão | Descrição |
|---|---|---|
| `TMDB_API_KEY` | vazio | Chave v3 do TMDB (obrigatória para listar filmes) |
| `DB_URL` | `jdbc:postgresql://localhost:5433/fav_filmes` | Conexão JDBC |
| `DB_USER` | `postgres` | Usuário do banco |
| `DB_PASSWORD` | `postgres` | Senha do banco (troque fora do ambiente local) |
| `FRONTEND_ORIGIN` | `http://localhost:5173` | Origem liberada no CORS |
| `SESSION_COOKIE_SECURE` | `false` | Use `true` com HTTPS |

> As migrations do Flyway rodam na primeira inicialização e o Hibernate apenas valida o esquema (`ddl-auto: validate`). `docker compose down -v` **apaga os dados locais**.

## Testes

```bash
./mvnw test                  # backend
cd frontend && npm run build # tipos e build do frontend
```

Ambos rodam automaticamente no GitHub Actions a cada push e pull request.

## API

| Método | Rota | Acesso |
|---|---|---|
| GET | `/api/auth/csrf` | Público |
| POST | `/api/usuarios` | Público (+CSRF) |
| POST | `/api/auth/login` | Público (+CSRF) |
| POST | `/api/auth/logout` | Autenticado (+CSRF) |
| GET | `/api/usuarios/me` | Autenticado |
| GET | `/api/filmes/populares?pagina=1` | Público |
| GET | `/api/filmes/buscar?consulta=matrix&pagina=1` | Público |
| GET | `/api/filmes/{tmdbId}` | Público |
| GET | `/api/favoritos` | Autenticado |
| POST | `/api/favoritos/{tmdbId}` | Autenticado (+CSRF) |
| DELETE | `/api/favoritos/{tmdbId}` | Autenticado (+CSRF) |

Exemplo com `curl` (sessão e CSRF):

```bash
curl -c cookies.txt http://localhost:8080/api/auth/csrf
TOKEN='COLE_O_TOKEN_RETORNADO'
curl -b cookies.txt -c cookies.txt -H "Content-Type: application/json" -H "X-XSRF-TOKEN: $TOKEN" \
  -d '{"email":"teste@exemplo.com","senha":"12345678"}' http://localhost:8080/api/usuarios
curl -b cookies.txt -c cookies.txt -H "Content-Type: application/json" -H "X-XSRF-TOKEN: $TOKEN" \
  -d '{"email":"teste@exemplo.com","senha":"12345678"}' http://localhost:8080/api/auth/login
curl -b cookies.txt http://localhost:8080/api/usuarios/me
```

Depois do login, peça `/api/auth/csrf` de novo e use o novo token nas operações de escrita.

## Segurança

- A chave do TMDB fica só no backend e nunca é enviada ao navegador nem versionada (`.env` está no `.gitignore`).
- Para produção: HTTPS, `SESSION_COOKIE_SECURE=true`, senha do banco forte e fora do código, limites de requisição e observabilidade.

## Licença

Distribuído sob a licença MIT. Veja [LICENSE](LICENSE).

Este produto usa a API do TMDB, mas não é endossado nem certificado pelo TMDB.
