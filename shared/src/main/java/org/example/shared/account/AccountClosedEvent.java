package org.example.shared.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountClosedEvent {
    private final String id;
}
