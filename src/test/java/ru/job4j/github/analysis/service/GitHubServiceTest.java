package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.github.analysis.dto.rest.responses.GitCommitResponse;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.remote.GitHubRemote;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    @Mock
    private GitHubRemote gitHubRemote;

    @InjectMocks
    private GitHubService gitHubService;

    @Test
    void fetchRepositoriesDelegatesToRemote() {
        List<Repository> expected = List.of(new Repository());
        when(gitHubRemote.fetchRepositories("alice")).thenReturn(expected);

        assertSame(expected, gitHubService.fetchRepositories("alice"));
        verify(gitHubRemote).fetchRepositories("alice");
    }

    @Test
    void fetchCommitsByOwnerAndRepoDelegatesToRemote() {
        List<GitCommitResponse> expected = List.of();
        when(gitHubRemote.fetchCommits("owner", "repo")).thenReturn(expected);

        assertEquals(expected, gitHubService.fetchCommits("owner", "repo"));
        verify(gitHubRemote).fetchCommits("owner", "repo");
    }

    @Test
    void fetchCommitsWithShaDelegatesToRemote() {
        List<GitCommitResponse> expected = List.of();
        when(gitHubRemote.fetchCommits("owner", "repo", "abc123")).thenReturn(expected);

        assertEquals(expected, gitHubService.fetchCommits("owner", "repo", "abc123"));
        verify(gitHubRemote).fetchCommits("owner", "repo", "abc123");
    }
}
