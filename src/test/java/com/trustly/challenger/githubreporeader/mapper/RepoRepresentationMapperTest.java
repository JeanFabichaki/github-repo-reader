package com.trustly.challenger.githubreporeader.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.trustly.challenger.githubreporeader.fixture.FileFixture;

class RepoRepresentationMapperTest {

    @Test
    void whenSendOneFileMustReturnOKK() {

        var mapper = new RepoRepresentationMapper();
        var result = mapper.toRepresentation(Collections.singletonList(FileFixture.get()
            .withOneFile().build()));

        final var extensionRepresentation = Objects.requireNonNull(result.blockFirst());
        Assertions.assertNotNull(extensionRepresentation);
        Assertions.assertEquals(1, extensionRepresentation.getTotalLines());
        Assertions.assertEquals(1, extensionRepresentation.getTotalBytes());
        Assertions.assertEquals("tst", extensionRepresentation.getExtension());
    }

    @Test
    void whenSendListFilesMustMustReturnOK() {

        var mapper = new RepoRepresentationMapper();
        var file = FileFixture.get().withOneFile().build();
        var result = mapper.toRepresentation(Collections.nCopies(5, file));

        var list = result.collectList().block();
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(5, list.get(0).getTotalLines());
        Assertions.assertEquals(5, list.get(0).getTotalBytes());
    }

    @Test
    void whenSendListFilesWithDifferentExtensionMustReturnOK() {

        var mapper = new RepoRepresentationMapper();
        var file = FileFixture.get().withOneFile().build();
        var fileJava = FileFixture.get().withOneFile().withName("class.java").build();
        var result = mapper.toRepresentation(List.of(file, file, fileJava));

        var list = result.collectList().block();
        Assertions.assertEquals(2, list.size());
        final var groupJava = list.stream().filter(group -> group.getExtension().equals("java")).findFirst();
        Assertions.assertNotNull(groupJava.get());

        Assertions.assertEquals(1, groupJava.get().getTotalBytes());
        Assertions.assertEquals(1, groupJava.get().getTotalLines());
    }
}
