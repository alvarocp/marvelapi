package es.i12capea.marvel.domain.entities

import es.i12capea.marvel.data.api.models.Thumbnail


data class CharacterEntity(
        val id: Int,
        val name: String,
        val description: String,
        val img: String,
)