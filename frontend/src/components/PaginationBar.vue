<script setup lang="ts">
defineProps<{ pagina: number; total: number; desabilitado?: boolean }>()
defineEmits<{ ir: [pagina: number] }>()
</script>

<template>
  <nav v-if="total > 1" class="paginacao" aria-label="Paginação">
    <button class="btn" type="button" :disabled="desabilitado || pagina <= 1" @click="$emit('ir', pagina - 1)">
      Anterior
    </button>
    <span class="status" aria-live="polite">Página {{ pagina }} de {{ total }}</span>
    <button class="btn" type="button" :disabled="desabilitado || pagina >= total" @click="$emit('ir', pagina + 1)">
      Próxima
    </button>
  </nav>
</template>

<style scoped>
.paginacao {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-top: 40px;
}

.status {
  color: var(--text-muted);
  font-size: 0.9rem;
}
</style>
