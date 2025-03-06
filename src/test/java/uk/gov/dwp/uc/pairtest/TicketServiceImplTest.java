package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketServiceImplTest {
    @Test
    public void testChildTicketWithoutAdultNotAllowed() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 3));
        });

    }

    @Test
    public void testInfantTicketWithoutAdultNotAllowed() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.TicketType.INFANT, 20));
        });
    }

    @Test
    public void testChildAndInfantWithoutAdultNotAllowed() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);

        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.INFANT, 2);
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 2);

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, infantTicket, childTicket);
        });
    }

    @Test
    public void testExceedMaxTicketLimit() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);

        TicketTypeRequest ticketRequestInfant = new TicketTypeRequest(TicketTypeRequest.TicketType.INFANT, 3);
        TicketTypeRequest ticketRequestAdult = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 20);
        TicketTypeRequest ticketRequestChild = new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 3);

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, ticketRequestInfant, ticketRequestAdult, ticketRequestChild);
        });
    }

    @Test
    public void testAccountIdIsNull() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(null, null);
        });
    }
    @Test
    public void testInvalidUserId() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(-1L, new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 20));
        });
    }

    @Test
    public void testNegativeTicketsNotAllowed() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(10L, new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, -62));
        });
    }

    @Test
    public void testZeroTicketNotAllowed() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(-1L, new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 0));
        });
    }

    @Test
    public void testTicketTypeRequestIsNull() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(-1L, null);
        });
    }



}
