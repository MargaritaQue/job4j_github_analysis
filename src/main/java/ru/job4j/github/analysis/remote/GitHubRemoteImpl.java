package ru.job4j.github.analysis.remote;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.github.analysis.dto.rest.responses.GitCommitResponse;
import ru.job4j.github.analysis.model.Repository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubRemoteImpl implements GitHubRemote {

    private final RestTemplate restTemplate;

    @Override
    public List<Repository> fetchRepositories(String userName) {
        String url = "https://api.github.com/users/" + userName + "/repos";
        ResponseEntity<List<Repository>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    @Override
    public List<GitCommitResponse> fetchCommits(String owner, String repositoryName) {
        String url = String.format("https://api.github.com/repos/%s/%s/commits", owner, repositoryName);
        ResponseEntity<List<GitCommitResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    @Override
    public List<GitCommitResponse> fetchCommits(String owner, String repoName, String sha) {
        String url = String.format("https://api.github.com/repos/%s/%s/commits?sha=%s", owner, repoName, sha);
        ResponseEntity<List<GitCommitResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }
}
