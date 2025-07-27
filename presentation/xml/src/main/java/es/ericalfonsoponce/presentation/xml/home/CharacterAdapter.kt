package es.ericalfonsoponce.presentation.xml.home

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.presentation.xml.R
import es.ericalfonsoponce.presentation.xml.databinding.ItemCharacterBinding
import es.ericalfonsoponce.presentation.xml.databinding.ItemViewLoaderBinding

class CharacterAdapter(
    private val onClick: (character: CharacterShow) -> Unit,
    private val onDelete: (character: CharacterShow) -> Unit,
    private val loadNextPage: () -> Unit
) : ListAdapter<CharacterShow?, ViewHolder>(ListAdapterCallback()) {

    private var isLoading: Boolean = false

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is CharacterViewHolder) {
            getItem(position)?.let { character ->
                holder.bind(character)
            }
            if (position == currentList.size - 1 && !isLoading) {
                loadNextPage()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_CHARACTER) {
            CharacterViewHolder.from(parent, onClick, onDelete)
        } else {
            LoaderViewHolder.from(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null) VIEW_TYPE_LOADER else VIEW_TYPE_CHARACTER
    }

    fun showLoader() {
        isLoading = true
        submitList(currentList.plus(null))
    }

    fun finishPaginationLoader() {
        isLoading = false
    }

    class CharacterViewHolder(
        private val binding: ItemCharacterBinding,
        private val onClick: (character: CharacterShow) -> Unit,
        private val onDelete: (character: CharacterShow) -> Unit
    ) : ViewHolder(binding.root) {

        fun bind(character: CharacterShow) {
            with(binding) {
                this.character = character
                Glide.with(root)
                    .load(character.image)
                    .into(imageCharacter)

                textStatusPlace.text = getCharacterStatusColored(character)

                Glide.with(root)
                    .load(
                        when (character.gender) {
                            CharacterGender.FEMALE -> R.drawable.ic_gender_female
                            CharacterGender.MALE -> R.drawable.ic_gender_male
                            CharacterGender.UNKNOWN, CharacterGender.GENDERLESS -> R.drawable.ic_gender_undefined
                        }
                    )
                    .into(imageGender)

                imageDelete.setOnClickListener {
                    onDelete(character)
                }

                root.setOnClickListener {
                    onClick(character)
                }
            }
        }

        private fun getCharacterStatusColored(character: CharacterShow): SpannableString {
            val stateAndPlaceText = "${character.status.value} - ${character.location}"
            val spannableText = SpannableString(stateAndPlaceText)
            val selectedColor = binding.root.context.getColor(
                when (character.status) {
                    CharacterStatus.ALIVE -> R.color.alive
                    CharacterStatus.DEAD -> R.color.dead
                    CharacterStatus.UNKNOWN -> R.color.unknown
                }
            )
            val foregroundColorSpanGreen = ForegroundColorSpan(selectedColor)
            spannableText.setSpan(foregroundColorSpanGreen, 0, character.status.value.length, 0)
            return spannableText
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onClick: (character: CharacterShow) -> Unit,
                onDelete: (character: CharacterShow) -> Unit
            ): CharacterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCharacterBinding.inflate(layoutInflater, parent, false)
                return CharacterViewHolder(binding, onClick, onDelete)
            }
        }
    }

    class LoaderViewHolder private constructor(
        binding: ItemViewLoaderBinding,
    ) : ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): LoaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemViewLoaderBinding.inflate(layoutInflater, parent, false)
                return LoaderViewHolder(binding)
            }
        }
    }

    class ListAdapterCallback : DiffUtil.ItemCallback<CharacterShow?>() {
        override fun areItemsTheSame(oldItem: CharacterShow, newItem: CharacterShow): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CharacterShow, newItem: CharacterShow): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        const val VIEW_TYPE_CHARACTER = 0
        const val VIEW_TYPE_LOADER = 1
    }
}