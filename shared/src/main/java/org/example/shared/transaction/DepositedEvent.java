package org.example.shared.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DepositedEvent {
    private final String id;
    private final double amount;
}
