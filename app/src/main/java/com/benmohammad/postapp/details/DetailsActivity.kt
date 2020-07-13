package com.benmohammad.postapp.details

import android.app.Activity
import android.app.AppComponentFactory
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.benmohammad.core.application.BaseActivity
import com.benmohammad.core.networking.Outcome
import com.benmohammad.postapp.R
import com.benmohammad.postapp.commons.PostDH
import com.benmohammad.postapp.commons.data.PostWithUser
import com.benmohammad.postapp.commons.data.local.Comment
import com.benmohammad.postapp.details.exceptions.DetailsExceptions
import com.benmohammad.postapp.details.viewmodel.DetailsViewModel
import com.benmohammad.postapp.details.viewmodel.DetailsViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class DetailsActivity: BaseActivity(), DetailsAdapter.Interaction {

    companion object {
        private const val SELECTED_POST = "post"
        private const val TITLE_TRANSITION_NAME = "title_transition"
        private const val BODY_TRANSITION_NAME = "body_transition"
        private const val AUTHOR_TRANSITION_NAME = "author_transition"
        private const val AVATAR_TRANSITION_NAME = "avatar_transition"

        fun start(
            context: Context,
            post: PostWithUser,
            tvTitle: TextView,
            tvBody: TextView,
            tvAuthorName: TextView,
            ivAvatar: ImageView
        ) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(SELECTED_POST, post)

            intent.putExtra(TITLE_TRANSITION_NAME, ViewCompat.getTransitionName(tvTitle))
            intent.putExtra(BODY_TRANSITION_NAME, ViewCompat.getTransitionName(tvBody))
            intent.putExtra(AUTHOR_TRANSITION_NAME, ViewCompat.getTransitionName(tvAuthorName))
            intent.putExtra(AVATAR_TRANSITION_NAME, ViewCompat.getTransitionName(ivAvatar))

            val p1 = Pair.create(tvTitle as View, ViewCompat.getTransitionName(tvTitle))
            val p2 = Pair.create(tvBody as View, ViewCompat.getTransitionName(tvBody))
            val p3 = Pair.create(tvAuthorName as View, ViewCompat.getTransitionName(tvAuthorName))
            val p4 = Pair.create(ivAvatar as View, ViewCompat.getTransitionName(ivAvatar))
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                p1, p2, p3, p4
            )

            context.startActivity(intent, options.toBundle())
        }


    }



    fun start(context: Context, postId: Int) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra(SELECTED_POST, postId)
        context.startActivity(intent)
    }

    override fun commentClicked(model: Comment) {
        Log.d(TAG, "Comment Clicked!!!: $model")
    }

    private val TAG = "DetailsActivity"
    private var selectedPost: PostWithUser? = null
    private val context: Context by lazy {this}
    private val component by lazy {PostDH.detailsComponent()}
    val adapter: DetailsAdapter by lazy {DetailsAdapter(this)}
    @Inject
    lateinit var viewModelFactory: DetailsViewModelFactory

    @Inject
    lateinit var picasso: Picasso

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        component.inject(this)
        getIntentData()
        srlComments.setOnRefreshListener { viewModel.refreshCommentsFor(selectedPost?.postId)}

    }

    private fun getIntentData() {
        if(!intent.hasExtra(SELECTED_POST)) {
            Log.d(TAG, "getIntentData could not find selected post")
            finish()
            return
        }

        selectedPost = intent.getParcelableExtra(SELECTED_POST)
        tvTitle.text = selectedPost?.postTitle
        tvBody.text = selectedPost?.postBody
        tvAuthorName.text = selectedPost?.userName
        picasso.load(selectedPost?.getAvatarPhoto()).into(ivAvatar)

        handleTransition(intent.extras)
        rvComments.adapter = adapter

        viewModel.loadCommentsFor(selectedPost?.postId)
        observeData()
    }

    private fun handleTransition(extras: Bundle?) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvTitle.transitionName = extras?.getString(TITLE_TRANSITION_NAME)
            tvBody.transitionName = extras?.getString(BODY_TRANSITION_NAME)
            tvAuthorName.transitionName = extras?.getString(AUTHOR_TRANSITION_NAME)
            ivAvatar.transitionName = extras?.getString(AVATAR_TRANSITION_NAME)
        }
    }

    private fun observeData() {
        viewModel.commentsOutcome.observe(this, Observer<Outcome<List<Comment>>>{
            outcome ->
            Log.d(TAG, "InitiateDataListener: " + outcome.toString())
            when(outcome) {
                is Outcome.Progress -> srlComments.isRefreshing = outcome.loading
                is Outcome.Success -> {
                    Log.d(TAG, "observeData: Successfully loaded data")
                    tvCommentError.visibility = View.GONE
                    adapter.swapData(outcome.data)
                }
                is Outcome.Failure -> {
                    when(outcome.e) {
                        DetailsExceptions.NoComments() -> tvCommentError.visibility = View.VISIBLE
                        IOException() -> Toast.makeText(context, R.string.need_internet_posts, Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(context, R.string.failed_posts_try_again, Toast.LENGTH_SHORT).show()

                    }                }
            }
        })
    }
}