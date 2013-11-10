package org.ovirt.engine.core.common.businessentities;

import java.util.Date;
import java.util.HashMap;

import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ovirt.engine.core.common.businessentities.Disk.DiskStorageType;
import org.ovirt.engine.core.common.config.Config;
import org.ovirt.engine.core.common.config.ConfigValues;
import org.ovirt.engine.core.common.validation.annotation.ValidI18NName;
import org.ovirt.engine.core.common.validation.group.CreateEntity;
import org.ovirt.engine.core.common.validation.group.ImportClonedEntity;
import org.ovirt.engine.core.common.validation.group.UpdateEntity;
import org.ovirt.engine.core.compat.Guid;

public class VmTemplate extends VmBase implements BusinessEntityWithStatus<Guid, VmTemplateStatus>, InstanceType, ImageType {
    private static final long serialVersionUID = -5238366659716600486L;

    private int childCount;

    private VmTemplateStatus status;

    private String vdsGroupName;

    private Guid storagePoolId;

    private boolean trustedService;

    private String storagePoolName;

    @EditableField
    private HashMap<Guid, DiskImage> diskImageMap;

    private HashMap<Guid, DiskImage> diskTemplateMap;

    private double bootDiskSizeGB;

    private double actualDiskSize;

    private VmEntityType templateType;

    private ArchitectureType clusterArch;

    public VmTemplate() {
        setNiceLevel(0);
        setCpuShares(0);
        diskTemplateMap = new HashMap<Guid, DiskImage>();
        status = VmTemplateStatus.OK;
        diskImageMap = new HashMap<Guid, DiskImage>();
        templateType = VmEntityType.TEMPLATE;
    }

    @EditableField
    private boolean disabled;

    public VmTemplate(int childCount, Date creationDate, String description, int memSizeMb, String name,
            int numOfSockets, int cpuPerSocket, int osId, Guid vdsGroupId, Guid vmtGuid,
            int numOfMonitors, boolean singleQxlPci, int status, int usbPolicy, String timeZone, int niceLevel,
            int cpuShares, boolean failBack, BootSequence defaultBootSequence, VmType vmType,
            boolean smartcardEnabled, boolean deleteProtected, SsoMethod ssoMethod, Boolean tunnelMigration, String vncKeyboardLayout,
            int minAllocatedMem, boolean stateless, boolean runAndPause, Guid createdByUserId, VmEntityType templateType,
            boolean autoStartup, int priority, DisplayType defaultDisplayType, String initrdUrl, String kernelUrl,
            String kernelParams, Guid quotaId, Guid dedicatedVmForVds, MigrationSupport migrationSupport,
            boolean allowConsoleReconnect, String isoPath, Integer migrationDowntime) {
        super(name,
                vmtGuid,
                vdsGroupId,
                osId,
                creationDate,
                description,
                "",
                memSizeMb,
                numOfSockets,
                cpuPerSocket,
                numOfMonitors,
                singleQxlPci,
                timeZone,
                vmType,
                UsbPolicy.forValue(usbPolicy),
                failBack,
                defaultBootSequence,
                niceLevel,
                cpuShares,
                priority,
                autoStartup,
                stateless,
                isoPath,
                OriginType.valueOf(Config.<String> getValue(ConfigValues.OriginType)),
                kernelUrl,
                kernelParams,
                initrdUrl,
                quotaId,
                smartcardEnabled,
                deleteProtected,
                ssoMethod,
                tunnelMigration,
                vncKeyboardLayout,
                minAllocatedMem,
                runAndPause,
                createdByUserId,
                migrationSupport,
                allowConsoleReconnect,
                dedicatedVmForVds,
                defaultDisplayType,
                migrationDowntime,
                null);

        diskTemplateMap = new HashMap<Guid, DiskImage>();
        diskImageMap = new HashMap<Guid, DiskImage>();

        this.childCount = childCount;
        this.setStatus(VmTemplateStatus.forValue(status));
        setTemplateType(templateType);
    }

    public ArchitectureType getClusterArch() {
        return clusterArch;
    }

    public void setClusterArch(ArchitectureType clusterArch) {
        this.clusterArch = clusterArch;
    }

    public int getChildCount() {
        return this.childCount;
    }

    public void setChildCount(int value) {
        this.childCount = value;
    }

    @Override
    public VmTemplateStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(VmTemplateStatus value) {
        status = value;
    }

    public String getVdsGroupName() {
        return vdsGroupName;
    }

    public void setVdsGroupName(String value) {
        vdsGroupName = value;
    }

    public void setTrustedService(boolean trustedService) {
        this.trustedService = trustedService;
    }

    public boolean isTrustedService() {
        return trustedService;
    }

    public Guid getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(Guid value) {
        storagePoolId = value;
    }

    public String getStoragePoolName() {
        return storagePoolName;
    }

    public void setStoragePoolName(String value) {
        storagePoolName = value;
    }

    public double getSizeGB() {
        return bootDiskSizeGB;
    }

    public void setSizeGB(double value) {
        bootDiskSizeGB = value;
    }

    @JsonIgnore
    public HashMap<Guid, DiskImage> getDiskTemplateMap() {
        return diskTemplateMap;
    }

    @Override
    public Object getQueryableId() {
        return getId();
    }

    public double getActualDiskSize() {
        if (actualDiskSize == 0 && getDiskImageMap() != null) {
            for (Disk disk : getDiskImageMap().values()) {
                if (disk.getDiskStorageType() == DiskStorageType.IMAGE) {
                    actualDiskSize += ((DiskImage) disk).getActualSize();
                }
            }
        }
        return actualDiskSize;
    }

    /**
     * empty setters to fix CXF issue
     */
    public void setActualDiskSize(double actualDiskSize) {
        // Purposely empty
    }

    @JsonIgnore
    public HashMap<Guid, DiskImage> getDiskImageMap() {
        return diskImageMap;
    }

    public void setDiskImageMap(HashMap<Guid, DiskImage> value) {
        diskImageMap = value;
    }

    @Override
    public boolean equals(Object obj) {
        boolean returnValue = super.equals(obj);
        if (!returnValue && obj != null && obj instanceof VmTemplate) {
            returnValue = getId()
                    .equals(((VmTemplate) obj).getId());
        }
        return returnValue;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    @Size(min = 1, max = BusinessEntitiesDefinitions.VM_TEMPLATE_NAME_SIZE,
            message = "VALIDATION.VM_TEMPLATE.NAME.MAX",
            groups = { Default.class, ImportClonedEntity.class })
    @ValidI18NName(message = "ACTION_TYPE_FAILED_NAME_MAY_NOT_CONTAIN_SPECIAL_CHARS", groups = { CreateEntity.class,
            UpdateEntity.class, ImportClonedEntity.class })
    public String getName() {
        return super.getName();
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public VmEntityType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(VmEntityType templateType) {
        this.templateType = templateType;
    }
}
