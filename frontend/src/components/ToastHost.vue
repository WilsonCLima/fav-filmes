<script setup lang="ts">
import { useToast } from '@/stores/toast'

const { avisos, fechar } = useToast()
</script>

<template>
  <div class="host" role="status" aria-live="polite">
    <TransitionGroup name="toast">
      <div v-for="aviso in avisos" :key="aviso.id" class="toast" :class="aviso.tipo">
        <span>{{ aviso.texto }}</span>
        <button type="button" class="fechar" aria-label="Fechar aviso" @click="fechar(aviso.id)">×</button>
      </div>
    </TransitionGroup>
  </div>
</template>

<style scoped>
.host {
  position: fixed;
  right: 20px;
  bottom: 20px;
  z-index: 50;
  display: grid;
  gap: 8px;
  max-width: min(360px, calc(100vw - 40px));
}

.toast {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px 14px;
  background: var(--text);
  color: var(--bg);
  border-radius: var(--radius-sm);
  box-shadow: var(--shadow-hover);
  font-size: 0.9rem;
}

.toast.erro {
  background: var(--danger);
  color: #fff;
}

.fechar {
  border: 0;
  background: transparent;
  color: inherit;
  font-size: 1.2rem;
  line-height: 1;
  cursor: pointer;
  opacity: 0.8;
}

.toast-enter-active,
.toast-leave-active {
  transition: opacity 0.2s var(--ease), transform 0.2s var(--ease);
}

.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(8px);
}
</style>
