package fr.skytasul.quests.options;

import fr.skytasul.quests.api.options.OptionSet;
import fr.skytasul.quests.api.options.QuestOptionItem;
import fr.skytasul.quests.utils.Lang;
import fr.skytasul.quests.utils.XMaterial;
import fr.skytasul.quests.utils.compatibility.DependenciesManager;

public class OptionHologramLaunch extends QuestOptionItem {
	
	public OptionHologramLaunch() {
		super(OptionStarterNPC.class);
	}
	
	@Override
	public XMaterial getDefaultMaterial() {
		return XMaterial.RED_STAINED_GLASS_PANE;
	}
	
	@Override
	public String getItemName() {
		return Lang.hologramLaunch.toString();
	}
	
	@Override
	public String getItemDescription() {
		return Lang.hologramLaunchLore.toString();
	}
	
	@Override
	public boolean shouldDisplay(OptionSet options) {
		return DependenciesManager.holod.isEnabled() && options.getOption(OptionStarterNPC.class).getValue() != null;
	}
	
}
