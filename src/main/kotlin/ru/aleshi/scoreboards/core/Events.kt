package ru.aleshi.scoreboards.core

import ru.aleshi.scoreboards.data.Team
import ru.aleshi.scoreboards.data.WarningCategory
import kotlin.math.absoluteValue

interface Event {

    companion object {
        @JvmStatic
        fun printTeam(team: Team) =
            when (team) {
                Team.LEFT -> "<font color='red'>красным</font>"
                Team.RIGHT -> "<font color='blue'>синим</font>"
            }

        @JvmStatic
        fun printCategory(category: WarningCategory) =
            when (category) {
                WarningCategory.TypeA -> "<b>A</b>"
                WarningCategory.TypeM -> "<b>M</b>"
                WarningCategory.TypeD -> "<b>D</b>"
            }
    }

    fun toFormattedString(): String

    class WarningEvent(private val targetTeam: Team, private val category: WarningCategory, private val amount: Int) :
        Event {
        override fun toFormattedString(): String =
            "<html><b>+$amount</b> ${printCategory(category)} ${plural()} ${printTeam(targetTeam)}</html>"

        private fun plural(): String =
            when (amount) {
                1 -> "предупреждение"
                else -> "предупреждения"
            }
    }

    class PointEvent(private val targetTeam: Team, private val amount: Int) : Event {

        override fun toFormattedString(): String =
            "<html><b>${sign()} ${amount.absoluteValue}</b> ${plural()} ${printTeam(targetTeam)}</html>"

        private fun sign(): Char =
            if (amount < 0) '-'
            else '+'

        private fun plural(): String =
            when (amount) {
                1 -> "очко"
                2 -> "очка"
                else -> "очков"
            }
    }
}