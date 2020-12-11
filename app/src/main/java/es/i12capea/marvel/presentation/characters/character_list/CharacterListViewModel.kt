package es.i12capea.marvel.presentation.characters.character_list

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.marvel.domain.entities.CharacterShortEntity
import es.i12capea.marvel.domain.entities.QueryResultsEntity
import es.i12capea.marvel.domain.usecases.GetCharactersWithOffsetUseCase
import es.i12capea.marvel.presentation.characters.character_list.state.CharacterListStateEvent
import es.i12capea.marvel.presentation.characters.character_list.state.CharacterListViewState
import es.i12capea.marvel.presentation.common.BaseViewModel
import es.i12capea.marvel.presentation.entities.Character
import es.i12capea.marvel.presentation.entities.CharacterShort
import es.i12capea.marvel.presentation.entities.QueryResults
import es.i12capea.marvel.presentation.entities.mappers.toPresentationListCharacterShort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CharacterListViewModel @ViewModelInject constructor(
        private val getCharactersWithOffset: GetCharactersWithOffsetUseCase,
        private val dispatcher: CoroutineDispatcher
) :
    BaseViewModel<CharacterListStateEvent, CharacterListViewState>(dispatcher) {

    init {
        val update = getCurrentViewStateOrNew()
        update.characters = QueryResults(
                null,
                emptyList()
        )
        setViewState(update)
    }


    override fun getJobNameForEvent(stateEvent: CharacterListStateEvent): String {
        return when(stateEvent){
            is CharacterListStateEvent.GetNextCharacters -> {
                CharacterListStateEvent.GetNextCharacters::class.java.name + getNextOffset()
            }
        }
    }

    override fun getJobForEvent(stateEvent: CharacterListStateEvent): Job {
        return launch {
            when(stateEvent){
                is CharacterListStateEvent.GetNextCharacters -> {
                    getNextOffset()?.let {
                        handleCharactersWithOffsetFlow(it)
                    }
                }
            }
        }
    }

    override fun initNewViewState(): CharacterListViewState {
        return CharacterListViewState()
    }

    private suspend fun handleCharactersWithOffsetFlow(offset: Int){
        try {
            getCharactersWithOffset.invoke(offset)
                .flowOn(Dispatchers.IO)
                .collect {
                    handleCollectCharacterList(it)
                }
        } catch (t: Throwable){
            handleThrowable(t)
        }
    }

    private fun handleCollectCharacterList(queryResultsEntity: QueryResultsEntity<CharacterShortEntity>){

        val update = getCurrentViewStateOrNew()
        val list = update.characters?.list?.toMutableList()
                ?: arrayListOf()
        list.addAll(queryResultsEntity.list.toPresentationListCharacterShort())

        val results = QueryResults(
                queryResultsEntity.total?:update.characters?.total?:0,
                list
        )
        update.characters = results
        postViewState(update)
    }

    private fun getListSize() : Int{
        return getCurrentViewStateOrNew().characters?.list?.size ?: 0
    }

    private fun getTotalQueryResults(): Int? {
        return getCurrentViewStateOrNew().characters?.total
    }

    fun getNextOffset() : Int? {
        val size = getListSize()
        val total = getTotalQueryResults()
        var offset : Int? = null

        total?.apply {
            offset = if (size < this){
                size
            }else {
                null
            }
        } ?: kotlin.run { offset = 0 }

            return offset
    }

    fun getCharacterList() : List<CharacterShort>? {
        return getCurrentViewStateOrNew().characters?.list
    }
}