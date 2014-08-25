



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
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.RatingSGDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.math.SparseMatrix;
import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;


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


public class FatoracaoMatrixMahout {

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
	
	public static float calcularProdutoTrustComR(int linha, int coluna, DataModel dataModelTrust, SVDRecommender rec){
		long resultado = 0;
		long numeroUsuarios = 0;
		try {
			PreferenceArray userLinha = dataModelTrust.getPreferencesFromUser(linha);
			
			//userLinha.getIDs();
			//userLinha.getUserID(388);
			
			for(Iterator<Preference> pref = userLinha.iterator(); pref.hasNext();){
				Preference preference = pref.next();
				long id = preference.getItemID();
				
				//pref.next().getValue();
				//pref.next().getUserID();
				//pref.next().
				//float valorTrust = userLinha.get((int)userLinha.getUserID((int) id)).getValue();
				float valorTrust = preference.getValue(); 
				
				float produto = 0;
				try{
					produto = valorTrust*rec.estimatePreference(id, coluna);
					numeroUsuarios++;
				} catch (TasteException e1) {
					System.out.println("PROBLEMA NO METODO DE TRUST " + id + " " + coluna);
				}
				resultado += produto;
				
			}
			
			if(numeroUsuarios != 0){
				resultado = resultado/numeroUsuarios;
			}
			
		} catch (TasteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("NÃO ENCONTROU TRUST " + linha);
		}
		
		return resultado;
	}
	
	public static void main(String[] args) {
		try {
			
			/*BufferedReader br = new BufferedReader(new FileReader("data/ratings_data.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("data/ratings.cvs"));
			
			String line = "";
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				bw.write(values[0] + "," + values[1] + "," + values[2] + "\n");
			}
			
			br.close();
			bw.close();
			// criação do objeto dataModel a partir do arquivo cvs
			DataModel dataModel = new FileDataModel(new File("data/ratings.cvs"));
			
			br = new BufferedReader(new FileReader("data/trust_data.txt"));
			bw = new BufferedWriter(new FileWriter("data/trust.cvs"));
			
			line = "";
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				bw.write(values[1] + "," + values[2] + "," + values[3] + "\n");
			}
			
			br.close();
			bw.close();
			*/
			
			DataModel dataModel = new FileDataModel(new File("data/ratings_treinamento.cvs"));
			DataModel dataModelTrust = new FileDataModel(new File("data/trust.cvs"));
			double alfa = 0.8;
			
			
					
			try {
				
					
					RatingSGDFactorizer factorizerPadrao = new RatingSGDFactorizer(dataModel,5, 0.0002 , 0.02,0.01, 1000,1.0);
					SVDRecommender rec = new SVDRecommender(dataModel, factorizerPadrao);
					
					/*SparseMatrix matrizRFinal = new SparseMatrix(49291, 139739);
					System.gc();
					for(int i = 1; i < matrizRFinal.numRows(); i++){
						for(int j =1; j < matrizRFinal.numCols(); j++){
							double valorIJ = 0;
							try{
								valorIJ = alfa*rec.estimatePreference(i, j) + (1-alfa)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
							} catch (TasteException e1) {
							
							}
							matrizRFinal.set(i, j, valorIJ);							
						}
						if(i%500==0){
							System.gc();
						}
					}*/
					
					/*SimpleMatrix matrizDensa = new SimpleMatrix(49, 13);
					for(int i = 1; i < matrizDensa.numRows(); i++){
						for(int j =1; j < matrizDensa.numCols(); j++){
							double valorIJ = 0;
							try{
								valorIJ = alfa*rec.estimatePreference(i, j) + (1-alfa)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
							} catch (TasteException e1) {
							
							}
							matrizDensa.set(i, j, valorIJ);							
						}
						if(i%1000==0){
							System.gc();
						}
					}
					*/
					
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
							int i = Integer.parseInt(usuario);
							int j = Integer.parseInt(item);
							
							try{
								double notaPredita = alfa*rec.estimatePreference(i, j) + (1-alfa)*calcularProdutoTrustComR(i, j, dataModelTrust, rec);
								//double notaPredita = matrizRFinal.get(i, j);			
								erro += Math.pow((Double.valueOf(nota) - notaPredita),2);
								contadorPontos++;
							} catch (TasteException e1) {
								System.out.println("ERRO NO METODO DO MAHOUT " + i + " " + j);
							}
						}
						erro = Math.sqrt(erro/contadorPontos);
						System.out.println("Taxa de erro - RMSE: " + erro);
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

