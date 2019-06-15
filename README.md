# httpclient

Cliente HTTP simple para ser usado como parte de una aplicación Java.

### Clases principales
- `HTTPClient` encargada de enviar los requests y devolver el resultado de los mismos. Esta clase es sólo el canal a través del cual se hacen las llamadas, por lo tanto no es necesario instanciar un nuevo `HTTPClient` para cada request.
- `Request` es la clase que modela un request HTTP. Tiene metodos convenientes para setear URL, http method, parametros (query), headers, payload, etc.
- `Response` contiene información de la respuesta de un Request. Contiente el código de status y la data devuelta de la llamada.

### Ejemplo basico de uso
```
HTTPClient client = new HTTPClient();
String url = "https://services.odata.org/V3/Northwind/Northwind.svc/$metadata";
Response response = new Request(url)
                .withMethod(Request.Method.GET)
                .execute(client);
```

Más ejemplos de uso en los tests unitarios.

### Tests unitarios
Hay dos clases de unit tests:
- `ClientTest`: esta clase tiene un test para un request que espera XML y otro para JSON.
- `FakeRESTAPITest`: en esta clase se realizan tests invocando servicios del proyecto [FakeRestAPI](https://github.com/alejandroleoz/FakeRestAPI). 
Para el correcto funcionamiento de estos tests se requiere que dicho endpoint esté corriendo localmente.

### Cómo incluirlo en el proyecto?
- Ejecutar `mvn clean package`. 
- El `jar` generado se puede encontrar en el directorio `target`. 
- Incluir el jar en el proyecto donde se quiera usar (verificar las dependencias y classpath)
