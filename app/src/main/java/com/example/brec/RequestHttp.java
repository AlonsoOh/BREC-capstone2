package com.example.brec;
import android.content.ContentValues;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class RequestHttp {
    public String request(String _url, ContentValues _param) {
        HttpURLConnection con = null;
        StringBuffer sparam = new StringBuffer();

        //보낼 데이터 렌더링 sparam = " a=13&b=124... "
        if (_param == null)
            sparam.append("");
        else {
            boolean isAnd = false;
            String key;
            String value;

            for (Map.Entry<String, Object> parameter : _param.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();
                if (isAnd)
                    sparam.append("&");
                sparam.append(key).append("=").append(value);
                if (!isAnd)
                    if (_param.size() >= 2)
                        isAnd = true;
            }


        }
        try {
            String p = URLEncoder.encode(sparam.toString(), "UTF-8");
            URL url = new URL(_url + "?" + p);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET"); // URL 요청에 대한 메소드 설정 : POST.
            con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");
            System.out.println("input : " + sparam.toString());
            //String strparam = sparam.toString();
            //OutputStreamWriter outStream = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            //PrintWriter writer = new PrintWriter(outStream);
            //writer.write(sparam.toString());
            //writer.flush();
            /*
            OutputStream os = con.getOutputStream();
            os.write(strparam.getBytes("UTF-8")); //출력 스트림에 출력(POST 이므로)
            os.flush();
            os.close();
             */
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            String line;
            String page = "";
            while ((line = reader.readLine()) != null) {
                page += line;
            }
            System.out.println("output : " + page);
            System.out.println("응답코드 : " + con.getResponseCode());
            return page;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();
        }
        return null;
    }
}

