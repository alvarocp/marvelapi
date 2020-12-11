package es.i12capea.marvel.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import es.i12capea.marvel.common.Constants
import es.i12capea.marvel.data.local.dao.CharacterDao
import es.i12capea.marvel.data.local.dao.CharacterShortDao
import es.i12capea.marvel.data.local.dao.QueryResultsDao
import es.i12capea.marvel.data.local.model.LocalCharacter
import es.i12capea.marvel.data.local.model.LocalCharacterShort
import es.i12capea.marvel.data.local.model.LocalQueryResults

@Database(
        entities = [
            LocalCharacterShort::class,
            LocalQueryResults::class,
            LocalCharacter::class,
        ],
        version = Constants.DB_VERSION
)
abstract class MarvelDatabase : RoomDatabase() {

    abstract fun getCharacterShortDao() : CharacterShortDao
    abstract fun getQueryResultsDao() : QueryResultsDao
    abstract fun getCharacterDao() : CharacterDao

}