package com.trustly.challenger.githubreporeader.service;

import static java.util.Objects.nonNull;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.trustly.challenger.githubreporeader.mapper.RepoRepresentationMapper;
import com.trustly.challenger.githubreporeader.presentation.controller.representation.ExtensionRepresentation;
import com.trustly.challenger.githubreporeader.repository.ZipFileRepository;
import com.trustly.challenger.githubreporeader.repository.impl.ZipFileRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubService {

    @Value("${app.url-github-zip-file}")
    private String urlGithub;

    private final ZipFileRepository zipFileRepository;
    private final RepoRepresentationMapper mapper;

    Cache<String, Flux<ExtensionRepresentation>> cache = Caffeine.newBuilder().build();

    public Flux<ExtensionRepresentation> getRepoInformation(final String user, final String repo) {

        var url = String.format(urlGithub, user, repo);

        return Optional.ofNullable(cache.getIfPresent(url)).orElseGet(() -> {
            log.info("Find information from endpoint {}", url);
            var extensions = zipFileRepository.getListFiles(url)
                .flatMapMany(mapper::toRepresentation);

            cache.put(url, extensions);
            return extensions;
        });
    }
}
