package com.example.kiddozfunquiz

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiddozfunquiz.databinding.ActivityQuizBinding
import com.google.firebase.database.FirebaseDatabase

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var quizModelList: MutableList<QuizModel>
    private lateinit var adapter: QuizListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityQuizBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        getDataFromFirebase()

    }

    private fun setupRecyclerView(){
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase(){
        //dummy data

//        FirebaseDatabase.getInstance().reference.get().addOnSuccessListener { dataSnapshot->
//            if (dataSnapshot.exists()){
//                for (snapshot in dataSnapshot.children){
//                    val quizModel = snapshot.getValue(QuizModel::class.java)
//                    if (quizModel != null) {
//                        quizModelList.add(quizModel)
//                    }
//                }
//            }
//            setupRecyclerView()
//        }

        val listQuestionModel = mutableListOf<QuestionModel>()
        listQuestionModel.add(QuestionModel("Hewan manakah yang berkaki 4?", mutableListOf("Ayam", "Laba-Laba", "Bebek", "Kuda"), "Kuda"))
        listQuestionModel.add(QuestionModel("Hewan manakah yang berkaki 2?", mutableListOf("Ayam", "Laba-Laba", "Kambing", "Kuda"), "Ayam"))
        listQuestionModel.add(QuestionModel("Hewan manakah yang berkaki 8?", mutableListOf("Ayam", "Laba-Laba", "Bebek", "Kuda"), "Laba-Laba"))

        quizModelList.add(QuizModel("1","Level 1","5", listQuestionModel))
        quizModelList.add(QuizModel("2","Level 2","10", listQuestionModel))
        quizModelList.add(QuizModel("3","Level 3","15", listQuestionModel))
        quizModelList.add(QuizModel("4","Level 4","20", listQuestionModel))
        quizModelList.add(QuizModel("5","Level 5","25", listQuestionModel))
        quizModelList.add(QuizModel("6","Level 6","30", listQuestionModel))
        quizModelList.add(QuizModel("7","Level 7","35", listQuestionModel))
        quizModelList.add(QuizModel("8","Level 8","40", listQuestionModel))
        quizModelList.add(QuizModel("9","Level 9","45", listQuestionModel))
        quizModelList.add(QuizModel("10","Level 10","50", listQuestionModel))
        setupRecyclerView()
    }
}