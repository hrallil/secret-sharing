import java.math.BigInteger;

public class Share {
    // class to contain a point on the polynomial
    private final BigInteger x;
    
    // share = poly(x)
    private final BigInteger share;

    public Share(BigInteger x, BigInteger share){
        this.x = x;
        this.share = share;

    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getShare() {
        return share;
    }
}
