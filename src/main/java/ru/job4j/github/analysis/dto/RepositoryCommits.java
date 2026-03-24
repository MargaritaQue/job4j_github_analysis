package ru.job4j.github.analysis.dto;

import lombok.Data;
import ru.job4j.github.analysis.model.Commit;

import java.util.List;

@Data
public class RepositoryCommits {

    private Long repository_Id;
    private List<Commit> commits;
}
