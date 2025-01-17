package com.telkom21.anaksehat

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.databinding.ActivityPastHistoryBinding
import com.telkom21.anaksehat.utils.HistoryDataClass
import com.telkom21.anaksehat.utils.calculateIMT
import com.telkom21.anaksehat.utils.calculateZScore
import com.telkom21.anaksehat.utils.getBMICategoryFromZScore
import com.telkom21.anaksehat.utils.getIMTReference

class PastHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPastHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityPastHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val nama = intent.getStringExtra("nama")
        val umur = intent.getStringExtra("umur")
        val jenisKelamin = intent.getStringExtra("jenisKelamin")
        val berat = intent.getStringExtra("berat")
        val tinggi = intent.getStringExtra("tinggi")

        binding.nama.text = nama
        binding.umur.text = umur
        binding.jenisKelamin.text = jenisKelamin
        binding.beratBadan.text = berat
        binding.tinggiBadan.text = tinggi
        calculateAndSetBMI(berat, tinggi, umur, jenisKelamin)

        val historyList = intent.getParcelableArrayListExtra<HistoryDataClass>("history_list")
        if (historyList != null) {
            setupLineChart(binding.lineChart)
            updateLineChart(historyList)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun calculateAndSetBMI(beratStr: String?, tinggiStr: String?, umurStr: String?, jenisKelamin: String?) {
        val berat = beratStr?.toDoubleOrNull()
        val tinggi = tinggiStr?.toDoubleOrNull()
        val umur = umurStr?.toIntOrNull()

        if (berat != null && tinggi != null && umur != null && jenisKelamin != null) {
            val bmi = calculateIMT(berat, tinggi)
            binding.IMT.text = String.format("%.2f", bmi)

            val reference = getIMTReference(jenisKelamin, umur)
            if (reference != null) {
                val zScore = calculateZScore(bmi, reference)
                val category = getBMICategoryFromZScore(zScore)
                binding.category.text = category
                giveSuggestion(category)
            }
        } else {
            binding.IMT.text = "-"
            binding.category.text = "-"
        }
    }

    private fun giveSuggestion(category: String) {
        val saran = binding.saran
        when (category) {
            "Gizi Buruk" -> saran.text =
                getString(R.string.saranGiziBuruk)

            "Gizi Kurang" -> saran.text =
                getString(R.string.saranGiziKurang)

            "Gizi Baik" -> saran.text =
                getString(R.string.saranGiziBaik)

            "Beresiko gizi lebih" -> saran.text =
                getString(R.string.saranResikoGiziLebih)

            "Gizi Lebih" -> saran.text =
                getString(R.string.saranGiziLebih)

            "Obesitas" -> saran.text =
                getString(R.string.saranObesitas)

            else -> saran.text = "-"
        }
    }

    private fun setupLineChart(lineChart: LineChart) {
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.apply {
            axisLineColor = ContextCompat.getColor(this@PastHistoryActivity, R.color.white)
            axisLineWidth = 3f
            textColor = ContextCompat.getColor(this@PastHistoryActivity, R.color.white)
            gridColor = ContextCompat.getColor(this@PastHistoryActivity, R.color.sec_white)
        }
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = ContextCompat.getColor(this@PastHistoryActivity, R.color.white)
            isGranularityEnabled = true
            granularity = 4f
            axisLineWidth = 3f
            axisLineColor = ContextCompat.getColor(this@PastHistoryActivity, R.color.white)
            gridColor = ContextCompat.getColor(this@PastHistoryActivity, R.color.sec_white)
        }
    }

    private fun fetchHistoryData(childId: String) {
        val database = FirebaseDatabase.getInstance().reference
        val historyRef = database.child("users").child(childId).child("history")

        historyRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val historyList = mutableListOf<HistoryDataClass>()
                for (dataSnapshot in snapshot.children) {
                    val historyData = dataSnapshot.getValue(HistoryDataClass::class.java)
                    historyData?.let {
                        historyList.add(it)
                    }
                }
                updateLineChart(historyList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(this@PastHistoryActivity, "Failed to load history data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun updateLineChart(historyList: List<HistoryDataClass>) {
        val entries = historyList.mapIndexed { index, data ->
            data.berat?.let {
                Entry(index.toFloat(), it.toFloat())
            }
        }.filterNotNull() // Filter out any null entries

        val lineDataSet = LineDataSet(entries, "Berat badan seiring waktu")
        lineDataSet.lineWidth = 2.5f
        lineDataSet.circleRadius = 4.5f
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        lineDataSet.color = ContextCompat.getColor(this, R.color.white)
        lineDataSet.valueTextColor = ContextCompat.getColor(this, R.color.white)
        lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.ter_blue))

        val lineData = LineData(lineDataSet)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate() // Refresh the chart
    }

}