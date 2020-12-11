package es.i12capea.marvel.data.api.models

data class RemoteCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail,
) {
}