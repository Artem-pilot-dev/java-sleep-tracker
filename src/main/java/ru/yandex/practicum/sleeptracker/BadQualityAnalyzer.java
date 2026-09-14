package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class BadQualityAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badSessions = sessions.stream().filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult("Сессии с плохим качеством сна", badSessions);
    }
}
