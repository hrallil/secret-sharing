
import time


class SecretSharing:
    def __init__(self):
        print("init secret sharing")

    def run(self):
        shares = self.create_shares(5, self.square)    
        print(shares)


    def create_polynomial(self, N, t):
        """
        Creates the polynomial that will be used to generate the shares and the secret
        """
        pass

    def create_shares(self, n, polynomial):
        """
        Function to generate shares 
        returns a list of touple (x, y) where x is the x coordinate and y is the y coordinate
        """
        return [(x,polynomial(x)) for x in range(n)]
    
    def square(self,x):
        """
        Simple 2 deg. polynomial to test
        """
        return x*x

    def combine_shares(self):
        pass

    def create_secret(self, polynomial):
        """
        Recives a polynomial and returns the secret which is the value of the polynomial at x=0
        """
        return polynomial(0)
