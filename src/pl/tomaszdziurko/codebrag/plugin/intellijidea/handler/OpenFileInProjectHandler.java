package pl.tomaszdziurko.codebrag.plugin.intellijidea.handler;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.PsiShortNamesCache;

public class OpenFileInProjectHandler {

    private final Logger log = Logger.getInstance(OpenFileInProjectHandler.class);

    private Project project;

    public OpenFileInProjectHandler(Project project) {
        this.project = project;
    }

    public void handle(final String fileName) {
        ApplicationManager.getApplication().invokeLater(
                new Runnable() {
                    public void run() {
                        ApplicationManager.getApplication().runReadAction(new Runnable() {
                            public void run() {
                                PsiFile[] foundFiles = PsiShortNamesCache.getInstance(project).getFilesByName(fileName);
                                if (foundFiles.length == 0) {
                                    log.info("No file with name " + fileName + " found");
                                    return;
                                }
                                if (foundFiles.length > 1) {
                                    log.warn("Found more than one file with name " + fileName);
                                }
                                PsiFile foundFile = foundFiles[0];
                                log.info("Found file " + foundFile.getName());
                                OpenFileDescriptor descriptor = new OpenFileDescriptor(project, foundFile.getVirtualFile());
                                descriptor.navigate(true);
                            }
                        });
                    }
                }
        );
    }
}
