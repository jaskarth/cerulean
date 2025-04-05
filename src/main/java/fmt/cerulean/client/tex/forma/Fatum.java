package fmt.cerulean.client.tex.forma;

public record Fatum(int ante, int astrum) {
    public Sors sors() {
        return new Sors(SpesPratum.pratumBonum(ante), astrum);
    }
}
