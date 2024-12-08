package com.example.appcomida.ui.Calendario

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class LinhaVermelha(private val dates: MutableList<CalendarDay>) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(LineSpan(Color.RED, 3f, 50f)) // Linha vermelha com largura 50f
    }

    fun addDate(date: CalendarDay) {
        dates.add(date)
    }

    fun removeDate(date: CalendarDay) {
        dates.remove(date)
    }
}