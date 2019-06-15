package utn.dds;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HTTPClient {

    private int readTimeout = 5000;
    private int connectTimeout = 5000;

    public Response doRequest(Request request) throws IOException {
        Map<String, String> headers = request.getHeaders();
        String payload = request.getPayload();

        // build the URL (url + query string)
        URL urlObject = buildURL(request);

        //  get connection object
        HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();

        // set  method
        con.setRequestMethod(request.getMethod().name());

        // set timeouts
        con.setReadTimeout(readTimeout);
        con.setConnectTimeout(connectTimeout);

        // set default content type
        con.setRequestProperty("Content-Type", request.getContentType());

        // set default accept type
        con.setRequestProperty("Accept", request.getAccept());

        // write headers if any
        if (Objects.nonNull(headers) && !headers.isEmpty()) {
            headers.forEach((headerName, value) -> {
                con.setRequestProperty(headerName, value);
            });
        }

        // write  payload (if any)
        if (request.requiresPayload()) {
            con.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(payload);
            out.close();
        }

        // execute request and return request output
        return execute(con);
    }

    // builds the final URL based on the given URL and the  query parameters
    private URL buildURL(Request request) throws MalformedURLException {
        String url = request.getUrl();
        Map<String, Serializable> urlParameters = request.getParameters();

        // setup query String for URL urlParameters
        String queryStr = "?";
        if (Objects.nonNull(urlParameters) && !urlParameters.isEmpty()) {
            List<NameValuePair> params = new ArrayList<>();
            urlParameters.forEach((paramName, paramValue) -> {
                params.add(new BasicNameValuePair(paramName, Objects.nonNull(paramValue) ? paramValue.toString() : ""));
            });
            queryStr = "?" + URLEncodedUtils.format(params, "UTF-8");
        }
        return new URL(url + queryStr);
    }

    // execute request, get response code and get response
    private Response execute(HttpURLConnection con) throws IOException {
        int status = con.getResponseCode();
        Reader streamReader = null;
        if (status > 299) {
            streamReader = new InputStreamReader(con.getErrorStream());
        } else {
            streamReader = new InputStreamReader(con.getInputStream());
        }

        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return new Response(status, content.toString());
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
