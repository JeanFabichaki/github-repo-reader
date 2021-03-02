package com.trustly.challenger.githubreporeader.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.trustly.challenger.githubreporeader.fixture.FileFixture;

class FileTest {

    @ParameterizedTest
    @CsvSource({"class.java,java", "dir/gradlew,gradlew", "/src/dir/test.java,java"})
    void whenReceiveJavaFileMustReturnJavaExtension(String fileName, String extension){
        final var file = FileFixture.get().withOneFile().withName(fileName).build();
        Assertions.assertEquals(extension, file.getExtension());
    }
}
