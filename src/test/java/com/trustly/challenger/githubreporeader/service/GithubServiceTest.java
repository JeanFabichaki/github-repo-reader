package com.trustly.challenger.githubreporeader.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.trustly.challenger.githubreporeader.fixture.FileFixture;
import com.trustly.challenger.githubreporeader.mapper.RepoRepresentationMapper;
import com.trustly.challenger.githubreporeader.repository.impl.ZipFileRepositoryImpl;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class GithubServiceTest {

    private GithubService service;
    private ZipFileRepositoryImpl repository;
    private RepoRepresentationMapper mapper;

    @BeforeAll
    void setUp() {
        repository = mock(ZipFileRepositoryImpl.class);
        mapper = spy(RepoRepresentationMapper.class);
        service = new GithubService(repository, mapper);

        ReflectionTestUtils.setField(service, "urlGithub", "https://codeload.github.com/%s/%s/zip/master");
    }

    @Test
    void whenReceiveUserAndRepoMustReturnListOfFiles() {

        final var files = Mono.just(List.of(FileFixture.get().withOneFile().build()));
        when(repository.getListFiles(any())).thenReturn(files);

        var result = service.getRepoInformation("JeanFabichaki", "todo-rust");
        var list = result.collectList().block();

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("tst", list.get(0).getExtension());

    }
}
