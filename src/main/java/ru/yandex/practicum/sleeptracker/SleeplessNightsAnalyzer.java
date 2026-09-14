package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

import static java.time.LocalTime.NOON;

public class SleeplessNightsAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        SleepingSession firstSession = sessions.getFirst();
        LocalDateTime firstStart = firstSession.getStartSession();
        LocalDate firstDate = firstStart.toLocalDate();

        if (firstStart.toLocalTime().isAfter(NOON)) {
            firstDate = firstDate.plusDays(1);
        }
        SleepingSession lastSession = sessions.getLast();
        LocalDateTime lastFinish = lastSession.getFinishSession();

        LocalDate lastDate = lastFinish.toLocalDate();

        if (lastFinish.toLocalTime().isAfter(NOON)) {
            lastDate = lastDate.minusDays(1);
        }
        long totalNights = firstDate.datesUntil(lastDate.plusDays(1)).count();

        long sleepNights = sessions.stream().filter(session -> (session.getStartSession().toLocalDate().
                isBefore(session.getFinishSession().toLocalDate())) || (session.getStartSession().toLocalTime()
                .isBefore(LocalTime.of(6, 0)) &&
                session.getFinishSession().toLocalTime().isBefore(LocalTime.of(6, 0)))).count();
        long sleeplessNights = totalNights - sleepNights;

        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessNights);
    }
}
