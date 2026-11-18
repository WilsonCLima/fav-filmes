import { computed, ref } from 'vue'
import { authApi } from '@/services/api'
import { ApiError } from '@/services/http'
import type { Usuario } from '@/types'

const usuario = ref<Usuario | null>(null)
const carregado = ref(false)

export function useAuth() {
  const logado = computed(() => usuario.value !== null)

  /** Descobre se já existe sessão ativa. Chamado uma vez ao iniciar o app. */
  async function iniciar() {
    if (carregado.value) return
    try {
      usuario.value = await authApi.perfil()
    } catch (e) {
      if (!(e instanceof ApiError) || e.status !== 401) console.error(e)
      usuario.value = null
    } finally {
      carregado.value = true
    }
  }

  async function entrar(email: string, senha: string) {
    await authApi.entrar(email, senha)
    usuario.value = await authApi.perfil()
  }

  async function cadastrar(email: string, senha: string) {
    await authApi.cadastrar(email, senha)
    await entrar(email, senha)
  }

  async function sair() {
    try {
      await authApi.sair()
    } finally {
      usuario.value = null
    }
  }

  return { usuario, logado, carregado, iniciar, entrar, cadastrar, sair }
}
