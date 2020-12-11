package es.i12capea.marvel.domain.usecases

import es.i12capea.marvel.domain.entities.CharacterShortEntity
import es.i12capea.marvel.domain.entities.QueryResultsEntity
import es.i12capea.marvel.domain.repositories.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCharactersWithOffsetUseCase(
    private val repository: CharacterRepository
) {
     suspend operator fun invoke(offset: Int) : Flow<QueryResultsEntity<CharacterShortEntity>> {
         return flow {
             repository.getCharactersWithOffset(offset)
                 .flowOn(Dispatchers.IO)
                 .collect {
                     emit(it)
                 }
         }
     }
}