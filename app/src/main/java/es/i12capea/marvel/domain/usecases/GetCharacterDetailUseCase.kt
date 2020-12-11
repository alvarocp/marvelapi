package es.i12capea.marvel.domain.usecases

import es.i12capea.marvel.domain.entities.CharacterEntity
import es.i12capea.marvel.domain.repositories.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCharacterDetailUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int) : Flow<CharacterEntity>{
        return flow {
            repository.getCharacter(id)
                .flowOn(Dispatchers.IO)
                .collect {
                    emit(it)
                }
        }
    }
}