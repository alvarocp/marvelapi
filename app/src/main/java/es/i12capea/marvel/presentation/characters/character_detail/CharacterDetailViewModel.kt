package es.i12capea.marvel.presentation.characters.character_detail

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.marvel.domain.usecases.GetCharacterDetailUseCase
import es.i12capea.marvel.presentation.characters.character_detail.state.CharacterDetailStateEvent
import es.i12capea.marvel.presentation.characters.character_detail.state.CharacterDetailViewState
import es.i12capea.marvel.presentation.common.BaseViewModel
import es.i12capea.marvel.presentation.entities.Character
import es.i12capea.marvel.presentation.entities.mappers.toPresentationCharacter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CharacterDetailViewModel @ViewModelInject constructor(
    private val getCharacterDetail : GetCharacterDetailUseCase,
    private val dispatcher: CoroutineDispatcher,
)  : BaseViewModel<CharacterDetailStateEvent, CharacterDetailViewState>(dispatcher){

    override fun getJobNameForEvent(stateEvent: CharacterDetailStateEvent): String? {
        return when(stateEvent){
            is CharacterDetailStateEvent.GetCharacterDetail -> {
                CharacterDetailStateEvent.GetCharacterDetail::class.java.name + stateEvent.id
            }
        }
    }

    override fun getJobForEvent(stateEvent: CharacterDetailStateEvent): Job? {
        return launch {
            when(stateEvent){
                is CharacterDetailStateEvent.GetCharacterDetail -> {
                    handleCharacterDetailFlow(stateEvent.id)
                }
            }
        }
    }

    override fun initNewViewState(): CharacterDetailViewState {
        return CharacterDetailViewState()
    }

    private suspend fun handleCharacterDetailFlow(id: Int) {
        try {
            getCharacterDetail(id)
                .flowOn(Dispatchers.IO)
                .collect {
                    setCharacter(it.toPresentationCharacter())
                }
        } catch (t: Throwable){
            handleThrowable(t)
        }
    }

    private fun setCharacter(character: Character){
        val update = getCurrentViewStateOrNew()
        update.character = character
        postViewState(update)
    }

    private fun getCharacter() : Character? {
        return getCurrentViewStateOrNew().character
    }

    fun isImageLoaded() : Boolean {
        return getCurrentViewStateOrNew().isImageLoaded ?:false
    }

    fun setImageLoad(isLoaded: Boolean){
        val update = getCurrentViewStateOrNew()
        update.isImageLoaded = isLoaded
        setViewState(update)
    }
}