import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080'
})

export default {
  createSite(site) {
    return api.post('/sites', site)
  },

  getAllSites() {
    return api.get('/sites')
  },

  getSite(siteSlug) {
    return api.get(`/sites/${siteSlug}`)
  },

  updateSite(site) {
    return api.put(`/sites`, {
      "id": site.primaryKey,
      "name": site.name,
      "slug": site.slug
    })
  },

  deleteSite(site) {
    return api.delete(`/sites/${site.slug}`)
  },

  getAllPages(siteSlug) {
    return api.get(`/sites/${siteSlug}/pages`)
  },

  getPage(siteSlug, pageSlug) {
    return api.get(`/sites/${siteSlug}/pages/${pageSlug}`)
  },

  createPage(page) {
    return api.post(`/sites/${page.site.slug}/pages`, {
      "site": page.site.slug,
      "slug": page.slug,
      "index": page.index,
      "title": page.title,
      "content": page.content
    })
  },

  updatePage(page) {
    return api.put(`/sites/${page.site.slug}/pages`, {
      "primaryKey": page.primaryKey,
      "site": page.site.slug,
      "slug": page.slug,
      "index": page.index,
      "title": page.title,
      "content": page.content
    })
  },

  deletePage(page) {
    return api.delete(`/sites/${page.site.slug}/pages/${page.slug}`)
  },

  getAllNews() {
    return api.get('/news')
  },

  createNews(data) {
    return api.post('/news', data)
  },

  updateNews(data) {
    return api.put('/news', data)
  },

  deleteNews(data) {
    return api.delete(`/news/${data.slug}`)
  },

  getAllMedicines() {
    return api.get('/medicines')
  },

  createMedicine(data) {
    return api.post('/medicines', data)
  },

  updateMedicine(data) {
    return api.put('/medicines', data)
  },

  deleteMedicine(data) {
    return api.delete(`/medicines/${data.id}`)
  },
}
