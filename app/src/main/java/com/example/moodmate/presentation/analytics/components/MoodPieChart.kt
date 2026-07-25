package com.example.moodmate.presentation.analytics.components

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.moodmate.data.local.MoodEntity
import com.example.moodmate.domain.model.Mood
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MoodPieChart(
    moodDistribution: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                setUsePercentValues(true)
                setExtraOffsets(5f, 10f, 5f, 5f)
                dragDecelerationFrictionCoef = 0.95f
                isDrawHoleEnabled = true
                setHoleColor(Color.WHITE)
                setTransparentCircleColor(Color.WHITE)
                setTransparentCircleAlpha(110)
                holeRadius = 58f
                transparentCircleRadius = 61f
                setDrawCenterText(true)
                rotationAngle = 0f
                isRotationEnabled = true
                setCenterText("Mood\nDistribution")
                setCenterTextSize(16f)
            }
        },
        update = { pieChart ->
            val entries = moodDistribution.map { (mood, count) ->
                PieEntry(count.toFloat(), mood)
            }

            val dataSet = PieDataSet(entries, "Moods").apply {
                colors = listOf(
                    Color.rgb(76, 175, 80),   // Happy - Green
                    Color.rgb(255, 152, 0),   // Neutral - Orange
                    Color.rgb(156, 39, 176),  // Tired - Purple
                    Color.rgb(158, 158, 158)  // Unknown - Grey
                )
                valueTextSize = 12f
                valueTextColor = Color.WHITE
            }

            pieChart.data = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter())
                setValueTextSize(12f)
                setValueTextColor(Color.WHITE)
            }

            pieChart.invalidate()
        },
        modifier = modifier
    )
}

@Composable
fun MoodLineChart(
    moods: List<MoodEntity>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setDragEnabled(true)
                setScaleEnabled(true)
                setPinchZoom(true)
                setDrawGridBackground(false)
                setDrawBorders(false)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    textSize = 10f
                    valueFormatter = IndexAxisValueFormatter(
                        moods.map {
                            SimpleDateFormat("dd MMM", Locale.getDefault())
                                .format(Date(it.createdAt))
                        }
                    )
                    labelRotationAngle = -45f
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    setDrawAxisLine(true)
                    textSize = 10f
                    axisMinimum = 0f
                    axisMaximum = 4f
                }

                axisRight.isEnabled = false

                legend.isEnabled = true
                legend.textSize = 10f
            }
        },
        update = { lineChart ->
            val entries = moods.mapIndexed { index, mood ->
                val moodValue = when (mood.mood) {
                    Mood.HAPPY -> 3f
                    Mood.NEUTRAL -> 2f
                    Mood.TIRED -> 1f
                    Mood.UNKNOWN -> 0f
                }
                Entry(index.toFloat(), moodValue)
            }

            val dataSet = LineDataSet(entries, "Mood Trend").apply {
                color = Color.rgb(33, 150, 243)
                setCircleColor(Color.rgb(33, 150, 243))
                lineWidth = 2f
                circleRadius = 4f
                setDrawCircleHole(false)
                setDrawFilled(true)
                fillColor = Color.rgb(33, 150, 243)
                fillAlpha = 50
                setDrawValues(true)
                valueTextSize = 10f
                valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return when (value.toInt()) {
                            3 -> "😊"
                            2 -> "😐"
                            1 -> "😴"
                            else -> "❓"
                        }
                    }
                }
            }

            lineChart.data = LineData(dataSet)
            lineChart.invalidate()
        },
        modifier = modifier
    )
}

@Composable
fun MoodBarChart(
    moodsByDay: Map<String, Map<Mood, Int>>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setDragEnabled(true)
                setScaleEnabled(true)
                setPinchZoom(true)
                setDrawGridBackground(false)
                setDrawBorders(false)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    textSize = 10f
                    labelRotationAngle = -45f
                    valueFormatter = IndexAxisValueFormatter(moodsByDay.keys.toList())
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    setDrawAxisLine(true)
                    textSize = 10f
                    axisMinimum = 0f
                }

                axisRight.isEnabled = false

                legend.isEnabled = true
                legend.textSize = 10f
            }
        },
        update = { barChart ->
            val days = moodsByDay.keys.toList()
            val entries = mutableListOf<BarEntry>()

            days.forEachIndexed { index, day ->
                val dayMoods = moodsByDay[day] ?: emptyMap()
                val happy = (dayMoods[Mood.HAPPY] ?: 0).toFloat()
                val neutral = (dayMoods[Mood.NEUTRAL] ?: 0).toFloat()
                val tired = (dayMoods[Mood.TIRED] ?: 0).toFloat()

                entries.add(BarEntry(index.toFloat(), floatArrayOf(happy, neutral, tired)))
            }

            val dataSet = BarDataSet(entries, "Daily Moods").apply {
                colors = listOf(
                    Color.rgb(76, 175, 80),   // Happy
                    Color.rgb(255, 152, 0),   // Neutral
                    Color.rgb(156, 39, 176)   // Tired
                )
                stackLabels = arrayOf("Happy", "Neutral", "Tired")
                valueTextSize = 10f
                setDrawValues(true)
            }

            val data = BarData(dataSet).apply {
                barWidth = 0.7f
            }

            barChart.data = data
            barChart.invalidate()
        },
        modifier = modifier
    )
}