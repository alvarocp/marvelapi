package es.i12capea.marvel.presentation.characters.character_list.state

import android.os.Parcelable
import es.i12capea.marvel.presentation.entities.CharacterShort
import es.i12capea.marvel.presentation.entities.QueryResults

data class CharacterListViewState(
        var characters : QueryResults<CharacterShort>? = null,
        var layoutManagerState: Parcelable? = null,
) {
}