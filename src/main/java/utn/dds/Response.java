package utn.dds;

public class Response {
    private String content;
    private int statusCode;

    public Response(int statusCode, String content) {
        this.content = content;
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
