package github.trending.jerme.reader;

import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDate;

public class APIReader {

    private static final Dotenv DOTENV = Dotenv.load();

    public static String getAPIByDate(LocalDate date) {
        return DOTENV.get("GITHUB_TRENDING_API") + date + "&sort=stars&order=desc";
    }

    public static String getAPIForRecentTrending() {
        return DOTENV.get("GITHUB_TRENDING_API") + LocalDate.now().minusDays(7) + "&sort=stars&order=desc";
    }
}
