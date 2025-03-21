import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.RatingSGDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;



public class Mestrado {
    
    public static int numeroUsuarios = 0;
    
    public static float calcularProdutoTrustComR(int linha, int coluna, DataModel dataModelTrust, SVDRecommender rec){
        float resultado = 0;
        numeroUsuarios = 0;
        try {
                  
                        //obtendo uma linha da matriz de confian�a
            PreferenceArray userLinha = dataModelTrust.getPreferencesFromUser(linha);

                        //percorre cada elemento da linha da matriz de confian�a
                        //multimplicando com os elementos da coluna da matriz R fatorada
            for(Iterator<Preference> pref = userLinha.iterator(); pref.hasNext();){
                Preference preference = pref.next();
                long id = preference.getItemID();
                float valorTrust = preference.getValue(); 
                
                float produto = 0;
                try{
                    produto = valorTrust*rec.estimatePreference(id, coluna);
                    numeroUsuarios++;
                } catch (TasteException e1) {
                    //System.out.println("NÃO É POSSIVEL PREDIZER A PREFERENCIA DO USUARIO " + id + " " + coluna);
                }
                resultado += produto;
                
            }
            
            if(numeroUsuarios != 0){
                resultado = resultado/numeroUsuarios;
                //System.out.print(numeroUsuarios);
            }
            
        } catch (TasteException e1) {
            //e1.printStackTrace();
            //System.out.println("O USUARIO " + linha + " NÃO EXPRESSOU CONFIANÇA");
        }
        
        return resultado;
    }
    
    
    public static float calcularProdutoTrustFatoradaComR(int linha, int coluna, SVDRecommender recTrust, SVDRecommender rec){
        float resultado = 0;
        numeroUsuarios = 0;
        try {
            
            for(int i = 0; i < recTrust.getDataModel().getNumItems(); i++){
            	
            	 try{
            		float linhaTrust = recTrust.estimatePreference(linha, i);
	            	float colunaR = rec.estimatePreference(i, coluna);
	            	numeroUsuarios++;
	            	resultado = resultado + linhaTrust*colunaR;
            	 } catch (TasteException e1) {
                     //System.out.println("NÃO É POSSIVEL PREDIZER A PREFERENCIA DO USUARIO " + id + " " + coluna);
                 }
            }
            
            if(numeroUsuarios != 0){
                resultado = resultado/numeroUsuarios;
                //System.out.print(numeroUsuarios);
            }
            
        } catch (TasteException e1) {
            //e1.printStackTrace();
            //System.out.println("O USUARIO " + linha + " NÃO EXPRESSOU CONFIANÇA");
        }
        
        return resultado;
    }
    
    public static void main(String[] args) {
        try {       

            DataModel dataModel = new FileDataModel(new File("data/ratings_treinamento1.cvs"));
            DataModel dataModelTrust = new FileDataModel(new File("data/trust.cvs"));
            //double alfa = 0.8;
            
            try {
                
                int maxUsuariosConfiaveis = 0;
                int mediaUsuariosConfiaveis = 0;
                for(LongPrimitiveIterator it = dataModelTrust.getUserIDs(); it.hasNext();){
                    Long idUsuer = it.next();
                    
                    PreferenceArray userLinha = dataModelTrust.getPreferencesFromUser(idUsuer);
                    int usuariosConfiaveis = userLinha.length();
                    mediaUsuariosConfiaveis=mediaUsuariosConfiaveis+usuariosConfiaveis;
                    if(usuariosConfiaveis > maxUsuariosConfiaveis){
                        maxUsuariosConfiaveis = usuariosConfiaveis;
                    }
                }
                mediaUsuariosConfiaveis = mediaUsuariosConfiaveis/dataModelTrust.getNumUsers();
                
                int maxAvalUsuario = 0;
                        
                for(LongPrimitiveIterator it = dataModel.getUserIDs(); it.hasNext();){
                    Long idUsuer = it.next();
                    
                    PreferenceArray userLinha = dataModel.getPreferencesFromUser(idUsuer);
                    int avalUsuario = userLinha.length();
                   
                    if(avalUsuario > maxAvalUsuario){
                        maxAvalUsuario = avalUsuario;
                    }
                }
                    
                RatingSGDFactorizer factorizerPadrao = new RatingSGDFactorizer(dataModel,5, 0.0002 , 0.02,0.01, 1000,1.0);
                SVDRecommender rec = new SVDRecommender(dataModel, factorizerPadrao);
                
                RatingSGDFactorizer factorizerPadraoTrust = new RatingSGDFactorizer(dataModelTrust,5, 0.0002 , 0.02,0.01, 1000,1.0);
                SVDRecommender recTrust = new SVDRecommender(dataModelTrust, factorizerPadraoTrust);

                    try {
                        BufferedReader br = new BufferedReader(new FileReader("data/ratings_data_teste1.txt"));
                        String line = "";                      
                        double erroMatriz = 0;  
                        double erroMatrizTrust2 = 0;
                        double erroMatrizTrust3 = 0;
                        double erroMatrizTrust4 = 0;
                        double erroMatrizTrust5 = 0;
                        double erroMatrizTrust6 = 0;
                        String usuario = null;
                        String item = null;
                        String nota = null;
                        double contadorPontos = 0.0;
                        int numeroUsuariosMenor =0;
                        while((line = br.readLine()) != null){
                            String[] values = line.split(" ");
                            usuario = values[0];
                            item = values[1];
                            nota = values[2];
                            int i = Integer.parseInt(usuario);
                            int j = Integer.parseInt(item);
                            
                            try{                                
                                
                                int numeroAvaliacoes = dataModel.getItemIDsFromUser(i).size();
                                PreferenceArray userLinha = dataModelTrust.getPreferencesFromUser(i);
                                int usuariosConfiaveis = userLinha.length();
                                float medidaTrust = (float) usuariosConfiaveis/ 135;                                
                                float medidaRatings = (float) numeroAvaliacoes/135;
                                
                                double notaPreditaMatriz = rec.estimatePreference(i, j);
                                double produto = calcularProdutoTrustComR(i, j, dataModelTrust, rec);
                                
                                double produtoTrustFatorada = calcularProdutoTrustFatoradaComR(i, j, recTrust, rec);
                                
                                double notaPreditaMatrizTrust2 = 0;
                                
                                medidaRatings = (float) numeroAvaliacoes/maxAvalUsuario;
                                medidaTrust = (float) usuariosConfiaveis/maxUsuariosConfiaveis;
                                
                                if(produto == 0){
                                    notaPreditaMatrizTrust2 = notaPreditaMatriz;
                                }else{
                                    notaPreditaMatrizTrust2 = (0.7)*notaPreditaMatriz + (0.3)*produto;                               
                                }
                                
                                double notaPreditaMatrizTrust3 = 0;
                                
                                medidaRatings = (float) numeroAvaliacoes/maxAvalUsuario;
                                medidaTrust = (float) usuariosConfiaveis/maxUsuariosConfiaveis;
                                
                                if(produto == 0){
                                	notaPreditaMatrizTrust3 = notaPreditaMatriz;
                                }else{
                                	notaPreditaMatrizTrust3 = (0.7)*notaPreditaMatriz + (0.3)*produtoTrustFatorada;                               
                                }
                                
//                                double notaPreditaMatrizTrust3 = 0;
//                                
//                                medidaRatings = (float) numeroAvaliacoes/maxAvalUsuario;
//                                medidaTrust = (float) usuariosConfiaveis/maxUsuariosConfiaveis;
//                                
//                                if(produto == 0){
//                                    notaPreditaMatrizTrust3 = notaPreditaMatriz;
//                                }else{
//                                    notaPreditaMatrizTrust3 = (0.7+medidaRatings-medidaTrust)*notaPreditaMatriz + (1-(0.7+medidaRatings) + medidaTrust)*produto;                               
//                                }                                
                                
                                double notaPreditaMatrizTrust4 = 0;
                                if(produto == 0){
                                    notaPreditaMatrizTrust4 = notaPreditaMatriz;
                                }else{
                                    notaPreditaMatrizTrust4 = (0.7-medidaRatings)*notaPreditaMatriz + (1-(0.7-medidaRatings))*produto;                               
                                }                                
                                
                                double notaPreditaMatrizTrust5 = 0;
                                if(produto == 0){
                                    notaPreditaMatrizTrust5 = notaPreditaMatriz;
                                }else{
                                    notaPreditaMatrizTrust5 = (0.7-medidaTrust)*notaPreditaMatriz + (1-(0.7 - medidaTrust))*produto;                               
                                }
                                
                                double notaPreditaMatrizTrust6 = 0;
                                if(produto == 0){
                                	notaPreditaMatrizTrust6 = notaPreditaMatriz;
                                }else{
                                	notaPreditaMatrizTrust6 = (1-medidaRatings)*notaPreditaMatriz + (medidaRatings)*produto;                               
                                }
                                
//                                if(numeroAvaliacoes > 50){
//                                    notaPreditaTrust = 0.9*notaPreditaMatriz 
//                                            + (1-0.9)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
//                                }else if(usuariosConfiaveis > 50 && numeroAvaliacoes < 20){
//                                    notaPreditaTrust = 0.3*notaPreditaMatriz 
//                                            + (1-0.3)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
//                                }else if(numeroAvaliacoes < 15 && usuariosConfiaveis > 15){
//                                    notaPreditaTrust = 0.3*notaPreditaMatriz 
//                                            + (1-0.3)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
//                                }else{
//                                    notaPreditaTrust = rec.estimatePreference(i, j);
//                                }
                                
                                erroMatriz += Math.pow((Double.valueOf(nota) - notaPreditaMatriz),2);   
                                
                                erroMatrizTrust2 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust2),2);
                                
                                erroMatrizTrust3 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust3),2);
                                
                                erroMatrizTrust4 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust4),2);
                                
                                erroMatrizTrust5 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust5),2);
                                
                                erroMatrizTrust6 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust6),2);
                                
                                
                                contadorPontos++;
                                
                            } catch (TasteException e1) {
                                //System.out.println("NÃO FOI POSSIVEL PREDIZER PREFERENCIA " + i + " " + j);
                            }
                        }
                        
                        erroMatriz = Math.sqrt(erroMatriz/contadorPontos);
                        erroMatrizTrust2 = Math.sqrt(erroMatrizTrust2/contadorPontos);
                        erroMatrizTrust3 = Math.sqrt(erroMatrizTrust3/contadorPontos);
                        erroMatrizTrust4 = Math.sqrt(erroMatrizTrust4/contadorPontos);
                        erroMatrizTrust5 = Math.sqrt(erroMatrizTrust5/contadorPontos);
                        erroMatrizTrust6 = Math.sqrt(erroMatrizTrust6/contadorPontos);
                        
                        
                        System.out.println("Taxa de erro - RMSE - Apenas Matriz: " + erroMatriz);  
                        System.out.println("Taxa de erro - RMSE - com alfa igual a 0.7: " + erroMatrizTrust3);
                        System.out.println("Taxa de erro - RMSE - utilizando medidaRatings e mediaTrust: " + erroMatrizTrust3); 
                        System.out.println("Taxa de erro - RMSE - utilzando medidaRatings: " + erroMatrizTrust4);   
                        System.out.println("Taxa de erro - RMSE - utilziando medidaTrust: " + erroMatrizTrust5);   
                        System.out.println("Taxa de erro - RMSE - utilziando medidaratings sem 0.7: " + erroMatrizTrust6);  
                        System.out.println(contadorPontos);
                        System.out.println(numeroUsuariosMenor);
                        br.close();
                    }catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                                        }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        try {       

            DataModel dataModel = new FileDataModel(new File("data/ratings_treinamento5.cvs"));
            DataModel dataModelTrust = new FileDataModel(new File("data/trust.cvs"));
            //double alfa = 0.8;
            
            try {
                
                int maxUsuariosConfiaveis = 0;
                int mediaUsuariosConfiaveis = 0;
                for(LongPrimitiveIterator it = dataModelTrust.getUserIDs(); it.hasNext();){
                    Long idUsuer = it.next();
                    
                    PreferenceArray userLinha = dataModelTrust.getPreferencesFromUser(idUsuer);
                    int usuariosConfiaveis = userLinha.length();
                    mediaUsuariosConfiaveis=mediaUsuariosConfiaveis+usuariosConfiaveis;
                    if(usuariosConfiaveis > maxUsuariosConfiaveis){
                        maxUsuariosConfiaveis = usuariosConfiaveis;
                    }
                }
                mediaUsuariosConfiaveis = mediaUsuariosConfiaveis/dataModelTrust.getNumUsers();
                
                int maxAvalUsuario = 0;
                        
                for(LongPrimitiveIterator it = dataModel.getUserIDs(); it.hasNext();){
                    Long idUsuer = it.next();
                    
                    PreferenceArray userLinha = dataModel.getPreferencesFromUser(idUsuer);
                    int avalUsuario = userLinha.length();
                   
                    if(avalUsuario > maxAvalUsuario){
                        maxAvalUsuario = avalUsuario;
                    }
                }
                    
                    RatingSGDFactorizer factorizerPadrao = new RatingSGDFactorizer(dataModel,5, 0.0002 , 0.02,0.01, 1000,1.0);
                    SVDRecommender rec = new SVDRecommender(dataModel, factorizerPadrao);

                    try {
                        BufferedReader br = new BufferedReader(new FileReader("data/ratings_data_teste5.txt"));
                        String line = "";                      
                        double erroMatriz = 0;
                        double erroMatrizTrust2 = 0;                      
                        double erroMatrizTrust3 = 0;
                        double erroMatrizTrust4 = 0;
                        double erroMatrizTrust5 = 0;
                        double erroMatrizTrust6 = 0;
                        String usuario = null;
                        String item = null;
                        String nota = null;
                        double contadorPontos = 0.0;
                        int numeroUsuariosMenor =0;
                        while((line = br.readLine()) != null){
                            String[] values = line.split(" ");
                            usuario = values[0];
                            item = values[1];
                            nota = values[2];
                            int i = Integer.parseInt(usuario);
                            int j = Integer.parseInt(item);
                            
                            
                            
                            try{
                                
                                
                                int numeroAvaliacoes = dataModel.getItemIDsFromUser(i).size();
                                PreferenceArray userLinha = dataModelTrust.getPreferencesFromUser(i);
                                int usuariosConfiaveis = userLinha.length();
                                float medidaTrust = (float) usuariosConfiaveis/ 135;                                
                                float medidaRatings = (float) numeroAvaliacoes/135;
                                
                                double notaPreditaMatriz = rec.estimatePreference(i, j);
                                double produto = calcularProdutoTrustComR(i, j, dataModelTrust, rec);                                
                                
                                medidaRatings = (float) numeroAvaliacoes/maxAvalUsuario;
                                medidaTrust = (float) usuariosConfiaveis/maxUsuariosConfiaveis;
                                
                                double notaPreditaMatrizTrust2 = 0;
                                
                                if(produto == 0){
                                    notaPreditaMatrizTrust2 = notaPreditaMatriz;
                                }else{
                                    notaPreditaMatrizTrust2 = (0.7)*notaPreditaMatriz + (0.3)*produto;                               
                                }
                                
                                double notaPreditaMatrizTrust3 = 0;
                                
                                medidaRatings = (float) numeroAvaliacoes/maxAvalUsuario;
                                medidaTrust = (float) usuariosConfiaveis/maxUsuariosConfiaveis;
                                
                                if(produto == 0){
                                    notaPreditaMatrizTrust3 = notaPreditaMatriz;
                                }else{
                                    notaPreditaMatrizTrust3 = (0.7+medidaRatings-medidaTrust)*notaPreditaMatriz + (1-(0.7+medidaRatings) + medidaTrust)*produto;                               
                                }
                                
                                
                                double notaPreditaMatrizTrust4 = 0;
                                if(produto == 0){
                                    notaPreditaMatrizTrust4 = notaPreditaMatriz;
                                }else{
                                    notaPreditaMatrizTrust4 = (0.7-medidaRatings)*notaPreditaMatriz + (1-(0.7-medidaRatings))*produto;                               
                                }
                                
                                
                                double notaPreditaMatrizTrust5 = 0;
                                if(produto == 0){
                                    notaPreditaMatrizTrust5 = notaPreditaMatriz;
                                }else{
                                    notaPreditaMatrizTrust5 = (0.7-medidaTrust)*notaPreditaMatriz + (1-(0.7 - medidaTrust))*produto;                               
                                }
                                
                                double notaPreditaMatrizTrust6 = 0;
                                if(produto == 0){
                                	notaPreditaMatrizTrust6 = notaPreditaMatriz;
                                }else{
                                	notaPreditaMatrizTrust6 = (1-medidaRatings)*notaPreditaMatriz + (medidaRatings)*produto;                               
                                }
                                
//                                if(numeroAvaliacoes > 50){
//                                    notaPreditaTrust = 0.9*notaPreditaMatriz 
//                                            + (1-0.9)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
//                                }else if(usuariosConfiaveis > 50 && numeroAvaliacoes < 20){
//                                    notaPreditaTrust = 0.3*notaPreditaMatriz 
//                                            + (1-0.3)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
//                                }else if(numeroAvaliacoes < 15 && usuariosConfiaveis > 15){
//                                    notaPreditaTrust = 0.3*notaPreditaMatriz 
//                                            + (1-0.3)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
//                                }else{
//                                    notaPreditaTrust = rec.estimatePreference(i, j);
//                                }
                                
                                erroMatriz += Math.pow((Double.valueOf(nota) - notaPreditaMatriz),2);   
                                
                                erroMatrizTrust2 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust2),2);
                                
                                erroMatrizTrust3 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust3),2);
                                
                                erroMatrizTrust4 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust4),2);
                                
                                erroMatrizTrust5 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust5),2);
                                
                                erroMatrizTrust6 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust6),2);
                                
                                
                                contadorPontos++;
                                
                            } catch (TasteException e1) {
                                //System.out.println("NÃO FOI POSSIVEL PREDIZER PREFERENCIA " + i + " " + j);
                            }
                        }
                        
                        erroMatriz = Math.sqrt(erroMatriz/contadorPontos);
                        erroMatrizTrust2 = Math.sqrt(erroMatrizTrust2/contadorPontos);
                        erroMatrizTrust3 = Math.sqrt(erroMatrizTrust3/contadorPontos);
                        erroMatrizTrust4 = Math.sqrt(erroMatrizTrust4/contadorPontos);
                        erroMatrizTrust5 = Math.sqrt(erroMatrizTrust5/contadorPontos);
                        erroMatrizTrust6 = Math.sqrt(erroMatrizTrust6/contadorPontos);
                        
                        
                        System.out.println("Taxa de erro - RMSE - Apenas Matriz: " + erroMatriz);      
                        System.out.println("Taxa de erro - RMSE - com alfa igual a 0.7: " + erroMatrizTrust2);
                        System.out.println("Taxa de erro - RMSE - utilizando medidaRatings e mediaTrust: " + erroMatrizTrust3); 
                        System.out.println("Taxa de erro - RMSE - utilzando medidaRatings: " + erroMatrizTrust4);   
                        System.out.println("Taxa de erro - RMSE - utilziando medidaTrust: " + erroMatrizTrust5);   
                        System.out.println("Taxa de erro - RMSE - utilziando medidaRating sem 0.7: " + erroMatrizTrust6);  
                        System.out.println(contadorPontos);
                        System.out.println(numeroUsuariosMenor);
                        br.close();
                    }catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                                        }

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