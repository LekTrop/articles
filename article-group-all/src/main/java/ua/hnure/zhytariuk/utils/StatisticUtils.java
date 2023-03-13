package ua.hnure.zhytariuk.utils;

import java.time.YearMonth;
import java.util.stream.IntStream;

public class StatisticUtils {
    public static int [] getMonthDays(final int year, final int monthNumber){
        final YearMonth yearMonth = YearMonth.of(year, monthNumber);

        return IntStream.range(1, yearMonth.lengthOfMonth() + 1).toArray();
    }
}
