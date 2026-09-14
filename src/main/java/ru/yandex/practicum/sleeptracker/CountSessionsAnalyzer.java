package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class CountSessionsAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult("Количество сессий сна", sessions.size());
    }


}
