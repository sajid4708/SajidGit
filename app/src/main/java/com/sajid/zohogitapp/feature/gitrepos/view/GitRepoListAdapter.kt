package com.sajid.zohogitapp.feature.gitrepos.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sajid.zohogitapp.BR
import com.sajid.zohogitapp.R
import com.sajid.zohogitapp.common.utils.GitHubRepoDiffUtil
import com.sajid.zohogitapp.databinding.RepoItemTwoBinding
import com.sajid.zohogitapp.datasources.model.GitItems


class GitRepoListAdapter : RecyclerView.Adapter<GitRepoViewHolder>() {
    private var settedList= mutableSetOf<GitItems>()
    private var gitRepoList = mutableListOf<GitItems>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitRepoViewHolder {
        val binding = DataBindingUtil.inflate<RepoItemTwoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.repo_item_two, parent, false
        )
        return GitRepoViewHolder(binding)
    }



    override fun onBindViewHolder(holder: GitRepoViewHolder, position: Int) {
        holder.bind(gitRepoList[position])
    }

    override fun getItemCount(): Int {
        return gitRepoList.size
    }

    override fun getItemId(position: Int): Long {
        return gitRepoList[position].id
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    fun setData(newList: List<GitItems>) {
        val diffcallBack=GitHubRepoDiffUtil(gitRepoList, newList)
        val diffResult = DiffUtil.calculateDiff(diffcallBack)
        gitRepoList.clear()
        gitRepoList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }



}


class GitRepoViewHolder(private val binding: RepoItemTwoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GitItems) {
        binding.setVariable(BR.gitRepoItem, item)
        binding.setVariable(BR.langImgSrc,binding.root.context.getDrawable(R.drawable.ic_dot_18dp))
        binding.setVariable(BR.starImgSrc,binding.root.context.getDrawable(R.drawable.ic_star_yellow_24dp))
        binding.executePendingBindings()
    }

}
