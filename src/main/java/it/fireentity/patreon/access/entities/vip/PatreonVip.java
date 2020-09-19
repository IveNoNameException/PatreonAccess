package it.fireentity.patreon.access.entities.vip;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class PatreonVip {
    private final String patreonName;
    private final String patreonDisplayName;
    private final long maxPlayTime;
}
