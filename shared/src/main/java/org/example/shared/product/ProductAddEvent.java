package org.example.shared.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductAddEvent {
    private final String id;
    private final String productName;
}
