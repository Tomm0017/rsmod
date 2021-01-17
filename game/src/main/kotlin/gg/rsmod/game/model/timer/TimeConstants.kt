package gg.rsmod.game.model.timer

import java.lang.StringBuilder
import java.text.DecimalFormat
import kotlin.math.roundToInt

object TimeConstants {
    const val CYCLES_PER_MINUTE = 100
    const val CYCLES_PER_HOUR = 6000
    const val CYCLES_PER_DAY = 144000
    const val CYCLES_PER_WEEK = 1008000
    const val CYCLES_PER_YEAR = 52416000

    const val SECOND = 1000
    const val MINUTE = 60000
    const val HOUR = 3600000
    const val DAY = 86400000
    const val WEEK = 604800000
    const val YEAR = 31449600000 // long

    /**
     * Negative values will return null as they do not make
     * sense in the context of game cycles
     *
     * _Approximate_ calculations for time to cycles conversions
     *   Note| with a cycle of 600ms = .6s some truncation will
     *   occur, but this is sufficient for adequate layman's abstractions
     *
     * @param seconds - number of seconds to roughly convert to cycles
     *
     * @return number of cycles approximately equal to the [seconds]
     */
    fun secondsToCycles(seconds: Int): Int? {
        val secs = (seconds * 0.6).roundToInt()
        return if(secs > 0) secs else null
    }

    fun minutesToCycles(minutes: Int): Int? {
        val mins = minutes * 100
        return if(mins > 0) mins else null
    }

    fun hoursToCycles(hours: Int): Int? = minutesToCycles(hours)?.times(60)

    fun daysToCycles(days: Int): Int? = hoursToCycles(days)?.times(24)

    fun weeksToCycles(weeks: Int): Int? = daysToCycles(weeks)?.times(7)

    fun yearsToCycles(years: Int): Int? = weeksToCycles(years)?.times(52)

    /**
     * Cycles to other time measurements
     */
    fun cyclesToSeconds(cycles: Int): Int? {
        val seconds = (cycles / 0.6).roundToInt()
        return if(seconds > 0) seconds else null
    }

    fun cyclesToMinutes(cycles: Int): Int? {
        val minutes = (cycles / 100.0).toInt()
        return if (minutes > 0) minutes else null
    }

    fun cyclesToHours(cycles: Int): Int? {
        val hours = (cycles / 6000.0).toInt()
        return if (hours > 0) hours else null
    }

    fun cyclesToDays(cycles: Int): Int? {
        val days = (cycles / 144000.0).toInt()
        return if (days > 0) days else null
    }

    fun cyclesToWeeks(cycles: Int): Int? {
        val weeks = (cycles / 1008000.0).toInt()
        return if (weeks > 0) weeks else null
    }

    fun cyclesToYears(cycles: Int): Int? {
        val years = (cycles / 52416000.0).toInt()
        return if (years > 0) years else null
    }

    fun timePassed(last: Long): Long {
        return System.currentTimeMillis() - last
    }

    fun getTimeContext(time: Long): String {
        val sb = StringBuilder()
        var value = time
        var amount: Double
        var formatted: String

        val df = DecimalFormat("#.#")

        when {
            value < MINUTE -> {
                amount = value / SECOND.toDouble()
                formatted = df.format(amount)
                sb.append("$formatted ${if(amount > 1) "seconds" else "second"}")
            }

            value < HOUR -> {
                amount = value / MINUTE.toDouble()
                formatted = df.format(amount)
                sb.append("$formatted ${if(amount > 1) "minutes" else "minute"}")
            }

            value < DAY -> {
                amount = value / HOUR.toDouble()
                formatted = df.format(amount)
                sb.append("$formatted ${if(amount > 1) "hours" else "hour"}")
            }

            // anything less than 84 days (three 28-day months) is displayed in days
            value < DAY.toLong() * 84 -> {
                amount = value / DAY.toDouble()
                sb.append("${amount.toInt()} ${if(amount.toInt() > 1) "days" else "day"}")
            }

            // anything else less than or equal to a year is in months
            value < YEAR -> {
                amount = value / (DAY.toLong() * 28).toDouble()
                sb.append("${amount.toInt()} ${if(amount.toInt() > 1) "months" else "month"}")
            }

            else -> {
                amount = value / YEAR.toDouble()
                formatted = df.format(amount)
                sb.append("$formatted ${if (amount > 1) "years" else "year"}")
            }
        }

        return sb.toString()
    }

    fun getBriefTimeContext(time: Long): String {
        val complete = getCompleteTimeContext(time)
        return if(complete.count { it == ' ' } > 2){
            val parts = complete.split(' ')
            "${parts[0]} ${parts[1]} ${parts[2]}"
        } else
            complete
    }

    fun getCompleteTimeContext(time: Long): String {
        val sb = StringBuilder()
        var value = time
        var amount = 0
        if(value >= YEAR){
            amount = (value / YEAR).toInt()
            sb.append("$amount ${if(amount > 1) "years" else "year"}")
            value %= YEAR
            if(value.toInt() != 0) sb.append(" ")
        }

        if(value >= WEEK){
            amount = (value / WEEK).toInt()
            sb.append("$amount ${if(amount > 1) "weeks" else "week"}")
            value %= WEEK
            if(value.toInt() != 0) sb.append(" ")
        }

        if(value >= DAY){
            amount = (value / DAY).toInt()
            sb.append("$amount ${if(amount > 1) "days" else "day"}")
            value %= DAY
            if(value.toInt() != 0) sb.append(" ")
        }

        if(value >= HOUR){
            amount = (value / HOUR).toInt()
            sb.append("$amount ${if(amount > 1) "hours" else "hour"}")
            value %= HOUR
            if(value.toInt() != 0) sb.append(" ")
        }

        if(value >= MINUTE){
            amount = (value / MINUTE).toInt()
            sb.append("$amount ${if(amount > 1) "minutes" else "minute"}")
            value %= MINUTE
            if(value.toInt() != 0) sb.append(" ")
        }

        if(value >= SECOND){
            amount = (value / SECOND).toInt()
            sb.append("$amount ${if(amount > 1) "seconds" else "second"}")
            value %= SECOND
        }

        // we don't care about ms
        return sb.toString()
    }
}