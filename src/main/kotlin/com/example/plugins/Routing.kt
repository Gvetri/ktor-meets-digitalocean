package com.example.plugins

import com.example.StarWarsFilm
import com.example.StarWarsFilms
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {

    routing {
        get("/") {
            val info = getFromDatabase()
            call.respondText("Hello World! $info")
        }
    }
}

suspend fun getFromDatabase(): List<StarWarsFilm> {
    val films = suspendedTransactionAsync(context = Dispatchers.IO) {
        StarWarsFilm.all()
    }.await()
    return films.toList()
}
