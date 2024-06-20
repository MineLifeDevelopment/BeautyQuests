package fr.skytasul.quests.integrations.npcs;

import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.NpcData;
import de.oliver.fancynpcs.api.events.NpcInteractEvent;
import de.oliver.fancynpcs.api.events.NpcRemoveEvent;
import de.oliver.fancynpcs.api.utils.SkinFetcher;
import fr.skytasul.quests.api.npcs.BqInternalNpc;
import fr.skytasul.quests.api.npcs.BqInternalNpcFactory;
import fr.skytasul.quests.api.npcs.NpcClickType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.stream.Collectors;

public class BQFancyNPCs implements BqInternalNpcFactory.BqInternalNpcFactoryCreatable, Listener {

  @Override
  public int getTimeToWaitForNPCs() {
    return 2;
  }

  @Override
  public boolean isNPC(Entity entity) {
    return FancyNpcsPlugin.get().getNpcManager().getAllNpcs().stream().anyMatch(x -> x.getEntityId() == entity.getEntityId());
  }

  @Override
  public BqInternalNpc fetchNPC(String internalId) {
    final Npc npc = FancyNpcsPlugin.get().getNpcManager().getNpcById(internalId);

    return npc == null ? null : new BQFancyNPC(npc);
  }

  @Override
  public Collection<String> getIDs() {
    return FancyNpcsPlugin.get().getNpcManager().getAllNpcs().stream().map(x -> x.getData().getId()).collect(Collectors.toList());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onNPCInteract(NpcInteractEvent e) {
    final Npc npc = e.getNpc();
    final boolean isRight = e.getInteractionType() == NpcInteractEvent.InteractionType.RIGHT_CLICK;
    final boolean isShift = e.getPlayer().isSneaking();

    npcClicked(e, npc.getData().getId(), e.getPlayer(), isRight ? (isShift ? NpcClickType.SHIFT_RIGHT : NpcClickType.RIGHT) : (isShift ? NpcClickType.SHIFT_LEFT : NpcClickType.LEFT));
  }

  @EventHandler
  public void onNPCRemove(NpcRemoveEvent e) {
    npcRemoved(e.getNpc().getData().getId());
  }

  @Override
  public boolean isValidEntityType(EntityType type) {
    return true;
  }

  @Override
  public BqInternalNpc create(Location location, EntityType type, String name, @Nullable String skin) {
    NpcData data = new NpcData(name, null, location);
    data.setType(type);
    if (skin != null) {
      SkinFetcher skin1 = new SkinFetcher(skin);
      data.setSkin(skin1);
    }

    Npc npc = FancyNpcsPlugin.get().getNpcAdapter().apply(data);
    FancyNpcsPlugin.get().getNpcManager().registerNpc(npc);
    npc.create();
    npc.spawnForAll();

    return new BQFancyNPC(npc);
  }

  public static class BQFancyNPC implements BqInternalNpc {
    private final Npc npc;

    private BQFancyNPC(Npc npc) {
      this.npc = npc;
    }

    @Override
    public String getInternalId() {
      return npc.getData().getId();
    }

    @Override
    public String getName() {
      return npc.getData().getName();
    }

    @Override
    public boolean isSpawned() {
      return true; // Depends on the player viewing, but usually, it is.
    }

    @Override
    public Entity getEntity() {
      return null; // Packet based, so no server-side entity :/
    }

    @Override
    public Location getLocation() {
      return npc.getData().getLocation();
    }

    @Override
    public boolean setNavigationPaused(boolean paused) {
      return true;
    }
  }
}
