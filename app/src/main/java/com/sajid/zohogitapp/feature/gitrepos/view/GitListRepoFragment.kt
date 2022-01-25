package com.sajid.zohogitapp.feature.gitrepos.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sajid.zohogitapp.common.utils.DataFetchState
import com.sajid.zohogitapp.common.utils.DataSourceState
import com.sajid.zohogitapp.common.utils.NetworkUtils
import com.sajid.zohogitapp.databinding.FragmentGitListRepoBinding
import com.sajid.zohogitapp.feature.gitrepos.viewmodel.GitRepoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.sajid.zohogitapp.common.utils.RecyclerEndScrollListner
import com.sajid.zohogitapp.datasources.remote.ApiState
import com.sajid.zohogitapp.feature.gitrepos.GitReposConfig.INITIAL_PAGE_NUMBER
import com.sajid.zohogitapp.feature.gitrepos.GitReposConfig.PAGE_SIZE
import com.sajid.zohogitapp.feature.gitrepos.viewmodel.GitViewModel
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class GitListRepoFragment @Inject constructor() : Fragment() {
    private val gitViewModel: GitViewModel by activityViewModels()
    private val gitRepoListViewModel: GitRepoListViewModel by viewModels()
    private lateinit var binding: FragmentGitListRepoBinding
    private var canLoadNew = true
    private lateinit var dataSourceState: DataSourceState


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGitListRepoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.gitRepoListViewModel = gitRepoListViewModel
        dataSourceState = if (NetworkUtils.isNetworkAvailable(requireContext()))
            DataSourceState.ONLINE_MODE
        else DataSourceState.OFFLINE_MODE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gitViewModel.isInternetConnected.observe(viewLifecycleOwner, {
            dataSourceState = it
        })
        lifecycleScope.launchWhenStarted {
            gitRepoListViewModel._gitRepoListFlow.collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        canLoadNew = true
                    }
                    is ApiState.Loading -> {
                        canLoadNew = false

                    }
                    is ApiState.Failure -> {
                        canLoadNew = true
                        Toast.makeText(
                            requireContext(),
                            it.msg.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
            }
        }
        gitRepoListViewModel.loadGitRepoListData(DataFetchState.FETCH_FIRST_DATA, dataSourceState)
        addSwipeListeners()
        addRecyclerViewScrollListner()

    }

    private fun addSwipeListeners() {
        binding.swipeRefresher.setOnRefreshListener {
            gitRepoListViewModel.loadGitRepoListData(DataFetchState.REFRESH_DATA, dataSourceState)
        }
    }

    private fun addRecyclerViewScrollListner() {
        binding.gitListRecyler.addOnScrollListener(object : RecyclerEndScrollListner() {
            override var canLoad: Boolean = canLoadNew
            override fun loadMoreData() {
                gitRepoListViewModel.loadGitRepoListData(DataFetchState.ADD_DATA, dataSourceState)
            }
        })
    }
}


