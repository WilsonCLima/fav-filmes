<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import AuthForm from '@/components/AuthForm.vue'
import { useAuth } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const { entrar } = useAuth()

async function enviar(email: string, senha: string) {
  await entrar(email, senha)
  const destino = String(route.query.redirecionar ?? '')
  // Só aceita caminhos internos, evitando redirecionamento para outro site.
  await router.push(destino.startsWith('/') && !destino.startsWith('//') ? destino : '/')
}
</script>

<template>
  <AuthForm titulo="Entrar" subtitulo="Acesse sua conta para ver seus favoritos." botao="Entrar" :enviar="enviar">
    <template #rodape>
      Ainda não tem conta? <RouterLink to="/cadastro">Criar conta</RouterLink>
    </template>
  </AuthForm>
</template>
