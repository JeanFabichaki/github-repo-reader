package com.trustly.challenger.githubreporeader.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.trustly.challenger.githubreporeader.domain.File;
import com.trustly.challenger.githubreporeader.presentation.controller.representation.ExtensionRepresentation;

import reactor.core.publisher.Flux;

@Component
public class RepoRepresentationMapper {


    public Flux<ExtensionRepresentation> toRepresentation(List<File> files) {

        var streamFiles = files.stream()
            .collect(Collectors.groupingBy(File::getExtension)).entrySet()
            .stream().map(entry ->
                ExtensionRepresentation.builder()
                    .extension(entry.getKey())
                    .totalLines(entry.getValue().stream().mapToLong(File::getLines).sum())
                    .totalBytes(entry.getValue().stream().mapToLong(File::getBytes).sum())
                    .build()
            );

        return Flux.fromStream(streamFiles);
    }
}
