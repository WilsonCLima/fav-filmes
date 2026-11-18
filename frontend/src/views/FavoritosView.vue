<script setup lang="ts">
import MovieGrid from '@/components/MovieGrid.vue'
import EmptyState from '@/components/EmptyState.vue'
import { useFavoritos } from '@/stores/favoritos'

const favoritos = useFavoritos()
</script>

<template>
  <section class="page">
    <header class="topo">
      <h1 class="page-title">Meus favoritos</h1>
      <p class="page-subtitle" v-if="!favoritos.carregando.value && favoritos.lista.value.length">
        {{ favoritos.lista.value.length }}
        {{ favoritos.lista.value.length === 1 ? 'filme salvo' : 'filmes salvos' }}
      </p>
    </header>

    <p v-if="favoritos.erro.value" class="alert" role="alert">{{ favoritos.erro.value }}</p>

    <EmptyState
      v-else-if="!favoritos.carregando.value && favoritos.lista.value.length === 0"
      titulo="Você ainda não favoritou nenhum filme"
      texto="Explore os filmes em alta e toque no coração para salvar aqui."
    >
      <RouterLink to="/" class="btn btn-primary">Explorar filmes</RouterLink>
    </EmptyState>

    <MovieGrid v-else :filmes="favoritos.lista.value" :carregando="favoritos.carregando.value" />
  </section>
</template>

<style scoped>
.topo {
  margin-bottom: 32px;
}
</style>
