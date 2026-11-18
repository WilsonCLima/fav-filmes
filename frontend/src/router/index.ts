import { createRouter, createWebHistory } from 'vue-router'
import { useAuth } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior: (_to, _from, salvo) => salvo ?? { top: 0 },
  routes: [
    { path: '/', name: 'inicio', component: () => import('@/views/HomeView.vue') },
    {
      path: '/filme/:id(\\d+)',
      name: 'filme',
      component: () => import('@/views/FilmeView.vue'),
      props: (rota) => ({ id: Number(rota.params.id) }),
    },
    {
      path: '/favoritos',
      name: 'favoritos',
      component: () => import('@/views/FavoritosView.vue'),
      meta: { exigeLogin: true },
    },
    { path: '/entrar', name: 'entrar', component: () => import('@/views/LoginView.vue'), meta: { soVisitante: true } },
    { path: '/cadastro', name: 'cadastro', component: () => import('@/views/CadastroView.vue'), meta: { soVisitante: true } },
    { path: '/:resto(.*)*', name: 'nao-encontrada', component: () => import('@/views/NaoEncontradaView.vue') },
  ],
})

router.beforeEach(async (to) => {
  const { iniciar, logado } = useAuth()
  await iniciar()

  if (to.meta.exigeLogin && !logado.value) {
    return { name: 'entrar', query: { redirecionar: to.fullPath } }
  }
  if (to.meta.soVisitante && logado.value) return { name: 'inicio' }
})

export default router
