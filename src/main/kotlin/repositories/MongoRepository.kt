package repositories

import db.MongoDbManager
import models.pokemonIndividual.Pokemon
import org.litote.kmongo.*

class MongoRepository : CrudRepository<Pokemon, String> {

    override fun findAll(): List<Pokemon> {
        println("\t📖📖findAll")
        return MongoDbManager.database.getCollection<Pokemon>().find().toList()
    }

    override fun delete(entity: Pokemon): Boolean {
        println("\t👉👉delete")
        var existe = false
        if (MongoDbManager.database.getCollection<Pokemon>().deleteOneById(entity.id).equals(entity)) {
            existe = true
        }
        return existe
    }

    override fun update(entity: Pokemon): Pokemon {
        TODO("Not yet implemented")
    }

    override fun save(entity: Pokemon): Pokemon {
        println("\t✍✍Saving $entity")
        MongoDbManager.database.getCollection<Pokemon>().save(entity)
        return entity
    }

    override suspend fun findById(id: String): Pokemon? {
        println("\t🔎🔎findById")
        return MongoDbManager.database.getCollection<Pokemon>().findOneById(id)
    }
}