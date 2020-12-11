package es.i12capea.marvel.domain.entities

data class QueryResultsEntity<T>(
        val total: Int,
        val offset: Int,
        val list: List<T>,
)