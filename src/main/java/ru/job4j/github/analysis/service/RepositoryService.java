package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.RepositoryRepository;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@AllArgsConstructor
@Service
public class RepositoryService {

    private final RepositoryRepository repositoryRepository;
    private final CommitSyncService commitSyncService;

    public void create(Repository repository) {
        String[] urlSplit = repository.getUrl().split("/");
        String owner = urlSplit[3];
        String repoName = urlSplit[4];
        if (!repository.getName().equals(repoName)) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Field name must match repository name in url: expected '" + repoName + "', got '" + repository.getName() + "'"
            );
        }
        Repository existing = repositoryRepository.findByUrl(repository.getUrl());
        Repository saveRepository;

        if (existing == null) {
            Repository rep = new Repository();
            rep.setName(repository.getName());
            rep.setUrl(repository.getUrl());
            saveRepository = repositoryRepository.save(rep);
        } else {
            saveRepository = existing;
        }

        commitSyncService.syncCommits(saveRepository.getId(), owner, repoName);
    }
}
