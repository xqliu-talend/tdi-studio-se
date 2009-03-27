// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Information;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preference.StatusPreferenceInitializer;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractEMFRepositoryFactory extends AbstractRepositoryFactory implements IRepositoryFactory {

    /**
     * Generates the next id for serializable. If no serializable returns 0.
     * 
     * @param project the project to scan
     * 
     * @return the next id for the project
     * @throws PersistenceException
     * @throws PersistenceException if processes cannot be retrieved
     */
    @Override
    public String getNextId() {
        return EcoreUtil.generateUUID();
    }

    public RootContainer<String, IRepositoryObject> getDocumentation(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.DOCUMENTATION, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataConnection(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_CONNECTIONS, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataSAPConnection(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_SAPCONNECTIONS, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataEBCDIC(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_EBCDIC, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataRules(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_RULES, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileDelimited(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_DELIMITED, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFilePositional(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_POSITIONAL, true);
    }

    public RootContainer<String, IRepositoryObject> getProcess(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.PROCESS, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getJoblets()
     */
    public RootContainer<String, IRepositoryObject> getJoblets(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.JOBLET, true);
    }

    public RootContainer<String, IRepositoryObject> getContext(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.CONTEXT, true);
    }

    public RootContainer<String, IRepositoryObject> getRoutine(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.ROUTINES, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataSQLPattern(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.SQLPATTERNS, true);
    }

    public RootContainer<String, IRepositoryObject> getSnippets(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.SNIPPETS, true);
    }

    public RootContainer<String, IRepositoryObject> getBusinessProcess(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.BUSINESS_PROCESS, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileRegexp(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_REGEXP, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileXml(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_XML, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileLdif(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_LDIF, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataFileExcel(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_FILE_EXCEL, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataSalesforceSchema(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataLDAPSchema(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_LDAP_SCHEMA, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataGenericSchema(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_GENERIC_SCHEMA, true);
    }

    public RootContainer<String, IRepositoryObject> getMetadataWSDLSchema(Project project) throws PersistenceException {
        return getObjectFromFolder(project, ERepositoryObjectType.METADATA_WSDL_SCHEMA, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getRecycleBinItems()
     */
    public List<IRepositoryObject> getRecycleBinItems(Project project) throws PersistenceException {
        ERepositoryObjectType types[] = { ERepositoryObjectType.DOCUMENTATION, ERepositoryObjectType.METADATA_CONNECTIONS,
                ERepositoryObjectType.METADATA_SAPCONNECTIONS, ERepositoryObjectType.SQLPATTERNS,
                ERepositoryObjectType.METADATA_FILE_DELIMITED, ERepositoryObjectType.METADATA_FILE_POSITIONAL,
                ERepositoryObjectType.PROCESS, ERepositoryObjectType.CONTEXT, ERepositoryObjectType.SNIPPETS,
                ERepositoryObjectType.ROUTINES, ERepositoryObjectType.BUSINESS_PROCESS,
                ERepositoryObjectType.METADATA_FILE_REGEXP, ERepositoryObjectType.METADATA_FILE_XML,
                ERepositoryObjectType.METADATA_FILE_LDIF, ERepositoryObjectType.METADATA_FILE_EXCEL,
                ERepositoryObjectType.METADATA_LDAP_SCHEMA, ERepositoryObjectType.METADATA_GENERIC_SCHEMA,
                ERepositoryObjectType.METADATA_WSDL_SCHEMA, ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA,
                ERepositoryObjectType.JOBLET, ERepositoryObjectType.METADATA_FILE_EBCDIC,
                ERepositoryObjectType.METADATA_FILE_RULES };

        List<IRepositoryObject> deletedItems = new ArrayList<IRepositoryObject>();
        for (int i = 0; i < types.length; i++) {
            RootContainer<String, IRepositoryObject> container = getObjectFromFolder(project, types[i], true);
            List<IRepositoryObject> repositoryObjects = container.getAbsoluteMembers().objects();
            for (IRepositoryObject object : repositoryObjects) {
                if (object.getProperty().getItem().getState().isDeleted()) {
                    deletedItems.add(object);
                }
            }
        }
        return deletedItems;
    }

    /**
     * DOC smallet Comment method "convert".
     * 
     * @param toReturn
     * @param serializableAllVersion
     */
    protected List<IRepositoryObject> convert(List<IRepositoryObject> serializableAllVersion) {
        List<IRepositoryObject> toReturn = new ArrayList<IRepositoryObject>();
        for (IRepositoryObject current : serializableAllVersion) {
            toReturn.add(current);
        }
        return toReturn;
    }

    protected List<IRepositoryObject> getSerializable(Project project, String id, boolean allVersion) throws PersistenceException {
        List<IRepositoryObject> toReturn = new ArrayList<IRepositoryObject>();

        ERepositoryObjectType[] repositoryObjectTypeList = new ERepositoryObjectType[] { ERepositoryObjectType.BUSINESS_PROCESS,
                ERepositoryObjectType.DOCUMENTATION, ERepositoryObjectType.METADATA_CONNECTIONS,
                ERepositoryObjectType.METADATA_SAPCONNECTIONS, ERepositoryObjectType.SQLPATTERNS,
                ERepositoryObjectType.METADATA_FILE_DELIMITED, ERepositoryObjectType.METADATA_FILE_POSITIONAL,
                ERepositoryObjectType.METADATA_FILE_REGEXP, ERepositoryObjectType.METADATA_FILE_XML,
                ERepositoryObjectType.METADATA_FILE_EXCEL, ERepositoryObjectType.METADATA_FILE_LDIF,
                ERepositoryObjectType.PROCESS, ERepositoryObjectType.ROUTINES, ERepositoryObjectType.CONTEXT,
                ERepositoryObjectType.SNIPPETS, ERepositoryObjectType.METADATA_LDAP_SCHEMA,
                ERepositoryObjectType.METADATA_GENERIC_SCHEMA, ERepositoryObjectType.METADATA_WSDL_SCHEMA,
                ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA, ERepositoryObjectType.JOBLET,
                ERepositoryObjectType.METADATA_FILE_EBCDIC, ERepositoryObjectType.METADATA_FILE_RULES };// feature6484
                                                                                                        // added
        for (ERepositoryObjectType repositoryObjectType : repositoryObjectTypeList) {
            Object folder = getFolder(project, repositoryObjectType);
            if (folder != null) {
                toReturn.addAll(getSerializableFromFolder(project, folder, id, repositoryObjectType, allVersion, true, true));
            }
        }
        return toReturn;
    }

    protected abstract Object getFolder(Project project, ERepositoryObjectType repositoryObjectType) throws PersistenceException;

    public List<IRepositoryObject> getAllVersion(Project project, String id) throws PersistenceException {
        List<IRepositoryObject> serializableAllVersion = getSerializable(project, id, true);
        return convert(serializableAllVersion);
    }

    public boolean isNameAvailable(Project project, Item item, String name) throws PersistenceException {
        if (name == null) {
            name = item.getProperty().getLabel();
        }

        if (item instanceof FolderItem) {
            FolderHelper folderHelper = getFolderHelper(project.getEmfProject());
            return !folderHelper.pathExists((FolderItem) item, name);
        }

        ERepositoryObjectType type = ERepositoryObjectType.getItemType(item);

        if (type == ERepositoryObjectType.METADATA_CON_TABLE) {
            return false;
        }
        boolean isSqlPattern = (type == ERepositoryObjectType.SQLPATTERNS);
        String path = null;
        if (item.getState() != null) {
            path = item.getState().getPath();
        }

        List<IRepositoryObject> list = getAll(project, type, true, false);

        for (IRepositoryObject current : list) {
            if (name.equalsIgnoreCase(current.getProperty().getLabel())
                    && item.getProperty().getId() != current.getProperty().getId()) {
                // To check SQLPattern in same path. see bug 0005038: unable to add a SQLPattern into repository.
                if (!isSqlPattern || current.getProperty().getItem().getState().getPath().equals(path)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected abstract List<IRepositoryObject> getSerializableFromFolder(Project project, Object folder, String id,
            ERepositoryObjectType type, boolean allVersion, boolean searchInChildren, boolean withDeleted)
            throws PersistenceException;

    protected abstract <K, T> RootContainer<K, T> getObjectFromFolder(Project project, ERepositoryObjectType type,
            boolean onlyLastVersion) throws PersistenceException;

    protected abstract <K, T> void addFolderMembers(Project project, ERepositoryObjectType type, Container<K, T> toReturn,
            Object objectFolder, boolean onlyLastVersion) throws PersistenceException;

    protected abstract FolderHelper getFolderHelper(org.talend.core.model.properties.Project emfProject);

    protected Item copyFromResource(Resource createResource) throws PersistenceException, BusinessException {
        return copyFromResource(createResource, true);
    }

    protected Item copyFromResource(Resource createResource, boolean changeLabelWithCopyPrefix) throws PersistenceException,
            BusinessException {
        Item newItem = (Item) EcoreUtil.getObjectByType(createResource.getContents(), PropertiesPackage.eINSTANCE.getItem());
        Property property = newItem.getProperty();
        property.setId(getNextId());
        property.setAuthor(getRepositoryContext().getUser());

        if (changeLabelWithCopyPrefix) {
            setPropNewName(property);
        }

        EcoreUtil.resolveAll(createResource);
        return newItem;
    }

    /**
     * DOC smallet Comment method "getCopiedLabel".
     * 
     * @param copiedProperty
     * @return
     * @throws PersistenceException
     * @throws BusinessException
     */
    private void setPropNewName(Property copiedProperty) throws PersistenceException, BusinessException {
        String originalLabel = copiedProperty.getLabel();
        String add1 = "Copy_of_"; //$NON-NLS-1$
        String initialTry = add1 + originalLabel;
        copiedProperty.setLabel(initialTry);
        if (isNameAvailable(getRepositoryContext().getProject(), copiedProperty.getItem(), null)) {
            return;
        } else {
            char j = 'a';
            while (!isNameAvailable(getRepositoryContext().getProject(), copiedProperty.getItem(), null)) {
                if (j > 'z') {
                    throw new BusinessException(Messages.getString("AbstractEMFRepositoryFactory.cannotGenerateItem")); //$NON-NLS-1$
                }
                String nextTry = initialTry + "_" + (j++) + ""; //$NON-NLS-1$ //$NON-NLS-2$
                copiedProperty.setLabel(nextTry);
            }
        }
    }

    protected void createSystemRoutines() throws PersistenceException {
        ILibrariesService service = CorePlugin.getDefault().getLibrariesService();
        Project project = getRepositoryContext().getProject();
        FolderHelper folderHelper = getFolderHelper(project.getEmfProject());

        List<URL> routines = service.getSystemRoutines();
        Path path = new Path(RepositoryConstants.SYSTEM_DIRECTORY);
        // will automatically set the children folders
        folderHelper.createFolder("code/routines/system"); //$NON-NLS-1$

        for (URL url : routines) {
            createRoutine(url, path);
        }
    }

    protected void createSystemSQLPatterns() throws PersistenceException {
        ILibrariesService service = CorePlugin.getDefault().getLibrariesService();
        Project project = getRepositoryContext().getProject();
        FolderHelper folderHelper = getFolderHelper(project.getEmfProject());
        // will automatically set the children folders
        folderHelper.createFolder("sqlPatterns/system"); //$NON-NLS-1$

        List<URL> routines = service.getSystemSQLPatterns();

        for (URL url : routines) {
            createSQLPattern(url);
        }
    }

    /**
     * DOC smallet Comment method "createRoutine".
     * 
     * @param url
     * @throws PersistenceException
     */
    private void createRoutine(URL url, IPath path) throws PersistenceException {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        InputStream stream = null;
        try {
            Property property = PropertiesFactory.eINSTANCE.createProperty();
            property.setId(getNextId());

            String[] fragments = url.toString().split("/"); //$NON-NLS-1$
            String label = fragments[fragments.length - 1];
            String[] tmp = label.split("\\."); //$NON-NLS-1$
            property.setLabel(tmp[0]);

            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();
            byteArray.setInnerContent(innerContent);

            RoutineItem routineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
            routineItem.setProperty(property);
            routineItem.setContent(byteArray);
            routineItem.setBuiltIn(true);
            if (!routineItem.getProperty().getLabel().equals(ITalendSynchronizer.TEMPLATE)) {
                create(getRepositoryContext().getProject(), routineItem, path);
            }
        } catch (IOException ioe) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new PersistenceException(ioe);
                }
            }
            throw new PersistenceException(ioe);
        }
    }

    private void createSQLPattern(URL url) throws PersistenceException {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        InputStream stream = null;
        try {
            Property property = PropertiesFactory.eINSTANCE.createProperty();
            property.setId(getNextId());

            String[] fragments = url.toString().split("/"); //$NON-NLS-1$
            String label = fragments[fragments.length - 1];
            String[] tmp = label.split("\\."); //$NON-NLS-1$

            Path relativePath = new Path(url.getFile());

            // for instance: categoryName is Teradata; fileName is
            // Loadfile.sqlpattern
            String fileName = relativePath.segment(relativePath.segmentCount() - 1);
            String categoryName = relativePath.segment(relativePath.segmentCount() - 2);

            tmp = fileName.split("\\."); //$NON-NLS-1$

            property.setLabel(tmp[0]);

            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();
            byteArray.setInnerContent(innerContent);

            SQLPatternItem sqlpatternItem = PropertiesFactory.eINSTANCE.createSQLPatternItem();
            sqlpatternItem.setProperty(property);
            sqlpatternItem.setEltName(categoryName);
            sqlpatternItem.setContent(byteArray);
            sqlpatternItem.setSystem(true);

            // set the item's relative path in the repository view
            IPath categoryPath = new Path(categoryName);
            IPath systemPath = categoryPath.append(RepositoryConstants.SYSTEM_DIRECTORY);
            IPath userPath = categoryPath.append(RepositoryConstants.USER_DEFINED);

            FolderHelper folderHelper = getFolderHelper(getRepositoryContext().getProject().getEmfProject());
            IPath parentPath = new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.SQLPATTERNS));
            if (folderHelper.getFolder(parentPath.append(categoryPath)) == null) {
                createFolder(getRepositoryContext().getProject(), ERepositoryObjectType.SQLPATTERNS, new Path(""), categoryPath //$NON-NLS-1$
                        .lastSegment());
            }
            if (folderHelper.getFolder(parentPath.append(systemPath)) == null) {
                createFolder(getRepositoryContext().getProject(), ERepositoryObjectType.SQLPATTERNS, categoryPath, systemPath
                        .lastSegment());
            }
            if (folderHelper.getFolder(parentPath.append(userPath)) == null) {
                createFolder(getRepositoryContext().getProject(), ERepositoryObjectType.SQLPATTERNS, categoryPath, userPath
                        .lastSegment());
            }
            create(getRepositoryContext().getProject(), sqlpatternItem, systemPath);

        } catch (IOException ioe) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new PersistenceException(ioe);
                }
            }
            throw new PersistenceException(ioe);
        }
    }

    public void logOnProject(Project project) throws LoginException, PersistenceException {
        new StatusPreferenceInitializer().initializeDefaultPreferences();
        String productVersion = RepositoryPlugin.getDefault().getBundle().getHeaders().get(
                org.osgi.framework.Constants.BUNDLE_VERSION).toString();

        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);

        String productBranding = brandingService.getFullProductName();

        project.getEmfProject().setProductVersion(productBranding + "-" + productVersion); //$NON-NLS-1$

        saveProject();
    }

    protected abstract void saveProject() throws PersistenceException;

    public List<ModuleNeeded> getModulesNeededForJobs() throws PersistenceException {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        IProxyRepositoryFactory repositoryFactory = CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
        List<IRepositoryObject> jobs = repositoryFactory.getAll(ERepositoryObjectType.PROCESS, true);
        for (IRepositoryObject cur : jobs) {
            if (repositoryFactory.getStatus(cur) != ERepositoryStatus.DELETED) {
                ProcessItem item = (ProcessItem) cur.getProperty().getItem();
                List<NodeType> nodes = item.getProcess().getNode();
                for (NodeType node : nodes) {
                    List<ElementParameterType> elementParameter = node.getElementParameter();
                    for (ElementParameterType elementParam : elementParameter) {
                        if (elementParam.getField().equals(EParameterFieldType.MODULE_LIST.getName())) {
                            String uniquename = ElementParameterParser.getUNIQUENAME(node);
                            ModuleNeeded toAdd = new ModuleNeeded(
                                    Messages.getString("AbstractEMFRepositoryFactory.job") + item.getProperty().getLabel(), //$NON-NLS-1$
                                    elementParam.getValue(),
                                    Messages.getString("AbstractEMFRepositoryFactory.requiredComponent") + uniquename + ".", true); //$NON-NLS-1$ //$NON-NLS-2$
                            importNeedsList.add(toAdd);
                        }
                    }
                }
            }
        }

        return importNeedsList;
    }

    public RootContainer<String, IRepositoryObject> getRoutineFromProject(Project project) throws PersistenceException {
        RootContainer<String, IRepositoryObject> toReturn = new RootContainer<String, IRepositoryObject>();
        ERepositoryObjectType type = ERepositoryObjectType.ROUTINES;

        IProject fsProject = ResourceModelUtils.getProject(project);

        IFolder objectFolder = ResourceUtils.getFolder(fsProject, ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.ROUTINES), true);

        addFolderMembers(project, type, toReturn, objectFolder, true);
        return toReturn;
    }

    public synchronized IRepositoryObject getLastVersion(Project project, String id) throws PersistenceException {
        List<IRepositoryObject> serializableAllVersion = getSerializable(project, id, false);

        if (serializableAllVersion.size() > 1) {
            throw new PersistenceException(Messages
                    .getString("AbstractEMFRepositoryFactory.presistenceException.onlyOneOccurenceAllowed")); //$NON-NLS-1$
        } else if (serializableAllVersion.size() == 1) {
            return serializableAllVersion.get(0);
        } else {
            return null;
        }
    }

    protected void computePropertyMaxInformationLevel(Property property) {
        EList<Information> informations = property.getInformations();
        InformationLevel maxLevel = null;
        for (Information information : informations) {
            int value = information.getLevel().getValue();
            if (maxLevel == null || value > maxLevel.getValue()) {
                maxLevel = information.getLevel();
            }
        }
        property.setMaxInformationLevel(maxLevel);
    }

    public Property getUptodateProperty(Project project, Property property) throws PersistenceException {
        List<IRepositoryObject> allVersion = getAllVersion(project, property.getId());
        for (IRepositoryObject repositoryObject : allVersion) {
            Property uptodateProperty = repositoryObject.getProperty();
            if (uptodateProperty.getVersion().equals(property.getVersion())) {
                return uptodateProperty;
            }
        }
        return null;
    }
}
