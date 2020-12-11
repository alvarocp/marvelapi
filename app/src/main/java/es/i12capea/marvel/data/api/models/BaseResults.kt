package es.i12capea.marvel.data.api.models

data class BaseResults<T>(
    val code: Int?,
    val status: String?,
    val data: BaseData<T>
)