package es.i12capea.marvel.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "character")
data class LocalCharacter(
        @PrimaryKey(autoGenerate = false)
        val id: Int,
        val img: String,
        val name: String,
        val description: String,
) : Parcelable