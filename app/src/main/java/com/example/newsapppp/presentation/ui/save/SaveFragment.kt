package com.example.newsapppp.presentation.ui.save

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapppp.databinding.FragmentSaveBinding
import com.example.newsapppp.presentation.adapters.NewsAdapter
import com.example.newsapppp.presentation.ui.base.BaseFragment
import com.example.newsapppp.presentation.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_save.*

@AndroidEntryPoint
class SaveFragment : BaseFragment<SaveState, FragmentSaveBinding, SaveFragmentViewModel>(
    FragmentSaveBinding::inflate
) {
    private val newsAdapter by lazy { NewsAdapter() }
    override val viewModel by viewModels<SaveFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation()
        setupRecyclerView()
        swipeToDelete()
        viewModel.getAllNews()
        setupOnClickListeners()
    }

    override fun renderState(state: SaveState) {
        when (state) {
            is SaveState.ShowLoading -> {
                binding.progressBar.visible()
            }
            is SaveState.ShowArticles -> {
                binding.tvBackgroundText.invisible()
                binding.progressBar.invisible()
                newsAdapter.submitList(state.articles)
            }
            is SaveState.ShowDeleteDialog ->{
                showDeleteDialog(
                    { viewModel.deleteArticle(state.article) },
                    { newsAdapter.notifyItemChanged(state.position) })
            }
        }
    }

    private fun setupOnClickListeners() {
        btDeleteAll.setOnClickListener {
            showDeleteDialog({ viewModel.deleteAllArticle() }, { })
        }
        newsAdapter.setOnItemClickListener {
            navigateDirections(SaveFragmentDirections.actionSaveFragmentToNewsFragment(it))
        }
    }

    private fun setupRecyclerView() = with(binding) {
        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun swipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val initial = viewHolder.adapterPosition
                val final = target.adapterPosition
                newsAdapter.notifyItemMoved(initial, final)
                return true
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = 0.3f

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.currentList[position]
                viewModel.onItemSwiped(article, position)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }
    }
}
