/** Filme no formato usado pela interface, independente da origem (TMDB ou favoritos). */
export interface Filme {
  id: number
  titulo: string
  sinopse: string
  dataLancamento: string | null
  posterPath: string | null
  nota: number | null
}

export interface Pagina<T> {
  pagina: number
  totalPaginas: number
  totalResultados: number
  itens: T[]
}

export interface Usuario {
  id: number
  email: string
  role: string
  dataCriacao: string
}

// Formatos crus devolvidos pela API
export interface TmdbFilmeDto {
  id: number
  title: string
  overview: string | null
  release_date: string | null
  poster_path: string | null
  vote_average: number | null
}

export interface TmdbPaginaDto {
  page: number
  results: TmdbFilmeDto[]
  total_pages: number
  total_results: number
}

export interface FavoritoDto {
  id: number
  titulo: string
  sinopse: string | null
  dataLancamento: string | null
  posterPath: string | null
  favoritedAt: string
}

export interface ProblemaDto {
  detail?: string
  campos?: { campo: string; erro: string }[]
}
