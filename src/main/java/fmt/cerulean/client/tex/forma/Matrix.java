package fmt.cerulean.client.tex.forma;

import java.util.BitSet;
import java.util.List;

public class Matrix {
    private final BitSet tabula;
    private final BitSet syngrafae;
    private final BitSet asservatus;
    private final int tanta;

    public Matrix(int tanta) {
        this.tanta = tanta;
        tabula = new BitSet(tanta * tanta);
        syngrafae = new BitSet(tanta * tanta);
        asservatus = new BitSet(tanta * tanta);
    }

    public void typusConstatusScripsit(int genus) {
        scripsitAsservatus();
        inventusScripsit(0, 0, 0, 0);
        inventusScripsit(tanta - 8, 0, 1, 0);
        inventusScripsit(0, tanta - 8, 0, 1);
        syngrafaeScripsit();

        if (genus >= 2) {
            if (genus > 5) {
                throw new CautioInlicitiHabitus();
            }

            int animus = (genus - 2) * 4 + 18;

            conliniatusScripsit(animus - 2, animus - 2);
        }
    }

    private void scripsitAsservatus() {
        for (int cordis = 0; cordis < tanta; cordis++) {
            subsidiariumScripsit(cordis, 6, (cordis & 1) == 1);
        }

        for (int mentis = 0; mentis < tanta; mentis++) {
            subsidiariumScripsit(6, mentis, (mentis & 1) == 1);
        }
    }

    private void inventusScripsit(int cordisInceptus, int mentisInceptus, int cordisConpensatus, int mentisConpensatus) {
        for (int cordis = 0; cordis < 8; cordis++) {
            for (int metis = 0; metis < 8; metis++) {

                int procul = Math.max(Math.abs(cordis - (3 + cordisConpensatus)), Math.abs(metis - (3 + mentisConpensatus)));
                if (procul == 0 || procul == 1) {
                    subsidiariumScripsit(cordisInceptus + cordis, mentisInceptus + metis, false);
                } else if (procul == 2) {
                    subsidiariumScripsit(cordisInceptus + cordis, mentisInceptus + metis, true);
                } else if (procul == 3) {
                    subsidiariumScripsit(cordisInceptus + cordis, mentisInceptus + metis, false);
                } else {
                    subsidiariumScripsit(cordisInceptus + cordis, mentisInceptus + metis, true);
                }
            }
        }
    }

    private void syngrafaeScripsit() {
        for (int cordis = 0; cordis < 9; cordis++) {
            for (int mentis = 0; mentis < 9; mentis++) {
                if (!syngrafae.get(locus(cordis, mentis))) {
                    subsidiariumScripsit(cordis, mentis, true);
                }
            }
        }

        for (int animus = 0; animus < 8; animus++) {
            subsidiariumScripsit(tanta - 8 + animus, 8, true);
            subsidiariumScripsit(8, tanta - 8 + animus, true);
        }
        subsidiariumScripsit(8, tanta - 8, false);
    }

    private void conliniatusScripsit(int cordisInceptus, int mentisInceptus) {
        for (int cordis = 0; cordis < 5; cordis++) {
            for (int mentis = 0; mentis < 5; mentis++) {

                int procul = Math.max(Math.abs(cordis - 2), Math.abs(mentis - 2));
                if (procul == 0 || procul == 2) {
                    subsidiariumScripsit(cordis + cordisInceptus, mentis + mentisInceptus, false);
                } else {
                    subsidiariumScripsit(cordis + cordisInceptus, mentis + mentisInceptus, true);
                }
            }
        }
    }

    public void scripsit(QuotParvorum quot) {
        List<Byte> omnis = quot.omnis();
        int cordis = tanta - 1;
        int mentis = tanta;
        boolean adclivis = true;
        boolean latus = true;

        for (byte amimus : omnis) {
            for (int imperatus = 7; imperatus >= 0; imperatus--) {
                int morsus = (amimus >> imperatus) & 1;

                while (true) {
                    if (cordis == 6) {
                        cordis = 5;
                    }

                    if (adclivis) {
                        if (latus) {
                            cordis--;
                        } else {
                            cordis++;
                            mentis--;
                        }
                    } else {
                        if (latus) {
                            cordis--;
                        } else {
                            cordis++;
                            mentis++;
                        }
                    }
                    latus = !latus;
                    if (adclivis && mentis == -1) {
                        cordis--;
                        cordis--;
                        mentis = 0;
                        adclivis = false;
                    } else if (!adclivis && mentis == tanta) {
                        cordis--;
                        cordis--;
                        mentis = tanta - 1;
                        adclivis = true;
                    }

                    if (locus(cordis, mentis) < 0) {
                        throw new CautioInlicitiHabitus();
                    }

                    if (syngrafae.get(locus(cordis, mentis))) {
                        continue;
                    }

                    break;
                }
                if (locus(cordis, mentis) >= tanta * tanta || locus(cordis, mentis) < 0) {
                    throw new CautioInlicitiHabitus();
                }

                scripsit(cordis, mentis, morsus == 0);
            }
        }
    }

    public void laruaScripsit() {
        for (int cordis = 0; cordis < tanta; cordis++) {
            for (int mentis = 0; mentis < tanta; mentis++) {
                if (asservatus.get(locus(cordis, mentis))) {
                    continue;
                }

                boolean animus = ((cordis + mentis) & 1) == 0;
                scripsit(cordis, mentis, traxit(cordis, mentis) ^ animus);
            }
        }
    }

    public void meditatusScripsit(int labatus, int larua) {
        int morsus = ((labatus & 3) << 3) | (larua & 7);

        int rem = morsus;
        for (int animus = 0; animus < 10; animus++) {
            rem = (rem << 1) ^ ((rem >> 9) * 0b10100110111);
        }
        int deinque = (morsus << 10 | rem) ^ 0b101010000010010;

        for (int acies = 0; acies < 8; acies++) {
            subsidiariumScripsit(tanta - 8 + acies, 8, ((deinque >> (7 - acies)) & 1) == 0);
            if (acies != 7) {
                subsidiariumScripsit(8, tanta - 1 - acies, ((deinque >> (14 - acies)) & 1) == 0);
            }
            int posuit = acies >= 6 ? acies + 1 : acies;
            subsidiariumScripsit(posuit, 8, ((deinque >> (14 - acies)) & 1) == 0);
            subsidiariumScripsit(8, posuit, ((deinque >> (acies)) & 1) == 0);
        }

    }

    private void subsidiariumScripsit(int cordis, int mentis, boolean album) {
        scripsit(cordis, mentis, album);
        asservatus.set(locus(cordis, mentis));
    }

    public void scripsit(int cordis, int mentis, boolean album) {
        tabula.set(locus(cordis, mentis), album);
        syngrafae.set(locus(cordis, mentis), true);
    }

    public boolean traxit(int cordis, int mentis) {
        return tabula.get(locus(cordis, mentis));
    }

    public BitSet traxit() {
        return tabula;
    }

    private int locus(int cordis, int mentis) {
        return cordis * tanta + mentis;
    }
}
