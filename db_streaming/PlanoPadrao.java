package db_streaming;

public class PlanoPadrao extends ContaStreaming {
    private String qualidadeVideo = "Full HD";
    private int limiteTelas = 2;

    public PlanoPadrao(String nome, String email) {
        super(nome, email);
        this.setPrecoBase(30.00); // Configura o preço específico
    }

    @Override
    public String toString() {
        return super.toString() + "\n📦 Plano: Padrão | 💰 Valor: R$ " + getPrecoBase() +
                " | 📺 Qualidade Máxima: " + qualidadeVideo + " | 📱 Limite: " + limiteTelas + " telas";
    }
}