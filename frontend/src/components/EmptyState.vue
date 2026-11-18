<script setup lang="ts">
defineProps<{ titulo: string; texto?: string }>()
</script>

<template>
  <div class="vazio">
    <h2>{{ titulo }}</h2>
    <p v-if="texto">{{ texto }}</p>
    <div class="acao"><slot /></div>
  </div>
</template>

<style scoped>
.vazio {
  display: grid;
  justify-items: center;
  gap: 8px;
  padding: 72px 16px;
  text-align: center;
}

h2 {
  font-size: 1.15rem;
}

p {
  max-width: 420px;
  color: var(--text-muted);
}

.acao {
  margin-top: 12px;
}
</style>
