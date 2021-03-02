package com.trustly.challenger.githubreporeader.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.trustly.challenger.githubreporeader.fixture.FileFixture;

class FileTest {

    @Test
    void whenReceiveJavaFileMustReturnJavaExtension(){
        final var file = FileFixture.get().withOneFile().withName("class.java").build();
        Assertions.assertEquals("java", file.getExtension());
    }
}
