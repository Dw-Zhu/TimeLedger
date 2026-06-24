import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('timeledger_token') || '')

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('timeledger_token', newToken)
  }

  function clearToken() {
    token.value = ''
    localStorage.removeItem('timeledger_token')
  }

  return { token, setToken, clearToken }
})