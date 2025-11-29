package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChallengeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_detail)

        // Get data from intent
        val challengeTitle = intent.getStringExtra("challenge_title") ?: "Challenge"
        val challengeDescription = intent.getStringExtra("challenge_description") ?: "Description"
        val challengeImageRes = intent.getIntExtra("challenge_image", R.drawable.ic_plastic)
        val challengeIndex = intent.getIntExtra("challenge_index", 0)

        // Initialize views
        val titleTextView = findViewById<TextView>(R.id.challengeTitle)
        val descriptionTextView = findViewById<TextView>(R.id.challengeDescription)
        val challengeImageView = findViewById<ImageView>(R.id.challengeImage)
        val backButton = findViewById<Button>(R.id.backButton)
        val markCompleteButton = findViewById<Button>(R.id.markCompleteButton)
        val shareButton = findViewById<Button>(R.id.shareButton)

        // Set challenge data
        titleTextView.text = challengeTitle
        descriptionTextView.text = challengeDescription
        challengeImageView.setImageResource(challengeImageRes) // تعيين الصورة المناسبة

        // Back button click
        backButton.setOnClickListener {
            finish()
        }

        // Mark as complete button
        markCompleteButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("completed_challenge_index", challengeIndex)
            setResult(RESULT_OK, resultIntent)

            Toast.makeText(this, "Challenge completed! 🌟", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Share button
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "EcoSphere Challenge")
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                "I'm taking the '$challengeTitle' challenge with EcoSphere! 🌍\n\n" +
                        "$challengeDescription\n\n" +
                        "Join me in making the world a better place!"
            )

            startActivity(Intent.createChooser(shareIntent, "Share Challenge"))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}