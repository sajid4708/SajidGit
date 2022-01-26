package com.sajid.zohogitapp.feature.gitrepos.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.sajid.zohogitapp.R
import com.sajid.zohogitapp.common.BaseActivity
import com.sajid.zohogitapp.common.utils.DataSourceState
import com.sajid.zohogitapp.common.utils.NetworkUtils
import com.sajid.zohogitapp.databinding.ActivityGitBinding
import com.sajid.zohogitapp.feature.gitrepos.viewmodel.GitViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
activity with top app bar,holds a Navhost Fragment in View to show List
 */
@AndroidEntryPoint
class GitActivity : BaseActivity() {
    private val gitViewModel: GitViewModel by viewModels()
    private lateinit var binding: ActivityGitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.gitViewModel = gitViewModel
        initNetworkListeners()
        dismissKeyBoard()
        observeSearchStateAndNavigateToFragment()
    }

    /**
    checks Internet Status and Updates current Internet Status at
     Start of Activity to view Model
     */
    private fun initNetworkListeners(){
        gitViewModel.isInternetConnected(if (NetworkUtils.isNetworkAvailable(this)) DataSourceState.ONLINE_MODE else DataSourceState.OFFLINE_MODE)
        lifecycleScope.launchWhenCreated { gitViewModel.checkIfOfflineDataExist() }
    }

    /**
    Observes Search State in View Model
     and based on Search State navigates to respective Fragment
     */
    private fun observeSearchStateAndNavigateToFragment(){
        gitViewModel.searchState.observe(this, {
            if (it) {
                if(binding.navHostFragment.findNavController().currentDestination?.id!=R.id.gitSearchRepoFragment){
                    binding.navHostFragment.findNavController()
                        .navigate(R.id.action_gitListRepoFragment_to_gitSearchRepoFragment)
                }
                binding.headerWithSearch.searchBoxTxt.isFocusableInTouchMode = true
                binding.headerWithSearch.searchBoxTxt.requestFocus()
                showKeyBoard(binding.headerWithSearch.searchBoxTxt)

            } else {
                binding.navHostFragment.findNavController().navigateUp()
                dismissKeyBoard()
            }
        })
    }


    /**
    When New Network Connection is Established it is received here
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        gitViewModel.isInternetConnected(if(isConnected) DataSourceState.ONLINE_MODE else DataSourceState.OFFLINE_MODE)
    }

    override fun onBackPressed() {
        if(binding.navHostFragment.findNavController().currentDestination?.id==R.id.gitSearchRepoFragment){
            gitViewModel.onBackClicked()
        }
        else{
            super.onBackPressed()
        }

    }


}