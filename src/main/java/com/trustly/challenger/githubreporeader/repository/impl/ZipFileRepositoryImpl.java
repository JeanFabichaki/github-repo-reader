package com.trustly.challenger.githubreporeader.repository.impl;

import static java.util.Objects.nonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.trustly.challenger.githubreporeader.domain.File;
import com.trustly.challenger.githubreporeader.exception.NotFoundException;
import com.trustly.challenger.githubreporeader.exception.ZipFileException;
import com.trustly.challenger.githubreporeader.repository.ZipFileRepository;
import com.trustly.challenger.githubreporeader.util.ZippedFileInputStream;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class ZipFileRepositoryImpl implements ZipFileRepository {

    public Mono<List<File>> getListFiles(final String url) {
        return this.downloadStreamFile(url)
            .map(this::convertInputStreamToListFile);
    }

   private Mono<InputStream> downloadStreamFile(final String url) {

        return WebClient.builder()
            .baseUrl(url)
            .build()
            .get().retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, resp -> Mono.error(new NotFoundException()))
            .bodyToFlux(DataBuffer.class)
            .map(b -> b.asInputStream(true))
            .reduce(SequenceInputStream::new);
    }

    private List<File> convertInputStreamToListFile(final InputStream inputStream) throws ZipFileException {

        var informationList = new ArrayList<File>();
        try (ZipInputStream stream = new ZipInputStream(inputStream)) {

            ZipEntry entry;

            while (nonNull((entry = stream.getNextEntry()))) {

                if (entry.getSize() > 0) {
                    informationList.add(this.getFileInformation(entry,
                        new ZippedFileInputStream(stream)));
                }
            }

        } catch (IOException e) {
            log.error("Error read zip file", e);
            throw new ZipFileException();
        }

        return informationList;
    }

    private File getFileInformation(final ZipEntry entry, ZippedFileInputStream stream) throws IOException {

        return File.builder()
            .bytes(entry.getSize())
            .name(entry.getName())
            .lines(getLinesFile(stream))
            .build();
    }

    private long getLinesFile(InputStream inputStream) throws IOException {

        var reader = new BufferedReader(new InputStreamReader(inputStream));
        var lines = reader.lines().count();
        inputStream.close();

        return lines;
    }

}
