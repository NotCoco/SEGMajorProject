import BackendService from './backend-service'

export default {
    async login(username, password) {
        const res = await BackendService.login(username, password)
        localStorage.setItem('api-key', res.data);

        return res.data
    },

    async logout() {
        await BackendService.logout()
        localStorage.removeItem('api-key')
    },

    async getUserName() {
        const res = await BackendService.getUserName()
        return res.data
    },

    isAuthenticated() {
        const apiKey = localStorage.getItem('api-key')
        if (!apiKey) return false
        return true
    }
}