import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe principal para realizar a solucao de um labirinto utilizando o menor caminho possivel
 */
public class Labirinto {

    private static Heroi heroi;
    private static int numLinhas;
    private static int numColunas;
    private static String arquivoEntrada;
    private static char[][] matrizLabirinto;
    private static boolean[][] matrizVisitados;
    private static Queue<Integer> filaMovimentosLinhas; // Armazena o numero da linha dos movimentos validos dados
    private static Queue<Integer> filaMovimentosColunas; // Armazena o numero da coluna dos movimentos validos dados
    private static int posRestantesParaVerificarVizinhos = 1;
    private static int numPosParaVerificarVizinhos = 0;

    public static void main(String[] args) {
        Timestamp inicioExecucao = new Timestamp(System.currentTimeMillis());
        System.out.println("\n\t --> Execucao iniciada <--");
        arquivoEntrada = "caso7_Cohen.txt";
        numLinhas = obterNumLinhas(arquivoEntrada);
        numColunas = obterNumColunas(arquivoEntrada);
        matrizLabirinto = new char[numLinhas][numColunas];
        matrizVisitados = new boolean[numLinhas][numColunas];
        lerArquivo(matrizLabirinto, arquivoEntrada);
        int numPassosAteVilao = procuraVilao();
        System.out.println("\t --> Execucao finalizada <--");
        System.out.println("\nMenor numero possivel de passos para o heroi encontrar o vilao: " + numPassosAteVilao);
        Timestamp fimExecucao = new Timestamp(System.currentTimeMillis());
        System.out.println("\nInicio da execucao:  " + inicioExecucao);
        System.out.println("Termino da execucao: " + fimExecucao);
        
    }

    /**
     * Metodo para encontrar um caminho valido entre o heroi e o vilao.
     * 
     * @return contadorMovimentos
     */
    public static int procuraVilao(){
        int posicaoHeroiLinha = heroi.getLinha(); // Obtem numero da linha da posicao que o heroi se encontra
        int posicaoHeroiColuna = heroi.getColuna(); // Obtem numero da coluna da posicao que o heroi se encontra
        filaMovimentosLinhas = new LinkedList<>(); // Cria uma fila para armazenar a linha da posicao dos movimentos dados
        filaMovimentosColunas = new LinkedList<>(); // Cria uma fila para armazenar a coluna da posicao dos movimentos dados
        int contadorMovimentos = 0;
        
        filaMovimentosLinhas.add(posicaoHeroiLinha); // Adiciona a linha da posicao que o heroi se encontra na fila de linhas
        filaMovimentosColunas.add(posicaoHeroiColuna); // Adiciona a coluna da posicao que o heroi se encontra na fila de colunas

        matrizVisitados[posicaoHeroiLinha][posicaoHeroiColuna] = true; // Marca na matriz de posicoes visitadas que a posicao do heroi ja foi visitada

        // Se a fila estiver vazia nesse momento, significa que todas as posicoes possiveis ja foram visitadas e nao foi encontrado um caminho valido
        while (filaMovimentosLinhas.size() > 0){ // Enquanto a fila nao estiver vazia, poderia ser tambem -> filaMovimentosColunas.size() > 0
            int posAtualLinha = filaMovimentosLinhas.remove(); // Remove da fila a linha da posicao do heroi e a armazena 
            int posAtualColuna = filaMovimentosColunas.remove(); // Remove da fila a coluna da posicao do heroi e a armazena 
            if (matrizLabirinto[posAtualLinha][posAtualColuna] == 'B'){ // Se a posicao atual for o vilao, caminho ate o vilao encontrado
                return contadorMovimentos; // Retorna o contador com o numero de passos dados ate o vilao ser alcancado
            }
            visitaVizinhos(posAtualLinha, posAtualColuna); // Visita os vizinhos da posicao atual
            posRestantesParaVerificarVizinhos--; // Decrementa em 1 o numero de posicoes restantes para verificar dos vizinhos da posicao atual apos visitarVizinhos
            if (posRestantesParaVerificarVizinhos == 0){ // Caso verdadeiro, ja visitamos os vizinhos possiveis e estamos um passo mais proximos do vilao
                posRestantesParaVerificarVizinhos = numPosParaVerificarVizinhos; // Recebe o numero de vizinhos para serem verificados na proxima iteracao
                numPosParaVerificarVizinhos = 0; // Recebe 0 pois vamos descobrir novos vizinhos para a posicao atual
                contadorMovimentos++;
            }
        }
        System.out.println("\nO labirinto nao possui um caminho valido levando o heroi ate o vilao!");
        return -1;
    }

    /**
     * Metodo auxiliar de procuraVilao. Realiza os movimentos possiveis da posicao atual verificando se ha 
     * movimentacao possivel.
     * 
     * @param posAtualLinha
     * @param posAtualColuna
     */
    private static void visitaVizinhos(int posAtualLinha, int posAtualColuna){
        int direcoesPossiveisLinha[] = {-1, 1, 0, 0}; // Um vetor que armazena as possiveis mudancas de linha
        int direcoesPossiveisColuna[] = {0, 0, 1, -1}; // Um vetor que armazena as possiveis mudancas de coluna
        
       /*
        * Os vetores acima sao iterados de forma que os movimentos possiveis do heroi sejam:
        * 
        * Norte: (-1, 0) // Cima
        * Sul: (+1, 0) // Baixo
        * Leste: (0, +1) // Direita
        * Oeste: (0, -1) // Esquerda
        */
        for (int i = 0; i < 4; i++){ // Verifica movimentos nas quatro direcoes possiveis
 
            int vizinhoLinha = posAtualLinha + direcoesPossiveisLinha[i]; // Posicao atual mais linha da direcao de movimento possivel
            int vizinhoColuna = posAtualColuna + direcoesPossiveisColuna[i]; // Posicao atual mais coluna da direcao de movimento possivel

            if (vizinhoLinha < 0 || vizinhoColuna < 0) // Verifica se a posicao esta dentro dos limites minimos do labirinto
                continue; 
            if (vizinhoLinha >= numLinhas || vizinhoColuna >= numColunas) // Verifica se a posicao esta dentro dos limites maximos do labirinto
                continue; 

            if (matrizVisitados[vizinhoLinha][vizinhoColuna]) // Verifica se posicao ja foi visitada
                continue;
            if (matrizLabirinto[vizinhoLinha][vizinhoColuna] == '#') // Verifica se posicao e' uma parede
                continue;
            
            filaMovimentosLinhas.add(vizinhoLinha); // Adiciona a linha da posicao atual do vizinho valido na fila de linhas
            filaMovimentosColunas.add(vizinhoColuna); // Adiciona a coluna da posicao atual do vizinho valido na fila de colunas
            matrizVisitados[vizinhoLinha][vizinhoColuna] = true; // Marca que esta posicao ja foi visitada
            numPosParaVerificarVizinhos++; // quantiPproximoNodosVerificarVizinhos

        }
    }

    public static void lerArquivo(char[][] matrizLabirinto, String arquivoEntrada) {

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoEntrada))){
            String leitura;
            int linha = 0;

            while ((leitura = br.readLine()) != null) {
                char[] caracteres = leitura.toCharArray();
                for (int i = 0; i < caracteres.length; i++){
                    char caracter = caracteres[i];
                    if (caracter == 'A')
                        heroi = new Heroi(linha, i);
                    matrizLabirinto[linha][i] = caracter;
                }            
                linha++;                                     
            }
            br.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static int obterNumLinhas(String arquivoEntrada){
        int linhas = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoEntrada))){
            while (br.readLine() != null) 
                linhas++;
            br.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return linhas;
    }

    public static int obterNumColunas(String arquivoEntrada){
        int colunas = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoEntrada))){
            String leitura = br.readLine();
            colunas = leitura.length();
            br.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return colunas;
    }
}