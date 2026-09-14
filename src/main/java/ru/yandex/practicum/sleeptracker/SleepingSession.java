package ru.yandex.practicum.sleeptracker;

import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.LocalDateTime;

public class SleepingSession {
    private final LocalDateTime startSession;
    private final LocalDateTime finishSession;
    private final SleepQuality quality;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public SleepingSession(LocalDateTime startSession, LocalDateTime finishSession, SleepQuality quality) {
        this.startSession = startSession;
        this.finishSession = finishSession;
        this.quality = quality;
    }

    public LocalDateTime getStartSession() {

        return startSession;
    }

    public LocalDateTime getFinishSession() {
        return finishSession;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationMinutes() {
        return Duration.between(startSession, finishSession).toMinutes();
    }

    public static SleepingSession fromString(String line) {

        String[] partOfLine = line.split(";");
        LocalDateTime start = LocalDateTime.parse(partOfLine[0], FORMATTER);
        LocalDateTime finish = LocalDateTime.parse(partOfLine[1], FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(partOfLine[2].trim());
        return new SleepingSession(start, finish, quality);

    }

}
