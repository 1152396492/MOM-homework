import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendLayout;
import org.knowm.xchart.style.Styler.LegendPosition;

public class RealTimeChart {
	private SwingWrapper<XYChart> swingWrapper ; 
	private XYChart chart ; 
	private JFrame frame ; 
	
	private String title ; 
	private String seriesName ; 
	private List<Double> seriesData ; 
	private int size = 1000 ; 
	
	public int getSize ( ) {
		return size ; 
	}
	
	public String getSeriesName ( ) {
		return seriesName ; 
	}
	
	public void setSeeriesName ( String seriesName ) {
		this.seriesName = seriesName ; 
	}
	
	public String getTitle ( ) {
		return title ; 
	}
	
	public void setTitle ( String title ) {
		this.title = title ; 
	}
	
	public RealTimeChart ( String title , String seriesName ) {
		super ( ) ; 
		this.seriesName = seriesName ; 
		this.title = title ; 
	}
	
	public RealTimeChart ( String title , String seriesName , int size ) {
		super ( ) ; 
		this.seriesName = seriesName ; 
		this.title = title ;
		this.size = size ; 
	}
	
	public void plot ( double data ) {
		if ( seriesData == null ) {
			seriesData = new LinkedList<>() ; 
		}
		if ( seriesData.size( ) == this.size ) {
			seriesData.clear ( ) ; 
		}
		seriesData.add(data) ; 
		
		if ( swingWrapper == null ) {
			chart = new XYChartBuilder().width(600).height(450).theme(ChartTheme.Matlab).title(title).build();
			chart.addSeries(seriesName, null, seriesData);
			chart.getStyler().setLegendPosition(LegendPosition.OutsideS);// 设置legend的位置为外底部
			chart.getStyler().setLegendLayout(LegendLayout.Horizontal);// 设置legend的排列方式为水平排列
 
			swingWrapper = new SwingWrapper<XYChart>(chart);
			frame = swingWrapper.displayChart();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 防止关闭窗口时退出
		}
		else { 
			chart.updateXYSeries ( seriesName , null , seriesData , null ) ; 
			swingWrapper.repaintChart();
		}
	}
}
