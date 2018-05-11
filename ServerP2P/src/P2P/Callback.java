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
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author sir-berg
 */
public class Callback extends UnicastRemoteObject implements ICallback{
    
    Cliente cli;
    
    public Callback(Cliente cli) throws RemoteException{
        super();
        this.cli = cli;
    }
    
    @Override
    public void addUserAvailable(Usuario user) throws RemoteException {
        cli.getUsers().add(user);
    }

    @Override
    public void rmUserAvailable(Usuario user) throws RemoteException {
        Usuario rem = null;
        for(Usuario us: cli.getUsers()){
            if(us.equals(user)){
                rem = us;
                break;
            }
        }
        if(rem != null){
            System.out.println(rem.getNome() + " saiu!!");
            cli.getUsers().remove(rem);
        }else{
            System.out.println("Usuario não encontrado!!");
        }
    }
    
    /**
     * Deixa em branco por enquanto
     * @param file
     * @throws RemoteException 
     */
    @Override
    public void uploadFile(String file) throws RemoteException {
        
    }
    
    /** 
     * Deixa em branco por enquanto
     */
    @Override
    public void downloadFile(String nome, String file) throws RemoteException {
        
    }
    
}
