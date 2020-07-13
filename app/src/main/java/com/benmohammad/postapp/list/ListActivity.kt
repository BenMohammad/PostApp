package com.benmohammad.postapp.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.benmohammad.core.application.BaseActivity
import com.benmohammad.core.networking.Outcome
import com.benmohammad.postapp.R
import com.benmohammad.postapp.commons.PostDH
import com.benmohammad.postapp.commons.data.PostWithUser
import com.benmohammad.postapp.details.DetailsActivity
import com.benmohammad.postapp.list.viewmodel.ListViewModel
import com.benmohammad.postapp.list.viewmodel.ListViewModelFactory
import kotlinx.android.synthetic.main.activity_list.*
import java.io.IOException
import javax.inject.Inject

class ListActivity: BaseActivity(), PostListAdapter.Interaction {

    private val component by lazy {
        PostDH.listComponent()
    }

    @Inject
    lateinit var viewModelFactory: ListViewModelFactory

    @Inject
    lateinit var adapter: PostListAdapter

    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private val context: Context by lazy {this}

    private val TAG = "ListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        component.inject(this)

        adapter.interaction = this
        rvPosts.adapter = adapter
        srlPosts.setOnRefreshListener { viewModel.refreshPosts() }
        viewModel.getPosts()
        initiatedDataListener()
    }

    private fun initiatedDataListener() {
        viewModel.postOutcome.observe(this, Observer<Outcome<List<PostWithUser>>> {
          outcome -> Log.d(TAG, "InitiateDataListener")
            when(outcome) {
                is Outcome.Progress -> srlPosts.isRefreshing = outcome.loading
                is Outcome.Success -> {
                    Log.d(TAG, "InitiateDataListener")
                    adapter.swapData(outcome.data)
                }

                is Outcome.Failure -> {
                    if (outcome.e is IOException)
                        Toast.makeText(
                            context, R.string.need_internet_posts, Toast.LENGTH_SHORT
                        ).show()
                    else

                        Toast.makeText(context, R.string.failed_posts_try_again, Toast.LENGTH_SHORT)
                            .show()

                }

                }
            }
        )
    }


    override fun postClicked(
        post: PostWithUser,
        tvTitle: TextView,
        tvBody: TextView,
        tvAuthorName: TextView,
        ivAvatar: ImageView
    ) {
        DetailsActivity.start(context, post, tvTitle, tvBody, tvAuthorName, ivAvatar)
    }
}