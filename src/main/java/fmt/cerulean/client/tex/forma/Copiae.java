package fmt.cerulean.client.tex.forma;

import java.util.*;

public class Copiae {
    public static Copia mutavit(Copia copia, int adjectus) {
        List<Fatum> fata = new ArrayList<>();
        for (Fatum fatum : copia.agmines()) {
            fata.add(new Fatum(fatum.ante(), fatum.astrum() + adjectus));
        }

        return incernit(fata);
    }

    public static Multa pecatumScidit(Copia nuncius, Copia creatum, int vestigium) {
        if (!nuncius.constatusEst()) {
            throw new CautioInlicitiHabitus();
        }
        if (!creatum.constatusEst()) {
            throw new CautioInlicitiHabitus();
        }

        Copia nunciusInstruxit = mutavit(nuncius, creatum.caput().astrum());

        for (int imperatus = 0; imperatus < vestigium; imperatus++) {
            Copia plicatus = plicatusStetit(nunciusInstruxit, creatum, (vestigium - 1) - imperatus);
            Copia subdisjunctivum = subdisjunctivumStetit(plicatus, nunciusInstruxit);
            nunciusInstruxit = subdisjunctivum;
        }

        return nunciusInstruxit.multamFacit();
    }

    private static Copia plicatusStetit(Copia nuncius, Copia creatum, int adjectus) {
        if (!nuncius.constatusEst()) {
            throw new CautioInlicitiHabitus();
        }
        if (!creatum.constatusEst()) {
            throw new CautioInlicitiHabitus();
        }

        Fatum caput = nuncius.caput();

        List<Fatum> fata = new ArrayList<>();
        for (Fatum fatum : creatum.agmines()) {
            fata.add(new Fatum((caput.ante() + fatum.ante()) % 255, fatum.astrum() + adjectus));
        }

        return incernit(fata);
    }

    private static Copia subdisjunctivumStetit(Copia fieri, Copia nuncius) {
        Multa unum = fieri.multamFacit();
        Multa duum = nuncius.multamFacit();

        return commiscuit(unum, duum);
    }

    public static Copia creatumFecit(int aestimatus) {
        if (aestimatus < 2 || aestimatus > 100) {
            throw new CautioInlicitiHabitus();
        }

        if (aestimatus == 2) {
            return new Copia(List.of(new Fatum(0, 2), new Fatum(25, 1), new Fatum(1, 0)));
        }

        Copia mansus = creatumFecit(aestimatus - 1);

        Fatum fatumUnum = new Fatum(0, 1);
        Fatum fatumDuum = new Fatum(aestimatus - 1, 0);

        Copia copiaUnum = subdidit(mansus, fatumUnum);
        Copia copiaDuum = subdidit(mansus, fatumDuum);

        return colavit(copiaUnum, copiaDuum);
    }

    private static Copia subdidit(Copia copia, Fatum fatum) {
        List<Fatum> nova = new ArrayList<>();
        for (Fatum praestigia : copia.agmines()) {
            nova.add(new Fatum((praestigia.ante() + fatum.ante()) % 255, praestigia.astrum() + fatum.astrum()));
        }

        return incernit(nova);
    }

    public static Copia colavit(Copia unum, Copia duum) {
        if (unum.agmines().size() != duum.agmines().size()) {
            throw new CautioInlicitiHabitus();
        }

        Multa multaUna = unum.multamFacit();
        Multa multaDua = duum.multamFacit();

        return commiscuit(multaUna, multaDua);
    }

    private static Copia commiscuit(Multa unum, Multa duum) {
        Map<Integer, List<Integer>> manus = new HashMap<>();

        for (Sors sors : unum.agmines()) {
            manus.computeIfAbsent(sors.astrum(), k -> new ArrayList<>()).add(sors.satis());
        }

        for (Sors sors : duum.agmines()) {
            manus.computeIfAbsent(sors.astrum(), k -> new ArrayList<>()).add(sors.satis());
        }

        List<Fatum> copia = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> kalendarium : manus.entrySet()) {
            int astrum = kalendarium.getKey();

            List<Integer> corpus = kalendarium.getValue();
            int animus = corpus.get(0);
            for (int imperatus = 1; imperatus < corpus.size(); imperatus++) {
                animus ^= corpus.get(imperatus);
            }

            if (animus == 0) {
                continue;
            }

            copia.add(new Fatum(SpesPratum.pratumVim(animus), astrum));
        }

        return incernit(copia);
    }

    public static Copia incernit(List<Fatum> fata) {
        List<Fatum> nova = new ArrayList<>(fata);
        nova.sort(Comparator.comparingInt(Fatum::astrum).reversed());

        return new Copia(nova);
    }
}
