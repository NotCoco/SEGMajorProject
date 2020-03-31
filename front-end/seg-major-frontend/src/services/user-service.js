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
	
	async getResetRequest(email){
		await BackendService.getResetRequest(email)
	},
	
	async resetPassword(token,passwd) {
		const res = await BackendService.resetPassword(token,passwd)
		return res.data
		//logout()
	},
	
    isAuthenticated() {
        const apiKey = localStorage.getItem('api-key')
        if (!apiKey) return false
        return true
    }
}