package it.fireentity.patreon.access.entities;

import it.fireentity.library.interfaces.Cacheable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class PatreonVip implements Cacheable<String> {
    private final String patreonName;
    @Getter
    private final String patreonDisplayName;
    @Getter
    private final long maxPlayTime;

    @Override
    public String getKey() {
        return patreonName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatreonVip that = (PatreonVip) o;

        return patreonName.equals(that.patreonName);
    }

    @Override
    public int hashCode() {
        return patreonName.hashCode();
    }

    public String getOnlineTime() {
        long millis = maxPlayTime;
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return days + " d " + hours + " h " + minutes + " m " + seconds + " s";
    }
}
