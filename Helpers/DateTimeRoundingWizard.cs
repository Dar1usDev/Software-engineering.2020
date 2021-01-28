using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyFunds.Helpers
{
    public static class DateTimeRoundingWizard
    {
        public static DateTime FloorByYear(DateTime date)
        {
            return new DateTime().AddYears(date.Year - 1);
        }

        public static DateTime FloorByMonth(DateTime date)
        {
            return FloorByYear(date).Add(TimeSpan.FromTicks(new DateTime().AddMonths(date.Month - 1).Ticks)).AddDays(1);
        }

        public static DateTime FloorByWeek(DateTime date)
        {
            int dayOfWeekModifier = 0;
            switch(date.DayOfWeek)
            {
                case (DayOfWeek.Tuesday): dayOfWeekModifier = -1; break;
                case (DayOfWeek.Wednesday): dayOfWeekModifier = -2; break;
                case (DayOfWeek.Thursday): dayOfWeekModifier = -3; break;
                case (DayOfWeek.Friday): dayOfWeekModifier = -4; break;
                case (DayOfWeek.Saturday): dayOfWeekModifier = -5; break;
                case (DayOfWeek.Sunday): dayOfWeekModifier = -6; break;
            }
            return FloorByYear(date).Add(TimeSpan.FromTicks(new DateTime().AddMonths(date.Month - 1).Ticks)).Add(TimeSpan.FromTicks(new DateTime().AddDays(date.Day + dayOfWeekModifier).Ticks));
        }

        public static DateTime FloorByDay(DateTime date)
        {
            return FloorByYear(date).Add(TimeSpan.FromTicks(new DateTime().AddMonths(date.Month - 1).Ticks)).Add(TimeSpan.FromTicks(new DateTime().AddDays(date.Day).Ticks));
        }
    }
}
