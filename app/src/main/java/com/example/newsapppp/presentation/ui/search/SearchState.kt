package com.example.newsapppp.presentation.ui.search

import com.example.newsapppp.core.State
import com.example.newsapppp.presentation.model.Article

sealed class SearchState : State {
    object Loading : SearchState()
    class Error(val message: String) : SearchState()
    data class ShowArticles(val articles: List<Article>) : SearchState()
}
