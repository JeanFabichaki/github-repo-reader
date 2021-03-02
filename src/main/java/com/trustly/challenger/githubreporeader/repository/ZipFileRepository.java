package com.trustly.challenger.githubreporeader.repository;

import java.util.List;

import com.trustly.challenger.githubreporeader.domain.File;

import reactor.core.publisher.Mono;

public interface ZipFileRepository {

    Mono<List<File>> getListFiles(final String url);
}
