package tobyspring.tobyspring6;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class PaymentService {
    public Payment prepare(Long orderId, String currency, BigDecimal foreignCurrencyAmount) throws IOException {
        /*
         환율 API 호출
         지정된 currency 값을 사용하여 환율 정보를 얻기 위해 외부 API로 GET 요청을 보냄
         HttpURLConnection 사용하여 API로부터 데이터를 받아오고, BufferedReader를 사용해 응답을 문자열로 반환
        */
        URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = br.lines().collect(Collectors.joining());
        br.close();

        /*
         Jackson을 이용한 JSON 파싱
         ObjectMapper는 Jackson 라이브러리에서 제공하는 JSON 파서, mapper.readValue() 메서드를 사용해 JSON 문자열을
         ExRateData라는 레코드로 변환, 이 레코드는 rates라는 필드에 Map<String, BicDecimal> 형식으로 여러 환율 정보를 담고 있으며, 이중 "KRW" 환율을 가져오는 코드
        */
        ObjectMapper mapper = new ObjectMapper();
        ExRateData data = mapper.readValue(response, ExRateData.class);
        BigDecimal exRate = data.rates().get("KRW");
        System.out.println(exRate);

        // 금액 계산
        BigDecimal convertedAmount = foreignCurrencyAmount.multiply(exRate);
        // 유효 시간 계산
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return Payment.builder()
                .orderId(orderId)
                .currency(currency)
                .foreignCurrencyAmount(foreignCurrencyAmount)
                .exRate(exRate)
                .convertedAmount(convertedAmount)
                .validUntil(validUntil)
                .build();
    }

    public static void main(String[] args) throws IOException {
        PaymentService paymentService = new PaymentService();
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}
