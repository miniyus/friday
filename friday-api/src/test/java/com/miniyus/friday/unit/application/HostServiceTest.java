package com.miniyus.friday.unit.application;

import com.github.javafaker.Faker;
import com.miniyus.friday.adapter.in.rest.request.RetrieveHostRequest;
import com.miniyus.friday.adapter.in.rest.request.CreateHostRequest;
import com.miniyus.friday.adapter.in.rest.request.UpdateHostRequest;
import com.miniyus.friday.application.service.HostService;
import com.miniyus.friday.domain.hosts.HostIds;
import com.miniyus.friday.domain.hosts.Host;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HostServiceTest {
    @Mock
    private CreateHostPort createHostPort;

    @Mock
    private RetrieveHostPort retrieveHostPort;

    @Mock
    private UpdateHostPort updateHostPort;

    @Mock
    private DeleteHostPort deleteHostPort;

    @InjectMocks
    private HostService hostService;

    private List<Host> testDomains;

    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        testDomains = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            var testDomain = new Host(
                i + 1L,
                faker.internet().domainName(),
                faker.name().username(),
                faker.name().fullName(),
                "/test/" + i,
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                1L,
                null
            );
            testDomains.add(testDomain);
        }
    }

    @Test
    public void createHostTest() {
        var testDomain = testDomains.get(0);
        CreateHostRequest createHostRequest = CreateHostRequest.builder()
            .host(testDomain.getHost())
            .summary(testDomain.getSummary())
            .description(testDomain.getDescription())
            .path(testDomain.getPath())
            .publish(testDomain.isPublish())
            .build();

        when(createHostPort.isUniqueHost(any())).thenReturn(true);
        when(createHostPort.create(any())).thenReturn(testDomain);

        Host created = hostService.createHost(createHostRequest.toDomain(1L));

        Assertions.assertThat(created)
            .hasFieldOrPropertyWithValue("host", created.getHost())
            .hasFieldOrPropertyWithValue("summary", created.getSummary())
            .hasFieldOrPropertyWithValue("description", created.getDescription())
            .hasFieldOrPropertyWithValue("path", created.getPath())
            .hasFieldOrPropertyWithValue("publish", created.isPublish());
    }

    @Test
    public void updateHostTest() {
        var testDomain = testDomains.get(0);
        var host = faker.internet().domainName();
        var summary = faker.lorem().word();
        var desc = faker.lorem().sentence();
        var path = faker.space().starCluster();

        UpdateHostRequest updateHostRequest = UpdateHostRequest.builder()
            .host(host)
            .summary(summary)
            .description(desc)
            .path(path)
            .build();
        when(updateHostPort.update(any())).thenReturn(
            new Host(
                1L,
                host,
                summary,
                desc,
                path,
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                1L,
                null
            )
        );

        when(updateHostPort.findById(any())).thenReturn(Optional.of(testDomain));

        Host updated = hostService.updateHost(updateHostRequest.toDomain(1L, 1L));

        Assertions.assertThat(updated)
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("host", host)
            .hasFieldOrPropertyWithValue("summary", summary)
            .hasFieldOrPropertyWithValue("description", desc)
            .hasFieldOrPropertyWithValue("path", path)
            .hasFieldOrPropertyWithValue("publish", true);
    }

    @Test
    public void retrieveHostTest() {
        var testDomain = testDomains.get(0);
        when(retrieveHostPort.findById(1L)).thenReturn(Optional.of(testDomain));

        Host host = hostService.retrieveById(new HostIds(1L, 1L));
        Assertions.assertThat(host)
            .hasFieldOrPropertyWithValue("host", host.getHost())
            .hasFieldOrPropertyWithValue("summary", host.getSummary())
            .hasFieldOrPropertyWithValue("description", host.getDescription())
            .hasFieldOrPropertyWithValue("path", host.getPath())
            .hasFieldOrPropertyWithValue("publish", host.isPublish());
    }

    @Test
    public void retrieveHostsTest() {
        var pageable = PageRequest.of(1, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        var retrieveHostRequest = RetrieveHostRequest
            .builder()
            .pageable(pageable)
            .build();

        Collections.reverse(testDomains);

        var page = new PageImpl<>(testDomains, pageable, testDomains.size());

        when(retrieveHostPort.findAll(any(), any(Pageable.class))).thenReturn(page);

        var hosts = hostService.retrieveAll(retrieveHostRequest.toDomain(1L),
            retrieveHostRequest.getPageable());
        Assertions.assertThat(hosts)
            .hasSize(testDomains.size());

        hosts.stream()
            .forEachOrdered(host -> Assertions.assertThat(host)
                .hasFieldOrProperty("host")
                .hasFieldOrProperty("summary")
                .hasFieldOrProperty("description")
                .hasFieldOrProperty("path")
                .hasFieldOrProperty("publish")
            );

        var hostList = hosts.stream().toList();
        IntStream.range(0, hostList.size()).forEach(
            index -> Assertions.assertThat(hostList.get(index))
                .hasFieldOrPropertyWithValue("id", hostList.get(index).getId())
        );
    }

    @Test
    public void deleteHostTest() {
        when(retrieveHostPort.findById(1L)).thenReturn(Optional.empty());

        deleteHostPort.deleteById(1L);

        var host = retrieveHostPort.findById(1L);
        Assertions.assertThat(host)
            .isEmpty();
    }
}
