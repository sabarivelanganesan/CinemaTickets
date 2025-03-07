package thirdparty.seatbooking;

public class SeatReservationServiceImpl implements SeatReservationService {
    @Override
    public void reserveSeat(long accountId, int totalSeatsToAllocate) {
        System.out.println("Successfully reserved " + totalSeatsToAllocate + " seat(s) for account ID " + accountId + ".");
    }

}
