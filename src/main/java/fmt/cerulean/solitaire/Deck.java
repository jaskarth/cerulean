package fmt.cerulean.solitaire;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.util.Pair;

public class Deck {
	public static final int SIZE = 42;
	public static final List<Card> ARTS_FACES = Lists.newArrayList();
	public static final List<Card> DREAM_FACES = Lists.newArrayList();

	static {
		for (Suit suit : Suit.values()) {
			if (suit.type == Suit.Type.ARTS) {
				for (int i = 1; i <= 14; i++) {
					ARTS_FACES.add(new Card(suit, i));
				}
			} else if (suit.type == Suit.Type.DREAM) {
				for (int i = 1; i <= 6; i++) {
					DREAM_FACES.add(new Card(suit, i));
				}
			}
		}
	}

	public static Pair<List<Card>, List<Card>> generateDeck(long seed) {
		Random random = new Random(seed);
		List<Card> arts = Lists.newArrayList(ARTS_FACES);
		List<Card> dream = Lists.newArrayList(DREAM_FACES);
		// Repetition breeds mastery
		Collections.shuffle(arts, random);
		Collections.shuffle(dream, random);
		return new Pair<List<Card>, List<Card>>(arts, dream);
	}
}
