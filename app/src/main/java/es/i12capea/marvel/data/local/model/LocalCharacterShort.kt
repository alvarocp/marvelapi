package es.i12capea.marvel.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "character_short")
data class LocalCharacterShort(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val offset: Int,
    val name: String,
    val img: String,
) : Parcelable