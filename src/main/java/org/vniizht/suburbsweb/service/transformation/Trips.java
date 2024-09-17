package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Autowired private JdbcTemplate jdbcTemplate;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");

    public Set<T1> multiplyT1(T1 t1, PrigMain prigMain) {

        short ticketCode = Converter.convertSeasonTicketCode(prigMain.getAbonement_type().charAt(0)); // ?????
        short period     = prigMain.getSeatstick_limit();
        Date begDate     = prigMain.getTicket_begdate();
        Date endDate     = prigMain.getTicket_enddate();

        Set<T1> set = new LinkedHashSet<>();

        set.add(t1);

        if(prigMain.getAbonement_type().charAt(0) != '0')
            calculateTripsPerMonth(ticketCode, period, begDate, endDate);
//            jdbcTemplate.execute(
//                    "select * from getfunction.ch_trips__per__mes('20'::char(2), "+ ticketCode + ", " + period + ", '" + formatter.format(begDate) + "', '" + formatter.format(endDate) + "');");

        return set;
    }

    private Map<String, Integer> calculateTripsPerMonth(Short ticketCode,
                                                        Short period,
                                                        Date begDate,
                                                        Date endDate) {

        SeasonTrip seasonTrip = handbookHolder.findTrip(ticketCode, period, begDate);

        if(seasonTrip != null)
            switch (seasonTrip.getPr_period()) {
                case '1': return calculateTripsPerMonthDaily(seasonTrip, begDate, endDate);
                case '2': return calculateTripsPerMonthMonthly(seasonTrip, begDate, endDate);
            }

        return new HashMap<>();
    }

    private Map<String, Integer> calculateTripsPerMonthDaily(SeasonTrip seasonTrip,
                                                        Date begDate,
                                                        Date endDate) {
        Map <String, Integer> result = new LinkedHashMap<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(begDate);
        float tripsCount = 0;
        while (calendar.getTime().getTime() <= endDate.getTime()) {
            int day       = calendar.get(Calendar.DAY_OF_MONTH);
            int month     = calendar.get(Calendar.MONTH);
            int year      = calendar.get(Calendar.YEAR) - 1900;

            boolean isStart = month == begDate.getMonth() && year == begDate.getYear();
            boolean isEnd   = month == endDate.getMonth() && year == endDate.getYear();

            int maxDays      = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int involvedDays = isEnd ? day : maxDays - day + 1;
            float trips      =
                    isStart ? (float) seasonTrip.getKol__round_trips() / 2 * involvedDays / maxDays :
                            isEnd ? (float) seasonTrip.getKol__round_trips() / 2 * result.size() - tripsCount
                                    : seasonTrip.getKol_trips();

            tripsCount += trips;

            result.put(formatter.format(calendar.getTime()), Math.round(trips));
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, isEnd ? endDate.getDate() : 1);
        }
        return result;
    }

    private Map<String, Integer> calculateTripsPerMonthMonthly(SeasonTrip seasonTrip,
                                                               Date begDate,
                                                               Date endDate) {
        Map <String, Integer> result = new LinkedHashMap<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(begDate);
        float tripsCount = 0;
        while (calendar.getTime().getTime() <= endDate.getTime()) {
            int day       = calendar.get(Calendar.DAY_OF_MONTH);
            int month     = calendar.get(Calendar.MONTH);
            int year      = calendar.get(Calendar.YEAR) - 1900;

            boolean isStart = month == begDate.getMonth() && year == begDate.getYear();
            boolean isEnd   = month == endDate.getMonth() && year == endDate.getYear();

            int maxDays      = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int involvedDays = isEnd ? day : maxDays - day + 1;
            float trips      =
                    isStart ? (float) seasonTrip.getKol__round_trips() / 2 * involvedDays / maxDays :
                            isEnd ? (float) seasonTrip.getKol__round_trips() / 2 * result.size() - tripsCount
                                    : seasonTrip.getKol_trips();

            tripsCount += trips;

            result.put(formatter.format(calendar.getTime()), Math.round(trips));
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, isEnd ? endDate.getDate() : 1);
        }
        return result;
    }

//    private Map<String, Integer>

    @PostConstruct
    private void test(){
        calculateTripsPerMonth(
                (short) 9,
                (short) 120,
                new Date(2024 - 1900, Calendar.DECEMBER, 15),
                new Date(2024 - 1900, Calendar.DECEMBER, 16))
                .forEach((k, v) -> System.out.println(k + " " + v));

        calculateTripsPerMonth(
                (short) 1,
                (short) 1,
                new Date(2024 - 1900, Calendar.JULY, 26),
                new Date(2024 - 1900, Calendar.OCTOBER, 25))
                .forEach((k, v) -> System.out.println(k + " " + v));
    }
}
