// (Row|Column)
class Move(val from: Pair<Int, Int>, val to: Pair<Int, Int>) {
    fun getPegToRemove(): Pair<Int, Int> = Pair((from.first + to.first) / 2, (from.second + to.second) / 2)

    override fun toString(): String {
        return "(${from.first}|${from.second}) => (${to.first}|${to.second})"
    }
}