package com.project01.skillineserver.projection;

import java.math.BigDecimal;
import java.time.Instant;

public interface OrderProjection {
    Long getId();
    String getStatus();
    Instant getCreatedAt();
    Long getQuantity();
    BigDecimal getTotalPrice();
    String getUsername();
    String getAddress();
    String getFullname();
    String getEmail();
    String getPhone();
}
