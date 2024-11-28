import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.CalendarDay

class CaixaAzulDecorator(
    private val initialDates: List<CalendarDay>,
    private val drawable: Drawable
) : DayViewDecorator {

    val dates = mutableListOf<CalendarDay>()

    init {
        dates.addAll(initialDates)
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(drawable)
    }

    fun updateDates(newDates: List<CalendarDay>) {
        dates.clear()
        dates.addAll(newDates)
    }
}
