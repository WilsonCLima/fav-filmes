<script setup lang="ts">
import MovieCard from './MovieCard.vue'
import { useFavoritos } from '@/stores/favoritos'
import { useAuth } from '@/stores/auth'
import { useToast } from '@/stores/toast'
import { useRouter, useRoute } from 'vue-router'
import type { Filme } from '@/types'

defineProps<{ filmes: Filme[]; carregando?: boolean }>()

const router = useRouter()
const route = useRoute()
const { logado } = useAuth()
const favoritos = useFavoritos()
const toast = useToast()

async function alternar(filme: Filme) {
  if (!logado.value) {
    toast.mostrar('Entre na sua conta para favoritar filmes.')
    await router.push({ name: 'entrar', query: { redirecionar: route.fullPath } })
    return
  }
  try {
    await favoritos.alternar(filme)
  } catch (e) {
    toast.erro(e instanceof Error ? e.message : 'Não foi possível atualizar o favorito.')
  }
}
</script>

<template>
  <div v-if="carregando" class="grid" aria-busy="true" aria-label="Carregando filmes">
    <div v-for="n in 12" :key="n" class="esqueleto">
      <div class="skeleton poster" />
      <div class="skeleton linha" />
    </div>
  </div>
  <ul v-else class="grid">
    <li v-for="filme in filmes" :key="filme.id">
      <MovieCard
        :filme="filme"
        :favorito="favoritos.eFavorito(filme.id)"
        :carregando="favoritos.estaPendente(filme.id)"
        @alternar="alternar(filme)"
      />
    </li>
  </ul>
</template>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(168px, 1fr));
  gap: 28px 20px;
  margin: 0;
  padding: 0;
  list-style: none;
}

.poster {
  aspect-ratio: 2 / 3;
}

.linha {
  height: 14px;
  width: 70%;
  margin-top: 12px;
  border-radius: 6px;
}

@media (max-width: 480px) {
  .grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 24px 14px;
  }
}
</style>
