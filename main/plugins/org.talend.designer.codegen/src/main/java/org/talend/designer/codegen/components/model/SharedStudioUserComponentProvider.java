package org.talend.designer.codegen.components.model;
//============================================================================
//
//Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
//This source code is available under agreement available at
//%InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
//You should have received a copy of the agreement
//along with this program; if not, write to Talend SA
//9 rue Pages 92150 Suresnes, France
//
//============================================================================

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.runtime.util.SharedStudioUtils;
import org.talend.designer.core.model.components.ComponentBundleToPath;

public class SharedStudioUserComponentProvider extends UserComponentsProvider implements SharedStudioSupportor{
    @Override
    public File getInstallationFolder() throws IOException {
		File componentFolder = SharedStudioUtils.getSharedStudioComponentFolder();
        IPath path = new Path(IComponentsFactory.EXTERNAL_COMPONENTS_INNER_FOLDER);
        path = path.append(ComponentUtilities.getExtFolder(getFolderName()));
        File installationFolder = new File (componentFolder, path.toOSString());
		return installationFolder;
    }
    

    public String getComponentsBundle() {
        return ComponentBundleToPath.SHARED_STUDIO_CUSTOME_COMPONENT_BUNDLE;
    }
}
