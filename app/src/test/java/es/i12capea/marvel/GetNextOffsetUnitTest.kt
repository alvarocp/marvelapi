package es.i12capea.marvel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import es.i12capea.marvel.domain.usecases.GetCharactersWithOffsetUseCase
import es.i12capea.marvel.presentation.characters.character_list.CharacterListViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4::class)
class GetNextOffsetUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        Dispatchers.setMain(Dispatchers.Unconfined)

        val episodesUseCase = mockk<GetCharactersWithOffsetUseCase>()

        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        unmockkAll()
    }

    @Test
    fun `01 totalquery and listsize the same, offset result should be null` (){
        val viewModel = spyk(CharacterListViewModel(
                getCharactersWithOffsetDummy(),
                Dispatchers.Unconfined
        ))

        every { viewModel["getTotalQueryResults"]() } returns 1200
        every { viewModel["getListSize"]() } returns 1200

        assert(viewModel.getNextOffset() == null)

    }

    @Test
    fun `02 totalquery lt listsize, offset result should be listsize` (){
        val viewModel = spyk(CharacterListViewModel(
                getCharactersWithOffsetDummy(),
                Dispatchers.Unconfined
        ))

        every { viewModel["getTotalQueryResults"]() } returns 1200
        every { viewModel["getListSize"]() } returns 800

        assert(viewModel.getNextOffset() == 800)

    }

    @Test
    fun `03 totalquery gt listsize, offset result should be null` (){
        val viewModel = spyk(CharacterListViewModel(
                getCharactersWithOffsetDummy(),
                Dispatchers.Unconfined
        ))

        every { viewModel["getTotalQueryResults"]() } returns 1200
        every { viewModel["getListSize"]() } returns 1201

        assert(viewModel.getNextOffset() == null)

    }

    @Test
    fun `04 totalquery null should return 0 offset` (){
        val viewModel = spyk(CharacterListViewModel(
                getCharactersWithOffsetDummy(),
                Dispatchers.Unconfined
        ))

        every { viewModel["getTotalQueryResults"]() } returns null
        every { viewModel["getListSize"]() } returns 1201

        assert(viewModel.getNextOffset() == 0)
    }

    private fun getCharactersWithOffsetDummy() : GetCharactersWithOffsetUseCase{
        return mockk<GetCharactersWithOffsetUseCase>()
    }
}