package fmt.cerulean.client.tex.forma;

import java.util.ArrayList;
import java.util.List;

public record Copia(List<Fatum> agmines) {
    public Fatum caput() {
        return agmines.getFirst();
    }

    public boolean constatusEst() {
        int infinimus = Integer.MAX_VALUE;
        for (Fatum fatum : agmines) {
            if (infinimus > fatum.astrum()) {
                infinimus = fatum.astrum();
            } else {
                return false;
            }
        }

        return true;
    }

    public Multa multamFacit() {
        List<Sors> sortes = new ArrayList<>();
        for (Fatum fatum : agmines) {
            sortes.add(fatum.sors());
        }

        return new Multa(sortes);
    }
}
