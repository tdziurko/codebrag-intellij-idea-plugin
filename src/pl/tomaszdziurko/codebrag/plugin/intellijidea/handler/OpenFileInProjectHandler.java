package pl.tomaszdziurko.codebrag.plugin.intellijidea.handler;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.PsiShortNamesCache;

public class OpenFileInProjectHandler {

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
                                        System.out.println("Handling file " + fileName);
                                        PsiFile[] foundFiles = PsiShortNamesCache.getInstance(project).getFilesByName(fileName);
                                        if (foundFiles.length == 0) {
                                            System.out.println("No file with name " + fileName + " found");
                                            return;
                                        }
                                        PsiFile foundFile = foundFiles[0];
                                        System.out.println("Found file " + foundFile.getName());
                                        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, foundFile.getVirtualFile());
                                        descriptor.navigate(true);
                                    }
                                });
                            }
                });



//        ProjectManager.getInstance().

//        http://stackoverflow.com/questions/18725340/create-a-background-task-in-intellij-plugin
//        System.out.println("Found file " + foundFiles[0].getContainingDirectory().getName());
    }
}
