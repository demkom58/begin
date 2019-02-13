package net.minecraft;

import net.minecraft.server.MinecraftServer;

import java.util.Set;
import java.util.logging.Logger;

public class ConsoleCommandHandler {
    private static Logger minecraftLogger = Logger.getLogger("Minecraft");
    private MinecraftServer minecraftServer;

    public ConsoleCommandHandler(MinecraftServer var1) {
        this.minecraftServer = var1;
    }

    public void handleCommand(ServerCommand var1) {
        String var2 = var1.command;
        ICommandListener var3 = var1.commandListener;
        String var4 = var3.getUsername();
        ServerConfigurationManager var5 = this.minecraftServer.configManager;
        if (!var2.toLowerCase().startsWith("help") && !var2.toLowerCase().startsWith("?")) {
            if (var2.toLowerCase().startsWith("list")) {
                var3.log("Connected players: " + var5.getPlayerList());
            } else if (var2.toLowerCase().startsWith("stop")) {
                this.sendNoticeToOps(var4, "Stopping the server..");
                this.minecraftServer.initiateShutdown();
            } else if (var2.toLowerCase().startsWith("save-all")) {
                this.sendNoticeToOps(var4, "Forcing save..");
                if (var5 != null) {
                    var5.savePlayerStates();
                }

                for (int var6 = 0; var6 < this.minecraftServer.worldMngr.length; ++var6) {
                    WorldServer var7 = this.minecraftServer.worldMngr[var6];
                    var7.saveWorld(true, null);
                }

                this.sendNoticeToOps(var4, "Save complete.");
            } else if (var2.toLowerCase().startsWith("save-off")) {
                this.sendNoticeToOps(var4, "Disabling level saving..");

                for (int var17 = 0; var17 < this.minecraftServer.worldMngr.length; ++var17) {
                    WorldServer var30 = this.minecraftServer.worldMngr[var17];
                    var30.levelSaving = true;
                }
            } else if (var2.toLowerCase().startsWith("save-on")) {
                this.sendNoticeToOps(var4, "Enabling level saving..");

                for (int var18 = 0; var18 < this.minecraftServer.worldMngr.length; ++var18) {
                    WorldServer var31 = this.minecraftServer.worldMngr[var18];
                    var31.levelSaving = false;
                }
            } else if (var2.toLowerCase().startsWith("op ")) {
                String var19 = var2.substring(var2.indexOf(" ")).trim();
                var5.opPlayer(var19);
                this.sendNoticeToOps(var4, "Opping " + var19);
                var5.sendChatMessageToPlayer(var19, "\u00a7eYou are now op!");
            } else if (var2.toLowerCase().startsWith("deop ")) {
                String var20 = var2.substring(var2.indexOf(" ")).trim();
                var5.deopPlayer(var20);
                var5.sendChatMessageToPlayer(var20, "\u00a7eYou are no longer op!");
                this.sendNoticeToOps(var4, "De-opping " + var20);
            } else if (var2.toLowerCase().startsWith("ban-ip ")) {
                String var21 = var2.substring(var2.indexOf(" ")).trim();
                var5.banIP(var21);
                this.sendNoticeToOps(var4, "Banning ip " + var21);
            } else if (var2.toLowerCase().startsWith("pardon-ip ")) {
                String var22 = var2.substring(var2.indexOf(" ")).trim();
                var5.pardonIP(var22);
                this.sendNoticeToOps(var4, "Pardoning ip " + var22);
            } else if (var2.toLowerCase().startsWith("ban ")) {
                String var23 = var2.substring(var2.indexOf(" ")).trim();
                var5.banPlayer(var23);
                this.sendNoticeToOps(var4, "Banning " + var23);
                EntityPlayerMP var32 = var5.getPlayerEntity(var23);
                if (var32 != null) {
                    var32.playerNetServerHandler.kickPlayer("Banned by admin");
                }
            } else if (var2.toLowerCase().startsWith("pardon ")) {
                String var24 = var2.substring(var2.indexOf(" ")).trim();
                var5.pardonPlayer(var24);
                this.sendNoticeToOps(var4, "Pardoning " + var24);
            } else if (var2.toLowerCase().startsWith("kick ")) {
                String var25 = var2.substring(var2.indexOf(" ")).trim();
                EntityPlayerMP var33 = null;

                for (int var8 = 0; var8 < var5.playerEntities.size(); ++var8) {
                    EntityPlayerMP var9 = var5.playerEntities.get(var8);
                    if (var9.username.equalsIgnoreCase(var25)) {
                        var33 = var9;
                    }
                }

                if (var33 != null) {
                    var33.playerNetServerHandler.kickPlayer("Kicked by admin");
                    this.sendNoticeToOps(var4, "Kicking " + var33.username);
                } else {
                    var3.log("Can't find user " + var25 + ". No kick.");
                }
            } else if (var2.toLowerCase().startsWith("tp ")) {
                String[] var26 = var2.split(" ");
                if (var26.length == 3) {
                    EntityPlayerMP var34 = var5.getPlayerEntity(var26[1]);
                    EntityPlayerMP var37 = var5.getPlayerEntity(var26[2]);
                    if (var34 == null) {
                        var3.log("Can't find user " + var26[1] + ". No tp.");
                    } else if (var37 == null) {
                        var3.log("Can't find user " + var26[2] + ". No tp.");
                    } else if (var34.dimension != var37.dimension) {
                        var3.log("User " + var26[1] + " and " + var26[2] + " are in different dimensions. No tp.");
                    } else {
                        var34.playerNetServerHandler.teleportTo(var37.posX, var37.posY, var37.posZ, var37.rotationYaw, var37.rotationPitch);
                        this.sendNoticeToOps(var4, "Teleporting " + var26[1] + " to " + var26[2] + ".");
                    }
                } else {
                    var3.log("Syntax error, please provice a source and a target.");
                }
            } else if (var2.toLowerCase().startsWith("give ")) {
                String[] var27 = var2.split(" ");
                if (var27.length != 3 && var27.length != 4) {
                    return;
                }

                String var35 = var27[1];
                EntityPlayerMP var38 = var5.getPlayerEntity(var35);
                if (var38 != null) {
                    try {
                        int var40 = Integer.parseInt(var27[2]);
                        if (Item.ITEMS_LIST[var40] != null) {
                            this.sendNoticeToOps(var4, "Giving " + var38.username + " some " + var40);
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
                            var3.log("There's no item with id " + var40);
                        }
                    } catch (NumberFormatException var11) {
                        var3.log("There's no item with id " + var27[2]);
                    }
                } else {
                    var3.log("Can't find user " + var35);
                }
            } else if (var2.toLowerCase().startsWith("time ")) {
                String[] var28 = var2.split(" ");
                if (var28.length != 3) {
                    return;
                }

                String var36 = var28[1];

                try {
                    int var39 = Integer.parseInt(var28[2]);
                    if ("add".equalsIgnoreCase(var36)) {
                        for (int var41 = 0; var41 < this.minecraftServer.worldMngr.length; ++var41) {
                            WorldServer var43 = this.minecraftServer.worldMngr[var41];
                            var43.func_32005_b(var43.getWorldTime() + (long) var39);
                        }

                        this.sendNoticeToOps(var4, "Added " + var39 + " to time");
                    } else if ("set".equalsIgnoreCase(var36)) {
                        for (int var42 = 0; var42 < this.minecraftServer.worldMngr.length; ++var42) {
                            WorldServer var44 = this.minecraftServer.worldMngr[var42];
                            var44.func_32005_b((long) var39);
                        }

                        this.sendNoticeToOps(var4, "Set time to " + var39);
                    } else {
                        var3.log("Unknown method, use either \"add\" or \"set\"");
                    }
                } catch (NumberFormatException var12) {
                    var3.log("Unable to convert time value, " + var28[2]);
                }
            } else if (var2.toLowerCase().startsWith("say ")) {
                var2 = var2.substring(var2.indexOf(" ")).trim();
                minecraftLogger.info("[" + var4 + "] " + var2);
                var5.sendPacketToAllPlayers(new Packet3Chat("\u00a7d[Server] " + var2));
            } else if (var2.toLowerCase().startsWith("tell ")) {
                String[] var29 = var2.split(" ");
                if (var29.length >= 3) {
                    var2 = var2.substring(var2.indexOf(" ")).trim();
                    var2 = var2.substring(var2.indexOf(" ")).trim();
                    minecraftLogger.info("[" + var4 + "->" + var29[1] + "] " + var2);
                    var2 = "\u00a77" + var4 + " whispers " + var2;
                    minecraftLogger.info(var2);
                    if (!var5.sendPacketToPlayer(var29[1], new Packet3Chat(var2))) {
                        var3.log("There's no player by that name online.");
                    }
                }
            } else if (var2.toLowerCase().startsWith("whitelist ")) {
                this.handleWhitelist(var4, var2, var3);
            } else {
                minecraftLogger.info("Unknown console command. Type \"help\" for help.");
            }
        } else {
            this.printHelp(var3);
        }

    }

    private void handleWhitelist(String var1, String var2, ICommandListener var3) {
        String[] var4 = var2.split(" ");
        if (var4.length >= 2) {
            String var5 = var4[1].toLowerCase();
            if ("on".equals(var5)) {
                this.sendNoticeToOps(var1, "Turned on white-listing");
                this.minecraftServer.propertyManagerObj.setProperty("white-list", true);
            } else if ("off".equals(var5)) {
                this.sendNoticeToOps(var1, "Turned off white-listing");
                this.minecraftServer.propertyManagerObj.setProperty("white-list", false);
            } else if ("list".equals(var5)) {
                Set<String> var6 = this.minecraftServer.configManager.getWhiteListedIPs();
                String var7 = "";

                for (String var9 : var6) {
                    var7 = var7 + var9 + " ";
                }

                var3.log("White-listed players: " + var7);
            } else if ("add".equals(var5) && var4.length == 3) {
                String var11 = var4[2].toLowerCase();
                this.minecraftServer.configManager.addToWhiteList(var11);
                this.sendNoticeToOps(var1, "Added " + var11 + " to white-list");
            } else if ("remove".equals(var5) && var4.length == 3) {
                String var10 = var4[2].toLowerCase();
                this.minecraftServer.configManager.removeFromWhiteList(var10);
                this.sendNoticeToOps(var1, "Removed " + var10 + " from white-list");
            } else if ("reload".equals(var5)) {
                this.minecraftServer.configManager.reloadWhiteList();
                this.sendNoticeToOps(var1, "Reloaded white-list from file");
            }

        }
    }

    private void printHelp(ICommandListener var1) {
        var1.log("To run the server without a gui, start it like this:");
        var1.log("   java -Xmx1024M -Xms1024M -jar minecraft_server.jar nogui");
        var1.log("Console commands:");
        var1.log("   help  or  ?               shows this message");
        var1.log("   kick <player>             removes a player from the server");
        var1.log("   ban <player>              bans a player from the server");
        var1.log("   pardon <player>           pardons a banned player so that they can connect again");
        var1.log("   ban-ip <ip>               bans an IP address from the server");
        var1.log("   pardon-ip <ip>            pardons a banned IP address so that they can connect again");
        var1.log("   op <player>               turns a player into an op");
        var1.log("   deop <player>             removes op status from a player");
        var1.log("   tp <player1> <player2>    moves one player to the same location as another player");
        var1.log("   give <player> <id> [num]  gives a player a resource");
        var1.log("   tell <player> <message>   sends a private message to a player");
        var1.log("   stop                      gracefully stops the server");
        var1.log("   save-all                  forces a server-wide level save");
        var1.log("   save-off                  disables terrain saving (useful for backup scripts)");
        var1.log("   save-on                   re-enables terrain saving");
        var1.log("   list                      lists all currently connected players");
        var1.log("   say <message>             broadcasts a message to all players");
        var1.log("   time <add|set> <amount>   adds to or sets the world time (0-24000)");
    }

    private void sendNoticeToOps(String var1, String var2) {
        String var3 = var1 + ": " + var2;
        this.minecraftServer.configManager.sendChatMessageToAllOps("\u00a77(" + var3 + ")");
        minecraftLogger.info(var3);
    }

    private int tryParse(String var1, int var2) {
        try {
            return Integer.parseInt(var1);
        } catch (NumberFormatException var4) {
            return var2;
        }
    }
}
