package db_passagens_aereas;

public class ClasseEconomica extends Passagem {
    private boolean despacharMala;
    private double taxaBagagem = 120.00;

    public ClasseEconomica(String nomePassageiro, String numeroVoo, double precoOriginal, boolean despacharMala) {
        super(nomePassageiro, numeroVoo, precoOriginal);
        this.despacharMala = despacharMala;
    }

    @Override
    public double calcularPrecoFinal() {
        if (despacharMala) {
            return getPrecoOriginal() + taxaBagagem;
        }
        return getPrecoOriginal();
    }

    public boolean isDespacharMala() { return despacharMala; }

    @Override
    public String toString() {
        return super.toString() + "\nClasse: Econômica | Bagagem de mão (10kg): Inclusa" +
                "\nDespachar Mala Extra? " + (despacharMala ? "Sim (Taxa de R$ 120,00 aplicada)" : "Não") +
                "\nVALOR FINAL BILHETE: R$ " + calcularPrecoFinal();
    }
}