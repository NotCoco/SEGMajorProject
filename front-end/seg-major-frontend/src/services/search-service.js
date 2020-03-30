function getSearchRegex(query) {
  const escapedQuery = query.replace(/[-/\\^$*+?.()|[\]{}]/g, '\\$&');
  return new RegExp(escapedQuery, 'i');
}

function searchPage(page, regex) {
  if (page.content) {
    let weight = searchNode(JSON.parse(page.content), regex);
    if (regex.test(page.title)) weight += 7;
    return weight;
  }
  else return 0;
}

function searchNode(node, regex) {
}

export default {
  search(pages, query) {
    const regex = getSearchRegex(query);
    return pages.map(page => ({
                      page: page,
                      weight: searchPage(page, regex)
                     }))
  }
}
