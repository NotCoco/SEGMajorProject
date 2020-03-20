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

    async getSite(siteSlug) {
        const res = await BackendService.getSite(siteSlug)
        return res.data
    },

    async getAllPages(siteSlug) {
        const res = await BackendService.getAllPages(siteSlug)
        return res.data
    },

    async getPage(siteSlug, pageSlug) {
        const res = await BackendService.getPage(siteSlug, pageSlug)
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