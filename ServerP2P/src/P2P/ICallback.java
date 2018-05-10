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

import java.rmi.*;
import java.util.ArrayList;

/**
 *
 * @author sir-berg
 */
public interface ICallback extends Remote {
    
    /** 
     * Método invocado pelo servidor para quando tiver um novo
     * cliente disponivel, os demais clientes serem informados.
     * 
     * Será passado como parametro um Usuario, neste usuario tem como informações
     * o seu nome, a pasta dos arquivos disponveis e a pasta que os arquivos serão salvos.
     * @param files 
     */
    public void addFilesAvailable(Usuario user) throws RemoteException;
    
    /**
     * Método invocado pelo servidor para quando um cliente fechar a sua aplicação, 
     * os demais clientes serem informados.
     * @param files 
     */
    public void rmFilesAvailable(Usuario user) throws RemoteException;
    
    /** 
     * Método que tem como função mandar um arquivo para outro usuario pela rede.
     * 
     * @param nome
     * @param file
     * @throws java.rmi.RemoteException
     */
    public void uploadFile(String nome, String file) throws RemoteException;
    
    /** 
     * Método que tem como função solicitar de outro usuario um arquivo que deseja 
     * guardar na sua máquina.
     * 
     * @param nome
     * @param file
     * @throws java.rmi.RemoteException
     */
    public void downloadFile(String nome, String file) throws RemoteException;
}
