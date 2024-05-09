import java.math.*;
import java.security.SecureRandom;


public class SecretSharing {
    private BigInteger field;

    public SecretSharing(BigInteger field) {
        this.field = field;
    }

    // Recover the secret from the shares
    public BigInteger recoverSecret(Share[] shares, int x) {
        // shares: (x, share)
        // x: The x value to recover the secret for
        int n = shares.length;
        BigInteger secretX = BigInteger.valueOf(x);
        BigInteger[] nums = new BigInteger[n];
        BigInteger[] dens = new BigInteger[n];

        // Calculate the delta numerator and denominator
        for (int i = 0; i < n; i++) {
            nums[i] = delta_num(n, shares, i, secretX);
            dens[i] = delta_den(n, shares, i);
        }

        // Product the denominators
        BigInteger common_den = BigInteger.ONE;
        for (int i = 0; i < n; i++) {
            common_den = common_den.multiply(dens[i]);
        }

        // Calculate the common numerator
        BigInteger common_num = BigInteger.ZERO;
        for (int i = 0; i < n; i++) {
             // Multiply the numerator by the share i
            BigInteger num_with_shares = nums[i].multiply(shares[i].getShare());

            // Each i numerator is multiplied by the common denominator and divided by the i denominator
            BigInteger num_with_common_den =
                gcd_div(num_with_shares.multiply(common_den).mod(this.field), dens[i], this.field);

            common_num = common_num.add(num_with_common_den);
        }

        // Find the inverse of the common denominator and multiply it with the common numerator, and return the result
        return gcd_div(common_num, common_den, this.field).add(this.field).mod(this.field);
    }
    
    // Calculate the delta numerator
    private BigInteger delta_num(int k, Share[] shares, int i, BigInteger x){
        // k: number of shares
        // shares: (x, share)
        // i: The index of the share
        BigInteger numerator = BigInteger.ONE;
        for (int j = 0; j < k; j++) {
            if (i != j) {
                numerator = numerator.multiply(x.subtract(shares[j].getX()));
            }
        }
        return numerator;
    }

    // Calculate the delta denominator
    private BigInteger delta_den(int k, Share[] shares, int i){
        // k: number of shares
        // shares: (x, share)
        // i: The index of the share
        BigInteger denominator = BigInteger.ONE;

        for (int j = 0; j < k; j++) {
            if (i != j) {
                denominator = denominator.multiply(shares[i].getX().subtract(shares[j].getX()));
            }
        }

        return denominator;
    }

    public Share[] getShares(BigInteger secret, int N, int k){
        // N - number of shares created
        // k - number of shares reequired to recover the secret
        // secret - The secret to be shared
        Share[] shares = new Share[N];

        SecureRandom r = new SecureRandom(); 

        BigInteger[] coefficients = new BigInteger[k];

        // Generate polynomial coefficients from the PRNG
        coefficients[0] = secret;
        for (int i = 1; i < coefficients.length; i++) {
            coefficients[i] = new BigInteger(256, r); // 256
        }

        // Find N shares 
        for (int i = 0; i < N; i++) {
            int x = i + 1;
            shares[i] = new Share(BigInteger.valueOf(x), poly(coefficients, BigInteger.valueOf(x)));
        }

        // Print the shares
        System.out.println("Creating a shares from secret: " + secret);
        for (int i = 0; i < coefficients.length; i++) {
            System.out.println("Share " + shares[i].getX() + ": " + shares[i].getShare());
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
    
    // Extended Euclidean algorithm to find the multiplicative inverse
    private BigInteger gcd_div(BigInteger a, BigInteger b, BigInteger field) {
        BigInteger vals = b.modInverse(field);
        return a.multiply(vals);
    }
}