package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class AverageDurationAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        double average = sessions.stream().mapToLong(SleepingSession::getDurationMinutes).average().orElse(0.0);
        return new SleepAnalysisResult("Средняя продолжительность сна", average);
    }
}
