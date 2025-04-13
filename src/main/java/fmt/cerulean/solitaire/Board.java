package fmt.cerulean.solitaire;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.Pair;

public class Board {
	public static int COLS = 7;
	public List<CardStack> arts = Lists.newArrayList();
	public List<CardStack> dream = Lists.newArrayList();
	public CardStack held = new CardStack();
	public CardStack mirroredHeld = new CardStack();
	public CardPos pickup = null;
	public List<Integer> moves = Lists.newArrayList();

	public Board(long seed) {
		for (int i = 0; i < COLS; i++) {
			arts.add(new CardStack());
			dream.add(new CardStack());
		}
		Pair<List<Card>, List<Card>> cards = Deck.generateDeck(seed);
		int deckSize = cards.getLeft().size();
		for (int i = 0; i < deckSize; i++) {
			int c = i % 6;
			if (c >= 3) {
				c++;
			}
			arts.get(c).cards.add(cards.getLeft().get(i));
			dream.get(c).cards.add(cards.getRight().get(i));
		}
	}

	public static boolean validate(long seed, List<Integer> moves) {
		try {
			Board board = new Board(seed);
			for (int move : moves) {
				CardPos from = CardPos.decode(move >> 16);
				CardPos to = CardPos.decode(move & 0xFFFF);
				if (board.pickup(from) && board.drop(to)) {
					continue;
				}
				return false;
			}
			return board.hasWon();
		} catch(Throwable t) {
			return false;
		}
	}

	public boolean hasWon() {
		for (int i = 0; i < COLS; i++) {
			CardStack stack = getStack(new CardPos(CardPos.Type.DREAM, i, 0));
			if (stack.isEmpty()) {
				return false;
			}
			if (stack.cards.size() != 6) {
				return false;
			}
			Card last = null;
			for (Card card : stack.cards) {
				if (last == null || canStack(card, last)) {
					last = card;
					continue;
				}
				return false;
			}
		}
		return true;
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
			if (pos.row() >= 0 && pos.row() < COLS) {
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

	public boolean pickup(CardPos pos) {
		if (held.isEmpty()) {
			CardStack stack = getStack(pos, true);
			if (!stack.isEmpty()) {
				for (int i = 1; i < stack.cards.size(); i++) {
					if (!canStack(stack.cards.get(i), stack.cards.get(i - 1))) {
						place(pos, stack);
						return false;
					}
				}
				held.cards = stack.cards;
				mirroredHeld = getStack(pos.mirrored(), true);
				pickup = pos;
				return true;
			}
		}
		return false;
	}

	public boolean drop(CardPos pos) {
		if (pos != null && !held.isEmpty()) {
			CardStack stack = getPosStack(pos);
			if (stack.isEmpty() || canStack(held.cards.get(0), stack.cards.get(stack.cards.size() - 1))) {
				stack.place(held);
				getPosStack(pos.mirrored()).place(mirroredHeld);
				held.cards.clear();
				mirroredHeld.cards.clear();
				int move = (pickup.encode() << 16) | pos.encode();
				moves.add(move);
				return true;
			}
		}
		if (pickup != null && !held.isEmpty()) {
			getPosStack(pickup).place(held);
			getPosStack(pickup.mirrored()).place(mirroredHeld);
			held.cards.clear();
			mirroredHeld.cards.clear();
		}
		return false;
	}

	public static boolean canStack(Card over, Card under) {
		if (over.suit().type != under.suit().type) {
			return false;
		} else {
			if (over.suit().type == Suit.Type.ARTS) {
				return over.suit() != under.suit() && (over.rank() == under.rank() - 1);
			} else {
				return over.suit() == under.suit() && (over.rank() == under.rank() + 1 || over.rank() == under.rank() - 1);
			}
		}
	}
}
