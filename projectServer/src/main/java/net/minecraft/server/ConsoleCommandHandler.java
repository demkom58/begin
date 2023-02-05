package net.minecraft.server;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.world.WorldServer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

public class ConsoleCommandHandler {
    private static Logger logger = Logger.getLogger("Minecraft");
    private final DecimalFormat format;
    private final MinecraftServer server;

    public ConsoleCommandHandler(MinecraftServer var1) {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        format = new DecimalFormat(".##", formatSymbols);

        this.server = var1;
    }

    public void handleCommand(ServerCommand command) {
        String commandName = command.command;
        ICommandListener listener = command.commandListener;
        String username = listener.getUsername();
        ServerConfigurationManager configManager = this.server.configManager;
        final String cmd = commandName.toLowerCase();

        if (!cmd.startsWith("help") && !cmd.startsWith("?")) {
            if (cmd.startsWith("tps")) {
                String tps1 = format.format(server.tps1.getAverage());
                String tps5 = format.format(server.tps5.getAverage());
                String tps15 = format.format(server.tps15.getAverage());
                listener.log("TPS from last 15m, 5m, 1m: " + tps15 + ", " + tps5 + ", " + tps1);
            } else if (cmd.startsWith("list")) {
                listener.log("Connected players: " + configManager.getPlayerList());
            } else if (cmd.startsWith("stop")) {
                this.sendNoticeToOps(username, "Stopping the server..");
                this.server.initiateShutdown();
            } else if (cmd.startsWith("save-all")) {
                this.sendNoticeToOps(username, "Forcing save..");
                if (configManager != null) {
                    configManager.savePlayerStates();
                }

                for (int var6 = 0; var6 < this.server.worldServers.length; ++var6) {
                    WorldServer var7 = this.server.worldServers[var6];
                    var7.saveWorld(true, null);
                }

                this.sendNoticeToOps(username, "Save complete.");
            } else if (cmd.startsWith("save-off")) {
                this.sendNoticeToOps(username, "Disabling level saving..");

                for (int i = 0; i < this.server.worldServers.length; ++i) {
                    WorldServer var30 = this.server.worldServers[i];
                    var30.levelSaving = true;
                }
            } else if (cmd.startsWith("save-on")) {
                this.sendNoticeToOps(username, "Enabling level saving..");

                for (int var18 = 0; var18 < this.server.worldServers.length; ++var18) {
                    WorldServer var31 = this.server.worldServers[var18];
                    var31.levelSaving = false;
                }
            } else if (cmd.startsWith("op ")) {
                String var19 = commandName.substring(commandName.indexOf(" ")).trim();
                configManager.opPlayer(var19);
                this.sendNoticeToOps(username, "Opping " + var19);
                configManager.sendChatMessageToPlayer(var19, "\u00a7eYou are now op!");
            } else if (cmd.startsWith("deop ")) {
                String var20 = commandName.substring(commandName.indexOf(" ")).trim();
                configManager.deopPlayer(var20);
                configManager.sendChatMessageToPlayer(var20, "\u00a7eYou are no longer op!");
                this.sendNoticeToOps(username, "De-opping " + var20);
            } else if (cmd.startsWith("ban-ip ")) {
                String var21 = commandName.substring(commandName.indexOf(" ")).trim();
                configManager.banIP(var21);
                this.sendNoticeToOps(username, "Banning ip " + var21);
            } else if (cmd.startsWith("pardon-ip ")) {
                String var22 = commandName.substring(commandName.indexOf(" ")).trim();
                configManager.pardonIP(var22);
                this.sendNoticeToOps(username, "Pardoning ip " + var22);
            } else if (cmd.startsWith("ban ")) {
                String var23 = commandName.substring(commandName.indexOf(" ")).trim();
                configManager.banPlayer(var23);
                this.sendNoticeToOps(username, "Banning " + var23);
                EntityPlayerMP var32 = configManager.getPlayerEntity(var23);
                if (var32 != null) {
                    var32.playerNetServerHandler.kickPlayer("Banned by admin");
                }
            } else if (cmd.startsWith("pardon ")) {
                String var24 = commandName.substring(commandName.indexOf(" ")).trim();
                configManager.pardonPlayer(var24);
                this.sendNoticeToOps(username, "Pardoning " + var24);
            } else if (cmd.startsWith("kick ")) {
                String var25 = commandName.substring(commandName.indexOf(" ")).trim();
                EntityPlayerMP var33 = null;

                for (int var8 = 0; var8 < configManager.playerEntities.size(); ++var8) {
                    EntityPlayerMP var9 = configManager.playerEntities.get(var8);
                    if (var9.username.equalsIgnoreCase(var25)) {
                        var33 = var9;
                    }
                }

                if (var33 != null) {
                    var33.playerNetServerHandler.kickPlayer("Kicked by admin");
                    this.sendNoticeToOps(username, "Kicking " + var33.username);
                } else {
                    listener.log("Can't find user " + var25 + ". No kick.");
                }
            } else if (cmd.startsWith("tp ")) {
                String[] var26 = commandName.split(" ");
                if (var26.length == 3) {
                    EntityPlayerMP var34 = configManager.getPlayerEntity(var26[1]);
                    EntityPlayerMP var37 = configManager.getPlayerEntity(var26[2]);
                    if (var34 == null) {
                        listener.log("Can't find user " + var26[1] + ". No tp.");
                    } else if (var37 == null) {
                        listener.log("Can't find user " + var26[2] + ". No tp.");
                    } else if (var34.dimension != var37.dimension) {
                        listener.log("User " + var26[1] + " and " + var26[2] + " are in different dimensions. No tp.");
                    } else {
                        var34.playerNetServerHandler.teleportTo(var37.posX, var37.posY, var37.posZ, var37.rotationYaw, var37.rotationPitch);
                        this.sendNoticeToOps(username, "Teleporting " + var26[1] + " to " + var26[2] + ".");
                    }
                } else {
                    listener.log("Syntax error, please provice a source and a target.");
                }
            } else if (cmd.startsWith("give ")) {
                String[] var27 = commandName.split(" ");
                if (var27.length != 3 && var27.length != 4) {
                    return;
                }

                String var35 = var27[1];
                EntityPlayerMP var38 = configManager.getPlayerEntity(var35);
                if (var38 != null) {
                    try {
                        int var40 = Integer.parseInt(var27[2]);
                        if (Item.ITEMS_LIST[var40] != null) {
                            this.sendNoticeToOps(username, "Giving " + var38.username + " some " + var40);
                            int var10 = 1;
                            if (var27.length > 3) {
                                var10 = this.tryParse(var27[3], 1);
                            }

                            if (var10 < 1) {
                                var10 = 1;
                            }

                            if (var10 > 64) {
                                var10 = 64;
                            }

                            var38.dropPlayerItem(new ItemStack(var40, var10, 0));
                        } else {
                            listener.log("There's no item with id " + var40);
                        }
                    } catch (NumberFormatException e) {
                        listener.log("There's no item with id " + var27[2]);
                    }
                } else {
                    listener.log("Can't find user " + var35);
                }
            } else if (cmd.startsWith("time ")) {
                String[] var28 = commandName.split(" ");
                if (var28.length != 3) {
                    return;
                }

                String var36 = var28[1];

                try {
                    int var39 = Integer.parseInt(var28[2]);
                    if ("add".equalsIgnoreCase(var36)) {
                        for (int var41 = 0; var41 < this.server.worldServers.length; ++var41) {
                            WorldServer var43 = this.server.worldServers[var41];
                            var43.setTime(var43.getWorldTime() + (long) var39);
                        }

                        this.sendNoticeToOps(username, "Added " + var39 + " to time");
                    } else if ("set".equalsIgnoreCase(var36)) {
                        for (int var42 = 0; var42 < this.server.worldServers.length; ++var42) {
                            WorldServer var44 = this.server.worldServers[var42];
                            var44.setTime(var39);
                        }

                        this.sendNoticeToOps(username, "Set time to " + var39);
                    } else {
                        listener.log("Unknown method, use either \"add\" or \"set\"");
                    }
                } catch (NumberFormatException e) {
                    listener.log("Unable to convert time value, " + var28[2]);
                }
            } else if (cmd.startsWith("say ")) {
                commandName = commandName.substring(commandName.indexOf(" ")).trim();
                logger.info("[" + username + "] " + commandName);
                configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7d[Server] " + commandName));
            } else if (cmd.startsWith("tell ")) {
                String[] var29 = commandName.split(" ");
                if (var29.length >= 3) {
                    commandName = commandName.substring(commandName.indexOf(" ")).trim();
                    commandName = commandName.substring(commandName.indexOf(" ")).trim();
                    logger.info("[" + username + "->" + var29[1] + "] " + commandName);
                    commandName = "\u00a77" + username + " whispers " + commandName;
                    logger.info(commandName);
                    if (!configManager.sendPacketToPlayer(var29[1], new Packet3Chat(commandName))) {
                        listener.log("There's no player by that name online.");
                    }
                }
            } else if (cmd.startsWith("whitelist ")) {
                this.handleWhitelist(username, commandName, listener);
            } else {
                logger.info("Unknown console command. Type \"help\" for help.");
            }
        } else {
            this.printHelp(listener);
        }

    }

    private void handleWhitelist(String var1, String var2, ICommandListener listener) {
        String[] var4 = var2.split(" ");
        if (var4.length >= 2) {
            String var5 = var4[1].toLowerCase();
            if ("on".equals(var5)) {
                this.sendNoticeToOps(var1, "Turned on white-listing");
                this.server.propertyManagerObj.setProperty("white-list", true);
            } else if ("off".equals(var5)) {
                this.sendNoticeToOps(var1, "Turned off white-listing");
                this.server.propertyManagerObj.setProperty("white-list", false);
            } else if ("list".equals(var5)) {
                Set<String> var6 = this.server.configManager.getWhiteListedIPs();
                StringBuilder var7 = new StringBuilder();

                for (String var9 : var6) {
                    var7.append(var9).append(" ");
                }

                listener.log("White-listed players: " + var7);
            } else if ("add".equals(var5) && var4.length == 3) {
                String var11 = var4[2].toLowerCase();
                this.server.configManager.addToWhiteList(var11);
                this.sendNoticeToOps(var1, "Added " + var11 + " to white-list");
            } else if ("remove".equals(var5) && var4.length == 3) {
                String var10 = var4[2].toLowerCase();
                this.server.configManager.removeFromWhiteList(var10);
                this.sendNoticeToOps(var1, "Removed " + var10 + " from white-list");
            } else if ("reload".equals(var5)) {
                this.server.configManager.reloadWhiteList();
                this.sendNoticeToOps(var1, "Reloaded white-list from file");
            }

        }
    }

    private void printHelp(ICommandListener listener) {
        listener.log("To run the server without a gui, start it like this:");
        listener.log("   java -Xmx1024M -Xms1024M -jar server.jar nogui");
        listener.log("Console commands:");
        listener.log("   help  or  ?               shows this message");
        listener.log("   kick <player>             removes a player from the server");
        listener.log("   ban <player>              bans a player from the server");
        listener.log("   pardon <player>           pardons a banned player so that they can connect again");
        listener.log("   ban-ip <ip>               bans an IP address from the server");
        listener.log("   pardon-ip <ip>            pardons a banned IP address so that they can connect again");
        listener.log("   op <player>               turns a player into an op");
        listener.log("   deop <player>             removes op status from a player");
        listener.log("   tp <player1> <player2>    moves one player to the same location as another player");
        listener.log("   give <player> <id> [num]  gives a player a resource");
        listener.log("   tell <player> <message>   sends a private message to a player");
        listener.log("   stop                      gracefully stops the server");
        listener.log("   save-all                  forces a server-wide level save");
        listener.log("   save-off                  disables terrain saving (useful for backup scripts)");
        listener.log("   save-on                   re-enables terrain saving");
        listener.log("   list                      lists all currently connected players");
        listener.log("   say <message>             broadcasts a message to all players");
        listener.log("   time <add|set> <amount>   adds to or sets the world time (0-24000)");
        listener.log("   tps                       shows near tps history");
        listener.log("   timings                   shows multiple sub-commands that help to profile server.");
    }

    private void sendNoticeToOps(String var1, String var2) {
        String var3 = var1 + ": " + var2;
        this.server.configManager.sendChatMessageToAllOps("\u00a77(" + var3 + ")");
        logger.info(var3);
    }

    private int tryParse(String var1, int var2) {
        try {
            return Integer.parseInt(var1);
        } catch (NumberFormatException e) {
            return var2;
        }
    }
}
