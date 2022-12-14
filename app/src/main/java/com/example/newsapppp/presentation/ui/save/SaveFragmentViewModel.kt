package com.example.newsapppp.presentation.ui.save

import com.example.newsapppp.domain.interactors.room.DeleteAllUseCase
import com.example.newsapppp.domain.interactors.room.DeleteArticleUseCase
import com.example.newsapppp.domain.interactors.room.GetRoomArticleUseCase
import com.example.newsapppp.presentation.mapper.ArticleMapperToModel
import com.example.newsapppp.presentation.model.Article
import com.example.newsapppp.presentation.ui.base.BaseViewModel
import com.example.newsapppp.presentation.utils.extensions.launchCoroutine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SaveFragmentViewModel @Inject constructor(
    private val getRoomArticleUseCase: GetRoomArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    private val deleteAllUseCase: DeleteAllUseCase,
    private val articleMapperToModel: ArticleMapperToModel
) : BaseViewModel<SaveState>() {

    override val _state = MutableStateFlow<SaveState>(SaveState.ShowLoading)
    override val state = _state.asStateFlow()

    fun getAllNews() = launchCoroutine {
        _state.emit(SaveState.ShowLoading)
        getRoomArticleUseCase(Unit).collect {
            _state.emit(SaveState.ShowArticles(articleMapperToModel.articleToModelArticle(it)))
        }
    }

    fun deleteArticle(article: Article) = launchCoroutine {
        deleteArticleUseCase(articleMapperToModel.mapFromEntity(article))
    }

    fun deleteAllArticle() = launchCoroutine {
        deleteAllUseCase(Unit)
    }

    fun onItemSwiped(article: Article, position: Int) = launchCoroutine {
        emit(SaveState.ShowDeleteDialog(article, position))
    }
}
