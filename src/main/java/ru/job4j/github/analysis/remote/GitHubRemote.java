package ru.job4j.github.analysis.remote;

import ru.job4j.github.analysis.dto.rest.responses.GitCommitResponse;
import ru.job4j.github.analysis.model.Repository;

import java.util.List;

public interface GitHubRemote {

    List<Repository> fetchRepositories(String userName);

    List<GitCommitResponse> fetchCommits(String owner, String repositoryName);

    List<GitCommitResponse> fetchCommits(String owner, String repoName, String sha);
}
