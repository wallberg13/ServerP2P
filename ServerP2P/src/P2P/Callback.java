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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
        cli.getLog().add("Usuario " + user.getNome() + " entrou.");
    }

    @Override
    public void rmUserAvailable(Usuario user) throws RemoteException {
        Usuario rem = null;
        for(Usuario us: cli.getUsers()){
            if(us.getNome().equals(user.getNome())){
                rem = us;
                break;
            }
        }
        if(rem != null){
            cli.getUsers().remove(rem);
            cli.getLog().add("Usuario " + rem.getNome() + " saiu");
        }else{
            cli.getLog().add("Usuario não encontrado!!");
        }
    }
    
    /** 
     * Deixa em branco por enquanto
     * @param u
     * @return 
     */
    @Override
    public byte[] uploadFile(Usuario u, String file) throws RemoteException {
        try {
            byte[] ret;
            Path files = FileSystems.getDefault().getPath(u.getDirUp(), file);
            ret = Files.readAllBytes(files);
            return ret;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    
}
