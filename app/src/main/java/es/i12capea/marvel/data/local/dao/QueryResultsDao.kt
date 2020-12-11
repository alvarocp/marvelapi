package es.i12capea.marvel.data.local.dao

import androidx.room.*
import es.i12capea.marvel.data.local.model.CharacterAndQueryResults
import es.i12capea.marvel.data.local.model.LocalQueryResults

@Dao
interface QueryResultsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQueryResult(queryResult: LocalQueryResults) : Long

    @Transaction
    @Query(
            """
    SELECT * FROM query_results
    WHERE `offset` == :offset
    """
    )
    fun searchQueryResultByOffset(offset: Int) : CharacterAndQueryResults?
}