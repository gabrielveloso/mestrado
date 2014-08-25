


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.RatingSGDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.math.SparseMatrix;


//http://ssc.io/wp-content/uploads/2013/02/cf-mahout.pdf
//http://pt.slideshare.net/sscdotopen/next-directions-in-mahouts-recommenders
//http://ssc.io/wp-content/uploads/2013/05/newdirections.pdf
// http://mymedialite.net/documentation/doxygen/class_my_media_lite_1_1_rating_prediction_1_1_s_v_d_plus_plus.html
// https://github.com/zenogantner/MyMediaLite/blob/master/src/MyMediaLite/RatingPrediction/SVDPlusPlus.cs
// http://www.columbia.edu/~srh2144/netflix2.pdf
// http://www.cnblogs.com/bianwenlong/p/3576776.html

//http://mahout.apache.org/users/recommender/recommender-documentation.html
//http://www.ehow.com.br/calcular-rmse-raiz-quadrada-erro-quadratico-medio-como_45534/
//como dividir o cojunto de teste e treinamento
// http://www.public.asu.edu/~jtang20/publication/globalandlocal.pdf


public class Matrix {

	public void SVD(DataModel dataModel) throws TasteException{
        System.out.println("-----------------------------------------------------------------------------");
        RecommenderEvaluator evaluator=new RMSRecommenderEvaluator();
        
        RecommenderBuilder builder=new RecommenderBuilder(){
            @Override
            public Recommender buildRecommender(DataModel dataModel)
                    throws TasteException {
                // TODO Auto-generated method stub
                return new SVDRecommender(dataModel,new SVDPlusPlusFactorizer(dataModel,10, 0.0002 , 0.02,0.01, 100, 1.0));
            }
        };
        
        double score=evaluator.evaluate(builder, null, dataModel, 0.7, 1.0);
        System.out.println("SVD score is "+score);
    }
	
	public static void main(String[] args) {
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("data/ratings_data_treinamento.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("data/ratings_treinamento.cvs"));
			
			String line = "";
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				bw.write(values[0] + "," + values[1] + "," + values[2] + "\n");
			}
			
			br.close();
			bw.close();
			// criação do objeto dataModel a partir do arquivo cvs
			//DataModel dataModel = new FileDataModel(new File("data/ratings.cvs"));
			
			br = new BufferedReader(new FileReader("data/trust_data.txt"));
			bw = new BufferedWriter(new FileWriter("data/trust.cvs"));
			
			line = "";
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				bw.write(values[1] + "," + values[2] + "," + values[3] + "\n");
			}
			
			br.close();
			bw.close();
			
			
			DataModel dataModel = new FileDataModel(new File("data/ratings.cvs"));
			DataModel dataModelTrust = new FileDataModel(new File("data/trust.cvs"));
			
			//DataModel dataModelFinal = new FileDataModel(new File("data/ratings.cvs"));
					
			try {
				/*SVDPlusPlusFactorizer  factorizer = 
						new SVDPlusPlusFactorizer(dataModel, 
								10, 0.0002 , 0.02, 
								0.01, 100, 1.0);
				
				SVDRecommender rec = new SVDRecommender(dataModel, factorizer);
				
				/*int i = 0;
				for(LongPrimitiveIterator users = dataModel.getUserIDs(); users.hasNext();){
					Long userID = users.nextLong();
					List<RecommendedItem> itensRecomendados = rec.recommend(userID, 5);
					
					for(RecommendedItem item : itensRecomendados){
						System.out.println(userID + ","+item.getItemID()+ ","+ item.getValue());
					}
					
					i++;
					if(i > 10){
						System.exit(1);
					}
				}*/;
				
					RatingSGDFactorizer factorizerPadrao = new RatingSGDFactorizer(dataModel,5, 0.0002 , 0.02,0.01, 1000,1.0);
					SVDRecommender rec = new SVDRecommender(dataModel, factorizerPadrao);
					
					
					//float itemValorteste = rec.estimatePreference(49179, 1);					
					//SparseMatrix matrizR = new SparseMatrix(dataModel.getNumUsers()+1, dataModel.getNumItems()+1);
					SparseMatrix matrizR = new SparseMatrix(49291, 139739);
					double alfa = 0.8;
					
					System.gc();
					
					int contadorTotal = 0;
					for(LongPrimitiveIterator users = dataModel.getUserIDs(); users.hasNext();){
						int contador = 0;
						Long userID = users.nextLong();
						for(LongPrimitiveIterator itens = dataModel.getItemIDs(); itens.hasNext();){							
							Long itemID = itens.nextLong();							
							float itemValor = rec.estimatePreference(userID, itemID);
							matrizR.set(userID.intValue(), itemID.intValue(), itemValor);
							contador++;
							contadorTotal++;
							if(contador%1000 == 0){
								if(contadorTotal%2 == 0){
									System.gc();
								}
							}
							
						}
						System.gc();
					}
					
					//SparseMatrix matrizTrust = new SparseMatrix(dataModelTrust.getNumUsers()+1, dataModelTrust.getNumItems()+1);
					SparseMatrix matrizTrust = new SparseMatrix(49291, 49291);
					
					
					contadorTotal = 0;
					for(LongPrimitiveIterator users = dataModelTrust.getUserIDs(); users.hasNext();){
						int contador = 0;
						Long userLinhaID = users.nextLong();
						for(LongPrimitiveIterator usersColunas = dataModelTrust.getItemIDs(); usersColunas.hasNext();){							
							Long userColunasID = usersColunas.nextLong();
							Float trustValor = dataModelTrust.getPreferenceValue(userLinhaID, userColunasID);
							matrizTrust.set(userLinhaID.intValue(), userColunasID.intValue(), trustValor);
							if(contador%1000 == 0){
								if(contadorTotal%2 == 0){
									System.gc();
								}
							}
						}
						System.gc();
					}
					
					SparseMatrix matrizRFinal = new SparseMatrix(49291, 139739);
					
					for(int i = 1; i <= matrizR.numRows(); i++){
						for(int j =1; j <= matrizR.numCols(); j++){
							double valorIJ = alfa*matrizR.get(i, j) + (1-alfa)*matrizTrust.viewRow(i).dot(matrizR.viewColumn(j));
							matrizRFinal.set(i, j, valorIJ);
							System.gc();
						}
						System.gc();
					}
					
					
					
						
					/*RecommenderEvaluator evaluator=new RMSRecommenderEvaluator();
					
					 RecommenderBuilder builder = new RecommenderBuilder() {
						public DataModel dataModelTrust = new FileDataModel(new File("data/trust.cvs"));
						
						@Override
						public Recommender buildRecommender(DataModel dataModel) throws TasteException {
							// TODO Auto-generated method stub
							return new SVDRecommender(dataModel,new GavaFactorizer(dataModel, dataModelTrust,5, 0.0002 , 0.02,0.01, 100,1.0));
						}
					};
					
					double score=evaluator.evaluate(builder, null, dataModel, 0.7, 1.0);	
					
			        System.out.println("SVD score is "+score);
			        */
			        
			       /* RecommenderBuilder builder2 = new RecommenderBuilder() {
						
						@Override
						public Recommender buildRecommender(DataModel dataModel) throws TasteException {
							// TODO Auto-generated method stub
							return new SVDRecommender(dataModel,new RatingSGDFactorizer(dataModel,10, 0.0002 , 0.02,0.01, 100, 1.0));
						}
					};
					double score2=evaluator.evaluate(builder2, null, dataModel, 0.7, 1.0);
			        System.out.println("SVD score is "+score2);
			        
			        RecommenderBuilder builder3 = new RecommenderBuilder() {
						
						@Override
						public Recommender buildRecommender(DataModel dataModel) throws TasteException {
							// TODO Auto-generated method stub
							return new SVDRecommender(dataModel,new RatingSGDFactorizer(dataModel,20, 0.0002 , 0.02,0.01, 10, 1.0));
						}
					};
					double score3=evaluator.evaluate(builder3, null, dataModel, 0.7, 1.0);
			        System.out.println("SVD score is "+score3);
			        */
			        
			        
			        /*double valores[] = {score, score2, score3};
			        GeradorGrafico gerador = new GeradorGrafico(valores);
			        gerador.salvar(new FileOutputStream("saida.png"));*/
			        
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
