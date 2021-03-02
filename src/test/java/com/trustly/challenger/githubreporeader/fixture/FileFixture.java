package com.trustly.challenger.githubreporeader.fixture;

import com.trustly.challenger.githubreporeader.domain.File;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FileFixture {

    private File file;

    public static FileFixture get() {
        return new FileFixture();
    }

    public FileFixture withOneFile() {
        file = File.builder()
            .bytes(1L)
            .lines(1L)
            .name("/test/.tst")
            .build();

        return this;
    }

    public FileFixture withName(String extension) {
        file.setName(extension);
        return this;
    }

    public File build() {
        return file;
    }
}
