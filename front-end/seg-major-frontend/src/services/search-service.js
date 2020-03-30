function getSearchRegex(query) {
  const escapedQuery = query.replace(/[-/\\^$*+?.()|[\]{}]/g, '\\$&');
  return new RegExp(escapedQuery, 'i');
}

export default {
  search(pages, query) {
    const regex = getSearchRegex(query);
  }
}
