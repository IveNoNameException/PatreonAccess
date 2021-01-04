package it.fireentity.patreon.access.entities;

import it.fireentity.library.interfaces.Cacheable;
import it.fireentity.patreon.access.PatreonAccess;
import it.fireentity.patreon.access.enumerations.Config;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWeather;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PatreonPlayer implements Cacheable<String> {
    private final String playerName;
    @Getter private final PatreonVip patreonVip;
    @Getter @Setter private long currentPlayedTime;
    @Getter @Setter private long joinedTime;

    public PatreonPlayer(PatreonVip patreonVip, String playerName, long currentPlayedTime) {
        this.currentPlayedTime = currentPlayedTime;
        this.patreonVip = patreonVip;
        this.playerName = playerName;
        this.joinedTime = System.currentTimeMillis();
    }

    public long onlinePlayedTime() {
        return System.currentTimeMillis() - joinedTime;
    }

    public boolean isExceeded() {
        return patreonVip.getMaxPlayTime() < currentPlayedTime;
    }

    public Optional<String> getOnlineTime() {
        long millis = getPatreonVip().getMaxPlayTime() - ((System.currentTimeMillis() - getJoinedTime()) + getCurrentPlayedTime());
        if(millis < 0) {
            return Optional.empty();
        }
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return Optional.of(days + " d " + hours + " h " + minutes + " m " + seconds + " s");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatreonPlayer that = (PatreonPlayer) o;

        return playerName.equals(that.playerName);
    }

    @Override
    public String getKey() {
        return playerName;
    }
}
