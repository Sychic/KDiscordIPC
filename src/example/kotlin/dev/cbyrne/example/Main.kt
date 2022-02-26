package dev.cbyrne.example

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.impl.CurrentUserUpdateEvent
import dev.cbyrne.kdiscordipc.core.event.impl.ErrorEvent
import dev.cbyrne.kdiscordipc.core.event.impl.ReadyEvent
import dev.cbyrne.kdiscordipc.data.activity.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val logger: Logger = LogManager.getLogger("Example")

suspend fun main() {
    val ipc = KDiscordIPC("945428344806183003")
    logger.info("Starting example!")

    ipc.activityManager.activity = activity("Hello", "world") {
        largeImage("https://avatars.githubusercontent.com/u/71222289?v=4", "KDiscordIPC")
        smallImage("https://avatars.githubusercontent.com/u/71222289?v=4", "Testing")

        party("test", listOf(1, 2))
        button("Click me", "https://google.com")
        timestamps(System.currentTimeMillis(), System.currentTimeMillis() + 50000)
    }

    ipc.on<ReadyEvent> {
        logger.info("Ready! (${data.user.username}#${data.user.discriminator})")

        // Subscribe to user updates
        ipc.userManager.subscribeToUserUpdates()

        // Get a specific user by ID
        val user = ipc.userManager.getUser("843135686173392946")
        logger.info("User by ID: $user")

        // Get the user's friend list
        val relationships = ipc.relationshipManager.getRelationships()
        logger.info("Relationships: ${relationships.size}")

        // Get an oauth token for the currently logged-in user
        val oauthToken = ipc.applicationManager.getOAuthToken()
        logger.info("Got Oauth token for application: ${oauthToken.application}")
    }

    ipc.on<ErrorEvent> {
        logger.error("IPC communication error (${data.code}): ${data.message}")
    }

    ipc.on<CurrentUserUpdateEvent> {
        logger.info("Current user updated!")
    }

    ipc.connect()
}