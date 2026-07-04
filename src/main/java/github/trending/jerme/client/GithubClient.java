package github.trending.jerme.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import github.trending.jerme.models.TrendingRepoGithub;
import github.trending.jerme.reader.APIReader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GithubClient {

    private final Gson gson;
    private final HttpClient client;

    public GithubClient() {
        this.gson = new Gson();
        this.client = HttpClient.newBuilder().build();
    }

    public List<TrendingRepoGithub> getRecentPopularRepo() {
        var listOfTrendyRepo = new ArrayList<TrendingRepoGithub>(10);

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(APIReader.getAPIForRecentTrending()))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "jerryme3")
                .build();

        return formatList(listOfTrendyRepo, request, 10);
    }

    public List<TrendingRepoGithub> getRecentPopularRepo(int limit) {
        var listOfTrendyRepo = new ArrayList<TrendingRepoGithub>(10);

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(APIReader.getAPIForRecentTrending()))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "jerryme3")
                .build();

        return formatList(listOfTrendyRepo, request, limit);
    }

    public List<TrendingRepoGithub> getTrendingRepoByDuration(String duration) {
        LocalDate date = parseDate(duration);

        var listOfTrendyRepo = new ArrayList<TrendingRepoGithub>(10);

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(APIReader.getAPIByDate(date)))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "jerryme3")
                .timeout(Duration.ofSeconds(10))
                .build();

        return formatList(listOfTrendyRepo, request, 10);
    }

    public List<TrendingRepoGithub> getTrendingRepoByDuration(String duration, int limit) {
        LocalDate date = parseDate(duration);

        var listOfTrendyRepo = new ArrayList<TrendingRepoGithub>(10);

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(APIReader.getAPIByDate(date)))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "jerryme3")
                .timeout(Duration.ofSeconds(10))
                .build();

        return formatList(listOfTrendyRepo, request, limit);
    }

    private List<TrendingRepoGithub> formatList(List<TrendingRepoGithub> listOfTrendyRepo, HttpRequest request, int limit) {

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) throw new IllegalArgumentException("Github API error: " + response.statusCode() + " - " + response.body());

            var root   = gson.fromJson(response.body(), JsonObject.class);
            var arrObj = root.getAsJsonArray("items");

            for (var element : arrObj) {
                if (listOfTrendyRepo.size() == limit) break;
                var topics = new ArrayList<String>(5);

                var curRepo = element.getAsJsonObject();
                var details = curRepo.get("owner").getAsJsonObject();

                String repoName = curRepo.get("name").getAsString();
                String desc     = curRepo.get("description").isJsonNull() ? "Description isn't available." : curRepo.get("description").getAsString();
                String owner    = details.get("login").getAsString();
                String url      = details.get("html_url").getAsString();
                var topicsArr   = curRepo.get("topics").getAsJsonArray();

                for (var ele : topicsArr) {
                    topics.add(ele.getAsString());
                }

                int stars       = curRepo.get("stargazers_count").getAsInt();
                var dateCreated = curRepo.get("created_at").getAsString().substring(0, 10);
                var dateUpdated = curRepo.get("updated_at").getAsString().substring(0, 10);
                var datePushed  = curRepo.get("pushed_at").getAsString().substring(0, 10);

                listOfTrendyRepo.add(new TrendingRepoGithub(
                        repoName, desc, owner, url, topics, stars,
                        LocalDate.parse(dateCreated),
                        LocalDate.parse(dateUpdated),
                        LocalDate.parse(datePushed)
                ));
            }

            return listOfTrendyRepo;

        } catch (InterruptedException | IOException e) {
            throw new IllegalArgumentException("Error: ", e);
        }
    }

    private LocalDate parseDate(String duration) {
        return switch (duration.trim().toLowerCase()) {
            case "today" -> LocalDate.now().minusDays(1);
            case "week"  -> LocalDate.now().minusWeeks(1);
            case "month" -> LocalDate.now().minusMonths(1);
            case "year" -> LocalDate.now().minusYears(1);
            default     -> throw new IllegalArgumentException(duration + " is not an appropriate argument for \"--duration\"");
        };
    }
}
