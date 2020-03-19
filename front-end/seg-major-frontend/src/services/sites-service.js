import BackendService from './backend-service'

export default {
    async createSite(site) {
        const res = await BackendService.createSite(site)
        return res.data
    },

    async getAllSites() {
        const res = await BackendService.getAllSites()
        return res.data
    },

    async getSite(siteName) {
        const res = await BackendService.getSite(siteName)
        return res.data
    },

    async getAllPages(siteName) {
        const res = await BackendService.getAllPages(siteName)
        return res.data
    },

    async getPage(siteName, pageSlug) {
        const res = await BackendService.getPage(siteName, pageSlug)
        return res.data
    },

    async createPage(page) {
        const res = await BackendService.createPage(page)
        return res.data
    },

    async updatePage(page) {
        const res = await BackendService.updatePage(page)
        return res.data
    }
}