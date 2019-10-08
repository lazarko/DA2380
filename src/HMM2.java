import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HMM2 {
    public static final int INPUT_LINES = 4;
    public static void main (String[] args) throws IOException {

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

        double [][] a = initMatrix(transData);
        double [][] b = initMatrix(emisData);
        double [][] pi = initMatrix(initialData);

        double [][] sigmaMatrix = new double[seq.length][a.length];
        int[][] indexMatrix = new int[seq.length][a.length];
        int initial_state = 0;

        for(int i = 0; i < pi[0].length; i++){
            sigmaMatrix[initial_state][i] = pi[initial_state][i] * b[i][seq[initial_state]];
        }

        for(int i = 1; i < seq.length; i++){
            for(int j = 0; j < a.length; j++){
                double [] comp = new double[a.length];
                for(int k = 0; k < a.length; k++){
                    comp[k] = sigmaMatrix[i-1][k] * a[k][j] * b[j][seq[i]];
                }
                sigmaMatrix[i][j] =  argMax(comp);
                indexMatrix[i-1][j] = argMaxIndex(comp);
            }
        }

        int [] output = new int[seq.length];

        output[seq.length-1] = argMaxIndex(sigmaMatrix[seq.length-1]);


        for(int i = seq.length-2; i >= 0; i--){
            output[i] = indexMatrix[i][output[i+1]];
        }

        for(int i = 0; i < output.length; i++){
            System.out.print(output[i] + " ");
        }


    }


    public static int argMaxIndex(double[] arr){
        double l = 0;
        int index = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i] > l){
                index = i;
                l = arr[i];
            }
        }
        return index;
    }

    public static double argMax(double[] arr){
        double l = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i] > l){
                l = arr[i];
            }
        }
        return l;
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
