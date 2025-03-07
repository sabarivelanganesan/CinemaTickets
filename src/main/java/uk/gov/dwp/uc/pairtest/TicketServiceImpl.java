package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

public class TicketServiceImpl implements TicketService{
    private final TicketPaymentService paymentService;
    private static final int MAX_TICKETS = 25;
    private final SeatReservationService reservationService;

    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService reservationService) {
        if (paymentService == null) {
            throw new IllegalArgumentException("Payment service cannot be null.");
        }
        if (reservationService == null) {
            throw new IllegalArgumentException("Reservation service cannot be null.");
        }
        this.paymentService = paymentService;
        this.reservationService = reservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validateUserInput(accountId, ticketTypeRequests);

        int totalTickets = calculateTotalTickets(ticketTypeRequests);
        int totalAmount = calculateTotalAmount(ticketTypeRequests);
        int totalSeats = calculateTotalSeats(ticketTypeRequests);
        boolean hasAdultTicket = containsAdultTicket(ticketTypeRequests);

        validateTicketRules(totalTickets, hasAdultTicket);

        processPayment(accountId, totalAmount);
        reserveSeats(accountId, totalSeats);
    }

    /**
     * Validates user input (Account ID and Ticket Type Requests)
     */
    public void validateUserInput(Long accountId, TicketTypeRequest... ticketTypeRequests) {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid Account ID.");
        }
        if (ticketTypeRequests == null) {
            throw new InvalidPurchaseException("Ticket type requests cannot be null");
        }
        if (ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException("At least one ticket type must be specified.");
        }
        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request == null) {
                throw new InvalidPurchaseException("Ticket type requests cannot contain null elements.");
            }
        }
    }

    /**
     * Calculates the total number of tickets.
     */
    public int calculateTotalTickets(TicketTypeRequest... ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests).mapToInt(TicketTypeRequest::getNoOfTickets).sum();
    }

    /**
     * Calculates the total amount for the requested tickets.
     */
    public int calculateTotalAmount(TicketTypeRequest... ticketTypeRequests) {
        int totalAmount = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            totalAmount += request.getTicketType().getPrice() * request.getNoOfTickets();
        }
        return totalAmount;
    }

    /**
     * Calculates the total number of seats to be reserved.
     * Infants are not allocated seats.
     */
    public int calculateTotalSeats(TicketTypeRequest... ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests)
                .filter(request -> request.getTicketType() != TicketTypeRequest.TicketType.INFANT)
                .mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum();
    }

    /**
     * Checks if there is at least one adult ticket in the request.
     */
    public boolean containsAdultTicket(TicketTypeRequest... ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests)
                .anyMatch(request -> request.getTicketType() == TicketTypeRequest.TicketType.ADULT);
    }

    /**
     * Validates business rules related to ticket purchases.
     */
    public void validateTicketRules(int totalTickets, boolean hasAdultTicket) {
        if (totalTickets <= 0) {
            throw new InvalidPurchaseException("At least one ticket must be purchased.");
        }
        if (totalTickets > MAX_TICKETS) {
            throw new InvalidPurchaseException("Cannot purchase more than 25 tickets at a time.");
        }
        if (!hasAdultTicket) {
            throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without an Adult ticket.");
        }
    }

    /**
     * Handles payment processing.
     */
    public void processPayment(Long accountId, int totalAmount) {
        paymentService.makePayment(accountId, totalAmount);
    }

    /**
     * Handles seat reservation.
     */
    public void reserveSeats(Long accountId, int totalSeats) {
        reservationService.reserveSeat(accountId, totalSeats);
    }
}
