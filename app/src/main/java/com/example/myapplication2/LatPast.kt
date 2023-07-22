package com.example.myapplication2

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.myapplication2.databinding.ActivityLatPastBinding
import com.example.myapplication2.databinding.ActivityLatPresentBinding

class LatPast : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mSelectedOptionPosition: Int = 0
    private var canSelectOption = true
    private var mCorrectAnswers: Int = 0
    private lateinit var mQuestionList: ArrayList<Questions>

    private lateinit var binding : ActivityLatPastBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatPastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

        mQuestionList = QuestionLat.getQuestions2()

        supportActionBar?.hide()

        setQuestion()
    }

    private fun setQuestion() {
        defaultOptionView()
        binding.btnSubmit.visibility = View.GONE
        canSelectOption = true

        val questionPackage: Questions = mQuestionList[mCurrentPosition - 1]
        binding.tvQuestion.text = questionPackage.question
//        if (questionPackage.image == 0) {
//            // cvImage.layoutParams = LinearLayout.LayoutParams(0, 0) ---> set width and height of card view to 0
//            cvImage.background.setTint(ContextCompat.getColor(this, R.color.primary_color))
//        }
//        ivQuestion.setImageResource(questionPackage.image)
        binding.progressBar.progress = mCurrentPosition
        binding.tvProgress.text = "$mCurrentPosition/${binding.progressBar.max}"
        binding.tvOptionOne.text = questionPackage.optionOne
        binding.tvOptionTwo.text = questionPackage.optionTwo
        binding.tvOptionThree.text = questionPackage.optionThree
        binding.tvOptionFour.text = questionPackage.optionFour

        if(mCurrentPosition == mQuestionList.size)
            binding.btnSubmit.text = "Finish"
        else
            binding.btnSubmit.text = "Submit"
    }

    private fun defaultOptionView() {
        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)

        for(option in options){
            option.setTextColor(ContextCompat.getColor(this, R.color.white3))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOption: Int) {
        defaultOptionView()
        mSelectedOptionPosition = selectedOption
        tv.setTextColor(ContextCompat.getColor(this, R.color.light_color))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when(answer){
            1 -> { binding.tvOptionOne.background = ContextCompat.getDrawable(this, drawableView) }
            2 -> { binding.tvOptionTwo.background = ContextCompat.getDrawable(this, drawableView) }
            3 -> { binding.tvOptionThree.background = ContextCompat.getDrawable(this, drawableView) }
            4 -> { binding.tvOptionFour.background = ContextCompat.getDrawable(this, drawableView) }
        }
    }

    private fun buttonVisible() {
        binding.btnSubmit.visibility = View.VISIBLE
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.tvOptionOne -> {
                if(canSelectOption) {
                    selectedOptionView(binding.tvOptionOne, 1)
                    buttonVisible()
                }
            }
            R.id.tvOptionTwo -> {
                if(canSelectOption) {
                    selectedOptionView(binding.tvOptionTwo, 2)
                    buttonVisible()
                }
            }
            R.id.tvOptionThree -> {
                if(canSelectOption) {
                    selectedOptionView(binding.tvOptionThree, 3)
                    buttonVisible()
                }
            }
            R.id.tvOptionFour -> {
                if(canSelectOption) {
                    selectedOptionView(binding.tvOptionFour, 4)
                    buttonVisible()
                }
            }
            R.id.btnSubmit -> {

                if(mSelectedOptionPosition == 0){
                    mCurrentPosition++

                    if(mCurrentPosition <= mQuestionList.size)
                        setQuestion()
                    else {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(DataQuestions.SCORE_KEY, mCorrectAnswers)
                        intent.putExtra(DataQuestions.TOTAL_QUESTION_KEY, mQuestionList.size)
                        startActivity(intent)
                        finish()
                    }
                }
                else {
                    val question = mQuestionList[mCurrentPosition-1]
                    if(question.answer != mSelectedOptionPosition)
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_bg)
                    else
                        mCorrectAnswers++

                    answerView(question.answer, R.drawable.correct_option_bg)

                    if(mCurrentPosition == mQuestionList.size)
                        binding.btnSubmit.text = "Finish"
                    else{
                        binding.btnSubmit.text = "Go To Next Question"
                    }

                    mSelectedOptionPosition = 0
                    canSelectOption = false
                }

            }
        }
    }
}