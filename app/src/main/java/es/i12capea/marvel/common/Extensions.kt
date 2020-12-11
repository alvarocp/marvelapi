package es.i12capea.marvel.common

import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

fun String.md5(): ByteArray = MessageDigest.getInstance("MD5").digest(this.toByteArray(UTF_8))
fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }

