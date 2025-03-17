package fmt.cerulean.solitaire;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.Pair;

public class Board {
	public static int ROWS = 6;
	public List<CardStack> arts = Lists.newArrayList();
	public List<CardStack> dream = Lists.newArrayList();
	public CardStack held = new CardStack();
	public CardStack mirroredHeld = new CardStack();
	public CardPos pickup = null;

	public Board(long seed) {
		for (int i = 0; i < ROWS; i++) {
			arts.add(new CardStack());
			dream.add(new CardStack());
		}
		Pair<List<Card>, List<Card>> cards = Deck.generateDeck(seed);
		int deckSize = cards.getLeft().size();
		for (int i = 0; i < deckSize; i++) {
			arts.get(i % ROWS).cards.add(cards.getLeft().get(i));
			dream.get(i % ROWS).cards.add(cards.getRight().get(i));
		}
	}

	private CardStack getPosStack(CardPos pos) {
		if (pos.type() == CardPos.Type.HELD) {
			if (pos.row() == 0) {
				return held;
			}
		} else {
			List<CardStack> stacks = switch(pos.type()) {
				case ARTS -> arts;
				case DREAM -> dream;
				default -> null;
			};
			if (pos.row() >= 0 && pos.row() < ROWS) {
				return stacks.get(pos.row());
			}
		}
		return new CardStack();
	}

	public Card getCard(CardPos pos) {
		CardStack stack = getPosStack(pos);
		if (pos.depth() < stack.cards.size()) {
			return stack.cards.get(pos.depth());
		}
		return null;
	}

	public CardStack getStack(CardPos pos) {
		return getStack(pos, false);
	}

	public CardStack getStack(CardPos pos, boolean remove) {
		CardStack stack = getPosStack(pos);
		if (pos.depth() < stack.cards.size()) {
			return stack.getSubStack(pos.depth(), remove);
		}
		return new CardStack();
	}

	public void place(CardPos pos, CardStack stack) {
		CardStack base = getPosStack(pos);
		base.place(stack);
	}

	public void pickup(CardPos pos) {
		if (held.isEmpty()) {
			CardStack stack = getStack(pos, true);
			if (!stack.isEmpty()) {
				for (int i = 1; i < stack.cards.size(); i++) {
					if (!canStack(stack.cards.get(i), stack.cards.get(i - 1))) {
						place(pos, stack);
						return;
					}
				}
				held.cards = stack.cards;
				mirroredHeld = getStack(pos.mirrored(), true);
				pickup = pos;
			}
		}
	}

	public void drop(CardPos pos) {
		if (pos != null && !held.isEmpty()) {
			CardStack stack = getPosStack(pos);
			if (stack.isEmpty() || canStack(held.cards.get(0), stack.cards.get(stack.cards.size() - 1))) {
				stack.place(held);
				getPosStack(pos.mirrored()).place(mirroredHeld);
				held.cards.clear();
				mirroredHeld.cards.clear();
				return;
			}
		}
		if (pickup != null && !held.isEmpty()) {
			getPosStack(pickup).place(held);
			getPosStack(pickup.mirrored()).place(mirroredHeld);
			held.cards.clear();
			mirroredHeld.cards.clear();
		}
	}

	public static boolean canStack(Card over, Card under) {
		if (over.suit().type != under.suit().type) {
			return false;
		} else {
			if (over.suit().type == Suit.Type.ARTS) {
				return over.suit() == under.suit() && (over.rank() == under.rank() - 1);
			} else {
				return over.suit() != under.suit() && (over.rank() == under.rank() + 1 || over.rank() == under.rank() - 1);
			}
		}
	}
}
