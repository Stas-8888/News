package com.example.newsapppp.presentation.fragments.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapppp.R
import com.example.newsapppp.databinding.FragmentMainBinding
import com.example.newsapppp.presentation.adapters.NewsAdapter
import com.example.newsapppp.presentation.fragments.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.no_internet_connections.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>() {
    private val newsAdapter by lazy { NewsAdapter() }
    override val viewModel by viewModels<MainFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showNewsList()
        viewModel.setupDayNight()
        setupRecyclerView()
        getCountryAndCategoryTabLayout()
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        tvCountry.setOnClickListener {
            rvNews.smoothScrollToPosition(0)
        }
        btProfile.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
        newsAdapter.setOnItemClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToNewsFragment(it))
        }
    }

    private fun showNewsList() = lifecycleScope.launch {
        viewModel.state.collect {
            when (it) {
                is MainState.ShowLoading -> {
                    binding.progressBar.isVisible = true
                }
                is MainState.HideLoading -> {
                    internetConnectionDialog()
                    binding.progressBar.isVisible = true
                }
                is MainState.ShowArticles -> {
                    binding.tvCenterText.isVisible = false
                    binding.progressBar.isVisible = false
                    newsAdapter.submitList(it.articles)
                }
                is MainState.ShowErrorScreen -> {
                    internetConnectionDialog()
                }
            }
        }
    }

    private fun internetConnectionDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.no_internet_connections)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.bt_try_again.setOnClickListener {
            recreate(requireActivity())
        }
        dialog.show()
    }

    private fun setupRecyclerView() = with(binding) {
        rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            toFirstRecyclerPosition()
        }
    }

    private fun showOrHideFloatButton() = lifecycleScope.launch {
        if (getFirstNewsPosition() < 1) {
            fabUp?.visibility = View.GONE
        } else fabUp?.visibility = View.VISIBLE
    }

    private fun getFirstNewsPosition(): Int =
        (rvNews?.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0

    private fun toFirstRecyclerPosition() {
        rvNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                showOrHideFloatButton()
            }
        })
        fabUp.setOnClickListener { rvNews.smoothScrollToPosition(0) }
    }

    private fun getCountryAndCategoryTabLayout() {
        val country = viewModel.getCountryFlag()
        viewModel.getNewsRetrofit(countryCode = country, category = categories[0])
        tvCountry.text = country

        binding.tabMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.getNewsRetrofit(countryCode = country, categories[tab!!.position])
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, container, false)

    private val categories = listOf(
        "Technology",
        "Sports",
        "Science",
        "Entertainment",
        "Business",
        "Health",
    )
}
