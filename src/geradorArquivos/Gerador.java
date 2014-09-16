package geradorArquivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Gerador {
	
	public static int k = 10;
	
	public static void preperarDados(){
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/ratings_data.txt"));
			BufferedWriter bwTeste = new BufferedWriter(new FileWriter("data/dados10/ratings_data_teste1temp.txt"));
			BufferedWriter bwTreino = new BufferedWriter(new FileWriter("data/dados10/ratings_data_treinamento1temp.txt"));			
			
			int quantidadeLinhas = 0;
			String line = "";
			int contador = 0;
			String valorAnterior = "";
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				
				if(!values[0].equals(valorAnterior)){
					contador++;
					if(contador == 1){
						bwTeste.write(values[0] + " " + values[1] + " " + values[2] + "\n");
						valorAnterior = values[0];
						contador =0;
						quantidadeLinhas++;
					}else{
						bwTreino.write(values[0] + " " + values[1] + " " + values[2] + "\n");
					}
					
				}else{
					bwTreino.write(values[0] + " " + values[1] + " " + values[2] + "\n");
					valorAnterior = values[0];
				}			
				
			}
			
			br.close();
			bwTreino.close();
			bwTeste.close();
			
			if(quantidadeLinhas < 660000){
				//br = new BufferedReader(new FileReader("data/ratings_data.txt"));
				
				br = new BufferedReader(new FileReader("data/dados10/ratings_data_treinamento1temp.txt"));
				bwTeste = new BufferedWriter(new FileWriter("data/dados10/ratings_data_teste1.txt"));
				bwTreino = new BufferedWriter(new FileWriter("data/dados10/ratings_data_treinamento1.txt"));
				
				while((line = br.readLine()) != null ){
					String[] values = line.split(" ");
					
					if(!values[0].equals(valorAnterior) && quantidadeLinhas < 66000){
						contador++;
						if(contador == 1){
							bwTeste.write(values[0] + " " + values[1] + " " + values[2] + "\n");
							valorAnterior = values[0];
							contador =0;
							quantidadeLinhas++;
						}else{
							bwTreino.write(values[0] + " " + values[1] + " " + values[2] + "\n");
						}
						
					}else{
						bwTreino.write(values[0] + " " + values[1] + " " + values[2] + "\n");
						valorAnterior = values[0];
					}			
					
				}
				
			}
			

//			BufferedReader brTeste = new BufferedReader(new FileReader("data/dados10/ratings_data_teste1temp.txt"));
//			
//			while((line = br.readLine()) != null){
//				String[] values = line.split(" ");
//			
//			
//			}
				
			br.close();
			bwTreino.close();
			bwTeste.close();
		}catch(IOException e){
			
		}
	}
	
	public static void arquivoCVS(){
		try {
			BufferedReader bwTreinoLeitor = new BufferedReader(new FileReader("data/dados10/ratings_data_treinamento1.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("data/dados10/ratings_treinamento1.cvs"));
			
			String line1 = "";
			
			while((line1 = bwTreinoLeitor.readLine()) != null){
				String[] values = line1.split(" ");
				bw.write(values[0] + "," + values[1] + "," + values[2] + "\n");
			}
			
			
			bwTreinoLeitor.close();
			bw.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		Gerador.preperarDados();
	}
}
