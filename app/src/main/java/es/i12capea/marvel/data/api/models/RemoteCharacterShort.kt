package es.i12capea.marvel.data.api.models

import android.media.Image

data class RemoteCharacterShort(
    val id : Int,
    val name: String,
    val thumbnail: Thumbnail
)