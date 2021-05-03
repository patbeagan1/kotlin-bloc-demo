package util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Scope {
    fun io() = CoroutineScope(Dispatchers.IO)
    fun default() = CoroutineScope(Dispatchers.Default)
    fun main() = CoroutineScope(Dispatchers.Main)
    fun unconfined() = CoroutineScope(Dispatchers.Unconfined)
}