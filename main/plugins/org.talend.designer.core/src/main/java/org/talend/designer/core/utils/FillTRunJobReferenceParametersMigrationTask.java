// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * Created by bhe on Nov 22, 2019
 */
public class FillTRunJobReferenceParametersMigrationTask extends AbstractItemMigrationTask {

    @Override
    public List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.PROCESS);
        toReturn.add(ERepositoryObjectType.JOBLET);
        return toReturn;
    }

    @Override
    public final ExecutionResult execute(Item item) {
        final IProxyRepositoryFactory factory = RepositoryPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
        boolean modified = false;
        try {
            if (item instanceof ProcessItem) {
                ProcessItem processItem = (ProcessItem) item;
                modified = updateTRunJobNodes(processItem.getProcess());
            } else if (item instanceof JobletProcessItem) {
                JobletProcessItem jobletItem = (JobletProcessItem) item;
                modified = updateTRunJobNodes(jobletItem.getJobletProcess());
            }
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
            return ExecutionResult.FAILURE;
        }

        if (modified) {
            try {
                factory.save(item, true);
                return ExecutionResult.SUCCESS_NO_ALERT;
            } catch (Exception ex) {
                ExceptionHandler.process(ex);
                return ExecutionResult.FAILURE;
            }
        }
        return ExecutionResult.NOTHING_TO_DO;
    }

    protected boolean updateTRunJobNodes(ProcessType processType) throws Exception {
        boolean modified = false;
        for (Object nodeObject : processType.getNode()) {
            NodeType nodeType = (NodeType) nodeObject;
            if (DesignerUtilities.isTRunJobComponent(nodeType)) {
                for (Object paramObjectType : nodeType.getElementParameter()) {
                    ElementParameterType param = (ElementParameterType) paramObjectType;
                    if (param.getName()
                            .equals(EParameterName.PROCESS.getName() + ":" + EParameterName.PROCESS_TYPE_PROCESS.getName())
                            && param.getValue().isEmpty()) {
                        String label = getTRunJobProcessLabel(nodeType);
                        String id = getIdFormLabel(label);
                        if (id != null) {
                            param.setValue(id);
                            modified = true;
                        }
                        // need to break anyway
                        break;
                    }
                }
            }
        }
        return modified;
    }

    private String getTRunJobProcessLabel(NodeType nodeType) {
        if (nodeType == null || nodeType.getElementParameter() == null) {
            return null;
        }
        for (Object paramObjectType : nodeType.getElementParameter()) {
            ElementParameterType param = (ElementParameterType) paramObjectType;
            if (param.getName().equals(EParameterName.PROCESS.getName()) && !param.getValue().isEmpty()) {
                return param.getValue();
            }
        }

        return null;
    }

    private static String getIdFormLabel(final String label) {
        if (label == null || label.isEmpty()) {
            return null;
        }
        final IProxyRepositoryFactory proxyRepositoryFactory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        try {
            List<IRepositoryViewObject> allRepositoryObject = proxyRepositoryFactory.getAll(ERepositoryObjectType.PROCESS, true);
            for (IRepositoryViewObject repObject : allRepositoryObject) {
                Item item = repObject.getProperty().getItem();
                if (item != null && label.equals(item.getProperty().getLabel())) {
                    return item.getProperty().getId();
                }
            }
        } catch (PersistenceException e) {
            //
        }
        return null;
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2019, 11, 22, 12, 0, 0);
        return gc.getTime();
    }
}
