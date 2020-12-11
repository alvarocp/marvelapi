package es.i12capea.marvel.presentation.characters.character_list

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.marvel.R
import es.i12capea.marvel.databinding.FragmentCharacterListBinding
import es.i12capea.marvel.presentation.characters.character_list.state.CharacterListStateEvent
import es.i12capea.marvel.presentation.common.displayErrorDialog
import es.i12capea.marvel.presentation.entities.CharacterShort

@AndroidEntryPoint
class CharacterListFragment : Fragment()
{

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    lateinit var characterListAdapter: CharacterListAdapter

    private val viewModel : CharacterListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        subscribeObservers()

        handleCharacters()
    }

    private fun subscribeObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.INVISIBLE
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner,  { viewState ->
            viewState.characters?.let {
                characterListAdapter.submitList(it.list)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                displayErrorDialog(it.desc)
            }
        })
    }

    private fun initRecyclerView(){
        binding.rvCharacters.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            characterListAdapter = CharacterListAdapter()
            adapter = characterListAdapter

            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager =
                            recyclerView.layoutManager as StaggeredGridLayoutManager
                    val lastPosition = IntArray(layoutManager.spanCount)
                    layoutManager.findLastVisibleItemPositions(lastPosition)
                    if (lastPosition[0] >= characterListAdapter.itemCount.minus(4)) {
                        viewModel.setStateEvent(CharacterListStateEvent.GetNextCharacters())
                        Log.d("A", "LastPositionReached")
                    }
                }
            })

            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    private fun handleCharacters() {
        viewModel.getCharacterList()?.let {
            if (it.isEmpty()){
                viewModel.setStateEvent(CharacterListStateEvent.GetNextCharacters())
            }else {
                //Hacer lo necesario cuando venga del back, en este caso nada
            }
        } ?: kotlin.run {
            viewModel.setStateEvent(CharacterListStateEvent.GetNextCharacters())
        }
    }
}