package db_passagens_aereas;

public class ClasseExecutiva extends Passagem {
    private boolean servicoBordoVip = true;
    private boolean acessoSalaVip = true;

    public ClasseExecutiva(String nomePassageiro, String numeroVoo, double precoOriginal) {
        super(nomePassageiro, numeroVoo, precoOriginal);
    }

    @Override
    public double calcularPrecoFinal() {
        return getPrecoOriginal() * 1.5;
    }

    @Override
    public String toString() {
        return super.toString() + "\nClasse: Executiva (Acréscimo de 50% embutido)" +
                "\nBagagem Despachada: 2 malas de até 23kg Grátis!" +
                "\nBenefícios: Serviço de Bordo VIP Incluso | Lounge: Acesso à Sala VIP liberado!" +
                "\nVALOR FINAL BILHETE: R$ " + calcularPrecoFinal();
    }
}