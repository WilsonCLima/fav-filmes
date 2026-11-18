<script setup lang="ts">
defineProps<{
  ativo: boolean
  carregando?: boolean
  rotulo?: boolean
}>()

defineEmits<{ alternar: [] }>()
</script>

<template>
  <button
    type="button"
    class="fav"
    :class="{ ativo, comRotulo: rotulo }"
    :aria-pressed="ativo"
    :aria-label="ativo ? 'Remover dos favoritos' : 'Adicionar aos favoritos'"
    :disabled="carregando"
    @click.prevent.stop="$emit('alternar')"
  >
    <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true">
      <path
        d="M12 20.5s-8-5-8-11A4.6 4.6 0 0 1 8.6 5 4.5 4.5 0 0 1 12 6.6 4.5 4.5 0 0 1 15.4 5 4.6 4.6 0 0 1 20 9.5c0 6-8 11-8 11z"
        :fill="ativo ? 'currentColor' : 'none'"
        stroke="currentColor"
        stroke-width="1.8"
        stroke-linejoin="round"
      />
    </svg>
    <span v-if="rotulo">{{ ativo ? 'Nos favoritos' : 'Favoritar' }}</span>
  </button>
</template>

<style scoped>
.fav {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 36px;
  height: 36px;
  padding: 0;
  border: 0;
  border-radius: 999px;
  background: color-mix(in srgb, var(--surface) 88%, transparent);
  backdrop-filter: blur(6px);
  color: var(--text);
  cursor: pointer;
  box-shadow: 0 1px 3px rgb(0 0 0 / 0.18);
  transition: transform 0.15s var(--ease), color 0.15s var(--ease);
}

.fav:hover:not(:disabled) {
  transform: scale(1.08);
}

.fav.ativo {
  color: var(--heart);
}

.fav:disabled {
  opacity: 0.6;
  cursor: progress;
}

.fav.comRotulo {
  width: auto;
  height: 44px;
  padding: 0 18px;
  border: 1px solid var(--border);
  background: var(--surface);
  box-shadow: none;
  font-weight: 600;
  font-size: 0.92rem;
}
</style>
