package com.example.play2win.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.play2win.R
import com.example.play2win.databinding.FragmentGameBinding
import com.example.play2win.ui.MainActivity


/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {

    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (or better yet, not define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "Which Bollywood personality was named by NITI Aayog to promote the Women Entrepreneurship platform (WEP)?",
                    answers = listOf("Sushant Singh Rajput", "Akshay Kumar", "Vidya Balan", "Shahrukh Khan")),
            Question(text = "Which Bollywood personality is set to be honoured with honorary doctorate by Melbourne’s La Trobe University?",
                    answers = listOf("Shahrukh Khan", "Shahrukh Khan", "Salman Khan", "Amitab Bachan")),
            Question(text = "How many football world cup Germany won?",
                    answers = listOf("4", "5", "6", "7")),
            Question(text = "How many Ballon D'Or Cristiano Ronaldo have?",
                    answers = listOf("5", "6", "7", "8")),
            Question(text = "How many Ballon D'Or Messi own?",
                    answers = listOf("5", "6", "7", "8")),
            Question(text = "Which Bollywood actor to be felicitated with ‘Excellence in Cinema’ award by Victorian Government?",
                    answers = listOf("Shahrukh Khan", "Amir Khan", "Salman Khan", "Akshay Kumar")),
            Question(text = "What is the name of the National-award winning Bollywood choreographer, who recently passed away?",
                    answers = listOf("Saroj Khan", "Protik Prakash", "Tridib Ghosh", "Geetha Nagabhushan")),
            Question(text = "Which Bollywood personality has become the brand ambassador for Government of India (GoI)’s road safety awareness campaign?",
                    answers = listOf("Akshay Kumar", "Shahrukh Khan", "Amir Khan", "Amitab Bachan")),
            Question(text = "The Versatile Bollywood actor Irrfan Khan, who recently passed away, had won National award for which movie?",
                    answers = listOf("Paan Singh Tomar", "Lunch Box", "Life of Pi", "Haidar")),
            Question(text = "Which Bollywood personality has been honoured by European Union for strengthening Europe-India cultural ties?",
                    answers = listOf("Amitab Bachan", "Amir Khan", "Shahrukh Khan", "Akshay Kumar"))
    )

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as MainActivity?)?.isOnGame = true



        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        (activity as MainActivity?)?.isOnGame = false
                        // We've won!  Navigate to the gameWonFragment.
                        view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(numQuestions, questionIndex))

                    }
                } else {
                    (activity as MainActivity?)?.isOnGame = false
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment2())
                }
            }
        }
        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}