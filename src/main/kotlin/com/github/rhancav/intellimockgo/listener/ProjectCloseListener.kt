package com.github.rhancav.intellimockgo.listener

import com.github.rhancav.intellimockgo.cache.DirectoryAutoCompletionCache
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

class ProjectCloseListener : ProjectManagerListener {
    override fun projectClosed(project: Project) {
        DirectoryAutoCompletionCache.clearCache()
    }
}