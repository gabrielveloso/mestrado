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
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.RatingSGDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;


//explicação do SGD mahout
//https://mahout.apache.org/users/recommender/matrix-factorization.html
public class Hibrido2 {
    
    public static void main(String[] args) {
        try {
        	
        	
        	BufferedReader fr = null;
    		try {
    			//C:\\Users\\gabriel\\git\\librec-mestrado\\librec\\dadosTreinamento\\trust_data.txt
    			//C:\\Users\\gabriel\\Desktop\\dados_epinions_alchemy\\trust_data.txt
    			fr = new BufferedReader(new FileReader("C:\\Users\\gabriel\\Desktop\\dados_epinions_alchemy\\trust_data.txt"));
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

    		Map<String, Map<String, Double>> userTrustorsMap = new HashMap<>();
    		String lineT = null;
    		Map<String, Double> trustors = null;
    		try{
    			while ((lineT = fr.readLine()) != null)
    			{
    				if (lineT.equals("")) continue;
    				String[] data = lineT.split(" ");
    				String trustor = data[0];
    				String trustee = data[1];
    				double trustScore = Double.parseDouble(data[2]);

    				if (trustee.equals(trustor)) continue; // to remove self-indicate entry

//    				if (userTrustorsMap.containsKey(trustee)) trustors = userTrustorsMap.get(trustee);
//    				else trustors = new HashMap<>();
    //
//    				trustors.put(trustor, trustScore);
//    				userTrustorsMap.put(trustee, trustors);
    				
    				if (userTrustorsMap.containsKey(trustor)) trustors = userTrustorsMap.get(trustor);
    					else trustors = new HashMap<>();
    				
    				trustors.put(trustee, trustScore);
    				userTrustorsMap.put(trustor, trustors);
    				
    			}
    			fr.close();
    		}catch(IOException e){
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		

            //DataModel dataModel = new FileDataModel(new File("C:\\Users\\gabriel\\git\\mestrado\\data\\ratings_treinamento1.cvs"));
    		DataModel dataModel = new FileDataModel(new File("C:\\Users\\gabriel\\Desktop\\dados_epinions_alchemy\\ratings_novo.cvs"));
            DataModel dataModelTrust = new FileDataModel(new File("C:\\Users\\gabriel\\Desktop\\trust.cvs"));            
                    
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
                
                    
                    RatingSGDFactorizer factorizerPadrao = new RatingSGDFactorizer(dataModel,5, 0.0002 , 0.02,0.01, 1000,1.0);
                    SVDRecommender rec = new SVDRecommender(dataModel, factorizerPadrao);

                    try {
                        //BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\gabriel\\git\\mestrado\\data\\ratings_data.txt"));
                        
                    	BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\gabriel\\Desktop\\dados_epinions_alchemy\\testealchemy.txt"));
                        String line = "";                        
                        double erroRMSE = 0;
                        double erroMAE = 0;                       
                        double erroMAE1 = 0;
                        double erroMAE2 = 0;
                        double erroMAE3 = 0;
                        double erroMAEMatriz = 0;
                        double erroMAEMatriz1 = 0;
                        double erroMAEMatriz2 = 0;
                        double erroMAEMatriz3 = 0;
                        double erroMAEMatrizSemRound = 0;
                        String usuario = null;
                        String item = null;
                        String nota = null;
                        double contadorPontos = 0.0;
                        double contadorTotal = 0;
                        double contadorPontos1 = 0;
                        double contadorPontos2 = 0;
                        double contadorPontos3 = 0;
                        double contadorPontosM = 0;
                        double contadorPontosM1 = 0;
                        double contadorPontosM2 = 0;
                        double contadorPontosM3 = 0;
                        double sum_maes = 0;
                        double sum_maesSemRound = 0;
                        double sum_rmses = 0;
                        double sum_maesMole1 = 0;
                        double sum_maesMole2 = 0;
                        double sum_maesMole3 = 0;
                        
                        double sum_maesMatriz = 0;
                        double sum_maesMatriz1 = 0;
                        double sum_maesMatriz2 = 0;
                        double sum_maesMatriz3 = 0;
                        double sum_maesMatrizSemRound = 0;
                        
                        double sum_maesHibrida = 0;
                        double sum_maesHibrida1 = 0;
                        double sum_maesHibrida2 = 0;
                        double sum_maesHibrida3 = 0;
                        double sum_maesHibrida2medida = 0;
                        double sum_maesHibrida2medida1 = 0;
                        double sum_maesHibrida2medida2 = 0;
                        double sum_maesHibrida2medida3 = 0;
                        double sum_maesHibrida3medida = 0;
                        double sum_maesHibrida3medida1 = 0;
                        double sum_maesHibrida3medida2 = 0;
                        double sum_maesHibrida3medida3 = 0;
                        double sum_maesHibrida4medida = 0;
                        double sum_maesHibrida5medida = 0;
                        double sum_maesHibrida6medida = 0;
                        double contadorHibrido = 0;
                        double contadorHibrido1 = 0;
                        double contadorHibrido2 = 0;
                        double contadorHibrido3 = 0;
                        double contadorHibrido2medida1 = 0;
                        double contadorHibrido2medida2 = 0;
                        double contadorHibrido2medida3 = 0;
                        double contadorHibrido3medida1 = 0;
                        double contadorHibrido3medida2 = 0;
                        double contadorHibrido3medida3 = 0;
                        while((line = br.readLine()) != null){
                            String[] values = line.split(" ");
                            usuario = values[0];
                            item = values[1];
                            nota = values[2];
                            int i = Integer.parseInt(usuario);
                            int j = Integer.parseInt(item);                           
                            double somaNumerador = 0;
                            double somaDenominador = 0;
                            double mediaAtivo = 0;
                            
                            double qtdAvaliacoes =0;
                            
                            
                            // CALCULO DA NOTA PREDITA DA MATRIZ FATORAÇÃO
                            double notaPreditaMatriz = 0;
                            double notaPreditaSemRound = 0;
                            try{ 
                            	notaPreditaSemRound = rec.estimatePreference(i, j);
                            	notaPreditaMatriz = Math.round(notaPreditaSemRound);
                            	if(notaPreditaMatriz < 1){
                            		notaPreditaMatriz = 1;
                            		notaPreditaSemRound = 1;
                            	}
                            	
                            	if(notaPreditaMatriz > 5){
                            		notaPreditaMatriz = 5;
                            		notaPreditaSemRound = 5;
                            	}
                            	//double notaPreditaMatriz = 3.99;
                            	double erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
                            	double erroMatrizSemRound = Double.parseDouble(nota) - notaPreditaSemRound;
                            	sum_maesMatriz += Math.abs(erroMatriz);
                            	sum_maesMatrizSemRound += Math.abs(erroMatrizSemRound);
                            	contadorPontosM++;
                            	
                            	qtdAvaliacoes = dataModel.getPreferencesFromUser(i).length();
                            	
                            	if(qtdAvaliacoes <= 10){
                            		erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
                            		sum_maesMatriz1 += Math.abs(erroMatriz);
                            		contadorPontosM1++;
                            	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                            		erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
                            		sum_maesMatriz2 += Math.abs(erroMatriz);
                            		contadorPontosM2++;
                            	}else if(qtdAvaliacoes > 100){
                            		erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
                            		sum_maesMatriz3 += Math.abs(erroMatriz);
                            		contadorPontosM3++;
                            	}
                            	
                            }catch(Exception e){
                            	System.out.println("###### não conseguiu estimar");
                            }
                            
                            
                            //VERIFICA OS ITENS AVALIADOS PELO USUARIO ATIVO
                            PreferenceArray notasAtivo = null;
                            try{
                            	notasAtivo = dataModel.getPreferencesFromUser(i);
                        	}catch(Exception e){
                        		continue;
                        	}                            
                            contadorTotal++;
                            
                            
                            //CALCULA A MEDIA DAS NOTAS DO USER ATIVO
                            Iterator<Preference> novoAtivo  = notasAtivo.iterator();
                            double somaPrefAtivo = 0;
                            double qtdPrefAtivo = 0;
            				while(novoAtivo.hasNext()){
            					Preference teste = novoAtivo.next();
            					somaPrefAtivo +=teste.getValue();
            					qtdPrefAtivo+=1;
            				}
            				
            				if(qtdPrefAtivo == 0){
            					mediaAtivo = 0;
            				}else{
            					mediaAtivo = somaPrefAtivo/qtdPrefAtivo;
            				}
            				
            				// RETORNA A LISTA DE USUARIOS CONFIAVEIS DO USER
                        	//Map<String, Double> scores = calcularMoleTrust(i);
            				Map<String, Double> scores = new HashMap<>();
                        	try{
                        		 scores = userTrustorsMap.get(i+"");
                        	}catch(Exception e){
                        		System.out.println(e.getMessage());
                        	}
                        	
                        	if(scores == null || scores.keySet() == null){
                        		scores = new HashMap<>();
                        	}
                        	
                        	int qtdAmigosAvaliaramItem = 0;
                        	// PARA CADA AMIGO, CALCULA A MEDIA DO AMIGO, A NOTA DADA PELO AMIGO PARA O ITEM
                        	//SUBTRAI A NOTA DA MEDIA DO AMIGO
                        	for(String amigo : scores.keySet()){
                        		double somaPreferencias = 0;
                            	double qtdPreferencias = 0;
                            	
                				if(Integer.parseInt(amigo) <= 50000){
                    				int friend = Integer.parseInt(amigo);
                    				PreferenceArray notasAmigo = null;
                    				try{
                    					notasAmigo = dataModel.getPreferencesFromUser(friend);
                    				}catch(Exception e){
                    					System.out.println("Usuario amigo "+friend+" sem preferencia");
                    					
                    					continue;
                    				}
                    				
                    				double rai = 0;
                    				try{
                    					rai = dataModel.getPreferenceValue(friend, j);
                    					qtdAmigosAvaliaramItem++;
                    				}catch(Exception te){
                    					continue;
                    				}
                    				
                    				Iterator<Preference> novo  = notasAmigo.iterator();	                    				
                    				while(novo.hasNext()){
                    					Preference teste = novo.next();
                    					somaPreferencias +=teste.getValue();
                    					qtdPreferencias+=1;
                    				}	                    				
                    				double mediaAmigo = somaPreferencias/qtdPreferencias;	                    				
                    				double pesoAmigo = scores.get(amigo);
                    				
                    					if(rai > 0){				
                    						somaNumerador += pesoAmigo*(rai - mediaAmigo);
                    						somaDenominador += pesoAmigo;
                    					}else{
                    						//System.out.println("amigo "+amigo+" não avaliou "+(j));
                    					}
                				}else{
                					//System.out.println("maior que o maximo "+amigo);
                				}	
                			}
                        	
                        	// SE ALGUM AMIGO AVALIOU O ITEM EM QUESTÃO
                        	long ruiRound = 0;
                        	double rui = 0;
                        	if(somaDenominador > 0 && !Double.isNaN(somaNumerador)){
                				 rui = mediaAtivo + somaNumerador/somaDenominador;
                				
                				
                				if(rui > 5){
                					rui = 5;
                				}else if(rui < 1){
                					rui = 1;
                				}
                				ruiRound = Math.round(rui);
                				
                				
                				double errRound = Integer.parseInt(nota) - ruiRound;
                				
                				sum_maesSemRound += Math.abs(Integer.parseInt(nota) - rui);
                				sum_maes += Math.abs(errRound);
                				sum_rmses += Math.pow((Double.valueOf(nota) - ruiRound),2);
                				contadorPontos++;
                				
                				
                				
                				if(qtdAvaliacoes <= 10){
                					sum_maesMole1 += Math.abs(errRound);
                					contadorPontos1++;
                            	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                            		sum_maesMole2 += Math.abs(errRound);
                					contadorPontos2++;
                            	}else if(qtdAvaliacoes > 100){
                            		sum_maesMole3 += Math.abs(errRound);
                					contadorPontos3++;
                            	}
                				
                        	}
                        	
                        	
                        	
            				double taxaRatings = (double)dataModel.getPreferencesFromUser(i).length()/(double)numeroMaximoAval;	
            				double alpha = 0;
            				if(taxaRatings == 0 ){
            					alpha = 1;
            				}else{
            					alpha = Math.abs(Math.log(taxaRatings))/Math.log(10000);
            				}  
            				
            				// se rui é igual a zero utilizar somente nota da matriz
            				if(rui == 0){
            					alpha = 0;
            				}            				
            				double notaHibrida = alpha*rui + (1 - alpha)*notaPreditaSemRound;             				
            				notaHibrida = Math.round(notaHibrida);  
            				double erroHibrido = Integer.parseInt(nota) - notaHibrida;
            				sum_maesHibrida += Math.abs(erroHibrido);
            				
            				if(qtdAvaliacoes <= 10){
            					sum_maesHibrida1 += Math.abs(erroHibrido);
            					contadorHibrido1++;
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		sum_maesHibrida2 += Math.abs(erroHibrido);
            					contadorHibrido2++;
                        	}else if(qtdAvaliacoes > 100){
                        		sum_maesHibrida3 += Math.abs(erroHibrido);
            					contadorHibrido3++;
                        	}
            				
            				// 2 medida de esparsividade
            				if((double)dataModel.getPreferencesFromUser(i).length() < 10 && ruiRound > 0){
            					notaHibrida = ruiRound;
            				}else{
            					notaHibrida = notaPreditaMatriz;
            				}
            				sum_maesHibrida2medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				erroHibrido = Integer.parseInt(nota) - notaHibrida;
            				
            				if(qtdAvaliacoes <= 10){
            					sum_maesHibrida2medida1 += Math.abs(erroHibrido);
            					contadorHibrido2medida1++;
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		sum_maesHibrida2medida2 += Math.abs(erroHibrido);
            					contadorHibrido2medida2++;
                        	}else if(qtdAvaliacoes > 100){
                        		sum_maesHibrida2medida3 += Math.abs(erroHibrido);
            					contadorHibrido2medida3++;
                        	}
            				
            				//3 medida de esparsividade
            				taxaRatings = (double)dataModel.getPreferencesFromUser(i).length()/(2*6);	
            				if(taxaRatings > 1){
            					taxaRatings = 1;
            				}
            				if(rui == 0){
            					taxaRatings = 1;
            				}   
            				
            				notaHibrida = (1 - taxaRatings)*rui + taxaRatings*notaPreditaSemRound;
            				notaHibrida = Math.round(notaHibrida); 
            				sum_maesHibrida3medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				erroHibrido = Integer.parseInt(nota) - notaHibrida;
            				
            				if(qtdAvaliacoes <= 10){
            					sum_maesHibrida3medida1 += Math.abs(erroHibrido);
            					contadorHibrido3medida1++;
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		sum_maesHibrida3medida2 += Math.abs(erroHibrido);
            					contadorHibrido3medida2++;
                        	}else if(qtdAvaliacoes > 100){
                        		sum_maesHibrida3medida3 += Math.abs(erroHibrido);
            					contadorHibrido3medida3++;
                        	}
            				
            				contadorHibrido++;
            				
            				
            				taxaRatings = qtdAmigosAvaliaramItem/(2*6);            				
            				if(rui == 0){
            					taxaRatings = 1;
            				}
            				notaHibrida = (1 - taxaRatings)*rui + taxaRatings*notaPreditaSemRound;
            				sum_maesHibrida4medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				
            				if(dataModel.getPreferencesForItem(j).length() != 0){
            					taxaRatings = qtdAmigosAvaliaramItem/(double)dataModel.getPreferencesForItem(j).length();
            				}else{
            					taxaRatings = 1;
            				}
            				if(rui == 0){
            					taxaRatings = 1;
            				}
            				notaHibrida = (1 - taxaRatings)*rui + taxaRatings*notaPreditaSemRound;
            				sum_maesHibrida5medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				
            				if(scores.keySet().size() != 0){
            					taxaRatings = qtdAmigosAvaliaramItem/(double)scores.keySet().size();
            				}else{
            					taxaRatings = 1;
            				}
            				if(rui == 0){
            					taxaRatings = 1;
            				}
            				notaHibrida = (1 - taxaRatings)*rui + taxaRatings*notaPreditaSemRound;
            				sum_maesHibrida6medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				
            				
            				
                        	//fazer pra outras medidas de esparsidade (artigo)
            				//aumentar a profundidade do moletrust
            				
            				
                        }
                        
                        erroMAE = sum_maes/contadorPontos;
                        erroRMSE = Math.sqrt(sum_rmses/contadorPontos);                        
                        erroMAE1 = sum_maesMole1/contadorPontos1;
                        erroMAE2 = sum_maesMole2/contadorPontos2;
                        erroMAE3 = sum_maesMole3/contadorPontos3;
                        
                        erroMAEMatriz = sum_maesMatriz/contadorPontosM;
                        erroMAEMatriz1 = sum_maesMatriz1/contadorPontosM1;
                        erroMAEMatriz2 = sum_maesMatriz2/contadorPontosM2;
                        erroMAEMatriz3 = sum_maesMatriz3/contadorPontosM3;
                        erroMAEMatrizSemRound = sum_maesMatrizSemRound/contadorPontosM;
                        
                        System.out.println("MAE MOLETRUST1 GERAL SEM ROUND " + sum_maesSemRound/contadorPontos); 
                        System.out.println("MAE MOLETRUST1 GERAL ROUND " + erroMAE + " RMSE: " + erroRMSE);    
                        System.out.println("Quantidade total "+contadorTotal+" Quantidade geral predita por moletrust "+contadorPontos);
                        
                        System.out.println("MAE MOLETRUST1 <= 10 " + erroMAE1 + " QTD: " + contadorPontos1); 
                        System.out.println("MAE MOLETRUST1 ENTRE 10 E 100 " + erroMAE2 + " QTD: " + contadorPontos2); 
                        System.out.println("MAE MOLETRUST1 MAIOR QUE 100 " + erroMAE3 + " QTD: " + contadorPontos3); 
                        
                        System.out.println("MAE matriz : " + erroMAEMatriz + " Quantidade" + contadorPontosM);
                        System.out.println("MAE matriz sem round : " + erroMAEMatrizSemRound + " Quantidade M " + contadorPontosM);
                        System.out.println("MAE matriz <= 10: " + erroMAEMatriz1 + " Quantidade" + contadorPontosM1);
                        System.out.println("MAE matriz ENTRE 10 E 100: " + erroMAEMatriz2 + " Quantidade" + contadorPontosM2);
                        System.out.println("MAE matriz MAIOR QUE 100: " + erroMAEMatriz3 + " Quantidade" + contadorPontosM3);
                        
                        
                        System.out.println("MAE HIBRIDA MEDIDA 1 <= 10: " + sum_maesHibrida1/contadorHibrido1 + " Quantidade" + contadorHibrido1);
                        System.out.println("MAE HIBRIDA MEDIDA1 ENTRE 10 E 100: " + sum_maesHibrida2/contadorHibrido2 + " Quantidade" + contadorHibrido2);
                        System.out.println("MAE HIBRIDA MEDIDA 1 MAIOR QUE 100: " + sum_maesHibrida3/contadorHibrido3 + " Quantidade" + contadorHibrido3);
                        System.out.println("MAE HIBRIDA MEDIDA 2 <= 10: " + sum_maesHibrida2medida1/contadorHibrido2medida1 );
                        System.out.println("MAE HIBRIDA MEDIDA 2 ENTRE 10 E 100: " + sum_maesHibrida2medida2/contadorHibrido2medida2 );
                        System.out.println("MAE HIBRIDA MEDIDA 2 MAIOR QUE 100: " + sum_maesHibrida2medida3/contadorHibrido2medida3 );
                        System.out.println("MAE HIBRIDA MEDIDA 3 <= 10: " + sum_maesHibrida3medida1/contadorHibrido3medida1 );
                        System.out.println("MAE HIBRIDA MEDIDA 3 ENTRE 10 E 100: " + sum_maesHibrida3medida2/contadorHibrido3medida2 );
                        System.out.println("MAE HIBRIDA MEDIDA 3 MAIOR QUE 100: " + sum_maesHibrida3medida3/contadorHibrido3medida3 );
                        
                        System.out.println("MAE HIBRIDA MEDIDA 3 TODOS: " + sum_maesHibrida3medida/contadorHibrido );                        
                        System.out.println("MAE HIBRIDA MEDIDA 4 TODOS: " + sum_maesHibrida4medida/contadorHibrido );
                        System.out.println("MAE HIBRIDA MEDIDA 5 TODOS: " + sum_maesHibrida5medida/contadorHibrido );
                        System.out.println("MAE HIBRIDA MEDIDA 6 TODOS: " + sum_maesHibrida6medida/contadorHibrido );
                      
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
    
    
    public static Map<String, Double> calcularMoleTrust(int ativo){	

		BufferedReader fr = null;
		try {
			fr = new BufferedReader(new FileReader("C:\\Users\\gabriel\\git\\librec-mestrado\\librec\\dadosTreinamento\\trust_data.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Map<String, Double>> userTrustorsMap = new HashMap<>();
		String line = null;
		Map<String, Double> trustors = null;
		try{
			while ((line = fr.readLine()) != null)
			{
				if (line.equals("")) continue;
				String[] data = line.split(" ");
				String trustor = data[0];
				String trustee = data[1];
				double trustScore = Double.parseDouble(data[2]);

				if (trustee.equals(trustor)) continue; // to remove self-indicate entry

//				if (userTrustorsMap.containsKey(trustee)) trustors = userTrustorsMap.get(trustee);
//				else trustors = new HashMap<>();
//
//				trustors.put(trustor, trustScore);
//				userTrustorsMap.put(trustee, trustors);
				
				if (userTrustorsMap.containsKey(trustor)) trustors = userTrustorsMap.get(trustor);
					else trustors = new HashMap<>();
				
				trustors.put(trustee, trustScore);
				userTrustorsMap.put(trustor, trustors);
				
			}
			fr.close();
		}catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return userTrustorsMap.get(ativo+"");

		return MoleTrust(userTrustorsMap, ativo+"", 1);
		//		for(String amigo : scores.keySet()){
		//			System.out.println(amigo + " " +scores.get(amigo));
		//		}


	}

	public static Map<String, Double> MoleTrust(Map<String, Map<String, Double>> trust_data, String sourceUser,
			int horizon)
			{
		// all the visited nodes
		List<String> nodes = new ArrayList<>(1000);
		// source user - edges[target users - trust value]
		Map<String, Map<String, Double>> edges = new HashMap<>(1000);

		/* Step 1: construct directed graphic and remove cyclic */
		int dist = 0;
		List<String>[] users = new List[horizon + 1];
		users[dist] = new ArrayList<>();
		users[dist].add(sourceUser);
		nodes.add(sourceUser);

		// Denote su: source user; tu: target user
		while (dist < horizon)
		{
			dist++;
			users[dist] = new ArrayList<>();
			for (String su : users[dist - 1])
			{
				Map<String, Double> tns = trust_data.get(su);
				if (tns == null) continue; // no trusted neighbours
				for (String tn : tns.keySet())
				{
					if (!nodes.contains(tn) && !users[dist].contains(tn) && !users[dist - 1].contains(tn))
					{
						users[dist].add(tn);
					}
				}
			}

			for (String su : users[dist - 1])
			{
				Map<String, Double> tns = trust_data.get(su);
				if (tns == null) continue;
				for (String tu : tns.keySet())
				{
					if (!nodes.contains(tu) && users[dist].contains(tu))
					{
						Map<String, Double> tuTrusts;
						if (edges.containsKey(su)) tuTrusts = edges.get(su);
						else tuTrusts = new HashMap<>();

						double trustValue = trust_data.get(su).get(tu);
						tuTrusts.put(tu, trustValue);
						edges.put(su, tuTrusts);
					}
				}
			}
		}

		/* Step 2: Evaluate trust score */
		dist = 0;
		double threashold = 0.0;
		// trusted neighbours - trust score map
		Map<String, Double> trustScores = new HashMap<>();
		trustScores.put(sourceUser, 1.0);
		while (dist < horizon)
		{
			dist++;
			for (String tu : users[dist])
			{

				double sum = 0.0;
				double weights = 0.0;
				for (String su : users[dist - 1])
				{
					Map<String, Double> tuTrusts = edges.get(su);
					if (tuTrusts == null) continue; // no edges for user su
					if (tuTrusts.containsKey(tu))
					{
						double trust_edge = tuTrusts.get(tu);
						if (trust_edge > threashold)
						{
							sum += trust_edge * trustScores.get(su);
							weights += trustScores.get(su);
						}
					}
				}
				double score = sum / weights;
				trustScores.put(tu, score);
			}
		}

		trustScores.remove(sourceUser);
		return trustScores;
	}
}