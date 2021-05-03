import bloc.FlowBlocCollector
import util.Scope
import bloc.collector
import dataaccess.FavoritesRepositoryImpl
import dataaccess.FruitRepositoryImpl
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.pipeline.PipelineContext
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import logic.BlocFavorite
import logic.BlocFruit
import logic.BlocFruitFlow
import util.Failure
import util.Success

object Demos {
    private fun serverDemo() {
        val client = HttpClient(Java)
        CoroutineScope(Dispatchers.IO).launch {
            delay(5000)
            val s: String = client.get(port = 8000)
            println(s)
        }

        GlobalScope.launch {
            embeddedServer(Netty, port = 8000) {
                routing {
                    get("/", getRoot())
                }
            }.start(wait = true)
        }
        Thread.sleep(10000)
    }

    fun demos() {
        val r = FruitRepositoryImpl()
        flowBlocDemo(r)
        observableBlocDemo(r)
        Scope.default().let { it.launch { delayDemo() } }
        serverDemo()
    }

    private fun getRoot(): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit =
        {
            call.respondText("Hello, world!")
        }

    private fun flowBlocDemo(r: FruitRepositoryImpl) {
        val flowBloc = BlocFruitFlow(r, Dispatchers.Default)
        val flowBloc2 = BlocFruitFlow(r, Dispatchers.IO)
        val collector = FlowBlocCollector(
            flowBloc,
            flowBloc2
        ) {
            when (it) {
                is Failure -> println("error")
                is Success -> println(it.ok)
            }
        }

        flowBloc.process(BlocFruitFlow.Action.B)
        flowBloc.process(BlocFruitFlow.Action.A)
        flowBloc.process(BlocFruitFlow.Action.A)
        flowBloc2.process(BlocFruitFlow.Action.C("2"))
        flowBloc.process(BlocFruitFlow.Action.A)
        flowBloc.process(BlocFruitFlow.Action.B)
        flowBloc.process(BlocFruitFlow.Action.A)
        flowBloc.process(BlocFruitFlow.Action.B)
        flowBloc.process(BlocFruitFlow.Action.C("1"))
        Thread.sleep(100)
        collector.clear()
    }

    private suspend fun delayDemo() {
        Scope.io().apply {
            launch {
                repeat(5) {
                    delay(1000)
                    println("test")
                }
            }
        }
        GlobalScope.launch {
            delay(5000)
//        val s: String = client.get(port = 8000)
//        println(s)
        }
        runBlocking {
            flow {
                for (i in 1..3) {
                    delay(100) // pretend we are doing something useful here
                    emit(i) // emit next value
                }
            }.collect { value -> println(value) }
        }

        fun simple(): Flow<Int> = flow {
            println("Flow started")
            for (i in 1..3) {
                delay(100)
                emit(i)
            }
        }

        fun main() = runBlocking {
            println("Calling simple function...")
            val flow = simple()
            println("Calling collect...")
            flow.collect { value -> println(value) }
            println("Calling collect again...")
            flow.collect { value -> println(value) }
        }

        main()

        Thread.sleep(20000)
    }

    private fun observableBlocDemo(r: FruitRepositoryImpl) {
        val blocFruit = BlocFruit(r)
        val blocFruit2 = BlocFruit(r)
        val blocFavorite = BlocFavorite(FavoritesRepositoryImpl())

        collector(
                blocFruit,
                blocFruit2,
                blocFavorite,
            onEvent = { result ->
                when (result) {
                    is Failure -> println("Error!")
                    is Success -> {
                        when (val it = result.ok) {
                            is BlocFruit.StateObservable -> println(it.s)
                            is BlocFavorite.StateFavorites.StateFavoritesError -> println("Err" + it.s)
                            is BlocFavorite.StateFavorites.StateFavoritesSuccess -> println(it.id)
                        }
                    }
                }
            },
            scheduler = Schedulers.computation()
        )

        blocFruit.process(BlocFruit.ActionObservable.A)
        blocFruit.process(BlocFruit.ActionObservable.A)
        blocFruit2.process(BlocFruit.ActionObservable.B)
        blocFavorite.unfavorite(2)
        blocFavorite.favorite(5)
    }
}