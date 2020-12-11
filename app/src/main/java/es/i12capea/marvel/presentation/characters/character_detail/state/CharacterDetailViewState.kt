package es.i12capea.marvel.presentation.characters.character_detail.state

import es.i12capea.marvel.presentation.entities.Character

data class CharacterDetailViewState(
    var character: Character? = null,
    var isImageLoaded: Boolean? = null,
) {
}