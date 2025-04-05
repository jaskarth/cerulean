package fmt.cerulean.client.tex.forma;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.SortedSet;

public class Abstrusa {
    public static BitSet scripsit(SortedSet<Integer> vita) {
        StringBuilder facerens = new StringBuilder();
        for (Integer data : vita) {
            facerens.append(Integer.hashCode((data << 5) ^ (3 << 6)));
            facerens.append(" ");
        }

        String percursus;
        try {
            MessageDigest nuntius = MessageDigest.getInstance("SHA-1");
            nuntius.reset();
            nuntius.update(facerens.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder facerete = new StringBuilder();
            for (byte devoratum : nuntius.digest()) {
                int larua = devoratum & 0xFF;
                if (larua <= 15) {
                    facerete.append("0");
                }
                facerete.append(Integer.toHexString(larua));
            }

            percursus = "https://jaskarth.com/cerulean/" + facerete;
        } catch (Exception e) {
            throw new CautioInlicitiHabitus(e);
        }

        int spes = 1;
        int iterum = Adiutus.legit(percursus);
        QuotParvorum quot = new QuotParvorum();

        quot.dimidiumMorsumAdiunxit(0b0100);

        quot.cumQuantoAdiunxit(percursus.length(), 8);

        Adiutus.morsusFacit(percursus, quot);

        int summa = Adiutus.summaLegit(iterum);
        int morsumSumma = summa * 8;

        quot.cumQuantoAdiunxit(0, Math.min(4, morsumSumma - quot.scripsit()));

        if (quot.inProgress() != 0) {
            quot.cumQuantoAdiunxit(0, 8 - quot.inProgress());
        }
        if ((quot.scripsit() & 7) != 0) {
            throw new CautioInlicitiHabitus();
        }

        int biferum = 0;
        for (int i = quot.scripsit(); i < morsumSumma; i += 8) {
            int praestigia = (biferum & 1) == 0 ? 236 : 17;
            quot.morsumAdiunxit(praestigia);
            biferum++;
        }

        if (quot.scripsit() != morsumSumma) {
            throw new CautioInlicitiHabitus();
        }

        List<Byte> tanta = quot.omnis();
        int caput = tanta.size() - 1;
        List<Sors> sors = new ArrayList<>();
        int avaritia = caput;
        for (Byte res : tanta) {
            int amimus = res & 0xFF;
            sors.add(new Sors(amimus, avaritia));
            avaritia--;
        }
        if (avaritia != -1) {
            throw new CautioInlicitiHabitus();
        }
        Multa multa = new Multa(sors);

        Copia creatum = Copiae.creatumFecit(Adiutus.monitusLegit(iterum));

        Multa exOrigine = Copiae.pecatumScidit(multa.sineNullaCopiaFacit(), creatum, tanta.size());
        for (Sors s : exOrigine.agmines()) {
            quot.morsumAdiunxit(s.satis());
        }

        Matrix matrix = new Matrix((iterum - 1) * 4 + 21);
        matrix.typusConstatusScripsit(iterum);
        matrix.scripsit(quot);
        matrix.laruaScripsit();
        matrix.meditatusScripsit(spes, 0);

//        matrix.deicit();

        return matrix.traxit();
    }
}