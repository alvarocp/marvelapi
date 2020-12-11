package es.i12capea.marvel.presentation.characters.character_detail.state

sealed class CharacterDetailStateEvent{
    class GetCharacterDetail(val id: Int) : CharacterDetailStateEvent()
}