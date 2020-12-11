package es.i12capea.marvel.domain.repositories

import es.i12capea.marvel.domain.entities.CharacterEntity
import es.i12capea.marvel.domain.entities.CharacterShortEntity
import es.i12capea.marvel.domain.entities.QueryResultsEntity
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharactersWithOffset(offset: Int) : Flow<QueryResultsEntity<CharacterShortEntity>>
    suspend fun getCharacter(id: Int) : Flow<CharacterEntity>
}