package es.i12capea.marvel.data.api

import es.i12capea.marvel.common.Constants
import es.i12capea.marvel.data.api.models.BaseData
import es.i12capea.marvel.data.api.models.BaseResults
import es.i12capea.marvel.data.api.models.RemoteCharacter
import es.i12capea.marvel.data.api.models.RemoteCharacterShort
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApi {

    @GET("characters")
    fun getCharactersWithOffset(@Query("offset") page: Int, @Query("limit") limit: Int = Constants.MAX_ITEM_PER_PAGE) : Call<BaseResults<RemoteCharacterShort>>

    @GET("characters/{characterId}")
    fun getCharacter(@Path("characterId") characterId: Int) : Call<BaseResults<RemoteCharacter>>

}