package ru.job4j.github.analysis.remote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.job4j.github.analysis.dto.rest.responses.GitCommitResponse;
import ru.job4j.github.analysis.model.Repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubRemoteImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GitHubRemoteImpl gitHubRemoteImpl;

    @Test
    void fetchRepositoriesCallsGitHubUsersReposUrl() {
        List<Repository> body = List.of();
        when(restTemplate.exchange(
                eq("https://api.github.com/users/alice/repos"),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<Repository>>>any()))
                .thenReturn(ResponseEntity.ok(body));

        assertEquals(body, gitHubRemoteImpl.fetchRepositories("alice"));

        verify(restTemplate).exchange(
                eq("https://api.github.com/users/alice/repos"),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<Repository>>>any());
    }

    @Test
    void fetchCommitsCallsGitHubReposCommitsUrl() {
        List<GitCommitResponse> body = List.of();
        when(restTemplate.exchange(
                eq("https://api.github.com/repos/o/r/commits"),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<GitCommitResponse>>>any()))
                .thenReturn(ResponseEntity.ok(body));

        assertEquals(body, gitHubRemoteImpl.fetchCommits("o", "r"));

        verify(restTemplate).exchange(
                eq("https://api.github.com/repos/o/r/commits"),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<GitCommitResponse>>>any());
    }

    @Test
    void fetchCommitsWithShaCallsUrlWithQueryParam() {
        List<GitCommitResponse> body = List.of();
        when(restTemplate.exchange(
                eq("https://api.github.com/repos/o/r/commits?sha=deadbeef"),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<GitCommitResponse>>>any()))
                .thenReturn(ResponseEntity.ok(body));

        assertEquals(body, gitHubRemoteImpl.fetchCommits("o", "r", "deadbeef"));

        verify(restTemplate).exchange(
                eq("https://api.github.com/repos/o/r/commits?sha=deadbeef"),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<GitCommitResponse>>>any());
    }
}
