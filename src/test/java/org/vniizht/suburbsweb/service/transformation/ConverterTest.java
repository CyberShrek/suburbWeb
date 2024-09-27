package org.vniizht.suburbsweb.service.transformation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;

class ConverterTest {

//    @Test
//    void yyyymm2yymm() {
//        assertEquals(1001, Converter.yyyymm2yymm(201001));
//        assertEquals(2211, Converter.yyyymm2yymm(202211));
//        assertEquals( 301, Converter.yyyymm2yymm(200301));
//    }
//
//    @Test
//    void date2yyyy() {
//        assertEquals("2010", Converter.date2yyyy(new Date(2010, 0, 1)));
//        assertEquals("2020", Converter.date2yyyy(new Date(2020, 4, 20)));
//        assertEquals("2003", Converter.date2yyyy(new Date(2003, 10, 1)));
//    }
//
//    @Test
//    void date2yy() {
//        assert Converter.date2yy(new Date(2010, 0, 1)).equals("10");
//        assert Converter.date2yy(new Date(2020, 4, 20)).equals("20");
//        assert Converter.date2yy(new Date(2030, 0, 1)).equals("30");
//    }
//
//    @Test
//    void date2yymm() {
//        assertEquals("1001", Converter.date2yymm(new Date(2010, 0, 1)));
//        assertEquals("2205", Converter.date2yymm(new Date(2022, 4, 20)));
//        assertEquals("0311", Converter.date2yymm(new Date(2003, 10, 1)));
//    }
//
//    @Test
//    void date2mm() {
//        assertEquals("01", Converter.date2mm(new Date(2010, 0, 1)));
//        assertEquals("05", Converter.date2mm(new Date(2022, 4, 20)));
//        assertEquals("11", Converter.date2mm(new Date(2003, 10, 1)));
//    }
//
//    @Test
//    void convertDepartment() {
//        assert Converter.convertDepartment("01").equals("1");
//        assert Converter.convertDepartment("02").equals("2");
//        assert Converter.convertDepartment("03").equals("3");
//    }
//
//    @Test
//    void convertOkato() {
//        assert Converter.convertOkato("12345678").equals("12345");
//    }
//
//    @Test
//    void convertTrainCategory() {
//        assert Converter.convertTrainCategory('С').equals('6');
//        assert Converter.convertTrainCategory('7').equals('5');
//        assert Converter.convertTrainCategory('А').equals('8');
//        assert Converter.convertTrainCategory('Б').equals('7');
//        assert Converter.convertTrainCategory('Г').equals('9');
//        assert Converter.convertTrainCategory('1').equals('4');
//        assert Converter.convertTrainCategory('М').equals('4');
//        assert Converter.convertTrainCategory('2').equals('1');
//    }
//
//    @Test
//    void convertCarriageClass() {
//        assert Converter.convertCarriageClass('A').equals("0A");
//    }
//
//    @Test
//    void convertTicketType() {
//        assert Converter.convertTicketType('0', '1', '1', '0').equals('8');
//        assert Converter.convertTicketType('0', '1', '0', '0').equals('6');
//        assert Converter.convertTicketType('0', '0', '0', '0').equals('2');
//        assert Converter.convertTicketType('0', '0', '0', '1').equals('3');
//        assert Converter.convertTicketType('1', '0', '0', '0').equals('5');
//        assert Converter.convertTicketType('2', '0', '0', '0').equals('5');
//        assert Converter.convertTicketType('3', '0', '0', '0').equals('5');
//        assert Converter.convertTicketType('4', '0', '0', '0').equals('5');
//        assert Converter.convertTicketType('5', '0', '0', '0').equals('4');
//        assert Converter.convertTicketType('6', '0', '0', '0').equals('4');
//        assert Converter.convertTicketType('7', '0', '0', '0').equals('5');
//        assert Converter.convertTicketType('8', '0', '0', '0').equals('5');
//    }
//
//    @Test
//    void convertPassengerCategory() {
//        assert Converter.convertPassengerCategory('1', '0', "0").equals('4');
//        assert Converter.convertPassengerCategory('0', '1', "0").equals('2');
//        assert Converter.convertPassengerCategory('0', '0', "0").equals('1');
//        assert Converter.convertPassengerCategory('0', '0', "0123any").equals('3');
//    }
//
//    @Test
//    void convertPaymentType() {
//        assert Converter.convertPaymentType(' ', "09", "6 ").equals('4');
//        assert Converter.convertPaymentType('8', "00", "  ").equals('3');
//        assert Converter.convertPaymentType('9', "00", "  ").equals('1');
//        assert Converter.convertPaymentType('1', "00", "  ").equals('2');
//        assert Converter.convertPaymentType('3', "00", "  ").equals('2');
//        assert Converter.convertPaymentType('6', "00", "  ").equals('5');
//    }
//
//    @Test
//    void convertDocRegistration() {
//    }

//    @Test
//    void convertAbonementType() {
//        assert Converter.convertAbonementType('0').equals('0');
//        assert Converter.convertAbonementType('1').equals('5');
//        assert Converter.convertAbonementType('2').equals('4');
//        assert Converter.convertAbonementType('3').equals('1');
//        assert Converter.convertAbonementType('4').equals('1');
//        assert Converter.convertAbonementType('5').equals('2');
//        assert Converter.convertAbonementType('6').equals('2');
//        assert Converter.convertAbonementType('7').equals('3');
//        assert Converter.convertAbonementType('8').equals('3');
//        assert Converter.convertAbonementType('9').equals('9');
//    }

//    @Test
//    void convertCarrionType() {
//        assert Converter.convertCarrionType('Ж').equals('1');
//        assert Converter.convertCarrionType('Т').equals('2');
//        assert Converter.convertCarrionType('В').equals('3');
//        assert Converter.convertCarrionType('Р').equals('4');
//        assert Converter.convertCarrionType(' ').equals(' ');
//    }
//
//    @Test
//    void convertPassengersCount() {
//        assert Converter.convertPassengersCount('0', (short) 111, (short) 222) == 222;
//        assert Converter.convertPassengersCount('1', (short) 111, (short) 222) == 111;
//        assert Converter.convertPassengersCount('2', (short) 111, (short) 222) == 111;
//        assert Converter.convertPassengersCount('3', (short) 111, (short) 222) == 111;
//        assert Converter.convertPassengersCount('4', (short) 111, (short) 222) == 111;
//        assert Converter.convertPassengersCount('5', (short) 111, (short) 222) == 111;
//        assert Converter.convertPassengersCount('6', (short) 111, (short) 222) == 222;
//    }

    private void assertEquals(Object expected, Object actual) {
        Assertions.assertEquals(expected, actual);
    }
}