package com.demkom58.timings;

import com.google.common.collect.Sets;
import net.minecraft.Entity;
import net.minecraft.Material;
import net.minecraft.TileEntity;
import net.minecraft.server.MinecraftServer;
import org.json.simple.JSONValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import static com.demkom58.timings.TimingsManager.HISTORY;
import static co.aikar.util.JSONUtil.appendObjectData;
import static co.aikar.util.JSONUtil.createObject;
import static co.aikar.util.JSONUtil.pair;
import static co.aikar.util.JSONUtil.toArray;
import static co.aikar.util.JSONUtil.toArrayMapper;
import static co.aikar.util.JSONUtil.toObjectMapper;

@SuppressWarnings({"rawtypes", "SuppressionAnnotation"})
class TimingsExport extends Thread {
    private static final long REPORT_DIFF = 60000; // 180000
    private static final long WAIT_TO_REPORT = 1; // 180000

    private final Map out;
    private final TimingHistory[] history;
    private static long lastReport = 0;
    public static boolean report = false;

    private TimingsExport(Map out, TimingHistory[] history) {
        super("Timings paste thread");
        this.out = out;
        this.history = history;
    }

    /**
     * Checks if any pending reports are being requested, and builds one if needed.
     */
    static void reportTimings() {
        if (!report) return;
        report = false;

        final Logger logger = MinecraftServer.LOGGER;
        long now = System.currentTimeMillis();
        final long lastReportDiff = now - lastReport;
        if (lastReportDiff < REPORT_DIFF) {
            logger.warning("Please wait at least 1 minute in between Timings reports. (" + (int)((REPORT_DIFF - lastReportDiff) / 1000) + " seconds)");
            return;
        }
        final long lastStartDiff = now - TimingsManager.timingStart;
        if (lastStartDiff < WAIT_TO_REPORT) {
            logger.warning("Please wait at least 3 minutes before generating a Timings report. Unlike Timings v1, v2 benefits from longer timings and is not as useful with short timings. (" + (int)((180000 - lastStartDiff) / 1000) + " seconds)");
            return;
        }
        logger.info("Preparing Timings Report...");
        lastReport = now;
        Map parent = createObject(
            // Get some basic system details about the server
            pair("version", "Beta 1.7"),
            pair("maxplayers", MinecraftServer.SERVER.configManager.maxPlayers),
            pair("start", TimingsManager.timingStart / 1000),
            pair("end", System.currentTimeMillis() / 1000),
            pair("sampletime", (System.currentTimeMillis() - TimingsManager.timingStart) / 1000)
        );
        if (!TimingsManager.privacy) {
            appendObjectData(parent,
                pair("server", "Begin Server"),
                pair("motd", "Begin Server Motd"),
                pair("online-mode", MinecraftServer.SERVER.onlineMode),
                pair("icon", "no icon")
            );
        }

        final Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

        parent.put("system", createObject(
                pair("timingcost", getCost()),
                pair("name", System.getProperty("os.name")),
                pair("version", System.getProperty("os.version")),
                pair("jvmversion", System.getProperty("java.version")),
                pair("arch", System.getProperty("os.arch")),
                pair("maxmem", runtime.maxMemory()),
                pair("cpu", runtime.availableProcessors()),
                pair("runtime", ManagementFactory.getRuntimeMXBean().getUptime()),
                pair("flags", String.join(" ", runtimeBean.getInputArguments())),
                pair("gc", toObjectMapper(ManagementFactory.getGarbageCollectorMXBeans(), input -> pair(input.getName(), toArray(input.getCollectionCount(), input.getCollectionTime()))))
            )
        );

        Set<Material> tileEntityTypeSet = Sets.newHashSet();
        Set<Entity> entityTypeSet = Sets.newHashSet();

        int size = HISTORY.size();
        TimingHistory[] history = new TimingHistory[size + 1];
        int i = 0;
        for (TimingHistory timingHistory : HISTORY) {
            tileEntityTypeSet.addAll(timingHistory.tileEntityTypeSet);
            history[i++] = timingHistory;
        }

        history[i] = new TimingHistory(); // Current snapshot
        tileEntityTypeSet.addAll(history[i].tileEntityTypeSet);

        Map handlers = createObject();
        for (TimingIdentifier.TimingGroup group : TimingIdentifier.GROUP_MAP.values()) {
            for (TimingHandler id : group.handlers) {
                if (!id.isTimed() && !id.isSpecial()) {
                    continue;
                }
                handlers.put(id.id, toArray(
                    group.id,
                    id.name
                ));
            }
        }

        parent.put("idmap", createObject(
            pair("groups", toObjectMapper(
                TimingIdentifier.GROUP_MAP.values(), group -> pair(group.id, group.name))),
            pair("handlers", handlers),
            pair("worlds", toObjectMapper(TimingHistory.worldMap.entrySet(), input -> pair(input.getValue(), input.getKey()))),
            pair("tileentity", toObjectMapper(tileEntityTypeSet, input -> pair(input.toString(), input.toString()))),
            pair("entity", toObjectMapper(entityTypeSet, input -> pair(input.getClass().getName(), input.getClass().getSimpleName())))
        ));

        parent.put("plugins", "{}");
        parent.put("config", createObject(pair("spigot", "{}"), pair("bukkit", "{}"), pair("paper", "{}")));

        new TimingsExport(parent, history).start();
    }

    static long getCost() {
        // Benchmark the users System.nanotime() for cost basis
        int passes = 100;
        TimingHandler SAMPLER1 = Timings.ofSafe("Timings Sampler 1");
        TimingHandler SAMPLER2 = Timings.ofSafe("Timings Sampler 2");
        TimingHandler SAMPLER3 = Timings.ofSafe("Timings Sampler 3");
        TimingHandler SAMPLER4 = Timings.ofSafe("Timings Sampler 4");
        TimingHandler SAMPLER5 = Timings.ofSafe("Timings Sampler 5");
        TimingHandler SAMPLER6 = Timings.ofSafe("Timings Sampler 6");

        long start = System.nanoTime();
        for (int i = 0; i < passes; i++) {
            SAMPLER1.startTiming();
            SAMPLER2.startTiming();
            SAMPLER3.startTiming();
            SAMPLER3.stopTiming();
            SAMPLER4.startTiming();
            SAMPLER5.startTiming();
            SAMPLER6.startTiming();
            SAMPLER6.stopTiming();
            SAMPLER5.stopTiming();
            SAMPLER4.stopTiming();
            SAMPLER2.stopTiming();
            SAMPLER1.stopTiming();
        }
        long timingsCost = (System.nanoTime() - start) / passes / 6;
        SAMPLER1.reset(true);
        SAMPLER2.reset(true);
        SAMPLER3.reset(true);
        SAMPLER4.reset(true);
        SAMPLER5.reset(true);
        SAMPLER6.reset(true);
        return timingsCost;
    }


    @Override
    public void run() {
        out.put("data", toArrayMapper(history, TimingHistory::export));


        String response = null;
        String timingsURL;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://timings.aikar.co/post").openConnection();
            con.setDoOutput(true);
            String hostName = "BrokenHost";
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch(Exception ignored) {}
            con.setRequestProperty("User-Agent", "Paper/" + "Begin Server Name" + "/" + hostName);
            con.setRequestMethod("POST");
            con.setInstanceFollowRedirects(false);

            OutputStream request = new GZIPOutputStream(con.getOutputStream()) {{
                this.def.setLevel(7);
            }};

            request.write(JSONValue.toJSONString(out).getBytes(StandardCharsets.UTF_8));
            request.close();

            response = getResponse(con);

            if (con.getResponseCode() != 302) {
                MinecraftServer.LOGGER.warning("Upload Error: " + con.getResponseCode() + ": " + con.getResponseMessage());
                MinecraftServer.LOGGER.warning("Check your logs for more information");
                if (response != null) {
                    MinecraftServer.LOGGER.log(Level.SEVERE, response);
                }
                return;
            }

            timingsURL = con.getHeaderField("Location");
            MinecraftServer.LOGGER.info("View Timings Report: " + timingsURL);

            if (response != null && !response.isEmpty()) {
                MinecraftServer.LOGGER.log(Level.INFO, "Timing Response: " + response);
            }
        } catch (IOException ex) {
            MinecraftServer.LOGGER.warning("Error uploading timings, check your logs for more information");
            if (response != null) {
                MinecraftServer.LOGGER.log(Level.SEVERE, response);
            }
            MinecraftServer.LOGGER.log(Level.SEVERE, "Could not paste timings", ex);
        }
    }

    private String getResponse(HttpURLConnection con) throws IOException {
        InputStream is = null;
        try {
            is = con.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            return bos.toString();

        } catch (IOException ex) {
            MinecraftServer.LOGGER.log(Level.WARNING, "Error uploading timings, check your logs for more information");
            MinecraftServer.LOGGER.log(Level.WARNING, con.getResponseMessage(), ex);
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
