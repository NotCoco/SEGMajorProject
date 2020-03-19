import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080'
})

export default {
    createSite(siteName) {
        return api.post('/sites', { name: siteName })
    },

    getAllSites() {
        return api.get('/sites')
    },

    getSite(siteName) {
        return api.get(`/sites/${siteName}`)
    },

    getAllPages(siteName) {
        return api.get(`/sites/${siteName}/pages`)
    },

    getPage(siteName, pageSlug) {
        return api.get(`/sites/${siteName}/pages/${pageSlug}`)
    },

    createPage(page) {
        console.log(page)
        return api.post(`/sites/${page.site.name}/pages`, {
            "site": page.site.name,
            "slug": page.slug,
            "index": page.index,
            "title": page.title,
            "content": page.content
        })
    },

    updatePage(page) {
        return api.put(`/sites/${page.site.name}/pages`, {
            "primaryKey": page.primaryKey,
            "site": page.site,
            "slug": page.slug,
            "index": page.index,
            "title": page.title,
            "content": page.content
        })
    }
}