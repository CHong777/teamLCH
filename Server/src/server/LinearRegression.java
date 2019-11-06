package server;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class LinearRegression {
	private RealMatrix w, estimate;

	public LinearRegression(double[][] xArray, double[][] yArray) throws Exception {
		applyNormalEquation(MatrixUtils.createRealMatrix(xArray), MatrixUtils.createRealMatrix(yArray));
	}

	private void applyNormalEquation(RealMatrix x, RealMatrix y) throws Exception {
		LUDecomposition lUDecomposition = new LUDecomposition(x.transpose().multiply(x));
		if (lUDecomposition.getDeterminant() == 0.0) throw new Exception("singular matrix w/ no inverse");
		else {w = lUDecomposition.getSolver().getInverse().multiply((x.transpose().multiply(y)));}
		estimate = x.multiply(w);
	}
	
	public double estimateRent(String entry) {
		return MatrixUtils.createColumnRealMatrix(new double[] {1, Double.valueOf(entry)}).transpose().multiply(w).getData()[0][0];
	}
	
	public RealMatrix getW() {return w;}
	public RealMatrix getEstimate() {return estimate;}

}
