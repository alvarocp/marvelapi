package es.i12capea.marvel.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class CharacterAndQueryResults(
    @Embedded
    val query: LocalQueryResults,
    @Relation(
        parentColumn = "offset",
        entityColumn = "offset"
    )
    val characters: List<LocalCharacterShort>
)