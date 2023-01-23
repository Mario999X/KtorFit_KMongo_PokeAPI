package db

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import mu.KotlinLogging
import org.litote.kmongo.KMongo

private val log = KotlinLogging.logger {}

object MongoDbManager {

    private lateinit var mongoClient: MongoClient
    lateinit var database: MongoDatabase

    init {
        log.debug("Inicializando conexion a MongoDB")
        // Aplicamos Hiraki para la conexión a la base de datos
        mongoClient = KMongo.createClient("mongodb://mongoadmin:mongopass@localhost/pokeTest?authSource=admin")
        database = mongoClient.getDatabase("pokeTest")
    }
}