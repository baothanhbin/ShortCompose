package com.baothanhbin.shortcompose.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sample_items")
data class SampleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val createdAt: Long,
)
