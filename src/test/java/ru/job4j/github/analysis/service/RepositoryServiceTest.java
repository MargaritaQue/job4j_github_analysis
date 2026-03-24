package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.RepositoryRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceTest {

    @Mock
    private RepositoryRepository repositoryRepository;

    @Mock
    private CommitSyncService commitSyncService;

    @InjectMocks
    private RepositoryService repositoryService;

    private Repository incoming;

    @BeforeEach
    void setUp() {
        incoming = new Repository();
        incoming.setName("repo");
        incoming.setUrl("https://github.com/owner/repo");
    }

    @Test
    void createWhenNewRepositorySavesThenSchedulesSync() {
        when(repositoryRepository.findByUrl(incoming.getUrl())).thenReturn(null);
        Repository saved = new Repository();
        saved.setId(10L);
        saved.setName(incoming.getName());
        saved.setUrl(incoming.getUrl());
        when(repositoryRepository.save(any(Repository.class))).thenReturn(saved);

        repositoryService.create(incoming);

        verify(commitSyncService).syncCommits(eq(10L), eq("owner"), eq("repo"));
    }

    @Test
    void createWhenExistingRepositorySchedulesSyncById() {
        Repository existing = new Repository();
        existing.setId(7L);
        existing.setName(incoming.getName());
        existing.setUrl(incoming.getUrl());
        when(repositoryRepository.findByUrl(incoming.getUrl())).thenReturn(existing);

        repositoryService.create(incoming);

        verify(commitSyncService).syncCommits(eq(7L), eq("owner"), eq("repo"));
    }
}
