package tobyspring.tobyspring6;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.IllformedLocaleException;

public class SimpleExRateProvider {
    BigDecimal getExRate(String currency) throws IOException {
        if (currency.equals("USD")) return BigDecimal.valueOf(1000);

        throw new IllformedLocaleException("지원되지 않는 통화입니다.");
    }
}
