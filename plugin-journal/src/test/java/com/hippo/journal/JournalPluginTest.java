package com.hippo.journal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import run.halo.app.extension.SchemeManager;
import run.halo.app.plugin.PluginContext;

@ExtendWith(MockitoExtension.class)
class JournalPluginTest {

    @Mock
    PluginContext context;

    @Mock
    SchemeManager schemeManager;

    @InjectMocks
    JournalPlugin plugin;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(plugin, "schemeManager", schemeManager);
    }

    @Test
    void contextLoads() {
        plugin.start();
        plugin.stop();
    }
}
