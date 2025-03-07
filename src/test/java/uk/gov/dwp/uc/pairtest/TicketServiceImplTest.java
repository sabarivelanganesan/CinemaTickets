package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.*;

public class TicketServiceImplTest {

    @Test
    public void testValidPurchaseWithALL() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
        TicketTypeRequest ticketRequestInfant = new TicketTypeRequest(TicketTypeRequest.TicketType.INFANT, 3);
        TicketTypeRequest ticketRequestAdult = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 8);
        TicketTypeRequest ticketRequestChild = new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 3);

        assertDoesNotThrow(() -> {
            ticketService.purchaseTickets(1L, ticketRequestAdult, ticketRequestChild, ticketRequestInfant);
        });
    }
    @Test
    public void testValidPurchaseWithOnlyAdult() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);

        assertDoesNotThrow(() -> {
            ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 8));
        });

    }

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
            ticketService.purchaseTickets(null, new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 20));
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
            ticketService.purchaseTickets(10L, new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, -26));
        });
    }

    @Test
    public void testZeroTicketNotAllowed() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 0));
        });
    }

    @Test
    public void testTicketTypeRequestIsNull() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, (TicketTypeRequest[]) null); // Explicitly cast null to TicketTypeRequest[]
        });
    }
    @Test
    public void testTicketTypeRequestContainsNullElement() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, new TicketTypeRequest[] { null });
        });
    }

    @Test
    public void testTicketTypeRequestContainsEmptyElement() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, new TicketTypeRequest[] {});
        });
    }
    @Test
    public void testConstructorWithNullPaymentService() {
        SeatReservationService reservationService = new SeatReservationServiceImpl();
        assertThrows(IllegalArgumentException.class, () -> {
            new TicketServiceImpl(null, reservationService);
        });
    }
    @Test
    public void testConstructorWithNullReservationService() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        assertThrows(IllegalArgumentException.class, () -> {
            new TicketServiceImpl(paymentService, null);
        });
    }
    @Test
    public void testCalculateTotalTickets() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();
        TicketServiceImpl ticketService = new TicketServiceImpl(paymentService, reservationService);

        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 2);
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 3);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.INFANT, 1);

        int totalTickets = ticketService.calculateTotalTickets(adultTicket, childTicket, infantTicket);
        assertEquals(6, totalTickets); // (2)+(3)+(1) = 6
    }

    @Test
    public void testCalculateTotalAmount() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();
        TicketServiceImpl ticketService = new TicketServiceImpl(paymentService, reservationService);

        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 2);
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 3);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.INFANT, 1);

        int totalAmount = ticketService.calculateTotalAmount(adultTicket, childTicket, infantTicket);
        assertEquals(95, totalAmount); // (2 * 25) + (3 * 15) + 0 = 95
    }
    @Test
    public void testCalculateTotalSeats() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();
        TicketServiceImpl ticketService = new TicketServiceImpl(paymentService, reservationService);

        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 2);
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 9);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.INFANT, 1);

        int totalSeats = ticketService.calculateTotalSeats(adultTicket, childTicket, infantTicket);
        assertEquals(11, totalSeats); // 2 (adult) + 3 (child) = 5 (infant does not require a seat)
    }

    @Test
    public void testContainsAdultTicket() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();
        TicketServiceImpl ticketService = new TicketServiceImpl(paymentService, reservationService);

        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 2);
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 3);

        boolean hasAdultTicket = ticketService.containsAdultTicket(adultTicket, childTicket);
        assertEquals(true, hasAdultTicket); // Contains adult ticket
    }

    @Test
    public void testDoesNotContainAdultTicket() {
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();
        TicketServiceImpl ticketService = new TicketServiceImpl(paymentService, reservationService);

        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 3);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.TicketType.INFANT, 1);

        boolean hasAdultTicket = ticketService.containsAdultTicket(childTicket, infantTicket);
        assertEquals(false, hasAdultTicket); // Does not contain adult ticket
    }

}
