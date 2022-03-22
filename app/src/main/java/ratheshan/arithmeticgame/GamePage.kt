package ratheshan.arithmeticgame

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game_page.*


class GamePage : AppCompatActivity() {
    companion object {
        //  Global Variable declaration
        val operators = listOf("-", "+", "*", "/")
        var answer = ""
        var answerCheck = ""
        var left_string = ""
        var right_string = ""
        var conditionR: Boolean = false
        var conditionL: Boolean = false
        var correctAnswers: Int = 0
        var wrongAnswers: Int = 0
        var totalCounts: Int = 0
        var solvedAnswer = 0
        var left_temp = 0
        var right_temp = 0
        val initialCountdown: Long = 50000
        val countDownInterval: Long = 1000
        var timeLeftOnTimer: Long = 50000
        val additionalTime: Long = 10000
    }

    lateinit var greaterBtn: Button
    lateinit var equalBtn: Button
    lateinit var lessBtn: Button
    lateinit var rightOperation: TextView
    lateinit var leftOperation: TextView
    lateinit var correctText: TextView
    lateinit var wrongText: TextView
    lateinit var countTime: TextView
    lateinit var resultView: TextView
    lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_page)

        greaterBtn = findViewById(R.id.greater_btn)
        equalBtn = findViewById(R.id.equal_btn)
        lessBtn = findViewById(R.id.less_btn)
        rightOperation = findViewById(R.id.right_text)
        leftOperation = findViewById(R.id.left_text)
        correctText = findViewById(R.id.correct_text)
        wrongText = findViewById(R.id.wrong_text)
        countTime = findViewById(R.id.timer_text)
        resultView = findViewById(R.id.result_text)

        newRound()
        startTimer()
    }

    /**
     * Saving initial states of the data using savedInstances
     * **/
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("savedString1",left_string)
        outState.putString("savedString2",  right_string)
        outState.putLong("savedTime", timeLeftOnTimer)
        outState.putInt("savedCorrectAnswer", correctAnswers)
        outState.putInt("savedWrongAnswer", wrongAnswers)
    }

    /**
     * Retrieving initial states of the data using restoreInstances
     * **/
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val leftString = savedInstanceState.getString("savedString1", "")
        val rightString = savedInstanceState.getString("savedString2", "")
        val timeLeft = savedInstanceState.getLong("savedTime", 0)
        val corrects = savedInstanceState.getInt("savedCorrectAnswer", 0)
        val wrongs = savedInstanceState.getInt("savedWrongAnswer", 0)

        left_string = leftString
        right_string = rightString
        rightOperation.text =  right_string
        leftOperation.text = left_string
        timeLeftOnTimer = timeLeft
        correctAnswers = corrects
        wrongAnswers = wrongs
    }


    /**
     * function to calculate expressions **/
    fun calculateMath(x: Int, y: Int, operator: String): Int {
        when (operator) {
            "+" -> {
                return x + y
            }
            "-" -> {
                return x-y
            }
            "*" -> {
                return x*y
            }
            "/" -> {
                return x/y
            }
            else -> {
                return 0
            }
        }
    }


    /**
     * New round function
     * **/
    private fun newRound(){
        conditionR = false
        conditionL = false

        // Listeners for the button inputs
        greaterBtn.setOnClickListener{
            ++totalCounts
            correct_text.visibility = View.INVISIBLE
            wrong_text.visibility = View.INVISIBLE
            answerCheck = "greater"
            updateScore()
        }

        equalBtn.setOnClickListener{
            ++totalCounts
            correct_text.visibility = View.INVISIBLE
            wrong_text.visibility = View.INVISIBLE
            answerCheck = "equal"
            updateScore()
        }

        lessBtn.setOnClickListener{
            ++totalCounts
            correct_text.visibility = View.INVISIBLE
            wrong_text.visibility = View.INVISIBLE
            answerCheck = "lesser"
            updateScore()
        }
        generateOperations()
        checkAnswers()
    }


    /**
     * Function that generates left and right random strings
     * **/
    private fun generateOperations(){
        val leftExpressionCount = (1..4).random()
        val rightExpressionCount = (1..4).random()
        val operatorList = mutableListOf<String>()

        // Function for single string expressions
        fun oneTerms(): String {
            var term_one:   Int = (1..21).random()
            var statement: String = "$term_one"
            solvedAnswer = term_one
            return statement
        }

        // Function for two terms expressions
        fun twoTerms(): String {
            val operator1 = operators.random()
            val num1: Int = (1..21).random()
            var num2: Int = (1..21).random()

            when (operator1) {
                "+" -> {
                    while (num1 + num2 >= 100){
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }

                "-" -> {
                    while (num2 > num1){
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }

                "*" -> {
                    while (num1 * num2 >= 100) {
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }

                "/" -> {
                    while (num1 % num2 != 0){
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }
            }
            return "($num1 ${operator1} $num2)"
        }

        // Function for three terms expressions
        fun threeTerms(): String {
            val operator1 = operators.random()
            val operator2 = operators.random()
            val num1: Int = (1..21).random()
            var num2: Int = (1..21).random()
            var num3: Int = (1..21).random()


            when (operator1) {
                "+" -> {
                    while (num1 + num2 >= 100){
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }

                "-" -> {
                    while (num2 > num1){
                        num2 = (1..num1).random()
                    }
                    solvedAnswer= calculateMath(num1, num2, operator1)
                }

                "*" -> {
                    while (num1 * num2 >= 100) {
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }

                "/" -> {
                    while (num1 % num2 != 0){
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }
            }
            when (operator2){

                "+" -> {
                    while (solvedAnswer  + num3 >= 100){
                        num3 = (1..solvedAnswer ).random()
                    }
                    solvedAnswer  = calculateMath(solvedAnswer, num3, operator1)
                }

                "-" -> {
                    while (num3 > solvedAnswer ){
                        num3 = (1..solvedAnswer ).random()
                    }
                    solvedAnswer = calculateMath(solvedAnswer, num3, operator1)
                }

                "*" -> {
                    while (solvedAnswer  * num3 >= 100) {
                        num3 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(solvedAnswer, num3, operator1)
                }

                "/" -> {
                    while (solvedAnswer  % num3 != 0){
                        num3 = (1..solvedAnswer ).random()
                    }
                    solvedAnswer  = calculateMath(solvedAnswer, num3, operator1)
                }
            }
            return "($num1 ${operator1} $num2) ${operator2} $num3"
        }

        // Function for four terms expressions
        fun fourTerms(): String {
            val operator1 = operators.random()
            val operator2 = operators.random()
            val operator3 = operators.random()
            val num1: Int = (1..21).random()
            var num2: Int = (1..21).random()
            var num3: Int = (1..21).random()
            var num4: Int = (1..21).random()

            when (operator1) {
                "+" -> {
                    while (num1 + num2 >= 100){
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }

                "-" -> {
                    while (num2 > num1){
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }

                "*" -> {
                    while (num1 * num2 >= 100) {
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }

                "/" -> {
                    while (num1 % num2 != 0){
                        num2 = (1..num1).random()
                    }
                    solvedAnswer = calculateMath(num1, num2, operator1)
                }
            }
            when (operator2){

                "+" -> {
                    while ( solvedAnswer + num3 >= 100){
                        num3 = (1.. solvedAnswer).random()
                    }
                    solvedAnswer = calculateMath(solvedAnswer, num3, operator2)
                }

                "-" -> {
                    while (num3 >  solvedAnswer){
                        num3 = (1.. solvedAnswer).random()
                    }
                    solvedAnswer = calculateMath(solvedAnswer, num3, operator2)
                }

                "*" -> {
                    while ( solvedAnswer * num3 >= 100) {
                        num3 = (1..solvedAnswer).random()
                    }
                    solvedAnswer = calculateMath(solvedAnswer, num3, operator2)
                }

                "/" -> {
                    while (solvedAnswer % num3 != 0){
                        num3 = (1..solvedAnswer).random()
                    }
                    solvedAnswer = calculateMath(solvedAnswer, num3, operator2)
                }
            }

            when (operator3){

                "+" -> {
                    while (solvedAnswer + num4>= 100){
                        num4 = (1..solvedAnswer).random()
                    }
                    solvedAnswer = calculateMath(solvedAnswer, num4, operator3)
                }

                "-" -> {
                    while (num4 > solvedAnswer){
                        num4 = (1..solvedAnswer).random()
                    }
                    solvedAnswer = calculateMath(solvedAnswer, num4, operator3)
                }

                "*" -> {
                    while (solvedAnswer * num4 >= 100) {
                        num4 = (1..solvedAnswer).random()
                    }
                    solvedAnswer = calculateMath(solvedAnswer, num4, operator3)
                }

                "/" -> {
                    while (solvedAnswer % num4 != 0){
                        num4 = (1..solvedAnswer).random()
                    }
                    solvedAnswer= calculateMath(solvedAnswer, num4, operator3)
                }
            }
            return "(($num1 ${operator1} $num2) ${operator2} $num3) ${operator3} ${num4}"
        }

        while (conditionL == false){
            if (leftExpressionCount == 1){
                left_string = oneTerms()
            }else if (leftExpressionCount == 2){
                left_string = twoTerms()
            }else if (leftExpressionCount == 3){
                left_string = threeTerms()
            }else if (leftExpressionCount == 4){
                left_string = fourTerms()
            }

            left_temp = solvedAnswer
            if (left_temp <=  100 ){
                conditionL = true
            }
        }

        while (conditionR == false){
            if (rightExpressionCount== 1){
                right_string  = oneTerms()
            }else if (rightExpressionCount == 2){
                right_string = twoTerms()
            }else if (rightExpressionCount == 3){
                right_string = threeTerms()
            }else if (rightExpressionCount == 4){
                right_string = fourTerms()
            }

            right_temp = solvedAnswer
            if (right_temp <= 100 ){
                conditionR = true
            }
        }

        // Displaying the Arithmetic operations
        leftOperation.text  = left_string
        rightOperation.text = right_string

        // Testing purpose only
        println("---------------------------------------------")
        println("Left Expression    | $left_string")
        println("Left Answer        : $left_temp")
        println("Right Expression   | $right_string")
        println("Right Answer       : $right_temp")
        println("---------------------------------------------")
    }

    /**
     * Checks the correct answer for the generated expressions
     * **/
    private fun checkAnswers(){
        var leftValue  = left_temp
        var rightValue = right_temp

        if (leftValue != null) {
            if (leftValue > rightValue !!){
                answer = "greater"
            }else {
                if (leftValue < rightValue !!){
                    answer = "lesser"
                }else if (leftValue == rightValue ){
                    answer = "equal"
                }
            }
        }
    }

    /**
     * Updates the score and displays the results **/
    private fun updateScore(){
        val text = "Extra 10s Added"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)

        if (answerCheck == answer){
            correctAnswers++
            if (correctAnswers % 5 == 0){
                addMoreTime()
                toast.show()
            }
            correctText.visibility = View.VISIBLE

        }else if (answerCheck !== answer){
            wrongAnswers++
            wrongText.visibility   = View.VISIBLE
        }

        newRound()

        // For Testing purpose only
        println("---------------------------------------------")
        println("Corrects Answers : $correctAnswers | Wrongs Answers : $wrongAnswers")
        println("---------------------------------------------")
    }

    /**
     * Ends the game with disabling the buttons and displays user results
     * **/
    private fun endRound(){
        resultView.text = ("CORRECT ANSWERS: "+ correctAnswers.toString() +"\nWRONG ANSWERS: "+ wrongAnswers.toString())
        greaterBtn.setEnabled(false)
        lessBtn.setEnabled(false)
        equalBtn.setEnabled(false)
    }

    /**
     * countdown timer function
     * **/
    private fun startTimer(){
        countDownTimer = object: CountDownTimer(initialCountdown, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished/1000
                countTime.text = timeLeft.toString() + "s"
            }
            override fun onFinish() {
                endRound()
            }
        }.start()
    }


    private fun restoreRound(){
        countDownTimer.cancel()
        val restoredTime = timeLeftOnTimer / 1000
        countTime.text = restoredTime.toString() + "s"
        countDownTimer = object: CountDownTimer(timeLeftOnTimer, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished/1000
                countTime.text = timeLeft.toString() + "s"
            }
            override fun onFinish() {
                endRound()
            }
        }.start()
    }

    /**
     * Adding more time to the round for every 5 correct answers
     * **/
    private fun addMoreTime(){
        countDownTimer.cancel()
        timeLeftOnTimer += additionalTime
        countTime.text = timeLeftOnTimer.toString() + "s"
        countDownTimer = object: CountDownTimer(timeLeftOnTimer, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished/1000
                countTime.text = timeLeft.toString() + "s"
            }
            override fun onFinish() {
                endRound()
            }
        }.start()
    }
}