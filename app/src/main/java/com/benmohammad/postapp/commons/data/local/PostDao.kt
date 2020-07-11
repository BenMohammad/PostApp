package com.benmohammad.postapp.commons.data.local

import androidx.room.*
import com.benmohammad.postapp.commons.data.PostWithUser
import io.reactivex.Flowable

@Dao
interface PostDao {

    @Query("SELECT post.postId AS postId, post.postTitle as postTitle, post.postBody as postBody, user.userName as userName FROM post, user WHERE post.userID = user.id")
    fun getAllPostWithUser(): Flowable<List<PostWithUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(posts: List<Post>)

    @Delete
    fun delete(post: Post)

    @Query("SELECT * FROM post")
    fun getAll(): Flowable<List<Post>>
}