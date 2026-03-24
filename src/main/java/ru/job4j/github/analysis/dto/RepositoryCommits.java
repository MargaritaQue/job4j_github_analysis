package ru.job4j.github.analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.job4j.github.analysis.model.Commit;

import java.util.List;

@Data
public class RepositoryCommits {

    @JsonProperty("repository_Id")
    private Long repositoryId;
    private List<Commit> commits;
}
