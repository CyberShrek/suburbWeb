package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.handbook.SeasonTrip;
import org.vniizht.suburbsweb.model.transformation.level2.PrigMain;
import org.vniizht.suburbsweb.model.transformation.level3.co22.T1;
import org.vniizht.suburbsweb.service.handbook.HandbookHolder;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class Trips {

    @Autowired
    private HandbookHolder handbookHolder;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");

    public Set<T1> multiplyByTrips(T1 t1, PrigMain prigMain) {

        Set<T1> t1Set = new LinkedHashSet<>();

        if (prigMain.getAbonement_type().charAt(0) != '0')
            calculateTripsPerMonth(
                    Converter.convertSeasonTicketCode(prigMain.getAbonement_type().charAt(0)),
                    prigMain.getSeatstick_limit(),
                    prigMain.getRequestDate().before(prigMain.getTicket_begdate()) ? prigMain.getTicket_begdate() : prigMain.getRequestDate(),
                    prigMain.getTicket_enddate())
                    .forEach((month, trips) -> t1Set.add(t1.toBuilder()
                            .yyyymm(month)
                            .p33(Long.valueOf(trips)).build()));
        else
            t1Set.add(t1);

        return t1Set;
    }

    private Map<String, Integer> calculateTripsPerMonth(Short ticketCode,
                                                        Short period,
                                                        Date begDate,
                                                        Date endDate) {
        Map <String, Integer> collector = new LinkedHashMap<>();
        SeasonTrip seasonTrip = handbookHolder.findTrip(ticketCode, period, begDate);
        if (seasonTrip == null)
            return collector;

        int    begMonth = begDate.getMonth(), begYear = begDate.getYear();
        int    endMonth = endDate.getMonth(), endYear = endDate.getYear();
        float  totalTrips = 0;

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(begDate);
        while (calendar.getTime().getTime() <= endDate.getTime()) {
            int     day           = calendar.get(Calendar.DAY_OF_MONTH);
            int     month         = calendar.get(Calendar.MONTH);
            int     year          = calendar.get(Calendar.YEAR) - 1900;
            boolean isStart       = month == begMonth && year == begYear;
            boolean isEnd         = month == endMonth && year == endYear;
            int     maxDays       = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int     involvedDays  = isEnd ? day : maxDays - day + 1;
            float   monthTrips    = isStart | isEnd ? (float) seasonTrip.getKol__round_trips() / 2 : seasonTrip.getKol_trips();
            float   involvedTrips =
                    isStart ? monthTrips * involvedDays / maxDays :
                            isEnd ? monthTrips * collector.size() - totalTrips
                                    : monthTrips;

            totalTrips += involvedTrips;

            collector.put(formatter.format(calendar.getTime()), Math.round(involvedTrips));
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, isEnd ? endDate.getDate() : 1);
        }
        return collector;
    }

    @PostConstruct
    private void test(){
        calculateTripsPerMonth(
                (short) 9,
                (short) 120,
                new Date(2024 - 1900, Calendar.DECEMBER, 15),
                new Date(2024 - 1900, Calendar.DECEMBER, 16))
                .forEach((k, v) -> System.out.println(k + " " + v));

        calculateTripsPerMonth(
                (short) 9,
                (short) 90,
                new Date(2024 - 1900, Calendar.JUNE, 27),
                new Date(2024 - 1900, Calendar.OCTOBER, 24))
                .forEach((k, v) -> System.out.println(k + " " + v));

        calculateTripsPerMonth(
                (short) 1,
                (short) 1,
                new Date(2024 - 1900, Calendar.JULY, 26),
                new Date(2024 - 1900, Calendar.OCTOBER, 25))
                .forEach((k, v) -> System.out.println(k + " " + v));
    }
}
