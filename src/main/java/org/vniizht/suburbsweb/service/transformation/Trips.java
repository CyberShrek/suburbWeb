package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.handbook.Trip;
import org.vniizht.suburbsweb.model.transformation.level2.PrigMain;
import org.vniizht.suburbsweb.model.transformation.level3.co22.T1;
import org.vniizht.suburbsweb.service.handbook.HandbookHolder;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class Trips {

    @Autowired
    private HandbookHolder handbookHolder;

    public Set<T1> multiplyT1(T1 t1, PrigMain prigMain) {

        short ticketCode = 1; // ?????
        short period     = prigMain.getSeatstick_limit();
        Date begDate     = prigMain.getTicket_begdate();
        Date endDate     = prigMain.getTicket_enddate();

        Set<T1> set = new LinkedHashSet<>();
        // TODO

        return set;
    }

    private Map<String, Integer> calculateTripsPerMonth(Short ticketCode,
                                                        Short period,
                                                        Date begDate,
                                                        Date endDate) {
        Map <String, Integer> result = new LinkedHashMap<>();
        Trip trip = handbookHolder.findTrip("20", ticketCode, period, begDate);


        return result;
    }

    @PostConstruct
    private void test(){
        calculateTripsPerMonth(
                (short) 9,
                (short) 120,
                new Date(2024, Calendar.JUNE, 27),
                new Date(2024, Calendar.OCTOBER, 24))
                .forEach((k, v) -> System.out.println(k + " " + v));
    }
}
