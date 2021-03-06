/*
MIT License

Copyright (c) 2018 Wall Berg Morais, Jefferson Góes

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package P2P;

import java.rmi.RemoteException;
import java.rmi.registry.*;

/**
 *
 * @author jefferson
 */
public class Servidor {
    
    public Servidor(Integer porta) {
        try {
            Napster nap = new Napster();
            // Caso tiver um servidor de registros
            // Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099)
            //Criando servidor de registros;
            Registry registro = LocateRegistry.createRegistry(porta);
            registro.rebind("MeuNap", nap);
            System.out.println("Servidor de registros criado");
        } catch (RemoteException e) {
            System.err.println("Erro na conexão: " + e.getMessage());
        }
    }
 
    public static void main(String[] args) {
        Servidor s = new Servidor(4321);
    }
    
}


