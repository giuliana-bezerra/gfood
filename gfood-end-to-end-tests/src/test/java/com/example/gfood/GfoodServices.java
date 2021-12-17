package com.example.gfood;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class GfoodServices {
  private String baseUrl(int port, String path, String... pathParams) {
    assertNotNull("host", getHost());
    StringBuilder sb = new StringBuilder("http://");
    sb.append(getHost());
    sb.append(":");
    sb.append(port);
    sb.append("/");
    sb.append(path);

    for (String param : pathParams) {
      sb.append("/");
      sb.append(param);
    }

    return sb.toString();
  }

  public abstract String getHost();

  public abstract int getApplicationPort();

  protected String consumerBaseUrl(String... pathParam) {
    return baseUrl(getApplicationPort(), "consumers", pathParam);
  }

  protected String restaurantBaseUrl(String... pathParam) {
    return baseUrl(getApplicationPort(), "restaurants", pathParam);
  }

  protected String orderBaseUrl(String... pathParam) {
    return baseUrl(getApplicationPort(), "orders", pathParam);
  }

  protected String courierBaseUrl(String... pathParam) {
    return baseUrl(getApplicationPort(), "couriers", pathParam);
  }
}
