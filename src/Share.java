import java.math.BigInteger;

public class Share {
    private final BigInteger i;
    private final BigInteger share;
    public Share(BigInteger i, BigInteger share){
        this.i = i;
        this.share = share;

    }

    public BigInteger getI() {
        return i;
    }

    public BigInteger getShare() {
        return share;
    }
}
