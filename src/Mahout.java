
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
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.RatingSGDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;

//explicação do SGD mahout
//https://mahout.apache.org/users/recommender/matrix-factorization.html
public class Mahout {
	
	

    public static float calcularProdutoTrustComR(int linha, int coluna, DataModel dataModelTrust, SVDRecommender rec){
        float resultado = 0;
        float numeroUsuarios = 0;
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
                	System.out.println("Usuario "+linha + "confia em " + userLinha.length());
                    System.out.println("NÃO É POSSIVEL PREDIZER A PREFERENCIA DO USUARIO " + id + " " + coluna);
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
    
    public static void main(String[] args) {
        try {
        

            DataModel dataModel = new FileDataModel(new File("data/ratings_treinamento.cvs"));
            DataModel dataModelTrust = new FileDataModel(new File("data/trust.cvs"));
            double alfa = 0.8;
            
            
                    
            try {
                
                    
                    RatingSGDFactorizer factorizerPadrao = new RatingSGDFactorizer(dataModel,5, 0.0002 , 0.02,0.01, 1000,1.0);
                    SVDRecommender rec = new SVDRecommender(dataModel, factorizerPadrao);

                    try {
                        BufferedReader br = new BufferedReader(new FileReader("data/ratings_data_teste.txt"));
                        String line = "";
                        double erroMatrizTrust5 = 0;
                        double erroMatrizTrust6 = 0;
                        double erroMatrizTrust7 = 0;
                        double erroMatrizTrust = 0;
                        double erroMatrizTrust1 = 0;
                        double erroMatrizTrust2 = 0;
                        double erroMatrizTrust3 = 0;
                        double erroMatrizTrust4 = 0;
                        double erroMatriz = 0;
                        double erroTrust = 0;
                        String usuario = null;
                        String item = null;
                        String nota = null;
                        double contadorPontos = 0.0;
                        double contadorAval = 0.0;
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
                                
                                
                                double notaPreditaMatriz = rec.estimatePreference(i, j);
                                
                                //System.out.print(numeroAvaliacoes+","+notaPreditaMatriz+",");
                                
                                double valorTrust = calcularProdutoTrustComR(i, j, dataModelTrust, rec);
                                
                                //System.out.println(","+produto+","+nota);
                                
                                double notaPreditaMatrizTrust5 = 0.1*notaPreditaMatriz 
                                        + (1-0.1)*valorTrust;
                                if(valorTrust == 0.0){
                                	notaPreditaMatrizTrust5 = notaPreditaMatriz;
                                }
                                
                                double notaPreditaMatrizTrust6 = 0.2*notaPreditaMatriz 
                                        + (1-0.2)*valorTrust;
                                if(valorTrust == 0.0){
                                	notaPreditaMatrizTrust6 = notaPreditaMatriz;
                                }
                                
                                double notaPreditaMatrizTrust7 = 0.3*notaPreditaMatriz 
                                        + (1-0.3)*valorTrust;
                                if(valorTrust == 0.0){
                                	notaPreditaMatrizTrust7 = notaPreditaMatriz;
                                }
                                
                                double notaPreditaMatrizTrust = 0.4*notaPreditaMatriz 
                                        + (1-0.4)*valorTrust;
                                if(valorTrust == 0.0){
                                	notaPreditaMatrizTrust = notaPreditaMatriz;
                                }
                                
                                double notaPreditaMatrizTrust1 = 0.5*notaPreditaMatriz 
                                        + (1-0.5)*valorTrust;
                                if(valorTrust == 0.0){
                                	notaPreditaMatrizTrust1 = notaPreditaMatriz;
                                }
                                
                                double notaPreditaMatrizTrust2 = 0.6*notaPreditaMatriz 
                                        + (1-0.6)*valorTrust;
                                if(valorTrust == 0.0){
                                	notaPreditaMatrizTrust2 = notaPreditaMatriz;
                                }
                                
                                double notaPreditaMatrizTrust3 = 0.7*notaPreditaMatriz 
                                        + (1-0.7)*valorTrust;
                                if(valorTrust == 0.0){
                                	notaPreditaMatrizTrust3 = notaPreditaMatriz;
                                }
                                
                                double notaPreditaMatrizTrust4 = 0.8*notaPreditaMatriz 
                                        + (1-0.8)*valorTrust;
                                if(valorTrust == 0.0){
                                	notaPreditaMatrizTrust3 = notaPreditaMatriz;
                                }
                                
                                
                                /*if(usuariosConfiaveis > 50 && numeroAvaliacoes < 20){
                                	notaPreditaMatrizTrust = 0.3*notaPreditaMatriz 
                                            + (1-0.3)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
                                }else{
                                	notaPreditaMatrizTrust = notaPreditaMatriz;
                                }*/
                                
                                
                                double notaPreditaTrust = 0;
                                
                                if(numeroAvaliacoes > 50){
                                	notaPreditaTrust = 0.9*notaPreditaMatriz 
                                            + (1-0.9)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);                                	
                                }else if(usuariosConfiaveis > 50 && numeroAvaliacoes < 20){
                                	notaPreditaTrust = 0.3*notaPreditaMatriz 
                                            + (1-0.3)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
                                }else if(numeroAvaliacoes < 15 && usuariosConfiaveis > 15){
                                	notaPreditaTrust = 0.3*notaPreditaMatriz 
                                            + (1-0.3)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
                                }else{
                                	notaPreditaTrust = rec.estimatePreference(i, j);
                                }
                                
                                if(valorTrust == 0.0){
                            		notaPreditaTrust = notaPreditaMatriz;
                                }
                                        
                                /*if(numeroAvaliacoes < 5 && usuariosConfiaveis > 20){
                                	erroMatrizTrust += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust),2);
                                	erroMatrizTrust1 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust1),2);
                                	contadorAval++;
                                }*/
                              	  
                                erroMatriz += Math.pow((Double.valueOf(nota) - notaPreditaMatriz),2);
                                
                                erroMatrizTrust5 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust5),2);
                                erroMatrizTrust6 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust6),2);
                                erroMatrizTrust7 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust7),2);
                                
                                erroMatrizTrust += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust),2);
                            	erroMatrizTrust1 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust1),2);
                                erroMatrizTrust2 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust2),2);
                                erroMatrizTrust3 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust3),2);
                                erroMatrizTrust4 += Math.pow((Double.valueOf(nota) - notaPreditaMatrizTrust4),2);
                                
                                erroTrust += Math.pow((Double.valueOf(nota) - notaPreditaTrust),2);
                                
                                contadorPontos++;
                                
                            } catch (TasteException e1) {
                                //System.out.println("NÃO FOI POSSIVEL PREDIZER PREFERENCIA " + i + " " + j);
                            }
                        }
                        
                        erroMatriz = Math.sqrt(erroMatriz/contadorPontos);
                        erroMatrizTrust5 = Math.sqrt(erroMatrizTrust5/contadorPontos);
                        erroMatrizTrust6 = Math.sqrt(erroMatrizTrust6/contadorPontos);
                        erroMatrizTrust7 = Math.sqrt(erroMatrizTrust7/contadorPontos);
                        erroMatrizTrust = Math.sqrt(erroMatrizTrust/contadorPontos);
                        erroMatrizTrust1 = Math.sqrt(erroMatrizTrust1/contadorPontos);
                        erroMatrizTrust2 = Math.sqrt(erroMatrizTrust2/contadorPontos);
                        erroMatrizTrust3 = Math.sqrt(erroMatrizTrust3/contadorPontos);
                        erroMatrizTrust4 = Math.sqrt(erroMatrizTrust4/contadorPontos);
                        erroTrust = Math.sqrt(erroTrust/contadorPontos);
                        
                        System.out.println("Taxa de erro - RMSE - Apenas Matriz: " + erroMatriz);
                        System.out.println("Taxa de erro - RMSE - Matriz com  Trust - 0.1: " + erroMatrizTrust5);
                        System.out.println("Taxa de erro - RMSE - Matriz com  Trust - 0.2: " + erroMatrizTrust6);
                        System.out.println("Taxa de erro - RMSE - Matriz com  Trust - 0.3: " + erroMatrizTrust7);
                        System.out.println("Taxa de erro - RMSE - Matriz com  Trust - 0.4: " + erroMatrizTrust);
                        System.out.println("Taxa de erro - RMSE - Matriz com  Trust - 0.5: " + erroMatrizTrust1);
                        System.out.println("Taxa de erro - RMSE - Matriz com  Trust - 0.6: " + erroMatrizTrust2);
                        System.out.println("Taxa de erro - RMSE - Matriz com  Trust - 0.7: " + erroMatrizTrust3);
                        System.out.println("Taxa de erro - RMSE - Matriz com  Trust - 0.8: " + erroMatrizTrust4);
                        System.out.println("Taxa de erro - RMSE - variação: " + erroTrust);
                        System.out.println(contadorPontos);
                        System.out.println(numeroUsuariosMenor);
                        System.out.println(contadorAval);
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