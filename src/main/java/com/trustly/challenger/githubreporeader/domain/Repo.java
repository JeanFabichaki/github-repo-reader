package com.trustly.challenger.githubreporeader.domain;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Repo {

    private String user;
    private String repo;
    private List<File> files;

}
