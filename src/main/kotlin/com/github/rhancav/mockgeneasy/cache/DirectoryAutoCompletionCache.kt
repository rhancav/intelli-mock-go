package com.github.rhancav.mockgeneasy.cache

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.util.textCompletion.TextCompletionCache

object DirectoryAutoCompletionCache : TextCompletionCache<String> {
    private val cache: MutableSet<String> = HashSet()
    override fun setItems(items: MutableCollection<String>) {
        if (items.isNotEmpty()) {
            cache.addAll(items)
        }
    }

    override fun getItems(
        prefix: String,
        parameters: CompletionParameters?
    ): MutableCollection<String> {
        return cache.filter { it.startsWith(prefix) }.toMutableList()
    }

    override fun updateCache(prefix: String, parameters: CompletionParameters?) {

    }

    fun clearCache(){
        cache.clear()
    }
}