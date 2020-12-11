package es.i12capea.marvel.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "query_results")
data class LocalQueryResults (
        @PrimaryKey(autoGenerate = false)
        val offset: Int,
        val total: Int,
) : Parcelable