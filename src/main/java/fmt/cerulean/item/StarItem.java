package fmt.cerulean.item;

import fmt.cerulean.flow.FlowResource;
import net.minecraft.item.Item;

public class StarItem extends Item {
	public final FlowResource resource;

	public StarItem(FlowResource resource, Settings settings) {
		super(settings);
		this.resource = resource;
	}
}
