package me.neznamy.tab.shared.features;

import me.neznamy.tab.shared.Configs;
import me.neznamy.tab.shared.ITabPlayer;
import me.neznamy.tab.shared.PacketAPI;
import me.neznamy.tab.shared.Shared;
import me.neznamy.tab.shared.packets.PacketPlayOutScoreboardObjective.EnumScoreboardHealthDisplay;

public class TabObjective implements SimpleFeature{

	private static final String ObjectiveName = "TAB-TabObjective";
	private static final int DisplaySlot = 0;
	
	public static String rawValue;
	private final String title = "ms";
	private EnumScoreboardHealthDisplay displayType;

	@Override
	public void load() {
		rawValue = Configs.config.getString("yellow-number-in-tablist", "%ping%");
		if (rawValue.equals("%health%")) {
			displayType = EnumScoreboardHealthDisplay.HEARTS;
		} else {
			displayType = EnumScoreboardHealthDisplay.INTEGER;
		}
		for (ITabPlayer p : Shared.getPlayers()){
			if (p.disabledTablistObjective) continue;
			PacketAPI.registerScoreboardObjective(p, ObjectiveName, title, DisplaySlot, displayType);
			for (ITabPlayer all : Shared.getPlayers()) PacketAPI.setScoreboardScore(all, p.getName(), ObjectiveName, getValue(p));
		}
		Shared.cpu.startRepeatingMeasuredTask(500, "refreshing tablist objective", "Yellow number in tablist", new Runnable() {
			public void run(){
				for (ITabPlayer p : Shared.getPlayers()){
					if (p.disabledTablistObjective) continue;
					if (p.properties.get("tablist-objective").isUpdateNeeded()) {
						for (ITabPlayer all : Shared.getPlayers()) PacketAPI.setScoreboardScore(all, p.getName(), ObjectiveName, getValue(p));
					}
				}
			}
		});
	}
	@Override
	public void unload() {
		for (ITabPlayer p : Shared.getPlayers()){
			if (p.disabledTablistObjective) continue;
			PacketAPI.unregisterScoreboardObjective(p, ObjectiveName);
		}
	}
	@Override
	public void onJoin(ITabPlayer connectedPlayer) {
		if (connectedPlayer.disabledTablistObjective) return;
		PacketAPI.registerScoreboardObjective(connectedPlayer, ObjectiveName, title, DisplaySlot, displayType);
		for (ITabPlayer all : Shared.getPlayers()){
			PacketAPI.setScoreboardScore(all, connectedPlayer.getName(), ObjectiveName, getValue(connectedPlayer));
			PacketAPI.setScoreboardScore(connectedPlayer, all.getName(), ObjectiveName, getValue(all));
		}
	}
	@Override
	public void onQuit(ITabPlayer disconnectedPlayer) {
	}
	@Override
	public void onWorldChange(ITabPlayer p, String from, String to) {
		if (p.disabledTablistObjective && !p.isDisabledWorld(Configs.disabledTablistObjective, from)) {
			PacketAPI.unregisterScoreboardObjective(p, ObjectiveName);
		}
		if (!p.disabledTablistObjective && p.isDisabledWorld(Configs.disabledTablistObjective, from)) {
			onJoin(p);
		}
	}
	public int getValue(ITabPlayer p) {
		return Shared.errorManager.parseInteger(p.properties.get("tablist-objective").get(), 0, "Yellow number in tablist");
	}
}