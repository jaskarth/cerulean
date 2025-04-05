package fmt.cerulean.client.tex.forma;

public record Sors(int satis, int astrum) {
    public Fatum fatum() {
        return new Fatum(SpesPratum.pratumVim(satis), astrum);
    }
}
