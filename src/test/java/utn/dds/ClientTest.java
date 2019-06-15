package utn.dds;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientTest {

    private HTTPClient client;

    @Before
    public void before() {
        client = new HTTPClient();
    }

    @Test
    public void getXML() throws IOException {
        String url = "https://services.odata.org/V3/Northwind/Northwind.svc/$metadata";

        Response response = new Request(url)
                .withMethod(Request.Method.GET)
                .execute(client);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getContent()).isNotEmpty();
        System.out.println(response.getContent());
    }

    @Test
    public void getJSON() throws IOException {
        String url = "https://services.odata.org/V3/Northwind/Northwind.svc/Customers";

        Map<String, Serializable> params = new ConcurrentHashMap<>();
        params.put("$format", "json");

        Response response = new Request(url)
                .withMethod(Request.Method.GET)
                .withContentType("application/json")
                .withParameters(params)
                .execute(client);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getContent()).isNotEmpty();
        System.out.println(response.getContent());
    }

}
