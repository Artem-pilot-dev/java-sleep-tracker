package ru.yandex.practicum.sleeptracker;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {
    private List<SleepingSession> sessions;
    private List<SleepingSession> sessionsForSleeplessNightsTest;
    private static final List<SleepingSession> EMPTY_LIST = List.of();


    @BeforeEach
    void prepareSession() {
        SleepingSession sessionOne = SleepingSession.fromString("10.10.25 23:55;11.10.25 06:10;GOOD");
        SleepingSession sessionTwo = SleepingSession.fromString("05.10.25 13:30;05.10.25 14:15;NORMAL");
        SleepingSession sessionThree = SleepingSession.fromString("03.10.25 23:40;04.10.25 08:00;BAD");
        sessions = List.of(sessionOne, sessionTwo, sessionThree);
    }

    @BeforeEach
    void prepareSleeplessNightsTest() {
        SleepingSession sessionOne = SleepingSession.fromString("01.10.25 23:00;02.10.25 06:00;GOOD");
        SleepingSession sessionTwo = SleepingSession.fromString("02.10.25 13:00;02.10.25 14:00;NORMAL");
        SleepingSession sessionThree = SleepingSession.fromString("03.10.25 23:00;04.10.25 06:00;GOOD");
        sessionsForSleeplessNightsTest = List.of(sessionOne, sessionTwo, sessionThree);
    }

    @Test
    void checkCountSessionAnalyzer() {
        CountSessionsAnalyzer analyzer = new CountSessionsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(3, result.getValue());
    }

    @Test
    void checkCountSessionAnalyzerWithEmptyList() {
        CountSessionsAnalyzer analyzer = new CountSessionsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(EMPTY_LIST);
        assertEquals(0, result.getValue());
    }

    @Test
    void checkMinDurationAnalyzer() {
        MinDurationAnalyzer analyzer = new MinDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(45L, result.getValue());
    }

    @Test
    void checkMinDurationAnalyzerWithEmptyList() {
        MinDurationAnalyzer analyzer = new MinDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(EMPTY_LIST);
        assertEquals(0L, result.getValue());
    }

    @Test
    void checkMaxDurationAnalyzer() {
        MaxDurationAnalyzer analyzer = new MaxDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(500L, result.getValue());
    }

    @Test
    void checkMaxDurationAnalyzerWithEmptyList() {
        MaxDurationAnalyzer analyzer = new MaxDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(EMPTY_LIST);
        assertEquals(0L, result.getValue());
    }

    @Test
    void checkAverageDurationAnalyzer() {
        AverageDurationAnalyzer analyzer = new AverageDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(306.666, (Double) result.getValue(), 0.001);
    }

    @Test
    void checkAverageDurationAnalyzerWithEmptyList() {
        AverageDurationAnalyzer analyzer = new AverageDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(EMPTY_LIST);
        assertEquals(0, (Double) result.getValue(), 0.001);
    }

    @Test
    void checkBadQualityAnalyzer() {
        BadQualityAnalyzer analyzer = new BadQualityAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(1L, result.getValue());
    }

    @Test
    void checkBadQualityAnalyzerWithEmptyList() {
        BadQualityAnalyzer analyzer = new BadQualityAnalyzer();
        SleepAnalysisResult result = analyzer.apply(EMPTY_LIST);
        assertEquals(0L, result.getValue());
    }

    @Test
    void checkSleeplessNightsAnalyzer() {
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessionsForSleeplessNightsTest);
        assertEquals(1L, result.getValue());
    }

    @Test
    void checkSleeplessNightsAnalyzerWhenUserSlept() {
        List<SleepingSession> oneNight = List.of(SleepingSession.fromString("01.10.25 23:00;02.10.25 06:00;GOOD"));
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(oneNight);
        assertEquals(0L, result.getValue());
    }

    @Test
    void checkSleeplessNightsAnalyzerWhenUserSleptWithTwoToFive() {
        List<SleepingSession> oneNight = List.of(SleepingSession.fromString("02.10.25 02:00;02.10.25 05:00;GOOD"));
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(oneNight);
        assertEquals(0L, result.getValue());
    }

    @Test
    void checkSleeplessNightsAnalyzerWhenUserSleptInDifferentMonths() {
        List<SleepingSession> oneNightInTwoMonths = List.of(SleepingSession.
                fromString("01.10.25 23:00;02.10.25 06:00;GOOD"), SleepingSession.
                fromString("02.11.25 23:00;03.11.25 06:00;GOOD"));
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(oneNightInTwoMonths);
        assertEquals(31L, result.getValue());
    }

    @Test
    void checkChronotypeAnalyzerWithOwlPreference() {
        List<SleepingSession> owlSessions = List.of(
                SleepingSession.fromString("01.10.25 23:15;02.10.25 09:30;GOOD"),
                SleepingSession.fromString("02.10.25 23:45;03.10.25 10:00;NORMAL"),
                SleepingSession.fromString("03.10.25 21:30;04.10.25 06:30;GOOD"));
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        SleepAnalysisResult result = analyzer.apply(owlSessions);
        assertEquals(Chronotype.SOVA.name(), result.getValue());
    }

    @Test
    void checkChronotypeAnalyzerWithTieReturnsGolub() {
        List<SleepingSession> tieSessions = List.of(
                SleepingSession.fromString("01.10.25 23:15;02.10.25 09:30;GOOD"),
                SleepingSession.fromString("02.10.25 21:00;03.10.25 06:30;NORMAL"));
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        SleepAnalysisResult result = analyzer.apply(tieSessions);
        assertEquals(Chronotype.GOLUB.name(), result.getValue());
    }

    @Test
    void checkChronotypeAnalyzerIgnoresDaytimeAndEmptyList() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        SleepAnalysisResult resultEmpty = analyzer.apply(EMPTY_LIST);
        assertEquals(Chronotype.GOLUB.name(), resultEmpty.getValue());
        List<SleepingSession> onlyDaytime = List.of(
                SleepingSession.fromString("05.10.25 13:30;05.10.25 14:15;NORMAL"));
        SleepAnalysisResult resultDaytime = analyzer.apply(onlyDaytime);
        assertEquals(Chronotype.GOLUB.name(), resultDaytime.getValue());
    }

}