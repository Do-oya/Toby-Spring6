package tobyspring.tobyspring6;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentService {
    public Payment prepare(Long orderId, String currency, BigDecimal foreignCurrencyAmount) {

        return Payment.builder()
                .orderId(orderId)
                .currency(currency)
                .foreignCurrencyAmount(foreignCurrencyAmount)
                .exRate(BigDecimal.ZERO)
                .convertedAmount(BigDecimal.ZERO)
                .validUntil(LocalDateTime.now())
                .build();
    }

    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}
