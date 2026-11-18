import { http, limparCsrf } from './http'
import type {
  FavoritoDto,
  Filme,
  Pagina,
  TmdbFilmeDto,
  TmdbPaginaDto,
  Usuario,
} from '@/types'

const paraFilme = (f: TmdbFilmeDto): Filme => ({
  id: f.id,
  titulo: f.title,
  sinopse: f.overview ?? '',
  dataLancamento: f.release_date || null,
  posterPath: f.poster_path,
  nota: f.vote_average,
})

const favoritoParaFilme = (f: FavoritoDto): Filme => ({
  id: f.id,
  titulo: f.titulo,
  sinopse: f.sinopse ?? '',
  dataLancamento: f.dataLancamento,
  posterPath: f.posterPath,
  nota: null,
})

const paraPagina = (p: TmdbPaginaDto): Pagina<Filme> => ({
  pagina: p.page,
  // A API do TMDB limita a navegação a 500 páginas.
  totalPaginas: Math.min(p.total_pages, 500),
  totalResultados: p.total_results,
  itens: p.results.map(paraFilme),
})

export const filmesApi = {
  populares: async (pagina: number) =>
    paraPagina(await http<TmdbPaginaDto>(`/api/filmes/populares?pagina=${pagina}`)),

  buscar: async (consulta: string, pagina: number) =>
    paraPagina(
      await http<TmdbPaginaDto>(
        `/api/filmes/buscar?consulta=${encodeURIComponent(consulta)}&pagina=${pagina}`,
      ),
    ),

  detalhar: async (id: number) => paraFilme(await http<TmdbFilmeDto>(`/api/filmes/${id}`)),
}

export const favoritosApi = {
  listar: async () => (await http<FavoritoDto[]>('/api/favoritos')).map(favoritoParaFilme),
  adicionar: (id: number) => http<FavoritoDto>(`/api/favoritos/${id}`, { metodo: 'POST' }),
  remover: (id: number) => http<void>(`/api/favoritos/${id}`, { metodo: 'DELETE' }),
}

export const authApi = {
  perfil: () => http<Usuario>('/api/usuarios/me'),

  cadastrar: (email: string, senha: string) =>
    http<Usuario>('/api/usuarios', { metodo: 'POST', corpo: { email, senha } }),

  async entrar(email: string, senha: string) {
    await http<void>('/api/auth/login', { metodo: 'POST', corpo: { email, senha } })
    limparCsrf() // a sessão mudou: um novo token é buscado na próxima escrita
  },

  async sair() {
    await http<void>('/api/auth/logout', { metodo: 'POST' })
    limparCsrf()
  },
}
