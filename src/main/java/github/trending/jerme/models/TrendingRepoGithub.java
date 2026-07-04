package github.trending.jerme.models;

import java.time.LocalDate;
import java.util.List;

public record TrendingRepoGithub(
        String repoName,
        String desc,
        String owner,
        String url,
        List<String> topics,
        int stars,
        LocalDate dateCreated,
        LocalDate dateUpdated,
        LocalDate datePushed) {
}
