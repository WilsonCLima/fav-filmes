<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MovieGrid from '@/components/MovieGrid.vue'
import PaginationBar from '@/components/PaginationBar.vue'
import EmptyState from '@/components/EmptyState.vue'
import { filmesApi } from '@/services/api'
import type { Filme, Pagina } from '@/types'

const route = useRoute()
const router = useRouter()

// A busca e a página vivem na URL: link compartilhável e botão voltar funcionando.
const consulta = computed(() => String(route.query.q ?? '').trim())
const pagina = computed(() => Math.max(1, Number(route.query.p) || 1))
const campo = ref(consulta.value)

const dados = ref<Pagina<Filme> | null>(null)
const carregando = ref(true)
const erro = ref('')
let requisicao = 0

async function carregar() {
  const atual = ++requisicao
  carregando.value = true
  erro.value = ''
  try {
    const resultado = consulta.value
      ? await filmesApi.buscar(consulta.value, pagina.value)
      : await filmesApi.populares(pagina.value)
    if (atual === requisicao) dados.value = resultado
  } catch (e) {
    if (atual === requisicao) erro.value = e instanceof Error ? e.message : 'Falha ao carregar filmes.'
  } finally {
    if (atual === requisicao) carregando.value = false
  }
}

watch([consulta, pagina], carregar, { immediate: true })
watch(consulta, (valor) => (campo.value = valor))

function buscar() {
  const termo = campo.value.trim()
  router.push({ name: 'inicio', query: termo ? { q: termo } : {} })
}

function limpar() {
  campo.value = ''
  router.push({ name: 'inicio' })
}

function irPara(p: number) {
  router.push({ name: 'inicio', query: { ...(consulta.value ? { q: consulta.value } : {}), p } })
}

const titulo = computed(() => (consulta.value ? `Resultados para "${consulta.value}"` : 'Em alta agora'))
</script>

<template>
  <section class="page">
    <header class="topo">
      <div>
        <h1 class="page-title">{{ titulo }}</h1>
        <p class="page-subtitle" v-if="!consulta">Os filmes mais populares do momento.</p>
        <p class="page-subtitle" v-else-if="dados">{{ dados.totalResultados }} resultados encontrados.</p>
      </div>

      <form class="busca" role="search" @submit.prevent="buscar">
        <label for="busca" class="sr-only">Buscar filmes por título</label>
        <input id="busca" v-model="campo" class="input" type="search" placeholder="Buscar por título" autocomplete="off" />
        <button class="btn btn-primary" type="submit">Buscar</button>
        <button v-if="consulta" class="btn" type="button" @click="limpar">Limpar</button>
      </form>
    </header>

    <p v-if="erro" class="alert" role="alert">
      {{ erro }} <button class="tentar" type="button" @click="carregar">Tentar novamente</button>
    </p>

    <template v-else>
      <EmptyState
        v-if="!carregando && dados && dados.itens.length === 0"
        titulo="Nenhum filme encontrado"
        texto="Tente outro título ou revise a grafia."
      />
      <MovieGrid v-else :filmes="dados?.itens ?? []" :carregando="carregando" />
      <PaginationBar :pagina="pagina" :total="dados?.totalPaginas ?? 0" :desabilitado="carregando" @ir="irPara" />
    </template>
  </section>
</template>

<style scoped>
.topo {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 32px;
}

.busca {
  display: flex;
  gap: 8px;
  width: min(100%, 440px);
}

.tentar {
  margin-left: 8px;
  border: 0;
  background: transparent;
  color: inherit;
  font-weight: 600;
  text-decoration: underline;
  cursor: pointer;
}
</style>
