package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val challengeStates = BooleanArray(6) { false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupChallenges()
        setupBottomNavigation()
        loadProgress()
        Toast.makeText(this, "EcoSphere Started!", Toast.LENGTH_SHORT).show()
    }

    private fun setupChallenges() {
        for (i in 1..6) {
            try {
                val challengeId = resources.getIdentifier("challenge$i", "id", packageName)
                val buttonId = resources.getIdentifier("button$i", "id", packageName)

                val challengeLayout = findViewById<LinearLayout>(challengeId)
                val buttonImage = findViewById<ImageView>(buttonId)

                challengeLayout.setOnClickListener {
                    handleChallengeClick(i-1, challengeLayout, buttonImage)
                }
            } catch (e: Exception) {
                continue
            }
        }
    }

    private fun handleChallengeClick(index: Int, layout: LinearLayout, button: ImageView) {
        // Click animation
        layout.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100)
            .withEndAction {
                layout.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
            }

        // If challenge is already completed, undo it
        if (challengeStates[index]) {
            undoChallenge(index, layout, button)
        } else {
            // If not completed, open detail page
            openChallengeDetail(index)
        }
    }

    private fun undoChallenge(index: Int, layout: LinearLayout, button: ImageView) {
        // Return to original state
        layout.setBackgroundColor(Color.parseColor("#E8F5E9"))
        button.clearColorFilter()
        changeTextColor(layout, Color.parseColor("#0F2800"))

        challengeStates[index] = false
        saveProgress()

        val messages = listOf(
            "No Plastic Day undone",
            "Refill Your Bottle undone",
            "Turn Off Lights undone",
            "Recycle One Item undone",
            "Reusable Bag undone",
            "Public Transport undone"
        )
        Toast.makeText(this, messages[index], Toast.LENGTH_SHORT).show()
    }

    private fun openChallengeDetail(index: Int) {
        // Challenge data
        val challengeTitles = listOf(
            "No Plastic Day",
            "Refill Your Bottle",
            "Turn Off the Lights",
            "Recycle One Item",
            "Reusable Bag",
            "Public Transport Challenge"
        )

        val challengeDescriptions = listOf(
            "Spend one whole day without using any plastic bags or bottles. This helps reduce plastic waste in our environment and protects marine life.",
            "Refill a reusable water bottle instead of buying a new plastic one. Save money and reduce plastic waste! Every bottle counts.",
            "Switch off the lights in rooms you're not using for at least one hour. Conserve energy and help reduce carbon emissions.",
            "Put at least one recyclable item in a recycling bin instead of the trash. Every small action counts towards a cleaner planet!",
            "Use a reusable cloth bag when shopping instead of taking a plastic bag. Reduce single-use plastic consumption and help the environment.",
            "Use public transportation or walk instead of driving. Reduce carbon emissions, traffic congestion, and save money on fuel."
        )

        // Challenge images - استخدام الصور التي وضعتيها في drawable
        val challengeImages = listOf(
            R.drawable.ic_plastic,    // للتحدي الأول
            R.drawable.ic_water,      // للتحدي الثاني
            R.drawable.ic_light,      // للتحدي الثالث
            R.drawable.ic_recycle,    // للتحدي الرابع
            R.drawable.ic_bag,        // للتحدي الخامس
            R.drawable.ic_transport   // للتحدي السادس
        )

        // Open detail activity
        val intent = Intent(this, ChallengeDetailActivity::class.java)
        intent.putExtra("challenge_title", challengeTitles[index])
        intent.putExtra("challenge_description", challengeDescriptions[index])
        intent.putExtra("challenge_image", challengeImages[index])
        intent.putExtra("challenge_index", index)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val completedIndex = data?.getIntExtra("completed_challenge_index", -1) ?: -1
            if (completedIndex != -1) {
                markChallengeAsCompleted(completedIndex)
            }
        }
    }

    private fun markChallengeAsCompleted(index: Int) {
        val layoutIds = listOf(R.id.challenge1, R.id.challenge2, R.id.challenge3,
            R.id.challenge4, R.id.challenge5, R.id.challenge6)
        val buttonIds = listOf(R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6)

        val layout = findViewById<LinearLayout>(layoutIds[index])
        val button = findViewById<ImageView>(buttonIds[index])

        // Change to completed state
        layout.setBackgroundColor(Color.parseColor("#C8E6C9"))
        button.setColorFilter(Color.parseColor("#1B5E20"))
        changeTextColor(layout, Color.parseColor("#1B5E20"))

        challengeStates[index] = true
        saveProgress()

        val messages = listOf(
            "No Plastic Day completed! 🌱",
            "Refill Your Bottle completed! 💧",
            "Turn Off Lights completed! 💡",
            "Recycle One Item completed! ♻️",
            "Reusable Bag completed! 🛍️",
            "Public Transport completed! 🚌"
        )
        Toast.makeText(this, messages[index], Toast.LENGTH_SHORT).show()

        checkAllChallengesCompleted()
    }

    private fun checkAllChallengesCompleted() {
        if (challengeStates.all { it }) {
            Toast.makeText(this, "🎉 Amazing! All challenges completed!", Toast.LENGTH_LONG).show()
        }
    }

    private fun changeTextColor(layout: LinearLayout, color: Int) {
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val innerChild = child.getChildAt(j)
                    if (innerChild is TextView) {
                        innerChild.setTextColor(color)
                    }
                }
            } else if (child is TextView) {
                child.setTextColor(color)
            }
        }
    }

    private fun setupBottomNavigation() {
        findViewById<LinearLayout>(R.id.homeLayout).setOnClickListener {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.challengesLayout).setOnClickListener {
            Toast.makeText(this, "Challenges", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.profileLayout).setOnClickListener {
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProgress() {
        val sharedPref = getSharedPreferences("EcoSphere", MODE_PRIVATE)
        with(sharedPref.edit()) {
            for (i in challengeStates.indices) {
                putBoolean("challenge_$i", challengeStates[i])
            }
            apply()
        }
    }

    private fun loadProgress() {
        val sharedPref = getSharedPreferences("EcoSphere", MODE_PRIVATE)
        for (i in challengeStates.indices) {
            challengeStates[i] = sharedPref.getBoolean("challenge_$i", false)
            if (challengeStates[i]) {
                updateChallengeUI(i)
            }
        }
    }

    private fun updateChallengeUI(index: Int) {
        val layoutIds = listOf(R.id.challenge1, R.id.challenge2, R.id.challenge3,
            R.id.challenge4, R.id.challenge5, R.id.challenge6)
        val buttonIds = listOf(R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6)

        val layout = findViewById<LinearLayout>(layoutIds[index])
        val button = findViewById<ImageView>(buttonIds[index])

        layout.setBackgroundColor(Color.parseColor("#C8E6C9"))
        button.setColorFilter(Color.parseColor("#1B5E20"))
        changeTextColor(layout, Color.parseColor("#1B5E20"))
    }
}