package ru.yandex.practicum.sleeptracker;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
public class SleepTrackerApp {
    private static final List<Function<List<SleepingSession>,SleepAnalysisResult>> analyzers =
            List.of(new CountSessionsAnalyzer(), new MaxDurationAnalyzer(), new MinDurationAnalyzer(),
                    new AverageDurationAnalyzer(),new BadQualityAnalyzer(), new SleeplessNightsAnalyzer(),
                    new ChronotypeAnalyzer());

    public static void main(String[] args) {

        List<SleepingSession> sessions = readSessions(args[0]);
        analyzers.stream().map(analyzer -> analyzer.apply(sessions)).
                forEach(System.out :: println);
    }

    public static List<SleepingSession> readSessions(String path){
        try{
            return Files.lines(Path.of(path)).map(SleepingSession :: fromString).toList();
        }catch(IOException e){
            throw new RuntimeException("Ошибка чтения файла", e);
        }
    }




}