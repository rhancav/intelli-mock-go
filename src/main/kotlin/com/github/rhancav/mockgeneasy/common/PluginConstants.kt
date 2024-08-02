package com.github.rhancav.mockgeneasy.common

interface PluginConstants {
    interface UITexts {
        companion object {
            const val DESTINATION: String = "Destination:"
            const val PACKAGE_NAME: String = "Package Name:"
            const val DEFAULT_PACKAGE: String = "mocks"
            const val ACTION_TITLE: String = "Generate Mock"
        }
    }

    interface MockGen {
        companion object {
            const val COMMAND_TEMPLATE =
                "mockgen -source=\"%s\" -destination=\"%s\" -package=\"%s\""
            const val INTERFACE_REGEX = "(?s)\\btype\\s+\\w+\\s+interface\\s*\\{.*?}"
            const val GO_EXTENSION = "go"
            const val MOCK_SUFFIX = "_mock.go"
        }
    }

    interface Commands {
        companion object {
            const val GO_VERSION = "go version"
            const val GOMOCK_VERSION = "mockgen --version"
            val WIN_SHELL = arrayOf("cmd.exe", "/c")
            val UNIX_SHELL = arrayOf("/bin/sh", "-c")
        }
    }

    interface Notification {
        companion object {
            const val NOTIFICATION_TITLE: String = "Mockgen Easy"
            const val NOTIFICATION_GROUP: String = "mockgen-easy-notifications"
        }
    }
}
