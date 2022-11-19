package com.example.movieticketer

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener


import com.example.movieticketer.databinding.ActivityMovieBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class MovieActivity : AppCompatActivity() {

    private var tvSelectedDate: TextView? = null
    private val aviableSits: Array<Int> = arrayOf(
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0
    )


    private lateinit var binding: ActivityMovieBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        inflateVar()
        var takenSits = 0
        takenSits = initButtons()
        ticketsCheck(takenSits)
        alertDialogPoster()
        val buttonList = initButtonList()
        var count = 0
        for (i in 0..17)
            buttonList[i].setOnCheckedChangeListener { tuggleButton, isChecked ->
                count = countSelectedSits(count, isChecked, i, buttonList)
            }
        val ticketsText: EditText = findViewById(R.id.ticketsText)
        if (!ticketsText.equals("")) {
            ticketsText.addTextChangedListener {
                if (ticketsText.text.toString() != "" && ticketsText.text.toString()
                        .toInt() >= 17 - takenSits
                ) {
                } else activeButtons()
            }
        }
        val btnDatePicker: Button = findViewById(R.id.btnDatePicker)
        btnDatePicker.setOnClickListener {
            clickDatePicker()
            activeButtons()
        }

        val btnBuy: Button = findViewById(R.id.btn_buy)
        btnBuy.setOnClickListener {
            if(findViewById<TextView>(R.id.selectedSeats).text == findViewById<TextView>(R.id.totalTickets).text)
            {
                alertDialogPBuy(btnBuy)
            }
            else Toast.makeText(this,"Please fill in all the details", Toast.LENGTH_SHORT).show()

        }

    }

    fun inflateVar() {
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val hourSpinner: Spinner = findViewById(R.id.hourSpinner)


        val description = intent.getStringExtra("description")
        val movieName = intent.getStringExtra("movieName")
        val categories = intent.getStringExtra("categories")
        val cast = intent.getStringExtra("cast")
        val director = intent.getStringExtra("director")
        val production = intent.getStringExtra("production")
        val originalLanguage = intent.getStringExtra("originalLanguage")
        val ageRestriction = intent.getStringExtra("ageRestriction")
        val showingOn = intent.getStringExtra("showingOn")
        val releaseDate = intent.getStringExtra("releaseDate")
        val screenTime = intent.getStringExtra("screenTime")
        val posterID = intent.getIntExtra("posterID", R.drawable.prey_poster)
        val trailerURL = intent.getStringExtra("trailerURL")
        val aviableTime = intent.getStringArrayListExtra("aviableTime")

        val aviableTimerList = arrayListOf<String?>("", "0", "0", "0", "0", "0")
        for (i in 1..5) {
            aviableTimerList[i] = aviableTime!![i].toString()
        }

        val adapterSpinner =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, aviableTimerList)
        hourSpinner.adapter = adapterSpinner


        binding.tmpDescription.text = description
        binding.tmpMovieTitle.text = movieName
        binding.tmpFilmGenre.text = "Film genre: $categories"
        binding.tmpCast.text = "Cast: $cast"
        binding.tmpDirector.text = "Director: $director"
        binding.tmpProduction.text = "Production: $production"
        binding.tmpOriginalLanguage.text = "Original language: $originalLanguage"
        binding.tmpAgeRestrictions.text = "Age restrictions: $ageRestriction"
        binding.tmpShowingIn.text = "Showing in: $showingOn"
        binding.tmpReleseDate.text = "Release date: $releaseDate"
        binding.tmpScreenTime.text = "Running time: $screenTime minutes"
        binding.tmpPosterImage.setImageResource(posterID)

        val btnTrailer: Button = findViewById(R.id.tmp_btnTrailer)
        btnTrailer.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(trailerURL)
                )
            )
        }
    }

    private fun clickDatePicker() {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        val dpd = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "$selectedDayOfMonth.${selectedMonth + 1}.$selectedYear"

                val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

                val theDate = sdf.parse(selectedDate)
                theDate?.let {
                    theDate.time / 60000

                    val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                    currentDate?.let {
                        tvSelectedDate?.text = selectedDate
                    }
                }
            }, year, month, day
        )
        dpd.datePicker.minDate = System.currentTimeMillis()
        dpd.datePicker.maxDate = System.currentTimeMillis() + 2592000000
        dpd.show()
    }

    fun activeButtons() {
        val sitsButtons: List<ToggleButton> = initButtonList()
        val tvSelectedDate: TextView = findViewById(R.id.tvSelectedDate)
        val hourSpinner: Spinner = findViewById(R.id.hourSpinner)
        val ticketsText: EditText = findViewById(R.id.ticketsText)


        if (ticketsText != null && tvSelectedDate.text != null &&
            (tvSelectedDate.text.isNotEmpty() || tvSelectedDate.text != "Select a date")
            && hourSpinner.selectedItem.toString() != ""
            && (ticketsText.text.toString().toInt() <= 9 && ticketsText.text.toString()
                .toInt() >= 1)
            && ticketsText.text.isNotEmpty()
        ) {
            for (i in 0..17) {
                if (aviableSits[i] == 1) sitsButtons[i].isClickable = true
                sitsButtons[i].isActivated = true
            }
        }
    }

    private fun initButtons(): Int {
        val sitsButtons: List<ToggleButton> = initButtonList()
        var counter = 0
        var randNum: Int

        for (i in 0..17) {
            randNum = Random.nextInt(0, 3)
            if (counter != 10 && randNum != 0) {
                sitsButtons[i].isEnabled = false
                aviableSits[i] = 0
                counter++
            } else {
                sitsButtons[i].isClickable = false
                aviableSits[i] = 1
            }
        }
        return counter
    }

    private fun ticketsCheck(takenSits: Int) {
        val plusBtn: Button = findViewById(R.id.plusBtn)
        val minusBtn: Button = findViewById(R.id.minusBtn)
        val ticketsText: EditText = findViewById(R.id.ticketsText)
        val totalPrice: TextView = findViewById(R.id.totalPrice)
        val totalTickets: TextView = findViewById(R.id.totalTickets)

        val maxTickets = 18 - takenSits

        var num = 0

        plusBtn.setOnClickListener {
            if (ticketsText.text.toString() == "") {
                ticketsText.setText("1")
                num = 1
                totalPrice.text = "${5}$"
                totalTickets.text = num.toString()
            } else if (num <= maxTickets) {
                num = ticketsText.text.toString().toInt() + 1
                ticketsText.setText(num.toString())
                totalPrice.text = "${5 * num}$"
                totalTickets.text = num.toString()
            }
        }
        minusBtn.setOnClickListener {

            if (num > 0 && (findViewById<TextView>(R.id.selectedSeats).text.toString()
                    .toInt()) < findViewById<TextView>(R.id.totalTickets).text.toString().toInt()
            ) {
                num = ticketsText.text.toString().toInt() - 1
                ticketsText.setText(num.toString())
                totalPrice.text = "${5 * num}$"
                totalTickets.text = num.toString()
            } else disableButtons(initButtonList())
            val sitsButtons: List<ToggleButton> = initButtonList()
            if (sitsButtons[0].isActivated && ticketsText.text.toString().toInt() == 0) {

                for (i in 0..17) {
                    sitsButtons[i].isClickable = false
                    sitsButtons[i].isActivated = false
                }
            }
        }

    }

    fun alertDialogPoster() {

        val tmpPosterImage: ImageView = findViewById(R.id.tmp_posterImage)
        tmpPosterImage.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.photo_alert_dialog, null)
            dialogView.findViewById<ImageView>(R.id.posterImage_alert)
                .setImageDrawable(tmpPosterImage.drawable)
            dialogView.findViewById<TextView>(R.id.movieName).text =
                findViewById<TextView>(R.id.tmp_movieTitle).text
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()

            dialogView.findViewById<Button>(R.id.okBtnAlert).setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun alertDialogPBuy(btnBuy: Button) {
        val builder = AlertDialog.Builder(btnBuy.context)
        val dialogView = layoutInflater.inflate(R.layout.buy_alertdialog, null)

        dialogView.findViewById<TextView>(R.id.movieTheater_buy).text =
            "Theater City : ${findViewById<Spinner>(R.id.theaterSpinner).selectedItem}"
        dialogView.findViewById<TextView>(R.id.movieName_buy).text =
            "Movie Name : ${findViewById<TextView>(R.id.tmp_movieTitle).text}"
        dialogView.findViewById<TextView>(R.id.movieDate_buy).text =
            "Movie Date : ${findViewById<TextView>(R.id.tvSelectedDate).text}"
        dialogView.findViewById<TextView>(R.id.movieHour_buy).text =
            "Movie Hour : ${findViewById<Spinner>(R.id.hourSpinner).selectedItem}"
        dialogView.findViewById<TextView>(R.id.ticketsSeats_Buy).text =
            "Selected Seats : ${sitsToString()}"
        dialogView.findViewById<TextView>(R.id.ticketsPrice_Buy).text =
            "Tickets Price : ${findViewById<TextView>(R.id.totalPrice).text}$"



        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()

        val firstName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.lastNameText).text
        val lastName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.firstNameText).text
        val email = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.emailEditText).text
        val phone = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.phoneEditText).text



        dialogView.findViewById<Button>(R.id.confirmBtn).setOnClickListener {
            //TODO: Bottom dialog

            val bottomSheetdDialog = BottomSheetDialog(this)
            val dialogViewBottom = layoutInflater.inflate(R.layout.bottom_dialog_confirm_purchase, null)
            dialogViewBottom.findViewById<TextView>(R.id.sumPurchase_bd)?.text = "Theater City : ${findViewById<TextView>(R.id.movieTheater_buy).text}"
            dialogViewBottom.findViewById<TextView>(R.id.movieTheater_bd)?.text = "Movie Name : ${findViewById<TextView>(R.id.movieName_buy).text}"
            dialogViewBottom.findViewById<TextView>(R.id.movieName_bd)?.text = "Movie Date : ${findViewById<TextView>(R.id.movieDate_buy).text}"
            dialogViewBottom.findViewById<TextView>(R.id.movieDate_bd)?.text = "Movie Hour : ${findViewById<TextView>(R.id.movieHour_buy).text}"
            dialogViewBottom.findViewById<TextView>(R.id.movieHour_bd)?.text = "Selected Seats : ${findViewById<TextView>(R.id.ticketsSeats_Buy).text}"
            dialogViewBottom.findViewById<TextView>(R.id.ticketsSeats_bd)?.text = "Tickets Price : ${findViewById<TextView>(R.id.ticketsPrice_Buy).text}"
            dialogViewBottom.findViewById<TextView>(R.id.firstName_bd)?.text = "First Name : $firstName"
            dialogViewBottom.findViewById<TextView>(R.id.lastName_bd)?.text = "Last Name : $lastName"
            dialogViewBottom.findViewById<TextView>(R.id.email_bd)?.text = "Email Address : $email"
            dialogViewBottom.findViewById<TextView>(R.id.phone_bd)?.text = "Email Address : $phone"


            bottomSheetdDialog.setContentView(dialogViewBottom)
            bottomSheetdDialog.show()

            dialog.dismiss()
        }

    }

    fun sitsToString(): String {
        var sitsString = ""
        for (i in 0..17) {
            if (aviableSits[i] == 2) {
                when (i) {
                    in 0..4 -> sitsString += "Row 1 Seat ${i + 1} "//row 1
                    in 5..9 -> sitsString += "Row 2 Seat ${i + 1} "//row 2
                    in 10..13 -> sitsString += "Row 3 Seat ${i + 1} "//row 3
                    in 14..17 -> sitsString += "Row 4 Seat ${i + 1} "//row 4

                }
            }
        }

        return sitsString
    }

    fun initButtonList(): List<ToggleButton> {
        return listOf(
            findViewById(R.id.tBtn1),
            findViewById(R.id.tBtn2),
            findViewById(R.id.tBtn3),
            findViewById(R.id.tBtn4),
            findViewById(R.id.tBtn5),
            findViewById(R.id.tBtn6),
            findViewById(R.id.tBtn7),
            findViewById(R.id.tBtn8),
            findViewById(R.id.tBtn9),
            findViewById(R.id.tBtn10),
            findViewById(R.id.tBtn11),//cripple
            findViewById(R.id.tBtn12),//cripple
            findViewById(R.id.tBtn13),
            findViewById(R.id.tBtn14),
            findViewById(R.id.tBtn15),
            findViewById(R.id.tBtn16),
            findViewById(R.id.tBtn17),
            findViewById(R.id.tBtn18)
        )
    }

    private fun countSelectedSits(
        count: Int,
        isChecked: Boolean,
        i: Int,
        buttonList: List<ToggleButton>
    ): Int {
        val selectedSeats = findViewById<TextView>(R.id.selectedSeats)
        val totalPrice = findViewById<TextView>(R.id.totalPrice)
        var count = count
        if (isChecked) {
            count++
            aviableSits[i] = 2
        } else {
            count--
            aviableSits[i] = 1
        }

        if (count == findViewById<TextView>(R.id.totalTickets).text.toString().toInt())
            disableButtons(buttonList)
        else
            enableButtons(buttonList)

        totalPrice.text = "${(count * 5)}$"
        selectedSeats.text = (count).toString()
        return count
    }

    fun disableButtons(buttonList: List<ToggleButton>) {
        for (i in 0..17) {
            if (aviableSits[i] != 2 && aviableSits[i] != 0) {
                buttonList[i].isActivated = false
                buttonList[i].isClickable = false
            }
        }
    }

    fun enableButtons(buttonList: List<ToggleButton>) {
        for (i in 1..17) {
            if (aviableSits[i] != 2 && aviableSits[i] != 0) {
                buttonList[i].isActivated = true
                buttonList[i].isClickable = true
            }
        }
    }

}


