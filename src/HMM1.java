import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HMM1 {
    public static final int INPUT_LINES = 4;

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
        double[] seq = new double[length]; //SEQUENCE
        for(int i = 0; i < length; i++){
            seq[i] = Double.parseDouble(fourth[i+1]);
        }

        double [][] a = initMatrix(transData);
        double [][] b = initMatrix(emisData);
        double [][] pi = initMatrix(initialData);



        double [][] alphaMatrix = new double[seq.length][a.length];


        for(int i = 0; i < pi[0].length; i++){
            alphaMatrix[0][i] = pi[0][i] * b[i][(int)seq[0]];
        }

        for(int i = 1; i < seq.length; i++){
            for(int k = 0; k < a.length; k++){
                double sum = 0;
                for(int j = 0; j < a.length; j++){
                    sum += alphaMatrix[i-1][j] * a[j][k];
                }
                alphaMatrix[i][k] = sum * b[k][(int)seq[i]];
                //System.out.println(sum + " * " + b[(int)seq[i]][k]);
                //System.out.print(alphaMatrix[i][k] + " ");
            }
            //System.out.println();
        }
        double alpha_sum = 0;

        for(int i = 0; i < a.length; i++){
            alpha_sum += alphaMatrix[seq.length-1][i];
        }

        System.out.println(alpha_sum);

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

}
