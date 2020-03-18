import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080'
})

export default {
    createSite(siteName) {
        return api.post('/sites', { name: siteName })
    }
}