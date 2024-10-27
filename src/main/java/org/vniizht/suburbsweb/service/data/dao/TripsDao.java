package org.vniizht.suburbsweb.service.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.data.entities.handbook.SeasonTrip;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigMain;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T1;
import org.vniizht.suburbsweb.service.handbook.HandbookCache;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TripsDao {

    @Autowired
    private HandbookCache handbookCache;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");

    public Set<T1> multiplyByTrips(T1 t1, PrigMain prigMain) {

        Set<T1> t1Set = new LinkedHashSet<>();

        if (prigMain.abonement_type.charAt(0) != '0')
            calculateTripsPerMonth(
                    (short) switch (prigMain.abonement_type.charAt(0)) {
                        case '1' -> 9; // билет на количество поездок
                        case '2' -> 7; // билет на определенные даты
                        case '3' -> 1; // билет «ежедневно» (помесячный)
                        case '4' -> 2; // билет «ежедневно» (посуточный)
                        case '5' -> 8; // билет «выходного дня»
                        case '7' -> 3; // билет «рабочего дня» (помесячный)
                        case '8' -> 4;
                        default -> // билет «рабочего дня» (посуточный)
                                0;
                    },
                    prigMain.seatstick_limit,
                    prigMain.operation_date,
                    prigMain.ticket_begdate,
                    prigMain.ticket_enddate)
                    .forEach((month, trips) -> t1Set.add(t1.toBuilder()
                            .key(t1.getKey().toBuilder()
                                    .report_yyyymm(month)
                                    .p2(t1.getKey().getP2() + 1)
                                    .build())
                            .p33(Long.valueOf(trips))
                            .build()));

        if(t1Set.isEmpty())
            t1Set.add(t1);

        return t1Set;
    }

    private Map<String, Integer> calculateTripsPerMonth(Short ticketCode,
                                                        Short period,
                                                        Date saleDate,
                                                        Date begDate,
                                                        Date endDate) {

        SeasonTrip seasonTrip = handbookCache.findTrip(ticketCode, period, begDate);
        if (seasonTrip == null) return new HashMap<>();

        int   saleMonth = saleDate.getMonth(), saleYear = saleDate.getYear();
        int   begMonth  = begDate.getMonth(),  begYear  = begDate.getYear();
        int   endMonth  = endDate.getMonth(),  endYear  = endDate.getYear();
        Map <String, Integer> collectorBefore = new LinkedHashMap<>();
        Map <String, Integer> collector = new LinkedHashMap<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(saleDate);
        while (calendar.getTime().getTime() <= begDate.getTime()) {
            collectorBefore.put(formatter.format(calendar.getTime()), 0);
            calendar.add(Calendar.MONTH, 1);
        }
        calendar.setTime(begDate);
        int totalTrips = 0;
        while (calendar.getTime().getTime() <= endDate.getTime()) {
            int     day           = calendar.get(Calendar.DAY_OF_MONTH);
            int     month         = calendar.get(Calendar.MONTH);
            int     year          = calendar.get(Calendar.YEAR) - 1900;
            boolean isStart       = month == begMonth && year == begYear;
            boolean isEnd         = month == endMonth && year == endYear;
            int     maxDays       = 30;
            int     involvedDays  = isEnd ? day : maxDays - day + 1;
            int     monthTrips    = seasonTrip.getKol_trips();
            int     involvedTrips =
                    isStart ? Math.round((float) (monthTrips * involvedDays) / maxDays) :
                            isEnd ? monthTrips * collector.size() - totalTrips
                                    : monthTrips;

            totalTrips += involvedTrips;

            collector.put(formatter.format(calendar.getTime()), involvedTrips);
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, isEnd ? endDate.getDate() : 1);
        }
        return collector;
    }

//    @PostConstruct
    private void test(){
//        calculateTripsPerMonth(
//                (short) 9,
//                (short) 120,
//                new Date(2024 - 1900, Calendar.DECEMBER, 15),
//                new Date(2024 - 1900, Calendar.DECEMBER, 16))
//                .forEach((k, v) -> System.out.println(k + " " + v));

        calculateTripsPerMonth(
                (short) 9,
                (short) 90,
                new Date(2024 - 1900, Calendar.JUNE, 10),
                new Date(2024 - 1900, Calendar.JUNE, 27),
                new Date(2024 - 1900, Calendar.OCTOBER, 24))
                .forEach((k, v) -> System.out.println(k + " " + v));

//        calculateTripsPerMonth(
//                (short) 1,
//                (short) 1,
//                new Date(2024 - 1900, Calendar.JULY, 26),
//                new Date(2024 - 1900, Calendar.OCTOBER, 25))
//                .forEach((k, v) -> System.out.println(k + " " + v));
    }
}
