##Project Description
- A CLI-based application that let the user see the top repositories within the given timeframe, with a choice of limiting the response!

##Commands & Guide

1. trending-repos - This is the base command. It sends the call to the API and defaults to showing the top 10 trending repositories for this week.
    command format: java -jar target/github-trending-jerme-1.jar trending-repos
    reminder: this is also the entry point for every command, so don't forget to include it.

2. --duration - Specifies the timeframe you want to see. It accepts these arguments:
    today - lets you see the top repositories as of today.
    week  - lets you see the top repositories as of this week.
    month - lets you see the top repositories as of this month.
    year  - lets you see the top repositories as of this year.

    command formats:
        default: java -jar target/github-trending-jerme-1.jar trending-repos --duration today
        with limit: java -jar target/github-trending-jerme-1.jar trending-repos --duration today -limit 5
    reminders: by default, it will scrape only the top 10, so this should usually be followed by the -limit argument.
               It can also be run without -limit.

3. -limit - Limits the API response to the given argument. The argument must be an integer within the range of 1-30,
    since 30 is the maximum the API can scrape.
    command format:
        with default duration: java -jar target/github-trending-jerme-1.jar trending-repos -limit 10
        with duration function: java -jar target/github-trending-jerme-1.jar trending-repos --duration week -limit 5
    reminders: by default (without -limit), the program will only fetch 10 trending repositories.
               It can also be run without --duration.

   ##Project Link
   - https://roadmap.sh/projects/github-trending-cli
