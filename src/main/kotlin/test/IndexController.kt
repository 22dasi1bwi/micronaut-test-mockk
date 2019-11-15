package test

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import javax.inject.Singleton

@Controller("/")
class IndexController(private val importantService: ImportantService) {

    @Get
    fun getData () = HttpResponse.ok(ResponseObject(importantService.retrieve("fin")))
}

data class ResponseObject(val message: String)

@Singleton
class ImportantService(private val repo: ImportantRepository, private val importantClient: ImportantClient){
    fun retrieve(fin: String): String = cacheUsing(repo, fin) {
        importantClient.retrieveSomethingImportant(fin)
    }
}

@Singleton
class ImportantClient {
    fun retrieveSomethingImportant(fin: String) = "ImportantFromClient - $fin"
}

@Singleton
open class ImportantRepository {
    fun findByFin(fin: String) : String? {
        return "ImportantFromCache - $fin"
    }
}

inline fun cacheUsing(repo: ImportantRepository, fin: String, onCacheMissing: () -> String) : String =
    repo.findByFin(fin) ?: onCacheMissing()
