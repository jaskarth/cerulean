package fmt.cerulean.client.tex.forma;

import java.util.HashMap;
import java.util.Map;

public class SpesPratum {
    private static final Map<Integer, Integer> EX_VI_BONUM = new HashMap<>();
    private static final Map<Integer, Integer> EX_BONO_VIM = new HashMap<>();

    static {
        EX_VI_BONUM.put(0, 1);

        for (int i = 0; i < 255; i++) {
            int clavis = pratumBonum(i);
            if (EX_BONO_VIM.containsKey(clavis)) {
                throw new CautioInlicitiHabitus();
            }
            EX_BONO_VIM.put(clavis, i);
        }
    }

    public static int pratumBonum(int vim) {
        if (vim < 0 || vim > 255) {
            throw new CautioInlicitiHabitus();
        }

        Integer bonum = EX_VI_BONUM.get(vim);
        if (bonum != null) {
            return bonum;
        }

        int bonumDuum = pratumBonum(vim - 1);
        bonumDuum *= 2;
        if (bonumDuum >= 256) {
            bonumDuum ^= 285;
        }

        EX_VI_BONUM.put(vim, bonumDuum);

        return bonumDuum;
    }

    public static int pratumVim(int bonum) {
        return EX_BONO_VIM.get(bonum);
    }
}
