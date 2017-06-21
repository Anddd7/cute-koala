package github.koala.core.rpc;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author edliao on 2017/6/21.
 * @description TODO
 */
@Slf4j
public class HttpClient {

  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  OkHttpClient httpClient;

  public HttpClient() {
    httpClient = new OkHttpClient();
  }

  public String get(String url) {
    Request request = new Request.Builder()
        .url(url)
        .build();

    log.info("发送[{}]", request.toString());

    String response = null;
    try {
      response = httpClient.newCall(request).execute().body().string();
      log.info("收到[{}]", response);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return response;
  }

  public String post(String url, String json) {
    RequestBody body = RequestBody.create(JSON, json);
    Request request = new Request.Builder()
        .url(url)
        .post(body)
        .build();

    log.info("发送[{}] ,Body[{}]", request.toString(), json);

    String response = null;
    try {
      response = httpClient.newCall(request).execute().body().string();
      log.info("收到[{}]", response);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return response;
  }

}
