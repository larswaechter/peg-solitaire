import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    simulate()
}

fun simulate() {
    var game = Solitaire()

    println("Starting game...")
    println(game)

    var counter = 0
    while (!game.hasWon()) {
        counter++

        print("Press any key to continue...")
        readLine()

        val move: Pair<Move?, Boolean>
        val ms = measureTimeMillis {
            move = game.dfs()
        }

        game = game.move(move.first!!)
        println("Playing move #$counter ${move.first} in $ms ms\n")

        println(game)
    }

    println("Game finished in $counter moves!")
}

fun performanceTest() {
    var game = Solitaire()

    println(game)

    val timeMS = measureTimeMillis {
        var counter = 0
        while (!game.hasWon()) {
            counter++
            val move: Pair<Move?, Boolean> = game.dfs()
            game = game.move(move.first!!)
        }
    }

    println(game)
    println("Done in $timeMS ms")
}