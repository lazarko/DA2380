import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.util.Arrays.copyOfRange;

public class HMM3 {
    public static final int INPUT_LINES = 4;

    static double [][] gamma, alpha, beta;
    static double [][][] di_gamma;
    static double a[][];
    static double b[][];
    static double pi[][];
    static double [] c;
    static double oldLogProb = -999999999;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] inputData = new String[INPUT_LINES];
        for(int i = 0; i < INPUT_LINES; i++){
            inputData[i] = br.readLine();
        }

        String[] first = inputData[0].split(" ");
        double[] transData = new double[first.length];
        for(int i = 0; i < first.length; i++){
            transData[i] = Double.parseDouble(first[i]);

        }

        String[] second = inputData[1].split(" ");
        double[] emisData = new double[second.length];
        for(int i = 0; i < second.length; i++){
            emisData[i] = Double.parseDouble(second[i]);

        }

        String[] third =  inputData[2].split(" ");
        double[] initialData = new double[third.length];
        for(int i = 0; i < third.length; i++){
            initialData[i] = Double.parseDouble(third[i]);

        }

        String[] fourth = inputData[3].split(" ");
        int length = Integer.parseInt(fourth[0]);
        int[] seq = new int[length]; //SEQUENCE
        for(int i = 0; i < length; i++){
            seq[i] = Integer.parseInt(fourth[i+1]);
        }

        a = initMatrix(transData);
        b = initMatrix(emisData);
        pi = initMatrix(initialData);
        gamma = new double[seq.length][a.length];
        di_gamma = new double[seq.length][a.length][a.length]; //TODO: CHECK ARRAY SIZE

        for(int i = 0; i < 700; i++){
            alphaPass(seq);
            betaPass(seq);
            gammas(seq);
            re_estimate(seq);
            double logProb = computeLog(seq);
            if((logProb > oldLogProb)){
                oldLogProb = logProb;

            }else{
               break;
            }

        }

        displayMatrix(a);
        displayMatrix(b);
    }

    public static double computeLog(int[] seq){
        double logProb = 0;
        for(int i = 0; i < seq.length; i++){
            logProb += Math.log(c[i]);
        }
        return -logProb;
    }
    public static void re_estimate(int[] seq){
        //ESTIMATE PI
        for(int i = 0; i < a.length; i++){
            pi[0][i] = gamma[0][i];
        }

        //ESTIMATE A
        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a.length; j++){
                double numer = 0;
                double denom = 0;
                for(int t = 0; t < seq.length-1; t++){
                    numer += di_gamma[t][i][j];
                    denom += gamma[t][i];
                }
                a[i][j] = numer/denom;
            }
        }

        //ESTIMATE B
        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < b[0].length; j++){
                double numer = 0;
                double denom = 0;
                for(int t = 0; t < seq.length; t++){
                    if(seq[t] == j){
                        numer += gamma[t][i];
                    }
                    denom += gamma[t][i];
                }
                b[i][j] = numer / denom;
            }
        }




    }

    public static void gammas(int[] seq){
        double denom = 0;
        for(int t = 0; t < seq.length-1; t++){
            for(int i = 0; i < a.length; i++ ){
                for(int j = 0; j < a.length; j++){
                    denom += alpha[t][i] * a[i][j] * b[j][seq[t+1]] * beta[t+1][j];
                }
            }
            for(int i = 0; i < a.length; i++){
                gamma[t][i] = 0;
                for(int j = 0; j < a.length; j++){
                    di_gamma[t][i][j] = (alpha[t][i] * a[i][j] * b[j][seq[t+1]] * beta[t+1][j]) / denom;
                    gamma[t][i] += di_gamma[t][i][j];
                }
            }
            denom = 0;
        }
            for(int i = 0; i < a.length; i++){
                denom += alpha[seq.length-1][i];
            }
            for(int i = 0; i < a.length; i++){
                gamma[seq.length-1][i] = alpha[seq.length-1][i] / denom;
            }

    }


    public static void betaPass(int [] seq){
        beta = new double[seq.length][a.length];

        for(int i = 0; i < a.length; i++){
            beta[seq.length-1][i] = c[seq.length-1];
        }

        for(int i = seq.length-2; i >= 0; i--){
            for(int j = 0; j < a.length; j++){
                double sum = 0;
                for(int k = 0; k < a.length; k++){
                    sum += a[j][k] * beta[i+1][k] * b[k][seq[i+1]];
                }
                beta[i][j] = sum * c[i];
            }
        }

    }


    public static void alphaPass(int [] seq){
        c = new double[seq.length];
        alpha = new double[seq.length][a.length];

        c[0] = 0;
        for(int i = 0; i < pi.length; i++){
            alpha[0][i] = pi[0][i] * b[i][seq[0]];
            c[0] += alpha[0][i];
        }

        c[0] = 1/c[0];
        for(int i = 0; i < a.length; i++){
            alpha[0][i] = c[0] * alpha[0][i];
        }
        for(int i = 1; i < seq.length; i++){
            c[i] = 0;
            for(int k = 0; k < a.length; k++){
                double sum = 0;
                for(int j = 0; j < a.length; j++){
                    sum += alpha[i-1][j] * a[j][k];
                }
                alpha[i][k] = sum * b[k][seq[i]];
                c[i] = c[i] + alpha[i][k];
            }
            c[i] = 1/c[i];
            for(int j = 0; j < a.length; j++){
                alpha[i][j] = c[i] * alpha[i][j];
            }
        }
    }
    public static double[][] initMatrix(double[] num){
        int rows = (int) num[0];
        int collons = (int) num[1];
        double [][] matrix = new double[rows][collons];
        int increment = 2;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < collons; j++){
                matrix[i][j] = num[increment];
                increment++;
            }
        }
        return matrix;
    }

    public static void displayMatrix(double[][] matrix){
        int rows = matrix.length;
        int col = matrix[0].length;
        System.out.print(rows + " " + col + " ");
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < col; j++){
                System.out.print(matrix[i][j] + " ");
            }
        }
        System.out.println();
    }
}
