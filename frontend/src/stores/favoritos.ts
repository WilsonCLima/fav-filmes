import { computed, ref, watch } from 'vue'
import { favoritosApi } from '@/services/api'
import { useAuth } from './auth'
import type { Filme } from '@/types'

const lista = ref<Filme[]>([])
const carregando = ref(false)
const erro = ref('')
const pendentes = ref<Set<number>>(new Set())
let observando = false

export function useFavoritos() {
  const { logado } = useAuth()
  const ids = computed(() => new Set(lista.value.map((f) => f.id)))

  async function carregar() {
    if (!logado.value) {
      lista.value = []
      return
    }
    carregando.value = true
    erro.value = ''
    try {
      lista.value = await favoritosApi.listar()
    } catch (e) {
      erro.value = e instanceof Error ? e.message : 'Falha ao carregar favoritos.'
    } finally {
      carregando.value = false
    }
  }

  // Recarrega (ou limpa) a lista sempre que o usuário entra ou sai.
  if (!observando) {
    observando = true
    watch(logado, carregar, { immediate: true })
  }

  const eFavorito = (id: number) => ids.value.has(id)
  const estaPendente = (id: number) => pendentes.value.has(id)

  /** Alterna o favorito. Lança erro para a tela decidir como avisar. */
  async function alternar(filme: Filme) {
    if (pendentes.value.has(filme.id)) return
    pendentes.value = new Set(pendentes.value).add(filme.id)
    try {
      if (eFavorito(filme.id)) {
        await favoritosApi.remover(filme.id)
        lista.value = lista.value.filter((f) => f.id !== filme.id)
      } else {
        await favoritosApi.adicionar(filme.id)
        lista.value = [filme, ...lista.value]
      }
    } finally {
      const proximo = new Set(pendentes.value)
      proximo.delete(filme.id)
      pendentes.value = proximo
    }
  }

  return { lista, carregando, erro, carregar, eFavorito, estaPendente, alternar }
}
