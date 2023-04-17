package ua.hnure.zhytariuk.utils;

import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder(toBuilder = true)
public class DateStatisticApi {
    private int year;
    private int month;
    private int[] monthDays;
}
