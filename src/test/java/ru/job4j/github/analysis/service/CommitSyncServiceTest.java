package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.github.analysis.dto.rest.responses.GitCommitResponse;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.CommitRepository;
import ru.job4j.github.analysis.repository.RepositoryRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommitSyncServiceTest {

    @Mock
    private GitHubService gitHubService;

    @Mock
    private CommitRepository commitRepository;

    @Mock
    private RepositoryRepository repositoryRepository;

    @InjectMocks
    private CommitSyncService commitSyncService;

    @Test
    void syncCommitsSavesOnlyNewCommits() {
        Repository repo = new Repository();
        repo.setId(10L);
        repo.setName("r");
        repo.setUrl("https://github.com/o/r");
        when(repositoryRepository.findById(10L)).thenReturn(Optional.of(repo));

        when(commitRepository.findAllShaByRepositoryId(10L)).thenReturn(List.of("old-sha"));

        GitCommitResponse oldOne = gitCommitResponse("old-sha", "m1");
        GitCommitResponse newOne = gitCommitResponse("new-sha", "m2");
        when(gitHubService.fetchCommits("owner", "repo")).thenReturn(List.of(oldOne, newOne));

        commitSyncService.syncCommits(10L, "owner", "repo");

        ArgumentCaptor<List<Commit>> captor = ArgumentCaptor.forClass(List.class);
        verify(commitRepository).saveAll(captor.capture());
        List<Commit> savedCommits = captor.getValue();
        assertEquals(1, savedCommits.size());
        assertEquals("new-sha", savedCommits.getFirst().getSha());
        assertEquals(10L, savedCommits.getFirst().getRepository().getId());
    }

    @Test
    void syncCommitsWhenGitHubReturnsEmptyDoesNotSave() {
        Repository repo = new Repository();
        repo.setId(1L);
        when(repositoryRepository.findById(1L)).thenReturn(Optional.of(repo));
        when(commitRepository.findAllShaByRepositoryId(1L)).thenReturn(List.of());
        when(gitHubService.fetchCommits("owner", "repo")).thenReturn(List.of());

        commitSyncService.syncCommits(1L, "owner", "repo");

        verify(commitRepository, never()).saveAll(anyList());
    }

    @Test
    void syncCommitsWhenGitHubReturnsNullDoesNotSave() {
        Repository repo = new Repository();
        repo.setId(1L);
        when(repositoryRepository.findById(1L)).thenReturn(Optional.of(repo));
        when(commitRepository.findAllShaByRepositoryId(1L)).thenReturn(List.of());
        when(gitHubService.fetchCommits("owner", "repo")).thenReturn(null);

        commitSyncService.syncCommits(1L, "owner", "repo");

        verify(commitRepository, never()).saveAll(any());
    }

    private static GitCommitResponse gitCommitResponse(String sha, String message) {
        GitCommitResponse r = new GitCommitResponse();
        r.setSha(sha);
        GitCommitResponse.CommitData cd = new GitCommitResponse.CommitData();
        GitCommitResponse.AuthorData ad = new GitCommitResponse.AuthorData();
        ad.setName("author");
        ad.setDate(OffsetDateTime.parse("2026-03-24T12:00:00Z"));
        cd.setMessage(message);
        cd.setAuthor(ad);
        r.setCommit(cd);
        return r;
    }
}
