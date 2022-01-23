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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GitSearchRepoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class GitSearchRepoFragment @Inject constructor() : Fragment() {

    private val gitViewModel: GitViewModel by activityViewModels()
    private val gitRepoSearchViewModel: GitRepoSearchViewModel by viewModels()
    private lateinit var binding: FragmentGitSearchRepoBinding
    private var pageNo= INITIAL_PAGE_NUMBER
    private var canLoadNew=true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGitSearchRepoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.gitRepoSearchViewModel = gitRepoSearchViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            gitRepoSearchViewModel._gitRepoListFlow.collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        canLoadNew=true
                    }
                    is ApiState.Loading -> {
                        canLoadNew=false

                    }
                    is ApiState.Failure -> {
                        canLoadNew=true
                        Toast.makeText(requireContext(),it.msg.localizedMessage,Toast.LENGTH_SHORT).show()


                    }
                }
            }
        }
        gitViewModel.searchQuery.observe(viewLifecycleOwner, {
                pageNo=1
                gitRepoSearchViewModel.getRepoSearchListData(pageNo, PAGE_SIZE,it)


        })
        addRecyclerViewScrollListner()



    }
    private fun addRecyclerViewScrollListner() {
        binding.searchRecyler.addOnScrollListener(object : RecyclerEndScrollListner() {
            override var canLoad: Boolean=canLoadNew
            override fun loadMoreData() {
                pageNo++
                gitRepoSearchViewModel.getRepoSearchListData(pageNo, PAGE_SIZE,gitViewModel.searchQuery.value)

            }
        })
    }

    override fun onDestroyView() {
        gitViewModel.onBackClicked()
        super.onDestroyView()
    }

}