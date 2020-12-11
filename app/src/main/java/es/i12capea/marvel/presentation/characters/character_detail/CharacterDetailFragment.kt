package es.i12capea.marvel.presentation.characters.character_detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.marvel.R
import es.i12capea.marvel.databinding.FragmentCharacterDetailBinding
import es.i12capea.marvel.presentation.characters.character_detail.state.CharacterDetailStateEvent
import es.i12capea.marvel.presentation.common.displayErrorDialog

@AndroidEntryPoint
class CharacterDetailFragment : Fragment() {

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    private val args: CharacterDetailFragmentArgs by navArgs()

    private val viewModel : CharacterDetailViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
            .addListener(object : Transition.TransitionListener{
                override fun onTransitionEnd(transition: Transition) {
                    viewModel.setImageLoad(true)
                }
                override fun onTransitionResume(transition: Transition) {}
                override fun onTransitionPause(transition: Transition) {}
                override fun onTransitionCancel(transition: Transition) {}
                override fun onTransitionStart(transition: Transition) {}
            })
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        adjustInset()

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        subscribeObservers()

        binding.expandedImage.apply {
            transitionName = args.characterId.toString()
            Glide.with(this)
                .load(args.characterImage.replace("\\", "/"))
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }
                })
                .into(this)
        }

        viewModel.setStateEvent(CharacterDetailStateEvent.GetCharacterDetail(args.characterId))

    }

    private fun subscribeObservers(){
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it){
                binding.scrollLayout.progressBar.visibility = View.VISIBLE
            }else{
                binding.scrollLayout.progressBar.visibility = View.INVISIBLE
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner,  { viewState ->
            viewState.character?.let {
                binding.scrollLayout.tvDescription.text = if (it.description.isEmpty()){
                    getString(R.string.empty_description_character)
                } else {
                    it.description
                }
                binding.scrollLayout.tvName.text = it.name
                binding.toolbar.title = it.name
            }
        })

        viewModel.error.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                displayErrorDialog(it.desc)
            }
        })
    }

    private fun adjustInset(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { _, insets ->
            (binding.toolbar.layoutParams as ViewGroup.MarginLayoutParams).topMargin = insets.systemWindowInsetTop
            insets.consumeSystemWindowInsets()
        }
    }

}