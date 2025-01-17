package com.telkom21.anaksehat.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.telkom21.anaksehat.PastHistoryActivity
import com.telkom21.anaksehat.R
import com.telkom21.anaksehat.databinding.ActivityChildDetailBinding
import com.telkom21.anaksehat.utils.ChildDataClass
import com.telkom21.anaksehat.utils.HistoryAdapter
import com.telkom21.anaksehat.utils.HistoryDataClass
import com.telkom21.anaksehat.utils.calculateIMT
import com.telkom21.anaksehat.utils.calculateZScore
import com.telkom21.anaksehat.utils.getBMICategoryFromZScore
import com.telkom21.anaksehat.utils.getIMTReference
import java.text.SimpleDateFormat
import java.util.Locale

class ChildDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChildDetailBinding
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val historyList = mutableListOf<HistoryDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityChildDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyRecyclerView = binding.riwayat
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(historyList)
        historyRecyclerView.adapter = historyAdapter

        val childName = intent.getStringExtra("child_name")
        val parentId = intent.getStringExtra("parent_id")
        if (childName != null && parentId != null) {
            fetchChildData(childName, parentId)
            fetchHistory(childName, parentId)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        val lineChart = binding.lineChart
        setupLineChart(lineChart)
    }

    private fun fetchChildData(childName: String, parentId: String) {
        val database = FirebaseDatabase.getInstance().reference
        val badutaRef = database.child("users")
            .child(parentId)
            .child("baduta")
            .orderByChild("nama")
            .equalTo(childName)
        val balitaRef = database.child("users")
            .child(parentId).child("balita").orderByChild("nama")
            .equalTo(childName)

        val fetchData = { ref: Query ->
            ref
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (childSnapshot in snapshot.children) {
                                val childData = childSnapshot
                                    .getValue(ChildDataClass::class.java)
                                if (childData != null) {
                                    binding.nama.text =
                                        childData.nama
                                    binding.umur.text =
                                        childData.umur
                                    binding.beratBadan.text =
                                        childData.berat
                                    binding.tinggiBadan.text =
                                        childData.tinggi
                                    binding.jenisKelamin.text =
                                        childData.jenisKelamin
                                    // Calculate and set BMI and Category
                                    calculateAndSetBMI(childData)
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@ChildDetailActivity,
                                "Data anak tidak ditemukan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@ChildDetailActivity,
                            "Gagal memuat data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        badutaRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    fetchData(badutaRef)
                } else {
                    fetchData(balitaRef)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ChildDetailActivity,
                    "Gagal mengambil data anak", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun fetchHistory(childName: String, parentId: String) {
        val database = FirebaseDatabase.getInstance().reference
        val badutaHistoryRef =
            database.child("users")
                .child(parentId)
                .child("baduta")
                .orderByChild("nama")
                .equalTo(childName)
        val balitaHistoryRef =
            database.child("users")
                .child(parentId)
                .child("balita")
                .orderByChild("nama")
                .equalTo(childName)
        val fetchHistoryData = { ref: Query ->
            ref.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        historyList.clear()
                        for (childSnapshot in snapshot.children) {
                            val historyRef = childSnapshot
                                .child("history")
                            for (dateSnapshot in historyRef.children) {
                                val berat = dateSnapshot
                                    .child("berat")
                                    .getValue(String::class.java)
                                    ?: "Unknown"
                                val tinggi =
                                    dateSnapshot
                                        .child("tinggi")
                                        .getValue(String::class.java)
                                        ?: "Unknown"
                                val tanggal =
                                    dateSnapshot
                                        .child("tanggal")
                                        .getValue(String::class.java)
                                        ?: "Unknown"
                                val historyItem =
                                    HistoryDataClass(tanggal, berat, tinggi)
                                historyList.add(historyItem)
                            }
                        }
                        sortHistoryByDateDescending()
                        historyAdapter.notifyDataSetChanged()
                        updateLineChart(historyList)
                        val historyArrayList = ArrayList(historyList)
                        historyRecyclerView.adapter = historyAdapter
                        historyAdapter.mListener = object : HistoryAdapter.OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                val clickedItem = historyList[position]
                                val intent = Intent(this@ChildDetailActivity, PastHistoryActivity::class.java).apply {
                                    putExtra("nama", binding.nama.text.toString())
                                    putExtra("umur", binding.umur.text.toString())
                                    putExtra("jenisKelamin", binding.jenisKelamin.text.toString())
                                    putExtra("berat", clickedItem.berat)
                                    putExtra("tinggi", clickedItem.tinggi)
                                    putParcelableArrayListExtra("history_list", historyArrayList)
                                }
                                startActivity(intent)
                            }
                        }

                    } else {
                        Toast.makeText(
                            this@ChildDetailActivity,
                            "Tidak ada data riwayat",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@ChildDetailActivity,
                        "Gagal mengambil riwayat",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        badutaHistoryRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    fetchHistoryData(badutaHistoryRef)
                } else {
                    fetchHistoryData(balitaHistoryRef)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ChildDetailActivity,
                    "Gagal mengambil riwayat",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun calculateAndSetBMI(childData: ChildDataClass) {
        val berat = childData.berat?.toDoubleOrNull()
        val tinggi = childData.tinggi?.toDoubleOrNull()
        val umur = childData.umur?.toIntOrNull()
        val gender = childData.jenisKelamin
        if (berat != null && tinggi != null
            && umur != null && gender != null
        ) {
            val bmi = calculateIMT(berat, tinggi)
            binding.IMT.text = String.format("%.2f", bmi)
            val reference = getIMTReference(gender, umur)
            if (reference != null) {
                val zScore = calculateZScore(bmi, reference)
                val category = getBMICategoryFromZScore(zScore)
                binding.category.text = category
                giveSuggestion(category)
            }
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
            axisLineColor = ContextCompat
                .getColor(
                    this@ChildDetailActivity,
                    R.color.white
                )
            axisLineWidth = 3f
            textColor = ContextCompat
                .getColor(
                    this@ChildDetailActivity,
                    R.color.white
                )
            gridColor = ContextCompat
                .getColor(
                    this@ChildDetailActivity,
                    R.color.sec_white
                )
        }
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = ContextCompat
                .getColor(
                    this@ChildDetailActivity,
                    R.color.white
                )
            isGranularityEnabled = true
            granularity = 4f
            axisLineWidth = 3f
            axisLineColor = ContextCompat
                .getColor(
                    this@ChildDetailActivity,
                    R.color.white
                )
            gridColor = ContextCompat
                .getColor(
                    this@ChildDetailActivity,
                    R.color.sec_white
                )
        }
    }

    private fun updateLineChart(
        historyList:
        List<HistoryDataClass>
    ) {
        val entries = historyList.mapIndexed { index, data ->
            data.berat?.let {
                Entry(
                    index.toFloat(),
                    it.toFloat()
                )
            }
        }

        val lineDataSet = LineDataSet(
            entries,
            "Berat badan seiring waktu "
        )
        lineDataSet.lineWidth = 2.5f
        lineDataSet.circleRadius = 4.5f
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        lineDataSet.color = ContextCompat
            .getColor(this, R.color.white)
        lineDataSet.valueTextColor = ContextCompat
            .getColor(this, R.color.white)
        lineDataSet.setCircleColor(
            ContextCompat
                .getColor(this, R.color.ter_blue)
        )

        val lineData = LineData(lineDataSet)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate()
    }

    @SuppressLint("SimpleDateFormat")
    private fun sortHistoryByDateDescending() {
        historyList.sortByDescending { historyItem ->
            try {
                dateFormat.parse(historyItem.tanggal)
            } catch (e: Exception) {
                null
            }
        }
    }
}