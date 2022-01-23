package com.sajid.zohogitapp.feature.gitrepos.view

import android.app.PendingIntent.getActivity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.sajid.zohogitapp.R
import com.sajid.zohogitapp.common.BaseActivity
import com.sajid.zohogitapp.databinding.ActivityGitBinding
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
        gitViewModel.searchState.observe(this, {
            if (it) {
                binding.navHostFragment.findNavController()
                    .navigate(R.id.action_gitListRepoFragment_to_gitSearchRepoFragment)
                binding.headerWithSearch.searchBoxTxt.isFocusableInTouchMode = true
                binding.headerWithSearch.searchBoxTxt.requestFocus()
                showKeyBoard(binding.headerWithSearch.searchBoxTxt)

            } else {
                binding.navHostFragment.findNavController().navigateUp()
                dismissKeyBoard()
            }
        })

    }




}