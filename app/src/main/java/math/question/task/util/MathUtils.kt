package math.question.task.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object MathUtils {


    fun getRandomNumber(): Int {
        val r = Random()
        val Low = 10000
        val High = 1000000000
        return (System.currentTimeMillis() % Integer.MAX_VALUE).toInt() + (r.nextInt(High - Low) + Low)
    }
}