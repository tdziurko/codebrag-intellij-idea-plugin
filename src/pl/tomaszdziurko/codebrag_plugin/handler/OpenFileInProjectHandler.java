package pl.tomaszdziurko.codebrag_plugin.handler;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.ui.awt.RelativePoint;

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
                                showBalloonPopup("Codebrag<br/>File opened successfully.<br/>", MessageType.INFO);
                            }
                        });
                    }
                }
        );
    }

    private void showBalloonPopup(String htmlText, MessageType messageType) {
        IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(project);

        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(htmlText, messageType, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getNorthEastOf(ideFrame.getComponent()), Balloon.Position.atRight);
    }
}
