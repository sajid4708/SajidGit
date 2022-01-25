package com.sajid.zohogitapp.feature.gitrepos.view

import android.app.PendingIntent.getActivity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.sajid.zohogitapp.R
import com.sajid.zohogitapp.common.BaseActivity
import com.sajid.zohogitapp.common.utils.ConnectivityReceiver
import com.sajid.zohogitapp.common.utils.DataSourceState
import com.sajid.zohogitapp.common.utils.NetworkUtils
import com.sajid.zohogitapp.databinding.ActivityGitBinding
import com.sajid.zohogitapp.datasources.local.GitDatabase
import com.sajid.zohogitapp.datasources.model.GitItems
import com.sajid.zohogitapp.datasources.model.Owner
import com.sajid.zohogitapp.feature.gitrepos.viewmodel.GitViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GitActivity : BaseActivity() {
    private val gitViewModel: GitViewModel by viewModels()
    private lateinit var binding: ActivityGitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dismissKeyBoard()
        binding.lifecycleOwner = this
        binding.gitViewModel = gitViewModel
        gitViewModel.isInternetConnected(if (NetworkUtils.isNetworkAvailable(this)) DataSourceState.ONLINE_MODE else DataSourceState.OFFLINE_MODE)
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



    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(!isConnected){
        Toast.makeText(this,getString(R.string.no_network_info),Toast.LENGTH_SHORT).show()
        }
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