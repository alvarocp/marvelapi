package es.i12capea.marvel.presentation.characters.character_list

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import es.i12capea.marvel.R
import es.i12capea.marvel.databinding.CharacterItemBinding
import es.i12capea.marvel.presentation.entities.CharacterShort

class CharacterListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding : CharacterItemBinding

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CharacterShort>() {

        override fun areItemsTheSame(
                oldItem: CharacterShort,
                newItem: CharacterShort
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
                oldItem: CharacterShort,
                newItem: CharacterShort
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.img == newItem.img
                    && oldItem.name == newItem.name
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CharacterViewHolder(
            binding,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<CharacterShort>) {
        differ.submitList(list)
    }

    class CharacterViewHolder
    constructor(
        private val binding: CharacterItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharacterShort) {
            binding.txtName.text = item.name
            binding.imgCharacter.apply {
                transitionName = item.id.toString()
                Glide.with(this)
                        .load(item.img)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(this)
            }

            binding.panel.setOnClickListener {
                val navController = it.findNavController()
                if (navController.currentDestination?.id == R.id.characterListFragment) {
                    //Este if arregla el bug del doble click antes de navegar.
                    val extras = FragmentNavigatorExtras(
                            binding.imgCharacter  to binding.imgCharacter.transitionName
                    )

                    val bundle = Bundle()

                    val direction =
                            CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
                                    characterId = item.id,
                                    characterImage = item.img
                            )
                    navController.navigate(direction, extras)
                }
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: CharacterShort, image: ImageView)
    }
}