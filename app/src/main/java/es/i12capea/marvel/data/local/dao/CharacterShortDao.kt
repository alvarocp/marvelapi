package es.i12capea.marvel.data.local.dao

import androidx.room.*
import es.i12capea.marvel.data.local.model.LocalCharacterShort
import es.i12capea.marvel.presentation.entities.CharacterShort

@Dao
interface CharacterShortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(localCharacterShort: LocalCharacterShort) : Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListCharacterShort(episodes: List<LocalCharacterShort>)

}