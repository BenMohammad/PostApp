package com.benmohammad.postapp.commons.data.local

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface CommentDao {

    @Query("SELECT * FROM comment where postId = :postId")
    fun getForPost(postId: Int): Flowable<List<Comment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(comments: List<Comment>)

    @Delete
    fun delete(comment: Comment)
}