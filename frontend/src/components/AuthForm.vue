<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ApiError } from '@/services/http'

const props = defineProps<{
  titulo: string
  subtitulo: string
  botao: string
  novaSenha?: boolean
  enviar: (email: string, senha: string) => Promise<void>
}>()

const form = reactive({ email: '', senha: '' })
const enviando = ref(false)
const erroGeral = ref('')
const erros = reactive<Record<string, string>>({})

const emailValido = computed(() => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email.trim()))

function validar(): boolean {
  erros.email = emailValido.value ? '' : 'Informe um e-mail válido.'
  erros.senha = !form.senha
    ? 'Informe a senha.'
    : props.novaSenha && form.senha.length < 8
      ? 'Use pelo menos 8 caracteres.'
      : ''
  return !erros.email && !erros.senha
}

async function aoEnviar() {
  erroGeral.value = ''
  if (!validar()) return
  enviando.value = true
  try {
    await props.enviar(form.email.trim(), form.senha)
  } catch (e) {
    if (e instanceof ApiError && Object.keys(e.campos).length) {
      erros.email = e.campos.email ?? ''
      erros.senha = e.campos.senha ?? ''
    } else {
      erroGeral.value = e instanceof Error ? e.message : 'Não foi possível concluir.'
    }
  } finally {
    enviando.value = false
  }
}
</script>

<template>
  <div class="wrap">
    <form class="cartao" novalidate @submit.prevent="aoEnviar">
      <h1>{{ titulo }}</h1>
      <p class="sub">{{ subtitulo }}</p>

      <p v-if="erroGeral" class="alert" role="alert">{{ erroGeral }}</p>

      <div class="field">
        <label for="email">E-mail</label>
        <input
          id="email"
          v-model="form.email"
          class="input"
          type="email"
          autocomplete="email"
          placeholder="voce@exemplo.com"
          :aria-invalid="!!erros.email"
          :aria-describedby="erros.email ? 'erro-email' : undefined"
        />
        <span v-if="erros.email" id="erro-email" class="field-error">{{ erros.email }}</span>
      </div>

      <div class="field">
        <label for="senha">Senha</label>
        <input
          id="senha"
          v-model="form.senha"
          class="input"
          type="password"
          :autocomplete="novaSenha ? 'new-password' : 'current-password'"
          :placeholder="novaSenha ? 'Mínimo de 8 caracteres' : 'Sua senha'"
          :aria-invalid="!!erros.senha"
          :aria-describedby="erros.senha ? 'erro-senha' : undefined"
        />
        <span v-if="erros.senha" id="erro-senha" class="field-error">{{ erros.senha }}</span>
      </div>

      <button class="btn btn-primary btn-block" type="submit" :disabled="enviando">
        {{ enviando ? 'Aguarde...' : botao }}
      </button>

      <p class="rodape"><slot name="rodape" /></p>
    </form>
  </div>
</template>

<style scoped>
.wrap {
  display: grid;
  place-items: start center;
  padding: 64px 0;
}

.cartao {
  display: grid;
  gap: 18px;
  width: min(100%, 400px);
  padding: 32px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow);
}

h1 {
  font-size: 1.5rem;
}

.sub {
  margin-top: -10px;
  color: var(--text-muted);
  font-size: 0.92rem;
}

.rodape {
  text-align: center;
  color: var(--text-muted);
  font-size: 0.9rem;
}

.rodape :deep(a) {
  color: var(--accent);
  font-weight: 600;
}
</style>
