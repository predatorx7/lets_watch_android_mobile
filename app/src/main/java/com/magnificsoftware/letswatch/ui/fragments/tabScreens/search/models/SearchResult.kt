package com.magnificsoftware.letswatch.ui.fragments.tabScreens.search.models

interface SearchResult {
    val name: String
    val poster: String
}

data class SearchResultImpl(
    override val name: String,
    override val poster: String,
) : SearchResult