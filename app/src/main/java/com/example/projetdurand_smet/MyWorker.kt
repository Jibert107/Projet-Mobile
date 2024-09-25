package com.example.projetdurand_smet

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.Calendar

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

        override fun doWork(): Result {
            val sharedPreferences =
                applicationContext.getSharedPreferences("QuestionPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            val calendar = Calendar.getInstance()
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            val question: String
            val answer: String

            when (dayOfWeek) {
                Calendar.MONDAY -> {
                    question = "Quel jour sommes-nous ?"
                    answer = "Lundi"
                }

                Calendar.TUESDAY -> {
                    question = "Quel jour sommes-nous ?"
                    answer = "Mardi"
                }

                Calendar.WEDNESDAY -> {
                    question = "Quel jour sommes-nous ?"
                    answer = "Mercredi"
                }

                Calendar.THURSDAY -> {
                    question = "Quel jour sommes-nous ?"
                    answer = "Jeudi"
                }

                Calendar.FRIDAY -> {
                    question = "Quel jour sommes-nous ?"
                    answer = "Vendredi"
                }

                Calendar.SATURDAY -> {
                    question = "Quel jour sommes-nous ?"
                    answer = "Samedi"
                }

                Calendar.SUNDAY -> {
                    question = "Quel jour sommes-nous ?"
                    answer = "Dimanche"
                }

                else -> {
                    question = "Quel jour sommes-nous ?"
                    answer = "Inconnu"
                }
            }

            editor.putString("question", question)
            editor.putString("answer", answer)
            editor.apply()

            return Result.success()
        }
}