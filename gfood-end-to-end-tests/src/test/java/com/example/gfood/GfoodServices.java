package com.example.gfood;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class GfoodServices {
  private String baseUrl(int port, String path, String pathParam) {
    assertNotNull("host", getHost());
    StringBuilder sb = new StringBuilder("http://");
    sb.append(getHost());
    sb.append(":");
    sb.append(port);
    sb.append("/");
    sb.append(path);
    sb.append("/");
    sb.append(pathParam);

    return sb.toString();
  }

  public abstract String getHost();

  public abstract int getApplicationPort();

  protected String consumerBaseUrl(String pathParam) {
    return baseUrl(getApplicationPort(), "consumers", pathParam);
  }
}
