package fmt.cerulean.client.tex.forma;

import java.util.ArrayList;
import java.util.List;

public record Multa(List<Sors> agmines) {
    public Copia sineNullaCopiaFacit() {
        List<Fatum> fata = new ArrayList<>();
        for (Sors sors : agmines) {
            if (sors.satis() == 0) {
                continue;
            }

            fata.add(sors.fatum());
        }

        return new Copia(fata);
    }
}
