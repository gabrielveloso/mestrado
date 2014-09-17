package geradorArquivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.math.function.LongProcedure;

public class AnaliseDados {

	
	public static void main(String[] args) {
		 try {
		        

	            DataModel dataModel = new FileDataModel(new File("data/ratings_treinamento.cvs"));
	            DataModel dataModelTrust = new FileDataModel(new File("data/trust.cvs"));	            
	                    
	            try {	            	
	            	
	            	float numeroMaximoUserTrust = 0;
	            	float numeroMaximoAval = 0;	            	
	            	
	            	LongPrimitiveIterator iterator2 =  dataModel.getUserIDs();
	            	while(iterator2.hasNext()){
	            		long id =  iterator2.next();
	            		int numero  = dataModel.getPreferencesFromUser((int)id).length();
	            		if(numero > numeroMaximoAval){
	            			numeroMaximoAval = numero;                	
	                	}
	            	}
	            	
	            	LongPrimitiveIterator iterator =  dataModelTrust.getUserIDs();
	            	while(iterator.hasNext()){
	            		long id =  iterator.next();
	            		int numero  = dataModelTrust.getPreferencesFromUser((int)id).length();
	            		if(numero > numeroMaximoUserTrust){
	                		numeroMaximoUserTrust = numero;                	
	                	}
	            	}
	            	BufferedReader br = new BufferedReader(new FileReader("data/ratings_data.txt"));
	            	BufferedWriter bwTeste = new BufferedWriter(new FileWriter("data/dados10/dadosUsuarios.txt"));
	            	
                    LongPrimitiveIterator temp = dataModel.getUserIDs();                    
                    
                    while(temp.hasNext()){
                    	long atual = temp.next();
    
                    	int usuariosConfiaveis = 0;
                        int numeroAvaliacoes = dataModel.getItemIDsFromUser(atual).size();
                        try{
                        	PreferenceArray userLinha = dataModelTrust.getPreferencesFromUser(atual);
                        	usuariosConfiaveis = userLinha.length();
                    	}catch(Exception e) {
                    	}
                        
                        float medidaTrust = (float)usuariosConfiaveis/(float)numeroMaximoUserTrust;                        
                        float medidaMatriz = (float)numeroAvaliacoes/(float)numeroMaximoAval;
                        
                        //System.out.println(atual +" " +usuariosConfiaveis+" "+numeroAvaliacoes +" "+ medidaTrust+" " + medidaMatriz);
                        bwTeste.write(atual +" " +usuariosConfiaveis+" "+numeroAvaliacoes +" "+ medidaTrust+" " + medidaMatriz +"\n");
	            	}
	            	bwTeste.close();
                    br.close();
                    
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	}
}
