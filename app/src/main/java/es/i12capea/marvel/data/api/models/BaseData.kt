package es.i12capea.marvel.data.api.models

data class BaseData<T>(
    val offset: Int?,
    val limit: Int?,
    val total: Int?,
    val count: Int?,
    val results: List<T>
)