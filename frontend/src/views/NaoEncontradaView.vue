<script setup lang="ts">
import EmptyState from '@/components/EmptyState.vue'
</script>

<template>
  <EmptyState titulo="Página não encontrada" texto="O endereço que você acessou não existe.">
    <RouterLink to="/" class="btn btn-primary">Voltar ao início</RouterLink>
  </EmptyState>
</template>
