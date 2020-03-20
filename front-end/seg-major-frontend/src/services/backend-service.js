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
}
