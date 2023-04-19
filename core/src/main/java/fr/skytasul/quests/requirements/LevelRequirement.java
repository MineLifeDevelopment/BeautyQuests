package fr.skytasul.quests.requirements;

import org.bukkit.entity.Player;
import fr.skytasul.quests.api.requirements.AbstractRequirement;
import fr.skytasul.quests.api.requirements.TargetNumberRequirement;
import fr.skytasul.quests.utils.ComparisonMethod;
import fr.skytasul.quests.utils.Lang;

public class LevelRequirement extends TargetNumberRequirement {
	
	public LevelRequirement() {
		this(null, null, 0, ComparisonMethod.GREATER_OR_EQUAL);
	}
	
	public LevelRequirement(String customDescription, String customReason, double target, ComparisonMethod comparison) {
		super(customDescription, customReason, target, comparison);
	}

	@Override
	public double getPlayerTarget(Player p) {
		return p.getLevel();
	}
	
	@Override
	protected String getDefaultReason(Player player) {
		return Lang.REQUIREMENT_LEVEL.format(getFormattedValue());
	}
	
	@Override
	public Class<? extends Number> numberClass() {
		return Integer.class;
	}
	
	@Override
	public void sendHelpString(Player p) {
		Lang.CHOOSE_XP_REQUIRED.send(p);
	}
	
	@Override
	public String getDefaultDescription(Player p) {
		return Lang.RDLevel.format(getShortFormattedValue());
	}

	@Override
	public AbstractRequirement clone() {
		return new LevelRequirement(getCustomDescription(), getCustomReason(), target, comparison);
	}

}
