package indigo

import kotlin.random.Random

enum class Card(val symbol: String, val value: Int) {
    KING_SPADE("K♣", 13), QUEEN_SPADE("Q♣", 12), JACK_SPADE("J♣", 11),
    TEN_SPADE("10♣", 10), NINE_SPADE("9♣", 9), EIGHT_SPADE("8♣", 8),
    SEVEN_SPADE("7♣", 7), SIX_SPADE("6♣", 6), FIVE_SPADE("5♣", 5),
    FOUR_SPADE("4♣", 4), THREE_SPADE("3♣", 3), TWO_SPADE("2♣", 2),
    AS_SPADE("A♣", 14),
    KING_DIAMOND("K♦", 13), QUEEN_DIAMOND("Q♦", 12), JACK_DIAMOND("J♦", 11),
    TEN_DIAMOND("10♦", 10), NINE_DIAMOND("9♦", 9), EIGHT_DIAMOND("8♦", 8),
    SEVEN_DIAMOND("7♦", 7), SIX_DIAMOND("6♦", 6), FIVE_DIAMOND("5♦", 5),
    FOUR_DIAMOND("4♦", 4), THREE_DIAMOND("3♦", 3), TWO_DIAMOND("2♦", 2),
    AS_DIAMOND("A♦", 14),

    KING_HEART("K♥", 13), QUEEN_HEART("Q♥", 12), JACK_HEART("J♥", 11),
    TEN_HEART("10♥", 10), NINE_HEART("9♥", 9), EIGHT_HEART("8♥", 8),
    SEVEN_HEART("7♥", 7), SIX_HEART("6♥", 6), FIVE_HEART("5♥", 5),
    FOUR_HEART("4♥", 4), THREE_HEART("3♥", 3), TWO_HEART("2♥", 2),
    AS_HEART("A♥", 14),

    KING_CLUB("K♠", 13), QUEEN_CLUB("Q♠", 12), JACK_CLUB("J♠", 11),
    TEN_CLUB("10♠", 10), NINE_CLUB("9♠", 9), EIGHT_CLUB("8♠", 8),
    SEVEN_CLUB("7♠", 7), SIX_CLUB("6♠", 6), FIVE_CLUB("5♠", 5),
    FOUR_CLUB("4♠", 4), THREE_CLUB("3♠", 3), TWO_CLUB("2♠", 2),
    AS_CLUB("A♠", 14);

    fun colour(): Char {
        if (this.value == 10) return this.symbol[2]
        return this.symbol[1]
    }
}

class Player(val hand: MutableList<Card> = mutableListOf(), var score: Int = 0, val collection: MutableList<Card> = mutableListOf()) {
    fun winCards() {
        for (i in tableDeck) {
            if (i.value >= 10) this.score++
        }
        last = this
        this.collection.addAll(tableDeck)
        tableDeck.clear()
    }
}

val deck = Card.values().toMutableList()
val tableDeck = mutableListOf<Card>()
val computer = Player()
val playerOne = Player()
var last: Player = computer

fun main() {
    println("Indigo Card Game")
    startGame()
    println("Game Over")
}

fun startGame() {
    println("Play first?")
    val firstCommand = readln()
    var firstPlay: Boolean
    when {
        firstCommand == "exit" -> {
            println("Game Over")
            return
        }

        firstCommand.equals("yes", true) -> {
            firstPlay = true
        }

        firstCommand.equals("no", true) -> {
            firstPlay = false
        }

        else -> {
            startGame()
            return
        }
    }
    //shuffles deck by randomizing order of it's elements
    repeat(deck.size) {
        val element = deck[Random.nextInt(deck.lastIndex)]
        deck.remove(element)
        deck.add(element)
    }
    println("Initial cards on the table: ")
    repeat(4) {
        tableDeck.add(deck[it])
        print(deck[it].symbol + ' ')
        deck.removeAt(it)
    }
    if (firstPlay) last = playerOne
    if (firstPlay) playerTurn()
    else computerTurn()

}

fun playerTurn() {
    printStateMessage()
    if (deck.isEmpty() && playerOne.hand.isEmpty()) {
        last.winCards()
        if (computer.collection.size > playerOne.collection.size) computer.score += 3
        else playerOne.score += 3
        printScoreMessage()
        return
    }
    println("Cards in hand: ")
    if (playerOne.hand.isEmpty()) {
        repeat(6) {
            playerOne.hand.add(deck[0])
            deck.removeAt(0)
        }
    }// if empty, takes to player's deck 6 cards from deck
    for (i in 0..playerOne.hand.lastIndex) {
        print("${i + 1})${playerOne.hand[i].symbol} ")
    }
    var chosenCardIndex = -1
    do {
        var again = false
        println("\nChoose a card to play (1-${playerOne.hand.size}):")
        val numberOfCards = readln()

        if (numberOfCards == "exit") return
        chosenCardIndex = try {
            numberOfCards.toInt() - 1
        } catch (e: Exception) {
            -1
        }

        if (chosenCardIndex !in 0 until playerOne.hand.size) {
            again = true
        }
    } while (again)

    if (tableDeck.isEmpty() || !(tableDeck.last().value == playerOne.hand[chosenCardIndex].value ||
                    tableDeck.last().colour() == playerOne.hand[chosenCardIndex].colour())) {
        tableDeck.add(playerOne.hand[chosenCardIndex])
    } else {
        tableDeck.add(playerOne.hand[chosenCardIndex])
        println("Player wins cards")
        playerOne.winCards()
        printScoreMessage()
    }
    playerOne.hand.removeAt(chosenCardIndex)
    computerTurn()
}

fun computerTurn() {
    printStateMessage()
    if (deck.isEmpty() && computer.hand.isEmpty()) {
        last.winCards()
        if (playerOne.collection.size > computer.collection.size) playerOne.score += 3
        else computer.score += 3
        printScoreMessage()
        return
    }
    if (computer.hand.isEmpty()) {
        repeat(6) {
            computer.hand.add(deck[0])
            deck.removeAt(0)
        }
    }
    val bestCard = bestCard(computer.hand)

    for (i in computer.hand) { // for testing purpose
        print(i.symbol + " ")
    }
    println()

    println("Computer plays ${bestCard.symbol}")

    if (tableDeck.isEmpty() || !(tableDeck.last().value == bestCard.value ||
                    tableDeck.last().colour() == bestCard.colour())) {
        tableDeck.add(bestCard)
    } else {
        tableDeck.add(bestCard)
        println("Computer wins cards")
        computer.winCards()
        printScoreMessage()
    }
    computer.hand.remove(bestCard)
    playerTurn()
}

fun printStateMessage() {
    if (tableDeck.isEmpty()) println("No cards on the table")
    else println("\n${tableDeck.size} cards on the table, and the top card is ${tableDeck.last().symbol}")

}

fun printScoreMessage() {
    println("Score: Player ${playerOne.score} - Computer ${computer.score}")
    println("Cards: Player ${playerOne.collection.size} - Computer ${computer.collection.size}")
}

fun bestCard(cards: MutableList<Card>): Card {
    // if only one card is available return it
    if (cards.size == 1) return cards[0]
    // if table is empty apply mostImportant function's logic
    if (tableDeck.isEmpty()) return mostImportant(cards)
    // candidates are available cards that could win a tour
    val candidates = cards.filter { it.colour() == tableDeck.last().colour() || it.value == tableDeck.last().value }
    // if there are no candidates apply mostImportant function's logic
    if (candidates.isEmpty()) return mostImportant(cards)
    // if there is only one of candidates return it
    if (candidates.size == 1) return candidates[0]
    // else return mostImportant function's logic applied on candidates
    return mostImportant(candidates.toMutableList())
}


fun mostImportant(cards: MutableList<Card>): Card {
    var spade = false
    var heart = false
    var diamond = false
    var club = false
    // for all available cards return doubled colour card
    for (card in cards) when (card.colour()) {
        '♣' -> {
            if (spade) return card
            spade = true
        }

        '♥' -> {
            if (heart) return card
            heart = true
        }

        '♦' -> {
            if (diamond) return card
            diamond = true
        }

        '♠' -> {
            if (club) return card
            club = true
        }
    }
    // else for all available cards return doubled rank card
    val list = mutableListOf<Int>()
    for (i in cards) {
        if (list.contains(i.value)) return i
        list.add(i.value)
    }
    //else return first available card
    return cards[0]
}