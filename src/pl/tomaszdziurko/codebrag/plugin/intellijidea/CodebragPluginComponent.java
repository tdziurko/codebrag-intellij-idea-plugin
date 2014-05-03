package pl.tomaszdziurko.codebrag.plugin.intellijidea;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import pl.tomaszdziurko.codebrag.plugin.intellijidea.handler.OpenFileInProjectHandler;
import pl.tomaszdziurko.codebrag.plugin.intellijidea.listener.CodebragListeningServer;

public class CodebragPluginComponent implements ProjectComponent {

    private Project project;

    public CodebragPluginComponent(Project project) {
        this.project = project;

        System.out.println("CodebragPluginIntegration constructor");
        CodebragListeningServer server = ServiceManager.getService(CodebragListeningServer.class);
        server.registerHandler(new OpenFileInProjectHandler(project));
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Codebrag Plugin Main Component";
    }
}
