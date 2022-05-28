package com.example

import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

//fun main() {
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
//        configureRouting()
//    }.start(wait = true)
//}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
//    val databaseUrl = environment.config.propertyOrNull("ktor.deployment.databaseurl")?.getString() ?: ""
//    val database = Database.connect(url = "jdbc:$databaseUrl", driver = "org.postgresql.Driver")
    val database= Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
    TransactionManager.defaultDatabase = database
    runBlocking {
        newSuspendedTransaction {
            SchemaUtils.create(StarWarsFilms)
            addLogger(StdOutSqlLogger)
            StarWarsFilm.new {
                name = "The Last Jedi ${System.currentTimeMillis()}"
            }
            StarWarsFilm.all().forEach {
                print("Films == ${it.name}")
            }
        }
    }

//    configureRouting()
}

class StarWarsFilm(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StarWarsFilm>(table = StarWarsFilms)
    var name by StarWarsFilms.name
}

object StarWarsFilms : IntIdTable() {
    val name: Column<String> = varchar("name", 50)
}
