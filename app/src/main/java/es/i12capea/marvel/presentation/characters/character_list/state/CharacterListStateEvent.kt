package es.i12capea.marvel.presentation.characters.character_list.state

sealed class CharacterListStateEvent{
    class GetNextCharacters(): CharacterListStateEvent()
}