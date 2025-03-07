package thirdparty.paymentgateway;

public class TicketPaymentServiceImpl implements TicketPaymentService{
    @Override
    public void makePayment(long accountId, int totalAmountToPay) {
        System.out.println("Payment of £" + totalAmountToPay + " for account ID " + accountId + " was successful.");
    }
}
