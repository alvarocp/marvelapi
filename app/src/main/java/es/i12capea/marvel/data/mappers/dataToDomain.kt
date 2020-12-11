package es.i12capea.marvel.data.mappers

import android.util.Log
import es.i12capea.marvel.data.api.models.BaseData
import es.i12capea.marvel.data.api.models.BaseResults
import es.i12capea.marvel.data.api.models.RemoteCharacter
import es.i12capea.marvel.data.api.models.RemoteCharacterShort
import es.i12capea.marvel.data.local.model.CharacterAndQueryResults
import es.i12capea.marvel.data.local.model.LocalCharacter
import es.i12capea.marvel.data.local.model.LocalCharacterShort
import es.i12capea.marvel.domain.entities.CharacterEntity
import es.i12capea.marvel.domain.entities.CharacterShortEntity
import es.i12capea.marvel.domain.entities.QueryResultsEntity




fun BaseData<RemoteCharacter>.toDomainCharacter() : CharacterEntity{
    val httpsPath = "https" + this.results[0].thumbnail.path.substring(4, this.results[0].thumbnail.path.length)

    return CharacterEntity(
            this.results[0].id,
            this.results[0].name,
            this.results[0].description,
            httpsPath
    )
}

fun BaseData<RemoteCharacterShort>.toDomainCharacterShortQueryResults() : QueryResultsEntity<CharacterShortEntity>?{
    return if (total != null && offset!=null){
        QueryResultsEntity(
                total,
                offset,
                this.results.toDomainCharacterShortList()
        )
    } else null
}

fun <T> BaseData<T>.hasNext() : Boolean {
    return if (limit != null && total != null && offset != null && count != null){
        ((this.count + (limit * offset)) < total)
    }else {
        false
    }
}

fun List<RemoteCharacterShort>.toDomainCharacterShortList() : ArrayList<CharacterShortEntity>{
    val list = ArrayList<CharacterShortEntity>()
    for(item in this){
        list.add(item.toDomainCharacterShort())
    }
    return list
}

fun RemoteCharacterShort.toDomainCharacterShort() : CharacterShortEntity{
    val httpsPath = "https" + this.thumbnail.path.substring(4, this.thumbnail.path.length)
    val uri = httpsPath + "." + this.thumbnail.extension
    Log.d("img", uri)
    return CharacterShortEntity(
        this.id,
        this.name,
        uri
    )
}

fun LocalCharacterShort.toDomainCharacterShort() : CharacterShortEntity{
    return CharacterShortEntity(
            id = id,
            name = name,
            img = img,
    )
}

fun List<LocalCharacterShort>.toDomainListCharacterShort() : List<CharacterShortEntity> {
    val list = ArrayList<CharacterShortEntity>()
    for(item in this){
        list.add(item.toDomainCharacterShort())
    }
    return list.sortedBy { it.name }
}

fun CharacterAndQueryResults.toDomainQueryResults() : QueryResultsEntity<CharacterShortEntity> {
    return QueryResultsEntity(
            total = this.query.total,
            offset = this.query.offset,
            list = this.characters.toDomainListCharacterShort()
    )
}

fun LocalCharacter.toDomainCharacter() : CharacterEntity {
    return CharacterEntity(
            id = id,
            name = name,
            description = description,
            img = img,
    )
}