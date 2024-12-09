package org.vniizht.suburbsweb.service.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.data.entities.handbook.SeasonTrip;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigMain;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T1;
import org.vniizht.suburbsweb.service.handbook.HandbookCache;
import org.vniizht.suburbsweb.util.Util;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class TripsDao {

    @Autowired
    private HandbookCache handbookCache;

    public Map<String, Integer> calculateTripsPerMonth(PrigMain main) {
        return calculateTripsPerMonth(
                handbookCache.findTrip(
                        abonementType2ticketCode(main.abonement_subtype, main.abonement_type),
                        main.seatstick_limit,
                        main.ticket_begdate),
                main.operation_date,
                main.ticket_begdate,
                main.ticket_enddate
        );
    }

    public Map<String, Integer> calculateTripsPerMonth(SeasonTrip seasonTrip,
                                                        Date saleDate,
                                                        Date begDate,
                                                        Date endDate) {

        Map<String, Integer> totalTripsPerMonth = new LinkedHashMap<>();
        calculateDaysWithTripsPerMonth(saleDate, begDate, endDate)
                .forEach((yyyymm, daysWithTrips) ->
                        totalTripsPerMonth
                                .put(yyyymm, seasonTrip == null ? 0 :
                                        Math.round(((float) seasonTrip.getKol__round_trips() / 2)
                                                * ((float) daysWithTrips / 31))
                                )
                );

        return totalTripsPerMonth;
    }

    private Map<String, Byte> calculateDaysWithTripsPerMonth(Date saleDate,
                                                             Date begDate,
                                                             Date endDate) {
        Map<String, Byte> daysWithTripsPerMonth = new LinkedHashMap<>();

        for (Date iterDate = new Date(Math.min(saleDate.getTime(), begDate.getTime()));
             iterDate.before(endDate) || iterDate.equals(endDate);
             iterDate = new Date(iterDate.getTime() + 86400000)) {

            byte daysWithTrips = daysWithTripsPerMonth.computeIfAbsent(yyyymm(iterDate), k -> (byte) 0);
            if (iterDate.getTime() >= begDate.getTime())
                daysWithTrips++;

            daysWithTripsPerMonth.put(yyyymm(iterDate), daysWithTrips);
        }
        return daysWithTripsPerMonth;
    }

    private short abonementType2ticketCode(Character abonementSubtype, String abonementType) {
        switch (abonementType.charAt(0)) {
            case '1': return 9; // билет на количество поездок
            case '2': switch (abonementSubtype) {
                case '1': return 6;
                case '2': return 5;
                default : return 7; // билет на определенные даты
            }
            case '3': return 1; // билет «ежедневно» (помесячный)
            case '4': return 2; // билет «ежедневно» (посуточный)
            case '5':
            case '6': return 8; // билет «выходного дня»
            case '7': return 3; // билет «рабочего дня» (помесячный)
            case '8': return 4; // билет «рабочего дня» (посуточный)
            default : return 0;
        }
    }

    private String yyyymm(Date date) {
        return Util.formatDate(date, "yyyyMM");
    }

//    @PostConstruct
    private void test() {

        handbookCache.load();

        calculateTripsPerMonth(
                handbookCache.findTrip(
                        (short) 9,
                        (short) 90
                ),
                new Date(2024 - 1900, Calendar.MAY, 10),
                new Date(2024 - 1900, Calendar.JUNE, 27),
                new Date(2024 - 1900, Calendar.OCTOBER, 24))
                .forEach((k, v) -> System.out.println(k + " " + v));
    }
}
