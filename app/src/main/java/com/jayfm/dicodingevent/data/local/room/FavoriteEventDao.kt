package com.jayfm.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jayfm.dicodingevent.data.local.entity.FavoriteEventEntity

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM favorite_events")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEvent: FavoriteEventEntity)

    @Delete
    suspend fun delete(favoriteEvent: FavoriteEventEntity)

    @Query("SELECT * FROM favorite_events WHERE id = :id")
    fun getFavoriteEventById(id: Int): LiveData<FavoriteEventEntity?>
}
