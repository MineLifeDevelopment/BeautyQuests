package fr.skytasul.quests.rewards;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import fr.skytasul.quests.api.objects.QuestObjectClickEvent;
import fr.skytasul.quests.api.objects.QuestObjectLoreBuilder;
import fr.skytasul.quests.api.rewards.AbstractReward;
import fr.skytasul.quests.gui.permissions.PermissionListGUI;
import fr.skytasul.quests.utils.Utils;
import fr.skytasul.quests.utils.types.Permission;

public class PermissionReward extends AbstractReward {
	
	public List<Permission> permissions;

	public PermissionReward(){
		this(null, new ArrayList<>());
	}

	public PermissionReward(String customDescription, List<Permission> permissions) {
		super(customDescription);
		this.permissions = permissions;
	}

	@Override
	public List<String> give(Player p) {
		for (Permission perm : permissions) {
			perm.give(p);
		}
		return null;
	}

	@Override
	public AbstractReward clone() {
		return new PermissionReward(getCustomDescription(), new ArrayList<>(permissions));
	}
	
	@Override
	protected void addLore(QuestObjectLoreBuilder loreBuilder) {
		super.addLore(loreBuilder);
		loreBuilder.addDescriptionAsValue(permissions.size() + " permissions");
	}
	
	@Override
	public void itemClick(QuestObjectClickEvent event) {
		new PermissionListGUI(permissions, permissions -> {
			PermissionReward.this.permissions = permissions;
			event.reopenGUI();
		}).create(event.getPlayer());
	}
	
	@Override
	public void save(ConfigurationSection section) {
		section.set("perms", Utils.serializeList(permissions, Permission::serialize));
	}

	@Override
	public void load(ConfigurationSection section){
		permissions.addAll(Utils.deserializeList(section.getMapList("perms"), Permission::deserialize));
	}
	
}
