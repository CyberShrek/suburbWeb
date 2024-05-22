package org.vniizht.suburbsweb.service.transformation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class TransformerTest {

    @Test
    void yyyymm2yymm() {
        assertEquals(1001, Transformer.yyyymm2yymm(201001));
        assertEquals(2211, Transformer.yyyymm2yymm(202211));
        assertEquals( 301, Transformer.yyyymm2yymm(200301));
    }

    @Test
    void date2yyyy() {
        assertEquals("2010", Transformer.date2yyyy(new Date(2010, 0, 1)));
        assertEquals("2020", Transformer.date2yyyy(new Date(2020, 4, 20)));
        assertEquals("2003", Transformer.date2yyyy(new Date(2003, 10, 1)));
    }

    @Test
    void date2yy() {
        assert Transformer.date2yy(new Date(2010, 0, 1)).equals("10");
        assert Transformer.date2yy(new Date(2020, 4, 20)).equals("20");
        assert Transformer.date2yy(new Date(2030, 0, 1)).equals("30");
    }

    @Test
    void date2yymm() {
        assertEquals("1001", Transformer.date2yymm(new Date(2010, 0, 1)));
        assertEquals("2205", Transformer.date2yymm(new Date(2022, 4, 20)));
        assertEquals("0311", Transformer.date2yymm(new Date(2003, 10, 1)));
    }

    @Test
    void date2mm() {
        assertEquals("01", Transformer.date2mm(new Date(2010, 0, 1)));
        assertEquals("05", Transformer.date2mm(new Date(2022, 4, 20)));
        assertEquals("11", Transformer.date2mm(new Date(2003, 10, 1)));
    }

    @Test
    void interpretDepartment() {
        assert Transformer.interpretDepartment("01").equals("1");
        assert Transformer.interpretDepartment("02").equals("2");
        assert Transformer.interpretDepartment("03").equals("3");
    }

    @Test
    void interpretOkato() {
        assert Transformer.interpretOkato("12345678").equals("12345");
    }

    @Test
    void interpretTrainCategory() {
        assert Transformer.interpretTrainCategory('С').equals('6');
        assert Transformer.interpretTrainCategory('7').equals('5');
        assert Transformer.interpretTrainCategory('А').equals('8');
        assert Transformer.interpretTrainCategory('Б').equals('7');
        assert Transformer.interpretTrainCategory('Г').equals('9');
        assert Transformer.interpretTrainCategory('1').equals('4');
        assert Transformer.interpretTrainCategory('М').equals('4');
        assert Transformer.interpretTrainCategory('2').equals('1');
    }

    @Test
    void interpretCarriageClass() {
        assert Transformer.interpretCarriageClass('A').equals("0A");
    }

    @Test
    void interpretTicketType() {
        assert Transformer.interpretTicketType('0', '1', '1', '0').equals('8');
        assert Transformer.interpretTicketType('0', '1', '0', '0').equals('6');
        assert Transformer.interpretTicketType('0', '0', '0', '0').equals('2');
        assert Transformer.interpretTicketType('0', '0', '0', '1').equals('3');
        assert Transformer.interpretTicketType('1', '0', '0', '0').equals('5');
        assert Transformer.interpretTicketType('2', '0', '0', '0').equals('5');
        assert Transformer.interpretTicketType('3', '0', '0', '0').equals('5');
        assert Transformer.interpretTicketType('4', '0', '0', '0').equals('5');
        assert Transformer.interpretTicketType('5', '0', '0', '0').equals('4');
        assert Transformer.interpretTicketType('6', '0', '0', '0').equals('4');
        assert Transformer.interpretTicketType('7', '0', '0', '0').equals('5');
        assert Transformer.interpretTicketType('8', '0', '0', '0').equals('5');
    }

    @Test
    void interpretPassengerCategory() {
        assert Transformer.interpretPassengerCategory('1', '0', "0").equals('4');
        assert Transformer.interpretPassengerCategory('0', '1', "0").equals('2');
        assert Transformer.interpretPassengerCategory('0', '0', "0").equals('1');
        assert Transformer.interpretPassengerCategory('0', '0', "0123any").equals('3');
    }

    @Test
    void interpretPaymentType() {
        assert Transformer.interpretPaymentType(' ', "09", "6 ").equals('4');
        assert Transformer.interpretPaymentType('8', "00", "  ").equals('3');
        assert Transformer.interpretPaymentType('9', "00", "  ").equals('1');
        assert Transformer.interpretPaymentType('1', "00", "  ").equals('2');
        assert Transformer.interpretPaymentType('3', "00", "  ").equals('2');
        assert Transformer.interpretPaymentType('6', "00", "  ").equals('5');
    }

    @Test
    void interpretDocRegistration() {
    }

    @Test
    void interpretAbonementType() {
        assert Transformer.interpretAbonementType('0').equals('0');
        assert Transformer.interpretAbonementType('1').equals('5');
        assert Transformer.interpretAbonementType('2').equals('4');
        assert Transformer.interpretAbonementType('3').equals('1');
        assert Transformer.interpretAbonementType('4').equals('1');
        assert Transformer.interpretAbonementType('5').equals('2');
        assert Transformer.interpretAbonementType('6').equals('2');
        assert Transformer.interpretAbonementType('7').equals('3');
        assert Transformer.interpretAbonementType('8').equals('3');
        assert Transformer.interpretAbonementType('9').equals('9');
    }

    @Test
    void interpretCarrionType() {
        assert Transformer.interpretCarrionType('Ж').equals('1');
        assert Transformer.interpretCarrionType('Т').equals('2');
        assert Transformer.interpretCarrionType('В').equals('3');
        assert Transformer.interpretCarrionType('Р').equals('4');
        assert Transformer.interpretCarrionType(' ').equals(' ');
    }

    @Test
    void interpretPassengersCount() {
        assert Transformer.interpretPassengersCount('0', (short) 111, (short) 222) == 222;
        assert Transformer.interpretPassengersCount('1', (short) 111, (short) 222) == 111;
        assert Transformer.interpretPassengersCount('2', (short) 111, (short) 222) == 111;
        assert Transformer.interpretPassengersCount('3', (short) 111, (short) 222) == 111;
        assert Transformer.interpretPassengersCount('4', (short) 111, (short) 222) == 111;
        assert Transformer.interpretPassengersCount('5', (short) 111, (short) 222) == 111;
        assert Transformer.interpretPassengersCount('6', (short) 111, (short) 222) == 222;
    }

    private void assertEquals(Object expected, Object actual) {
        Assertions.assertEquals(expected, actual);
    }
}