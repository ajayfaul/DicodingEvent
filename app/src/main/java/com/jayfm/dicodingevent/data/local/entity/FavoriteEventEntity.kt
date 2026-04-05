package com.jayfm.dicodingevent.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEventEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "imageLogo")
    val imageLogo: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "cityName")
    val cityName: String,

    @ColumnInfo(name = "beginTime")
    val beginTime: String
)
