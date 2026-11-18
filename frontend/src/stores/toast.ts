import { ref } from 'vue'

export interface Aviso {
  id: number
  texto: string
  tipo: 'erro' | 'info'
}

const avisos = ref<Aviso[]>([])
let contador = 0

export function useToast() {
  function mostrar(texto: string, tipo: Aviso['tipo'] = 'info') {
    const id = ++contador
    avisos.value = [...avisos.value, { id, texto, tipo }]
    setTimeout(() => fechar(id), 4500)
  }

  function fechar(id: number) {
    avisos.value = avisos.value.filter((a) => a.id !== id)
  }

  return { avisos, mostrar, fechar, erro: (t: string) => mostrar(t, 'erro') }
}
