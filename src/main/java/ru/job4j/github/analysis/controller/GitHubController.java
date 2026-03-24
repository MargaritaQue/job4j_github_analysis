package ru.job4j.github.analysis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.github.analysis.dto.RepositoryCommits;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.CommitRepository;
import ru.job4j.github.analysis.repository.RepositoryRepository;
import ru.job4j.github.analysis.service.RepositoryService;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GitHubController {

    private final RepositoryService repositoryService;
    private final RepositoryRepository repositoryRepository;
    private final CommitRepository commitRepository;

    @GetMapping("/repositories")
    public List<Repository> getAllRepositories() {
        return repositoryRepository.findAll();
    }

    @GetMapping("/commits/{name}")
    public RepositoryCommits getCommits(@PathVariable(value = "name") String name) {
        Repository repo = repositoryRepository.findByName(name);
        if (repo == null) {
            throw new ResponseStatusException(NOT_FOUND, "Repository not found: " + name);
        }
        RepositoryCommits dto = new RepositoryCommits();
        dto.setRepositoryId(repo.getId());
        dto.setCommits(commitRepository.findAllByRepositoryId(repo.getId()));
        return dto;
    }

    @PostMapping("/repository")
    public ResponseEntity<Void> create(@RequestBody Repository repository) {
        repositoryService.create(repository);
        return ResponseEntity.noContent().build();
    }
}
