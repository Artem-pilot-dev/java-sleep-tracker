package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream().filter(s -> !s.getStartSession()
                .toLocalDate().equals(s.getFinishSession().toLocalDate())).map(this::getChronotypeForSession)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long maxCount = counts.values().stream().max(Long::compare).orElse(0L);
        Chronotype userChronotype = Chronotype.GOLUB;

        if (maxCount > 0) {
            long winnersCount = counts.values().stream().filter(count -> count == maxCount).count();
            if (winnersCount == 1) {
                userChronotype = counts.entrySet().stream()
                        .filter(entry -> entry.getValue() == maxCount).map(Map.Entry::getKey)
                        .findFirst().orElse(Chronotype.GOLUB);
            }
        }
        return new SleepAnalysisResult("Хронотип пользователя", userChronotype.name());
    }

    private Chronotype getChronotypeForSession(SleepingSession session) {

        LocalTime bedtime = session.getStartSession().toLocalTime();
        LocalTime wakeup = session.getFinishSession().toLocalTime();
        if (bedtime.isAfter(LocalTime.of(23, 0)) && wakeup.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.SOVA;
        } else if (bedtime.isBefore(LocalTime.of(22, 0))
                && wakeup.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.ZHAVORONOK;
        } else {
            return Chronotype.GOLUB;
        }
    }
}
