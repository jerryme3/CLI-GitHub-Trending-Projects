package github.trending.jerme;

import github.trending.jerme.client.GithubClient;
import github.trending.jerme.models.TrendingRepoGithub;

import java.util.Arrays;

public class App {

    private static final GithubClient GITHUB_CLIENT = new GithubClient();

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Arguments are expected but none were passed.");
            return;
        }

        if (args[0].equals("--help")) {
            System.out.println("Welcome to Github Trending Repository!");
            System.out.println("""
                    Commands
                    1. trending-repos - as itself, it sends back the call from the API. It defaults to the top 10 trending repository to this week.
                        command format: java -jar target/github-trending-jerme-1.jar trending-repos
                        reminders: it is also the entry point for every command you will do, so don't forget about it.
                    
                    2. --duration - it specifies what timeframe do you want to see, it includes these arguments:
                        today - let's you see the top repositories as of today.
                        week  - let's you see the top repositories as of this week.
                        month - let's you see the top repositories as of this month.
                        year  - let's you see the top repositories as of this year.
                    
                        command formats:
                            default: java -jar target/github-trending-jerme-1.jar trending-repos --duration today
                            with limit: java -jar target/github-trending-jerme-1.jar trending-repos --duration today -limit 5
                        reminders: as default, it will scrape just the top 10. should be always tailed by these arguments.
                                   it can be executed without the -limit function in the end.
                    
                    3. -limit - as it is, it limits the API response to its given argument. the argument should be an integer and in range of 1-30
                        as the maximum the API can scrape is up to 30.
                        command format:
                            with default duration: java -jar target/github-trending-jerme-1.jar trending-repos -limit 10
                            with duration function: java -jar target/github-trending-jerme-1.jar trending-repos --duration week -limit 5
                        reminders: as default (no -limit function) the program will only get 10 trending repository. furthermore, it can be executed
                                   without the --duration function with its argument.""");


            return;
        }

        if (!args[0].equals("trending-repos")) {
            System.out.println("Command cannot be executed. Enter \"--help\" for help.");
            return;
        }

        if (args.length == 1) {
            int i = 0;

            for (TrendingRepoGithub repo : GITHUB_CLIENT.getRecentPopularRepo()) {
                System.out.println(++i + ". " + formatRepo(repo));
            }

            return;
        }

        if (args.length == 3) {

            if (!args[1].equals("-limit") && !args[1].equals("--duration")) {
                System.out.println("Function " + args[1] + " cannot be executed. Type \"--help\" for help.");
                return;
            }

            if (args[1].equals("-limit") && (args[2].matches("\\d+") || args[2].matches("-\\d+"))) {
                if (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > 30) {
                    System.out.println("Your -limit argument is out of range (1-30 only).");
                    return;
                }

                int i = 0;

                for (TrendingRepoGithub repo : GITHUB_CLIENT.getRecentPopularRepo(Integer.parseInt(args[2]))) {
                    System.out.println(++i + ". " + formatRepo(repo));
                }
                return;
            } else if (args[1].equals("-limit") && !(args[2].matches("\\d+") || args[2].matches("-\\d+"))) {
                System.out.println("Argument for the function -limit should be an integer.");
                return;
            }

            if (!args[1].equals("--duration")) {
                System.out.println("Invalid duration command. Enter \"--help\" for help.");
                return;
            }

            if (Arrays.stream(new String[]{"today", "week", "month", "year"}).noneMatch(dur -> dur.equalsIgnoreCase(args[2]))) {
                System.out.println("Invalid duration arguments. Enter \"--help\" for help.");
            } else {
                int i = 0;

                for (TrendingRepoGithub repo : GITHUB_CLIENT.getTrendingRepoByDuration(args[2])) {
                    System.out.println(++i + ". " + formatRepo(repo));
                }

                return;
            }
        }
        if (args.length == 5) {
            if (!args[1].equals("--duration")) {
                if (args[1].equals("-limit")) {
                    System.out.println("The -limit function should come after the --duration function and its argument in this command.");
                } else {
                    System.out.println("This function is not executable. Enter \"--help\" for help.");
                }
                return;
            }

            if (Arrays.stream(new String[]{"today", "week", "month", "year"}).noneMatch(dur -> dur.equalsIgnoreCase(args[2]))) {
                System.out.println("Invalid duration argument. Enter \"--help\" for help.");
                return;
            }

            if (!args[3].equals("-limit")) {
                if (args[3].equals("--duration")) {
                    System.out.println("The --duration function should come before the -limit function with its argument in this command.");
                } else {
                    System.out.println("This function is not executable. Enter \"--help\" for help.");
                }
                return;
            }

            if (!(args[4].matches("\\d+") || args[4].matches("-\\d+"))) {
                System.out.println("Argument for the function -limit should be an integer.");
                return;
            }

            if (Integer.parseInt(args[4]) < 1 || Integer.parseInt(args[4]) > 30) {
                System.out.println("Your -limit argument is out of range (1-30 only).");
                return;
            }

            int i = 0;

            for (TrendingRepoGithub repo : GITHUB_CLIENT.getTrendingRepoByDuration(args[2], Integer.parseInt(args[4]))) {
                System.out.println(++i + ". " + formatRepo(repo));
            }
        }
    }

    private static String formatRepo(TrendingRepoGithub repo) {
        return String.format(
          "Repository Name: %s%n    Description: %s%n    Owner: %s%n    Link: %s%n    Topics: %s%n    Stars: %d%n    Date Created: %s%n    Date Updated: %s%n    Date Pushed: %s%n%n",
          repo.repoName(), repo.desc(), repo.owner(), repo.url(), repo.topics().toString(), repo.stars(), repo.dateCreated().toString(),
                repo.dateUpdated().toString(), repo.datePushed().toString()
        );
    }
}
