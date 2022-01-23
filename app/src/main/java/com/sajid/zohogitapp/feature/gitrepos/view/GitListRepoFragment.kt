package com.sajid.zohogitapp.feature.gitrepos.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sajid.zohogitapp.databinding.FragmentGitListRepoBinding
import com.sajid.zohogitapp.feature.gitrepos.viewmodel.GitRepoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.sajid.zohogitapp.common.utils.RecyclerEndScrollListner
import com.sajid.zohogitapp.datasources.remote.ApiState
import com.sajid.zohogitapp.feature.gitrepos.GitReposConfig.INITIAL_PAGE_NUMBER
import com.sajid.zohogitapp.feature.gitrepos.GitReposConfig.PAGE_SIZE
import kotlinx.coroutines.flow.collect





@AndroidEntryPoint
class GitListRepoFragment @Inject constructor() : Fragment(){
    private val gitRepoListViewModel: GitRepoListViewModel by viewModels()
    private lateinit var binding: FragmentGitListRepoBinding
    private var pageNo= INITIAL_PAGE_NUMBER
    private var canLoadNew=true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGitListRepoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.gitRepoListViewModel = gitRepoListViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            gitRepoListViewModel._gitRepoListFlow.collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        canLoadNew=true
                    }
                    is ApiState.Loading -> {
                        canLoadNew=false

                    }
                    is ApiState.Failure -> {
                        canLoadNew=true


                    }
                }
            }
        }
         gitRepoListViewModel.getRepoListData(pageNo, PAGE_SIZE)
                addSwipeListeners()
                addRecyclerViewScrollListner()

    }

    private fun addSwipeListeners() {
        binding.swipeRefresher.setOnRefreshListener {
            pageNo=1
            gitRepoListViewModel.getRepoListData(pageNo, PAGE_SIZE)
        }
    }

    private fun addRecyclerViewScrollListner() {
        binding.gitListRecyler.addOnScrollListener(object : RecyclerEndScrollListner() {
            override var canLoad: Boolean=canLoadNew
            override fun loadMoreData() {
                    pageNo++
                    gitRepoListViewModel.addRepoListData(pageNo, PAGE_SIZE)
            }
        })
    }
}


