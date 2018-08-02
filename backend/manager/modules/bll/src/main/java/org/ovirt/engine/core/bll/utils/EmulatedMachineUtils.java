package org.ovirt.engine.core.bll.utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.lang.StringUtils;
import org.ovirt.engine.core.common.businessentities.ChipsetType;
import org.ovirt.engine.core.common.businessentities.Cluster;
import org.ovirt.engine.core.common.businessentities.VmBase;
import org.ovirt.engine.core.common.config.Config;
import org.ovirt.engine.core.common.config.ConfigValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmulatedMachineUtils {

    private static final Logger log = LoggerFactory.getLogger(EmulatedMachineUtils.class);

    public static String getEffective(VmBase vmBase, Supplier<Cluster> clusterSupplier) {
        if (vmBase.getCustomEmulatedMachine() != null) {
            return vmBase.getCustomEmulatedMachine();
        }

        // The 'default' to be set
        Cluster cluster = clusterSupplier.get();
        String recentClusterDefault = cluster != null ? cluster.getEmulatedMachine() : "";
        if (vmBase.getCustomCompatibilityVersion() == null
                && chipsetMatchesEmulatedMachine(vmBase.getBiosType().getChipsetType(), recentClusterDefault)) {
            return recentClusterDefault;
        }

        String bestMatch = findBestMatchForEmulatedMachine(
                vmBase.getBiosType().getChipsetType(),
                recentClusterDefault,
                Config.getValue(
                        ConfigValues.ClusterEmulatedMachines,
                        vmBase.getCustomCompatibilityVersion().getValue()));
        log.info("Emulated machine '{}' selected since Custom Compatibility Version is set for '{}'", bestMatch, vmBase);
        return bestMatch;
    }

    protected static String findBestMatchForEmulatedMachine(
            ChipsetType chipsetType,
            String currentEmulatedMachine,
            List<String> candidateEmulatedMachines) {
        if (candidateEmulatedMachines.contains(currentEmulatedMachine)) {
            return currentEmulatedMachine;
        }
        return candidateEmulatedMachines
                .stream()
                .map(em -> replaceChipset(em, chipsetType))
                .max(Comparator.comparingInt(s -> StringUtils.indexOfDifference(currentEmulatedMachine, s)))
                .orElse(currentEmulatedMachine);
    }

    private static boolean chipsetMatchesEmulatedMachine(ChipsetType chipsetType, String emulatedMachine) {
        return chipsetType == ChipsetType.I440FX || chipsetType == ChipsetType.fromMachineType(emulatedMachine);
    }

    private static String replaceChipset(String emulatedMachine, ChipsetType chipsetType) {
        if (chipsetType != ChipsetType.Q35) {
            return emulatedMachine;
        }

        final String I440FX_CHIPSET_NAME = ChipsetType.I440FX.getChipsetName();
        final String Q35_CHIPSET_NAME = ChipsetType.Q35.getChipsetName();

        if (emulatedMachine.contains(I440FX_CHIPSET_NAME)) {
            return emulatedMachine.replace(I440FX_CHIPSET_NAME, Q35_CHIPSET_NAME);
        }
        return emulatedMachine.replace("pc-", "pc-" + Q35_CHIPSET_NAME + '-');
    }

}