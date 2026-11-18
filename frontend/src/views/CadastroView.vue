<script setup lang="ts">
import { useRouter } from 'vue-router'
import AuthForm from '@/components/AuthForm.vue'
import { useAuth } from '@/stores/auth'
import { useToast } from '@/stores/toast'

const router = useRouter()
const { cadastrar } = useAuth()
const toast = useToast()

async function enviar(email: string, senha: string) {
  await cadastrar(email, senha)
  toast.mostrar('Conta criada. Bem-vindo!')
  await router.push('/')
}
</script>

<template>
  <AuthForm
    titulo="Criar conta"
    subtitulo="Salve seus filmes favoritos em um só lugar."
    botao="Criar conta"
    nova-senha
    :enviar="enviar"
  >
    <template #rodape>
      Já tem conta? <RouterLink to="/entrar">Entrar</RouterLink>
    </template>
  </AuthForm>
</template>
