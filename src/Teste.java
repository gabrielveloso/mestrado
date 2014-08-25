import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import org.apache.mahout.math.SparseMatrix;


public class Teste {
	
	public static int k = 200;
	
	public static SparseMatrix retornarMatrizQ(){
		
		SparseMatrix m = new SparseMatrix(49289, k);
		Random gerador = new Random();
		
		for(int i = 0; i < 49289; i++){
			for(int j = 0; j < k; j++){
				m.setQuick(i, j, gerador.nextInt(6));
			}
		}
		
		return m;
		
		
	}
	
	public static SparseMatrix retornarMatrizP(){
		SparseMatrix m = new SparseMatrix(k, 139738);
		
		Random gerador = new Random();
		for(int i = 0; i < k; i++){
			for(int j = 0; j < 139738; j++){
				m.setQuick(i, j, gerador.nextInt(6));
			}
		}
		
		return m;
	}

		
	public static SparseMatrix retornarMatrizR(){
		
		
		SparseMatrix m = new SparseMatrix(49289, 139738);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/ratings_data.txt"));
			
			String line = "";
			
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				m.setQuick(new Integer(values[0]), new Integer(values[1]), new Double(values[2]));
				
				
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
		
		return m;
	}
	
	public static void main(String[] args) {
		SparseMatrix matrizR = retornarMatrizR();
		
		SparseMatrix matrizQ = retornarMatrizQ();
		
		
		SparseMatrix matrizP = retornarMatrizP();
		
		SparseMatrix nova = (SparseMatrix) matrizQ.times(matrizP);
		
		System.out.println(nova.getQuick(1, 1));
		
		//System.out.println(matrizQ.toString());
		
		
		System.out.println(matrizR.getQuick(1, 1));
		System.out.println(matrizR.getQuick(1, 2));
		System.out.println(matrizR.getQuick(1, 3));
		System.out.println(matrizR.getQuick(1, 4));
		System.out.println(matrizR.getQuick(1, 5));
		System.out.println(matrizR.getQuick(1, 1000));
		
		//System.out.println(matrizEsparca.toString());
	}

}
