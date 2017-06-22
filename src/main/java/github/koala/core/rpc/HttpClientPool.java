package github.koala.core.rpc;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author edliao on 2017/6/21.
 * @description Http传输数据的具体实现
 */
@Slf4j
class HttpClientPool {

  private OkHttpClient httpClient;

  HttpClientPool() {
    httpClient = new OkHttpClient();
  }

  Request initRequest(String url) {
    return new Request.Builder()
        .url(url)
        .build();
  }

  Request initRequest(String url, MediaType mediaType, String json) {
    return new Request.Builder()
        .url(url)
        .post(RequestBody.create(mediaType, json))
        .build();
  }

  Response execute(Request request) throws IOException {
    return httpClient.newCall(request).execute();
  }
}
