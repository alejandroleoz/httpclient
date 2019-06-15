package utn.dds;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FakeRESTAPITest {

    private HTTPClient client;
    private Gson gson = new Gson();
    private String baseURL = "http://localhost:8080/";
    private String endpoint = baseURL + "Persona/";

    @Before
    public void before() {
        client = new HTTPClient();
    }

    @Test
    public void isAliveTest() throws IOException {
        Response response = new Request(baseURL)
                .withMethod(Request.Method.GET)
                .execute(client);

        // check code
        assertThat(response.getStatusCode())
                .as("Chequeando endpoint FakeRESTApi este activo")
                .isNotZero();
    }

    @Test
    public void fullTest() throws IOException {
        Response response;
        PersonaDTO[] personas;

        Long dni = 123456L;

        // GET ALL - Precondicion: el servicio no debe tener personas persistidas
        response = new Request(endpoint)
                .withMethod(Request.Method.GET)
                .execute(client);
        assertThat(response.getStatusCode())
                .overridingErrorMessage("Pre-condicion: El servicio debe devolver 0 Personas para poder correr este test")
                .isEqualTo(200);
        personas = gson.fromJson(response.getContent(), PersonaDTO[].class);
        assertThat(personas).hasSize(0);

        // POST
        String payload = String.format("{ \"dni\": %d, \"nombre\": \"%s\", \"apellido\": \"%s\", \"edad\": %d }",
                dni,
                "Alejandro",
                "Perez",
                30);
        response = new Request(endpoint)
                .withMethod(Request.Method.POST)
                .withContentType("application/json")
                .withPayload(payload)
                .execute(client);
        assertThat(response.getStatusCode()).isEqualTo(200);
        System.out.println(String.format("POST Status %d", response.getStatusCode()));
        System.out.println(String.format(response.getContent()));

        // GET ALL
        response = new Request(endpoint)
                .withMethod(Request.Method.GET)
                .execute(client);
        assertThat(response.getStatusCode()).isEqualTo(200);
        personas = gson.fromJson(response.getContent(), PersonaDTO[].class);
        assertThat(personas).hasSize(1);
        System.out.println(String.format("GET ALL Status %d", response.getStatusCode()));
        System.out.println(String.format(response.getContent()));

        // PUT
        payload = String.format("{ \"dni\": %d, \"nombre\": \"%s\", \"apellido\": \"%s\", \"edad\": %d }",
                dni,
                "Ezequiel",
                "Sarlanga",
                33);
        response = new Request(endpoint)
                .withMethod(Request.Method.PUT)
                .withContentType("application/json")
                .withPayload(payload)
                .execute(client);
        assertThat(response.getStatusCode()).isEqualTo(200);
        System.out.println(String.format("PUT Status %d", response.getStatusCode()));
        System.out.println(String.format(response.getContent()));

        // GET SINGLE
        String url = String.format("%s%d", endpoint, dni);
        response = new Request(url)
                .withMethod(Request.Method.GET)
                .execute(client);
        assertThat(response.getStatusCode()).isEqualTo(200);
        PersonaDTO personaDTO = gson.fromJson(response.getContent(), PersonaDTO.class);
        assertThat(personaDTO.getDni()).isEqualTo(dni);
        assertThat(personaDTO.getNombre()).isEqualTo("Ezequiel");
        assertThat(personaDTO.getApellido()).isEqualTo("Sarlanga");
        assertThat(personaDTO.getEdad()).isEqualTo(33);
        System.out.println(String.format("GET SINGLE Status %d", response.getStatusCode()));
        System.out.println(String.format(response.getContent()));

        // DELETE
        url = String.format("%s%d", endpoint, dni);
        response = new Request(url)
                .withMethod(Request.Method.DELETE)
                .execute(client);
        assertThat(response.getStatusCode()).isEqualTo(200);
        System.out.println(String.format("DELETE Status %d", response.getStatusCode()));
        System.out.println(String.format(response.getContent()));

        // GET ALL
        response = new Request(endpoint)
                .withMethod(Request.Method.GET)
                .execute(client);
        assertThat(response.getStatusCode()).isEqualTo(200);
        personas = gson.fromJson(response.getContent(), PersonaDTO[].class);
        assertThat(personas).isEmpty();
        System.out.println(String.format("GET ALL Status %d", response.getStatusCode()));
        System.out.println(String.format(response.getContent()));

    }
}
