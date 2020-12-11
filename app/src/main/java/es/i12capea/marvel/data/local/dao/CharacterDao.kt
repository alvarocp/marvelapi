package es.i12capea.marvel.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.i12capea.marvel.data.local.model.LocalCharacter

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: LocalCharacter) : Long

    @Query("""
        SELECT *
        FROM character
        WHERE `id` == :id
    """)
    fun getCharacterById(id: Int) : LocalCharacter?
}