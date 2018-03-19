package org.ovirt.engine.core.common.action;

import org.ovirt.engine.core.common.businessentities.Snapshot;
import org.ovirt.engine.core.compat.Guid;

public class RemoveMemoryVolumesParameters extends ActionParametersBase {

    private Snapshot snapshot;
    private Guid vmId;
    /** In the general case, we remove the memory volumes only if there is only
     *  one snapshot in DB that uses it because we remove the memory before
     *  removing the snapshots from the DB. But in some cases we first remove
     *  the snapshot from the DB and only then remove its memory and in that
     *  case we should remove the memory only if no other snapshot uses it */
    private boolean removeOnlyIfNotUsedAtAll;

    private boolean forceRemove;

    public RemoveMemoryVolumesParameters(Snapshot snapshot, Guid vmId) {
        this(snapshot, vmId, false);
    }

    public RemoveMemoryVolumesParameters(Snapshot snapshot, Guid vmId, boolean forceRemove) {
        this.snapshot = snapshot;
        this.vmId = vmId;
        this.forceRemove = forceRemove;
    }

    public RemoveMemoryVolumesParameters() {
        this.vmId = Guid.Empty;
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public Guid getVmId() {
        return vmId;
    }

    public void setVmId(Guid vmId) {
        this.vmId = vmId;
    }

    public boolean isRemoveOnlyIfNotUsedAtAll() {
        return removeOnlyIfNotUsedAtAll;
    }

    public void setRemoveOnlyIfNotUsedAtAll(boolean removeOnlyIfNotUsedAtAll) {
        this.removeOnlyIfNotUsedAtAll = removeOnlyIfNotUsedAtAll;
    }

    public boolean isForceRemove() {
        return forceRemove;
    }

    public void setForceRemove(boolean forceRemove) {
        this.forceRemove = forceRemove;
    }
}
