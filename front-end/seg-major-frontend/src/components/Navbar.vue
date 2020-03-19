<template>
  <nav class="navbar" role="navigation" aria-label="main navigation">
    <div class="container is-fluid">
      <div class="navbar-brand">
        <a class="navbar-item">
          <h1 class="title is-5">Logo</h1>
        </a>
      </div>

      <div class="navbar-menu">
        <div class="navbar-start" style="margin-left: 35px">
          <a class="navbar-item">Home</a>
          <a class="navbar-item">News</a>
        </div>

        <div class="navbar-end">
          <div class="searchbox-container">
            <div class="control has-icons-left" style="height: 100%">
              <span class="icon is-small is-left" style="height: 100%;">
                <i class="search-icon material-icons">search</i>
              </span>
              <input class="searchbox input" type="text" placeholder="Search" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
export default {};
</script>

<style lang="scss" scoped>
nav.navbar {
  flex-shrink: 0;
  border-bottom: 3px solid #f5f5f8;
  .navbar-brand {
    padding-left: 14px;
  }
  .brand-text {
    .brand-top {
      font-size: 13.5px;
      opacity: 0.85;
      margin-bottom: -2px;
    }
    .brand-bottom {
      font-weight: bold;
      font-size: 16px;
      opacity: 0.92;
      margin-bottom: 0px;
    }
  }
}
.input.searchbox {
  height: 100%;
  width: 400px;
  border: 0;
  box-shadow: none;
  background-color: #fbfbfb;
  &:hover {
    background-color: #f9f9f9;
  }
  &:focus {
    background-color: #f5f5f7;
  }
  &::placeholder {
    opacity: 1;
    color: #888;
  }
}
.search-icon {
  color: #aaa;
}
</style>