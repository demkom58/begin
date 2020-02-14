package net.potion.stats;

import net.potion.client.Session;

import java.io.*;
import java.util.Map;

public class StatsSyncher {
    private volatile boolean field_27438_a = false;
    private volatile Map<StatBase, Integer> field_27437_b = null;
    private volatile Map<StatBase, Integer> field_27436_c = null;
    private StatFileWriter fileWriter;

    private File statUnsent;
    private File stat;
    private File statUnsentTmp;
    private File statTmp;
    private File statUnsentOld;
    private File statOld;

    private Session session;
    private int field_27427_l = 0;
    private int field_27426_m = 0;

    public StatsSyncher(Session session, StatFileWriter fileWriter, File statsRoot) {
        this.statUnsent = new File(statsRoot, "stats_" + session.username.toLowerCase() + "_unsent.dat");
        this.stat = new File(statsRoot, "stats_" + session.username.toLowerCase() + ".dat");
        this.statUnsentOld = new File(statsRoot, "stats_" + session.username.toLowerCase() + "_unsent.old");
        this.statOld = new File(statsRoot, "stats_" + session.username.toLowerCase() + ".old");
        this.statUnsentTmp = new File(statsRoot, "stats_" + session.username.toLowerCase() + "_unsent.tmp");
        this.statTmp = new File(statsRoot, "stats_" + session.username.toLowerCase() + ".tmp");

        if (!session.username.toLowerCase().equals(session.username)) {
            this.moveFile(statsRoot, "stats_" + session.username + "_unsent.dat", this.statUnsent);
            this.moveFile(statsRoot, "stats_" + session.username + ".dat", this.stat);
            this.moveFile(statsRoot, "stats_" + session.username + "_unsent.old", this.statUnsentOld);
            this.moveFile(statsRoot, "stats_" + session.username + ".old", this.statOld);
            this.moveFile(statsRoot, "stats_" + session.username + "_unsent.tmp", this.statUnsentTmp);
            this.moveFile(statsRoot, "stats_" + session.username + ".tmp", this.statTmp);
        }

        this.fileWriter = fileWriter;
        this.session = session;

        if (this.statUnsent.exists())
            fileWriter.func_27179_a(this.readStats(this.statUnsent, this.statUnsentTmp, this.statUnsentOld));

        this.func_27418_a();
    }

    private void moveFile(File rootDirectory, String fileName, File newFile) {
        File file = new File(rootDirectory, fileName);
        if (file.exists() && !file.isDirectory() && !newFile.exists())
            file.renameTo(newFile);
    }

    private Map<StatBase, Integer> readStats(File statFile, File statTmp, File statOld) {
        if (statFile.exists())
            return this.readStatsMapFromFile(statFile);

        if (statOld.exists())
            return this.readStatsMapFromFile(statOld);

        return statTmp.exists() ? this.readStatsMapFromFile(statTmp) : null;
    }

    private Map<StatBase, Integer> readStatsMapFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();

            String bufferString;
            while ((bufferString = reader.readLine()) != null)
                builder.append(bufferString);

            return StatFileWriter.readStatMap(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void updateStatFile(Map<StatBase, Integer> map, File unsentFile, File tmpFile, File oldFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(tmpFile, false))) {
            writer.print(StatFileWriter.toJson(this.session.username, "local", map));
        }

        if (oldFile.exists())
            oldFile.delete();

        if (unsentFile.exists())
            unsentFile.renameTo(oldFile);

        tmpFile.renameTo(unsentFile);
    }

    public void func_27418_a() {
        if (this.field_27438_a)
            throw new IllegalStateException("Can't get stats from server while StatsSyncher is busy!");

        this.field_27427_l = 100;
        this.field_27438_a = true;

        new Thread(() -> {
            try {
                if (field_27437_b != null) {
                    updateStatFile(field_27437_b, stat, statTmp, statOld);
                } else if (stat.exists()) {
                    field_27437_b = readStats(stat, statTmp, statOld);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                field_27438_a = false;
            }
        }).start();
    }

    public void func_27424_a(Map<StatBase, Integer> map) {
        if (this.field_27438_a)
            throw new IllegalStateException("Can't save stats while StatsSyncher is busy!");

        this.field_27427_l = 100;
        this.field_27438_a = true;

        new Thread(() -> {
            try {
                updateStatFile(map, statUnsent, statUnsentTmp, statUnsentOld);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                field_27438_a = false;
            }
        }).start();
    }

    public void syncStatsFileWithMap(Map<StatBase, Integer> var1) {
        int var2 = 30;

        while (this.field_27438_a) {
            --var2;
            if (var2 <= 0) {
                break;
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.field_27438_a = true;

        try {
            this.updateStatFile(var1, this.statUnsent, this.statUnsentTmp, this.statUnsentOld);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.field_27438_a = false;
        }

    }

    public boolean func_27420_b() {
        return this.field_27427_l <= 0 && !this.field_27438_a && this.field_27436_c == null;
    }

    public void func_27425_c() {
        if (this.field_27427_l > 0) {
            --this.field_27427_l;
        }

        if (this.field_27426_m > 0) {
            --this.field_27426_m;
        }

        if (this.field_27436_c != null) {
            this.fileWriter.func_27187_c(this.field_27436_c);
            this.field_27436_c = null;
        }

        if (this.field_27437_b != null) {
            this.fileWriter.func_27180_b(this.field_27437_b);
            this.field_27437_b = null;
        }

    }
}
