package net.sistransito.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val code: String,
    val state: String
) 