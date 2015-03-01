package esparcividade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.RatingSGDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;

public class Matriz {

	
	public static void main(String[] args) {
		
		try{
		DataModel dataModeltrain = new FileDataModel(new File("C:\\Users\\gabriel\\Desktop\\conjuntoDados\\PAOLO\\train3.arff"));
                
                RatingSGDFactorizer factorizerPadrao = new RatingSGDFactorizer(dataModeltrain,10, 0.0002 , 0.02,0.01, 1000,1.0);
                SVDRecommender rec = new SVDRecommender(dataModeltrain, factorizerPadrao);

                try {
                    //BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\gabriel\\git\\mestrado\\data\\ratings_data.txt"));
                    
                	BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\gabriel\\Desktop\\conjuntoDados\\PAOLO\\test3.arff"));
                    String line = "";   
                    String usuario = null;
                    String item = null;
                    String nota = null;
                    double notaPredita =0;
                    double sum_maesMatriz =0;
                    double contadorPontosM = 0;
                    double contadorPontosFora = 0;
                    while((line = br.readLine()) != null){
                        String[] values = line.split(",");
                        usuario = values[0];
                        item = values[1];
                        nota = values[2];
                        int i = Integer.parseInt(usuario);
                        int j = Integer.parseInt(item); 
                        try{
	                        notaPredita = rec.estimatePreference(i, j);
	                        notaPredita = Math.round(notaPredita);
	                    	if(notaPredita < 1){
	                    		notaPredita = 1;
	                    		notaPredita = 1;
	                    	}
	                    	
	                    	if(notaPredita > 5){
	                    		notaPredita = 5;
	                    		notaPredita = 5;
	                    	}
	                    	
	                    	double erroMatriz = Double.parseDouble(nota) - notaPredita;
	                    	sum_maesMatriz += Math.abs(erroMatriz);
	                    	contadorPontosM++;
                        }catch(TasteException e){
                        	contadorPontosFora++;
                        }
                    }
                    
                    System.out.println(" mae "+sum_maesMatriz/contadorPontosM + " qtd "+contadorPontosM + " qtd fora "+contadorPontosFora);
                }catch(Exception e){
                	
                }
		}catch(Exception e){
			
		}
                    
	}
}
