package es.i12capea.marvel.data.mappers

import es.i12capea.marvel.data.local.model.LocalCharacter
import es.i12capea.marvel.data.local.model.LocalCharacterShort
import es.i12capea.marvel.data.local.model.LocalQueryResults
import es.i12capea.marvel.domain.entities.CharacterEntity
import es.i12capea.marvel.domain.entities.CharacterShortEntity
import es.i12capea.marvel.domain.entities.QueryResultsEntity

fun CharacterShortEntity.toLocalCharacterShort(offset: Int) : LocalCharacterShort{
    return LocalCharacterShort(
            id = id,
            offset = offset,
            name = name,
            img = img
    )
}

fun List<CharacterShortEntity>.toListLocalCharacterShort(offset: Int) : List<LocalCharacterShort>{
    val list = ArrayList<LocalCharacterShort>()
    for (item in this) {
        list.add(item.toLocalCharacterShort(offset))
    }
    return list
}

fun QueryResultsEntity<CharacterShortEntity>.toLocalQueryResults() : LocalQueryResults{
    return LocalQueryResults(
                offset = offset,
                total = total
        )
}

fun CharacterEntity.toLocalCharacter() : LocalCharacter {
    return LocalCharacter(
            id = id,
            name = name,
            description = description,
            img = img,
    )
}