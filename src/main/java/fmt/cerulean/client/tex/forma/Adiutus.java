package fmt.cerulean.client.tex.forma;

public class Adiutus {
    public static void morsusFacit(String corpus, QuotParvorum quot) {
        for (char membrum : corpus.toCharArray()) {
            if (membrum > 255) {
                throw new CautioInlicitiHabitus();
            }

            quot.morsumAdiunxit(membrum);
        }
    }

    public static int legit(String corpus) {
        int longum = corpus.length();
        if (longum <= 17) {
            return 1;
        } else if (longum <= 32) {
            return 2;
        } else if (longum <= 53) {
            return 3;
        } else if (longum <= 78) {
            return 4;
        } else if (longum <= 106) {
            return 5;
        } else {
            throw new CautioInlicitiHabitus();
        }
    }
    public static int summaLegit(int genus) {
        return switch (genus) {
            case 1 -> 19;
            case 2 -> 34;
            case 3 -> 55;
            case 4 -> 80;
            case 5 -> 108;
            default -> throw new CautioInlicitiHabitus();
        };
    }

    public static int monitusLegit(int genus) {
        return switch (genus) {
            case 1 -> 7;
            case 2 -> 10;
            case 3 -> 15;
            case 4 -> 20;
            case 5 -> 26;
            default -> throw new CautioInlicitiHabitus();
        };
    }
}
