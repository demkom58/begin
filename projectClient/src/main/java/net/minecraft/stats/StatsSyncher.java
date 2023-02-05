package net.minecraft.stats;

import net.minecraft.client.Session;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatsSyncher {
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private volatile boolean saving = false;

    private volatile Map<StatBase, Integer> stats = null;

    private final StatFileWriter fileWriter;
    private final File statUnsent;
    private final File stat;
    private final File statUnsentTmp;
    private final File statTmp;
    private final File statUnsentOld;
    private final File statOld;
    private final Session session;

    private int pushesToSave = 0;

    public StatsSyncher(Session session, StatFileWriter fileWriter, File statsRoot) {
        final String lowerName = session.username.toLowerCase();

        this.statUnsent = new File(statsRoot, "stats_" + lowerName + "_unsent.dat");
        this.stat = new File(statsRoot, "stats_" + lowerName + ".dat");

        this.statUnsentOld = new File(statsRoot, "stats_" + lowerName + "_unsent.old");
        this.statOld = new File(statsRoot, "stats_" + lowerName + ".old");

        this.statUnsentTmp = new File(statsRoot, "stats_" + lowerName + "_unsent.tmp");
        this.statTmp = new File(statsRoot, "stats_" + lowerName + ".tmp");

        this.fileWriter = fileWriter;
        this.session = session;

        if (this.statUnsent.exists()) {
            fileWriter.addStats(this.readStatsMap(this.statUnsent, this.statUnsentTmp, this.statUnsentOld));
        }

        this.syncStats();
    }

    private void syncStats() {
        if (this.saving) {
            throw new IllegalStateException("Can't get stats from server while StatsSyncher is busy!");
        }

        this.pushesToSave = 100;
        this.saving = true;

        executor.execute(() -> {
            try {
                if (stats != null) {
                    writeStats(stats, stat, statTmp, statOld);
                } else if (stat.exists()) {
                    stats = readStatsMap(stat, statTmp, statOld);
                    System.out.println("INITIAL SYNC: " + stats);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                saving = false;
            }
        });
    }

    public void pushStats() {
        if (this.pushesToSave > 0) {
            --this.pushesToSave;
        }

        if (this.stats != null) {
            this.fileWriter.addStats(this.stats);
            this.stats = null;
        }
    }

    private Map<StatBase, Integer> readStatsMap(File statFile, File statTmp, File statOld) {
        if (statFile.exists()) {
            return this.readStats(statFile);
        }

        if (statOld.exists()) {
            return this.readStats(statOld);
        }

        if (statTmp.exists()) {
            return this.readStats(statTmp);
        }

        return null;
    }

    private Map<StatBase, Integer> readStats(File file) {
        try {
            return StatFileWriter.fromJson(Files.readString(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void writeStats(Map<StatBase, Integer> map, File unsentFile, File tmpFile, File oldFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(tmpFile, false))) {
            writer.print(StatFileWriter.toJson(this.session.username, "local", map));
        }

        if (oldFile.exists()) {
            oldFile.delete();
        }

        if (unsentFile.exists()) {
            unsentFile.renameTo(oldFile);
        }

        tmpFile.renameTo(unsentFile);
    }

    public void writeStats(Map<StatBase, Integer> map) {
        if (this.saving) {
            throw new IllegalStateException("Can't save stats while StatsSyncher is busy!");
        }

        this.pushesToSave = 100;
        this.saving = true;

        executor.execute(() -> {
            try {
                writeStats(map, statUnsent, statUnsentTmp, statUnsentOld);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                saving = false;
            }
        });
    }

    public void tryToWrite(Map<StatBase, Integer> stats) {
        int tries = 30;

        while (this.saving) {
            --tries;
            if (tries <= 0) {
                break;
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.saving = true;

        try {
            this.writeStats(stats, this.statUnsent, this.statUnsentTmp, this.statUnsentOld);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.saving = false;
        }

    }

    public boolean shouldSave() {
        return this.pushesToSave <= 0 && !this.saving;
    }
}
