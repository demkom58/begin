package net.minecraft.world.storage;

public class SaveFormatData implements Comparable<SaveFormatData> {
    private final String fileName;
    private final String displayName;
    private final long lastTimePlayed;
    private final long sizeOnDisk;
    private final boolean invalidVersion;

    public SaveFormatData(String fileName, String displayName, long lastTimePlayed, long sizeOnDisk, boolean invalidVersion) {
        this.fileName = fileName;
        this.displayName = displayName;
        this.lastTimePlayed = lastTimePlayed;
        this.sizeOnDisk = sizeOnDisk;
        this.invalidVersion = invalidVersion;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public long getSizeOnDisk() {
        return this.sizeOnDisk;
    }

    public boolean isInvalidVersion() {
        return this.invalidVersion;
    }

    public long getLastTimePlayed() {
        return this.lastTimePlayed;
    }

    public int compareTo(SaveFormatData comparator) {
        if (this.lastTimePlayed < comparator.lastTimePlayed)
            return 1;

        return this.lastTimePlayed > comparator.lastTimePlayed ? -1 : this.fileName.compareTo(comparator.fileName);
    }

}
