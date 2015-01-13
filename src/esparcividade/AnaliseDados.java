package esparcividade;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.RatingSGDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;


//explicação do SGD mahout
//https://mahout.apache.org/users/recommender/matrix-factorization.html
public class AnaliseDados {
	
    public static void main(String[] args) {
        try {
        	
            //DataModel dataModel = new FileDataModel(new File("C:\\Users\\gabriel\\git\\mestrado\\data\\ratings_treinamento1.cvs"));
    		DataModel dataModel = new FileDataModel(new File("C:\\Users\\gabriel\\Desktop\\dados_epinions_alchemy\\ratings_novo.cvs"));
            DataModel dataModelTrust = new FileDataModel(new File("C:\\Users\\gabriel\\Desktop\\trust.cvs"));            
                    
            try {
            		int contador1 = 0;
            		int contador2 = 0;
            		int contador3 =0;
            		int contador4 =0;
            		int contador5 =0;
            		int contador6 =0;
            		int contador7 =0;
            		int contador8 =0;
            		int contador9 =0;
            		int contador10 =0;
            		int contador11 = 0;
            		
            		int cont1 =0;
            		int cont2 =0;
            		int cont3 =0;
            		int cont4 =0;
            		int cont5 =0;
            		int cont6 =0;
            		int cont7 =0;
            		
            		int numUsuario = 0;
            		LongPrimitiveIterator idUsuarios = dataModel.getUserIDs();
            		while(idUsuarios.hasNext()){
            			numUsuario++;
            			Long usuario = idUsuarios.next();
            			FastIDSet conjItens = dataModel.getItemIDsFromUser(usuario);
            			LongPrimitiveIterator idItens = conjItens.iterator();
            			while(idItens.hasNext()){
            				Long item = idItens.next();
            				float valor = dataModel.getPreferenceValue(usuario, item);
            				if(valor > 0){
            					int qtdAvaliacoes = dataModel.getPreferencesFromUser(usuario).length();
            					
//            					if(qtdAvaliacoes <= 10){
//                            		contador1++;
//                            	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
//                            		contador2++;
//                            	}else if(qtdAvaliacoes > 100){
//                            		contador3++;
//                            	} 
            					
            					
            					if(qtdAvaliacoes <= 10){
                            		contador1++;
                            	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 20){
                            		contador2++;
                            	}else if(qtdAvaliacoes > 20 && qtdAvaliacoes <= 30){
                            		contador3++;
                            	}else if(qtdAvaliacoes > 30 && qtdAvaliacoes <= 40){
                            		contador4++;
                            	}else if(qtdAvaliacoes > 40 && qtdAvaliacoes <= 50){
                            		contador5++;
                            	}else if(qtdAvaliacoes > 50 && qtdAvaliacoes <= 60){
                            		contador6++;
                            	} else if(qtdAvaliacoes > 60 && qtdAvaliacoes <= 70){
                            		contador7++;
                            	}else if(qtdAvaliacoes > 70 && qtdAvaliacoes <= 80){
                            		contador8++;
                            	}else if(qtdAvaliacoes > 80 && qtdAvaliacoes <= 90){
                            		contador9++;
                            	}else if(qtdAvaliacoes > 90 && qtdAvaliacoes <= 100){
                            		contador10++;
                            	}else if(qtdAvaliacoes > 100){ 
                            		contador11++;
                            	} 
            					
            					int base = 2;
            					if((Math.log(qtdAvaliacoes)/Math.log(base)) <= 1){
            						cont1++;
            					}else if((Math.log(qtdAvaliacoes)/Math.log(base)) <= 2){
            						cont2++;
            					}else if((Math.log(qtdAvaliacoes)/Math.log(base)) <= 3){
            						cont3++;
            					}else if((Math.log(qtdAvaliacoes)/Math.log(base)) <= 4){
            						cont4++;
            					}else if((Math.log(qtdAvaliacoes)/Math.log(base)) <= 5){
            						cont5++;
            					}else if((Math.log(qtdAvaliacoes)/Math.log(base)) <= 6){
            						cont6++;
            					}else if((Math.log(qtdAvaliacoes)/Math.log(base)) <= 7){
            						cont7++;
            					}
}
            			}
            		}
            		
            		System.out.println("QUANTIDADE DE USUARIOS " + numUsuario);
            		System.out.println("QUANTIDADE DE AVALIAÇÕES 1 " + contador1);
            		System.out.println("QUANTIDADE DE AVALIAÇÕES 2 " + contador2);
            		System.out.println("QUANTIDADE DE AVALIAÇÕES 3 " + contador3);
            		
            		System.out.println(contador1);
            		System.out.println(contador2);
            		System.out.println(contador3);
            		System.out.println(contador4);
            		System.out.println(contador5);
            		System.out.println(contador6);
            		System.out.println(contador7);
            		System.out.println(contador8);
            		System.out.println(contador9);
            		System.out.println(contador10);
            		System.out.println(contador11);
            		System.out.println("------------");
            		System.out.println(cont1);
            		System.out.println(cont2);
            		System.out.println(cont3);
            		System.out.println(cont4);
            		System.out.println(cont5);
            		System.out.println(cont6);
            		System.out.println(cont7);
            		
            	
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