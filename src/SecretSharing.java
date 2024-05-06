import java.math.*;
import java.security.SecureRandom;

public class SecretSharing {
    private BigInteger field;

    public SecretSharing(BigInteger field) {
        // Constructor
        this.field = field;
    }

    // Extracts the secret from the shares
    public BigInteger recoverSecret(Share[] shares) {
        int n = shares.length;
        BigInteger secretX = BigInteger.ZERO;
        BigInteger sum = BigInteger.ONE;

        for (int i = 0; i < n; i++) {   
            sum = sum.add(delta(n, shares, i, secretX).multiply(shares[i].getShare())).mod(this.field);
        }

        return sum;
    }
    
    private BigInteger delta(int k, Share[] shares, int i, BigInteger x){
        // Lagrange interpolation delta function. Will create the i-th delta function from the x values in the given shares.  
        BigInteger numerator = BigInteger.ONE;
        BigInteger denominator = BigInteger.ONE;

        for (int j = 0; j < k; j++) {
            if (i != j) {
                numerator = numerator.multiply(x.subtract(shares[i].getI())).mod(this.field);
                denominator = denominator.multiply(shares[i].getI().subtract(shares[j].getI())).mod(this.field);
            }
        }

        return numerator.divide(denominator);
    }
    

    public Share[] getShares(BigInteger secret, int N, int k){
        // N - number of shares created
        // k - number of shares reequired to recover the secret
        // secret - The secret to be shared
        Share[] shares = new Share[N];

        // Generate the secret
        SecureRandom r = new SecureRandom(); 

        BigInteger[] coefficients = new BigInteger[k];
        
        coefficients[0] = secret;
        for (int i = 1; i < coefficients.length; i++) {
            coefficients[i] = new BigInteger(256, r);
        }

        for (int i = 0; i < N; i++) {
            int x = i + 1;
            shares[i] = new Share(BigInteger.valueOf(x), poly(coefficients, BigInteger.valueOf(x)));
        }

        return shares;
    }

    private BigInteger poly(BigInteger[] coefficients, BigInteger x) {
        // Creates a polynomial from the given coefficients and calculates the value of the polynomial at x
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < coefficients.length; i++) {
            result = result.add(coefficients[i].multiply(x.pow(i)));
        }

        return result.mod(this.field);
    }
}
