package es.i12capea.marvel.data.repository

import android.util.Log
import es.i12capea.marvel.data.api.CharacterApi
import es.i12capea.marvel.data.api.call
import es.i12capea.marvel.data.local.dao.CharacterDao
import es.i12capea.marvel.data.local.dao.CharacterShortDao
import es.i12capea.marvel.data.local.dao.QueryResultsDao
import es.i12capea.marvel.data.mappers.*
import es.i12capea.marvel.domain.entities.CharacterEntity
import es.i12capea.marvel.domain.entities.CharacterShortEntity
import es.i12capea.marvel.domain.entities.QueryResultsEntity
import es.i12capea.marvel.domain.exceptions.RequestException
import es.i12capea.marvel.domain.repositories.CharacterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class CharacterRepositoryImpl @Inject constructor(
    private val characterApi: CharacterApi,
    private val queryResultsDao: QueryResultsDao,
    private val characterShortDao: CharacterShortDao,
    private val characterDao: CharacterDao,
) : CharacterRepository {
    override suspend fun getCharactersWithOffset(offset: Int): Flow<QueryResultsEntity<CharacterShortEntity>> {
        return flow {
            queryResultsDao.searchQueryResultByOffset(offset)?.let {
                emit(it.toDomainQueryResults())
            } ?: kotlin.run {
                try {
                    val result = characterApi.getCharactersWithOffset(offset).call()
                    val resultOffset = result.data.offset
                    val domainResult = result.data.toDomainCharacterShortQueryResults()

                    domainResult?.let { domResult ->
                        emit (domResult)

                        Thread{
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    characterShortDao.insertListCharacterShort(domainResult.list.toListLocalCharacterShort(resultOffset!!))
                                    Log.d("BD", "Base de datos actualizada en segundo plano")
                                    queryResultsDao.insertQueryResult(domainResult.toLocalQueryResults())
                                }catch (e: Exception){
                                    Log.d("BD", "No se ha podido insertar la página")
                                }
                            }
                        }.start()
                    }
                }catch (t: Throwable){
                    if (t !is RequestException){
                        throw t
                    } else {
                        null
                    }
                }

            }

        }

    }

    override suspend fun getCharacter(id: Int): Flow<CharacterEntity> {
        return flow {
            characterDao.getCharacterById(id)?.let {
                emit(it.toDomainCharacter())
            } ?: kotlin.run {
                try {
                    val result = characterApi.getCharacter(id).call()
                    val characterEntity = result.data.toDomainCharacter()
                    emit(characterEntity)
                    Thread{
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                characterDao.insert(characterEntity.toLocalCharacter())
                            }catch (e: Exception){
                                Log.d("BD", "No se ha podido insertar el personaje")
                            }
                        }
                    }.start()
                } catch (t: Throwable){
                    if (t !is RequestException){
                        throw t
                    }
                }
            }
        }
    }
}