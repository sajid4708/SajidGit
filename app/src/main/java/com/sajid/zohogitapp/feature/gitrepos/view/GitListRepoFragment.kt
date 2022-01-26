package com.sajid.zohogitapp.feature.gitrepos.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.sajid.zohogitapp.common.utils.DataFetchState
import com.sajid.zohogitapp.common.utils.DataSourceState
import com.sajid.zohogitapp.common.utils.NetworkUtils
import com.sajid.zohogitapp.common.utils.RecyclerEndScrollListner
import com.sajid.zohogitapp.databinding.FragmentGitListRepoBinding
import com.sajid.zohogitapp.datasources.remote.ApiState
import com.sajid.zohogitapp.feature.gitrepos.viewmodel.GitRepoListViewModel
import com.sajid.zohogitapp.feature.gitrepos.viewmodel.GitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.security.acl.Owner

/**
fragment holds list of public repositories
 */
@AndroidEntryPoint
class GitListRepoFragment : Fragment() {
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
        initUpdateNetworkStatus()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.gitRepoListViewModel = gitRepoListViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkStatusObserver()
        dataSourceResponseObserverStatus()
        gitRepoListViewModel.loadGitRepoListData(DataFetchState.FETCH_FIRST_DATA, dataSourceState)
        addSwipeListeners()
        addRecyclerViewScrollListner()
    }

    /**
     Swipe refresh Listner
     */
    private fun addSwipeListeners() {
        binding.swipeRefresher.setOnRefreshListener {
            gitRepoListViewModel.loadGitRepoListData(DataFetchState.REFRESH_DATA, dataSourceState)
        }
    }

    private fun addRecyclerViewScrollListner() {
        binding.gitListRecyler.addOnScrollListener(object : RecyclerEndScrollListner() {
            override var canLoad: Boolean = canLoadNew
            override fun loadMoreData() {
                lifecycleScope.launch {
                gitRepoListViewModel.loadGitRepoListData(DataFetchState.ADD_DATA, dataSourceState)
                }
            }
        })
    }

    /**
    checks current Network Status and Updates
     */
    private fun initUpdateNetworkStatus() {
        dataSourceState = if (NetworkUtils.isNetworkAvailable(requireContext()))
            DataSourceState.ONLINE_MODE
        else DataSourceState.OFFLINE_MODE
    }

    /**
    Observes current change in network connection
     */
    private fun networkStatusObserver() {
        gitViewModel.isInternetConnected.observe(viewLifecycleOwner, {
            if (it == DataSourceState.OFFLINE_MODE) {
                lifecycleScope.launchWhenCreated {
                    gitRepoListViewModel.setErrorState(true)
                }
            }
            dataSourceState = it
        })
    }

    /**
    Observes data Source data fetch status
     */
    private fun dataSourceResponseObserverStatus() {
        lifecycleScope.launchWhenStarted {
            gitRepoListViewModel._gitRepoListFlow.collect {
                when (it) {
                    is ApiState.Success -> {
                        canLoadNew = true
                        gitViewModel.checkIfOfflineDataExist()
                    }
                    is ApiState.Loading -> {
                        canLoadNew = false

                    }
                    is ApiState.Failure -> {
                        canLoadNew = true
                        Toast.makeText(
                            requireContext(),
                            it.msg.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }
            }
        }
    }
}


