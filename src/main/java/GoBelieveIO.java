import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class GoBelieveIO {
    private static final int TIMEOUT_IN_MILLIONS = 5000;

    private static final String API = "https://api.gobelieve.io/v2";

    private static final long appID = 999999;
    private static final String appSecret = "999999999";

    private static String base64Encode(byte[] c) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(c);
    }

    public static String getBasicAuth(long appID, String appSecret) {
        String t = "" + appID + ":" + appSecret;
        final String encodedText = base64Encode(t.getBytes());
        return encodedText;
    }

    public static String doAuthGrant(long uid, String userName) {
        String url = String.format("%s/auth/grant", API);
        OutputStream out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/json");
            conn.setRequestProperty("charset", "utf-8");

            String basicAuth = getBasicAuth(appID, appSecret);
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);

            JSONObject json = new JSONObject();
            json.put("uid", uid);
            json.put("user_name", userName);
            String param = json.toString();

            out = conn.getOutputStream();
            out.write(param.getBytes("utf8"));
            out.flush();

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
            System.out.println("result:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        doAuthGrant(1, "11");
    }
}