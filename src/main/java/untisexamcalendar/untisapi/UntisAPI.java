package untisexamcalendar.untisapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.List;

public class UntisAPI {

    private String jSessionId;

    public void login(String user, String key) {

        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            URL url = new URL(new URI("https", "antiope.webuntis.com", "/WebUntis/jsonrpc_intern.do", "school=" + "lam", "").toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            connection.connect();

            OutputStream out = connection.getOutputStream();

            JSONArray params = new JSONArray();
            JSONObject auth = new JSONObject();
            try {
                auth.put("auth", UntisAuthentication.getAuthObject(user, key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.put(auth);

            out.write(new JSONObject()
                    .put("id", 0)
                    .put("method", "getUserData2017")
                    .put("params", params)
                    .put("jsonrpc", "2.0")
                    .toString().getBytes());
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {

                throw new Exception(new JSONObject()
                        .put("id", 0)
                        .put("error", new JSONObject()
                                .put("message", "Unexpected status code: " + statusCode)
                        ).toString());

            }

            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
            for (HttpCookie cookie : cookies) {
                System.out.println(cookie.getDomain());
                System.out.println(cookie);

                if (cookie.getName().equals("JSESSIONID")) {
                    jSessionId = cookie.getValue();
                }
            }

            InputStream inputStream = connection.getInputStream();


            if (inputStream != null) {
                //System.out.println(readStream(inputStream));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {

                throw new Exception(new JSONObject()
                        .put("id", 0)
                        .put("error", new JSONObject()
                                .put("message", e.getMessage())
                        ).toString());

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public JSONObject requestExams(String startDate, String endDate, boolean withGrades, int klasseId) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://antiope.webuntis.com/WebUntis/api/exams?startDate=" + startDate + "&endDate=" + endDate + "&withGrades=" + withGrades + "&klasseId=" + klasseId)
                //.url("https://antiope.webuntis.com/WebUntis/api/exams?startDate=20200915&endDate=20210715&withGrades=true&klasseId=-1")
                .method("GET", null)
                .addHeader("Cookie", "JSESSIONID=" + jSessionId)
                .build();
        try {
            Response response = client.newCall(request).execute();

            JSONObject jsonResponse = new JSONObject(response.body().string());
            if (jsonResponse.has("data")) {
                return jsonResponse;
            } else {
                throw new Exception(jsonResponse.toString());
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject().put("error", e.getMessage());
        }

    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


}
