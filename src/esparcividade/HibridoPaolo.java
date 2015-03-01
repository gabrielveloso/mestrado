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
public class HibridoPaolo {
    
    public static void main(String[] args) {
        try {
        	
        	
        	BufferedReader fr = null;
    		try {
    			fr = new BufferedReader(new FileReader("C:\\Users\\gabriel\\Desktop\\conjuntoDados\\PAOLO\\trust_data.txt"));
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
    		
    		

    		DataModel dataModel = new FileDataModel(new File("C:\\Users\\gabriel\\Desktop\\conjuntoDados\\PAOLO\\ratings.cvs"));
    		DataModel dataModeltrain = new FileDataModel(new File("C:\\Users\\gabriel\\Desktop\\conjuntoDados\\PAOLO\\estratificado\\trainestratificado3.arff"));
            DataModel dataModelTrust = new FileDataModel(new File("C:\\Users\\gabriel\\Desktop\\conjuntoDados\\PAOLO\\trust.cvs"));  
            
                    
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
                
                    
                    RatingSGDFactorizer factorizerPadrao = new RatingSGDFactorizer(dataModeltrain,5, 0.0002 , 0.02,0.01, 1000,1.0);
                    SVDRecommender rec = new SVDRecommender(dataModeltrain, factorizerPadrao);

                    try {
                        
                    	BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\gabriel\\Desktop\\conjuntoDados\\PAOLO\\estratificado\\testeestratificado3.arff"));
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
                        double contadorPontosMoleTrust = 0.0;
                        double contadorTotal = 0;
                        double contadorPontos1 = 0;
                        double contadorPontos2 = 0;
                        double contadorPontos3 = 0;
                        double contadorPontosM = 0;
                        double contadorPontosM1 = 0;
                        double contadorPontosM2 = 0;
                        double contadorPontosM3 = 0;
                        
                        double contadorPontos1teste = 0;
                        double contadorPontos2teste = 0;
                        double contadorPontos3teste = 0;
                        
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
                        
                        int quantidade_usuarios = 49290;
                        
                        double[] sum_erro_matriz_visao1 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_matriz_visao1 = new int[quantidade_usuarios];
                        double[] sum_erro_matriz_visao2 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_matriz_visao2 = new int[quantidade_usuarios];
                        double[] sum_erro_matriz_visao3 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_matriz_visao3 = new int[quantidade_usuarios];
                        
                        double[] sum_erro_moletrust_visao1 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_moletrust_visao1 = new int[quantidade_usuarios];
                        double[] sum_erro_moletrust_visao2 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_moletrust_visao2 = new int[quantidade_usuarios];
                        double[] sum_erro_moletrust_visao3 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_moletrust_visao3 = new int[quantidade_usuarios];
                        
                        
                        double sum_maeItensNicho2medida1 = 0;
                        double contadorItensNicho2medida1 = 0;
                        double sum_maeItensNicho2medida2 = 0;
                        double contadorItensNicho2medida3 = 0;
                        
                        double sum_maeItensNichoMatriz1 = 0;
                        double contadorItensNichoMatriz1 = 0;
                        double sum_maeItensNichoMatriz2 = 0;
                        double contadorItensNichoMatriz2 = 0;
                        
                        double sum_maeItensControvMatriz1 = 0;
                        double contadorItensControvMatriz1 = 0;
                        double sum_maeItensControvMatriz2 = 0;
                        double contadorItensControvMatriz2 = 0;
                        
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
                        double sum_maesHibrida4medida1 = 0;
                        double sum_maesHibrida4medida2 = 0;
                        double sum_maesHibrida4medida3 = 0;
                        double sum_maesHibrida5medida = 0;
                        double sum_maesHibrida5medida1 = 0;
                        double sum_maesHibrida5medida2 = 0;
                        double sum_maesHibrida5medida3 = 0;
                        double sum_maesHibrida6medida = 0;
                        double sum_maesHibrida6medida1 = 0;
                        double sum_maesHibrida6medida2 = 0;
                        double sum_maesHibrida6medida3 = 0;
                        
                        double sum_maesHibrida7medida = 0;
                        double sum_maesHibrida7medida1 = 0;
                        double sum_maesHibrida7medida2 = 0;
                        double sum_maesHibrida7medida3 = 0;
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
                        double contadorHibrido4medida1 = 0;
                        double contadorHibrido4medida2 = 0;
                        double contadorHibrido4medida3 = 0;
                        double contadorHibrido5medida1 = 0;
                        double contadorHibrido5medida2 = 0;
                        double contadorHibrido5medida3 = 0;
                        double contadorHibrido6medida1 = 0;
                        double contadorHibrido6medida2 = 0;
                        double contadorHibrido6medida3 = 0;
                        double contadorHibrido7medida1 = 0;
                        double contadorHibrido7medida2 = 0;
                        double contadorHibrido7medida3 = 0;
                        
                        
                        
                        double[] sum_erro = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario = new int[quantidade_usuarios];
                        
                        double[] sum_erro_matriz = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_matriz = new int[quantidade_usuarios];
                        
                        double[] sum_erro_hibrida1 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida1 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida1_visao1 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida1_visao1 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida1_visao2 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida1_visao2 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida1_visao3 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida1_visao3 = new int[quantidade_usuarios];
                        
                        double[] sum_erro_hibrida2 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida2 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida2_visao1 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida2_visao1 = new int[quantidade_usuarios];                        
                        double[] sum_erro_hibrida2_visao2 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida2_visao2 = new int[quantidade_usuarios];                        
                        double[] sum_erro_hibrida2_visao3 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida2_visao3 = new int[quantidade_usuarios];  
                        
                        double[] sum_erro_hibrida3 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida3 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida3_visao1 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida3_visao1 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida3_visao2 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida3_visao2 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida3_visao3 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida3_visao3 = new int[quantidade_usuarios];
                        
                        
                        double[] sum_erro_hibrida4 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida4 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida4_visao1 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida4_visao1 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida4_visao2 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida4_visao2 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida4_visao3 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida4_visao3 = new int[quantidade_usuarios];
                        
                        
                        double[] sum_erro_hibrida5 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida5 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida5_v1 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida5_v1 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida5_v2 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida5_v2 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida5_v3 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida5_v3 = new int[quantidade_usuarios];
                        
                        
                        double[] sum_erro_hibrida6 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida6 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida6_v1 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida6_v1 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida6_v2 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida6_v2 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida6_v3 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida6_v3 = new int[quantidade_usuarios];
                        
                        
                        double[] sum_erro_hibrida7 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida7 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida7_v1 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida7_v1 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida7_v2 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida7_v2 = new int[quantidade_usuarios];
                        double[] sum_erro_hibrida7_v3 = new double[quantidade_usuarios];
                        int[] quantidade_por_usuario_hibrida7_v3 = new int[quantidade_usuarios];
                        
                        
                        int[] usuarios_visao1 = new int[quantidade_usuarios];
                        int[] usuarios_visao2 = new int[quantidade_usuarios];
                        int[] usuarios_visao3 = new int[quantidade_usuarios];
                        
                        while((line = br.readLine()) != null){
                            String[] values = line.split(",");
                            usuario = values[0];
                            item = values[1];
                            nota = values[2];
                            int i = Integer.parseInt(usuario);
                            int j = Integer.parseInt(item);                           
                            double somaNumerador = 0;
                            double somaDenominador = 0;
                            double mediaAtivo = 0;
                            
                            double qtdAvaliacoes =0;
                            try{
                            	qtdAvaliacoes = dataModel.getPreferencesFromUser(i).length();
                            }catch(Exception e){
                            	System.out.println("parou aqui");
                            }
                            
                            if(qtdAvaliacoes <= 10){
                        		contadorPontos1teste++;
                        		usuarios_visao1[i] +=1;
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		contadorPontos2teste++;
                        		usuarios_visao2[i] +=1;
                        	}else if(qtdAvaliacoes > 100){
                        		contadorPontos3teste++;
                        		usuarios_visao3[i] +=1;
                        	}                            
                            
                            
                            // CALCULO DA NOTA PREDITA DA FATORAÇÃO DE MATRIZ
                            double notaPreditaMatriz = 0;
                            double notaPreditaSemRound = 0;
                            try{ 
                            	notaPreditaSemRound = rec.estimatePreference(i, j); // SE LEVANTAR EXCEPTION A NOTA SERÁ 0
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
                            	
                            	//qtdAvaliacoes = dataModel.getPreferencesFromUser(i).length();
                            	
                            	if(qtdAvaliacoes <= 10){
                            		erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
                            		sum_maesMatriz1 += Math.abs(erroMatriz);
                            		contadorPontosM1++;
                            		
                            		sum_erro_matriz_visao1[i] += Math.abs(erroMatriz);
                     				quantidade_por_usuario_matriz_visao1[i] += 1;
                     				
                            	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                            		erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
                            		sum_maesMatriz2 += Math.abs(erroMatriz);
                            		contadorPontosM2++;
                            		
                            		sum_erro_matriz_visao2[i] += Math.abs(erroMatriz);
                     				quantidade_por_usuario_matriz_visao2[i] += 1;
                            	}else if(qtdAvaliacoes > 100){
                            		erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
                            		sum_maesMatriz3 += Math.abs(erroMatriz);
                            		contadorPontosM3++;
                            		
                            		sum_erro_matriz_visao3[i] += Math.abs(erroMatriz);
                     				quantidade_por_usuario_matriz_visao3[i] += 1;
                            	}
                            	
                            	
                            	int preferenciasItem = dataModel.getPreferencesForItem(j).length();
                                if(preferenciasItem <= 5){
                                	erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
                                	sum_maeItensNichoMatriz1+=Math.abs(erroMatriz);
                                	contadorItensNichoMatriz1++;
                                }else{
                                	erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
                                	sum_maeItensNichoMatriz2+=Math.abs(erroMatriz);
                                	contadorItensNichoMatriz2++;
                                }
                                
                                sum_erro_matriz[i] += Math.abs(erroMatriz);
                				quantidade_por_usuario_matriz[i] += 1;
                                
//                                PreferenceArray pref = dataModel.getPreferencesForItem(j);
//                                float soma = 0;
//                                for(int p = 0; p < preferenciasItem; p++){
//                                	soma += pref.get(p).getValue();
//                                }
//                                float media = soma/preferenciasItem;
//                                double variancia = 0;
//                                
//                                for(int p = 0; p < preferenciasItem; p++){
//                                	 variancia += Math.pow((pref.get(p).getValue() - media),2);
//                                }
//                                variancia = Math.sqrt(variancia/preferenciasItem);
//                                
//                                if(variancia > 1.5){
//                                	erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
//                                	sum_maeItensControvMatriz1+=Math.abs(erroMatriz);
//                                	contadorItensControvMatriz1++;
//                                }else{
//                                	erroMatriz = Double.parseDouble(nota) - notaPreditaMatriz;
//                                	sum_maeItensControvMatriz2+=Math.abs(erroMatriz);
//                                	contadorItensControvMatriz2++;
//                                }
                                
                            	
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
                        		 //scores = userTrustorsMap.get(i+"");
                        		 scores = MoleTrust(userTrustorsMap, i+"", 1);
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
                        	
                        	
                        	long ruiRound = 0; // nota predita moletrust com aredondamento
                        	double rui = 0;
                        	// SE ALGUM AMIGO AVALIOU O ITEM EM QUESTÃO
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
                				contadorPontosMoleTrust++;
                				
                				sum_erro[i] += Math.abs(errRound);
                				quantidade_por_usuario[i] += 1;
                				
                				
                				if(qtdAvaliacoes <= 10){
                					sum_maesMole1 += Math.abs(errRound);
                					contadorPontos1++;
                					
                					sum_erro_moletrust_visao1[i] += Math.abs(errRound);
                     				quantidade_por_usuario_moletrust_visao1[i] += 1;
                     				
                            	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                            		sum_maesMole2 += Math.abs(errRound);
                					contadorPontos2++;
                					
                					sum_erro_moletrust_visao2[i] += Math.abs(errRound);
                     				quantidade_por_usuario_moletrust_visao2[i] += 1;
                     				
                            	}else if(qtdAvaliacoes > 100){
                            		sum_maesMole3 += Math.abs(errRound);
                					contadorPontos3++;
                					
                					sum_erro_moletrust_visao3[i] += Math.abs(errRound);
                     				quantidade_por_usuario_moletrust_visao3[i] += 1;
                            	}
                				
                        	}
                        	
                        	
                        	if(notaPreditaSemRound != 0 || rui != 0){
                        	
                        	contadorHibrido++;

                        	/**
                        	 * AQUI É FEITO O CALCULADA DA NOTA PREDITA BASEADO NAS DUAS TÉCNICAS
                        	 * SERÃO CACULADAS A PARTIR DA EQUAÇÃO (1), UTILZIAND MEDIDAS DE ESPARSIDADE
                        	 */
                        	
                        	//
                        	// MEDIDA ESPARSIDADE 1 
                        	//
                        	//
                        	                        	
            				double taxaRatings = (double)dataModel.getPreferencesFromUser(i).length()/(double)numeroMaximoAval;	
            				double alpha = 0;
            				if(taxaRatings == 0 ){
            					// usuarios com nenhuma avaliação a nota dependerá apenas dos amigos (moletrust)
            					alpha = 1;
            				}else{
            					alpha = Math.abs(Math.log(taxaRatings))/Math.log(10000);
            				}  
            				
            				// se rui é igual a zero utilizar somente nota da matriz
            				if(rui == 0){
            					alpha = 0;
            				}    
            				if(notaPreditaSemRound == 0){
            					alpha = 1;
            				}
            				double notaHibrida = alpha*rui + (1 - alpha)*notaPreditaSemRound;             				
            				notaHibrida = Math.round(notaHibrida);  
            				double erroHibrido = Integer.parseInt(nota) - notaHibrida;
            				sum_maesHibrida += Math.abs(erroHibrido);
            				
            				sum_erro_hibrida1[i] += Math.abs(erroHibrido);
             				quantidade_por_usuario_hibrida1[i] += 1;
            				
            				if(qtdAvaliacoes <= 10){
            					sum_maesHibrida1 += Math.abs(erroHibrido);
            					contadorHibrido1++;
            					
            					sum_erro_hibrida1_visao1[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida1_visao1[i] += 1;
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		sum_maesHibrida2 += Math.abs(erroHibrido);
            					contadorHibrido2++;
            					
            					sum_erro_hibrida1_visao2[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida1_visao2[i] += 1;
                        	}else if(qtdAvaliacoes > 100){
                        		sum_maesHibrida3 += Math.abs(erroHibrido);
            					contadorHibrido3++;
            					
            					sum_erro_hibrida1_visao3[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida1_visao3[i] += 1;
                        	}
            				
            				//
                        	// MEDIDA ESPARSIDADE 2 
                        	//
                        	//
                        	
            				if((double)dataModel.getPreferencesFromUser(i).length() < 10 && ruiRound > 0 || notaPreditaMatriz == 0){
            					notaHibrida = ruiRound;
            				}else {
            					notaHibrida = notaPreditaMatriz;
            				}
            				
            				notaHibrida = notaPreditaMatriz;
            				sum_maesHibrida2medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				erroHibrido = Integer.parseInt(nota) - notaHibrida;
            				
            				sum_erro_hibrida2[i] += Math.abs(erroHibrido);
             				quantidade_por_usuario_hibrida2[i] += 1;
            				
            				if(qtdAvaliacoes <= 10){
            					sum_maesHibrida2medida1 += Math.abs(erroHibrido);
            					contadorHibrido2medida1++;
            					
            					sum_erro_hibrida2_visao1[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida2_visao1[i] += 1;
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		sum_maesHibrida2medida2 += Math.abs(erroHibrido);
            					contadorHibrido2medida2++;
            					
            					sum_erro_hibrida2_visao2[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida2_visao2[i] += 1;
                        	}else if(qtdAvaliacoes > 100){
                        		sum_maesHibrida2medida3 += Math.abs(erroHibrido);
            					contadorHibrido2medida3++;
            					
            					sum_erro_hibrida2_visao3[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida2_visao3[i] += 1;
                        	}
            				
            				//
                        	// MEDIDA ESPARSIDADE 3 
                        	// QUANTIDAE AVALIAÇÕES DO USUÁRIOS ATIVO DIVIDIDO PELO DOBRO DA MEDIANA
                        	//
                        	
            				taxaRatings = (double)dataModel.getPreferencesFromUser(i).length()/(2*6);	
            				if(taxaRatings > 1){
            					taxaRatings = 1;
            				}
            				if(rui == 0){
            					taxaRatings = 1;
            				}   
            				if(notaPreditaSemRound == 0){
            					taxaRatings = 0;
            				}
            				
            				notaHibrida = (1 - taxaRatings)*rui + taxaRatings*notaPreditaSemRound;
            				notaHibrida = Math.round(notaHibrida); 
            				sum_maesHibrida3medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				erroHibrido = Integer.parseInt(nota) - notaHibrida;
            				
            				sum_erro_hibrida3[i] += Math.abs(erroHibrido);
             				quantidade_por_usuario_hibrida3[i] += 1;
            				
            				if(qtdAvaliacoes <= 10){
            					sum_maesHibrida3medida1 += Math.abs(erroHibrido);
            					contadorHibrido3medida1++;
            					
            					sum_erro_hibrida3_visao1[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida3_visao1[i] += 1;                			
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		sum_maesHibrida3medida2 += Math.abs(erroHibrido);
            					contadorHibrido3medida2++;
            					
            					sum_erro_hibrida3_visao2[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida3_visao2[i] += 1;                			
                        	}else if(qtdAvaliacoes > 100){
                        		sum_maesHibrida3medida3 += Math.abs(erroHibrido);
            					contadorHibrido3medida3++;
            					
            					sum_erro_hibrida3_visao3[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida3_visao3[i] += 1;                			
                        	}
            				
            				
            				
            				//
                        	// MEDIDA ESPARSIDADE 4
                        	// QUANTIDAE DE AMIGOS QUE AVALIARAM DIVIDIDO PELO DOBRO DA MEDIANA
                        	//
                        	
            				taxaRatings = (double)qtdAmigosAvaliaramItem/(double)(2*6);
            				if(taxaRatings > 1){
            					taxaRatings = 1;
            				}
            				
            				if(rui == 0){
            					taxaRatings = 1;
            				}
            				
            				if(notaPreditaSemRound == 0){
            					taxaRatings = 0;
            				}
            				
            				notaHibrida = (1 - taxaRatings)*rui + taxaRatings*notaPreditaSemRound;
            				notaHibrida = Math.round(notaHibrida);
            				sum_maesHibrida4medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				erroHibrido = Integer.parseInt(nota) - notaHibrida;
            				
            				sum_erro_hibrida4[i] += Math.abs(erroHibrido);
             				quantidade_por_usuario_hibrida4[i] += 1;
            				
            				if(qtdAvaliacoes <= 10){
            					sum_maesHibrida4medida1 += Math.abs(erroHibrido);
            					contadorHibrido4medida1++;
            					
            					sum_erro_hibrida4_visao1[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida4_visao1[i] += 1;
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		sum_maesHibrida4medida2 += Math.abs(erroHibrido);
            					contadorHibrido4medida2++;
            					
            					sum_erro_hibrida4_visao2[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida4_visao2[i] += 1;
                        	}else if(qtdAvaliacoes > 100){
                        		sum_maesHibrida4medida3 += Math.abs(erroHibrido);
            					contadorHibrido4medida3++;
            					
            					sum_erro_hibrida4_visao3[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida4_visao3[i] += 1;
                        	}
            				
            				
            				//
                        	// MEDIDA ESPARSIDADE 5 
                        	//	BASEADA NO ARTIGO
            				// NUMERO DE AMIGOS QUE AVALIARAM O ITEM J DIVIDO PELO NUMERO TOTAL DE PESSOAS QUE AVALIAM J
                        	//
                        	
            				if(dataModel.getPreferencesForItem(j).length() != 0){
            					taxaRatings = qtdAmigosAvaliaramItem/(double)dataModel.getPreferencesForItem(j).length();
            				}else{
            					taxaRatings = 1;
            				}
            				if(rui == 0){
            					taxaRatings = 1;
            				}
            				
            				if(notaPreditaSemRound == 0){
            					taxaRatings = 0;
            				}
            				
            				notaHibrida = (1 - taxaRatings)*rui + taxaRatings*notaPreditaSemRound;
            				notaHibrida = Math.round(notaHibrida);
            				sum_maesHibrida5medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				erroHibrido = Integer.parseInt(nota) - notaHibrida;
            				
            				sum_erro_hibrida5[i] += Math.abs(erroHibrido);
             				quantidade_por_usuario_hibrida5[i] += 1;
            				
            				if(qtdAvaliacoes <= 10){
            					sum_maesHibrida5medida1 += Math.abs(erroHibrido);
            					contadorHibrido5medida1++;
            					
            					sum_erro_hibrida5_v1[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida5_v1[i] += 1;
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		sum_maesHibrida5medida2 += Math.abs(erroHibrido);
            					contadorHibrido5medida2++;
            					
            					sum_erro_hibrida5_v2[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida5_v2[i] += 1;
                        	}else if(qtdAvaliacoes > 100){
                        		sum_maesHibrida5medida3 += Math.abs(erroHibrido);
            					contadorHibrido5medida3++;
            					
            					sum_erro_hibrida5_v3[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida5_v3[i] += 1;
                        	}
            				//
                        	// MEDIDA ESPARSIDADE 6 
                        	// NUMERO DE AMIGOS QUE AVALIARAM O ITEM J DIVIDO PELO NÚMERO TOTAL DE AMIGOS
                        	//
                        	
            				if(scores.keySet().size() != 0){
            					taxaRatings = qtdAmigosAvaliaramItem/(double)scores.keySet().size();
            				}else{
            					taxaRatings = 1;
            				}
            				if(rui == 0){
            					taxaRatings = 1;
            				}
            				
            				if(notaPreditaSemRound == 0){
            					taxaRatings = 0;
            				}
            				
            				notaHibrida = (1 - taxaRatings)*rui + taxaRatings*notaPreditaSemRound;
            				notaHibrida = Math.round(notaHibrida);
            				sum_maesHibrida6medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				erroHibrido = Integer.parseInt(nota) - notaHibrida;
            				
            				sum_erro_hibrida6[i] += Math.abs(erroHibrido);
             				quantidade_por_usuario_hibrida6[i] += 1;
             				
            				if(qtdAvaliacoes <= 10){
            					sum_maesHibrida6medida1 += Math.abs(erroHibrido);
            					contadorHibrido6medida1++;
            					
            					sum_erro_hibrida6_v1[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida6_v1[i] += 1;
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		sum_maesHibrida6medida2 += Math.abs(erroHibrido);
            					contadorHibrido6medida2++;
            					
            					sum_erro_hibrida6_v2[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida6_v2[i] += 1;
                        	}else if(qtdAvaliacoes > 100){
                        		sum_maesHibrida6medida3 += Math.abs(erroHibrido);
            					contadorHibrido6medida3++;
            					
            					sum_erro_hibrida6_v3[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida6_v3[i] += 1;
                        	}
            				
            				notaHibrida = (0.5)*rui + 0.5*notaPreditaSemRound;
            				if(rui == 0){
            					notaHibrida = notaPreditaSemRound;
            				}
            				notaHibrida = Math.round(notaHibrida);
            				sum_maesHibrida7medida += Math.abs(Integer.parseInt(nota) - notaHibrida);
            				erroHibrido = Integer.parseInt(nota) - notaHibrida;
            				
            				sum_erro_hibrida7[i] += Math.abs(erroHibrido);
             				quantidade_por_usuario_hibrida7[i] += 1;
             				
            				if(qtdAvaliacoes <= 10){
            					sum_maesHibrida7medida1 += Math.abs(erroHibrido);
            					contadorHibrido7medida1++;
            					
            					sum_erro_hibrida7_v1[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida7_v1[i] += 1;
                        	}else if(qtdAvaliacoes > 10 && qtdAvaliacoes <= 100){
                        		sum_maesHibrida7medida2 += Math.abs(erroHibrido);
            					contadorHibrido7medida2++;
            					
            					sum_erro_hibrida7_v2[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida7_v2[i] += 1;
                        	}else if(qtdAvaliacoes > 100){
                        		sum_maesHibrida7medida3 += Math.abs(erroHibrido);
            					contadorHibrido7medida3++;
            					
            					sum_erro_hibrida7_v3[i] += Math.abs(erroHibrido);
                 				quantidade_por_usuario_hibrida7_v3[i] += 1;
                        	}
            				
            				
            				
                        	//fazer pra outras medidas de esparsidade (artigo)
            				//aumentar a profundidade do moletrust
                        	}
            				
            				
                        }
                        
                        erroMAE = sum_maes/contadorPontosMoleTrust;
                        erroRMSE = Math.sqrt(sum_rmses/contadorPontosMoleTrust);                        
                        erroMAE1 = sum_maesMole1/contadorPontos1;
                        erroMAE2 = sum_maesMole2/contadorPontos2;
                        erroMAE3 = sum_maesMole3/contadorPontos3;
                        
                        erroMAEMatriz = sum_maesMatriz/contadorPontosM;
                        erroMAEMatriz1 = sum_maesMatriz1/contadorPontosM1;
                        erroMAEMatriz2 = sum_maesMatriz2/contadorPontosM2;
                        erroMAEMatriz3 = sum_maesMatriz3/contadorPontosM3;
                        erroMAEMatrizSemRound = sum_maesMatrizSemRound/contadorPontosM;
                        
                        System.out.println("MAE MOLETRUST1 GERAL SEM ROUND " + sum_maesSemRound/contadorPontosMoleTrust); 
                        System.out.println("MAE MOLETRUST1 GERAL ROUND " + erroMAE + " RMSE: " + erroRMSE);    
                        System.out.println("Quantidade total "+contadorTotal+" Quantidade geral predita por moletrust "+contadorPontosMoleTrust);
                        
                        System.out.println("MAE MOLETRUST1 <= 10 " + erroMAE1 + " QTD: " + contadorPontos1); 
                        System.out.println("MAE MOLETRUST1 ENTRE 10 E 100 " + erroMAE2 + " QTD: " + contadorPontos2); 
                        System.out.println("MAE MOLETRUST1 MAIOR QUE 100 " + erroMAE3 + " QTD: " + contadorPontos3); 
                        
                        System.out.println("MAE matriz : " + erroMAEMatriz + " Quantidade" + contadorPontosM);
                        System.out.println("MAE matriz sem round : " + erroMAEMatrizSemRound + " Quantidade M " + contadorPontosM);
                        System.out.println("MAE matriz <= 10: " + erroMAEMatriz1 + " Quantidade" + contadorPontosM1);
                        System.out.println("MAE matriz ENTRE 10 E 100: " + erroMAEMatriz2 + " Quantidade" + contadorPontosM2);
                        System.out.println("MAE matriz MAIOR QUE 100: " + erroMAEMatriz3 + " Quantidade" + contadorPontosM3);
                        
                        
                        System.out.println("MAE HIBRIDA MEDIDA 1 <= 10: " + sum_maesHibrida1/contadorHibrido1 + " Quantidade " + contadorHibrido1);
                        System.out.println("MAE HIBRIDA MEDIDA1 ENTRE 10 E 100: " + sum_maesHibrida2/contadorHibrido2 + " Quantidade " + contadorHibrido2);
                        System.out.println("MAE HIBRIDA MEDIDA 1 MAIOR QUE 100: " + sum_maesHibrida3/contadorHibrido3 + " Quantidade " + contadorHibrido3);
                        System.out.println("MAE HIBRIDA MEDIDA 2 <= 10: " + sum_maesHibrida2medida1/contadorHibrido2medida1 );
                        System.out.println("MAE HIBRIDA MEDIDA 2 ENTRE 10 E 100: " + sum_maesHibrida2medida2/contadorHibrido2medida2 );
                        System.out.println("MAE HIBRIDA MEDIDA 2 MAIOR QUE 100: " + sum_maesHibrida2medida3/contadorHibrido2medida3 );
                        System.out.println("MAE HIBRIDA MEDIDA 3 <= 10: " + sum_maesHibrida3medida1/contadorHibrido3medida1 );
                        System.out.println("MAE HIBRIDA MEDIDA 3 ENTRE 10 E 100: " + sum_maesHibrida3medida2/contadorHibrido3medida2 );
                        System.out.println("MAE HIBRIDA MEDIDA 3 MAIOR QUE 100: " + sum_maesHibrida3medida3/contadorHibrido3medida3 );
                        System.out.println("MAE HIBRIDA MEDIDA 4 <= 10: " + sum_maesHibrida4medida1/contadorHibrido4medida1 );
                        System.out.println("MAE HIBRIDA MEDIDA 4 ENTRE 10 E 100: " + sum_maesHibrida4medida2/contadorHibrido4medida2 );
                        System.out.println("MAE HIBRIDA MEDIDA 4 MAIOR QUE 100: " + sum_maesHibrida4medida3/contadorHibrido4medida3 );
                        System.out.println("MAE HIBRIDA MEDIDA 5 <= 10: " + sum_maesHibrida5medida1/contadorHibrido5medida1 );
                        System.out.println("MAE HIBRIDA MEDIDA 5 ENTRE 10 E 100: " + sum_maesHibrida5medida2/contadorHibrido5medida2 );
                        System.out.println("MAE HIBRIDA MEDIDA 5 MAIOR QUE 100: " + sum_maesHibrida5medida3/contadorHibrido5medida3 );
                        System.out.println("MAE HIBRIDA MEDIDA 6 <= 10: " + sum_maesHibrida6medida1/contadorHibrido6medida1 );
                        System.out.println("MAE HIBRIDA MEDIDA 6 ENTRE 10 E 100: " + sum_maesHibrida6medida2/contadorHibrido6medida2 );
                        System.out.println("MAE HIBRIDA MEDIDA 6 MAIOR QUE 100: " + sum_maesHibrida6medida3/contadorHibrido6medida3 );
                        System.out.println("MAE HIBRIDA MEDIDA 7 <= 10: " + sum_maesHibrida7medida1/contadorHibrido7medida1 );
                        System.out.println("MAE HIBRIDA MEDIDA 7 ENTRE 10 E 100: " + sum_maesHibrida7medida2/contadorHibrido7medida2 );
                        System.out.println("MAE HIBRIDA MEDIDA 7 MAIOR QUE 100: " + sum_maesHibrida7medida3/contadorHibrido7medida3 );
                        
                        System.out.println(erroMAE+" "+sum_maesMole1/contadorPontos1+" "+sum_maesMole2/contadorPontos2+" "+sum_maesMole3/contadorPontos3);
                        System.out.println(erroMAEMatriz+ " "+erroMAEMatriz1+" "+erroMAEMatriz2+" "+erroMAEMatriz3);
                        System.out.println(sum_maesHibrida/contadorHibrido+" "+sum_maesHibrida1/contadorHibrido1+" "+sum_maesHibrida2/contadorHibrido2+" "+sum_maesHibrida3/contadorHibrido3);
                        System.out.println(sum_maesHibrida2medida/contadorHibrido+" "+sum_maesHibrida2medida1/contadorHibrido2medida1+" "+sum_maesHibrida2medida2/contadorHibrido2medida2+" "+sum_maesHibrida2medida3/contadorHibrido2medida3);
                        System.out.println(sum_maesHibrida3medida/contadorHibrido+ " "+ sum_maesHibrida3medida1/contadorHibrido3medida1+" "+sum_maesHibrida3medida2/contadorHibrido3medida2+" "+sum_maesHibrida3medida3/contadorHibrido3medida3);
                        System.out.println(sum_maesHibrida4medida/contadorHibrido+ " "+ sum_maesHibrida4medida1/contadorHibrido4medida1+" "+sum_maesHibrida4medida2/contadorHibrido4medida2+" "+sum_maesHibrida4medida3/contadorHibrido4medida3);
                        System.out.println(sum_maesHibrida5medida/contadorHibrido+ " "+ sum_maesHibrida5medida1/contadorHibrido5medida1+" "+sum_maesHibrida5medida2/contadorHibrido5medida2+" "+sum_maesHibrida5medida3/contadorHibrido5medida3);
                        System.out.println(sum_maesHibrida6medida/contadorHibrido+ " "+ sum_maesHibrida6medida1/contadorHibrido6medida1+" "+sum_maesHibrida6medida2/contadorHibrido6medida2+" "+sum_maesHibrida6medida3/contadorHibrido6medida3);
                        System.out.println(sum_maesHibrida7medida/contadorHibrido+ " "+ sum_maesHibrida7medida1/contadorHibrido7medida1+" "+sum_maesHibrida7medida2/contadorHibrido7medida2+" "+sum_maesHibrida7medida3/contadorHibrido7medida3);                        
                        System.out.println(contadorTotal+" "+contadorPontos1teste+" "+contadorPontos2teste+" "+contadorPontos3teste);
                        System.out.println(contadorPontosMoleTrust + " "+contadorPontos1+" "+contadorPontos2+" "+contadorPontos3);
                        System.out.println(contadorPontosM + " "+contadorPontosM1+" "+contadorPontosM2+" "+contadorPontosM3);
                        System.out.println(contadorHibrido1+contadorHibrido2+contadorHibrido3+" "+contadorHibrido1+" "+contadorHibrido2+" "+contadorHibrido3);
                        //System.out.println(sum_maesHibrida1/contadorHibrido1+","+sum_maesHibrida2/contadorHibrido2+","+sum_maesHibrida3/contadorHibrido3);
                        
                        System.out.println("MAE HIBRIDA MEDIDA 1 TODOS: " + sum_maesHibrida/contadorHibrido );
                        System.out.println("MAE HIBRIDA MEDIDA 2 TODOS: " + sum_maesHibrida2medida/contadorHibrido );
                        System.out.println("MAE HIBRIDA MEDIDA 3 TODOS: " + sum_maesHibrida3medida/contadorHibrido );                        
                        System.out.println("MAE HIBRIDA MEDIDA 4 TODOS: " + sum_maesHibrida4medida/contadorHibrido );
                        System.out.println("MAE HIBRIDA MEDIDA 5 TODOS: " + sum_maesHibrida5medida/contadorHibrido );
                        System.out.println("MAE HIBRIDA MEDIDA 6 TODOS: " + sum_maesHibrida6medida/contadorHibrido );
                        System.out.println("MAE HIBRIDA MEDIDA 7 TODOS: " + sum_maesHibrida7medida/contadorHibrido );
                        
                        System.out.println("total de testes menor 10 " + contadorPontos1teste );
                        System.out.println("total de testes entre 10 e 100 aval " + contadorPontos2teste );
                        System.out.println("total de testes maior que 100 aval " + contadorPontos3teste );
                        
                        System.out.println("MAE MATRIZ ITENS NICHO 1: " + sum_maeItensNichoMatriz1/contadorItensNichoMatriz1 + "qtd " + contadorItensNichoMatriz1);
                        System.out.println("MAE MATRIZ ITENS NICHO 2: " + sum_maeItensNichoMatriz2/contadorItensNichoMatriz2 + "qtd " + contadorItensNichoMatriz2);
                        
                        System.out.println("MAE MATRIZ ITENS controversos 1: " + sum_maeItensControvMatriz1/contadorItensControvMatriz1 + "qtd " + contadorItensControvMatriz1);
                        System.out.println("MAE MATRIZ ITENS controversos 2: " + sum_maeItensControvMatriz2/contadorItensControvMatriz2 + "qtd " + contadorItensControvMatriz2);
                        
                        
                        // CALCULANDO MAE POR USUARIO
                        double[] mae_user = new double[quantidade_usuarios];
                        double[] mae_user_matriz = new double[quantidade_usuarios];
                        double[] mae_user_hibrida1 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida2 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida3 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida4 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida5 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida6 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida7 = new double[quantidade_usuarios];
                        for(int b = 0; b <= 49289; b++){
                        	if(quantidade_por_usuario[b] > 0){
                        		mae_user[b] = sum_erro[b]/quantidade_por_usuario[b];
                        	}
                        	
                        	if(quantidade_por_usuario_matriz[b] > 0){
                        		mae_user_matriz[b] = sum_erro_matriz[b]/quantidade_por_usuario_matriz[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida1[b] > 0){
                        		mae_user_hibrida1[b] = sum_erro_hibrida1[b]/quantidade_por_usuario_hibrida1[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida2[b] > 0){
                        		mae_user_hibrida2[b] = sum_erro_hibrida2[b]/quantidade_por_usuario_hibrida2[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida3[b] > 0){
                        		mae_user_hibrida3[b] = sum_erro_hibrida3[b]/quantidade_por_usuario_hibrida3[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida4[b] > 0){
                        		mae_user_hibrida4[b] = sum_erro_hibrida4[b]/quantidade_por_usuario_hibrida4[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida5[b] > 0){
                        		mae_user_hibrida5[b] = sum_erro_hibrida5[b]/quantidade_por_usuario_hibrida5[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida6[b] > 0){
                        		mae_user_hibrida6[b] = sum_erro_hibrida6[b]/quantidade_por_usuario_hibrida6[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida7[b] > 0){
                        		mae_user_hibrida7[b] = sum_erro_hibrida7[b]/quantidade_por_usuario_hibrida7[b];
                        	}
                        	
                        }
                        
                        // CALCULANDO A MEDIA MAE POR USUARIO NA VISAO 1
                        
                     // CALCULANDO MAE POR USUARIO
                        
                        double[] mae_user_matriz_v1 = new double[quantidade_usuarios];
                        double[] mae_user_moletrust_v1 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida1_v1 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida2_v1 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida3_v1 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida4_v1 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida5_v1 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida6_v1 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida7_v1 = new double[quantidade_usuarios];
                        
                        double[] mae_user_matriz_v2 = new double[quantidade_usuarios];
                        double[] mae_user_moletrust_v2 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida1_v2 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida2_v2 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida3_v2 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida4_v2 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida5_v2 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida6_v2 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida7_v2 = new double[quantidade_usuarios];
                        
                        double[] mae_user_matriz_v3 = new double[quantidade_usuarios];
                        double[] mae_user_moletrust_v3 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida1_v3 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida2_v3 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida3_v3 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida4_v3 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida5_v3 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida6_v3 = new double[quantidade_usuarios];
                        double[] mae_user_hibrida7_v3 = new double[quantidade_usuarios];
                        
                        for(int b = 0; b <= 49289; b++){

                        	
                        	if(quantidade_por_usuario_moletrust_visao1[b] > 0){
                        		mae_user_moletrust_v1[b] = sum_erro_moletrust_visao1[b]/quantidade_por_usuario_moletrust_visao1[b];
                        	}
                        	
                        	if(quantidade_por_usuario_matriz_visao1[b] > 0){
                        		mae_user_matriz_v1[b] = sum_erro_matriz_visao1[b]/quantidade_por_usuario_matriz_visao1[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida1_visao1[b] > 0){
                        		mae_user_hibrida1_v1[b] = sum_erro_hibrida1_visao1[b]/quantidade_por_usuario_hibrida1_visao1[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida2_visao1[b] > 0){
                        		mae_user_hibrida2_v1[b] = sum_erro_hibrida2_visao1[b]/quantidade_por_usuario_hibrida2_visao1[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida3_visao1[b] > 0){
                        		mae_user_hibrida3_v1[b] = sum_erro_hibrida3_visao1[b]/quantidade_por_usuario_hibrida3_visao1[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida4_visao1[b] > 0){
                        		mae_user_hibrida4_v1[b] = sum_erro_hibrida4_visao1[b]/quantidade_por_usuario_hibrida4_visao1[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida5_v1[b] > 0){
                        		mae_user_hibrida5_v1[b] = sum_erro_hibrida5_v1[b]/quantidade_por_usuario_hibrida5_v1[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida6_v1[b] > 0){
                        		mae_user_hibrida6_v1[b] = sum_erro_hibrida6_v1[b]/quantidade_por_usuario_hibrida6_v1[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida7_v1[b] > 0){
                        		mae_user_hibrida7_v1[b] = sum_erro_hibrida7_v1[b]/quantidade_por_usuario_hibrida7_v1[b];
                        	}
                        	
                        	// ----------------------- VISÃO 2 ----------------
                        	
                        	if(quantidade_por_usuario_moletrust_visao2[b] > 0){
                        		mae_user_moletrust_v2[b] = sum_erro_moletrust_visao2[b]/quantidade_por_usuario_moletrust_visao2[b];
                        	}
                        	
                        	if(quantidade_por_usuario_matriz_visao2[b] > 0){
                        		mae_user_matriz_v2[b] = sum_erro_matriz_visao2[b]/quantidade_por_usuario_matriz_visao2[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida1_visao2[b] > 0){
                        		mae_user_hibrida1_v2[b] = sum_erro_hibrida1_visao2[b]/quantidade_por_usuario_hibrida1_visao2[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida2_visao2[b] > 0){
                        		mae_user_hibrida2_v2[b] = sum_erro_hibrida2_visao2[b]/quantidade_por_usuario_hibrida2_visao2[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida3_visao2[b] > 0){
                        		mae_user_hibrida3_v2[b] = sum_erro_hibrida3_visao2[b]/quantidade_por_usuario_hibrida3_visao2[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida4_visao2[b] > 0){
                        		mae_user_hibrida4_v2[b] = sum_erro_hibrida4_visao2[b]/quantidade_por_usuario_hibrida4_visao2[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida5_v2[b] > 0){
                        		mae_user_hibrida5_v2[b] = sum_erro_hibrida5_v2[b]/quantidade_por_usuario_hibrida5_v2[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida6_v2[b] > 0){
                        		mae_user_hibrida6_v2[b] = sum_erro_hibrida6_v2[b]/quantidade_por_usuario_hibrida6_v2[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida7_v2[b] > 0){
                        		mae_user_hibrida7_v2[b] = sum_erro_hibrida7_v2[b]/quantidade_por_usuario_hibrida7_v2[b];
                        	}
                        	
                        	// ----------------------- VISÃO 3 ----------------
                        	
                        	if(quantidade_por_usuario_moletrust_visao3[b] > 0){
                        		mae_user_moletrust_v3[b] = sum_erro_moletrust_visao3[b]/quantidade_por_usuario_moletrust_visao3[b];
                        	}
                        	
                        	if(quantidade_por_usuario_matriz_visao3[b] > 0){
                        		mae_user_matriz_v3[b] = sum_erro_matriz_visao3[b]/quantidade_por_usuario_matriz_visao3[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida1_visao3[b] > 0){
                        		mae_user_hibrida1_v3[b] = sum_erro_hibrida1_visao3[b]/quantidade_por_usuario_hibrida1_visao3[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida2_visao3[b] > 0){
                        		mae_user_hibrida2_v3[b] = sum_erro_hibrida2_visao3[b]/quantidade_por_usuario_hibrida2_visao3[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida3_visao3[b] > 0){
                        		mae_user_hibrida3_v3[b] = sum_erro_hibrida3_visao3[b]/quantidade_por_usuario_hibrida3_visao3[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida4_visao3[b] > 0){
                        		mae_user_hibrida4_v3[b] = sum_erro_hibrida4_visao3[b]/quantidade_por_usuario_hibrida4_visao3[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida5_v3[b] > 0){
                        		mae_user_hibrida5_v3[b] = sum_erro_hibrida5_v3[b]/quantidade_por_usuario_hibrida5_v3[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida6_v3[b] > 0){
                        		mae_user_hibrida6_v3[b] = sum_erro_hibrida6_v3[b]/quantidade_por_usuario_hibrida6_v3[b];
                        	}
                        	
                        	if(quantidade_por_usuario_hibrida7_v3[b] > 0){
                        		mae_user_hibrida7_v3[b] = sum_erro_hibrida7_v3[b]/quantidade_por_usuario_hibrida7_v3[b];
                        	}
                        	
                        	
                        }
                        
                        
                        
                        double mae_moletrust = 0;
                        double total_usuarios = 0;
                        double mae_matriz = 0;
                        double total_usuarios_matriz = 0;
                        double mae_hibrida1 = 0;
                        double total_usuarios_hibrida1 = 0;
                        double mae_hibrida2 = 0;
                        double mae_hibrida3 = 0;
                        double mae_hibrida4 = 0;
                        double mae_hibrida5 = 0;
                        double mae_hibrida6 = 0;
                        double mae_hibrida7 = 0;
                        
                        for(int b = 0; b <= 49289; b++){
                        	if(quantidade_por_usuario[b] > 0){
                        		mae_moletrust += mae_user[b];
                        		total_usuarios +=1; 
                        	}
                        	if(quantidade_por_usuario_matriz[b] > 0){
                        		mae_matriz += mae_user_matriz[b];
                        		total_usuarios_matriz += 1;
                        	}
                        	if(quantidade_por_usuario_hibrida1[b] > 0){
                        		mae_hibrida1 += mae_user_hibrida1[b];
                        		total_usuarios_hibrida1 += 1;
                        		// todos os hibridos tem o mesmo numero de usuarios
                        		mae_hibrida2 += mae_user_hibrida2[b];
                        		mae_hibrida3 += mae_user_hibrida3[b];
                        		mae_hibrida4 += mae_user_hibrida4[b];
                        		mae_hibrida5 += mae_user_hibrida5[b];
                        		mae_hibrida6 += mae_user_hibrida6[b];
                        		mae_hibrida7 += mae_user_hibrida7[b];
                        	}
                        }
                        
                        /// visões
                        double mae_moletrust_v1= 0;
                        double total_usuarios_moletrust_v1 = 0;
                        double mae_matriz_v1 = 0;
                        double total_usuarios_matriz_v1 = 0;
                        double mae_hibrida1_v1 = 0;
                        double total_usuarios_hibrida1_v1 = 0;
                        double mae_hibrida2_v1 = 0;
                        double mae_hibrida3_v1 = 0;
                        double mae_hibrida4_v1 = 0;
                        double mae_hibrida5_v1 = 0;
                        double mae_hibrida6_v1 = 0;
                        double mae_hibrida7_v1 = 0;
                        
                        double mae_moletrust_v2= 0;
                        double total_usuarios_moletrust_v2 = 0;
                        double mae_matriz_v2 = 0;
                        double total_usuarios_matriz_v2 = 0;
                        double mae_hibrida1_v2 = 0;
                        double total_usuarios_hibrida1_v2 = 0;
                        double mae_hibrida2_v2 = 0;
                        double mae_hibrida3_v2 = 0;
                        double mae_hibrida4_v2 = 0;
                        double mae_hibrida5_v2 = 0;
                        double mae_hibrida6_v2 = 0;
                        double mae_hibrida7_v2 = 0;
                        
                        double mae_moletrust_v3= 0;
                        double total_usuarios_moletrust_v3 = 0;
                        double mae_matriz_v3 = 0;
                        double total_usuarios_matriz_v3 = 0;
                        double mae_hibrida1_v3 = 0;
                        double total_usuarios_hibrida1_v3 = 0;
                        double mae_hibrida2_v3 = 0;
                        double mae_hibrida3_v3 = 0;
                        double mae_hibrida4_v3 = 0;
                        double mae_hibrida5_v3 = 0;
                        double mae_hibrida6_v3 = 0;
                        double mae_hibrida7_v3 = 0;
                        
                        int num_usuarios_visao1 = 0;
                        int num_usuarios_visao2 = 0;
                        int num_usuarios_visao3 = 0;

                        
                        for(int b = 0; b <= 49289; b++){
                        	if(quantidade_por_usuario_moletrust_visao1[b] > 0){
                        		mae_moletrust_v1 += mae_user_moletrust_v1[b];
                        		total_usuarios_moletrust_v1 +=1; 
                        	}
                        	if(quantidade_por_usuario_matriz_visao1[b] > 0){
                        		mae_matriz_v1 += mae_user_matriz_v1[b];
                        		total_usuarios_matriz_v1 += 1;
                        	}
                        	if(quantidade_por_usuario_hibrida1_visao1[b] > 0){
                        		mae_hibrida1_v1 += mae_user_hibrida1_v1[b];
                        		total_usuarios_hibrida1_v1 += 1;
                        		// todos os hibridos tem o mesmo numero de usuarios
                        		mae_hibrida2_v1 += mae_user_hibrida2_v1[b];
                        		mae_hibrida3_v1 += mae_user_hibrida3_v1[b];
                        		mae_hibrida4_v1 += mae_user_hibrida4_v1[b];
                        		mae_hibrida5_v1 += mae_user_hibrida5_v1[b];
                        		mae_hibrida6_v1 += mae_user_hibrida6_v1[b];
                        		mae_hibrida7_v1 += mae_user_hibrida7_v1[b];
                        	}
                        	
                        	// visão 2
                        	
                        	if(quantidade_por_usuario_moletrust_visao2[b] > 0){
                        		mae_moletrust_v2 += mae_user_moletrust_v2[b];
                        		total_usuarios_moletrust_v2 +=1; 
                        	}
                        	if(quantidade_por_usuario_matriz_visao2[b] > 0){
                        		mae_matriz_v2 += mae_user_matriz_v2[b];
                        		total_usuarios_matriz_v2 += 1;
                        	}
                        	if(quantidade_por_usuario_hibrida1_visao2[b] > 0){
                        		mae_hibrida1_v2 += mae_user_hibrida1_v2[b];
                        		total_usuarios_hibrida1_v2 += 1;
                        		// todos os hibridos tem o mesmo numero de usuarios
                        		mae_hibrida2_v2 += mae_user_hibrida2_v2[b];
                        		mae_hibrida3_v2 += mae_user_hibrida3_v2[b];
                        		mae_hibrida4_v2 += mae_user_hibrida4_v2[b];
                        		mae_hibrida5_v2 += mae_user_hibrida5_v2[b];
                        		mae_hibrida6_v2 += mae_user_hibrida6_v2[b];
                        		mae_hibrida7_v2 += mae_user_hibrida7_v2[b];
                        	}
                        	
                        	// visão 3
                        	
                        	if(quantidade_por_usuario_moletrust_visao3[b] > 0){
                        		mae_moletrust_v3 += mae_user_moletrust_v3[b];
                        		total_usuarios_moletrust_v3 +=1; 
                        	}
                        	if(quantidade_por_usuario_matriz_visao3[b] > 0){
                        		mae_matriz_v3 += mae_user_matriz_v3[b];
                        		total_usuarios_matriz_v3 += 1;
                        	}
                        	if(quantidade_por_usuario_hibrida1_visao3[b] > 0){
                        		mae_hibrida1_v3 += mae_user_hibrida1_v3[b];
                        		total_usuarios_hibrida1_v3 += 1;
                        		// todos os hibridos tem o mesmo numero de usuarios
                        		mae_hibrida2_v3 += mae_user_hibrida2_v3[b];
                        		mae_hibrida3_v3 += mae_user_hibrida3_v3[b];
                        		mae_hibrida4_v3 += mae_user_hibrida4_v3[b];
                        		mae_hibrida5_v3 += mae_user_hibrida5_v3[b];
                        		mae_hibrida6_v3 += mae_user_hibrida6_v3[b];
                        		mae_hibrida7_v3 += mae_user_hibrida7_v3[b];
                        	}
                        	
                        	if(usuarios_visao1[b] > 0){
                        		num_usuarios_visao1 +=1;
                        	}                        	
                        	if(usuarios_visao2[b] > 0){
                        		num_usuarios_visao2 +=1;
                        	}                        	
                        	if(usuarios_visao3[b] > 0){
                        		num_usuarios_visao3 +=1;
                        	}
                        	
                        }
                        // FIM DO CALCULO DE MAE POR USUARIO
//                        System.out.println("mae por ususario moletrust " + mae_moletrust/total_usuarios + " qtd "+total_usuarios);                        
//                        System.out.println("mae por ususario matriz " + mae_matriz/total_usuarios_matriz + " qtd "+total_usuarios_matriz);
//                        System.out.println("mae por ususario hibrida1 " + mae_hibrida1/total_usuarios_hibrida1 + " qtd users"+total_usuarios_hibrida1);
//                        System.out.println("mae por ususario hibrida2 " + mae_hibrida2/total_usuarios_hibrida1 + " qtd "+total_usuarios_hibrida1);
//                        System.out.println("mae por ususario hibrida3 " + mae_hibrida3/total_usuarios_hibrida1 + " qtd "+total_usuarios_hibrida1);
//                        System.out.println("mae por ususario hibrida4 " + mae_hibrida4/total_usuarios_hibrida1 + " qtd "+total_usuarios_hibrida1);
//                        System.out.println("mae por ususario hibrida5 " + mae_hibrida5/total_usuarios_hibrida1 + " qtd "+total_usuarios_hibrida1);
//                        System.out.println("mae por ususario hibrida6 " + mae_hibrida6/total_usuarios_hibrida1 + " qtd "+total_usuarios_hibrida1);
//                        System.out.println("mae por ususario hibrida7 " + mae_hibrida7/total_usuarios_hibrida1 + " qtd "+total_usuarios_hibrida1);
                        
                        System.out.println("mae por ususario");
	                    System.out.println("moletrust " +mae_moletrust/total_usuarios+" "+mae_moletrust_v1/total_usuarios_moletrust_v1+" "+mae_moletrust_v2/total_usuarios_moletrust_v2+" "+mae_moletrust_v3/total_usuarios_moletrust_v3);
	                    System.out.println("fatoração " +mae_matriz/total_usuarios_matriz+" "+mae_matriz_v1/total_usuarios_matriz_v1+" "+mae_matriz_v2/total_usuarios_matriz_v2+" "+mae_matriz_v3/total_usuarios_matriz_v3);
	                    System.out.println("hibrida1 " +mae_hibrida1/total_usuarios_hibrida1+" "+mae_hibrida1_v1/total_usuarios_hibrida1_v1+" "+mae_hibrida1_v2/total_usuarios_hibrida1_v2+" "+mae_hibrida1_v3/total_usuarios_hibrida1_v3);
	                    System.out.println("hibrida2 "+mae_hibrida2/total_usuarios_hibrida1+" "+mae_hibrida2_v1/total_usuarios_hibrida1_v1+" "+mae_hibrida2_v2/total_usuarios_hibrida1_v2+" "+mae_hibrida2_v3/total_usuarios_hibrida1_v3);
	                    System.out.println("hibrida3 "+mae_hibrida3/total_usuarios_hibrida1+" "+mae_hibrida3_v1/total_usuarios_hibrida1_v1+" "+mae_hibrida3_v2/total_usuarios_hibrida1_v2+" "+mae_hibrida3_v3/total_usuarios_hibrida1_v3);
	                    System.out.println("hibrida4 "+mae_hibrida4/total_usuarios_hibrida1+" "+mae_hibrida4_v1/total_usuarios_hibrida1_v1+" "+mae_hibrida4_v2/total_usuarios_hibrida1_v2+" "+mae_hibrida4_v3/total_usuarios_hibrida1_v3);
	                    System.out.println("hibrida5 "+mae_hibrida5/total_usuarios_hibrida1+" "+mae_hibrida5_v1/total_usuarios_hibrida1_v1+" "+mae_hibrida5_v2/total_usuarios_hibrida1_v2+" "+mae_hibrida5_v3/total_usuarios_hibrida1_v3);
	                    System.out.println("hibrida6 "+mae_hibrida6/total_usuarios_hibrida1+" "+mae_hibrida6_v1/total_usuarios_hibrida1_v1+" "+mae_hibrida6_v2/total_usuarios_hibrida1_v2+" "+mae_hibrida6_v3/total_usuarios_hibrida1_v3);
	                    System.out.println("hibrida7 "+mae_hibrida7/total_usuarios_hibrida1+" "+mae_hibrida7_v1/total_usuarios_hibrida1_v1+" "+mae_hibrida7_v2/total_usuarios_hibrida1_v2+" "+mae_hibrida7_v3/total_usuarios_hibrida1_v3);
	                    
	                    System.out.println(num_usuarios_visao1+num_usuarios_visao2+num_usuarios_visao3+" "+num_usuarios_visao1+" "+num_usuarios_visao2+ " "+num_usuarios_visao3);
	                    System.out.println(total_usuarios+" "+total_usuarios_moletrust_v1+" "+total_usuarios_moletrust_v2+" "+total_usuarios_moletrust_v3);
	                    System.out.println(total_usuarios_matriz+" "+total_usuarios_matriz_v1+" "+total_usuarios_matriz_v2+" "+total_usuarios_matriz_v3);
	                    System.out.println(total_usuarios_hibrida1+" "+total_usuarios_hibrida1_v1+" "+total_usuarios_hibrida1_v2+" "+total_usuarios_hibrida1_v3);
	                   
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
		List<String> nodes = new ArrayList<>(47000);
		// source user - edges[target users - trust value]
		Map<String, Map<String, Double>> edges = new HashMap<>(47000);

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