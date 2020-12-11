package es.i12capea.marvel.presentation.entities.mappers

import es.i12capea.marvel.domain.entities.CharacterEntity
import es.i12capea.marvel.domain.entities.CharacterShortEntity
import es.i12capea.marvel.presentation.entities.Character
import es.i12capea.marvel.presentation.entities.CharacterShort

fun CharacterShortEntity.toPresentationCharacterShort() : CharacterShort{
    return CharacterShort(
            id = id,
            name = name,
            img = img,
    )
}

fun List<CharacterShortEntity>.toPresentationListCharacterShort() : List<CharacterShort> {
    val list = ArrayList<CharacterShort>()
    for (item in this){
        list.add(item.toPresentationCharacterShort())
    }
    return list
}

fun CharacterEntity.toPresentationCharacter() : Character{
    return Character(
        id = id,
        name = name,
        description = description,
        img = img
    )
}