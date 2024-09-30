package tobyspring.tobyspring6;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.IllformedLocaleException;

public interface ExRateProvider {
    BigDecimal getExRate(String currency) throws IOException;
}
