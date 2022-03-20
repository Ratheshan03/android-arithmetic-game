package ratheshan.arithmeticgame

import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
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
        var left_temp = 0.0
        var right_temp = 0.0
        var counter = 0

    }
    lateinit var prefs: SharedPreferences
    lateinit var greaterBtn: Button
    lateinit var equalBtn: Button
    lateinit var lessBtn: Button
    lateinit var rightOperation: TextView
    lateinit var leftOperation: TextView
    lateinit var correctText: TextView
    lateinit var wrongText: TextView
    lateinit var countTime: TextView
    lateinit var resultView: TextView

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

        val timer = MyCounter(50000,1000)
        timer.start()
        newRound()
    }

    // function to calculate expressions
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

    // New round function
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


    // Function that generates left and right random strings
    private fun generateOperations(){
        val leftExpressionCount = (1..4).random()
        val rightExpressionCount = (1..4).random()
        val operatorList = mutableListOf<String>()
        var subExpression: Boolean = false

        fun oneTerms(): String {
            var statement = ""
            var term_one: Int = (1..20).random()
            solvedAnswer = term_one
            statement = "$term_one"

            return statement
        }

        fun twoTerms(): String {
            var statement = ""
            while(subExpression == false){
                operatorList.add(operators.random())
                var term_one: Int = (1..20).random()
                var term_two: Int = (1..20).random()

                solvedAnswer = calculateMath(term_one,term_two, operatorList[0])
                if (operatorList[0].equals("+")){
                    if (solvedAnswer < 100){
                        statement = "$term_one ${operatorList[0]} $term_two"
                        subExpression = true
                    }else{
                        twoTerms()
                    }
                }
                if (operatorList[0].equals("-")){
                    if (solvedAnswer < 100){
                        statement = "$term_one ${operatorList[0]} $term_two"
                        subExpression = true
                    }else{
                        twoTerms()
                    }
                }
                if (operatorList[0].equals("*")){
                    if (solvedAnswer < 100){
                        statement = "$term_one ${operatorList[0]} $term_two"
                        subExpression = true
                    }else{
                        twoTerms()
                    }
                }
                if (operatorList[0].equals("/")){
                    if (term_one.toDouble() % term_two.toDouble() == 0.0 && solvedAnswer < 100){
                        statement = "$term_one ${operatorList[0]} $term_two"
                        subExpression = true
                    }else{
                        twoTerms()
                    }
                }
            }
            return statement
        }

        fun threeTerms(): String {
            var statement = ""
            while(subExpression == false){
                operatorList.add(operators.random())
                operatorList.add(operators.random())
                var term_one: Int = (1..20).random()
                var term_two: Int = (1..20).random()
                var term_three: Int = (1..20).random()

                solvedAnswer = calculateMath(term_one,term_two, operatorList[0])
                if (operatorList[0].equals("+")){
                    if (solvedAnswer < 100){
                        if(operatorList[1].equals("+")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("-")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("*")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("/")){
                            if (solvedAnswer.toDouble() % term_three.toDouble() == 0.0 && solvedAnswer / term_three < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                    }else{
                        threeTerms()
                    }
                }
                if (operatorList[0].equals("-")){
                    if (solvedAnswer < 100){
                        if(operatorList[1].equals("+")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("-")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("*")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("/")){
                            if (solvedAnswer.toDouble() % term_three.toDouble() == 0.0 && solvedAnswer / term_three < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                    }else{
                        threeTerms()
                    }
                }
                if (operatorList[0].equals("*")){
                    if (solvedAnswer < 100){
                        if(operatorList[1].equals("+")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("-")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("*")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("/")){
                            if (solvedAnswer.toDouble() % term_three.toDouble() == 0.0 && solvedAnswer / term_three < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                    }else{
                        threeTerms()
                    }
                }
                if (operatorList[0].equals("/")){
                    if (term_one.toDouble() % term_two.toDouble() == 0.0){
                        if(operatorList[1].equals("+")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("-")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("*")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                        if(operatorList[1].equals("/")){
                            if (solvedAnswer.toDouble() % term_three.toDouble() == 0.0 && solvedAnswer / term_three < 100){
                                statement = "($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three"
                                subExpression = true
                            }else{
                                threeTerms()
                            }
                        }
                    }else{
                        threeTerms()
                    }
                }
            }
            return statement
        }

        fun fourTerms(): String {
            //solvedAnswer = calculateMath(solvedAnswer,term_three, operatorList[1])
            //solvedAnswer = calculateMath(solvedAnswer,term_four, operatorList[2])

            var statement: String = ""
            while(subExpression == false){
                operatorList.add(operators.random())
                operatorList.add(operators.random())
                operatorList.add(operators.random())
                var term_one: Int = (1..20).random()
                var term_two: Int = (1..20).random()
                var term_three: Int = (1..20).random()
                var term_four: Int = (1..20).random()

                solvedAnswer = calculateMath(term_one,term_two, operatorList[0])
                if (operatorList[0].equals("+")){
                    if (solvedAnswer < 100){
                        if(operatorList[1].equals("+")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("-")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("*")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("/")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                    }else{
                        fourTerms()
                    }
                }
                if (operatorList[0].equals("-")){
                    if (solvedAnswer < 100){
                        if(operatorList[1].equals("+")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("-")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("*")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("/")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                    }else{
                        fourTerms()
                    }
                }
                if (operatorList[0].equals("*")){
                    if (solvedAnswer < 100){
                        if(operatorList[1].equals("+")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("-")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("*")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("/")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                    }else{
                        fourTerms()
                    }
                }
                if (operatorList[0].equals("/")){
                    if (term_one.toDouble() % term_two.toDouble() == 0.0){
                        if(operatorList[1].equals("+")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("-")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("*")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                        if(operatorList[1].equals("/")){
                            solvedAnswer = calculateMath(solvedAnswer,term_three,operatorList[1])
                            if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer < 100){
                                if(operatorList[2].equals("+")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("-")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("*")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                                if(operatorList[2].equals("/")){
                                    solvedAnswer = calculateMath(solvedAnswer,term_four,operatorList[2])
                                    if (solvedAnswer.toDouble() % term_four.toDouble() == 0.0 && solvedAnswer / term_four < 100){
                                        statement = "(($term_one ${operatorList[0]} $term_two) ${operatorList[1]} $term_three) ${operatorList[2]} $term_four"
                                        subExpression = true
                                    }else{
                                        fourTerms()
                                    }
                                }
                            }else{
                                fourTerms()
                            }
                        }
                    }else{
                        fourTerms()
                    }
                }
            }
            return statement
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

            left_temp = solvedAnswer.toDouble()

            if (left_temp != null){
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

            right_temp = solvedAnswer.toDouble()

            if (left_temp != null){
                conditionL = true
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

    // Checks the correct answer for the generated expressions
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

    // Updates the score and displays the results
    private fun updateScore(){
        if (answerCheck == answer){
            correctAnswers++
            correctText.visibility = View.VISIBLE

        }else if (answerCheck !== answer){
            wrongAnswers++
            wrongText.visibility   = View.VISIBLE
        }

//        Handler().postDelayed({
//            newRound()
//        }, 2000)
        newRound()

        // Testing purpose only
        println("---------------------------------------------")
        println("Corrects Answers : $correctAnswers | Wrongs Answers : $wrongAnswers")
        println("---------------------------------------------")
    }


    //Countdown Timer Class
    inner class MyCounter(millisInFuture:Long,countdownInterval:Long):
        CountDownTimer(millisInFuture,countdownInterval){
        override fun onFinish() {
            resultView.text = ("CORRECT ANSWERS: "+ correctAnswers.toString() +"\nWRONG ANSWERS: "+ wrongAnswers.toString())
            greaterBtn.setEnabled(false)
            lessBtn.setEnabled(false)
            equalBtn.setEnabled(false)
        }
        override fun onTick(millisUntilFinished: Long) {
            countTime.text = (millisUntilFinished/1000).toString()
        }
    }
}