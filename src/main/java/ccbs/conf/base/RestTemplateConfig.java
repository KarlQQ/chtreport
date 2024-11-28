package ccbs.conf.base;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
  public RestTemplate createRestTemplate()
      throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
    SSLContext sslContext =
        SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();

    CloseableHttpClient httpClient = HttpClients.custom()
                                         .setSSLContext(sslContext)
                                         .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                         .build();

    HttpComponentsClientHttpRequestFactory factory =
        new HttpComponentsClientHttpRequestFactory(httpClient);
    return new RestTemplate(factory);
  }
}