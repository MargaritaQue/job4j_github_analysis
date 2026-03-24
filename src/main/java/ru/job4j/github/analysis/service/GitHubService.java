package ru.job4j.github.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.dto.rest.responses.GitCommitResponse;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.remote.GitHubRemote;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubRemote gitHubRemote;

    public List<Repository> fetchRepositories(String userName) {
        return gitHubRemote.fetchRepositories(userName);
    }

    public List<GitCommitResponse> fetchCommits(String owner, String repositoryName) {
        return gitHubRemote.fetchCommits(owner, repositoryName);
    }

    public List<GitCommitResponse> fetchCommits(String owner, String repoName, String sha) {
        return gitHubRemote.fetchCommits(owner, repoName, sha);
    }
}
