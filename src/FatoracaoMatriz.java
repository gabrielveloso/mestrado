import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;


public class FatoracaoMatriz {
	
	
	public static double[][] retornarMatrizR(){
		double[][] matrizR = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/ratings_data.txt"));
			
			matrizR = new double[49289][139738];
			
			String line = "";
			
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				matrizR[new Integer(values[0])][new Integer(values[1])] = new Integer(values[2]);
				
				
			}
				
				br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return matrizR;
	}
	
	
	public static void main(String[] args) {
		
		
		RealMatrix m = MatrixUtils.createRealMatrix(retornarMatrizR());
		
		RealMatrix linha = m.getRowMatrix(1);
		
		System.out.println(linha.toString());
	}

}
