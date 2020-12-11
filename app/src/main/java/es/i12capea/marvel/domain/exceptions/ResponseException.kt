package es.i12capea.marvel.domain.exceptions

data class ResponseException(val code: Int, val desc: String) : Throwable() {
}