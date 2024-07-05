package com.example.kiddozfunquiz

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kiddozfunquiz.databinding.ActivityAttemptQuizBinding
import com.example.kiddozfunquiz.databinding.ScoreDialogBinding
import android.graphics.Color

class AttemptQuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        var questionModelList : List<QuestionModel> = listOf()
        var time : String = ""
    }

    private lateinit var binding: ActivityAttemptQuizBinding

    var currentQuestionIndex = 0
    var selectedAnswer = ""
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAttemptQuizBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            btnAnswerQuestionA.setOnClickListener(this@AttemptQuizActivity)
            btnAnswerQuestionB.setOnClickListener(this@AttemptQuizActivity)
            btnAnswerQuestionC.setOnClickListener(this@AttemptQuizActivity)
            btnAnswerQuestionD.setOnClickListener(this@AttemptQuizActivity)
            btnNextQuestion.setOnClickListener(this@AttemptQuizActivity)
        }

        loadQuestions()
        startTimer()
    }

    private fun startTimer(){
        val totalTimeInMillis = time.toInt() * 60 * 1000
        object : CountDownTimer(totalTimeInMillis.toLong(), 1000L){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTv.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {

            }
        }.start()
    }

    private fun loadQuestions(){

        selectedAnswer = ""
        if (currentQuestionIndex == questionModelList.size){
            finishQuiz()
            return
        }

        binding.apply {
            questionIndicatorTv.text = "Question ${currentQuestionIndex+1}/ ${questionModelList.size}"
            questionProgressIndicator.progress = (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTv.text = questionModelList[currentQuestionIndex].question
            btnAnswerQuestionA.text = questionModelList[currentQuestionIndex].options[0]
            btnAnswerQuestionB.text = questionModelList[currentQuestionIndex].options[1]
            btnAnswerQuestionC.text = questionModelList[currentQuestionIndex].options[2]
            btnAnswerQuestionD.text = questionModelList[currentQuestionIndex].options[3]
        }
    }

    override fun onClick(view: View) {

        binding.apply {
            btnAnswerQuestionA.setBackgroundColor(getColor(R.color.grey))
            btnAnswerQuestionB.setBackgroundColor(getColor(R.color.grey))
            btnAnswerQuestionC.setBackgroundColor(getColor(R.color.grey))
            btnAnswerQuestionD.setBackgroundColor(getColor(R.color.grey))
        }

        val clickedBtn = view as Button
        if (clickedBtn.id == R.id.btn_next_question) {
            if (selectedAnswer == questionModelList[currentQuestionIndex].correct){
                score++
                Log.i("Score of Quiz", score.toString())
            }
            currentQuestionIndex++
            loadQuestions()
        } else {
            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }
    }

    private fun finishQuiz(){
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if (percentage > 60){
                scoreTittle.text = "Congrast! You Have Passed"
                scoreTittle.setTextColor(Color.BLACK)
            }else{
                scoreTittle.text = "Oops! You Have Failed"
                scoreTittle.setTextColor(Color.RED)
            }

            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            btnFinish.setOnClickListener{
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()

    }
}