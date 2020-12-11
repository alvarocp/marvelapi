package es.i12capea.marvel.presentation.entities

class QueryResults<T>(
        val total: Int?,
        val list: List<T>,
)