import BackendService from './backend-service'

export default {
  newsPromise: null,

  async getAllNews() {
    if (!this.newsPromise) {
      this.newsPromise = BackendService.getAllNews()
                                       .then(function(res) {
        let data = res.data;
        data.forEach(news => news.date = new Date(news.date));
        return data;
      });
    }
    
    return await this.newsPromise;
  },

  async getNews(slug) {
    return (await this.getAllNews())
            .find(elem => elem.slug === slug) ?? null;
  }
}
