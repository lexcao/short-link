package io.github.lexcao.shortlink.server

import com.mongodb.ConnectionString
import io.github.lexcao.shortlink.common.Link
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

object Store {

    private val mongo: CoroutineCollection<Link>

    init {
        val mongoURI = System.getenv("MONGODB_URI") ?: "mongodb://localhost/"
        val connection = ConnectionString("$mongoURI?retryWrites=false")
        val client = KMongo.createClient(connection).coroutine
        val database = client.getDatabase(connection.database ?: "short-link")
        mongo = database.getCollection("links")
    }

    suspend fun save(link: Link) {
        mongo.save(link)
    }

    suspend fun findByName(name: String): Link? = mongo.findOne(Link::name eq name)
}