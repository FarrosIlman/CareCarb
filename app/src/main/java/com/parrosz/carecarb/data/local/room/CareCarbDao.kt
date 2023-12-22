package com.parrosz.carecarb.data.local.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.parrosz.carecarb.data.local.entity.CareCarbEntity

@Dao
interface CareCarbDao {
    @Query("SELECT * FROM story")
    fun getStoriesAsLiveData(): LiveData<List<CareCarbEntity>>

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, CareCarbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStories(stories: List<CareCarbEntity>)

    @Query("DELETE FROM story")
    fun deleteAllStories()

    @Query("SELECT COUNT(id) FROM story")
    fun getCount(): Int
}