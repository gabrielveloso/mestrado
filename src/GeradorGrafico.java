import java.io.IOException;
import java.io.OutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class GeradorGrafico {
	
	private DefaultCategoryDataset dados;
	private JFreeChart grafico;
	
	public GeradorGrafico(double[] valores) {
		
		DefaultCategoryDataset ds = new DefaultCategoryDataset();
		int i =0;
		for(double valor: valores){
			i++;
			ds.addValue(valor, "maximo",i+"");
		}
		
		this.grafico = ChartFactory.createLineChart("Meu Grafico", "Features", 
			    "Valor", ds, PlotOrientation.VERTICAL, true, true, false);
	}
	
	public void salvar(OutputStream out) throws IOException {
	     ChartUtilities.writeChartAsPNG(out, grafico, 500, 350);
	   }

}
