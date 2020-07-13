package com.benmohammad.postapp.list

import android.widget.ImageView
import android.widget.TextView
import com.benmohammad.core.application.BaseActivity
import com.benmohammad.postapp.commons.data.PostWithUser

class ListActivity: BaseActivity(), PostListAdapter.Interaction {



    override fun postClicked(
        post: PostWithUser,
        tvTitle: TextView,
        tvBody: TextView,
        tvAuthorName: TextView,
        ivAvatar: ImageView
    ) {
        TODO("Not yet implemented")
    }
}