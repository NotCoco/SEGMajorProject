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
  switch(node.type) {
    case "paragraph": { // Base case
      const text = getCombinedContent(node);
      return regex.test(text) ? 1 : 0;
    }
    case "heading": { // Base case
      const text = getCombinedContent(node);
      return regex.test(text) ? (5 - node.attrs.level) : 0;
    }
    default: { // Recursive case
      if (!node.content) return 0;
      return node.content
                 .map(child => searchNode(child, regex))
                 .reduce((a, b) => a + b, 0);
    }
  }
}

function getCombinedContent(node) {
  if (!node.content) return '';
  let result = "";
  node.content.forEach(contentNode => {
    if (contentNode.type === "text") result += contentNode.text;
  });
  return result;
}

export default {
  search(pages, query) {
    const regex = getSearchRegex(query);
    return pages.map(page => ({
                      page: page,
                      weight: searchPage(page, regex)
                     }))
                .filter(result => result.weight > 0)
                .sort((a, b) => b.weight - a.weight)
                .map(result => result.page);
  }
}
