package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.repository.RepositoryRepository;

@AllArgsConstructor
@Service
public class ScheduledTasks {
    private final RepositoryService repositoryService;
    private final RepositoryRepository repositoryRepository;

    @Scheduled(fixedDelay = 60000)
    public void fetchCommits() {
        repositoryRepository.findAll().forEach(repositoryService::create);
    }
}
