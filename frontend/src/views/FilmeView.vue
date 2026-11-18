<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import FavoriteButton from '@/components/FavoriteButton.vue'
import { filmesApi } from '@/services/api'
import { useAuth } from '@/stores/auth'
import { useFavoritos } from '@/stores/favoritos'
import { useToast } from '@/stores/toast'
import { dataLonga, imagemUrl } from '@/utils/formatar'
import type { Filme } from '@/types'

const props = defineProps<{ id: number }>()

const route = useRoute()
const router = useRouter()
const { logado } = useAuth()
const favoritos = useFavoritos()
const toast = useToast()

const filme = ref<Filme | null>(null)
const carregando = ref(true)
const erro = ref('')

async function carregar() {
  carregando.value = true
  erro.value = ''
  filme.value = null
  try {
    filme.value = await filmesApi.detalhar(props.id)
  } catch (e) {
    erro.value = e instanceof Error ? e.message : 'Falha ao carregar o filme.'
  } finally {
    carregando.value = false
  }
}

watch(() => props.id, carregar, { immediate: true })

const poster = computed(() => imagemUrl(filme.value?.posterPath ?? null, 'w500'))

async function alternar() {
  if (!filme.value) return
  if (!logado.value) {
    toast.mostrar('Entre na sua conta para favoritar filmes.')
    await router.push({ name: 'entrar', query: { redirecionar: route.fullPath } })
    return
  }
  try {
    await favoritos.alternar(filme.value)
  } catch (e) {
    toast.erro(e instanceof Error ? e.message : 'Não foi possível atualizar o favorito.')
  }
}
</script>

<template>
  <section class="page">
    <button class="voltar" type="button" @click="router.back()">← Voltar</button>

    <div v-if="carregando" class="detalhe" aria-busy="true">
      <div class="skeleton capa" />
      <div class="textos">
        <div class="skeleton l1" />
        <div class="skeleton l2" />
        <div class="skeleton l3" />
      </div>
    </div>

    <p v-else-if="erro" class="alert" role="alert">{{ erro }}</p>

    <article v-else-if="filme" class="detalhe">
      <div class="capa">
        <img v-if="poster" :src="poster" :alt="`Pôster de ${filme.titulo}`" />
        <div v-else class="sem-poster">Sem imagem</div>
      </div>

      <div class="textos">
        <h1 class="page-title">{{ filme.titulo }}</h1>
        <p class="meta">
          <span v-if="filme.dataLancamento">{{ dataLonga(filme.dataLancamento) }}</span>
          <span v-if="filme.nota" class="nota">★ {{ filme.nota.toFixed(1) }} / 10</span>
        </p>

        <h2 class="secao">Sinopse</h2>
        <p class="sinopse">{{ filme.sinopse || 'Sinopse não disponível em português.' }}</p>

        <div class="acao">
          <FavoriteButton
            rotulo
            :ativo="favoritos.eFavorito(filme.id)"
            :carregando="favoritos.estaPendente(filme.id)"
            @alternar="alternar"
          />
        </div>
      </div>
    </article>
  </section>
</template>

<style scoped>
.voltar {
  margin-bottom: 24px;
  padding: 6px 0;
  border: 0;
  background: transparent;
  color: var(--text-muted);
  font-size: 0.9rem;
  cursor: pointer;
}

.voltar:hover {
  color: var(--text);
}

.detalhe {
  display: grid;
  grid-template-columns: minmax(200px, 320px) 1fr;
  gap: 40px;
  align-items: start;
}

.capa {
  aspect-ratio: 2 / 3;
  overflow: hidden;
  border-radius: var(--radius-lg);
  background: var(--surface-2);
  box-shadow: var(--shadow-hover);
}

.capa img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.sem-poster {
  display: grid;
  place-items: center;
  height: 100%;
  color: var(--text-muted);
}

.meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-top: 8px;
  color: var(--text-muted);
}

.nota {
  color: var(--text);
  font-weight: 600;
}

.secao {
  margin-top: 28px;
  font-size: 0.8rem;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--text-muted);
}

.sinopse {
  margin-top: 8px;
  max-width: 62ch;
  line-height: 1.7;
}

.acao {
  margin-top: 32px;
}

.l1, .l2, .l3 {
  height: 18px;
  margin-bottom: 14px;
  border-radius: 6px;
}
.l1 { width: 60%; height: 32px; }
.l2 { width: 90%; }
.l3 { width: 75%; }

@media (max-width: 720px) {
  .detalhe {
    grid-template-columns: 1fr;
    gap: 24px;
  }
  .capa {
    max-width: 260px;
  }
}
</style>
