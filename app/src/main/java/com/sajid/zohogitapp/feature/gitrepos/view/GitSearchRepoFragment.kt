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
import com.sajid.zohogitapp.R
import com.sajid.zohogitapp.common.BaseActivity
import com.sajid.zohogitapp.common.utils.DataFetchState
import com.sajid.zohogitapp.common.utils.DataSourceState
import com.sajid.zohogitapp.common.utils.NetworkUtils
import com.sajid.zohogitapp.common.utils.RecyclerEndScrollListner
import com.sajid.zohogitapp.databinding.FragmentGitListRepoBinding
import com.sajid.zohogitapp.databinding.FragmentGitSearchRepoBinding
import com.sajid.zohogitapp.datasources.remote.ApiState
import com.sajid.zohogitapp.feature.gitrepos.GitReposConfig
import com.sajid.zohogitapp.feature.gitrepos.GitReposConfig.INITIAL_PAGE_NUMBER
import com.sajid.zohogitapp.feature.gitrepos.GitReposConfig.PAGE_SIZE
import com.sajid.zohogitapp.feature.gitrepos.viewmodel.GitRepoSearchViewModel
import com.sajid.zohogitapp.feature.gitrepos.viewmodel.GitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class GitSearchRepoFragment : Fragment() {

    private val gitViewModel: GitViewModel by activityViewModels()
    private val gitRepoSearchViewModel: GitRepoSearchViewModel by viewModels()
    private lateinit var binding: FragmentGitSearchRepoBinding
    private var canLoadNew = true
    private lateinit var dataSourceState: DataSourceState


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGitSearchRepoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.gitRepoSearchViewModel = gitRepoSearchViewModel
        dataSourceState = if (NetworkUtils.isNetworkAvailable(requireContext()))
            DataSourceState.ONLINE_MODE
        else DataSourceState.OFFLINE_MODE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            gitRepoSearchViewModel._gitRepoListFlow.collect {
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
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }
            }
        }
        gitViewModel._searchQuery.observe(viewLifecycleOwner, {
            gitRepoSearchViewModel.loadSearchData(DataFetchState.FETCH_FIRST_DATA, it,dataSourceState)
        })
        addRecyclerViewScrollListner()


    }

    private fun addRecyclerViewScrollListner() {
        binding.searchRecyler.addOnScrollListener(object : RecyclerEndScrollListner() {
            override var canLoad: Boolean = canLoadNew
            override fun loadMoreData() {
                gitRepoSearchViewModel.loadSearchData(
                    DataFetchState.ADD_DATA,
                    gitViewModel._searchQuery.value,
                    dataSourceState
                )

            }
        })
    }

}