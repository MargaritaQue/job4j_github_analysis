package ru.job4j.github.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.dto.rest.responses.GitCommitResponse;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.CommitRepository;
import ru.job4j.github.analysis.repository.RepositoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommitSyncService {

    private final GitHubService gitHubService;
    private final CommitRepository commitRepository;
    private final RepositoryRepository repositoryRepository;

    @Async
    public void syncCommits(Long repositoryId, String owner, String repoName) {
        Repository saveRepository = repositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new IllegalStateException("Repository not found: id=" + repositoryId));

        List<String> allShaDB = commitRepository.findAllShaByRepositoryId(repositoryId);

        List<GitCommitResponse> gitHubCommits = gitHubService.fetchCommits(owner, repoName);
        if (gitHubCommits != null && !gitHubCommits.isEmpty()) {
            List<Commit> newCommits = gitHubCommits.stream()
                    .filter(c -> c.getSha() != null && !allShaDB.contains(c.getSha()))
                    .map(c -> c.toEntity(saveRepository))
                    .toList();
            if (!newCommits.isEmpty()) {
                commitRepository.saveAll(newCommits);
            }
        }
    }
}
