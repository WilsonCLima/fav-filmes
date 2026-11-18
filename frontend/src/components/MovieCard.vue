<script setup lang="ts">
import { computed } from 'vue'
import FavoriteButton from './FavoriteButton.vue'
import { imagemUrl, anoDe } from '@/utils/formatar'
import type { Filme } from '@/types'

const props = defineProps<{
  filme: Filme
  favorito: boolean
  carregando?: boolean
}>()

defineEmits<{ alternar: [] }>()

const poster = computed(() => imagemUrl(props.filme.posterPath, 'w342'))
const ano = computed(() => anoDe(props.filme.dataLancamento))
</script>

<template>
  <article class="card">
    <RouterLink :to="{ name: 'filme', params: { id: filme.id } }" class="area" :aria-label="filme.titulo">
      <div class="poster">
        <img v-if="poster" :src="poster" :alt="`Pôster de ${filme.titulo}`" loading="lazy" />
        <div v-else class="sem-poster" aria-hidden="true">Sem imagem</div>
        <div class="fav-slot">
          <FavoriteButton :ativo="favorito" :carregando="carregando" @alternar="$emit('alternar')" />
        </div>
      </div>
      <div class="info">
        <h3 class="titulo">{{ filme.titulo }}</h3>
        <p class="meta">
          <span v-if="ano">{{ ano }}</span>
          <span v-if="filme.nota" class="nota">★ {{ filme.nota.toFixed(1) }}</span>
        </p>
      </div>
    </RouterLink>
  </article>
</template>

<style scoped>
.card {
  border-radius: var(--radius);
  transition: transform 0.2s var(--ease);
}

.card:hover {
  transform: translateY(-3px);
}

.area {
  display: block;
}

.poster {
  position: relative;
  aspect-ratio: 2 / 3;
  overflow: hidden;
  border-radius: var(--radius);
  background: var(--surface-2);
  box-shadow: var(--shadow);
  transition: box-shadow 0.2s var(--ease);
}

.card:hover .poster {
  box-shadow: var(--shadow-hover);
}

.poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.sem-poster {
  display: grid;
  place-items: center;
  height: 100%;
  color: var(--text-muted);
  font-size: 0.85rem;
}

.fav-slot {
  position: absolute;
  top: 10px;
  right: 10px;
}

.info {
  padding: 10px 2px 0;
}

.titulo {
  font-size: 0.95rem;
  font-weight: 600;
  letter-spacing: -0.01em;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.meta {
  display: flex;
  gap: 10px;
  margin-top: 2px;
  color: var(--text-muted);
  font-size: 0.82rem;
}

.nota {
  color: var(--text);
}
</style>
