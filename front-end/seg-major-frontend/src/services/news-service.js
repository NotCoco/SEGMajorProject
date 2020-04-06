import BackendService from './backend-service'

export default {
  newsPromise: null,

  async getAllArticles() {
    if (!this.newsPromise) {
      this.newsPromise = BackendService.getAllArticles()
                                       .then(function(res) {
        let data = res.data;
        data.forEach(news => news.date = new Date(news.date));
        return data;
      });
    }
    
    return await this.newsPromise;
  },

  async getArticle(slug) {
    return (await this.getAllArticles())
            .find(elem => elem.slug === slug) ?? null;
  }
}
