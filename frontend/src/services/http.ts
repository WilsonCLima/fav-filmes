import type { ProblemaDto } from '@/types'

export class ApiError extends Error {
  readonly status: number
  readonly campos: Record<string, string>

  constructor(status: number, mensagem: string, campos: Record<string, string> = {}) {
    super(mensagem)
    this.status = status
    this.campos = campos
  }
}

let csrfToken: string | null = null

async function obterCsrf(forcar = false): Promise<string> {
  if (csrfToken && !forcar) return csrfToken
  const resposta = await fetch('/api/auth/csrf', { credentials: 'include' })
  if (!resposta.ok) throw new ApiError(resposta.status, 'Não foi possível iniciar a sessão segura.')
  const corpo = (await resposta.json()) as { token: string }
  csrfToken = corpo.token
  return csrfToken
}

/** Descarta o token em memória (use após login e logout). */
export function limparCsrf(): void {
  csrfToken = null
}

async function converterErro(resposta: Response): Promise<ApiError> {
  let problema: ProblemaDto = {}
  try {
    problema = (await resposta.json()) as ProblemaDto
  } catch {
    // corpo vazio ou que não é JSON
  }
  const campos: Record<string, string> = {}
  for (const c of problema.campos ?? []) campos[c.campo] = c.erro
  const padrao =
    resposta.status === 401
      ? 'Sua sessão expirou. Entre novamente.'
      : resposta.status >= 500
        ? 'Algo deu errado no servidor. Tente novamente.'
        : 'Não foi possível concluir a solicitação.'
  return new ApiError(resposta.status, problema.detail || padrao, campos)
}

export async function http<T>(caminho: string, opcoes: { metodo?: string; corpo?: unknown } = {}): Promise<T> {
  const metodo = opcoes.metodo ?? 'GET'
  const alteraEstado = metodo !== 'GET'

  const enviar = async (renovarCsrf: boolean) => {
    const headers: Record<string, string> = { Accept: 'application/json' }
    if (opcoes.corpo !== undefined) headers['Content-Type'] = 'application/json'
    if (alteraEstado) headers['X-XSRF-TOKEN'] = await obterCsrf(renovarCsrf)
    return fetch(caminho, {
      method: metodo,
      headers,
      credentials: 'include',
      body: opcoes.corpo !== undefined ? JSON.stringify(opcoes.corpo) : undefined,
    })
  }

  let resposta = await enviar(false)
  // 403 em operação de escrita costuma indicar token CSRF vencido: renova e tenta uma vez.
  if (resposta.status === 403 && alteraEstado) resposta = await enviar(true)

  if (!resposta.ok) throw await converterErro(resposta)
  if (resposta.status === 204) return undefined as T
  const texto = await resposta.text()
  return (texto ? JSON.parse(texto) : undefined) as T
}
