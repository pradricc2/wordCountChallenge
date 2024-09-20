package countPattern;

public class CountStrategyFactory {
    public static CountStrategy getStrategy(String option) {
        switch (option) {
            case "-c":
                return new ByteCountStrategy();
            case "-l":
                return new LineCountStrategy();
            case "-w":
                return new WordCountStrategy();
            case "-m":
                return new CharacterCountStrategy();
            default:
                throw new IllegalArgumentException("Invalid option: " + option);
        }
    }
}