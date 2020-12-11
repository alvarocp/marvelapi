package es.i12capea.marvel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import es.i12capea.marvel.domain.usecases.GetCharactersWithOffsetUseCase
import es.i12capea.marvel.presentation.characters.character_list.CharacterListViewModel
import es.i12capea.marvel.presentation.characters.character_list.state.CharacterListStateEvent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4::class)
class CharacterListVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        unmockkAll()
    }

    @Test
    fun `01 get characters`() {
        var eventCount = 0

        val getCharactersUseCase = getCharactersWithOffsetDummy(0)

        val characterListViewModel = CharacterListViewModel(
            getCharactersUseCase,
            Dispatchers.Unconfined
        )

        characterListViewModel.viewState.observeForever {
            eventCount++
            when(eventCount){
                1 -> assert(it.characters?.list?.size == 0) //Initial setValue
                2 -> assert(it.characters?.list?.size == 20) //Observed result
                else -> assert(false)
            }
        }

        characterListViewModel.setStateEvent(CharacterListStateEvent.GetNextCharacters())

        assert(eventCount == 2) //Initial and observed result

        coVerify { getCharactersUseCase(0) }
    }

    private fun getCharactersWithOffsetDummy(offset: Int) : GetCharactersWithOffsetUseCase{
        val useCase = mockk<GetCharactersWithOffsetUseCase>()
        coEvery { useCase(offset) } returns flow {
            emit(provideSampleList(offset))
        }
        return useCase
    }
}
