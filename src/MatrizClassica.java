


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.SparseMatrix;
import org.apache.mahout.math.Vector;


public class MatrizClassica {
	
	public static int k = 10;
	
	public static void preperarDados(){
		
		try {
			/*BufferedReader br = new BufferedReader(new FileReader("data/ratings_data.txt"));
			BufferedWriter bwTeste = new BufferedWriter(new FileWriter("data/ratings_data_teste5.txt"));
			BufferedWriter bwTreino = new BufferedWriter(new FileWriter("data/ratings_data_treinamento5.txt"));
			
			String line = "";
			int contador = 0;
			String valorAnterior = "";
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				
				if(!values[0].equals(valorAnterior)){
					contador++;
					if(contador == 5){
						bwTeste.write(values[0] + " " + values[1] + " " + values[2] + "\n");
						valorAnterior = values[0];
						contador =0;
					}else{
						bwTreino.write(values[0] + " " + values[1] + " " + values[2] + "\n");
					}
					
				}else{
					bwTreino.write(values[0] + " " + values[1] + " " + values[2] + "\n");
					valorAnterior = values[0];
				}
				
				//valorAnterior = values[0];
			}*/
			
			BufferedReader bwTreinoLeitor = new BufferedReader(new FileReader("data/ratings_data_treinamento2.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("data/ratings_treinamento2.cvs"));
			
			String line1 = "";
			while((line1 = bwTreinoLeitor.readLine()) != null){
				String[] values = line1.split(" ");
				bw.write(values[0] + "," + values[1] + "," + values[2] + "\n");
			}
			
			bwTreinoLeitor.close();
			bw.close();
			
			
			/*br.close();
			bwTreino.close();
			bwTeste.close();
			*/
		}catch(IOException e){
			
		}
	}

	public static SparseMatrix getMatrizR(){
		BufferedReader br;
		SparseMatrix mR = null;
		try {
			
		br = new BufferedReader(new FileReader("data/ratings_data_treinamento.txt"));
		
		//BufferedWriter bwTreino = new BufferedWriter(new FileWriter("data/treino.txt"));
		
		String line = "";
		mR = new SparseMatrix(49290, 139738);
		
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				mR.setQuick(Integer.parseInt(values[0])-1, Integer.parseInt(values[1])-1, new Double(values[2]));	
			}
			
			
			/*for(int i = 0; i < 49290; i ++){
				String linha = "";
				for(int j = 0; j < 139738; j++){
					linha += mR.get(i, j) + " ";
				}
				linha += "\n";	
				bwTreino.write(linha);
			}
			
			bwTreino.close();*/
			
			br.close();
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return mR;
		
	}
	
	
	public static DenseMatrix getMatrizP(){
		DenseMatrix mP = null;
		
		Random gerador = new Random();
		
		mP = new DenseMatrix(49290, k);
		
		for(int i = 0; i < mP.numRows(); i++){
			for(int j = 0; j < mP.numCols(); j++){
				if(gerador.nextInt(2) == 0 ){
					mP.setQuick(i, j, gerador.nextInt(6));
				}
			}
		}
		
		return mP;
	}
	
	public static DenseMatrix getMatrizQ(){
		DenseMatrix mQ = null;		
		
		Random gerador = new Random();		
		//139738
		
		mQ = new DenseMatrix(139738, k);
		
		for(int i = 0; i < mQ.numRows(); i++){
			for(int j = 0; j < mQ.numCols(); j++){
				if(gerador.nextInt(2) == 0 ){
					mQ.setQuick(i, j, gerador.nextInt(6));
				}
			}
		}
		return mQ;
	}
	
	
	
	public static ArrayList<DenseMatrix> fatoracao(SparseMatrix mR, DenseMatrix mP, DenseMatrix mQ, int iteracoes, double txA, double txB){
		DenseMatrix tranQ = (DenseMatrix) mQ.transpose();
		DenseMatrix p = mP;
		ArrayList<DenseMatrix> lista = new ArrayList<DenseMatrix>();
		
		for(int n = 0; n < iteracoes; n++){
			for(int i = 0; i < mR.numRows(); i++){
				
				for(int j =0; j < mR.numCols(); j++){
					if(mR.getQuick(i, j) > 0){
						
						double erroij = mR.getQuick(i, j) - p.viewRow(i).dot(tranQ.viewColumn(j));
							
							Vector temp1p = tranQ.viewColumn(j).times(2*erroij);
							
							Vector temp2p = p.viewRow(i).times(txB);
							
							Vector temp3p = temp1p.minus(temp2p);
							
							p.assignRow(i, p.viewRow(i).plus(temp3p.times(txA)));
							
							Vector temp1q = p.viewRow(i).times(2*erroij);
							
							Vector temp2q = tranQ.viewColumn(j).times(txB);
							
							Vector temp3q = temp1q.minus(temp2q);
							
							tranQ.assignColumn(j, tranQ.viewColumn(j).plus(temp3q.times(txA)));
					}						
				}
			}
			
//			System.gc();
//			//SparseMatrix mRpred = (SparseMatrix) p.times(tranQ);	
//			double erroPredicao = 0;
//			for(int i = 0; i < mR.numRows(); i++){
//				for(int j =0; j < mR.numCols(); j++){
//					if(mR.getQuick(i, j) > 0){
//						erroPredicao = erroPredicao + Math.pow(mR.getQuick(i, j) - p.viewRow(i).dot(tranQ.viewColumn(j)),2);
//						for(int k =0; k < p.numCols(); k++){
//							erroPredicao = erroPredicao + (txB/2.0)*(Math.pow(p.get(i, k),2) + Math.pow(tranQ.get(k, j),2));
//						}
//					}
//				}
//			}
//			System.out.println("Erro: "+erroPredicao);
//			
//			if(erroPredicao < 0.001){
//				break;
//			}	
					
		}
		
		System.gc();
		//SparseMatrix mRpred = (SparseMatrix) p.times(tranQ);	
		double erroPredicao = 0;
		for(int i = 0; i < mR.numRows(); i++){
			for(int j =0; j < mR.numCols(); j++){
				if(mR.getQuick(i, j) > 0){
					erroPredicao = erroPredicao + Math.pow(mR.getQuick(i, j) - p.viewRow(i).dot(tranQ.viewColumn(j)),2);
					for(int k =0; k < p.numCols(); k++){
						erroPredicao = erroPredicao + (txB/2.0)*(Math.pow(p.get(i, k),2) + Math.pow(tranQ.get(k, j),2));
					}
				}
			}
		}
		System.out.println("Erro: "+erroPredicao);
		
		lista.add(p);
		lista.add(tranQ);
		return lista;
			
	}
	
	public static void main(String[] args) {
		//System.out.println(getMatrizR().toString());
		//System.out.println(getMatrizQ().toString());
		//System.out.println(getMatrizQ().toString());
		
		//http://sifter.org/~simon/journal/20061211.html
		
		//preperarDados();
		
		SparseMatrix mR = getMatrizR();
		//0.002
		ArrayList<DenseMatrix> lista = fatoracao(getMatrizR(), getMatrizP(), getMatrizQ(), 500, 0.002, 0.01);
		DenseMatrix p = lista.get(1);
		DenseMatrix tranQ = lista.get(2);
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/ratings_data_teste.txt"));
			String line = "";	
			double erro = 0;
			String usuario = null;
			String item = null;
			String nota = null;
			double contadorPontos = 0.0;
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				usuario = values[0];
				item = values[1];
				nota = values[2];
				
				double notaPredita = p.viewRow(Integer.valueOf(usuario)).dot(tranQ.viewColumn(Integer.valueOf(item)));				
				erro += Math.pow((Double.valueOf(nota) - notaPredita),2);
				contadorPontos++;
			}
			erro = Math.sqrt(erro/contadorPontos);
			System.out.println("Taxa de erro - RMSE: " + erro);
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//varrer partindo da matriz de testes
		//mR.
	}
}
