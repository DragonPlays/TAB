package me.neznamy.tab.bukkit.unlimitedtags;

import org.bukkit.entity.Player;

import me.neznamy.tab.shared.ITabPlayer;

public class NameTagLineManager {

	public static void removeFromRegistered(ITabPlayer armorStandOwner, ITabPlayer removed) {
		for (ArmorStand as : armorStandOwner.getArmorStands()) as.removeFromRegistered(removed);
	}
	public static ArmorStand bindLine(ITabPlayer p, String text, double heightDifference, String ID){
		if (!NameTagX.enable) return null;
		ArmorStand as = new ArmorStand(p, text, heightDifference, ID);
		p.armorStands.add(as);
		return as;
	}
	public static void updateVisibility(ITabPlayer armorStandOwner) {
		for (ArmorStand as : armorStandOwner.getArmorStands()) as.updateVisibility();
	}
	public static void sneak(ITabPlayer armorStandOwner, boolean sneaking) {
		for (ArmorStand as : armorStandOwner.getArmorStands()) as.sneak(sneaking);
	}
	public static void destroy(ITabPlayer armorStandOwner) {
		for (ArmorStand as : armorStandOwner.getArmorStands()) as.destroy();
	}
	public static void destroy(ITabPlayer armorStandOwner, ITabPlayer packetReceiver){
		for (ArmorStand as : armorStandOwner.getArmorStands()) as.getDestroyPacket(packetReceiver, true).send(packetReceiver);
	}
	public static void spawnArmorStand(ITabPlayer armorStandOwner, ITabPlayer packetReceiver, boolean addToRegistered) {
		for (ArmorStand as : armorStandOwner.getArmorStands()) as.getSpawnPacket(packetReceiver, addToRegistered).send(packetReceiver);
	}
	public static void teleportArmorStand(ITabPlayer armorStandOwner, ITabPlayer packetReceiver) {
		for (ArmorStand as : armorStandOwner.getArmorStands()) as.getTeleportPacket().send(packetReceiver);
	}
	public static void teleportOwner(ITabPlayer armorStandOwner, ITabPlayer packetReceiver) {
		if (armorStandOwner.getName().equals(packetReceiver.getName())) return; //avoiding buggy movement when riding entities
		new PacketPlayOutEntityTeleport((Player)armorStandOwner.getPlayer()).send(packetReceiver);
	}
	public static void refreshNames(ITabPlayer armorStandOwner) {
		for (ArmorStand as : armorStandOwner.getArmorStands()) as.refreshName();
	}
	public static boolean isArmorStandID(ITabPlayer p, int id) {
		for (ArmorStand as : p.getArmorStands()) if (as.getEntityId() == id) return true;
		return false;
	}
}