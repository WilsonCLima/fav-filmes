<script setup lang="ts">
import AppHeader from '@/components/AppHeader.vue'
import ToastHost from '@/components/ToastHost.vue'
</script>

<template>
  <a class="skip" href="#conteudo">Ir para o conteúdo</a>
  <AppHeader />
  <main id="conteudo" class="container">
    <RouterView />
  </main>
  <ToastHost />
</template>

<style scoped>
.skip {
  position: absolute;
  left: -999px;
  top: 8px;
  z-index: 100;
  padding: 8px 12px;
  background: var(--accent);
  color: var(--accent-contrast);
  border-radius: var(--radius-sm);
}

.skip:focus {
  left: 8px;
}
</style>
