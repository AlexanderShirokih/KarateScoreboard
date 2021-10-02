package ru.aleshi.scoreboards.core

import ru.aleshi.scoreboards.data.Team
import ru.aleshi.scoreboards.data.WarningCategory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

abstract class Event {

    private val date = Date()

    companion object {
        private val dateFormat = SimpleDateFormat("HH:mm:ss")

        @JvmStatic
        fun printTeam(team: Team) =
            when (team) {
                Team.RED -> "<font color='red'>красным</font>"
                Team.BLUE -> "<font color='blue'>синим</font>"
            }

        @JvmStatic
        fun printCategory(category: WarningCategory) =
            when (category) {
                WarningCategory.TypeA -> "<b>A</b>"
                WarningCategory.TypeM -> "<b>M</b>"
                WarningCategory.TypeD -> "<b>D</b>"
            }
    }

    protected fun printDate(): String = "<i>${dateFormat.format(date)}</i>"

    abstract fun toFormattedString(): String

    class WarningEvent(private val targetTeam: Team, private val category: WarningCategory, private val amount: Int) :
        Event() {
        override fun toFormattedString(): String =
            "<html>${printDate()}: <b>+$amount</b> ${printCategory(category)} ${plural()} ${printTeam(targetTeam)}</html>"

        private fun plural(): String =
            when (amount.absoluteValue) {
                1 -> "предупреждение"
                else -> "предупреждения"
            }
    }

    class PointEvent(private val targetTeam: Team, private val amount: Int) : Event() {

        override fun toFormattedString(): String =
            "<html>${printDate()}: <b>${amount.signChar} ${amount.absoluteValue}</b> ${plural()} ${printTeam(targetTeam)}</html>"

        private fun plural(): String =
            when (amount.absoluteValue) {
                1 -> "очко"
                else -> "очка"
            }
    }

    class TimeEvent(private val seconds: Int) : Event() {

        override fun toFormattedString(): String =
            "<html>${printDate()}: <b>${seconds.signChar} ${seconds.absoluteValue}</b> секунд</html>"

    }

    open class MessageEvent(private val message: String) : Event() {
        override fun toFormattedString(): String {
            return "<html>${printDate()}: $message</html>"
        }
    }

    object TimeOutEvent : MessageEvent("<b>время вышло!</b>")

    internal val Int.signChar
        get() =
            if (this < 0) '-'
            else '+'
}