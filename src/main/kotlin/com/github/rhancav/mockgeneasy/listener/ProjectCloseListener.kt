package com.github.rhancav.mockgeneasy.listener

import com.github.rhancav.mockgeneasy.cache.DirectoryAutoCompletionCache
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

class ProjectCloseListener : ProjectManagerListener {
    override fun projectClosed(project: Project) {
        DirectoryAutoCompletionCache.clearCache()
    }
}