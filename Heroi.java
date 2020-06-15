public class Heroi {
    
    private int linha;
    private int coluna;
    
    public Heroi (int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    @Override
    public String toString() {
        return "(" + linha + ", " + coluna + ")";
    }
}