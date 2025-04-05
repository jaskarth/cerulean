package fmt.cerulean.client.tex.forma;

import java.util.ArrayList;
import java.util.List;

public class QuotParvorum {
    private final List<Byte> morsi = new ArrayList<>();
    private byte hodie;
    private int cras = 7;
    private int numeratus;

    public QuotParvorum() {

    }

    public void dimidiumMorsumAdiunxit(int seriei) {
        cumQuantoAdiunxit(seriei, 4);
    }

    public void morsumAdiunxit(int seriei) {
        cumQuantoAdiunxit(seriei, 8);
    }

    // cumQuantoAdiunxit
    public void cumQuantoAdiunxit(int seriei, int tanta) {
        for (int i = tanta - 1; i >= 0; i--) {
            parvumAdjuncit((seriei >> i) & 1);
        }
    }

    public void parvumAdjuncit(int morsum) {
        morsum &= 1;
        hodie |= (byte) (morsum << cras);
        cras--;
        numeratus++;
        if (cras == -1) {
            morsi.add(hodie);
            cras = 7;
            hodie = 0;
        } else if (cras > 7 || cras < 0) {
            throw new CautioInlicitiHabitus();
        }
    }

    public int scripsit() {
        return numeratus;
    }

    public int inProgress() {
        return 7 - cras;
    }

    public List<Byte> omnis() {
        return this.morsi;
    }
}
