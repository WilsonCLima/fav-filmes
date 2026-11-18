# Fav Filmes: Frontend

Interface web do Fav Filmes, feita com **Vue 3**, **TypeScript** e **Vite**. Permite explorar filmes populares, buscar por título, ver detalhes e salvar favoritos por usuário.

## Funcionalidades

- Filmes em alta, busca por título e paginação (busca e página ficam na URL).
- Página de detalhes com sinopse, data e nota.
- Cadastro, login e logout com sessão HTTP e proteção CSRF.
- Favoritar e desfavoritar com atualização imediata; lista pessoal em `/favoritos`.
- Tema claro e escuro automático (segue o sistema), layout responsivo e acessível (foco visível, rótulos, `aria-live`).

## Pré-requisitos

| Ferramenta | Versão |
|---|---|
| Node.js | 20.19 ou superior (testado com 24) |
| npm | 10 ou superior |
| Java | 21 ou superior (para o backend) |
| PostgreSQL | 17 (via Docker ou instalação local) |
| Chave da API do TMDB | gratuita, em https://www.themoviedb.org/settings/api |

O front depende do backend e do banco rodando. Siga os passos na ordem.

## 1. Banco de dados

Na raiz do projeto (pasta acima de `frontend`), com o Docker instalado:

```bash
docker compose up -d db
```

Isso cria o PostgreSQL em `localhost:5433`, banco `fav_filmes`, usuário `postgres` e senha `postgres` (ou o valor de `DB_PASSWORD`). As tabelas são criadas automaticamente pelo Flyway na primeira execução do backend.

Sem Docker, use um PostgreSQL local: crie o banco `fav_filmes` e informe a conexão pelas variáveis `DB_URL`, `DB_USER` e `DB_PASSWORD` no passo seguinte.

## 2. Backend

Na raiz do projeto, defina a chave do TMDB e inicie a API. O `mvnw` não lê o arquivo `.env`, então a variável precisa estar no ambiente do terminal.

PowerShell (Windows):

```powershell
$env:TMDB_API_KEY = "sua_chave_aqui"
.\mvnw spring-boot:run
```

Bash (Linux, macOS ou Git Bash):

```bash
export TMDB_API_KEY="sua_chave_aqui"
./mvnw spring-boot:run
```

Variáveis opcionais, com os padrões já apontando para o banco do passo 1:

| Variável | Padrão |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5433/fav_filmes` |
| `DB_USER` | `postgres` |
| `DB_PASSWORD` | `postgres` |
| `FRONTEND_ORIGIN` | `http://localhost:5173` |

A API fica em `http://localhost:8080`. Sem `TMDB_API_KEY`, o login e o cadastro funcionam, mas a listagem de filmes responde 503 e a tela mostra "TMDB_API_KEY não configurada".

## 3. Frontend

Em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

Abra http://localhost:5173.

O servidor de desenvolvimento encaminha as chamadas `/api` para `http://localhost:8080` (proxy do Vite). Assim, front e API ficam na mesma origem e os cookies de sessão e CSRF funcionam sem configuração extra. Se o backend usar outro endereço, crie `frontend/.env` com:

```
VITE_API_PROXY_TARGET=http://localhost:8080
```

## Scripts

| Comando | O que faz |
|---|---|
| `npm run dev` | Servidor de desenvolvimento com recarga automática |
| `npm run typecheck` | Verifica os tipos TypeScript |
| `npm run build` | Verifica os tipos e gera a versão de produção em `dist/` |
| `npm run preview` | Serve a versão de produção localmente |

## Estrutura

```
frontend/src
├── components/   Peças reutilizáveis (cabeçalho, card, grade, formulário, avisos)
├── views/        Telas: início, filme, favoritos, entrar, cadastro
├── router/       Rotas e guardas (rotas privadas exigem login)
├── services/     http.ts (fetch, sessão e CSRF) e api.ts (chamadas e conversão de dados)
├── stores/       Estado compartilhado: auth, favoritos e avisos
├── styles/       Tokens de design e estilos globais
├── types/        Tipos TypeScript dos dados da API
└── utils/        Formatação de datas e imagens
```

## Design

- Minimalista: fundo neutro, uma única cor de destaque, bordas finas e sombras suaves.
- Tokens em variáveis CSS (`src/styles/main.css`): cores, raios, sombras e espaçamento em múltiplos de 4px.
- Tipografia do sistema (sem fontes externas), grade de pôsteres fluida e esqueletos de carregamento.
- Respeita `prefers-color-scheme` e `prefers-reduced-motion`.

## Como a segurança funciona

- O login cria uma sessão HTTP (`JSESSIONID`, cookie HttpOnly). O front nunca guarda senha ou token de sessão.
- Operações que alteram dados (POST e DELETE) enviam o token CSRF no header `X-XSRF-TOKEN`. O token vem de `GET /api/auth/csrf` e é renovado após login e logout.
- A chave do TMDB fica somente no backend.
- O redirecionamento pós-login só aceita caminhos internos do próprio site.

## Problemas comuns

| Sintoma | Causa provável |
|---|---|
| Tela de filmes com "TMDB_API_KEY não configurada" | A variável não foi definida no terminal do backend |
| Erros de rede ou 502 nas chamadas `/api` | Backend fora do ar ou em outra porta (ajuste `VITE_API_PROXY_TARGET`) |
| Backend não inicia com "Connection refused" | Banco parado: rode `docker compose up -d db` |
| Porta 5173 ocupada | Rode `npm run dev -- --port 5174` e ajuste `FRONTEND_ORIGIN` no backend |
| Pôsteres não aparecem | Sem acesso a `image.tmdb.org` (rede ou bloqueio) |

## Dados de teste

Crie contas pela tela de cadastro, com e-mails fictícios (por exemplo `usuario1@exemplo.com`) e senha de pelo menos 8 caracteres. As contas ficam no volume `postgres_data` do Docker; `docker compose down -v` apaga o banco e elas precisam ser recriadas. Não use dados reais de pessoas.
