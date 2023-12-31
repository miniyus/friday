package com.meteormin.friday.hosts.application;

import com.meteormin.friday.annotation.UnitTest;
import com.meteormin.friday.api.hosts.request.CreateHostRequest;
import com.meteormin.friday.api.hosts.request.RetrieveHostRequest;
import com.meteormin.friday.api.hosts.request.UpdateHostRequest;
import com.meteormin.friday.common.util.JsonNullableUtil;
import com.meteormin.friday.hosts.application.port.out.HostPort;
import com.meteormin.friday.hosts.application.service.HostService;
import com.meteormin.friday.hosts.domain.Host;
import com.meteormin.friday.hosts.domain.HostIds;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HostServiceTest {
    @Mock
    private HostPort hostPort;

    @InjectMocks
    private HostService hostService;

    private List<Host> testDomains;

    private final Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        testDomains = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            var testDomain = new Host(
                i + 1L,
                faker.internet().domainName(),
                faker.name().name(),
                faker.lorem().sentence(),
                "/test/" + i,
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                1L
            );
            testDomains.add(testDomain);
        }
    }

    @UnitTest
    public void createHostTest() {
        var testDomain = testDomains.get(0);
        CreateHostRequest createHostRequest = CreateHostRequest.builder()
            .host(testDomain.getHostname())
            .summary(testDomain.getSummary())
            .description(testDomain.getDescription())
            .path(testDomain.getPath())
            .publish(testDomain.isPublish())
            .build();

        when(hostPort.isUniqueHost(any())).thenReturn(true);
        when(hostPort.create(any())).thenReturn(testDomain);

        Host created = hostService.createHost(createHostRequest.toDomain(1L));

        Assertions.assertThat(created)
            .hasFieldOrPropertyWithValue("hostname", created.getHostname())
            .hasFieldOrPropertyWithValue("summary", created.getSummary())
            .hasFieldOrPropertyWithValue("description", created.getDescription())
            .hasFieldOrPropertyWithValue("path", created.getPath())
            .hasFieldOrPropertyWithValue("publish", created.isPublish());
    }

    @UnitTest
    public void updateHostTest() {
        var testDomain = testDomains.get(0);
        var host = JsonNullable.of(faker.internet().domainName());
        var summary = JsonNullable.of(faker.lorem().word());
        var desc = JsonNullable.of(faker.lorem().sentence());
        var path = JsonNullable.of(faker.space().starCluster());

        UpdateHostRequest updateHostRequest = UpdateHostRequest.builder()
            .host(host)
            .summary(summary)
            .description(desc)
            .path(path)
            .build();

        when(hostPort.update(any())).thenReturn(
            new Host(
                1L,
                JsonNullableUtil.unwrap(host, null),
                JsonNullableUtil.unwrap(summary, null),
                JsonNullableUtil.unwrap(desc, null),
                JsonNullableUtil.unwrap(path, null),
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                1L));

        when(hostPort.findById(any())).thenReturn(Optional.of(testDomain));

        Host updated = hostService.patchHost(updateHostRequest.toDomain(1L, 1L));

        Assertions.assertThat(updated)
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("hostname", host.get())
            .hasFieldOrPropertyWithValue("summary", summary.get())
            .hasFieldOrPropertyWithValue("description", desc.get())
            .hasFieldOrPropertyWithValue("path", path.get())
            .hasFieldOrPropertyWithValue("publish", true);
    }

    @UnitTest
    public void retrieveHostTest() {
        var testDomain = testDomains.get(0);
        when(hostPort.findById(1L)).thenReturn(Optional.of(testDomain));

        Host host = hostService.retrieveHostById(new HostIds(1L, 1L));
        Assertions.assertThat(host)
            .hasFieldOrPropertyWithValue("hostname", host.getHostname())
            .hasFieldOrPropertyWithValue("summary", host.getSummary())
            .hasFieldOrPropertyWithValue("description", host.getDescription())
            .hasFieldOrPropertyWithValue("path", host.getPath())
            .hasFieldOrPropertyWithValue("publish", host.isPublish());
    }

    @UnitTest
    public void retrieveHostsTest() {
        var pageable = PageRequest.of(1, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        var retrieveHostRequest = RetrieveHostRequest
            .builder()
            .build();

        Collections.reverse(testDomains);

        var page = new PageImpl<>(testDomains, pageable, testDomains.size());

        when(hostPort.findAll(any())).thenReturn(page);

        var hosts = hostService.retrieveHosts(retrieveHostRequest.toDomain(1L, pageable));
        Assertions.assertThat(hosts)
            .hasSize(testDomains.size());

        hosts.stream()
            .forEachOrdered(host -> Assertions.assertThat(host)
                .hasFieldOrProperty("hostname")
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

    @UnitTest
    public void deleteHostTest() {
        when(hostPort.findById(1L)).thenReturn(Optional.empty());
        hostPort.deleteById(1L);

        var host = hostPort.findById(1L);
        Assertions.assertThat(host)
            .isEmpty();
    }
}
