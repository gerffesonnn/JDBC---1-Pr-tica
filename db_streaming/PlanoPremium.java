package db_streaming;

public class PlanoPremium extends ContaStreaming {
    private String qualidadeVideo = "4K Ultra HD";
    private int limiteTelas = 4;
    private boolean permiteDownload = true;

    public PlanoPremium(String nome, String email) {
        super(nome, email);
        this.setPrecoBase(50.00); // Configura o preço específico
    }

    @Override
    public String toString() {
        return super.toString() + "\n📦 Plano: Premium ✨ | 💰 Valor: R$ " + getPrecoBase() +
                " | 📺 Qualidade Máxima: " + qualidadeVideo + " | 📱 Limite: " + limiteTelas + " telas" +
                "\n⚡ Vantagem Exclusiva: Download offline habilitado? " + (permiteDownload ? "Sim" : "Não");
    }
}