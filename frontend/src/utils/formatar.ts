const TMDB_IMAGENS = 'https://image.tmdb.org/t/p'

export function imagemUrl(caminho: string | null, tamanho: 'w342' | 'w500' = 'w342'): string | null {
  return caminho ? `${TMDB_IMAGENS}/${tamanho}${caminho}` : null
}

export function anoDe(data: string | null): string {
  return data ? data.slice(0, 4) : ''
}

export function dataLonga(data: string | null): string {
  if (!data) return ''
  const [ano, mes, dia] = data.split('-').map(Number)
  return new Date(ano!, mes! - 1, dia).toLocaleDateString('pt-BR', {
    day: 'numeric',
    month: 'long',
    year: 'numeric',
  })
}
