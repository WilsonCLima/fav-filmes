<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useAuth } from '@/stores/auth'
import { useToast } from '@/stores/toast'

const router = useRouter()
const { logado, usuario, sair } = useAuth()
const toast = useToast()

async function aoSair() {
  try {
    await sair()
    await router.push({ name: 'inicio' })
  } catch (e) {
    toast.erro(e instanceof Error ? e.message : 'Não foi possível sair.')
  }
}
</script>

<template>
  <header class="header">
    <div class="container bar">
      <RouterLink to="/" class="brand" aria-label="Fav Filmes, página inicial">
        <svg viewBox="0 0 24 24" width="22" height="22" aria-hidden="true">
          <path
            fill="currentColor"
            d="M12 21s-8.5-5.3-8.5-11.2A4.8 4.8 0 0 1 8.3 5c1.5 0 2.8.7 3.7 1.9A4.6 4.6 0 0 1 15.7 5a4.8 4.8 0 0 1 4.8 4.8C20.5 15.7 12 21 12 21z"
          />
        </svg>
        <span>Fav Filmes</span>
      </RouterLink>

      <nav class="nav" aria-label="Principal">
        <RouterLink to="/" class="link">Filmes</RouterLink>
        <RouterLink v-if="logado" to="/favoritos" class="link">Favoritos</RouterLink>
      </nav>

      <div class="actions">
        <template v-if="logado">
          <span class="email" :title="usuario?.email">{{ usuario?.email }}</span>
          <button class="btn" type="button" @click="aoSair">Sair</button>
        </template>
        <template v-else>
          <RouterLink to="/entrar" class="btn">Entrar</RouterLink>
          <RouterLink to="/cadastro" class="btn btn-primary">Criar conta</RouterLink>
        </template>
      </div>
    </div>
  </header>
</template>

<style scoped>
.header {
  position: sticky;
  top: 0;
  z-index: 20;
  background: color-mix(in srgb, var(--bg) 82%, transparent);
  backdrop-filter: saturate(180%) blur(12px);
  border-bottom: 1px solid var(--border);
}

.bar {
  display: flex;
  align-items: center;
  gap: 24px;
  height: 64px;
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-weight: 700;
  letter-spacing: -0.02em;
  color: var(--accent);
}

.brand span {
  color: var(--text);
}

.nav {
  display: flex;
  gap: 4px;
  flex: 1;
}

.link {
  padding: 6px 12px;
  border-radius: var(--radius-sm);
  color: var(--text-muted);
  font-size: 0.92rem;
  font-weight: 500;
  transition: color 0.15s var(--ease), background 0.15s var(--ease);
}

.link:hover,
.link.router-link-exact-active {
  color: var(--text);
  background: var(--surface-2);
}

.actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.email {
  max-width: 180px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--text-muted);
  font-size: 0.85rem;
}

@media (max-width: 640px) {
  .bar {
    gap: 12px;
  }
  .email,
  .brand span {
    display: none;
  }
}
</style>
