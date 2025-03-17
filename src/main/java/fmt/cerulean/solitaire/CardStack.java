package fmt.cerulean.solitaire;

import java.util.List;

import com.google.common.collect.Lists;

public class CardStack {
	public static CardStack EMPTY = new CardStack(List.of());
	public List<Card> cards;

	public CardStack() {
		this(Lists.newArrayList());
	}

	public CardStack(List<Card> cards) {
		this.cards = cards;
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public void place(CardStack stack) {
		cards.addAll(stack.cards);
	}

	public CardStack getSubStack(int depth, boolean remove) {
		if (depth < 0 || depth >= cards.size()) {
			return new CardStack();
		}
		List<Card> ret = Lists.newArrayList();
		for (int i = depth; i < cards.size(); i++) {
			ret.add(cards.get(i));
			if (remove) {
				cards.remove(i);
				i--;
			}
		}
		return new CardStack(ret);
	}
}
