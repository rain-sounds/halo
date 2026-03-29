package com.hippo.journal;

import com.hippo.journal.extension.JournalCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import run.halo.app.extension.SchemeManager;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;

/**
 * <p>Plugin main class to manage the lifecycle of the plugin.</p>
 * <p>This class must be public and have a public constructor.</p>
 * <p>Only one main class extending {@link BasePlugin} is allowed per plugin.</p>
 *
 * @author lee
 * @since 1.0.0
 */
@Component
public class JournalPlugin extends BasePlugin {

    @Autowired
    private SchemeManager schemeManager;

    public JournalPlugin(PluginContext pluginContext) {
        super(pluginContext);
    }

    @Override
    public void start() {
        schemeManager.register(JournalCategory.class);
        System.out.println("插件启动成功！");
    }

    @Override
    public void stop() {
        System.out.println("插件停止！");
    }
}
