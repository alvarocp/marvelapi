package es.i12capea.marvel

import es.i12capea.marvel.domain.entities.CharacterShortEntity
import es.i12capea.marvel.domain.entities.QueryResultsEntity
import es.i12capea.marvel.presentation.entities.CharacterShort
import es.i12capea.marvel.presentation.entities.QueryResults

fun provideSampleList(offset: Int) : QueryResultsEntity<CharacterShortEntity>{
    val list = ArrayList<CharacterShortEntity>()
    for (i in 1..20){
        list.add(
                CharacterShortEntity(
                        i,
                        "sample",
                        "route.jpg",
                )
        )
    }
    return QueryResultsEntity(
            100,
            offset,
            list
    )
}

fun testLoading(loadingCount: Int, it: Boolean){
    when (loadingCount) {
        1 -> {//Initial value, not loading
            assert(!it)
        }
        2 -> {//Loading
            assert(it)
        }
        3 -> {//Finished
            assert(!it)
        }
        else -> {
            assert(false)
        }
    }
}