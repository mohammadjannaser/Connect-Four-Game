package connectfour

fun main() {

    val connectFour = ConnectFour()
    connectFour.playGame()

}

enum class PlayerTurn { FIRST_PLAYER_TURN, SECOND_PLAYER_TURN }


class ConnectFour {

    private var numberOfGame: Int = 1
    private val sign = mutableListOf("║", "╚", "═", "╩", "╝", " ")
    private val playerSign = mutableListOf("o", "*")

    private val firstPlayerWinCondition = "oooo"
    private val secondPlayerWinCondition = "****"

    private lateinit var firstPlayerName: String
    private lateinit var secondPlayerName: String

    private var firstPlayerScore = 0
    private var secondPlayerScore = 0

    private var numberOfRows = 6
    private var numberOfCols = 7

    private var playerTurn = PlayerTurn.FIRST_PLAYER_TURN
    private var board = MutableList(0) { MutableList(0) { " " } }


    fun playGame() {

        getPlayersInfo()
        while (true) {
            if (isBoardDimensionsSet()) break
        }
        multipleGame()
        displayPlayers()
        displayTotalGame()

        first@ for (game in 1..numberOfGame) {

            if (numberOfGame > 1) currentGame(game)
            drawTheBoard()
            displayBoard()


            second@ while (true) {

                // if true it means user has entered a valid input.
                // if false it means user has not entered a valid input or typed end.
                val input = askForDiceInput()

                if (input) {
                    displayBoard()
                    val winner = winningCondition()
                    if (winner != null) {
                        println(winner)
                        break
                    }
                }
                else break@first
            }

            if (numberOfGame > 1) gameScore()
        }

        gameOver()
    }


    private fun drawTheBoard() {

        board.clear()

        for (row in 0..numberOfRows + 1) {
            val rowBoard = MutableList(0) { " " }
            for (col in 0..numberOfCols * 2) {
                when {
                    row == 0 && col % 2 == 1 -> rowBoard.add(((col / 2) + 1).toString())
                    row == 0 -> rowBoard.add(sign[5])
                    row == numberOfRows + 1 && col == 0 -> rowBoard.add(sign[1])
                    row == numberOfRows + 1 && col == numberOfCols * 2 -> rowBoard.add(sign[4])
                    row == numberOfRows + 1 && col % 2 == 1 -> rowBoard.add(sign[2])
                    row == numberOfRows + 1 && col % 2 == 0 -> rowBoard.add(sign[3])
                    col % 2 == 0 -> rowBoard.add(sign[0])
                    else -> rowBoard.add(sign[5])
                }
            }
            board.add(rowBoard)
        }

    }

    private fun displayBoard() {
        for (row in board) {
            println(row.joinToString(""))
        }
    }

    /**
     * Now, it is time to implement the winning condition. A player wins when they place four discs of the same
     * color in a row horizontally, vertically, or diagonally. After each move, the program checks if the condition
     * is met. Also, if the board is full and the win condition isn't fulfilled, claim it a draw.
     ******************************************************************************************************************/
    private fun winningCondition(): String? {

        // check every condition for the rows
        for (row in board) {
            if (row.filter { it == "*" || it == "o" }.joinToString("").contains(firstPlayerWinCondition)) {
                firstPlayerScore += 2
                return "Player $firstPlayerName won "
            } else if (row.filter { it == "*" || it == "o" }.joinToString("").contains(secondPlayerWinCondition)) {
                secondPlayerScore += 2
                return "Player $secondPlayerName won "
            }
        }

        // now check for winning condition in columns
        // first assign all value in a string then check only string
        for (col in 1..numberOfCols * 2 step 2) {
            val allColInputs = MutableList(0) { "" }
            // first assign all inputs in one variable then check the inputs
            for (row in 1..numberOfRows) {
                allColInputs.add(board[row][col])
            }

            // now check for winning condition of first player and second player
            if (allColInputs.joinToString("").contains(firstPlayerWinCondition)) {
                firstPlayerScore += 2
                return "Player $firstPlayerName won "
            } else if (allColInputs.joinToString("").contains(secondPlayerWinCondition)) {
                secondPlayerScore += 2
                return "Player $secondPlayerName won "
            }
        }

        var firstLoopRowIndex = numberOfRows
        var firstLoopColStartIndex = 1
        var firstLoopColEndIndex = 1


        // now check winning condition diagonally
        // (1,1) (1,2 2,1) (1,3 2,2 3,1) (1,4 2,3 3,2 4,1) (1,5 2,4 3,3 4,2 5,1) winning condition pattern
        // first initialize two variable one for row and one for colum
        // start iterating from the 4 column because before that there will be no winner
        for (row in 1 until numberOfRows + numberOfCols) {
            // col start end finish
            val diagonalInputs = MutableList(0) { " " }

            var innerRow = firstLoopRowIndex
            for (col in firstLoopColStartIndex downTo firstLoopColEndIndex) {
                diagonalInputs.add(board[innerRow][col * 2 - 1])
                innerRow--
            }
            if (row > numberOfCols) firstLoopRowIndex--
            if (row > numberOfRows) firstLoopColEndIndex++
            if (firstLoopColStartIndex < numberOfCols) firstLoopColStartIndex++

            // now check for winning condition of first player and second player
            if (diagonalInputs.joinToString("").contains(firstPlayerWinCondition)) {
                firstPlayerScore += 2
                return "Player $firstPlayerName won "
            } else if (diagonalInputs.joinToString("").contains(secondPlayerWinCondition)) {
                secondPlayerScore += 2
                return "Player $secondPlayerName won "
            }
        }


        firstLoopRowIndex = numberOfRows
        firstLoopColStartIndex = numberOfCols
        firstLoopColEndIndex = numberOfCols
        // now check winning condition diagonally
        // (1,1) (1,2 2,1) (1,3 2,2 3,1) (1,4 2,3 3,2 4,1) (1,5 2,4 3,3 4,2 5,1) winning condition pattern
        // first initialize two variable one for row and one for colum
        // start iterating from the 4 column because before that there will be no winner
        for (row in 1 until numberOfRows + numberOfCols) {
            // col start end finish
            val diagonalInputs = MutableList(0) { " " }

            var innerRow = firstLoopRowIndex
            for (col in firstLoopColStartIndex..firstLoopColEndIndex) {
                diagonalInputs.add(board[innerRow][col * 2 - 1])
                innerRow--
            }

            if (row < numberOfCols) firstLoopColStartIndex--
            if (row > numberOfRows) firstLoopColEndIndex--
            if (row > numberOfCols) firstLoopRowIndex--

            // now check for winning condition of first player and second player
            if (diagonalInputs.joinToString("").contains(firstPlayerWinCondition)) {
                firstPlayerScore += 2
                return "Player $firstPlayerName won "
            } else if (diagonalInputs.joinToString("").contains(secondPlayerWinCondition)) {
                secondPlayerScore += 2
                return "Player $secondPlayerName won "
            }
        }

        // now check if the board is full or not
        // if the board is full and there is no winner so output It is a draw

        if (isBoardFull()) {
            firstPlayerScore += 1
            secondPlayerScore += 1
            return "It is a draw"
        }


        return null

    }

    private fun askForDiceInput(): Boolean {

        while (true) {
            println("${if (playerTurn == PlayerTurn.FIRST_PLAYER_TURN) firstPlayerName else secondPlayerName}'s turn:")
            val columnNumber = readln()
            if (columnNumber == "end") return false

            if (insertDiceValidation(columnNumber)) return true
        }

    }

    private fun gameScore() = println("Score\n$firstPlayerName: $firstPlayerScore $secondPlayerName: $secondPlayerScore ")

    private fun insertDiceValidation(columnNumber: String): Boolean {

        if (columnNumber.toIntOrNull() == null) {
            // column number is not valid integer the function toIntOrNull will return null
            println("Incorrect column number")
            return false
        } else if (columnNumber.toInt() !in 1..numberOfCols) {
            println("The column number is out of range (1 - $numberOfCols)")
            return false
        } else if (insertDice(columnNumber.toInt()) == -1) {
            println("Column $columnNumber is full")
            return false
        }

        return true
    }

    /**
     * This function is responsible to insert the dice for the particular column
     * if the insertion done it will return the last inserted row else it will return -1
     * it means there is not empty space to insert the dice
     ******************************************************************************************************************/
    private fun insertDice(columnNumber: Int): Int {
        // check all the row from the bottom to top if there is no sign in any of them
        // just return the row number else -1 it means no empty space
        for (row in numberOfRows downTo 1) {
            if (board[row][columnNumber * 2 - 1] !in playerSign) {
                board[row][columnNumber * 2 - 1] =
                    if (playerTurn == PlayerTurn.FIRST_PLAYER_TURN) playerSign[0] else playerSign[1]
                playerTurn =
                    if (playerTurn == PlayerTurn.FIRST_PLAYER_TURN) PlayerTurn.SECOND_PLAYER_TURN else PlayerTurn.FIRST_PLAYER_TURN
                return row
            }
        }
        return -1
    }

    private fun multipleGame() {

        while (true) {
            println(
                """
                    Do you want to play single or multiple games?
                    For a single game, input 1 or press Enter
                    Input a number of games:
                """.trimIndent()
            )

            val numOfGame = readln()
            if (numOfGame.isNotEmpty()) {
                if ((numOfGame.toIntOrNull() ?: 0) < 1) {
                    println("Invalid input")
                }
                else {
                    numberOfGame = numOfGame.toInt()
                    break
                }
            } else {
                numberOfGame = 1
                break
            }
        }

    }

    private fun isBoardFull(): Boolean {

        for (col in 1..numberOfCols) {
            if (board[1][col * 2 - 1] !in playerSign) {
                return false
            }
        }
        return true
    }


    private fun displayPlayers() {
        println(String.format("%s VS %s", firstPlayerName, secondPlayerName))
        println("$numberOfRows X $numberOfCols board")
    }

    private fun displayTotalGame() {
        println(if (numberOfGame == 1) "Single game" else "Total $numberOfGame games")
    }

    private fun currentGame(currentGame: Int) = println("Game #$currentGame")



    private fun gameOver() = println("Game over!")

    private fun getPlayersInfo() {
        println("Connect Four")
        println("First player's name:")
        firstPlayerName = readln()
        println("Second player's name:")
        secondPlayerName = readln()

    }

    private fun isBoardDimensionsSet(): Boolean {

        println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")
        val boardDimensions = readln()

        return if (boardDimensions.isNotEmpty()) {
            setDimensions(removeSpaces(boardDimensions.lowercase()))
        } else true // the default will be chosen so break the loop

    }

    private fun removeSpaces(input: String) = input.replace("\\s".toRegex(), "")

    private fun setDimensions(dimension: String): Boolean {


        val regex = Regex("[0-9]+ ?x ?[0-9]+")
        if (!dimension.matches(regex)) {
            println("Invalid Input")
            return false
        } else if (dimension.substringBefore("x").first().digitToIntOrNull() !in 5..9) {
            println("Board rows should be from 5 to 9")
            return false
        } else if (dimension.substringAfter("x").last().digitToIntOrNull() !in 5..9) {
            println("Board columns should be from 5 to 9")
            return false
        }

        // Initialization here...
        numberOfRows = dimension.substringBefore("x").toInt()
        numberOfCols = dimension.substringAfter("x").toInt()

        return true
    }
}
