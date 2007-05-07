// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.sbi.engines.client.ui.actions;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.sbi.engines.client.i18n.Messages;
import org.talend.sbi.engines.client.ui.wizards.GenerateSpagoBIXML;
import org.talend.sbi.engines.client.ui.wizards.PublishOnSpagoExportWizard;

/**
 * Action used to export job scripts. <br/>
 * 
 * $Id: PublishOnSpagoAction.java 1 2007-04-26 11:25:00 cantoine
 * 
 */
public final class PublishOnSpagoAction extends AContextualAction {

    private static final String EXPORTJOBSCRIPTS = Messages.getString("PublishOnSpagoAction.actionLabel"); //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = true;
        List<RepositoryNode> nodes = (List<RepositoryNode>) selection.toList();
        for (RepositoryNode node : nodes) {
            if (node.getType() != ENodeType.REPOSITORY_ELEMENT
                    || node.getProperties(EProperties.CONTENT_TYPE) != ERepositoryObjectType.PROCESS) {
                canWork = false;
                break;
            }
        }
        setEnabled(canWork);
    }

    public boolean isVisible() {
        return isEnabled();
    }

    public PublishOnSpagoAction() {
        super();
        this.setText(EXPORTJOBSCRIPTS);
        this.setToolTipText(EXPORTJOBSCRIPTS);
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.EXPORT_ICON));
    }

    public void run() {
        PublishOnSpagoExportWizard publishWizard = new PublishOnSpagoExportWizard();
        IWorkbench workbench = this.getViewPart().getViewSite().getWorkbenchWindow().getWorkbench();
        publishWizard.setWindowTitle(EXPORTJOBSCRIPTS + " (SpagoBI)");
        publishWizard.init(workbench, (IStructuredSelection) this.getSelection());

        Shell activeShell = Display.getCurrent().getActiveShell();
        WizardDialog dialog = new WizardDialog(activeShell, publishWizard);
        dialog.open();
    }
}
