package it.fireentity.patreon.access.entities.player;

import it.fireentity.patreon.access.entities.vip.PatreonVip;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class PatreonPlayer {
    private final PatreonVip patreonVip;
    private final long time;
    private final long currentPlayedTime;
    private final long joinedTime;

    public boolean isExceeded() {
        return patreonVip.getMaxPlayTime() > currentPlayedTime;
    }

}
