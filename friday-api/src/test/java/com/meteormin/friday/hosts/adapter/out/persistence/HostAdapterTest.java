package com.meteormin.friday.hosts.adapter.out.persistence;

import com.meteormin.friday.infrastructure.persistence.repositories.HostEntityRepository;
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
    private HostEntityRepository hostRepository;
}
