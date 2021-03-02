package com.trustly.challenger.githubreporeader.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trustly.challenger.githubreporeader.mapper.RepoRepresentationMapper;
import com.trustly.challenger.githubreporeader.presentation.controller.representation.ExtensionRepresentation;
import com.trustly.challenger.githubreporeader.presentation.controller.swagger.GithubAPI;
import com.trustly.challenger.githubreporeader.service.GithubService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/analysis")
public class GithubController implements GithubAPI {

    @Autowired
    private GithubService service;

    @Autowired
    private RepoRepresentationMapper mapper;

    @GetMapping("/user/{user}/repo/{repo}")
    public Flux<ExtensionRepresentation> getRepoInformation(@PathVariable String user, @PathVariable String repo) {
        return service.getRepoInformation(user, repo);
    }
}
