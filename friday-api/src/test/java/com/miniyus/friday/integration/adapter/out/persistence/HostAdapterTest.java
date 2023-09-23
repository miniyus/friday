package com.miniyus.friday.integration.adapter.out.persistence;

import com.miniyus.friday.adapter.out.persistence.HostAdapter;
import com.miniyus.friday.infrastructure.persistence.repositories.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
@Transactional
public class HostAdapterTest {
    @Autowired
    private HostAdapter hostAdapter;

    @Autowired
    private HostRepository hostRepository;
}
