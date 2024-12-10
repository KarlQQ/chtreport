package ccbs.service.impl;

import ccbs.data.entity.QueryAllNameAddrOfBillHistoryInput;
import ccbs.data.entity.QueryAllNameAddrOfBillHistoryOutput;
import ccbs.service.intf.WebServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Service
@Slf4j
public class WebServiceClientImpl implements WebServiceClient {

    @Value("${ccbs.webServiceEndpoint}") private String webServiceEndpoint;
    private final RestTemplate restTemplate;

    public WebServiceClientImpl() {
        this.restTemplate = new RestTemplate();
    }

    public QueryAllNameAddrOfBillHistoryOutput queryAllNameAddrOfBillHistory(QueryAllNameAddrOfBillHistoryInput input) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/xml;charset=UTF-8"));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

        HttpEntity<QueryAllNameAddrOfBillHistoryInput> requestEntity = new HttpEntity<>(input, headers);

        try {
            log.info("Sending request to: {}", webServiceEndpoint);
            ResponseEntity<QueryAllNameAddrOfBillHistoryOutput> response = restTemplate.exchange(
                webServiceEndpoint,
                HttpMethod.POST,
                requestEntity,
                QueryAllNameAddrOfBillHistoryOutput.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("HTTP error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error in queryAllNameAddrOfBillHistory", e);
        }
        return null;
    }
}
